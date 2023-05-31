package app.ui.fragments;

import static app.constants.PokemonConstants.NUMBER_OF_STATS;
import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;
import static app.constants.PokemonDatabaseFields.PUBLIC_POKEMON_COLLECTION;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentPokemonDetailsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.connections.async_tasks.GetOtherPokemonDataAT;
import app.connections.firebase.DeletePokemonDB;
import app.connections.firebase.GetOtherPokemonDataDB;
import app.connections.firebase.GetPokemonSpriteDB;
import app.data_objects.Move;
import app.data_objects.Pokemon;
import app.stats_calculators.IStatsCalculator;
import app.stats_calculators.StatsCalculator;
import app.storages.Storage;
import app.ui.adapters.MoveItemAdapterForDetails;
import app.ui.dialogs.DeleteDialog;
import app.ui.dialogs.DownloadDialog;
import app.ui.dialogs.QRCodeDialog;
import app.ui.dialogs.UploadDialog;

public class PokemonDetails extends GeneralisedFragment<FragmentPokemonDetailsBinding> {

    private Pokemon pokemon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pokemon = Storage.getCopyOfSelectedPokemon();
        if (pokemon == null) {
            toast(getString(R.string.error_selecting_pokemon));
            navigateTo(R.id.action_details_to_collection);
            return;
        }

        if (Storage.isPublicPokemon()) {
            binding.fpdEditB.setVisibility(View.GONE);
            binding.fpdUploadB.setVisibility(View.GONE);
            binding.fpdDeleteB.setVisibility(View.GONE);
        } else {
            binding.fpdDownloadB.setVisibility(View.GONE);
        }

        disableActivityTouchInput();
        new GetOtherPokemonDataDB(this, pokemon.getID(), Storage.isPublicPokemon() ? PUBLIC_POKEMON_COLLECTION : POKEMON_COLLECTION).execute();

        int ability_weight = 5, stats_weight = 20, moves_weight = 32;
        int space_weight = 100 - ability_weight - stats_weight - moves_weight;

        LinearLayout space = binding.fPDSpace;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
        params.weight = space_weight;
        space.setLayoutParams(params);

        binding.fPDAbility.setVisibility(View.GONE);
        binding.fPDStats.setVisibility(View.GONE);
        binding.fPDMoves.setVisibility(View.GONE);
        setHideClickListener(binding.fPDAbility, binding.fPDHideAbility, ability_weight);
        setHideClickListener(binding.fPDStats, binding.fPDHideStats, stats_weight);
        setHideClickListener(binding.fPDMoves, binding.fPDHideMoves, moves_weight);

        binding.fpdEditB.setOnClickListener(v -> navigateTo(R.id.action_details_to_add));
        binding.fpdQrCodeB.setOnClickListener(v -> new QRCodeDialog(this, pokemon).load());
        binding.fpdDeleteB.setOnClickListener(v -> new DeleteDialog(this, pokemon.getID()).load());
        binding.fpdUploadB.setOnClickListener(v -> new UploadDialog(this, pokemon).load());
        binding.fpdDownloadB.setOnClickListener(v -> new DownloadDialog(this, pokemon, getAuthenticatedUserId()).load());
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setPageInfo() {
        binding.fPDName.setText(pokemon.getName());
        binding.fPDImage.setImageBitmap(pokemon.getImage());

        String text = pokemon.getSpecies() + " " + pokemon.getGender() + " Lv. " + pokemon.getLevel();
        binding.fPDSpecies.setText(text);
        binding.fPDNature.setText(pokemon.getNature().getName());

        binding.fPDType1.setText(pokemon.getTypes().get(0).getName().toUpperCase());
        binding.fPDType1.setBackgroundResource(pokemon.getTypes().get(0).getColor());
        if (pokemon.getTypes().size() == 1) {
            binding.fPDType2.setVisibility(View.GONE);
        } else {
            binding.fPDType2.setText(pokemon.getTypes().get(1).getName().toUpperCase());
            binding.fPDType2.setBackgroundResource(pokemon.getTypes().get(1).getColor());
        }

        binding.fPDAbilityName.setText(pokemon.getAbility().getName());
        binding.fPDAbilityDescription.setText(pokemon.getAbility().getDescription());

        List<List<TextView>> statsViews = Arrays.asList(
                Arrays.asList(binding.fPDBaseHp, binding.fPDBaseAttack, binding.fPDBaseDefense, binding.fPDBaseSpecialAttack, binding.fPDBaseSpecialDefense, binding.fPDBaseSpeed),
                Arrays.asList(binding.fPDIvsHp, binding.fPDIvsAttack, binding.fPDIvsDefense, binding.fPDIvsSpecialAttack, binding.fPDIvsSpecialDefense, binding.fPDIvsSpeed),
                Arrays.asList(binding.fPDEvsHp, binding.fPDEvsAttack, binding.fPDEvsDefense, binding.fPDEvsSpecialAttack, binding.fPDEvsSpecialDefense, binding.fPDEvsSpeed),
                Arrays.asList(binding.fPDTotalHp, binding.fPDTotalAttack, binding.fPDTotalDefense, binding.fPDTotalSpecialAttack, binding.fPDTotalSpecialDefense, binding.fPDTotalSpeed)
        );

        IStatsCalculator calculator = new StatsCalculator();
        List<Long> totalStats = calculator.calculateStats(
                pokemon.getBaseStats(),
                pokemon.getIVs(),
                pokemon.getEVs(),
                pokemon.getLevel(),
                pokemon.getNature());

        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            statsViews.get(0).get(i).setText(String.valueOf(pokemon.getBaseStats().get(i)));
            statsViews.get(1).get(i).setText(String.valueOf(pokemon.getIVs().get(i)));
            statsViews.get(2).get(i).setText(String.valueOf(pokemon.getEVs().get(i)));
            statsViews.get(3).get(i).setText(String.valueOf(totalStats.get(i)));
        }

        List<Move> moves = new ArrayList<>();
        pokemon.getMoves().forEach(move -> {
            if (move != null) moves.add(move);
        });

        MoveItemAdapterForDetails moveItemsAdapter = new MoveItemAdapterForDetails(this.getContext(), moves);
        binding.fPDMoves.setAdapter(moveItemsAdapter);
    }

    private void setHideClickListener(View view, View hideView, int weight) {
        hideView.setOnClickListener(v -> {
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
                addToLayoutWeightOfSpace(weight);
            } else {
                view.setVisibility(View.GONE);
                addToLayoutWeightOfSpace(-weight);
            }
        });
    }

    private void addToLayoutWeightOfSpace(int value) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fPDSpace.getLayoutParams();
        params.weight = params.weight + value;
        binding.fPDSpace.setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetOtherPokemonDataDB) {
            Pokemon pokemon = (Pokemon) result;
            this.pokemon.setLevel(pokemon.getLevel());
            this.pokemon.setAbility(pokemon.getAbility());
            this.pokemon.setNature(pokemon.getNature());
            this.pokemon.setIVs(pokemon.getIVs());
            this.pokemon.setEVs(pokemon.getEVs());
            this.pokemon.setMoves(pokemon.getMoves());
            new GetOtherPokemonDataAT(this, this.pokemon).execute();
        } else if (caller instanceof GetOtherPokemonDataAT) {
            new GetPokemonSpriteDB(this, (Pokemon) result).execute();
        } else if (caller instanceof GetPokemonSpriteDB) {
            pokemon = (Pokemon) result;
            Storage.setCopyOfSelectedPokemon(pokemon);
            setPageInfo();
            enableActivityTouchInput();
        } else if (caller instanceof DeletePokemonDB) {
            Storage.removeByIdFromPokemonList((String) result);
            navigateTo(R.id.action_details_to_collection);
        } else if (caller instanceof UploadDialog) {
            toast(getString(R.string.pokemon_uploaded));
        } else if (caller instanceof DownloadDialog){
            Storage.addToPokemonList(pokemon);
            toast(getString(R.string.pokemon_downloaded));
        }
    }

    @Override
    public void timedOut(Object caller) {
        toast(getString(R.string.server_timeout));
        if (caller instanceof GetOtherPokemonDataDB || caller instanceof GetOtherPokemonDataAT) {
            enableActivityTouchInput();
            navigateTo(R.id.action_details_to_collection);
        }
    }
}

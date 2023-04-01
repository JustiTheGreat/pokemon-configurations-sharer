package app.ui.fragments;

import static app.constants.Messages.ERROR_SELECTING_POKEMON;
import static app.constants.PokemonConstants.NUMBER_OF_STATS;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentPokemonDetailsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.Storage;
import app.data_objects.Move;
import app.data_objects.Pokemon;
import app.layout_adapters.MoveItemAdapterForDetails;
import app.stats_calculators.IStatsCalculator;
import app.stats_calculators.StatsCalculator;

public class PokemonDetails extends UtilityFragment {
    private Pokemon pokemon;
    private FragmentPokemonDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Storage.setPokemonDetailsFragment(this);
//        ((MainActivity) requireActivity()).setToolbarMenuVisible();

        if (Storage.getSelectedPokemonForDetails() == null) {
            toast(ERROR_SELECTING_POKEMON);
            navigateTo(R.id.action_details_to_collection);
            return;
        }
        pokemon = Storage.getSelectedPokemonForDetails();
        setPageInfo();

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

        //end of adapting layouts
        binding.fPDDelete.setOnClickListener(v -> deletePokemon());
        binding.fPDQrCode.setOnClickListener(v -> showQRCode());
        binding.fPDEdit.setOnClickListener(v -> editPokemon());
    }

    private void deletePokemon() {
        Dialog dialog = createDialog(R.layout.dialog_delete);
//        dialog.findViewById(R.id.d_delete_yes).setOnClickListener(v -> {
//            new DeleteTask().execute(this, pokemon);
//            dialog.dismiss();
//        });
        dialog.findViewById(R.id.d_delete_no).setOnClickListener(v -> dialog.dismiss());
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void showQRCode() {
        Dialog dialog = createDialog(R.layout.dialog_qr);
        int width = getDisplayWidthInPixels() / 2;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(pokemon.toStringOfTransmissibleData(), BarcodeFormat.QR_CODE, width, width);
            ImageView imageViewQrCode = dialog.findViewById(R.id.d_qr_image);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void editPokemon() {
        Storage.setSelectedPokemonForAdd(pokemon);
        navigateTo(R.id.action_details_to_add);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setPageInfo() {
        binding.fPDName.setText(pokemon.getName());
        binding.fPDImage.setImageBitmap(pokemon.getImage());

        String text = pokemon.getSpecies()
                + " " + pokemon.getGender()
                + " Lv. " + pokemon.getLevel();
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

    private void setHideClickListener(View view, View hideView, int weight){
        hideView.setOnClickListener(v->{
            if(view.getVisibility()==View.GONE){
                view.setVisibility(View.VISIBLE);
                addToLayoutWeightOfSpace(weight);
            } else {
                view.setVisibility(View.GONE);
                addToLayoutWeightOfSpace(-weight);
            }
        });
    }

    private void addToLayoutWeightOfSpace(int value){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fPDSpace.getLayoutParams();
        params.weight = params.weight + value;
        binding.fPDSpace.setLayoutParams(params);
    }
}
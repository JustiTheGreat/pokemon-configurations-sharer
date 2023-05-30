package app.ui.fragments;

import static app.constants.PokemonConstants.DEFAULT_POKEDEX_NUMBER;
import static app.data_objects.PokemonType.POKEMON_TYPES;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentPublicPokemonCollectionBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import app.connections.async_tasks.GetAllPokemonSpeciesDataAT;
import app.connections.async_tasks.GetPokemonListPartialDisplayDataAT;
import app.connections.firebase.GetFilteredPokemonDisplayDataListDB;
import app.connections.firebase.GetPokemonListPartialDisplayDataDB;
import app.connections.firebase.InsertPokemonDB;
import app.data_objects.Pokemon;
import app.data_objects.PokemonType;
import app.storages.Storage;
import app.ui.adapters.PokemonConfigurationAdapter;
import app.ui.dialogs.FilterDialog;

public class PublicPokemonCollection extends GeneralisedFragment<FragmentPublicPokemonCollectionBinding> {

    private FilterDialog filterDialog;
    private GetPokemonListPartialDisplayDataAT currentAsyncTask;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPublicPokemonCollectionBinding.inflate(inflater, container, false);

        binding.fppcFilterFAB.setOnClickListener(view -> {
            filterDialog = new FilterDialog(this);
            filterDialog.load();
        });

        binding.fppcListGV.setOnItemClickListener((adapterView, view, i, l) -> {
            Pokemon pokemon = (Pokemon) adapterView.getAdapter().getItem(i);
            Storage.setCopyOfSelectedPokemon(pokemon);
            navigateTo(R.id.action_publicCollection_to_details);
        });

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setPublicPokemon(true);
        if (Storage.getPokemonSpeciesList() == null) {
            new GetAllPokemonSpeciesDataAT(this).execute();
        }

        new GetFilteredPokemonDisplayDataListDB(this, DEFAULT_POKEDEX_NUMBER).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetFilteredPokemonDisplayDataListDB) {
            if (currentAsyncTask != null && currentAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                currentAsyncTask.cancel(true);
            }
            currentAsyncTask = new GetPokemonListPartialDisplayDataAT(this, (List<Pokemon>) result);
            currentAsyncTask.execute();
        } else if (caller instanceof GetPokemonListPartialDisplayDataAT) {
            List<Pokemon> pokemonList = (List<Pokemon>) result;

            if (filterDialog != null) {
                pokemonList = pokemonList.stream().filter(pokemon -> {
                    List<PokemonType> pokemonTypes = new ArrayList<>();
                    if (!filterDialog.getSelectedPokemonType1().equals(POKEMON_TYPES.get(0))) {
                        pokemonTypes.add(filterDialog.getSelectedPokemonType1());
                    }
                    if (!filterDialog.getSelectedPokemonType2().equals(POKEMON_TYPES.get(0))) {
                        pokemonTypes.add(filterDialog.getSelectedPokemonType2());
                    }

                    return pokemon.getName().contains(filterDialog.getNamePattern())
                            && new HashSet<>(pokemon.getTypes()).containsAll(pokemonTypes);
                }).collect(Collectors.toList());
            }

            binding.fppcListGV.setAdapter(new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList)));
            binding.fppcListGV.setVisibility(View.VISIBLE);
            binding.fppcLoadingPB.setVisibility(View.GONE);
        } else if (caller instanceof GetAllPokemonSpeciesDataAT) {
            Storage.setPokemonSpeciesList((List<Pokemon>) result);
            if (filterDialog != null) {
                filterDialog.notifyPokemonSpeciesDataIsAvailable();
            }
        } else if (caller instanceof FilterDialog) {
            binding.fppcListGV.setVisibility(View.GONE);
            binding.fppcLoadingPB.setVisibility(View.VISIBLE);
        } else if (caller instanceof InsertPokemonDB){
            Storage.addToPokemonList((Pokemon) result);
        }
    }

    @Override
    public void timedOut(Object caller) {
        toast(getString(R.string.server_timeout));
        if (caller instanceof GetPokemonListPartialDisplayDataDB || caller instanceof GetPokemonListPartialDisplayDataAT) {
            binding.fppcLoadingPB.setVisibility(View.GONE);
        }
    }
}

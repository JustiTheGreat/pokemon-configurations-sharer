package app.ui.fragments;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.connections.async_tasks.GetPokemonListPartialDisplayDataAT;
import app.connections.firebase.GetPokemonListPartialDisplayDataDB;
import app.connections.firebase.InsertPokemonDB;
import app.data_objects.Pokemon;
import app.storages.Storage;
import app.ui.activities.MainActivity;
import app.ui.adapters.PokemonConfigurationAdapter;
import app.ui.dialogs.AddOptionsDialog;

@RequiresApi(api = Build.VERSION_CODES.R)
public class PokemonCollection extends GeneralisedFragment<FragmentCollectionBinding> {
    private List<Pokemon> pokemonList;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result != null) {
                    if (result.getContents() == null) {
                        toast(getString(R.string.scan_canceled));
                    } else {
                        binding.fcListGV.setEnabled(false);
                        binding.fcAddOptionsFAB.setEnabled(false);
                        toast(getString(R.string.scan_successful));
                        Pokemon pokemon = Pokemon.fromStringOfTransmissibleData(result.getContents());
                        String userId = getAuthenticatedUserId();
                        pokemon.setUserId(userId);
                        Storage.setCopyOfSelectedPokemon(pokemon);
                        new InsertPokemonDB(this, pokemon, POKEMON_COLLECTION).execute();
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);

        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding.fcListGV.setOnItemClickListener((adapterView, view, i, l) -> {
            Storage.setCopyOfSelectedPokemon(pokemonList.get((int) l));
            navigateTo(R.id.action_collection_to_details);
        });

        binding.fcAddOptionsFAB.setOnClickListener(view -> new AddOptionsDialog(this, barcodeLauncher).load());
        binding.fcPublicCollectionFAB.setOnClickListener(view -> navigateTo(R.id.action_collection_to_publicCollection));

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setCopyOfSelectedPokemon(null);
        Storage.setPublicPokemon(false);
        if (Storage.getPokemonList() != null) {
            pokemonList = Storage.getPokemonList();
            loadGridView();
        } else {
            new GetPokemonListPartialDisplayDataDB(this, getAuthenticatedUserId()).execute();
        }
    }

    private void loadGridView() {
        binding.fcListGV.setAdapter(new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList)));
        binding.fcLoadingPB.setVisibility(View.GONE);
    }

    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonListPartialDisplayDataDB) {
//            ((List<Pokemon>) result).forEach(pokemon -> new GetPokemonHomeArtDB(this, pokemon).execute());
            List<Pokemon> pokemonList= (List<Pokemon>) result;
            new GetPokemonListPartialDisplayDataAT(this, (List<Pokemon>) result).execute();
        } else if (caller instanceof GetPokemonListPartialDisplayDataAT) {
            pokemonList = (List<Pokemon>) result;
            Storage.setPokemonList(pokemonList);
            loadGridView();
        } else if (caller instanceof InsertPokemonDB) {
            pokemonList.add((Pokemon) result);
        }
    }

    @Override
    public void timedOut(Object caller) {
        toast(getString(R.string.server_timeout));
        if (caller instanceof GetPokemonListPartialDisplayDataDB || caller instanceof GetPokemonListPartialDisplayDataAT) {
            binding.fcLoadingPB.setVisibility(View.GONE);
        }
    }
}

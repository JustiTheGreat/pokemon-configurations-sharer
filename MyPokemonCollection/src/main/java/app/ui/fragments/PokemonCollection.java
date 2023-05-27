package app.ui.fragments;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

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

import app.connections.async_tasks.GetPokemonDisplayDataListAT;
import app.data_objects.Pokemon;
import app.connections.firebase.GetPokemonDisplayDataListDB;
import app.connections.firebase.InsertPokemonDB;
import app.ui.layout_adapters.PokemonConfigurationAdapter;
import app.storages.Storage;
import app.ui.activities.MainActivity;
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
                        binding.fCGridview.setEnabled(false);
                        binding.fCAddOptionsButton.setEnabled(false);
                        toast(getString(R.string.scan_successful));
                        Pokemon pokemon = Pokemon.fromStringOfTransmissibleData(result.getContents());
                        String userId = getAuthenticatedUserId();
                        pokemon.setUserId(userId);
                        Storage.setCopyOfSelectedPokemon(pokemon);
                        new InsertPokemonDB(this, pokemon).execute();
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);

        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding.fCGridview.setOnItemClickListener(this::seePokemonDetails);
        binding.fCAddOptionsButton.setOnClickListener(view -> new AddOptionsDialog(this, barcodeLauncher).load());
        return binding.getRoot();
    }
    
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setCopyOfSelectedPokemon(null);
        if (Storage.getPokemonList() != null) {
            pokemonList = Storage.getPokemonList();
            loadGridView();
        } else {
            new GetPokemonDisplayDataListDB(this, getAuthenticatedUserId(), POKEMON_COLLECTION).execute();
        }
    }

    private void seePokemonDetails(AdapterView<?> adapterView, View view, int position, long id) {
        Storage.setCopyOfSelectedPokemon(pokemonList.get((int) id));
        navigateTo(R.id.action_collection_to_details);
    }

    private void loadGridView() {
        BaseAdapter baseAdapter = new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList));
        binding.fCGridview.setAdapter(baseAdapter);
        binding.fCProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonDisplayDataListDB) {
            new GetPokemonDisplayDataListAT(this, (List<Pokemon>) result).execute();
        } else if (caller instanceof GetPokemonDisplayDataListAT) {
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
        binding.fCProgressbar.setVisibility(View.GONE);
    }
}

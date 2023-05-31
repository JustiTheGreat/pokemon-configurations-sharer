package app.ui.fragments;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentPokemonCollectionBinding;

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
public class PokemonCollection extends GeneralisedFragment<FragmentPokemonCollectionBinding> {

    private List<Pokemon> pokemonList;
    private int count = 2;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result != null) {
                    if (result.getContents() == null) {
                        toast(getString(R.string.scan_canceled));
                    } else {
                        binding.fpcListGV.setEnabled(false);
                        binding.fpcAddOptionsFAB.setEnabled(false);
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
        binding = FragmentPokemonCollectionBinding.inflate(inflater, container, false);

        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        binding.fpcListGV.setOnItemClickListener((adapterView, view, i, l) -> {
            Storage.setCopyOfSelectedPokemon((Pokemon) adapterView.getAdapter().getItem(i));
            navigateTo(R.id.action_collection_to_details);
        });

        binding.fpcListGV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != RecyclerView.SCROLL_STATE_IDLE
                        || view.getCount() == 0
                        || !GetPokemonListPartialDisplayDataDB.canRead()) {
                    return;
                }

                View lastChildView = view.getChildAt(view.getChildCount() - 1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom = view.getBottom() - view.getPaddingBottom();
                int lastPosition = view.getPositionForView(lastChildView);

                if (lastChildBottom == recyclerBottom && lastPosition == view.getCount() - 1) {
                    binding.fpcLoadingPB.setVisibility(View.VISIBLE);
                    new GetPokemonListPartialDisplayDataDB(PokemonCollection.this, getAuthenticatedUserId(), ++count).execute();
                }
            }
        });

        binding.fpcAddOptionsFAB.setOnClickListener(view -> new AddOptionsDialog(this, barcodeLauncher).load());
        binding.fpcPublicCollectionFAB.setOnClickListener(view -> navigateTo(R.id.action_collection_to_publicCollection));

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setCopyOfSelectedPokemon(null);
        Storage.setPublicPokemon(false);
        if (Storage.getPokemonList() != null) {
            pokemonList = Storage.getPokemonList();
            binding.fpcListGV.setAdapter(new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList)));
            binding.fpcLoadingPB.setVisibility(View.GONE);
        } else {
            new GetPokemonListPartialDisplayDataDB(this, getAuthenticatedUserId(), ++count).execute();
        }
    }

    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonListPartialDisplayDataDB) {
            new GetPokemonListPartialDisplayDataAT(this, (List<Pokemon>) result).execute();
        } else if (caller instanceof GetPokemonListPartialDisplayDataAT) {
            if (pokemonList == null) {
                pokemonList = (List<Pokemon>) result;
                Storage.setPokemonList(pokemonList);
            } else {
                pokemonList.addAll((List<Pokemon>) result);
            }

            binding.fpcListGV.setAdapter(new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList)));
            binding.fpcLoadingPB.setVisibility(View.GONE);
        } else if (caller instanceof InsertPokemonDB) {
            pokemonList.add((Pokemon) result);
        }
    }

    @Override
    public void timedOut(Object caller) {
        toast(getString(R.string.server_timeout));
        if (caller instanceof GetPokemonListPartialDisplayDataDB || caller instanceof GetPokemonListPartialDisplayDataAT) {
            binding.fpcLoadingPB.setVisibility(View.GONE);
        }
    }
}

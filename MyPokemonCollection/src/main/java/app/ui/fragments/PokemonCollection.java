package app.ui.fragments;

import static app.constants.Messages.SCAN_A_POKEMON_QR_CODE;
import static app.constants.Messages.SCAN_CANCELED;
import static app.constants.Messages.SCAN_SUCCESSFUL;
import static app.constants.Messages.SERVER_TIMEOUT;
import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import app.firebase.GetPokemonDisplayDataListDB;
import app.firebase.InsertPokemonDB;
import app.async_tasks.GetPokemonDisplayDataListAT;
import app.data_objects.Pokemon;
import app.layout_adapters.PokemonConfigurationAdapter;
import app.storages.Storage;
import app.ui.activities.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.R)
public class PokemonCollection extends UtilityFragment {
    private FragmentCollectionBinding binding;
    private List<Pokemon> pokemonList;
    private GetPokemonDisplayDataListAT getPokemonDisplayDataListTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);

        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        //todo
        setMenuVisibility(true);

//        setHasOptionsMenu(true);
//        ((MainActivity) requireActivity()).getSupportActionBar().

//                getMenu().setGroupVisible(0, true);;

//        binding.fCProgressbar.setVisibility(View.INVISIBLE);
        binding.fCGridview.setOnItemClickListener(this::seePokemonDetails);
        binding.fCAddOptionsButton.setOnClickListener(this::addOptions);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Storage.setCurrentFragment(this);
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

    private void addOptions(View view) {
        Dialog dialog = createDialog(R.layout.dialog_add_options);
        Window window = dialog.getWindow();
        window.setDimAmount(0);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.x = binding.fCAddOptionsButton.getWidth() / 2 + 100;
        layoutParams.y = 30;
        window.setAttributes(layoutParams);
        dialog.findViewById(R.id.dao_write).setOnClickListener(view1 -> addPokemonManually(dialog));
        dialog.findViewById(R.id.dao_scan).setOnClickListener(view1 -> {
            scanQRCode();
            dialog.dismiss();
        });
    }

    private void addPokemonManually(Dialog dialog) {
        navigateTo(R.id.action_collection_to_add);
        dialog.dismiss();
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result != null) {
                    if (result.getContents() == null) {
                        toast(SCAN_CANCELED);
                    } else {
                        binding.fCGridview.setEnabled(false);
                        binding.fCAddOptionsButton.setEnabled(false);
                        toast(SCAN_SUCCESSFUL);
                        Pokemon pokemon = Pokemon.fromStringOfTransmissibleData(result.getContents());
                        String userId = getAuthenticatedUserId();
                        pokemon.setUserId(userId);
                        Storage.setCopyOfSelectedPokemon(pokemon);
                        new InsertPokemonDB(this, pokemon).execute();
                    }
                }
            });

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setOrientationLocked(false);
        options.setPrompt(SCAN_A_POKEMON_QR_CODE);
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
    }

    private void loadGridView() {
        BaseAdapter baseAdapter = new PokemonConfigurationAdapter(this.getActivity(), new ArrayList<>(pokemonList));
        binding.fCGridview.setAdapter(baseAdapter);
        binding.fCProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (getPokemonDisplayDataListTask != null && getPokemonDisplayDataListTask.getStatus() != AsyncTask.Status.FINISHED){
            getPokemonDisplayDataListTask.cancel(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonDisplayDataListDB) {
            getPokemonDisplayDataListTask = new GetPokemonDisplayDataListAT(this);
            getPokemonDisplayDataListTask.execute((List<Pokemon>) result);
        } else if (caller instanceof GetPokemonDisplayDataListAT) {
            pokemonList = (List<Pokemon>) result;
            Storage.setPokemonList(pokemonList);
            loadGridView();
        } else if (caller instanceof InsertPokemonDB) {
            pokemonList.add((Pokemon)result);
        }
    }

    @Override
    public void timedOut(Object caller) {
        toast(SERVER_TIMEOUT);
        binding.fCProgressbar.setVisibility(View.GONE);
    }
}

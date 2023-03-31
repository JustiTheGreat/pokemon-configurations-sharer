package app.ui.fragments;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.async_tasks.database.GetPokemonList;
import app.async_tasks.database.ICallbackContext;
import app.data_objects.GridViewCell;
import app.data_objects.Pokemon;
import app.layout_adapters.PokemonConfigurationAdapter;
import app.ui.activities.MainActivity;

public class PokemonCollection extends Fragment implements ICallbackContext {
    private FragmentCollectionBinding binding;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private GetPokemonList getPokemonListTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("StaticFieldLeak")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Storage.setPokemonCollectionFragment(this);
        //Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        //((MainActivity) requireActivity()).setToolbarMenuVisible();

        getPokemonListTask = new GetPokemonList(this);
        getPokemonListTask.execute();

        binding.fCGridview.setOnItemClickListener(this::seePokemonDetails);
        binding.fCAddOptionsButton.setOnClickListener(this::addOptions);
    }

    private void seePokemonDetails(AdapterView<?> adapterView, View view, int position, long id) {
//        Storage.setSelectedPokemonForDetails(getPokemon(id));
//        NavHostFragment
//                .findNavController(PokemonCollection.this)
//                .navigate(R.id.action_collection_to_details);
    }

    private void addOptions(View view) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_add_options);
        Window window = dialog.getWindow();
        window.setDimAmount(0);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.x = binding.fCAddOptionsButton.getWidth() / 2 + 100;
        layoutParams.y = 30;

        window.setAttributes(layoutParams);
        dialog.show();
        //dialog.findViewById(R.id.dao_write).setOnClickListener(view1 -> addPokemonManually(dialog));
        //dialog.findViewById(R.id.dao_scan).setOnClickListener(view1 -> scanQRCode(dialog));
    }

    private void addPokemonManually(Dialog dialog) {
//        NavHostFragment
//                .findNavController(PokemonCollection.this)
//                .navigate(R.id.action_collection_to_add);
//        dialog.dismiss();
    }

//    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//            result -> {
//                if (result != null) {
//                    if (result.getContents() == null) {
//                        Toast.makeText(this.getActivity(), "Scan cancelled!", Toast.LENGTH_LONG).show();
//                    } else {
//                        binding.fCGridview.setEnabled(false);
//                        binding.fCAddOptionsButton.setEnabled(false);
//                        Toast.makeText(this.getActivity(), "Scan successful!", Toast.LENGTH_LONG).show();
//                        new CreatePokemon().execute(this, result.getContents());
//                    }
//                }
//            });

//    private void scanQRCode(Dialog dialog) {
//        ScanOptions options = new ScanOptions();
//        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
//        options.setOrientationLocked(false);
//        options.setPrompt("Scan a QR code");
//        options.setBeepEnabled(false);
//        barcodeLauncher.launch(options);
//        dialog.dismiss();
//    }

    public void afterANewPokemonWasCreatedInScan() {
        NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_collection_to_add);
    }

//    private Pokemon getPokemon(long id) {
//        for (Pokemon pokemon : pokemonList) {
//            if (pokemon.getID() == id)
//                return pokemon;
//        }
//        return null;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (getPokemonListTask.getStatus() != AsyncTask.Status.FINISHED) {
            getPokemonListTask.cancel(true);
        }
    }

    @Override
    public void callback(Object caller, Object result) {
        if (caller instanceof GetPokemonList) {
            pokemonList = (List<Pokemon>) result;
            List<GridViewCell> gridViewCells = new ArrayList<>();
            for (Pokemon pokemon : pokemonList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    gridViewCells.add(new GridViewCell(
                            pokemon.getID(),
                            pokemon.getOfficialArt(),
                            pokemon.getName(),
                            pokemon.getSpecies(),
                            pokemon.getTypes()
                    ));
                }
            }
            BaseAdapter baseAdapter = new PokemonConfigurationAdapter(this.getActivity(), gridViewCells);
            binding.fCGridview.setAdapter(baseAdapter);
            binding.fCProgressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void timedOut() {
        Toast.makeText(this.getActivity(), "SERVER TIMEOUT", Toast.LENGTH_LONG).show();
    }
}

package com.example.testapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import com.example.testapp.R;
import com.example.testapp.Storage;
import com.example.testapp.async_tasks.CreatePokemon;
import com.example.testapp.async_tasks.GetPokemonList;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.databinding.FragmentCollectionBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class PokemonCollection extends Fragment {
    private FragmentCollectionBinding binding;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();
    private GetPokemonList getPokemonListTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("StaticFieldLeak")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPokemonListTask = new GetPokemonList();
        getPokemonListTask.execute(this);

        binding.fCGridview.setOnItemClickListener(this::seePokemonDetails);
        binding.fCAddOptionsButton.setOnClickListener(this::addOptions);
    }

    private void seePokemonDetails(AdapterView<?> adapterView, View view, int position, long id) {
        Storage.setSelectedPokemon(getPokemon(id));
        NavHostFragment
                .findNavController(PokemonCollection.this)
                .navigate(R.id.action_pokemonCollection_to_pokemonDetails);
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
        dialog.findViewById(R.id.dao_write).setOnClickListener(view1 -> addPokemonManually(dialog));
        dialog.findViewById(R.id.dao_scan).setOnClickListener(view1 -> scanQRCode(dialog));
    }

    private void addPokemonManually(Dialog dialog) {
        NavHostFragment
                .findNavController(PokemonCollection.this)
                .navigate(R.id.action_pokemonCollection_to_addPokemon);
        dialog.dismiss();
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this.getActivity(), "Scan cancelled!", Toast.LENGTH_LONG).show();
                    } else {
                        binding.fCGridview.setEnabled(false);
                        binding.fCAddOptionsButton.setEnabled(false);
                        Toast.makeText(this.getActivity(), "Scan successful!", Toast.LENGTH_LONG).show();
                        new CreatePokemon().execute(this, result.getContents());
                    }
                }
            });

    public void afterANewPokemonWasCreatedInScan() {
        NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_pokemonCollection_to_addPokemon);
    }

    private void scanQRCode(Dialog dialog) {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setOrientationLocked(false);
        options.setPrompt("Scan a QR code");
        options.setBeepEnabled(false);
        barcodeLauncher.launch(options);
        dialog.dismiss();
    }

    private Pokemon getPokemon(long id) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getID() == id)
                return pokemon;
        }
        return null;
    }

    public void setPokemonList(ArrayList<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    public void setCollectionGridViewAdapter(BaseAdapter gridViewAdapter) {
        binding.fCGridview.setAdapter(gridViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (getPokemonListTask.getStatus() != AsyncTask.Status.FINISHED) {
            getPokemonListTask.cancel(true);
        }
    }
}

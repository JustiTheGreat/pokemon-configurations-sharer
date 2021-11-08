package com.example.testapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.communication.GetAbilities;
import com.example.testapp.communication.GetSpecies;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.SpeciesRow;
import com.example.testapp.databinding.AddPokemonBinding;
import com.example.testapp.layout_adapters.AbilitiesAdapter;
import com.example.testapp.layout_adapters.SpeciesAdapter;

import java.util.ArrayList;

public class AddPokemon extends Fragment implements PokemonConstants {
    private AddPokemonBinding binding;
    private ArrayList<SpeciesRow> speciesRows;
    private ArrayList<Ability> abilitiesRows;
    private SpeciesRow speciesRow;
    private Ability abilityRow;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddPokemonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addpokemonChosenspecies.setVisibility(View.GONE);
        binding.addpokemonChosenability.setVisibility(View.GONE);
        binding.addpokemonChosenstats.setVisibility(View.GONE);
        binding.addpokemonChosenmoves.setVisibility(View.GONE);

        binding.addpokemonSpeciesbutton.setEnabled(false);
        binding.addpokemonAbilitiesbutton.setEnabled(false);
        binding.addpokemonStatsbutton.setEnabled(false);
        binding.addpokemonMovesbutton.setEnabled(false);

        binding.addpokemonSpeciesbutton.setOnClickListener(v -> speciesDialog());
        new GetSpecies().execute(this);
        binding.addpokemonAbilitiesbutton.setOnClickListener(v -> abilitiesDialog());
        binding.addpokemonStatsbutton.setOnClickListener(v -> statsDialog());
    }

    public void setSpeciesRows(ArrayList<SpeciesRow> speciesRows) {
        this.speciesRows = speciesRows;
        binding.addpokemonSpeciesbutton.setEnabled(true);
    }

    public void setAbilitiesRows(ArrayList<Ability> abilitiesRows) {
        this.abilitiesRows = abilitiesRows;
        binding.addpokemonAbilitiesbutton.setEnabled(true);
    }

    public void setStatsRows(ArrayList<> statsRows){

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    public void speciesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView species = dialog.findViewById(R.id.searchdialog_search);
        species.setAdapter(new SpeciesAdapter(this.getContext(), speciesRows));
        species.setThreshold(1);
        species.setOnTouchListener((View v, MotionEvent event) -> {
            species.showDropDown();
            return false;
        });
        species.setOnItemClickListener((parent, view, position, id) -> speciesRow = (SpeciesRow) species.getAdapter().getItem(position));
        Button button = dialog.findViewById(R.id.search_dialog_button);
        button.setOnClickListener(v -> setSpeciesInfo(dialog));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setSpeciesInfo(Dialog dialog) {
        if(speciesRow!=null) {
            new GetAbilities().execute(this, speciesRow.getSpecies());

            binding.addpokemonIcon.setImageBitmap(speciesRow.getIcon());
            binding.addpokemonSpecies.setText(speciesRow.getSpecies());
            binding.addpokemonGender.setText(MALE);
            binding.addpokemonGender.setOnClickListener((v) -> {
                if (binding.addpokemonGender.getText().equals(MALE))
                    binding.addpokemonGender.setText(FEMALE);
                else binding.addpokemonGender.setText(MALE);
            });
            TextView type1 = binding.addpokemonType1;
            TextView type2 = binding.addpokemonType2;
            ArrayList<TextView> types = new ArrayList<>();
            types.add(type1);
            if (speciesRow.getTypes().size() == 2) {
                type2.setVisibility(View.VISIBLE);
                types.add(type2);
            } else {
                type2.setVisibility(View.GONE);
            }
            types.forEach(t -> {
                t.setText(speciesRow.getTypes().get(types.indexOf(t)));
                TYPES.forEach(T -> {
                    if (T.getName().equalsIgnoreCase(speciesRow.getTypes().get(types.indexOf(t)))) {
                        t.setBackgroundResource(T.getColor());
                    }
                });
            });

            binding.addpokemonChosenspecies.setVisibility(View.VISIBLE);
            binding.addpokemonChosenability.setVisibility(View.GONE);
            binding.addpokemonChosenstats.setVisibility(View.GONE);
            binding.addpokemonChosenmoves.setVisibility(View.GONE);

            binding.addpokemonSpeciesbutton.setText(R.string.change_species);
            binding.addpokemonAbilitiesbutton.setText(R.string.choose_ability);
            binding.addpokemonStatsbutton.setText(R.string.choose_stats_related);
            binding.addpokemonMovesbutton.setText(R.string.choose_moves);

            binding.addpokemonStatsbutton.setEnabled(true);
            binding.addpokemonMovesbutton.setEnabled(true);
        }

        dialog.dismiss();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void abilitiesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView abilities = dialog.findViewById(R.id.searchdialog_search);
        abilities.setAdapter(new AbilitiesAdapter(this.getContext(), abilitiesRows));
        abilities.setThreshold(1);
        abilities.setOnTouchListener((View v, MotionEvent event) -> {
            abilities.showDropDown();
            return false;
        });
        abilities.setOnItemClickListener((parent, view, position, id) ->
                abilityRow = (Ability) abilities.getAdapter().getItem(position));
        Button button = dialog.findViewById(R.id.search_dialog_button);
        button.setOnClickListener(v -> setAbilityInfo(dialog));
    }

    public void setAbilityInfo(Dialog dialog) {
        binding.addpokemonAbilityname.setText(abilityRow.getName());
        binding.addpokemonAbilitydescription.setText(abilityRow.getDescription());

        binding.addpokemonChosenability.setVisibility(View.VISIBLE);
        binding.addpokemonAbilitiesbutton.setText(R.string.change_ability);
        dialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

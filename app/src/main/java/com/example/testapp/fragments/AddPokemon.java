package com.example.testapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.data_objects.NATURE;
import com.example.testapp.async_tasks.GetPokemonMoves;
import com.example.testapp.async_tasks.Helper;
import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.async_tasks.GetAbilities;
import com.example.testapp.async_tasks.GetSpecies;
import com.example.testapp.async_tasks.GetStats;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.Move;
import com.example.testapp.data_objects.SpeciesRow;
import com.example.testapp.databinding.AddPokemonBinding;
import com.example.testapp.layout_adapters.AbilitiesAdapter;
import com.example.testapp.layout_adapters.SpeciesAdapter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddPokemon extends Fragment implements PokemonConstants {
    private AddPokemonBinding binding;
    private ArrayList<SpeciesRow> speciesRows;
    private ArrayList<Ability> abilitiesRows;
    private SpeciesRow speciesRow;
    private Ability abilityRow;
    private ArrayList<Integer> baseStats;
    //last values of the editable elements
    private int level = 1;
    private NATURE nature = NATURE_BASHFUL;
    private final ArrayList<Integer> ivs = new ArrayList<Integer>() {{
        add(0);
        add(0);
        add(0);
        add(0);
        add(0);
        add(0);
    }};
    private final ArrayList<Integer> evs = new ArrayList<Integer>() {{
        add(0);
        add(0);
        add(0);
        add(0);
        add(0);
        add(0);
    }};
    //editable elements
    private EditText levelET;
    private Spinner natureSpinner;
    private ArrayList<EditText> ivsET;
    private ArrayList<EditText> evsET;
    private ArrayList<TextView> totalTV;
    private ArrayList<Move> moves;

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
        binding.addpokemonMovesbutton.setOnClickListener(v -> movesDialog());
    }

    public void setSpeciesRows(ArrayList<SpeciesRow> speciesRows) {
        this.speciesRows = speciesRows;
        binding.addpokemonSpeciesbutton.setEnabled(true);
    }

    public void setAbilitiesRows(ArrayList<Ability> abilitiesRows) {
        this.abilitiesRows = abilitiesRows;
        binding.addpokemonAbilitiesbutton.setEnabled(true);
    }

    public void setStatsRows(ArrayList<Integer> baseStats) {
        this.baseStats = baseStats;
        binding.addpokemonStatsbutton.setEnabled(true);
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
        binding.addpokemonMovesbutton.setEnabled(true);
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

        AutoCompleteTextView species = dialog.findViewById(R.id.d_search_search);
        species.setAdapter(new SpeciesAdapter(this.getContext(), speciesRows));
        species.setThreshold(1);
        if (speciesRow != null) {
            species.setText(speciesRow.getSpecies());
        }
        species.setOnTouchListener((View v, MotionEvent event) -> {
            species.showDropDown();
            return false;
        });
        species.setOnItemClickListener((parent, view, position, id) -> speciesRow = (SpeciesRow) species.getAdapter().getItem(position));
        dialog.findViewById(R.id.d_search_button).setOnClickListener(v -> setSpeciesInfo(dialog));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setSpeciesInfo(Dialog dialog) {
        if (speciesRow != null) {
            new GetAbilities().execute(this, speciesRow.getSpecies());
            new GetStats().execute(this, speciesRow.getSpecies());
            new GetPokemonMoves().execute(this,speciesRow.getSpecies());

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
                t.setText(speciesRow.getTypes().get(types.indexOf(t)).getName());
                t.setBackgroundResource(speciesRow.getTypes().get(types.indexOf(t)).getColor());
            });

            binding.addpokemonChosenspecies.setVisibility(View.VISIBLE);
            binding.addpokemonChosenability.setVisibility(View.GONE);
            binding.addpokemonChosenstats.setVisibility(View.GONE);
            binding.addpokemonChosenmoves.setVisibility(View.GONE);

            binding.addpokemonSpeciesbutton.setText(R.string.change_species);
            binding.addpokemonAbilitiesbutton.setText(R.string.choose_ability);
            binding.addpokemonStatsbutton.setText(R.string.choose_stats_related);
            binding.addpokemonMovesbutton.setText(R.string.choose_moves);
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

        AutoCompleteTextView abilities = dialog.findViewById(R.id.d_search_search);
        abilities.setAdapter(new AbilitiesAdapter(this.getContext(), abilitiesRows));
        abilities.setThreshold(1);
        if (abilityRow != null) {
            abilities.setText(abilityRow.getName());
        }
        abilities.setOnTouchListener((View v, MotionEvent event) -> {
            abilities.showDropDown();
            return false;
        });
        abilities.setOnItemClickListener((parent, view, position, id) ->
                abilityRow = (Ability) abilities.getAdapter().getItem(position));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> setAbilityInfo(dialog));
    }

    public void setAbilityInfo(Dialog dialog) {
        binding.addpokemonAbilityname.setText(abilityRow.getName());
        binding.addpokemonAbilitydescription.setText(abilityRow.getDescription());

        binding.addpokemonChosenability.setVisibility(View.VISIBLE);
        binding.addpokemonAbilitiesbutton.setText(R.string.change_ability);
        dialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void statsDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_stats);
        dialog.show();

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        levelET = dialog.findViewById(R.id.d_stats_level);
        levelET.setText(""+level);

        natureSpinner = dialog.findViewById(R.id.d_stats_nature);
        natureSpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, NATURES.stream().map(NATURE::getName).collect(Collectors.toList())));
        NATURE N = NATURE.getNature(nature.getName());
        natureSpinner.setSelection(NATURES.indexOf(N));

        ivsET = new ArrayList<EditText>() {{
            add(dialog.findViewById(R.id.d_stats_ivhp));
            add(dialog.findViewById(R.id.d_stats_ivattack));
            add(dialog.findViewById(R.id.d_stats_ivdefense));
            add(dialog.findViewById(R.id.d_stats_ivspecialattack));
            add(dialog.findViewById(R.id.d_stats_ivspecialdefense));
            add(dialog.findViewById(R.id.d_stats_ivspeed));
        }};
        evsET = new ArrayList<EditText>() {{
            add(dialog.findViewById(R.id.d_stats_evhp));
            add(dialog.findViewById(R.id.d_stats_evattack));
            add(dialog.findViewById(R.id.d_stats_evdefense));
            add(dialog.findViewById(R.id.d_stats_evspecialattack));
            add(dialog.findViewById(R.id.d_stats_evspecialdefense));
            add(dialog.findViewById(R.id.d_stats_evspeed));
        }};
        totalTV = new ArrayList<TextView>() {{
            add(dialog.findViewById(R.id.d_stats_tothp));
            add(dialog.findViewById(R.id.d_stats_totattack));
            add(dialog.findViewById(R.id.d_stats_totdefense));
            add(dialog.findViewById(R.id.d_stats_totspecialattack));
            add(dialog.findViewById(R.id.d_stats_totspecialdefense));
            add(dialog.findViewById(R.id.d_stats_totspeed));
        }};
        ivsET.forEach(ivET -> ivET.setText(""+ivs.get(ivsET.indexOf(ivET))));
        evsET.forEach(evET -> evET.setText(""+evs.get(evsET.indexOf(evET))));
        setTotalStatsTVs();

        //addOnTextChangedListener(levelET);

//        natureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                setTotalStatsTVs();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        //ivsET.forEach(this::addOnTextChangedListener);
        //evsET.forEach(this::addOnTextChangedListener);

        Button button = dialog.findViewById(R.id.d_stats_button);
        button.setOnClickListener(v -> setStatsInfo(dialog));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setStatsInfo(Dialog dialog) {
        level = Integer.parseInt(levelET.getText().toString());
        binding.addpokemonLevel.setText(""+level);
        binding.addpokemonNature.setText(NATURE.getNature(natureSpinner.getSelectedItem().toString()).getName());
        ivs.forEach(iv-> ivs.set(ivs.indexOf(iv),Integer.parseInt(ivsET.get(ivs.indexOf(iv)).getText().toString())));
        evs.forEach(ev-> evs.set(evs.indexOf(ev),Integer.parseInt(evsET.get(evs.indexOf(ev)).getText().toString())));
        binding.addpokemonHp.setText(totalTV.get(0).getText());
        binding.addpokemonAttack.setText(totalTV.get(1).getText());
        binding.addpokemonDefense.setText(totalTV.get(2).getText());
        binding.addpokemonSpecialattack.setText(totalTV.get(3).getText());
        binding.addpokemonSpecialdefense.setText(totalTV.get(4).getText());
        binding.addpokemonSpeed.setText(totalTV.get(5).getText());

        binding.addpokemonChosenstats.setVisibility(View.VISIBLE);
        binding.addpokemonStatsbutton.setText(R.string.change_stats_related);
        dialog.dismiss();
    }

    public void movesDialog() {
//        Dialog dialog = new Dialog(this.getActivity());
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setContentView(R.layout.dialog_search);
//        dialog.show();
//
//        Button button = dialog.findViewById(R.id.d_search_button);
//        button.setOnClickListener(v -> setAbilityInfo(dialog));
    }

    public void addOnTextChangedListener(Object object){
        if(object instanceof EditText){
            ((EditText) object).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @SuppressLint("ResourceAsColor")
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String correctValue = s.toString();
                    if (!correctValue.equals("")) {
                        if (correctValue.startsWith("0") && correctValue.length() != 1) {
                            correctValue = correctValue.replace("0", "");
                        }
                        int limit = (object.equals(levelET) ? 100 :
                                (ivsET.contains(object) ? 31 :
                                        (evsET.contains(object) ? 252 : 0))
                        );
                        if (Integer.parseInt(correctValue) > limit) {
                            correctValue = "" + limit;
                        }
                        ((EditText) object).setText(correctValue);
                    }
                }
                @SuppressLint("ResourceAsColor")
                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals(""))
                        setTotalStatsTVs();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setTotalStatsTVs() {
        int level = Integer.parseInt(levelET.getText().toString());
        NATURE nature = NATURE.getNature(natureSpinner.getSelectedItem().toString());
        ArrayList<Integer> ivs = (ArrayList<Integer>)ivsET.stream().map(ivET->Integer.parseInt(ivET.getText().toString())).collect(Collectors.toList());
        ArrayList<Integer> evs = (ArrayList<Integer>)evsET.stream().map(evET->Integer.parseInt(evET.getText().toString())).collect(Collectors.toList());
        assert nature != null;
        ArrayList<Integer> totalStats = Helper.calculateStats(baseStats, ivs, evs, level, nature.getEffects());
        totalTV.forEach(totTV -> totTV.setText(""+totalStats.get(totalTV.indexOf(totTV))));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

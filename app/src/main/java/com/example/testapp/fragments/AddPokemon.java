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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.SelectedPokemon;
import com.example.testapp.async_tasks.GetAbilities;
import com.example.testapp.async_tasks.GetPokemonMoves;
import com.example.testapp.async_tasks.GetAllSpecies;
import com.example.testapp.async_tasks.GetBaseStats;
import com.example.testapp.async_tasks.Helper;
import com.example.testapp.async_tasks.InsertTask;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.Move;
import com.example.testapp.data_objects.Nature;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.data_objects.SpeciesRow;
import com.example.testapp.databinding.AddPokemonBinding;
import com.example.testapp.layout_adapters.AbilityAdapter;
import com.example.testapp.layout_adapters.MoveAdapter;
import com.example.testapp.layout_adapters.SpeciesAdapter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddPokemon extends Fragment implements PokemonConstants {
    private AddPokemonBinding binding;
    private final int species_header_height = 6, species_weight = 8, ability_header_weight = 6, ability_weight = 5,
            stats_header_weight = 6, stats_weight = 20, moves_header_weight = 3, move_weight = 10, finalize_weight = 6;
    private final int space_weight = 100 - species_weight - ability_weight - stats_weight - moves_header_weight - 3 * move_weight;
    private int current_space_weight = space_weight;
    private ArrayList<SpeciesRow> speciesRows;
    private ArrayList<Ability> abilitiesRows;
    private ArrayList<Move> movesRows;
    private Pokemon pokemon = new Pokemon(
            -1,
            null,
            null,
            MALE,
            1,
            null,
            NATURE_BASHFUL,
            new ArrayList<Integer>() {{
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
            }},
            new ArrayList<Integer>() {{
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
                add(0);
            }},
            new ArrayList<>(),
            null,
            null,
            null,
            null
    );

    //editable elements
    private AutoCompleteTextView speciesTV;
    private ArrayList<TextView> typesTV;
    private EditText levelET;
    private Spinner natureSpinner;
    private final ArrayList<EditText> ivsET = new ArrayList<>();
    private final ArrayList<EditText> evsET = new ArrayList<>();
    private final ArrayList<TextView> totalTV = new ArrayList<>();
    private final ArrayList<LinearLayout> movesLL = new ArrayList<>();
    private final ArrayList<TextView> movesNamesTV = new ArrayList<>();
    private final ArrayList<TextView> movesTypesTV = new ArrayList<>();
    private final ArrayList<ImageView> movesCategoriesIV = new ArrayList<>();
    private final ArrayList<TextView> movesPowersTV = new ArrayList<>();
    private final ArrayList<TextView> movesAccuraciesTV = new ArrayList<>();
    private final ArrayList<TextView> movesPPsTV = new ArrayList<>();
    private final ArrayList<TextView> movesDescriptionsTV = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddPokemonBinding.inflate(inflater, container, false);
        {
            movesLL.add(binding.addpokemonMoves1);
            movesLL.add(binding.addpokemonMoves2);
            movesLL.add(binding.addpokemonMoves3);
            movesLL.add(binding.addpokemonMoves4);

            movesNamesTV.add(binding.addpokemonMoves1Name);
            movesNamesTV.add(binding.addpokemonMoves2Name);
            movesNamesTV.add(binding.addpokemonMoves3Name);
            movesNamesTV.add(binding.addpokemonMoves4Name);

            movesTypesTV.add(binding.addpokemonMoves1Type);
            movesTypesTV.add(binding.addpokemonMoves2Type);
            movesTypesTV.add(binding.addpokemonMoves3Type);
            movesTypesTV.add(binding.addpokemonMoves4Type);

            movesCategoriesIV.add(binding.addpokemonMoves1Category);
            movesCategoriesIV.add(binding.addpokemonMoves2Category);
            movesCategoriesIV.add(binding.addpokemonMoves3Category);
            movesCategoriesIV.add(binding.addpokemonMoves4Category);

            movesPowersTV.add(binding.addpokemonMoves1Power);
            movesPowersTV.add(binding.addpokemonMoves2Power);
            movesPowersTV.add(binding.addpokemonMoves3Power);
            movesPowersTV.add(binding.addpokemonMoves4Power);

            movesAccuraciesTV.add(binding.addpokemonMoves1Accuracy);
            movesAccuraciesTV.add(binding.addpokemonMoves2Accuracy);
            movesAccuraciesTV.add(binding.addpokemonMoves3Accuracy);
            movesAccuraciesTV.add(binding.addpokemonMoves4Accuracy);

            movesPPsTV.add(binding.addpokemonMoves1Pp);
            movesPPsTV.add(binding.addpokemonMoves2Pp);
            movesPPsTV.add(binding.addpokemonMoves3Pp);
            movesPPsTV.add(binding.addpokemonMoves4Pp);

            movesDescriptionsTV.add(binding.addpokemonMoves1Description);
            movesDescriptionsTV.add(binding.addpokemonMoves2Description);
            movesDescriptionsTV.add(binding.addpokemonMoves3Description);
            movesDescriptionsTV.add(binding.addpokemonMoves4Description);
        }
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pokemon.getID() != -1) {
            pokemon = SelectedPokemon.getPokemon();
            initializePageWithData();
            binding.addpokemonSpeciesbutton.setText(R.string.empty);
            int space_weight = 100 - species_weight - ability_weight - stats_weight - moves_header_weight - 3 * move_weight;
            current_space_weight = space_weight + species_weight + ability_weight + stats_weight + moves_header_weight + 4 * move_weight;
            if (pokemon.getMoves().get(3) == null) {
                current_space_weight -= move_weight;
                movesLL.get(3).setVisibility(View.GONE);
                if (pokemon.getMoves().get(2) == null) {
                    current_space_weight -= move_weight;
                    movesLL.get(2).setVisibility(View.GONE);
                    if (pokemon.getMoves().get(1) == null) {
                        current_space_weight -= move_weight;
                        movesLL.get(1).setVisibility(View.GONE);
                    }
                }
            }
        } else {
            binding.addpokemonChosenspecies.setVisibility(View.GONE);
            binding.addpokemonChosenability.setVisibility(View.GONE);
            binding.addpokemonChosenstats.setVisibility(View.GONE);
            binding.addpokemonMovesHeader.setVisibility(View.GONE);
            movesLL.get(0).setVisibility(View.GONE);
            movesLL.get(1).setVisibility(View.GONE);
            movesLL.get(2).setVisibility(View.GONE);
            movesLL.get(3).setVisibility(View.GONE);
            binding.addpokemonAddmove.setVisibility(View.VISIBLE);

            binding.addpokemonFinalize.setEnabled(false);
            new GetAllSpecies().execute(this);
            binding.addpokemonSpeciesbutton.setOnClickListener(v -> speciesDialog());
            binding.addpokemonGender.setOnClickListener((v) -> {
                if (binding.addpokemonGender.getText().equals(MALE)) {
                    pokemon.setGender(FEMALE);
                    binding.addpokemonGender.setText(pokemon.getGender());
                } else {
                    pokemon.setGender(MALE);
                    binding.addpokemonGender.setText(pokemon.getGender());
                }
            });
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.addpokemonSpace.getLayoutParams();
        params.weight = current_space_weight;
        binding.addpokemonSpace.setLayoutParams(params);

        binding.addpokemonSpeciesbutton.setEnabled(false);
        binding.addpokemonAbilitiesbutton.setEnabled(false);
        binding.addpokemonStatsbutton.setEnabled(false);
        binding.addpokemonAddmove.setEnabled(false);

        binding.addpokemonAbilitiesbutton.setOnClickListener(v -> abilitiesDialog());
        binding.addpokemonStatsbutton.setOnClickListener(v -> statsDialog());
        binding.addpokemonAddmove.setOnClickListener(v -> movesDialog());
        binding.addpokemonFinalize.setOnClickListener(v -> nameDialog());
    }

    private void initializePageWithData(){
        new GetAbilities().execute(this, pokemon.getSpecies());
        new GetBaseStats().execute(this, pokemon.getSpecies());
        new GetPokemonMoves().execute(this, pokemon.getSpecies());

        //load species
        {
            binding.addpokemonIcon.setImageBitmap(pokemon.getSprite());
            binding.addpokemonSpecies.setText(pokemon.getSpecies());
            binding.addpokemonGender.setText(pokemon.getGender());
            typesTV = new ArrayList<TextView>() {{
                add(binding.addpokemonType1);
            }};
            TextView type2 = binding.addpokemonType2;
            if (pokemon.getTypes().size() == 2) {
                type2.setVisibility(View.VISIBLE);
                typesTV.add(type2);
            } else {
                type2.setVisibility(View.GONE);
            }
            typesTV.forEach(t -> {
                t.setText(pokemon.getTypes().get(typesTV.indexOf(t)).getName().toUpperCase());
                t.setBackgroundResource(pokemon.getTypes().get(typesTV.indexOf(t)).getColor());
            });
        }
        //load ability
        {
            binding.addpokemonAbilityname.setText(pokemon.getAbility().getName());
            binding.addpokemonAbilitydescription.setText(pokemon.getAbility().getDescription());
            binding.addpokemonAbilitiesbutton.setText(R.string.change_ability);
        }
        //load stats related
        {
            binding.addpokemonLevel.setText(String.valueOf(pokemon.getLevel()));
            binding.addpokemonNature.setText(pokemon.getNature().getName());
            ArrayList<Integer> totalStats = Helper.calculateStats(
                    pokemon.getBaseStats(),
                    pokemon.getIVs(),
                    pokemon.getEVs(),
                    pokemon.getLevel(),
                    pokemon.getNature()
            );
            binding.addpokemonHp.setText(String.valueOf(totalStats.get(0)));
            binding.addpokemonAttack.setText(String.valueOf(totalStats.get(1)));
            binding.addpokemonDefense.setText(String.valueOf(totalStats.get(2)));
            binding.addpokemonSpecialattack.setText(String.valueOf(totalStats.get(3)));
            binding.addpokemonSpecialdefense.setText(String.valueOf(totalStats.get(4)));
            binding.addpokemonSpeed.setText(String.valueOf(totalStats.get(5)));
            binding.addpokemonStatsbutton.setText(R.string.change_stats_related);
        }
        //load moves
    }

    private void seeIfFinalizingIsPossible() {
        if (binding.addpokemonChosenability.getVisibility() == View.VISIBLE
                && binding.addpokemonChosenstats.getVisibility() == View.VISIBLE
                && binding.addpokemonMoves1.getVisibility() == View.VISIBLE) {
            binding.addpokemonFinalize.setEnabled(true);
        }
    }

    private void nameDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_addname);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.d_addname_button).setOnClickListener(v -> {
            EditText name = dialog.findViewById(R.id.d_addname_name);
            if (name.getText().toString().trim().equals("")) {
                Toast.makeText(dialog.getContext(), "Empty name!", Toast.LENGTH_SHORT).show();
                return;
            }
            pokemon.setName(name.getText().toString().trim());
            int n = 4-pokemon.getMoves().size();
            for(int i=0;i<n;i++)
                pokemon.getMoves().add(null);
            if(pokemon.getID()!=-1){
                //new UpdateTask().execute(this,pokemon);
            }
            else {
                new InsertTask().execute(this, pokemon);
            }
            dialog.dismiss();
        });
    }

    public void setSpeciesRows(ArrayList<SpeciesRow> speciesRows) {
        this.speciesRows = speciesRows;
        binding.addpokemonSpeciesbutton.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void speciesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        speciesTV = dialog.findViewById(R.id.d_search_search);
        speciesTV.setAdapter(new SpeciesAdapter(this.getContext(), speciesRows));
        speciesTV.setThreshold(1);
        if (pokemon.getSpecies() != null) {
            speciesTV.setText(pokemon.getSpecies());
        }
        speciesTV.setOnTouchListener((View v, MotionEvent event) -> {
            speciesTV.showDropDown();
            return false;
        });
        AtomicReference<SpeciesRow> speciesRow = new AtomicReference<>();
        speciesTV.setOnItemClickListener((parent, view, position, id) -> speciesRow.set((SpeciesRow) speciesTV.getAdapter().getItem(position)));
        dialog.findViewById(R.id.d_search_button).setOnClickListener(v -> setSpeciesInfo(dialog, speciesRow.get()));
    }

    private void setSpeciesInfo(Dialog dialog, SpeciesRow speciesRow) {
        if (speciesRow != null) {
            pokemon.setSprite(speciesRow.getSprite());
            pokemon.setSpecies(speciesRow.getSpecies());
            pokemon.setTypes(speciesRow.getTypes());
            binding.addpokemonAbilitiesbutton.setEnabled(false);
            binding.addpokemonStatsbutton.setEnabled(false);
            binding.addpokemonAddmove.setEnabled(false);
            new GetAbilities().execute(this, pokemon.getSpecies());
            new GetBaseStats().execute(this, pokemon.getSpecies());
            new GetPokemonMoves().execute(this, pokemon.getSpecies());
            binding.addpokemonIcon.setImageBitmap(pokemon.getSprite());
            binding.addpokemonSpecies.setText(pokemon.getSpecies());
            binding.addpokemonGender.setText(pokemon.getGender());

            typesTV = new ArrayList<TextView>() {{
                add(binding.addpokemonType1);
            }};
            TextView type2 = binding.addpokemonType2;
            if (pokemon.getTypes().size() == 2) {
                type2.setVisibility(View.VISIBLE);
                typesTV.add(type2);
            } else {
                type2.setVisibility(View.GONE);
            }
            typesTV.forEach(t -> {
                t.setText(pokemon.getTypes().get(typesTV.indexOf(t)).getName().toUpperCase());
                t.setBackgroundResource(pokemon.getTypes().get(typesTV.indexOf(t)).getColor());
            });

            binding.addpokemonChosenspecies.setVisibility(View.VISIBLE);
            binding.addpokemonChosenability.setVisibility(View.GONE);
            binding.addpokemonChosenstats.setVisibility(View.GONE);
            binding.addpokemonMovesHeader.setVisibility(View.GONE);
            binding.addpokemonMoves1.setVisibility(View.GONE);
            binding.addpokemonMoves2.setVisibility(View.GONE);
            binding.addpokemonMoves3.setVisibility(View.GONE);
            binding.addpokemonMoves4.setVisibility(View.GONE);
            binding.addpokemonAddmove.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.addpokemonSpace.getLayoutParams();
            current_space_weight = space_weight + species_weight;
            params.weight = current_space_weight;
            binding.addpokemonSpace.setLayoutParams(params);

            binding.addpokemonSpeciesbutton.setText(R.string.change_species);
            binding.addpokemonAbilitiesbutton.setText(R.string.choose_ability);
            binding.addpokemonStatsbutton.setText(R.string.choose_stats_related);
            pokemon.setMoves(new ArrayList<>());

            binding.addpokemonFinalize.setEnabled(false);
        }
        dialog.dismiss();
    }

    public void setAbilitiesRows(ArrayList<Ability> abilitiesRows) {
        this.abilitiesRows = abilitiesRows;
        binding.addpokemonAbilitiesbutton.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void abilitiesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView abilities = dialog.findViewById(R.id.d_search_search);
        abilities.setAdapter(new AbilityAdapter(this.getContext(), abilitiesRows));
        abilities.setThreshold(1);
        if (pokemon.getAbility() != null) {
            abilities.setText(pokemon.getAbility().getName());
        }
        abilities.setOnTouchListener((View v, MotionEvent event) -> {
            abilities.showDropDown();
            return false;
        });
        AtomicReference<Ability> ability = new AtomicReference<>();
        abilities.setOnItemClickListener((parent, view, position, id) -> ability.set((Ability) abilities.getAdapter().getItem(position)));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> setAbilityInfo(dialog, ability.get()));
    }

    private void setAbilityInfo(Dialog dialog, Ability ability) {
        if (ability != null) {
            pokemon.setAbility(ability);
            binding.addpokemonAbilityname.setText(pokemon.getAbility().getName());
            binding.addpokemonAbilitydescription.setText(pokemon.getAbility().getDescription());
            binding.addpokemonAbilitiesbutton.setText(R.string.change_ability);
            if (binding.addpokemonChosenability.getVisibility() == View.GONE) {
                binding.addpokemonChosenability.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.addpokemonSpace.getLayoutParams();
                current_space_weight = current_space_weight + ability_weight;
                params.weight = current_space_weight;
                binding.addpokemonSpace.setLayoutParams(params);
            }
            seeIfFinalizingIsPossible();
        }
        dialog.dismiss();
    }

    public void setStatsRows(ArrayList<Integer> baseStats) {
        pokemon.setBaseStats(baseStats);
        binding.addpokemonStatsbutton.setEnabled(true);
    }


    private void statsDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_stats);
        dialog.show();

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        {
            levelET = null;
            natureSpinner = null;
            ivsET.clear();
            evsET.clear();
            totalTV.clear();

            levelET = dialog.findViewById(R.id.d_stats_level);

            natureSpinner = dialog.findViewById(R.id.d_stats_nature);

            ivsET.add(dialog.findViewById(R.id.d_stats_ivhp));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivattack));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivdefense));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspecialattack));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspecialdefense));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspeed));

            evsET.add(dialog.findViewById(R.id.d_stats_evhp));
            evsET.add(dialog.findViewById(R.id.d_stats_evattack));
            evsET.add(dialog.findViewById(R.id.d_stats_evdefense));
            evsET.add(dialog.findViewById(R.id.d_stats_evspecialattack));
            evsET.add(dialog.findViewById(R.id.d_stats_evspecialdefense));
            evsET.add(dialog.findViewById(R.id.d_stats_evspeed));

            totalTV.add(dialog.findViewById(R.id.d_stats_tothp));
            totalTV.add(dialog.findViewById(R.id.d_stats_totattack));
            totalTV.add(dialog.findViewById(R.id.d_stats_totdefense));
            totalTV.add(dialog.findViewById(R.id.d_stats_totspecialattack));
            totalTV.add(dialog.findViewById(R.id.d_stats_totspecialdefense));
            totalTV.add(dialog.findViewById(R.id.d_stats_totspeed));
        }

        levelET.setText(String.valueOf(pokemon.getLevel()));

        natureSpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, NATURES.stream().map(Nature::getName).collect(Collectors.toList())));
        Nature N = Nature.getNature(pokemon.getNature().getName());
        natureSpinner.setSelection(NATURES.indexOf(N));

        ivsET.forEach(ivET -> ivET.setText(String.valueOf(pokemon.getIVs().get(ivsET.indexOf(ivET)))));
        evsET.forEach(evET -> evET.setText(String.valueOf(pokemon.getEVs().get(evsET.indexOf(evET)))));
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

    private void setStatsInfo(Dialog dialog) {
        setTotalStatsTVs();
        pokemon.setLevel(Integer.parseInt(levelET.getText().toString()));
        binding.addpokemonLevel.setText(String.valueOf(pokemon.getLevel()));
        pokemon.setNature(Nature.getNature(natureSpinner.getSelectedItem().toString()));
        binding.addpokemonNature.setText(pokemon.getNature().getName());
        pokemon.getIVs().forEach(iv -> pokemon.getIVs().set(pokemon.getIVs().indexOf(iv), Integer.parseInt(ivsET.get(pokemon.getIVs().indexOf(iv)).getText().toString())));
        pokemon.getEVs().forEach(ev -> pokemon.getEVs().set(pokemon.getEVs().indexOf(ev), Integer.parseInt(evsET.get(pokemon.getEVs().indexOf(ev)).getText().toString())));
        binding.addpokemonHp.setText(totalTV.get(0).getText());
        binding.addpokemonAttack.setText(totalTV.get(1).getText());
        binding.addpokemonDefense.setText(totalTV.get(2).getText());
        binding.addpokemonSpecialattack.setText(totalTV.get(3).getText());
        binding.addpokemonSpecialdefense.setText(totalTV.get(4).getText());
        binding.addpokemonSpeed.setText(totalTV.get(5).getText());
        binding.addpokemonStatsbutton.setText(R.string.change_stats_related);

        if (binding.addpokemonChosenstats.getVisibility() == View.GONE) {
            binding.addpokemonChosenstats.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.addpokemonSpace.getLayoutParams();
            current_space_weight = current_space_weight + stats_weight;
            params.weight = current_space_weight;
            binding.addpokemonSpace.setLayoutParams(params);
        }
        seeIfFinalizingIsPossible();
        dialog.dismiss();
    }

    private void addOnTextChangedListener(Object object) {
        if (object instanceof EditText) {
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
                                        (evsET.contains(object) ? 252 : 0)
                                )
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
                    if (!s.toString().equals(""))
                        setTotalStatsTVs();
                }
            });
        }
    }

    private void setTotalStatsTVs() {
        int level = Integer.parseInt(levelET.getText().toString());
        Nature nature = Nature.getNature(natureSpinner.getSelectedItem().toString());
        ArrayList<Integer> ivs = (ArrayList<Integer>) ivsET.stream().map(ivET -> Integer.parseInt(ivET.getText().toString())).collect(Collectors.toList());
        ArrayList<Integer> evs = (ArrayList<Integer>) evsET.stream().map(evET -> Integer.parseInt(evET.getText().toString())).collect(Collectors.toList());
        assert nature != null;
        ArrayList<Integer> totalStats = Helper.calculateStats(pokemon.getBaseStats(), ivs, evs, level, nature);
        totalTV.forEach(totTV -> totTV.setText(String.valueOf(totalStats.get(totalTV.indexOf(totTV)))));
    }

    public void setMoves(ArrayList<Move> movesRows) {
        this.movesRows = movesRows;
        binding.addpokemonAddmove.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void movesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 90 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView movesTV = dialog.findViewById(R.id.d_search_search);
        movesTV.setAdapter(new MoveAdapter(this.getContext(), movesRows));
        movesTV.setThreshold(1);
        movesTV.setOnTouchListener((View v, MotionEvent event) -> {
            movesTV.showDropDown();
            return false;
        });
        AtomicReference<Move> move = new AtomicReference<>();
        movesTV.setOnItemClickListener((parent, view, position, id) -> move.set((Move) movesTV.getAdapter().getItem(position)));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> setMoveInfo(dialog, move.get()));
    }

    private void setMoveInfo(Dialog dialog, Move move) {
        if (move != null) {
            boolean moveAlreadyExists = false;
            for (Move m : pokemon.getMoves()) {
                if (move.getName().equals(m.getName())) {
                    Toast.makeText(this.getActivity(), "Move already added!", Toast.LENGTH_SHORT).show();
                    moveAlreadyExists = true;
                    break;
                }
            }
            if (!moveAlreadyExists) {
                int index = pokemon.getMoves().size();
                pokemon.getMoves().add(move);
                movesNamesTV.get(index).setText(move.getName());
                movesTypesTV.get(index).setText(move.getType().getName().toUpperCase());
                movesTypesTV.get(index).setBackgroundResource(move.getType().getColor());
                movesCategoriesIV.get(index).setImageBitmap(move.getCategory().getIcon());
                movesPowersTV.get(index).setText(String.valueOf(move.getPower()));
                movesAccuraciesTV.get(index).setText(String.valueOf(move.getAccuracy()));
                movesPPsTV.get(index).setText(String.valueOf(move.getPP()));
                movesDescriptionsTV.get(index).setText(move.getDescription());
                if (index == 3) {
                    binding.addpokemonAddmove.setVisibility(View.GONE);
                }
                if (movesLL.get(index).getVisibility() == View.GONE) {
                    if (index == 0) {
                        binding.addpokemonMovesHeader.setVisibility(View.VISIBLE);
                        current_space_weight += 3;
                    }
                    movesLL.get(index).setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.addpokemonSpace.getLayoutParams();
                    current_space_weight += move_weight;
                    params.weight = current_space_weight;
                    binding.addpokemonSpace.setLayoutParams(params);
                }
            }
        }
        seeIfFinalizingIsPossible();
        dialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

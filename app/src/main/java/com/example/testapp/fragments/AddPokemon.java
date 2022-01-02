package com.example.testapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testapp.PokemonConstants;
import com.example.testapp.R;
import com.example.testapp.Storage;
import com.example.testapp.async_tasks.GetAbilities;
import com.example.testapp.async_tasks.GetAllSpecies;
import com.example.testapp.async_tasks.GetBaseStats;
import com.example.testapp.async_tasks.GetPokemonMoves;
import com.example.testapp.async_tasks.database.InsertTask;
import com.example.testapp.async_tasks.database.UpdateTask;
import com.example.testapp.data_objects.Ability;
import com.example.testapp.data_objects.Move;
import com.example.testapp.data_objects.Nature;
import com.example.testapp.data_objects.Pokemon;
import com.example.testapp.data_objects.SpeciesRow;
import com.example.testapp.databinding.FragmentAddPokemonBinding;
import com.example.testapp.layout_adapters.AbilityAdapter;
import com.example.testapp.layout_adapters.MoveAdapter;
import com.example.testapp.layout_adapters.MoveItemAdapterForAddEdit;
import com.example.testapp.layout_adapters.SpeciesAdapter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.R)
public class AddPokemon extends Fragment implements PokemonConstants {
    private GetAllSpecies getAllSpeciesTask;
    private GetAbilities getAbilitiesTask;
    private GetBaseStats getBaseStatsTask;
    private GetPokemonMoves getPokemonMovesTask;
    private FragmentAddPokemonBinding binding;
    private MoveItemAdapterForAddEdit adapter;
    private ArrayList<SpeciesRow> allSpeciesData;
    private ArrayList<Ability> allAbilitiesData;
    private ArrayList<Move> allMovesData;
    private Pokemon pokemon = new Pokemon(
            -1,
            null,
            null,
            MALE,
            1,
            null,
            NATURE_SERIOUS,
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddPokemonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Storage.pokemonIsSelectedForAdd()) {
            pokemon = Storage.getSelectedPokemonForAdd();
            adapter = new MoveItemAdapterForAddEdit(getContext(), pokemon.getMoves());
            binding.fAPMoves.setAdapter(adapter);
            initializePageWithData();
            binding.fAPSpeciesButton.setText(R.string.empty);
        } else {
            getAllSpeciesTask = new GetAllSpecies();
            getAllSpeciesTask.execute(this);
            adapter = new MoveItemAdapterForAddEdit(getContext(), pokemon.getMoves());
            binding.fAPMoves.setAdapter(adapter);
            binding.fAPChosenSpecies.setVisibility(View.GONE);
            binding.fAPChosenAbility.setVisibility(View.GONE);
            binding.fAPChosenStats.setVisibility(View.GONE);
            binding.fAPMoves.setVisibility(View.GONE);
            binding.fAPAddMove.setVisibility(View.VISIBLE);

            binding.fAPFinalize.setEnabled(false);

            binding.fAPSpeciesButton.setOnClickListener(v -> speciesDialog());
        }

        recalculateWeights();

        binding.fAPSpeciesButton.setEnabled(false);
        binding.fAPAbilitiesButton.setEnabled(false);
        binding.fAPStatsButton.setEnabled(false);
        binding.fAPAddMove.setEnabled(false);

        binding.fAPGender.setOnClickListener(v -> changeGender());
        binding.fAPAbilitiesButton.setOnClickListener(v -> abilitiesDialog());
        binding.fAPStatsButton.setOnClickListener(v -> statsDialog());
        binding.fAPAddMove.setOnClickListener(v -> movesDialog());
        binding.fAPFinalize.setOnClickListener(v -> nameDialog());
    }

    private void recalculateWeights() {
        int species_weight = 10, ability_weight = 6, stats_weight = 16, move_weight = 11, add_move_weight = 11;
        int space_weight = 100 - species_weight - ability_weight - stats_weight - 4 * move_weight + add_move_weight;
        if (binding.fAPChosenSpecies.getVisibility() == View.VISIBLE)
            space_weight += species_weight;
        if (binding.fAPChosenAbility.getVisibility() == View.VISIBLE)
            space_weight += ability_weight;
        if (binding.fAPChosenStats.getVisibility() == View.VISIBLE)
            space_weight += stats_weight;

        if (binding.fAPMoves.getVisibility() == View.VISIBLE) {
            space_weight += pokemon.getMoves().size() * move_weight;
            if (pokemon.getMoves().size() == 4) space_weight -= add_move_weight;

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPMoves.getLayoutParams();
            params.weight = 100 - pokemon.getMoves().size() * move_weight;
            binding.fAPMoves.setLayoutParams(params);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPSpace.getLayoutParams();
        params.weight = space_weight;
        binding.fAPSpace.setLayoutParams(params);
    }

    private void initializePageWithData() {
        getAbilitiesTask = new GetAbilities();
        getAbilitiesTask.execute(this, pokemon.getSpecies());
        getBaseStatsTask = new GetBaseStats();
        getBaseStatsTask.execute(this, pokemon.getSpecies());
        getPokemonMovesTask = new GetPokemonMoves();
        getPokemonMovesTask.execute(this, pokemon.getSpecies());

        //load species
        {
            binding.fAPSprite.setImageBitmap(pokemon.getSprite());
            binding.fAPSpecies.setText(pokemon.getSpecies());
            binding.fAPGender.setText(pokemon.getGender());
            typesTV = new ArrayList<TextView>() {{
                add(binding.fAPType1);
            }};
            if (pokemon.getTypes().size() == 2) {
                binding.fAPType2.setVisibility(View.VISIBLE);
                typesTV.add(binding.fAPType2);
            } else {
                binding.fAPType2.setVisibility(View.GONE);
            }
            typesTV.forEach(t -> {
                t.setText(pokemon.getTypes().get(typesTV.indexOf(t)).getName().toUpperCase());
                t.setBackgroundResource(pokemon.getTypes().get(typesTV.indexOf(t)).getColor());
            });
        }
        //load ability
        {
            binding.fAPAbilityName.setText(pokemon.getAbility().getName());
            binding.fAPAbilityDescription.setText(pokemon.getAbility().getDescription());
            binding.fAPAbilitiesButton.setText(R.string.change_ability);
        }
        //load stats related
        {
            binding.fAPLevel.setText(String.valueOf(pokemon.getLevel()));
            binding.fAPNature.setText(pokemon.getNature().getName());
            ArrayList<Integer> totalStats = PokemonConstants.calculateStats(
                    pokemon.getBaseStats(),
                    pokemon.getIVs(),
                    pokemon.getEVs(),
                    pokemon.getLevel(),
                    pokemon.getNature()
            );
            binding.fAPHp.setText(String.valueOf(totalStats.get(0)));
            binding.fAPAttack.setText(String.valueOf(totalStats.get(1)));
            binding.fAPDefense.setText(String.valueOf(totalStats.get(2)));
            binding.fAPSpecialAttack.setText(String.valueOf(totalStats.get(3)));
            binding.fAPSpecialDefense.setText(String.valueOf(totalStats.get(4)));
            binding.fAPSpeed.setText(String.valueOf(totalStats.get(5)));
            binding.fAPStatsButton.setText(R.string.change_stats_related);
        }
        //load moves
        adapter.notifyDataSetChanged();
        if (pokemon.getMoves().size() != 4) binding.fAPAddMove.setVisibility(View.VISIBLE);

        recalculateWeights();
    }

    private void changeGender() {
        if (binding.fAPGender.getText().equals(MALE)) pokemon.setGender(FEMALE);
        else pokemon.setGender(MALE);
        binding.fAPGender.setText(pokemon.getGender());
    }

    public void setAllSpeciesData(ArrayList<SpeciesRow> allSpeciesData) {
        this.allSpeciesData = allSpeciesData;
        binding.fAPSpeciesButton.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void speciesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        speciesTV = dialog.findViewById(R.id.d_search_search);
        speciesTV.setAdapter(new SpeciesAdapter(this.getContext(), allSpeciesData));
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

    @SuppressLint("StaticFieldLeak")
    private void setSpeciesInfo(Dialog dialog, SpeciesRow speciesRow) {
        if (speciesRow != null) {
            binding.fAPAbilitiesButton.setEnabled(false);
            binding.fAPStatsButton.setEnabled(false);
            binding.fAPMoves.setEnabled(false);
            binding.fAPFinalize.setEnabled(false);

            if (getAllSpeciesTask != null && getAllSpeciesTask.getStatus() != AsyncTask.Status.FINISHED)
                getAllSpeciesTask.cancel(true);
            if (getAbilitiesTask != null && getAbilitiesTask.getStatus() != AsyncTask.Status.FINISHED)
                getAbilitiesTask.cancel(true);
            if (getBaseStatsTask != null && getBaseStatsTask.getStatus() != AsyncTask.Status.FINISHED)
                getBaseStatsTask.cancel(true);
            if (getPokemonMovesTask != null && getPokemonMovesTask.getStatus() != AsyncTask.Status.FINISHED)
                getPokemonMovesTask.cancel(true);

            getAbilitiesTask = new GetAbilities();
            getAbilitiesTask.execute(this, speciesRow.getSpecies());
            getBaseStatsTask = new GetBaseStats();
            getBaseStatsTask.execute(this, speciesRow.getSpecies());
            getPokemonMovesTask = new GetPokemonMoves();
            getPokemonMovesTask.execute(this, speciesRow.getSpecies());

            pokemon.setSprite(speciesRow.getSprite());
            pokemon.setSpecies(speciesRow.getSpecies());
            pokemon.setTypes(speciesRow.getTypes());
            pokemon.getMoves().clear();
            adapter.notifyDataSetChanged();
            binding.fAPSprite.setImageBitmap(pokemon.getSprite());
            binding.fAPSpecies.setText(pokemon.getSpecies());
            binding.fAPGender.setText(pokemon.getGender());
            typesTV = new ArrayList<TextView>() {{
                add(binding.fAPType1);
            }};
            if (pokemon.getTypes().size() == 2) {
                binding.fAPType2.setVisibility(View.VISIBLE);
                typesTV.add(binding.fAPType2);
            } else {
                binding.fAPType2.setVisibility(View.GONE);
            }
            typesTV.forEach(t -> {
                t.setText(pokemon.getTypes().get(typesTV.indexOf(t)).getName().toUpperCase());
                t.setBackgroundResource(pokemon.getTypes().get(typesTV.indexOf(t)).getColor());
            });

            binding.fAPChosenSpecies.setVisibility(View.VISIBLE);
            binding.fAPChosenAbility.setVisibility(View.GONE);
            binding.fAPChosenStats.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.fAPMoves.getLayoutParams();
            params.weight = 0;
            binding.fAPMoves.setLayoutParams(params);
            binding.fAPMoves.setVisibility(View.GONE);
            binding.fAPAddMove.setVisibility(View.VISIBLE);

            recalculateWeights();

            binding.fAPSpeciesButton.setText(R.string.change_species);
            binding.fAPAbilitiesButton.setText(R.string.choose_ability);
            binding.fAPStatsButton.setText(R.string.choose_stats_related);
        }
        dialog.dismiss();
    }

    public void setAbilities(ArrayList<Ability> allAbilitiesData) {
        this.allAbilitiesData = allAbilitiesData;
        binding.fAPAbilitiesButton.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void abilitiesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView abilities = dialog.findViewById(R.id.d_search_search);
        abilities.setAdapter(new AbilityAdapter(this.getContext(), allAbilitiesData));
        abilities.setThreshold(1);
        if (pokemon.getAbility() != null) abilities.setText(pokemon.getAbility().getName());
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
            binding.fAPAbilityName.setText(pokemon.getAbility().getName());
            binding.fAPAbilityDescription.setText(pokemon.getAbility().getDescription());
            binding.fAPAbilitiesButton.setText(R.string.change_ability);
            if (binding.fAPChosenAbility.getVisibility() == View.GONE) {
                binding.fAPChosenAbility.setVisibility(View.VISIBLE);
                recalculateWeights();
            }
            seeIfFinalizingIsPossible();
        }
        dialog.dismiss();
    }

    public void setBaseStats(ArrayList<Integer> baseStats) {
        pokemon.setBaseStats(baseStats);
        binding.fAPStatsButton.setEnabled(true);
    }

    private void statsDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_stats);
        dialog.show();

        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        {
            levelET = dialog.findViewById(R.id.d_stats_level);
            natureSpinner = dialog.findViewById(R.id.d_stats_nature);

            ivsET.clear();
            ivsET.add(dialog.findViewById(R.id.d_stats_ivhp));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivattack));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivdefense));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspecialattack));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspecialdefense));
            ivsET.add(dialog.findViewById(R.id.d_stats_ivspeed));

            evsET.clear();
            evsET.add(dialog.findViewById(R.id.d_stats_evhp));
            evsET.add(dialog.findViewById(R.id.d_stats_evattack));
            evsET.add(dialog.findViewById(R.id.d_stats_evdefense));
            evsET.add(dialog.findViewById(R.id.d_stats_evspecialattack));
            evsET.add(dialog.findViewById(R.id.d_stats_evspecialdefense));
            evsET.add(dialog.findViewById(R.id.d_stats_evspeed));

            totalTV.clear();
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

        addOnTextChangedListener(levelET);

        natureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTotalStatsTVs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ivsET.forEach(this::addOnTextChangedListener);
        evsET.forEach(this::addOnTextChangedListener);

        Button button = dialog.findViewById(R.id.d_stats_button);
        button.setOnClickListener(v -> setStatsInfo(dialog));
    }

    private void setStatsInfo(Dialog dialog) {
        setTotalStatsTVs();
        pokemon.setLevel(Integer.parseInt(levelET.getText().toString()));
        pokemon.setNature(Nature.getNature(natureSpinner.getSelectedItem().toString()));
        for (int i = 0; i < NUMBER_OF_STATS; i++) {
            pokemon.getIVs().set(i, Integer.parseInt(ivsET.get(i).getText().toString()));
            pokemon.getEVs().set(i, Integer.parseInt(evsET.get(i).getText().toString()));
        }

        binding.fAPLevel.setText(String.valueOf(pokemon.getLevel()));
        binding.fAPNature.setText(pokemon.getNature().getName());
        binding.fAPHp.setText(totalTV.get(0).getText());
        binding.fAPAttack.setText(totalTV.get(1).getText());
        binding.fAPDefense.setText(totalTV.get(2).getText());
        binding.fAPSpecialAttack.setText(totalTV.get(3).getText());
        binding.fAPSpecialDefense.setText(totalTV.get(4).getText());
        binding.fAPSpeed.setText(totalTV.get(5).getText());
        binding.fAPStatsButton.setText(R.string.change_stats_related);

        if (binding.fAPChosenStats.getVisibility() == View.GONE) {
            binding.fAPChosenStats.setVisibility(View.VISIBLE);
            recalculateWeights();
        }
        seeIfFinalizingIsPossible();
        dialog.dismiss();
    }

    private void addOnTextChangedListener(Object object) {
        if (object instanceof EditText) {
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String correctValue = s.toString();
                    if (!correctValue.equals("")) {
                        if (correctValue.startsWith("0") && correctValue.length() != 1) {
                            correctValue = correctValue.replace("0", "");
                        }
                        int limit = (object.equals(levelET) ? 100 : (ivsET.contains(object) ? 31 : (evsET.contains(object) ? 252 : 0)));
                        if (Integer.parseInt(correctValue) > limit) {
                            correctValue = "" + limit;
                        }
                        ((EditText) object).removeTextChangedListener(this);
                        ((EditText) object).setText(correctValue);
                        ((EditText) object).addTextChangedListener(this);
                    }
                }

                @SuppressLint("ResourceAsColor")
                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals(""))
                        setTotalStatsTVs();
                }
            };
            ((EditText) object).addTextChangedListener(textWatcher);
        }
    }

    private void setTotalStatsTVs() {
        int level = Integer.parseInt(levelET.getText().toString());
        Nature nature = Nature.getNature(natureSpinner.getSelectedItem().toString());
        ArrayList<Integer> ivs = (ArrayList<Integer>) ivsET.stream().map(ivET -> Integer.parseInt(ivET.getText().toString())).collect(Collectors.toList());
        ArrayList<Integer> evs = (ArrayList<Integer>) evsET.stream().map(evET -> Integer.parseInt(evET.getText().toString())).collect(Collectors.toList());
        assert nature != null;
        ArrayList<Integer> totalStats = PokemonConstants.calculateStats(pokemon.getBaseStats(), ivs, evs, level, nature);
        totalTV.forEach(totTV -> totTV.setText(String.valueOf(totalStats.get(totalTV.indexOf(totTV)))));
    }

    public void setMoves(ArrayList<Move> allMovesData) {
        this.allMovesData = allMovesData;
        binding.fAPAddMove.setEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void movesDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 90 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        AutoCompleteTextView movesTV = dialog.findViewById(R.id.d_search_search);
        movesTV.setAdapter(new MoveAdapter(this.getContext(), allMovesData));
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
                pokemon.getMoves().add(move);
                adapter.notifyDataSetChanged();

                if (pokemon.getMoves().size() == 1) binding.fAPMoves.setVisibility(View.VISIBLE);
                if (pokemon.getMoves().size() == 4) binding.fAPAddMove.setVisibility(View.GONE);

                recalculateWeights();
            }
        }
        seeIfFinalizingIsPossible();
        dialog.dismiss();
    }

    private void seeIfFinalizingIsPossible() {
        if (binding.fAPChosenAbility.getVisibility() == View.VISIBLE
                && binding.fAPChosenStats.getVisibility() == View.VISIBLE
                && pokemon.getMoves().size() > 0) {
            binding.fAPFinalize.setEnabled(true);
        }
    }

    private void nameDialog() {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_add_name);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.d_addname_button).setOnClickListener(v -> {
            EditText name = dialog.findViewById(R.id.d_addname_name);
            if (name.getText().toString().trim().equals("")) {
                Toast.makeText(dialog.getContext(), "Empty name!", Toast.LENGTH_SHORT).show();
                return;
            }
            pokemon.setName(name.getText().toString().trim());
            ArrayList<Move> arrayCopy = new ArrayList<>(pokemon.getMoves());
            pokemon.setMoves(arrayCopy);
            int n = 4 - pokemon.getMoves().size();
            for (int i = 0; i < n; i++) pokemon.getMoves().add(null);
            if (pokemon.getID() == -1) new InsertTask().execute(this, pokemon);
            else new UpdateTask().execute(this, pokemon);
            dialog.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (getAllSpeciesTask != null && getAllSpeciesTask.getStatus() != AsyncTask.Status.FINISHED)
            getAllSpeciesTask.cancel(true);
        if (getAbilitiesTask != null && getAbilitiesTask.getStatus() != AsyncTask.Status.FINISHED)
            getAbilitiesTask.cancel(true);
        if (getBaseStatsTask != null && getBaseStatsTask.getStatus() != AsyncTask.Status.FINISHED)
            getBaseStatsTask.cancel(true);
        if (getPokemonMovesTask != null && getPokemonMovesTask.getStatus() != AsyncTask.Status.FINISHED)
            getPokemonMovesTask.cancel(true);
    }
}

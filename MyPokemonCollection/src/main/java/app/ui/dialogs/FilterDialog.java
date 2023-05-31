package app.ui.dialogs;

import static app.constants.PokemonConstants.DEFAULT_POKEDEX_NUMBER;
import static app.data_objects.PokemonType.POKEMON_TYPES;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.mypokemoncollection.R;

import java.util.stream.Collectors;

import app.data_objects.Pokemon;
import app.data_objects.PokemonType;
import app.storages.Storage;
import app.ui.adapters.PokemonTypeAdapter;
import app.ui.adapters.searchable.SpeciesAdapter;
import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.ICallbackContext;

public class FilterDialog extends GeneralisedDialog {

    private long selectedSpeciesPokedexNumber;
    private PokemonType selectedPokemonType1;
    private PokemonType selectedPokemonType2;

    private EditText namePattern;
    private AutoCompleteTextView species;
    private Spinner pokemonType1;
    private Spinner pokemonType2;

    public FilterDialog(ICallbackContext callbackContext) {
        super(callbackContext);
    }

    @Override
    protected void create() {
        GeneralisedFragment<?> fragment = ((GeneralisedFragment<?>) callbackContext);
        dialog = new Dialog(fragment.requireActivity());
        dialog.setContentView(R.layout.dialog_left);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_1),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_2),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_3),
                        ContextCompat.getColor(fragment.requireActivity(), R.color.analogous_4),
                });
        dialog.getWindow().setBackgroundDrawable(gradientDrawable);
        dialog.getWindow().setGravity(Gravity.START);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void setupFunctionality() {
        namePattern = dialog.findViewById(R.id.dfNamePatternET);
        species = dialog.findViewById(R.id.dfSpeciesACTV);
        pokemonType1 = dialog.findViewById(R.id.dfType1S);
        pokemonType2 = dialog.findViewById(R.id.dfType2S);

        if (Storage.getPokemonSpeciesList() == null) {
            species.setEnabled(false);
        } else {
            notifyPokemonSpeciesDataIsAvailable();
        }
        pokemonType2.setEnabled(false);

        dialog.findViewById(R.id.dfContent).setOnClickListener(view -> hideSoftKeyboard());

        selectedSpeciesPokedexNumber = DEFAULT_POKEDEX_NUMBER;
        species.setThreshold(1);
        species.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpeciesPokedexNumber = ((Pokemon) adapterView.getAdapter().getItem(i)).getPokedexNumber();
                pokemonType1.setSelection(0);
                pokemonType2.setSelection(0);
                selectedPokemonType1 = POKEMON_TYPES.get(0);
                selectedPokemonType2 = POKEMON_TYPES.get(0);
                pokemonType1.setEnabled(false);
                pokemonType2.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        species.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (selectedSpeciesPokedexNumber != DEFAULT_POKEDEX_NUMBER) {
                    selectedSpeciesPokedexNumber = DEFAULT_POKEDEX_NUMBER;
                    pokemonType1.setEnabled(true);
                    if (!selectedPokemonType2.equals(POKEMON_TYPES.get(0))) {
                        pokemonType2.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        selectedPokemonType1 = POKEMON_TYPES.get(0);
        pokemonType1.setAdapter(new PokemonTypeAdapter(((GeneralisedFragment<?>) callbackContext).requireActivity(), POKEMON_TYPES));
        pokemonType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPokemonType1 = (PokemonType) adapterView.getAdapter().getItem(i);
                pokemonType2.setAdapter(new PokemonTypeAdapter(
                        ((GeneralisedFragment<?>) callbackContext).requireActivity(),
                        POKEMON_TYPES.stream()
                                .filter(pokemonType ->
                                        pokemonType.equals(POKEMON_TYPES.get(0))
                                        || !pokemonType.equals(selectedPokemonType1))
                                .collect(Collectors.toList())));
                pokemonType2.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        selectedPokemonType2 = POKEMON_TYPES.get(0);
        pokemonType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPokemonType2 = (PokemonType) adapterView.getAdapter().getItem(i);
                if (selectedPokemonType2.equals(POKEMON_TYPES.get(0))) {
                    pokemonType2.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        dialog.findViewById(R.id.dfFilterB).setOnClickListener(view -> {
            callbackContext.callback(this, selectedSpeciesPokedexNumber);
            dialog.dismiss();
        });
    }

    public void notifyPokemonSpeciesDataIsAvailable() {
        species.setAdapter(new SpeciesAdapter(dialog.getContext(), Storage.getPokemonSpeciesList()));
        species.setEnabled(true);
        dialog.findViewById(R.id.dfLoadingPB).setVisibility(View.GONE);
    }

    public String getNamePattern() {
        return namePattern.getText().toString();
    }

    public PokemonType getSelectedPokemonType1() {
        return selectedPokemonType1;
    }

    public PokemonType getSelectedPokemonType2() {
        return selectedPokemonType2;
    }

    private void hideSoftKeyboard() {
        Activity activity = ((GeneralisedFragment<?>)callbackContext).requireActivity();
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    dialog.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}

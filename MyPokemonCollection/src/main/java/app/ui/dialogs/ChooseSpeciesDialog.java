package app.ui.dialogs;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import app.data_objects.Pokemon;
import app.ui.layout_adapters.searchable.SpeciesAdapter;
import app.ui.fragments.AddPokemon;
import app.ui.fragments.ICallbackContext;

public class ChooseSpeciesDialog extends GeneralisedDialog {

    private final List<Pokemon> allSpeciesData;
    private final Pokemon pokemon;

    public ChooseSpeciesDialog(ICallbackContext context, List<Pokemon> allSpeciesData, Pokemon pokemon) {
        super(context);
        this.allSpeciesData = allSpeciesData;
        this.pokemon = pokemon;
    }

    @Override
    protected void create() {
        dialog = new android.app.Dialog(((AddPokemon) callbackContext).getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_search);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AddPokemon) callbackContext).requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void setupFunctionality() {
        AutoCompleteTextView speciesTV = dialog.findViewById(R.id.d_search_search);
        speciesTV.setAdapter(new SpeciesAdapter(((AddPokemon) callbackContext).getActivity(), allSpeciesData));
        speciesTV.setThreshold(1);
        if (pokemon.getPokedexNumber() > 0) {
            speciesTV.setText(pokemon.getSpecies());
        }

        speciesTV.setOnTouchListener((View v, MotionEvent event) -> {
            speciesTV.showDropDown();
            return false;
        });

        AtomicReference<Pokemon> species = new AtomicReference<>();
        speciesTV.setOnItemClickListener((parent, view, position, id) -> species.set((Pokemon) speciesTV.getAdapter().getItem(position)));
        dialog.findViewById(R.id.d_search_button).setOnClickListener(v -> {
            if (species.get() != null) callbackContext.callback(this, species.get());
            else callbackContext.timedOut(this);
            dialog.dismiss();
        });
    }
}

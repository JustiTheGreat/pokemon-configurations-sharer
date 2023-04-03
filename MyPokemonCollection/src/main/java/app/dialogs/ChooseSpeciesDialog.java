package app.dialogs;

import android.annotation.SuppressLint;
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

import app.ui.fragments.ICallbackContext;
import app.data_objects.Pokemon;
import app.layout_adapters.SpeciesAdapter;
import app.ui.fragments.AddPokemon;

public class ChooseSpeciesDialog extends app.dialogs.Dialog {
    private final List<Pokemon> allSpeciesData;
    private final Pokemon pokemon;
    private AutoCompleteTextView speciesTV;

    public ChooseSpeciesDialog(ICallbackContext context, int resourceId, List<Pokemon> allSpeciesData, Pokemon pokemon) {
        super(context, resourceId);
        this.allSpeciesData = allSpeciesData;
        this.pokemon = pokemon;
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void loadDialog() {
        dialog = new android.app.Dialog(((AddPokemon) context).getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(resourceId);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.TOP;
        window.setAttributes(layoutParams);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((AddPokemon) context).requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        speciesTV = dialog.findViewById(R.id.d_search_search);
        speciesTV.setAdapter(new SpeciesAdapter(((AddPokemon) context).getActivity(), allSpeciesData));
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
            if (species.get() != null) context.callback(this, species.get());
            else context.timedOut();
            dialog.dismiss();
        });
        dialog.show();
    }
}

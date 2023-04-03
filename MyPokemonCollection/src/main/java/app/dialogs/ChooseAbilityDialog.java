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
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import app.ui.fragments.ICallbackContext;
import app.data_objects.Ability;
import app.layout_adapters.AbilityAdapter;
import app.ui.fragments.AddPokemon;

public class ChooseAbilityDialog extends Dialog {
    private final List<Ability> allAbilitiesData;
    private final Ability pokemonAbility;

    public ChooseAbilityDialog(ICallbackContext context, int resourceId, List<Ability> allAbilitiesData, Ability pokemonAbility) {
        super(context, resourceId);
        this.allAbilitiesData = allAbilitiesData;
        this.pokemonAbility = pokemonAbility;
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

        AutoCompleteTextView abilities = dialog.findViewById(R.id.d_search_search);
        abilities.setAdapter(new AbilityAdapter(((AddPokemon) context).getActivity(), allAbilitiesData));
        abilities.setThreshold(1);
        if (pokemonAbility != null) abilities.setText(pokemonAbility.getName());
        abilities.setOnTouchListener((View v, MotionEvent event) -> {
            abilities.showDropDown();
            return false;
        });
        AtomicReference<Ability> ability = new AtomicReference<>();
        abilities.setOnItemClickListener((parent, view, position, id) -> ability.set((Ability) abilities.getAdapter().getItem(position)));
        Button button = dialog.findViewById(R.id.d_search_button);
        button.setOnClickListener(v -> {
            if (ability.get() != null) context.callback(this, ability.get());
            else context.timedOut();
            dialog.dismiss();
        });
        dialog.show();
    }
}

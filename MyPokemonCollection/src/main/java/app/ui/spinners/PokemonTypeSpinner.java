package app.ui.spinners;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

public class PokemonTypeSpinner extends AppCompatSpinner {

    private boolean open = false;

    public PokemonTypeSpinner(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean performClick() {
        open = true;
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            open = false;
        }
    }

    public boolean hasBeenOpened() {
        return open;
    }
}

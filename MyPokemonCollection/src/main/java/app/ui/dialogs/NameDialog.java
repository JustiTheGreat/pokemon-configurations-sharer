package app.ui.dialogs;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mypokemoncollection.R;

import app.data_objects.Pokemon;
import app.connections.firebase.InsertPokemonDB;
import app.connections.firebase.UpdatePokemonDB;
import app.ui.fragments.ICallbackContext;
import app.ui.fragments.GeneralisedFragment;

public class NameDialog extends GeneralisedDialog {

    private final Pokemon pokemon;

    public NameDialog(ICallbackContext context, Pokemon pokemon) {
        super(context);
        this.pokemon = pokemon;
    }

    @Override
    protected void create() {
        GeneralisedFragment<?> fragment = ((GeneralisedFragment<?>) callbackContext);

        dialog = new Dialog(fragment.requireActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_add_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragment.requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels * 80 / 100;
        dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void setupFunctionality() {
        GeneralisedFragment<?> fragment = ((GeneralisedFragment<?>) callbackContext);

        EditText name = dialog.findViewById(R.id.d_addname_name);
        if (pokemon.getID() != null) name.setText(pokemon.getName());
        dialog.findViewById(R.id.d_addname_button).setOnClickListener(v -> {
            if (name.getText().toString().trim().equals(fragment.getString(R.string.empty))) {
                toast(fragment.getString(R.string.please_input_a_name));
                return;
            }
            pokemon.setName(name.getText().toString().trim());

            if (pokemon.getID() == null) {
                String user_id = getAuthenticatedUserId();
                pokemon.setUserId(user_id);

                disableActivityTouchInput(fragment.requireActivity());
                new InsertPokemonDB(callbackContext, pokemon, POKEMON_COLLECTION).execute();
            } else {
                disableActivityTouchInput(fragment.requireActivity());
                new UpdatePokemonDB(callbackContext, pokemon).execute();
            }
            dialog.dismiss();
        });
    }

    private void disableActivityTouchInput(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void toast(String message) {
        Toast.makeText(((GeneralisedFragment<?>) callbackContext).requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    private String getAuthenticatedUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            callbackContext.timedOut(callbackContext);
            return null;
        }
        return currentUser.getUid();
    }
}

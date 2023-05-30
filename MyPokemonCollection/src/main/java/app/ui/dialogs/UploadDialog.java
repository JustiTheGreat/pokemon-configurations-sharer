package app.ui.dialogs;

import static app.constants.PokemonDatabaseFields.PUBLIC_POKEMON_COLLECTION;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mypokemoncollection.R;

import app.connections.firebase.InsertPokemonDB;
import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class UploadDialog  extends YesNoQuestionDialog implements ICallbackContext {

    private final Pokemon pokemon;

    public UploadDialog(ICallbackContext callbackContext, Pokemon pokemon) {
        super(callbackContext, R.string.do_you_really_want_to_upload_this_pokemon);
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void doTheAction() {
        new InsertPokemonDB(this, pokemon, PUBLIC_POKEMON_COLLECTION).execute();
    }

    @Override
    public void callback(Object caller, Object result) {
        callbackContext.callback(this, result);
    }

    @Override
    public void timedOut(Object caller) {
        callbackContext.timedOut(this);
    }
}

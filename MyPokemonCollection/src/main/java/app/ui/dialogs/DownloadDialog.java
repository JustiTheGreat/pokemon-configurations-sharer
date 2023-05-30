package app.ui.dialogs;

import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mypokemoncollection.R;

import app.connections.firebase.InsertPokemonDB;
import app.data_objects.Pokemon;
import app.storages.Storage;
import app.ui.fragments.GeneralisedFragment;
import app.ui.fragments.ICallbackContext;

public class DownloadDialog extends YesNoQuestionDialog implements ICallbackContext {

    private final Pokemon pokemon;
    private final String userId;

    public DownloadDialog(ICallbackContext callbackContext, Pokemon pokemon, String userId) {
        super(callbackContext, R.string.do_you_really_want_to_download_this_pokemon);
        this.pokemon = pokemon;
        this.userId = userId;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void doTheAction() {
        pokemon.setUserId(userId);
        new InsertPokemonDB(this, pokemon, POKEMON_COLLECTION).execute();
    }

    @Override
    public void callback(Object caller, Object result) {
        callbackContext.callback(this, pokemon);
    }

    @Override
    public void timedOut(Object caller) {
        callbackContext.timedOut(this);
    }
}

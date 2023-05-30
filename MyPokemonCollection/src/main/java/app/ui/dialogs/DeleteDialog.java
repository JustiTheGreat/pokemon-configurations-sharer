package app.ui.dialogs;

import com.mypokemoncollection.R;

import app.connections.firebase.DeletePokemonDB;
import app.ui.fragments.ICallbackContext;

public class DeleteDialog extends YesNoQuestionDialog {

    private final String pokemonId;

    public DeleteDialog(ICallbackContext callbackContext, String pokemonId) {
        super(callbackContext, R.string.do_you_really_want_to_delete_this_pokemon);
        this.pokemonId = pokemonId;
    }

    @Override
    protected void doTheAction() {
        new DeletePokemonDB(callbackContext, pokemonId).execute();
    }
}

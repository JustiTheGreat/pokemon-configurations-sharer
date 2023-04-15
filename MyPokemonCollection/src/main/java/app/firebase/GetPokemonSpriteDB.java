package app.firebase;

import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.storage.FirebaseStorage;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetPokemonSpriteDB {

    private final ICallbackContext callbackContext;
    private final Pokemon pokemon;

    public GetPokemonSpriteDB(ICallbackContext callbackContext, Pokemon pokemon) {
        this.callbackContext = callbackContext;
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        final long ONE_MEGABYTE = 1024 * 1024;
        FirebaseStorage.getInstance().getReference()
                .child("pokemon_sprite/" + pokemon.getPokedexNumber() + ".png")
                .getBytes(ONE_MEGABYTE)
                .addOnFailureListener(task -> callbackContext.timedOut(this))
                .addOnSuccessListener(bytes -> {
                    pokemon.setSprite(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    callbackContext.callback(this, pokemon);
                });
    }
}

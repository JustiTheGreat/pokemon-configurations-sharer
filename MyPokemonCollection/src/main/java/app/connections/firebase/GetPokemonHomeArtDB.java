package app.connections.firebase;

import static app.constants.Gender.FEMALE_GENDER;
import static app.constants.Gender.FEMALE_GENDER_VALUES;
import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.MALE_GENDER_VALUES;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.PokemonConstants.NORMAL_FORM_VALUE;
import static app.constants.PokemonConstants.SHINY_FORM_VALUE;
import static app.constants.StringConstants.FIRESTORE_REFERENCE_TEMPLATE;
import static app.constants.StringConstants.GENDER_PLACEHOLDER;
import static app.constants.StringConstants.POKEDEX_NUMBER_PLACEHOLDER;
import static app.constants.StringConstants.SHINY_PLACEHOLDER;

import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import app.data_objects.Pokemon;
import app.ui.fragments.ICallbackContext;

public class GetPokemonHomeArtDB {

    private final ICallbackContext callbackContext;
    private final Pokemon pokemon;

    public GetPokemonHomeArtDB(ICallbackContext callbackContext, Pokemon pokemon) {
        this.callbackContext = callbackContext;
        this.pokemon = pokemon;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void execute() {
        final long ONE_MEGABYTE = 1024 * 1024;
        getImageStorageReference(pokemon.getPokedexNumber(), pokemon.getGender(), pokemon.isShiny())
                .getBytes(ONE_MEGABYTE)
                .addOnFailureListener(task -> callbackContext.timedOut(this))
                .addOnSuccessListener(bytes -> {
                    pokemon.setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    callbackContext.callback(this, pokemon);
                });
    }

    private StorageReference getImageStorageReference(long pokedexNumber, String gender, boolean isShiny) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        String s = FIRESTORE_REFERENCE_TEMPLATE
                .replace(POKEDEX_NUMBER_PLACEHOLDER, String.format("%04d", pokedexNumber))
                .replace(SHINY_PLACEHOLDER, isShiny ? SHINY_FORM_VALUE : NORMAL_FORM_VALUE);
        StorageReference pathReference = null;
        switch (gender) {
            case MALE_GENDER:
                for (String genderValue : MALE_GENDER_VALUES) {
                    String temp = s.replace(GENDER_PLACEHOLDER, genderValue);
                    pathReference = storageReference.child(temp);
                    try {
                        pathReference.getDownloadUrl();
                        break;
                    } catch (Exception ignored) {
                    }
                }
                break;
            case FEMALE_GENDER:
                for (String genderValue : FEMALE_GENDER_VALUES) {
                    String temp = s.replace(GENDER_PLACEHOLDER, genderValue);
                    pathReference = storageReference.child(temp);
                    try {
                        pathReference.getDownloadUrl();
                        break;
                    } catch (Exception ignored) {
                    }
                }
                break;
            case UNKNOWN_GENDER:
                String temp = s.replace(GENDER_PLACEHOLDER, gender);
                pathReference = storageReference.child(temp);
                break;
        }
        return pathReference;
    }
}

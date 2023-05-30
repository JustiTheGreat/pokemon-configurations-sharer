package app.connections.firebase;

import static android.content.ContentValues.TAG;
import static app.constants.Gender.FEMALE_GENDER;
import static app.constants.Gender.FEMALE_GENDER_VALUES;
import static app.constants.Gender.MALE_GENDER;
import static app.constants.Gender.MALE_GENDER_VALUES;
import static app.constants.Gender.UNKNOWN_GENDER;
import static app.constants.Gender.UNKNOWN_GENDER_VALUE;
import static app.constants.PokemonConstants.NORMAL_FORM_VALUE;
import static app.constants.PokemonConstants.SHINY_FORM_VALUE;
import static app.constants.StringConstants.FIRESTORE_REFERENCE_TEMPLATE;
import static app.constants.StringConstants.GENDER_PLACEHOLDER;
import static app.constants.StringConstants.POKEDEX_NUMBER_PLACEHOLDER;
import static app.constants.StringConstants.SHINY_PLACEHOLDER;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String s = FIRESTORE_REFERENCE_TEMPLATE
                .replace(POKEDEX_NUMBER_PLACEHOLDER, String.format("%04d", pokemon.getPokedexNumber()))
                .replace(SHINY_PLACEHOLDER, pokemon.isShiny() ? SHINY_FORM_VALUE : NORMAL_FORM_VALUE);
        switch (pokemon.getGender()) {
            case MALE_GENDER:
                getDownloadUrl(storageReference, s, MALE_GENDER_VALUES, 0);
                break;
            case FEMALE_GENDER:
                getDownloadUrl(storageReference, s, FEMALE_GENDER_VALUES, 0);
                break;
            case UNKNOWN_GENDER:
                String temp = s.replace(GENDER_PLACEHOLDER, UNKNOWN_GENDER_VALUE);
                downloadImage(storageReference.child(temp));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getDownloadUrl(StorageReference storageReference, String s, String[] values, int valueIndex) {
        String genderValue = values[valueIndex];
        String temp = s.replace(GENDER_PLACEHOLDER, genderValue);
        StorageReference pathReference = storageReference.child(temp);
        final int finalValueIndex = valueIndex + 1;
        if (finalValueIndex < values.length) {
            try {
                pathReference.getDownloadUrl()
                        .addOnFailureListener(runnable -> getDownloadUrl(storageReference, s, values, finalValueIndex))
                        .addOnSuccessListener(runnable -> downloadImage(pathReference));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void downloadImage(StorageReference storageReference) {
        final long ONE_MEGABYTE = 1024 * 1024;
        try {
            storageReference.getBytes(ONE_MEGABYTE)
                    .addOnFailureListener(task -> {
                        Log.e(TAG, task.getMessage());
                        task.printStackTrace();
                        callbackContext.timedOut(this);
                    })
                    .addOnSuccessListener(bytes -> {
                        pokemon.setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        callbackContext.callback(this, pokemon);
                    });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            callbackContext.timedOut(this);
        }
    }
}

package app.ui.fragments;

import static app.constants.Messages.DELETING_FAILED;
import static app.constants.PokemonDatabaseFields.POKEMON_COLLECTION;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mypokemoncollection.R;

import app.data_objects.Pokemon;
import app.storages.Storage;

public abstract class UtilityFragment extends Fragment implements ICallbackContext {
    protected void navigateTo(int rId) {
        NavHostFragment.findNavController(this).navigate(rId);
    }

    protected void toast(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard(ViewBinding binding) {
        InputMethodManager imm = (InputMethodManager) this.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }

    protected Dialog createDialog(int rId) {
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(rId);
        dialog.show();
        return dialog;
    }

    protected int getDisplayWidthInPixels() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    protected boolean haveAuthenticatedUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser == null;
    }

    protected String getAuthenticatedUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            timedOut();
            return null;
        }
        return currentUser.getUid();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void insertPokemonInDatabase(Pokemon pokemon, int navigateAfterId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POKEMON_COLLECTION)
                .add(pokemon.getDatabaseMapObject())
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (Storage.getSelectedPokemon() != null) Storage.setSelectedPokemon(null);
                                Storage.setChangesMade(true);
                                navigateTo(navigateAfterId);
                            } else timedOut();
                        }
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void updatePokemonInDatabase(Pokemon pokemon, int navigateAfterId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POKEMON_COLLECTION)
                .document(pokemon.getID())
                .set(pokemon.getDatabaseMapObject())
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (Storage.getSelectedPokemon() != null) Storage.setSelectedPokemon(null);
                                Storage.setChangesMade(true);
                                navigateTo(R.id.action_add_to_collection);
                            } else timedOut();
                        }
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    protected void deletePokemonFromDatabase(Pokemon pokemon, int navigateAfterId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POKEMON_COLLECTION)
                .document(pokemon.getID())
                .delete()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (Storage.getSelectedPokemon() != null) Storage.setSelectedPokemon(null);
                                Storage.setChangesMade(true);
                                navigateTo(R.id.action_details_to_collection);
                            } else {
                                toast(DELETING_FAILED);
                            }
                        }
                );
    }
}

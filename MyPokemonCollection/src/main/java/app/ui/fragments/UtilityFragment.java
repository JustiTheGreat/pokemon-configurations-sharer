package app.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.ui.fragments.ICallbackContext;

public abstract class UtilityFragment extends Fragment implements ICallbackContext {

//    void saveCurrentFragmentReference() {
//        requireActivity().getSupportFragmentManager().putFragment(requireActivity().getIntent().getExtras(), "current fragment", this);
//    }

    public void navigateTo(int rId) {
        NavHostFragment.findNavController(this).navigate(rId);
    }

    protected void toast(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
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
        return currentUser != null;
    }

    protected String getAuthenticatedUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            timedOut(this);
            return null;
        }
        return currentUser.getUid();
    }

    protected void enableActivityTouchInput() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void disableActivityTouchInput() {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}

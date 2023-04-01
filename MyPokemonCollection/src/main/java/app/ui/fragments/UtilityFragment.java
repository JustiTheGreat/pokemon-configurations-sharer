package app.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

public class UtilityFragment extends Fragment {
    protected void navigateTo(int rId) {
        NavHostFragment.findNavController(this).navigate(rId);
    }

    protected void toast(String message){
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void hideKeyboard(ViewBinding binding){
        InputMethodManager imm = (InputMethodManager) this.requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }

    protected Dialog createDialog(int rId){
        Dialog dialog = new Dialog(this.getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(rId);
        dialog.show();
        return dialog;
    }

    protected int getDisplayWidthInPixels(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}

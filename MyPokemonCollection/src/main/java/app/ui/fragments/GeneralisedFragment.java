package app.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import app.connections.async_tasks.GeneralisedTask;
import app.ui.activities.MainActivity;

public abstract class GeneralisedFragment<T> extends Fragment implements ICallbackContext {
    protected T binding;
    protected List<GeneralisedTask<?>> asyncTasks = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cancelAsyncTasks();
    }

    public void addTask(GeneralisedTask<?> asyncTask) {
        asyncTasks.add(asyncTask);
    }

    public void navigateTo(int rId) {
        NavHostFragment.findNavController(this).navigate(rId);
    }

    protected void setLogoutVisibility(boolean visibility) {
        ((MainActivity) requireActivity()).setLogoutVisibility(visibility);
    }

    protected void toast(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected void hideKeyboard(ViewBinding binding) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }

    protected boolean haveAuthenticatedUser() {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void cancelAsyncTasks() {
        asyncTasks.forEach(asyncTask -> {
                    if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED)
                        asyncTask.cancel(true);
                }
        );
    }
}

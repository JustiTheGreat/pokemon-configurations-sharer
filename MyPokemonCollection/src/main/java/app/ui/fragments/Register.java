package app.ui.fragments;

import static app.constants.StringConstants.REGISTER_SUCCESS;
import static app.constants.StringConstants.SERVER_TIMEOUT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentRegisterBinding;

import app.async_tasks.database.ICallbackContext;
import app.async_tasks.database.RegisterTask;

public class Register extends Fragment {
    private FragmentRegisterBinding binding;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private final Fragment THIS = this;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = binding.registerTextboxEmail;
        password = binding.registerTextboxPassword;
//        binding.registerButtonRegisterbutton.setOnClickListener(v ->
//                new RegisterTask(this).execute(username.getText().toString(),
//                        email.getText().toString(), password.getText().toString()));
        binding.registerButtonRegisterbutton.setOnClickListener(v -> mAuth
                .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        NavHostFragment
                                .findNavController(this)
                                .navigate(R.id.action_register_to_login);
                        Toast.makeText(THIS.getActivity(), "Account created!", Toast.LENGTH_SHORT).show();
                    } else {
                        email.setText("");
                        password.setText("");
                        Toast.makeText(THIS.getActivity(), "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    public void callback(Object caller, Object result) {
//        if (caller instanceof RegisterTask) {
//            if (result.equals(REGISTER_SUCCESS)) {
//                NavHostFragment
//                        .findNavController(this)
//                        .navigate(R.id.action_register_to_login);
//            } else Toast.makeText(this.getActivity(), (String) result, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void timedOut() {
//        Toast.makeText(this.getActivity(), SERVER_TIMEOUT, Toast.LENGTH_LONG).show();
//    }
}
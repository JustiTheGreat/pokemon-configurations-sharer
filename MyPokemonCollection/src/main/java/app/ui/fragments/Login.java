package app.ui.fragments;

import static app.constants.Messages.EMPTY;
import static app.constants.Messages.PLEASE_INPUT_YOUR_EMAIL;
import static app.constants.Messages.PLEASE_INPUT_YOUR_PASSWORD;
import static app.constants.Messages.WRONG_CREDENTIALS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentLoginBinding;

public class Login extends UtilityFragment {
    private FragmentLoginBinding binding;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        email = binding.loginEmailTB;
        password = binding.loginPasswordTB;
        mAuth = FirebaseAuth.getInstance();
        binding.loginLoginB.setOnClickListener(this::loginButtonListener);
        binding.loginRegisterTVL.setOnClickListener(v -> {
            resetEditTexts();
            navigateTo(R.id.action_login_to_register);
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) navigateTo(R.id.action_login_to_collection);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void resetEditTexts() {
        email.setText(EMPTY);
        password.setText(EMPTY);
    }

    private void loginButtonListener(View view) {
        if (email.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_EMAIL);
            return;
        }
        if (password.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_PASSWORD);
            return;
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this.requireActivity(), task -> {
                    hideKeyboard(binding);
                    if (task.isSuccessful()) {
                        navigateTo(R.id.action_login_to_collection);
                    } else {
                        resetEditTexts();
                        toast(WRONG_CREDENTIALS);
                    }
                });
    }
}
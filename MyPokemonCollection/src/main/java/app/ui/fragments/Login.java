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
import androidx.annotation.Nullable;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentLoginBinding;

import app.firebase.LoginAuth;

public class Login extends UtilityFragment {
    private FragmentLoginBinding binding;
    private EditText email;
    private EditText password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        email = binding.loginEmailTB;
        password = binding.loginPasswordTB;
        binding.loginLoginB.setOnClickListener(this::loginButtonListener);
        binding.loginRegisterTVL.setOnClickListener(v -> {
            resetEditTexts();
            navigateTo(R.id.action_login_to_register);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (haveAuthenticatedUser()) {
            setLogoutVisibility(true);
            navigateTo(R.id.action_login_to_collection);
        }
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
        if (email.getText().toString().trim().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_EMAIL);
            return;
        }
        if (password.getText().toString().trim().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_PASSWORD);
            return;
        }
        new LoginAuth(this, email.getText().toString().trim(), password.getText().toString().trim()).execute();
    }

    @Override
    public void callback(Object caller, Object result) {
        if(caller instanceof LoginAuth) {
            hideKeyboard(binding);
            setLogoutVisibility(true);
            navigateTo(R.id.action_login_to_collection);
        }
    }

    @Override
    public void timedOut(Object caller) {
        if(caller instanceof LoginAuth) {
            hideKeyboard(binding);
            resetEditTexts();
            toast(WRONG_CREDENTIALS);
        }
    }
}
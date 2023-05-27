package app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentLoginBinding;

import app.connections.firebase.LoginAuth;

public class Login extends GeneralisedFragment<FragmentLoginBinding> {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

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
        if (haveAuthenticatedUser()) {
            setLogoutVisibility(true);
            navigateTo(R.id.action_login_to_collection);
        }
    }

    private void resetEditTexts() {
        binding.loginEmailTB.setText(getString(R.string.empty));
        binding.loginPasswordTB.setText(getString(R.string.empty));
    }

    private void loginButtonListener(View view) {
        if (binding.loginEmailTB.getText().toString().trim().isEmpty()) {
            toast(getString(R.string.please_input_your_email));
            return;
        }
        if (binding.loginPasswordTB.getText().toString().trim().isEmpty()) {
            toast(getString(R.string.please_input_your_password));
            return;
        }
        new LoginAuth(this, binding.loginEmailTB.getText().toString().trim(), binding.loginPasswordTB.getText().toString().trim()).execute();
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
            toast(getString(R.string.wrong_credentials));
        }
    }
}
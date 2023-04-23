package app.ui.fragments;

import static app.constants.Messages.ACCOUNT_CREATED;
import static app.constants.Messages.EMPTY;
import static app.constants.Messages.PLEASE_INPUT_YOUR_EMAIL;
import static app.constants.Messages.PLEASE_INPUT_YOUR_PASSWORD;
import static app.constants.Messages.REGISTER_FAILED;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentRegisterBinding;

import app.firebase.RegisterAuth;

public class Register extends UtilityFragment {
    private FragmentRegisterBinding binding;
    private EditText email, password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        email = binding.registerEmailTB;
        password = binding.registerPasswordTB;
        binding.registerRegisterB.setOnClickListener(this::registerButtonListener);
        return binding.getRoot();
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

    private void registerButtonListener(View view) {
        if (email.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_EMAIL);
            return;
        }
        if (password.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_PASSWORD);
            return;
        }
        new RegisterAuth(this, email.getText().toString().trim(), password.getText().toString().trim()).execute();
    }

    @Override
    public void callback(Object caller, Object result) {
        if(caller instanceof RegisterAuth) {
            hideKeyboard(binding);
            navigateTo(R.id.action_register_to_login);
            toast(ACCOUNT_CREATED);
        }
    }

    @Override
    public void timedOut(Object caller) {
        if(caller instanceof RegisterAuth) {
            hideKeyboard(binding);
            resetEditTexts();
            toast(REGISTER_FAILED);
        }
    }
}
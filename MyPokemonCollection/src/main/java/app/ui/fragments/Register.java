package app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentRegisterBinding;

import app.connections.firebase.RegisterAuth;

public class Register extends GeneralisedFragment<FragmentRegisterBinding> {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.registerRegisterB.setOnClickListener(this::registerButtonListener);

        return binding.getRoot();
    }

    private void resetEditTexts() {
        binding.registerEmailTB.setText(getString(R.string.empty));
        binding.registerPasswordTB.setText(getString(R.string.empty));
    }

    private void registerButtonListener(View view) {
        if (binding.registerEmailTB.getText().toString().isEmpty()) {
            toast(getString(R.string.please_input_your_email));
            return;
        }
        if (binding.registerPasswordTB.getText().toString().isEmpty()) {
            toast(getString(R.string.please_input_your_password));
            return;
        }
        new RegisterAuth(this, binding.registerEmailTB.getText().toString().trim(),
                binding.registerPasswordTB.getText().toString().trim()).execute();
    }

    @Override
    public void callback(Object caller, Object result) {
        if(caller instanceof RegisterAuth) {
            hideKeyboard(binding);
            navigateTo(R.id.action_register_to_login);
            toast(getString(R.string.account_created));
        }
    }

    @Override
    public void timedOut(Object caller) {
        if(caller instanceof RegisterAuth) {
            hideKeyboard(binding);
            resetEditTexts();
            toast(getString(R.string.register_failed));
        }
    }
}
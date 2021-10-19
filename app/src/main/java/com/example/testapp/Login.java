package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.databinding.LoginBinding;

public class Login extends Fragment {

    private LoginBinding binding;
    private EditText username, password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = binding.loginTextboxUsername;
        password = binding.loginTextboxPassword;
        binding.loginButtonLoginbutton.setOnClickListener(this::login);
        binding.loginTextviewRegisterlink.setOnClickListener(v -> NavHostFragment
                .findNavController(Login.this)
                .navigate(R.id.action_login_to_register)
        );
    }

    public void login(View view) {
        new LoginServer(this).execute(username.getText().toString(), password.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
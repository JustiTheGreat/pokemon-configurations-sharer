package com.example.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testapp.async_tasks.LoginTask;
import com.example.testapp.R;
import com.example.testapp.databinding.FragmentLoginBinding;

public class Login extends Fragment {

    private FragmentLoginBinding binding;
    private EditText username, password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = binding.loginTextboxUsername;
        password = binding.loginTextboxPassword;
        binding.loginButtonLoginbutton.setOnClickListener(this::login);
        binding.loginTextviewRegisterlink.setOnClickListener(this::goToRegisterPage);
    }

    public void login(View view) {
        new LoginTask().execute(this, username.getText().toString(), password.getText().toString());
    }

    public void goToRegisterPage(View view) {
        NavHostFragment
                .findNavController(Login.this)
                .navigate(R.id.action_login_to_register);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testapp.databinding.RegisterBinding;

public class Register extends Fragment {

    private RegisterBinding binding;
    private EditText username, password, email;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = binding.registerTextboxUsername;
        password = binding.registerTextboxPassword;
        email = binding.registerTextboxEmail;
        binding.registerButtonRegisterbutton.setOnClickListener(this::register);
    }

    public void register(View view) {
        new RegisterServer(this).execute(username.getText().toString(), password.getText().toString(), email.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
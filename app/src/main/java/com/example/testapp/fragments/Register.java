package com.example.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testapp.async_tasks.RegisterTask;
import com.example.testapp.databinding.RegisterBinding;

public class Register extends Fragment {
    private RegisterBinding binding;
    private EditText username, email, password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = binding.registerTextboxUsername;
        email = binding.registerTextboxEmail;
        password = binding.registerTextboxPassword;
        binding.registerButtonRegisterbutton.setOnClickListener(this::register);
    }

    public void register(View view) {
        new RegisterTask().execute(this, username.getText().toString(), email.getText().toString(), password.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
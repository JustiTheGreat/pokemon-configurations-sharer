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

import com.google.firebase.auth.FirebaseAuth;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentRegisterBinding;

public class Register extends UtilityFragment {
    private FragmentRegisterBinding binding;
    private EditText email, password;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        email = binding.registerEmailTB;
        password = binding.registerPasswordTB;
        mAuth = FirebaseAuth.getInstance();
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

    private void registerButtonListener(View view){
        if (email.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_EMAIL);
            return;
        }
        if (password.getText().toString().isEmpty()) {
            toast(PLEASE_INPUT_YOUR_PASSWORD);
            return;
        }
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this.requireActivity(), task -> {
                    hideKeyboard(binding);
                    if (task.isSuccessful()) {
                        navigateTo(R.id.action_register_to_login);
                        toast(ACCOUNT_CREATED);
                    } else {
                        resetEditTexts();
                        toast(REGISTER_FAILED);
                    }
                });
    }
}
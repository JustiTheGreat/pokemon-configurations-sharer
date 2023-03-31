package app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mypokemoncollection.R;
import com.mypokemoncollection.databinding.FragmentLoginBinding;

public class Login extends Fragment {
    private FragmentLoginBinding binding;
    private EditText email, password;
    private FirebaseAuth mAuth;
    private final Fragment THIS = this;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = binding.loginTextboxEmail;
        password = binding.loginTextboxPassword;

        binding.loginButtonLoginbutton.setOnClickListener(v -> mAuth
                .signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        NavHostFragment
                                .findNavController(THIS)
                                .navigate(R.id.action_login_to_collection);
                    } else {
                        email.setText("");
                        password.setText("");
                        Toast.makeText(THIS.getActivity(), "Wrong credentials!", Toast.LENGTH_SHORT).show();
                    }
                }));
        binding.loginTextviewRegisterlink.setOnClickListener(v -> NavHostFragment
                .findNavController(Login.this)
                .navigate(R.id.action_login_to_register));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.action_login_to_collection);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
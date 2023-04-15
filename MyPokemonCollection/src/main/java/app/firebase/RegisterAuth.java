package app.firebase;

import com.google.firebase.auth.FirebaseAuth;

import app.ui.fragments.ICallbackContext;

public class RegisterAuth {
    private final ICallbackContext callbackContext;
    private final String email;
    private final String password;

    public RegisterAuth(ICallbackContext callbackContext, String email, String password) {
        this.callbackContext = callbackContext;
        this.email = email;
        this.password = password;
    }

    public void execute() {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> callbackContext.callback(this, null))
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}

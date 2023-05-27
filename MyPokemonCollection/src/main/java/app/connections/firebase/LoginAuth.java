package app.connections.firebase;

import com.google.firebase.auth.FirebaseAuth;

import app.ui.fragments.ICallbackContext;

public class LoginAuth {
    
    private final ICallbackContext callbackContext;
    private final String email;
    private final String password;

    public LoginAuth(ICallbackContext callbackContext, String email, String password) {
        this.callbackContext = callbackContext;
        this.email = email;
        this.password = password;
    }

    public void execute() {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> callbackContext.callback(this, null))
                .addOnFailureListener(task -> callbackContext.timedOut(this));
    }
}

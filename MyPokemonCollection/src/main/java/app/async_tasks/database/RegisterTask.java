package app.async_tasks.database;

import static android.content.ContentValues.TAG;
import static app.constants.StringConstants.REGISTER_PROBLEMS;
import static app.constants.StringConstants.REGISTER_SUCCESS;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import app.constants.collection_fields.Users;

public class RegisterTask extends AsyncTask<String, String, String> {
    private final ICallbackContext callbackContext;

    public RegisterTask(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @Override
    protected String doInBackground(String... params) {
        AtomicReference<String> result = new AtomicReference<>();

        Map<String, Object> user = new HashMap<>();
        user.put(Users.USERNAME, params[0]);
        user.put(Users.EMAIL, params[1]);
        user.put(Users.PASSWORD, params[2]);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    result.set(REGISTER_SUCCESS);
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    result.set(REGISTER_PROBLEMS);
                });
        return result.get();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.isEmpty()) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}

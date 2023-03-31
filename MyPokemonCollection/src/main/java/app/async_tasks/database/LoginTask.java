package app.async_tasks.database;

import static android.content.ContentValues.TAG;
import static app.constants.StringConstants.LOGIN_PROBLEMS;
import static app.constants.StringConstants.LOGIN_SUCCESS;
import static app.constants.collection_fields.Users.PASSWORD;
import static app.constants.collection_fields.Users.USERNAME;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class LoginTask extends AsyncTask<String,String,String> {
    private final ICallbackContext callbackContext;

    public LoginTask(ICallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    @Override
    protected String doInBackground(String... params) {
        AtomicReference<String> result = new AtomicReference<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo(USERNAME, params[0])
                .whereEqualTo(PASSWORD, params[1])
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        result.set(LOGIN_SUCCESS);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        result.set(LOGIN_PROBLEMS);
                    }
                });
        return result.get();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.isEmpty()) callbackContext.timedOut();
        else callbackContext.callback(this, result);
    }
}
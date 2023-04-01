package app.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {
    private static FirebaseAuth authInstance = null;
    private static FirebaseFirestore firestoreInstance = null;
    private Firebase(){}
    public static FirebaseAuth getAuthInstance(){
        if(authInstance == null) {
            authInstance = FirebaseAuth.getInstance();
        }
        return authInstance;
    }
    public static FirebaseFirestore getFirestoreInstance(){
        if(firestoreInstance == null) {
            firestoreInstance = FirebaseFirestore.getInstance();
        }
        return firestoreInstance;
    }
}

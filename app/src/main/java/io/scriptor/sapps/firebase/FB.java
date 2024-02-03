package io.scriptor.sapps.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class FB {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static FirebaseStorage getStorage() {
        return FirebaseStorage.getInstance();
    }
}

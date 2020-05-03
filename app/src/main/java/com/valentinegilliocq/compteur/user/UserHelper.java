package com.valentinegilliocq.compteur.user;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserHelper {
    public static Task<QuerySnapshot> checkUniquePseudo(String pseudo) {
        return FirebaseFirestore.getInstance().collection("pseudo").whereEqualTo("pseudo", pseudo).get();
    }

    public static Task<DocumentReference> createPseudo(String pseudo) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pseudo", pseudo);
        return FirebaseFirestore.getInstance().collection("pseudo").add(map);
    }
}

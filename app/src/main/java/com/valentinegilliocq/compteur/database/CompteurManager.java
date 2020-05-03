package com.valentinegilliocq.compteur.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.valentinegilliocq.compteur.compteur.Compteur;

import java.util.Map;

public class CompteurManager {

    public static CollectionReference getCollection(){
        return FirebaseFirestore.getInstance().collection("compteurs");
    }

    public static DocumentReference getCompteur(String id){
        return getCollection().document(id);
    }

    public static Task<Void> createCompteur(Compteur compteur){
        DocumentReference docRef = getCollection().document();
        compteur.setId(docRef.getId());
        return docRef.set(compteur.toMap());
    }

    public static Task<Void> updateCompteur(String id, Map map){
        return getCollection().document(id).set(map, SetOptions.merge());
    }

    public static Task<Void> deleteCompteur(String id){
        return getCollection().document(id).delete();
    }

    public static Task<Void> addPoint(Compteur compteur, String playerPseudo) {
        compteur.addPoint(playerPseudo);
        return getCollection().document(compteur.getId()).set(compteur.toMap(), SetOptions.merge());
    }
}
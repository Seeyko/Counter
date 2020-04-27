package com.valentinegilliocq.compteur.compteur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.valentinegilliocq.compteur.MainActivity;
import com.valentinegilliocq.compteur.R;
import com.valentinegilliocq.compteur.database.CompteurManager;
import com.valentinegilliocq.compteur.utils.AppUtils;

import java.util.ArrayList;

public class CreateCompteurFormActivity extends AppCompatActivity {

    ImageButton addPlayerButton;
    ImageButton removePlayerButton;
    LinearLayout listeJoueurLayout;
    FloatingActionButton validateFormButton;
    EditText joueur1;
    private String nomCompteur;
    private ArrayList<String> playersName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_compteur_form_layout);
        setupToolbar();
        setupView();

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer();
            }
        });
        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlayer();

            }
        });
        validateFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();

            }
        });
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        TypedValue colorOnPrimary = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorOnPrimary, colorOnPrimary, true);
        upArrow.setColorFilter(getResources().getColor(colorOnPrimary.resourceId), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void validateForm() {
        boolean isFormValide = true;

        EditText nomCompteurEditText = findViewById(R.id.compteur_name);
        nomCompteur = nomCompteurEditText.getText().toString();

        if (nomCompteur.isEmpty()) {
            isFormValide = false;
        }
        playersName = new ArrayList<>();
        for (int i = 0; i < listeJoueurLayout.getChildCount(); i++) {
            EditText joueuri = (EditText) listeJoueurLayout.getChildAt(i);
            String joueuriName = joueuri.getText().toString();
            if (joueuriName.isEmpty()) {
                isFormValide = false;
            }
            playersName.add(joueuriName);
            //i++
        }

        if (isFormValide)
            createCompteur();

    }

    private void createCompteur() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show(this, null, "Création du compteur");
        Compteur compteur = new Compteur(nomCompteur, playersName);
        CompteurManager.createCompteur(compteur)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Compteur crée", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erreur inconnue", Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                });
    }

    private void removePlayer() {
        if (listeJoueurLayout.getChildCount() > 1) {
            listeJoueurLayout.removeViewAt(listeJoueurLayout.getChildCount() - 1);
        } else Toast.makeText(this, "Un joueur minimum requis", Toast.LENGTH_SHORT).show();

    }

    private void addPlayer() {
        if (listeJoueurLayout.getChildCount() < 8) {
            TextView playerToAdd;
            playerToAdd = new EditText(this);
            playerToAdd.setHint("Joueur " + (listeJoueurLayout.getChildCount() + 1));
            listeJoueurLayout.addView(playerToAdd);
        } else {
            Toast.makeText(this, "Nombre maximal de joueur atteint.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupView() {
        addPlayerButton = findViewById(R.id.add_player);
        removePlayerButton = findViewById(R.id.remove_player);
        listeJoueurLayout = findViewById(R.id.liste_joueur);
        validateFormButton = findViewById(R.id.validate_form);
        joueur1 = findViewById(R.id.nom_joueur1);
        initPlayerOneName();
    }

    private void initPlayerOneName() {
        String playerOneName = AppUtils.getCreatorPseudo(this);
        if (playerOneName == null) {
            Toast.makeText(this, "Pseudo pas défini", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            joueur1.setText(playerOneName);
        }
    }
}
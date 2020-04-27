package com.valentinegilliocq.compteur.compteur;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.valentinegilliocq.compteur.R;
import com.valentinegilliocq.compteur.database.CompteurManager;
import com.valentinegilliocq.compteur.utils.AppUtils;

import java.util.List;
import java.util.Map;

import static android.content.Intent.EXTRA_UID;

public class CompteurActivity extends AppCompatActivity {

    private String compteurId;
    private View progressBar;
    private Compteur compteur;
    private CompteurFlexboxLayout playersButtonsLayout;
    private TextView compteurTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compteur);
        compteurId = getIntent().getExtras().getString(EXTRA_UID);
        this.progressBar = findViewById(R.id.progressBar);
        this.playersButtonsLayout = findViewById(R.id.players_buttons_flexbox_layout);
        this.compteurTotal = findViewById(R.id.compteur_total_text);
        compteur = null;

        setupToolbar();
        setupCompteur();
    }

    private void setupCompteur() {
        CompteurManager.getCompteur(compteurId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    if(compteur == null){
                        compteur = documentSnapshot.toObject(Compteur.class);
                        createUI();
                    }else{
                        compteur = documentSnapshot.toObject(Compteur.class);
                        updateUI();
                    }
                }
            }
        });
    }

    private void createUI() {
        AppUtils.fadeView(progressBar, View.GONE, 0F, 500);

        setTitle(compteur.getName());

        createPlayersButtons();
        updatePlayersScore();
        updateTotal();
    }

    private void updateUI() {
        updatePlayersScore();
        updateTotal();
    }

    private void createPlayersButtons() {
        //Pour chaque joueur du compteur compteur.getPlayers()
        //Crée un layout (R.id.item_player_counter = bouton pour les scores des joueurs)
        //Initialisé ce layout avec le nom du joueur : TextView (R.id.name)
        //Ajouter un click listener qui appellera la méthode addPoint()
        //Ajouter ce layout a ton flexboxlayout et à une List de layout pour les garder en mémoire et les mettres à jour
        for (Map.Entry<String, List<Timestamp>> player : compteur.getPlayers().entrySet()) {
            String playerPseudo = player.getKey();
            List<Timestamp> playerTemps = player.getValue();
            int playerScore = playerTemps.size();

            PlayerCompteurButtonView playerCompteurButtonView = new PlayerCompteurButtonView(this, playerPseudo, playerScore);
            playerCompteurButtonView.addToParent(playerCompteurButtonView);

        }
    }



    private void updatePlayersScore() {
        //Pour chaque boutons de ta List de layout (qui correspond a chaque joueur de ton compteur)
        //Mettre à jour son score : TextView (R.id.score)
        for (Map.Entry<String, List<Timestamp>> player : compteur.getPlayers().entrySet()) {
            String playerPseudo = player.getKey();
            List<Timestamp> playerTemps = player.getValue();
            int playerScore = playerTemps.size();

            PlayerCompteurButtonView playerCompteurButtonView = new PlayerCompteurButtonView(this, playerPseudo, playerScore);
//            playersButtonsLayout.updateChild(playerCompteurButtonView);
        }
    }

    private void updateTotal() {
        TextView totalScoreView = findViewById(R.id.compteur_total_text);
        totalScoreView.setText("Total : ");
    }

    private int addPoint(int nbPoint, AppCompatTextView textScore) {
        nbPoint++;
        textScore.setText(String.valueOf(nbPoint));
        Log.e("nbPoint", String.valueOf(nbPoint));
        return nbPoint;
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
}


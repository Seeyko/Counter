package com.valentinegilliocq.compteur.compteur;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.tomandrieu.utilities.SeeykoUtils;
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
    private GridView playersBoxLayout;
    private TextView compteurTotal;
    private Toolbar toolbar;
    private PlayerButtonAdapter playerButtonAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compteur);
        compteurId = getIntent().getExtras().getString(EXTRA_UID);
        this.progressBar = findViewById(R.id.progressBar);
        this.playersBoxLayout = findViewById(R.id.players_box_layout);
        this.compteurTotal = findViewById(R.id.compteur_total_text);
        compteur = null;

        setupToolbar();
        setupCompteur();
    }

    private void setupCompteur() {
        CompteurManager.getCompteur(compteurId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    if (compteur == null) {
                        compteur = documentSnapshot.toObject(Compteur.class);
                        createUI();
                    } else {
                        compteur = documentSnapshot.toObject(Compteur.class);
                        updateUI();
                    }
                }
            }
        });
    }

    private void createUI() {
        Log.e("=>", "createUI : " + compteur.toString());
        AppUtils.fadeView(progressBar, View.GONE, 0F, 500);

        toolbar.setTitle(compteur.getName());

        createPlayersButtons();
        updatePlayersScore();
        updateTotal();
    }

    private void updateUI() {
        Log.e("=>", "updateUI : " + compteur);
        updatePlayersScore();
        updateTotal();
    }

    private void createPlayersButtons() {
        //Pour chaque joueur du compteur compteur.getPlayers()
        //Crée un layout (R.id.item_player_counter = bouton pour les scores des joueurs)
        //Initialisé ce layout avec le nom du joueur : TextView (R.id.name)
        //Ajouter un click listener qui appellera la méthode addPoint()
        //Ajouter ce layout a ton flexboxlayout et à une List de layout pour les garder en mémoire et les mettres à jour
        playerButtonAdapter = new PlayerButtonAdapter(this, compteur.getPlayers());
        this.playersBoxLayout.setAdapter(playerButtonAdapter);
        this.playersBoxLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addPoint(playerButtonAdapter.getPseudo(position));
            }
        });
    }


    private void updatePlayersScore() {
        //Pour chaque boutons de ta List de layout (qui correspond a chaque joueur de ton compteur)
        //Mettre à jour son score : TextView (R.id.score)
        playerButtonAdapter.update(compteur.getPlayers());
        updateTotal();
    }

    private void updateTotal() {
        TextView totalScoreView = findViewById(R.id.compteur_total_text);
        totalScoreView.setText("Total : " + playerButtonAdapter.getScoreTotal());
    }

    private void addPoint(String playerPseudo) {
        CompteurManager.addPoint(compteur, playerPseudo);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
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


package com.valentinegilliocq.compteur.compteur;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayout;
import com.valentinegilliocq.compteur.R;

public class PlayerCompteurButtonView extends LinearLayout {

    private int playerScore;
    private String playerPseudo;

    public PlayerCompteurButtonView(Context context) {
        super(context);
    }

    public PlayerCompteurButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerCompteurButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlayerCompteurButtonView(CompteurActivity context, String playerPseudo, int playerScore) {
        super(context);
        this.playerPseudo = playerPseudo;
        this.playerScore = playerScore;
    }

    public void addToParent(View parent) {
        inflate(getContext(), R.layout.item_player_counter, (ViewGroup) parent);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PlayerCompteurButtonView) {
            return getPlayerName()
                    .equals(((PlayerCompteurButtonView) obj).getPlayerName());
        } else {
            return super.equals(obj);
        }
    }

    public void setPlayerName(String playerName) {
        ((TextView) findViewById(R.id.name)).setText(playerName);
        playerPseudo = playerName;
    }

    public void setPlayerScore(int score) {
        playerScore = score;
        ((TextView) findViewById(R.id.score)).setText(String.valueOf(score));
    }

    public void update(){
        setPlayerName(playerPseudo);
        setPlayerScore(playerScore);
    }

    public void update(PlayerCompteurButtonView view) {
        setPlayerName(view.getPlayerName());
        setPlayerScore(view.getPlayerScore());
    }

    private int getPlayerScore() {
        return playerScore;
    }

    private String getPlayerName() {
        return playerPseudo;
    }
}

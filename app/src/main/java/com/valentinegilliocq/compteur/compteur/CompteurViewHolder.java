package com.valentinegilliocq.compteur.compteur;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.valentinegilliocq.compteur.R;

public class CompteurViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    private final TextView compteurDate;
    private final TextView compteurName;
    private final TextView compteurPlayers;

    public CompteurViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.compteurDate = itemView.findViewById(R.id.compteur_date);
        this.compteurName = itemView.findViewById(R.id.compteur_name);
        this.compteurPlayers = itemView.findViewById(R.id.compteur_players);
    }

    public void bind(final Context mContext, final Compteur compteur) {
        this.compteurDate.setText(compteur.getDateAsString());
        this.compteurName.setText(compteur.getName());
        this.compteurPlayers.setText(compteur.getPlayersPseudo());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CompteurActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_UID, compteur.getId());
                mContext.startActivity(intent);
            }
        });
    }
}

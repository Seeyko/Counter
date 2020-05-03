package com.valentinegilliocq.compteur.compteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.valentinegilliocq.compteur.R;
import com.valentinegilliocq.compteur.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerButtonAdapter extends BaseAdapter {

    ArrayList<String> playersPseudo;
    ArrayList<List<Timestamp>> playersTimeStamp;
    Context mContext;

    public PlayerButtonAdapter(Context context, HashMap<String, List<Timestamp>> players) {
        mContext = context;

        playersPseudo = new ArrayList<>();
        playersTimeStamp = new ArrayList<>();

        for(Map.Entry<String, List<Timestamp>> entry: players.entrySet()){
            playersPseudo.add(entry.getKey());
            playersTimeStamp.add(entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return playersPseudo.size();
    }

    @Override
    public HashMap<String, List<Timestamp>> getItem(int position) {
        HashMap<String, List<Timestamp>> player = new HashMap<>();
        player.put(playersPseudo.get(position), playersTimeStamp.get(position));
        return player;
    }

    public String getPseudo(int pos){
        return playersPseudo.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return playersPseudo.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).
                inflate(R.layout.item_player_counter, parent, false);

        TextView scoreTv = convertView.findViewById(R.id.score);
        TextView pseudoTv = convertView.findViewById(R.id.name);
        scoreTv.setText(playersPseudo.get(position));
        pseudoTv.setText(String.valueOf(playersTimeStamp.get(position).size()));
        return convertView;
    }

    public int getScoreTotal() {
        int total = 0;
        for(List<Timestamp> timestampList : playersTimeStamp){
            total += timestampList.size();
        }
        return total;
    }

    public void update(HashMap<String, List<Timestamp>> players) {
        for(Map.Entry<String, List<Timestamp>> entry: players.entrySet()){
            int pos = playersPseudo.indexOf(entry.getKey());
            if(pos != -1) {
                playersTimeStamp.set(pos, entry.getValue());
            }
        }
        notifyDataSetChanged();
    }

}

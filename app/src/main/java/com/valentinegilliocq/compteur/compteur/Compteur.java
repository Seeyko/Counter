package com.valentinegilliocq.compteur.compteur;

import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.Timestamp;
import com.valentinegilliocq.compteur.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.valentinegilliocq.compteur.utils.AppUtils.getDate;

public class Compteur {

    private String id;
    private String name;
    //Map avec en clé :  le pseudo d'un utilisateur et sa valeur et la liste des temps
    // auxquels il a compté ses points
    //Sur ce compteur.
    private HashMap<String, List<Timestamp>> players;
    private String creatorPseudo;
    private long lastModified;

    public Compteur(){

    }
    public Compteur(String nomCompteur, ArrayList<String> playersName) {
        this.name = nomCompteur;
        this.players = new HashMap<>();
        for(int i = 0; i < playersName.size(); i++){
            this.players.put(playersName.get(i), new ArrayList<Timestamp>());
        }
        this.creatorPseudo = playersName.get(0);
        this.lastModified = Timestamp.now().getSeconds();
    }

    //region Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, List<Timestamp>> getPlayers() {
        return players;
    }

    public int getTotal() {
        int total = 0;
        for (Map.Entry<String, List<Timestamp>> entry : getPlayers().entrySet()) {
            total += entry.getValue().size();
        }
        return total;
    }

    public String getCreatorPseudo() {
        return creatorPseudo;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(HashMap<String, List<Timestamp>> players) {
        this.players = players;
    }

    public void setCreatorPseudo(String creatorPseudo) {
        this.creatorPseudo = creatorPseudo;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, Object> toMap() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("id", getId());
        map.put("name", getName());
        map.put("players", getPlayers());
        map.put("creatorPseudo", getCreatorPseudo());
        map.put("lastModified", Timestamp.now().getSeconds());
        return map;
    }

    public String getPlayersPseudo() {
        String playersPseudo = "";
        for(Map.Entry<String, List<Timestamp>> entry : players.entrySet()){
            playersPseudo += entry.getKey();
            playersPseudo += ", ";
        }
        return playersPseudo.isEmpty() ? "" : playersPseudo.substring(0, playersPseudo.length()-2);
    }

    public String getDateAsString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(getDate(this.lastModified));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Compteur){
            return this.id != null && ((Compteur) obj).getId() != null && this.id.equals(((Compteur) obj).getId());
        }else{
            return super.equals(obj);
        }
    }

    //endregion

}

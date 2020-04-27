package com.valentinegilliocq.compteur.compteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valentinegilliocq.compteur.R;

import java.util.List;

public class CompteurAdapter extends RecyclerView.Adapter<CompteurViewHolder> {
    private List<Compteur> list;
    private Context mContext;

    public CompteurAdapter(Context context, List<Compteur> list) {
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CompteurViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compteur, parent, false);
        return new CompteurViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteurViewHolder holder, int position) {
        holder.bind(mContext, list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean update(Compteur compteur) {
        int position = list.indexOf(compteur);
        if(position != -1){
            list.set(position, compteur);
            notifyItemChanged(position);
            return true;
        }else{
            return false;
        }
    }

    public boolean remove(Compteur compteur) {
        int positionToRemoved = list.indexOf(compteur);
        Compteur removed;
        if(positionToRemoved != -1){
            removed = list.remove(positionToRemoved);
        }
        notifyItemRemoved(positionToRemoved);
        return positionToRemoved != -1;
    }

    public void add(Compteur compteur) {
        list.add(compteur);
        notifyDataSetChanged();
    }
}

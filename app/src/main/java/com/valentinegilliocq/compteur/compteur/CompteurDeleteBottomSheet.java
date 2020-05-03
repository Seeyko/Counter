package com.valentinegilliocq.compteur.compteur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valentinegilliocq.compteur.R;
import com.valentinegilliocq.compteur.database.CompteurManager;

public class CompteurDeleteBottomSheet  extends BottomSheetDialogFragment {

    private Compteur compteur;

    public CompteurDeleteBottomSheet(Compteur compteur) {
        this.compteur = compteur;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compteur_list_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View deleteChat = view.findViewById(R.id.delete_compteur);
        ((TextView) view.findViewById(R.id.delete_text)).setText(getActivity().getString(R.string.action_delete_compteur));
        deleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompteurManager.deleteCompteur(compteur.getId());
                dismiss();
            }
        });
    }
}

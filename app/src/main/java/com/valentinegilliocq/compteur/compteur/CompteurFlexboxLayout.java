package com.valentinegilliocq.compteur.compteur;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class CompteurFlexboxLayout extends FlexboxLayout {
    private ArrayList<PlayerCompteurButtonView> playerButtonsList;

    public CompteurFlexboxLayout(Context context) {
        super(context);
        this.playerButtonsList = new ArrayList<>();
    }

    public CompteurFlexboxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.playerButtonsList = new ArrayList<>();
    }

    public CompteurFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.playerButtonsList = new ArrayList<>();
    }

    @Override
    public void addView(View child) {
        if(child instanceof PlayerCompteurButtonView){
            ((PlayerCompteurButtonView) child).update();
            this.playerButtonsList.add((PlayerCompteurButtonView) child);
            super.addView(child);
            Log.e("=>", "add child");
        }else{
            throw new RuntimeException("Cannot add other view than PlayerCompteurButtonView in CompteurFlexboxLayout");
        }
    }

    @Override
    public PlayerCompteurButtonView getChildAt(int index) {
        return (PlayerCompteurButtonView) super.getChildAt(index);
    }

    public void updateChild(PlayerCompteurButtonView view){
        getChildAt(playerButtonsList.indexOf(view)).update(view);
    }

}

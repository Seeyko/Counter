package com.valentinegilliocq.compteur.compteur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.valentinegilliocq.compteur.R;
import com.valentinegilliocq.compteur.utils.AppUtils;

import java.util.Calendar;

public class CompteurViewHolder extends RecyclerView.ViewHolder {
    private final View view;
    private final TextView compteurDate;
    private final TextView compteurName;
    private final TextView compteurPlayers;
    private final TextView compteurTotal;

    public CompteurViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.compteurDate = itemView.findViewById(R.id.compteur_date);
        this.compteurName = itemView.findViewById(R.id.compteur_name);
        this.compteurPlayers = itemView.findViewById(R.id.compteur_players);
        this.compteurTotal = itemView.findViewById(R.id.compteur_total);
    }

    public void bind(final AppCompatActivity mActivity, final Compteur compteur) {
        this.compteurDate.setText(compteur.getDateAsString());
        this.compteurName.setText(compteur.getName());
        this.compteurPlayers.setText(compteur.getPlayersPseudo());
        this.compteurTotal.setText("\u2022 " + compteur.getTotal());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CompteurActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_UID, compteur.getId());
                mActivity.startActivity(intent);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CompteurDeleteBottomSheet bottomSheetFragment = new CompteurDeleteBottomSheet(compteur);
                bottomSheetFragment.show(mActivity.getSupportFragmentManager(), bottomSheetFragment.getTag());
                return true;
            }
        });

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(Timestamp.now().toDate());
        cal2.setTime(AppUtils.getDate(compteur.getLastModified()));
        boolean sameDay = AppUtils.sameDay(cal1, cal2);

        if (sameDay) {
            int hours = cal2.get(Calendar.HOUR_OF_DAY);
            int minutes = cal2.get(Calendar.MINUTE);
            String hoursString = String.valueOf(hours);
            String minutesString = String.valueOf(minutes);
            if (hours < 10) {
                hoursString = "0" + hoursString;
            }
            if (minutes < 10) {
                minutesString = "0" + minutesString;
            }
            compteurDate.setText(hoursString + ":" + minutesString);
        } else {
            Calendar previousWeek = Calendar.getInstance();
            previousWeek.setTime(Timestamp.now().toDate());
            previousWeek.add(Calendar.DAY_OF_YEAR, -7);
            Log.e("previousWeek", previousWeek.toString());
            Log.e("currentDate", cal2.toString());
            boolean sameWeek = AppUtils.sameWeek(previousWeek, cal2);
            Log.e("=>", "sameweek : " + sameWeek);
            if (sameWeek) {
                compteurDate.setText(AppUtils.firstCharUppercase(AppUtils.getDayFromTimestamp(cal2)));
            } else {
                compteurDate.setText(cal2.get(Calendar.DAY_OF_MONTH) + " " + AppUtils.firstCharUppercase(AppUtils.getMonth(cal2.get(Calendar.MONTH))));
            }
            //dateTextView.setText(Utils.firstCharUppercase(Utils.getDayFromTimestamp(cal2)));

        }
    }
}

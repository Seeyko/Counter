package com.valentinegilliocq.compteur.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tomandrieu.utilities.SeeykoUtils;
import com.valentinegilliocq.compteur.MainActivity;
import com.valentinegilliocq.compteur.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtils {

    public static void closeApplication(AppCompatActivity appCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appCompatActivity.finishAndRemoveTask();
        } else {
            appCompatActivity.finishAffinity();
        }
    }


    public static String getCreatorPseudo(Context context) {
        return context.getSharedPreferences(MainActivity.USER_PREF, Context.MODE_PRIVATE)
                .getString(MainActivity.USER_PSEUDO_KEY, null);
    }

    /**
     * @param view         View to animate
     * @param toVisibility Visibility at the end of animation
     * @param toAlpha      Alpha at the end of animation
     * @param duration     Animation duration in ms
     */
    public static void fadeView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(view.getAlpha() > 0f ? 1f : 0);
            view.setVisibility(View.VISIBLE);
        } else {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

    public static LinearLayout getPlayerButton(Context context, String playerPseudo, int playerScore) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView playerNameTextView = new TextView(context);
        TextView playerScoreTextView = new TextView(context);
        playerNameTextView.setText(playerPseudo);
        playerScoreTextView.setText(String.valueOf(playerScore));

        linearLayout.addView(playerNameTextView);
        linearLayout.addView(playerScoreTextView);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SeeykoUtils.pixelsInDp(200, context));
        linearLayout.setLayoutParams(layoutParams);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        linearLayout.setBackground(context.getResources().getDrawable(outValue.resourceId));
        return linearLayout;
    }

    public static Date getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static String getDayFromTimestamp(Calendar calendar) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        return dayFormat.format(calendar.getTime());
    }
    /**
     * @param cal1
     * @param cal2
     * @return true if the two date are in the same day
     */
    public static boolean sameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    /**
     * @param cal1 oldest date
     * @param cal2 recent date
     * @return true if the two date are the same week
     */
    public static boolean sameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static String firstCharUppercase(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        } else {
            return str;
        }
    }

}

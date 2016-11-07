package net.coronite.quizlet_math_plus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Utility {

    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.username_key), context.getString(R.string.default_username));
    }

    public static Boolean getShowTermBoolean(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.show_term_key), true);
    }
}

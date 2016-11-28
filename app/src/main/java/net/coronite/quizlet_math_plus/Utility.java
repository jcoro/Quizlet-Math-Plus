package net.coronite.quizlet_math_plus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Locale;


public class Utility {

    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.username_key), null);
    }

    static void setUsername(Context context, String username){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    static Boolean getIsFirstRun(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("isFirstRun", true);
    }

    static void setIsFirstRun(Context context, Boolean isFirstRun){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstRun", isFirstRun);
        editor.apply();
    }

    static Boolean getShowTermBoolean(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.show_term_key), true);
    }

    /**
     * @param view         View to animate
     * @param toVisibility Visibility at the end of animation
     * @param toAlpha      Alpha at the end of animation
     * @param duration     Animation duration in ms
     */
    static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
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

    public static String getCreatedByString(Context context, String string ) {
        int formatId = R.string.created_by;
        return String.format(Locale.US, context.getString(formatId), string);
    }

    static String getShareString(Context context, String term, String definition ) {
        int formatId = R.string.share_string;
        return String.format(Locale.US, context.getString(formatId), term, definition);
    }
}

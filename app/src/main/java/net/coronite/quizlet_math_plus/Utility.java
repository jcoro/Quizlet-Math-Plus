package net.coronite.quizlet_math_plus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Locale;


public class Utility {

    /**
     * Returns the username from SharedPreferences
     * @param context the context
     * @return the username
     */
    public static String getUsername(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.username_key), null);
    }

    /**
     * Sets the username in SharedPreferences
     * @param context the context
     * @param username the username to set
     */
    static void setUsername(Context context, String username){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    /**
     * Tests if the app is running for the first time (used to prompt for a username).
     * @param context The context
     * @return Returns a boolean indicating if the app is running for the first time.
     */
    static Boolean getIsFirstRun(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("isFirstRun", true);
    }

    /**
     * Sets a boolean in SharedPreferences indicating if the app is running for the first time.
     * @param context the context
     * @param isFirstRun the boolean indicating if the app is running for the first time.
     */
    static void setIsFirstRun(Context context, Boolean isFirstRun){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstRun", isFirstRun);
        editor.apply();
    }

    /**
     * Gets a boolean from SharedPreferences indicating if the user wants the flashcard TERM to be visible
     * with the DEFINITION hidden.
     * @param context the context
     * @return the boolean indicating the user's preference.
     */
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

    /**
     * Get the creator of the flash card set and prepend "Created By: " to it
     * (shown in the MainActivity RecyclerView lists).
     * @param context the context
     * @param string the username string
     * @return the "Created By: " string
     */
    public static String getCreatedByString(Context context, String string ) {
        int formatId = R.string.created_by;
        return String.format(Locale.US, context.getString(formatId), string);
    }

    /**
     * Create a String for sharing the current flash card via text/email.
     * @param context the context
     * @param term the Term being shared
     * @param definition The Definition being shared
     * @return The term and definition as a string.
     */
    static String getShareString(Context context, String term, String definition ) {
        int formatId = R.string.share_string;
        return String.format(Locale.US, context.getString(formatId), term, definition);
    }
}

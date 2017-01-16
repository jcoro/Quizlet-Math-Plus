package net.coronite.quizlet_math_plus.callback;

/**
 * An Interface implemented by {@code UserSetFragment}, {@code UserStudiedFragment}, and
 * {@code MainActivity} which allows the fragments to signal their containing Activity when
 * the data is downloaded.
 *
 * It is used to dismiss the overlay in the MainActivity when the data download is done.
 *
 * Note that because two fragments are sending the callback, the dataIsLoaded() method
 * in the MainActivity is synchronized.
 */

public interface DataLoadedCallback {
    void dataIsLoaded();
}

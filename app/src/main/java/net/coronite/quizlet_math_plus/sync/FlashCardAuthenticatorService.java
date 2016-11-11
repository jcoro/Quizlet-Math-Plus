package net.coronite.quizlet_math_plus.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FlashCardAuthenticatorService  extends Service {
    private FlashCardAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new FlashCardAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

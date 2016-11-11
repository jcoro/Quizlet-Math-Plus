package net.coronite.quizlet_math_plus.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class FlashCardSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FlashCardSyncAdapter sFlashCardSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sFlashCardSyncAdapter == null) {
                sFlashCardSyncAdapter = new FlashCardSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

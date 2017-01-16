package net.coronite.quizlet_math_plus.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * The service necessary for running the {@code FlashCardSyncAdapter}
 * The lock ensures that multiple SyncAdapters aren't created.
 */
public class FlashCardSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static FlashCardSyncAdapter sFlashCardSyncAdapter = null;

    @Override
    public void onCreate() {
        //Log.d("FlashCardSyncService", "onCreate - FlashCardSyncService");
        synchronized (sSyncAdapterLock) {
            if (sFlashCardSyncAdapter == null) {
                sFlashCardSyncAdapter = new FlashCardSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return sFlashCardSyncAdapter.getSyncAdapterBinder();
    }
}

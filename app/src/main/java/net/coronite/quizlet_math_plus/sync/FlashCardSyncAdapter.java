package net.coronite.quizlet_math_plus.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import net.coronite.quizlet_math_plus.R;
import net.coronite.quizlet_math_plus.Utility;
import net.coronite.quizlet_math_plus.data.FlashCardContract;
import net.coronite.quizlet_math_plus.data.QuizletSetsAPI;
import net.coronite.quizlet_math_plus.data.models.Set;
import net.coronite.quizlet_math_plus.data.models.SetList;
import net.coronite.quizlet_math_plus.data.models.StudiedSet;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlashCardSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 30; //60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int FLASHCARD_NOTIFICATION_ID = 3004;


    private static final String[] SET_PROJECTION = new String[]{
            FlashCardContract.SetEntry.COLUMN_SET_ID,
            FlashCardContract.SetEntry.COLUMN_SET_STUDIED,
            FlashCardContract.SetEntry.COLUMN_SET_URL,
            FlashCardContract.SetEntry.COLUMN_SET_TITLE
    };

    // these indices must match the projection
    private static final int INDEX_COLUMN_SET_ID = 0;
    private static final int INDEX_SET_STUDIED = 1;
    private static final int INDEX_SET_URL = 2;
    private static final int INDEX_SET_TITLE = 3;

    List<Set> mUserSets;
    List<StudiedSet> mStudiedSets;

    public FlashCardSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        Log.d("SYNC_ADAPTER", "Starting sync");
        String username = Utility.getUsername(getContext());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.quizlet.com/2.0/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        QuizletSetsAPI quizletSetsAPI = retrofit.create(QuizletSetsAPI.class);

        Call<SetList> call = quizletSetsAPI.loadSets(username);
        try {
            SetList setList = (SetList) call.execute().body();
            mUserSets = setList.getSets();
            mStudiedSets = setList.getStudiedSets();
            Log.v( "M_USER_SETS", mUserSets.get(0).getQuizletSetId() );
            Log.v( "M_STUDIED_SETS", mStudiedSets.get(0).getSet().getQuizletSetId() );
        } catch(Exception e) {
            Log.e("IOException", e.toString());
        }

        // delete old data so we don't build up an endless history
        getContext().getContentResolver().delete( FlashCardContract.SetEntry.CONTENT_URI, null, null );

        Vector<ContentValues> cVVector = new Vector<ContentValues>(mUserSets.size());

        for (Set set : mUserSets) {
            ContentValues setValues = new ContentValues();
            setValues.put(FlashCardContract.SetEntry.COLUMN_SET_ID, set.getQuizletSetId());
            setValues.put(FlashCardContract.SetEntry.COLUMN_SET_STUDIED, 0);
            setValues.put(FlashCardContract.SetEntry.COLUMN_SET_URL, set.getUrl());
            setValues.put(FlashCardContract.SetEntry.COLUMN_SET_TITLE, set.getTitle());

            cVVector.add(setValues);
        }

        for (StudiedSet studiedSet : mStudiedSets) {
            ContentValues studiedSetValues = new ContentValues();
            studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_ID, studiedSet.getSet().getQuizletSetId());
            studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_STUDIED, 1);
            studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_URL, studiedSet.getSet().getUrl());
            studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_TITLE, studiedSet.getSet().getTitle());

            cVVector.add(studiedSetValues);
        }

        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(FlashCardContract.SetEntry.CONTENT_URI, cvArray);

        }
        Log.d("SYNC ADAPTER", "Sync Complete. " + cVVector.size() + " Inserted");
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.d("syncImmediately", "syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
        Log.d(".requestSync", ".requestSync");
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(newAccount, context.getString(R.string.content_authority), 1);
             * here.
             */
            ContentResolver.setIsSyncable(newAccount, context.getString(R.string.content_authority), 1);
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        FlashCardSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }


    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}

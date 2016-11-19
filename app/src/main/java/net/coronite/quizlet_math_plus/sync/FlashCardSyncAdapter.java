package net.coronite.quizlet_math_plus.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import net.coronite.quizlet_math_plus.MainActivity;
import net.coronite.quizlet_math_plus.R;
import net.coronite.quizlet_math_plus.Utility;
import net.coronite.quizlet_math_plus.data.FlashCardContract;
import net.coronite.quizlet_math_plus.data.QuizletSetsAPI;
import net.coronite.quizlet_math_plus.data.QuizletTermsAPI;
import net.coronite.quizlet_math_plus.data.models.Set;
import net.coronite.quizlet_math_plus.data.models.SetList;
import net.coronite.quizlet_math_plus.data.models.StudiedSet;
import net.coronite.quizlet_math_plus.data.models.Term;
import net.coronite.quizlet_math_plus.data.models.TermList;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlashCardSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with quizlet, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 60 * 24;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    List<Set> mUserSets;
    List<StudiedSet> mStudiedSets;
    List<Term> mTermList;
    List<Term> mStudiedTermList;

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
            SetList setList = call.execute().body();
            mUserSets = setList.getSets();
            mStudiedSets = setList.getStudiedSets();
        } catch(Exception e) {
            Log.e("IOException", e.toString());
        }

        // delete old set and term data so we don't build up an endless history
        getContext().getContentResolver().delete( FlashCardContract.SetEntry.CONTENT_URI, null, null );
        getContext().getContentResolver().delete( FlashCardContract.TermEntry.CONTENT_URI, null, null );

        Vector<ContentValues> totalSetsVector;
        int totalNumOfSets = 0;
        if (mUserSets != null){
            totalNumOfSets = mUserSets.size();
        }
        if (mStudiedSets != null){
            totalNumOfSets = totalNumOfSets + mStudiedSets.size();
        }
        if (mStudiedSets != null || mUserSets != null) {
            totalSetsVector = new Vector<>(totalNumOfSets);

            if (mUserSets != null) {

                for (Set set : mUserSets) {
                    ContentValues setValues = new ContentValues();
                    setValues.put(FlashCardContract.SetEntry.COLUMN_SET_ID, set.getQuizletSetId());
                    setValues.put(FlashCardContract.SetEntry.COLUMN_SET_STUDIED, 0);
                    setValues.put(FlashCardContract.SetEntry.COLUMN_SET_URL, set.getUrl());
                    setValues.put(FlashCardContract.SetEntry.COLUMN_SET_TITLE, set.getTitle());
                    setValues.put(FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY, set.getCreatedBy());
                    totalSetsVector.add(setValues);
                    Log.d("US CREATED_BY: ", set.getCreatedBy());

                    retrofit = new Retrofit.Builder()
                            .baseUrl(QuizletTermsAPI.ENDPOINT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    QuizletTermsAPI service = retrofit.create(QuizletTermsAPI.class);
                    Call<TermList> call2 = service.getFeed(set.getQuizletSetId());
                    try {
                        mTermList = call2.execute().body().terms;
                    } catch (Exception e) {
                        Log.e("IOException", e.toString());
                    }

                    Vector<ContentValues> termsVector = new Vector<>(mTermList.size());
                    for (Term term : mTermList) {
                        ContentValues termValues = new ContentValues();
                        termValues.put(FlashCardContract.TermEntry.COLUMN_SET_ID, set.getQuizletSetId());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_TERM, term.getTerm());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_DEFINITION, term.getDefinition());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_IMAGE, term.getImage());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_RANK, term.getRank());
                        termsVector.add(termValues);
                    }
                    // add to database
                    if (termsVector.size() > 0) {
                        ContentValues[] termsCvArray = new ContentValues[termsVector.size()];
                        termsVector.toArray(termsCvArray);
                        getContext().getContentResolver().bulkInsert(FlashCardContract.TermEntry.CONTENT_URI, termsCvArray);
                    }
                    Log.d("SYNC ADAPTER", termsVector.size() + " USER TERMS Inserted");
                }
            }

            if (mStudiedSets != null) {
                for (StudiedSet studiedSet : mStudiedSets) {
                    ContentValues studiedSetValues = new ContentValues();
                    studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_ID, studiedSet.getSet().getQuizletSetId());
                    studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_STUDIED, 1);
                    studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_URL, studiedSet.getSet().getUrl());
                    studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_TITLE, studiedSet.getSet().getTitle());
                    studiedSetValues.put(FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY, studiedSet.getSet().getCreatedBy());
                    totalSetsVector.add(studiedSetValues);
                    Log.d("USS CREATED_BY: ", studiedSet.getSet().getCreatedBy());

                    retrofit = new Retrofit.Builder()
                            .baseUrl(QuizletTermsAPI.ENDPOINT)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    QuizletTermsAPI studiedsService = retrofit.create(QuizletTermsAPI.class);
                    Call<TermList> call3 = studiedsService.getFeed(studiedSet.getSet().getQuizletSetId());
                    try {
                        mStudiedTermList = call3.execute().body().terms;
                    } catch (Exception e) {
                        Log.e("IOException", e.toString());
                    }

                    Vector<ContentValues> studiedTermsVector = new Vector<>(mStudiedTermList.size());
                    for (Term studiedTerm : mStudiedTermList) {
                        ContentValues termValues = new ContentValues();
                        termValues.put(FlashCardContract.TermEntry.COLUMN_SET_ID, studiedSet.getSet().getQuizletSetId());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_TERM, studiedTerm.getTerm());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_DEFINITION, studiedTerm.getDefinition());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_IMAGE, studiedTerm.getImage());
                        termValues.put(FlashCardContract.TermEntry.COLUMN_RANK, studiedTerm.getRank());
                        studiedTermsVector.add(termValues);
                    }
                    // add to database
                    if (studiedTermsVector.size() > 0) {
                        ContentValues[] studiedTermsCvArray = new ContentValues[studiedTermsVector.size()];
                        studiedTermsVector.toArray(studiedTermsCvArray);
                        getContext().getContentResolver().bulkInsert(FlashCardContract.TermEntry.CONTENT_URI, studiedTermsCvArray);
                    }

                    Log.d("SYNC ADAPTER", studiedTermsVector.size() + " STUDIED TERMS Inserted");
                }

                // add to database
                if (totalSetsVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[totalSetsVector.size()];
                    totalSetsVector.toArray(cvArray);
                    getContext().getContentResolver().bulkInsert(FlashCardContract.SetEntry.CONTENT_URI, cvArray);

                }
            }
            getContext().sendBroadcast(new Intent(MainActivity.ACTION_FINISHED_SYNC));
            Log.d("SYNC ADAPTER", "Sync Complete. " + totalSetsVector.size() + " SETS Inserted");
        }

    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.d("SYNC ADAPTER", "CONFIG PERIODIC SYNC");
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
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
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
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
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

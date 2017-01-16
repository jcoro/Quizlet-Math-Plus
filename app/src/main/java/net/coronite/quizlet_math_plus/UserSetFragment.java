package net.coronite.quizlet_math_plus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coronite.quizlet_math_plus.adapters.SetCursorAdapter;
import net.coronite.quizlet_math_plus.callback.DataLoadedCallback;
import net.coronite.quizlet_math_plus.data.FlashCardContract;

/**
 * A Fragment for displaying the list of flashcard sets created by the user.
 */
public class UserSetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, DataLoadedCallback {

    @Override
    public void dataIsLoaded(){

    }

    private static final int SET_LOADER = 1;
    private static IntentFilter syncIntentFilter = new IntentFilter(MainActivity.ACTION_FINISHED_SYNC);
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            restartLoaderFromBroadcast();
        }
    };

    private static final String[] SET_COLUMNS = new String[]{
            FlashCardContract.SetEntry.ID,
            FlashCardContract.SetEntry.COLUMN_SET_ID,
            FlashCardContract.SetEntry.COLUMN_SET_STUDIED,
            FlashCardContract.SetEntry.COLUMN_SET_URL,
            FlashCardContract.SetEntry.COLUMN_SET_TITLE,
            FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY
    };

    // these indices must match the projection
    @SuppressWarnings("unused")
    public static final int INDEX_COLUMN_AUTO_ID = 0;
    public static final int INDEX_COLUMN_SET_ID = 1;
    @SuppressWarnings("unused")
    public static final int INDEX_COLUMN_SET_STUDIED = 2;
    public static final int INDEX_COLUMN_SET_URL = 3;
    public static final int INDEX_COLUMN_SET_TITLE = 4;
    public static final int INDEX_COLUMN_SET_CREATED_BY = 5;

    private SetCursorAdapter mAdapter;
    private TextView mEmptyView;
    private RecyclerView mSetRecyclerView;

    public UserSetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_set, container, false);

        mSetRecyclerView = (RecyclerView) view.findViewById(R.id.user_set_recycler_view);
        mSetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SetCursorAdapter(getActivity(), null);
        mSetRecyclerView.setAdapter(mAdapter);
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SET_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(SET_LOADER, null, this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(syncBroadcastReceiver, syncIntentFilter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(syncBroadcastReceiver);
        super.onPause();
    }

    /**
     * Method to restart loader via a broadcast from the sync adapter.
     */
    private void restartLoaderFromBroadcast(){
        //Log.d( "US BROADCAST RECEIVED", "US BROADCAST RECEIVED" );
        getLoaderManager().restartLoader(SET_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FlashCardContract.SetEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),     // context
                uri,               // uri
                SET_COLUMNS,       // projection
                "set_studied=?",   // selection
                new String[]{"0"}, // selectionArgs
                null               // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            // Use swapCursor() with a CursorLoader as per:
            // https://developer.android.com/guide/components/loaders.html
            //Log.d( "US CURSOR RETURNED", Integer.toString(data.getCount()) );
            mAdapter.swapCursor(data);
            if (mSetRecyclerView.getVisibility() == View.GONE){
                mSetRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
        } else {
            //Log.d( "US CURSOR EMPTY", "CURSOR EMPTY" );
            mSetRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        ((DataLoadedCallback) getActivity()).dataIsLoaded();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}

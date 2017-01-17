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
 * A Fragment for displaying the list of flashcard sets the user didn't create, but is studying.
 */
public class UserStudiedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        DataLoadedCallback {

    @Override
    public void dataIsLoaded(){

    }

    private static final int SET_LOADER = 0;
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
            FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY,
            FlashCardContract.SetEntry.COLUMN_SET_TERM_COUNT
    };

    private SetCursorAdapter mAdapter;
    private TextView mEmptyView;
    private RecyclerView mSetRecyclerView;

    public UserStudiedFragment() {
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
        //Log.d( "USS BROADCAST RECEIVED", "USS BROADCAST RECEIVED" );
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
                new String[]{"1"}, // selectionArgs
                null               // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            // Log.d( "USS CURSOR RETURNED", Integer.toString(data.getCount()) );
            // Use swapCursor() with a CursorLoader as per:
            // https://developer.android.com/guide/components/loaders.html
            mAdapter.swapCursor(data);
            if (mSetRecyclerView.getVisibility() == View.GONE){
                mSetRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);

            }
        } else {
            // Log.d( "USS CURSOR EMPTY", "CURSOR EMPTY" );
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

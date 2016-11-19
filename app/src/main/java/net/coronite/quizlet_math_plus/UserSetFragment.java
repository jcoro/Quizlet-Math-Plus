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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coronite.quizlet_math_plus.adapters.MyListCursorAdapter;
import net.coronite.quizlet_math_plus.data.FlashCardContract;
import net.coronite.quizlet_math_plus.sync.FlashCardSyncAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserSetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static IntentFilter syncIntentFilter = new IntentFilter(MainActivity.ACTION_FINISHED_SYNC);
    private static final int SET_LOADER = 0;
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            restartLoader();
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
    public static final int INDEX_COLUMN_AUTO_ID = 0;
    public static final int INDEX_COLUMN_SET_ID = 1;
    public static final int INDEX_COLUMN_SET_STUDIED = 2;
    public static final int INDEX_COLUMN_SET_URL = 3;
    public static final int INDEX_COLUMN_SET_TITLE = 4;
    public static final int INDEX_COLUMN_SET_CREATED_BY = 5;

    private MyListCursorAdapter mAdapter;
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
        mAdapter = new MyListCursorAdapter(getActivity(), null);
        mSetRecyclerView.setAdapter(mAdapter);
        mEmptyView = (TextView) view.findViewById(R.id.empty_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SET_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register for sync
        getActivity().registerReceiver(syncBroadcastReceiver, syncIntentFilter);
        // do your resuming magic
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(syncBroadcastReceiver);
        super.onPause();
    };

    void onUsernameChanged( ) {
        updateSets();
        getLoaderManager().restartLoader(SET_LOADER, null, this);
    }

    private void updateSets() {
        FlashCardSyncAdapter.syncImmediately(getActivity());
    }

    private void restartLoader(){
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
            Log.d( "US CURSOR RETURNED", Integer.toString(data.getCount()) );
            mAdapter.swapCursor(data);
            if (mSetRecyclerView.getVisibility() == View.GONE){
                mSetRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
        } else {
            Log.d( "US CURSOR EMPTY", "CURSOR EMPTY" );
            mSetRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        MainActivity.dismissDialog();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

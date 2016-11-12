package net.coronite.quizlet_math_plus;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coronite.quizlet_math_plus.adapters.MyListCursorAdapter;
import net.coronite.quizlet_math_plus.data.FlashCardContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserSetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SET_LOADER = 0;

    private static final String[] SET_COLUMNS = new String[]{
            FlashCardContract.SetEntry.COLUMN_SET_ID,
            FlashCardContract.SetEntry.COLUMN_SET_STUDIED,
            FlashCardContract.SetEntry.COLUMN_SET_URL,
            FlashCardContract.SetEntry.COLUMN_SET_TITLE
    };

    // these indices must match the projection
    public static final int INDEX_COLUMN_SET_ID = 0;
    public static final int INDEX_COLUMN_SET_STUDIED = 1;
    public static final int INDEX_COLUMN_SET_URL = 2;
    public static final int INDEX_COLUMN_SET_TITLE = 3;

    MyListCursorAdapter mAdapter;

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

        RecyclerView mSetRecyclerView = (RecyclerView) view.findViewById(R.id.user_set_recycler_view);
        if(mAdapter.getItemCount() > 0) {
            mSetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new MyListCursorAdapter(getContext(), null);
            mSetRecyclerView.setAdapter(mAdapter);
        } else {
            mSetRecyclerView.setVisibility(View.GONE);
            TextView mEmptyView = (TextView) view.findViewById(R.id.empty_view);
            mEmptyView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SET_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FlashCardContract.SetEntry.buildGetUserSetUri();

        return new CursorLoader(
                getActivity(),     // context
                uri,               // uri
                SET_COLUMNS,       // projection
                null,              // selection
                null,              // selectionArgs
                null               // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

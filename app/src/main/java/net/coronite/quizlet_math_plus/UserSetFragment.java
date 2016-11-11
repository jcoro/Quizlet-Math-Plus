package net.coronite.quizlet_math_plus;

import android.content.Intent;
import android.database.Cursor;
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
import net.coronite.quizlet_math_plus.data.models.Set;
import net.coronite.quizlet_math_plus.sync.FlashCardSyncAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserSetFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SET_LOADER = 0;

    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";
    private static final String USER_CREATED_SETS = "USER_CREATED_SETS";

    private static final String[] SET_COLUMNS = new String[]{
            FlashCardContract.SetEntry.COLUMN_SET_ID,
            FlashCardContract.SetEntry.COLUMN_SET_STUDIED,
            FlashCardContract.SetEntry.COLUMN_SET_URL,
            FlashCardContract.SetEntry.COLUMN_SET_TITLE
    };

    // these indices must match the projection
    public static final int INDEX_COLUMN_SET_ID = 0;
    public static final int INDEX_SET_STUDIED = 1;
    public static final int INDEX_SET_URL = 2;
    public static final int INDEX_SET_TITLE = 3;

    private List<Set> mUserSets;

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
        if(!mUserSets.isEmpty()) {
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

    void onPreferencesChanged( ) {
        updateSets();
        getLoaderManager().restartLoader(SET_LOADER, null, this);
    }

    private void updateSets() {
        FlashCardSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Uri uri = FlashCardContract.SetEntry.buildUserSetUri();

        return new CursorLoader(
                getActivity(),                                           // context
                FlashCardContract.SetEntry.CONTENT_URI,                  // uri
                SET_COLUMNS,                                             // projection
                FlashCardContract.SetEntry.COLUMN_SET_STUDIED + " = ?",  // selection
                new String[] { Integer.toString(0) },                    // selectionArgs
                null                                                     // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class SetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private Set mSet;
        private String mSetId;
        private String mSetTitle;

        public SetHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_set_title_text_view);
        }

        public void bindSet(Set set) {
            mSet = set;
            mSetId = mSet.getQuizletSetId();
            mSetTitle = mSet.getTitle();
            mTitleTextView.setText(mSetTitle);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(EXTRA_SET_ID, mSetId);
            intent.putExtra(EXTRA_SET_TITLE, mSetTitle);
            startActivity(intent);
        }
    }

    private class SetAdapter extends RecyclerView.Adapter<SetHolder> {

        private List<Set> mSets;

        public SetAdapter(List<Set> sets) {
            mSets = sets;
        }

        @Override
        public SetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.users_set_list_item, parent, false);
            return new SetHolder(view);
        }

        @Override
        public void onBindViewHolder(SetHolder holder, int position) {
            Set set = mSets.get(position);
            holder.bindSet(set);
        }

        @Override
        public int getItemCount() {
            return mSets.size();
        }
    }
}

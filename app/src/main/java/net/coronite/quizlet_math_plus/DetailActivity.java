package net.coronite.quizlet_math_plus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.coronite.quizlet_math_plus.data.FlashCardContract;
import net.coronite.quizlet_math_plus.data.models.Term;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SET_LOADER = 0;
    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";
    private static final String ARG_SET_COUNT = "SET_COUNT";
    private static final String ARG_SET_TITLE = "ARG_SET_TITLE";
    private static final String ARG_SET_ID = "arg_set_id";
    private static final String ARG_CARD_NUM = "CARD_NUM";
    private static final String ARG_SHOW_TERM = "ARG_SHOW_TERM";
    private CustomViewPager mPager;
    private List<Term> mTerms;
    private ProgressDialog pd = null;
    private Context mContext = this;
    private String mSetTitle;
    private int mSetCount;
    private Boolean mShowTerm;
    private String mSetId;

    private static final String[] TERM_COLUMNS = new String[]{
            //FlashCardContract.TermEntry.ID,
            FlashCardContract.TermEntry.COLUMN_SET_ID,
            FlashCardContract.TermEntry.COLUMN_TERM,
            FlashCardContract.TermEntry.COLUMN_DEFINITION,
            FlashCardContract.TermEntry.COLUMN_IMAGE,
            FlashCardContract.TermEntry.COLUMN_RANK
    };

    // these indices must match the projection
    //public static final int INDEX_COLUMN_AUTO_ID = 0;
    public static final int INDEX_COLUMN_SET_ID = 0;
    public static final int INDEX_COLUMN_TERM = 1;
    public static final int INDEX_COLUMN_DEFINITION = 2;
    public static final int INDEX_COLUMN_IMAGE = 3;
    public static final int INDEX_COLUMN_RANK = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetId = getIntent().getStringExtra(EXTRA_SET_ID);
        if(pd == null) {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
        }
        mSetTitle = getIntent().getStringExtra(EXTRA_SET_TITLE);
        mShowTerm = Utility.getShowTermBoolean(mContext);
        setContentView(R.layout.activity_detail);
        setupActionBar();
        getSupportLoaderManager().initLoader(SET_LOADER, null, this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);

    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mSetTitle);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = FlashCardContract.TermEntry.CONTENT_URI;
        Log.d("Detail Activity - Uri", uri.toString());
        Log.d("mSetId", mSetId);
        return new CursorLoader(
                mContext,             // context
                uri,                  // uri
                TERM_COLUMNS,         // projection
                "quizlet_set_id=?",   // selection
                new String[]{mSetId}, // selectionArgs
                "rank ASC"            // sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("CURSOR_COUNT", Integer.toString(data.getCount()));
        if(data.getCount() > 0) {
            mTerms = buildTerms(data);
            Log.d("M_Terms_COUNT", Integer.toString(mTerms.size()));
            mSetCount = mTerms.size();
            if(pd.isShowing()) {
                pd.dismiss();
            }
        }
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<Term> buildTerms(Cursor cursor) {
        List<Term> mCursorTerms = new ArrayList<>();
        // if Cursor contains results
        if(cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    //String _id = cursor.getString(INDEX_COLUMN_AUTO_ID);
                    String id = cursor.getString(INDEX_COLUMN_SET_ID);
                    String url = cursor.getString(INDEX_COLUMN_TERM);
                    String term = cursor.getString(INDEX_COLUMN_DEFINITION);
                    String definition = cursor.getString(INDEX_COLUMN_IMAGE);
                    String rank = cursor.getString(INDEX_COLUMN_RANK);
                    Term singleTerm = new Term( id, url, term, definition, rank );
                    mCursorTerms.add(singleTerm);
                }
            } finally {
                cursor.close();
            }
        }
        return mCursorTerms;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            if (mTerms !=null) {
                Term term = mTerms.get(position);
                args.putParcelable(ARG_SET_ID, term);
                args.putInt(ARG_SET_COUNT, mSetCount);
                args.putInt(ARG_CARD_NUM, position);
                args.putString(ARG_SET_TITLE, mSetTitle);
                args.putBoolean(ARG_SHOW_TERM, mShowTerm);
            }
            Fragment fragment = new DetailActivityFragment();
            if (args.size()>0){
                fragment.setArguments(args);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            if (mTerms !=null) {
                return mTerms.size();
            } else {
                return 0;
            }
        }
    }

}

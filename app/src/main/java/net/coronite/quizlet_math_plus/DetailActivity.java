package net.coronite.quizlet_math_plus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import net.coronite.quizlet_math_plus.data.QuizletTermsAPI;
import net.coronite.quizlet_math_plus.data.models.Term;
import net.coronite.quizlet_math_plus.data.models.TermList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";
    private static final String ARG_SET_COUNT = "SET_COUNT";
    private static final String ARG_SET_TITLE = "ARG_SET_TITLE";
    private static final String ARG_SET_ID = "arg_set_id";
    private static final String ARG_CARD_NUM = "CARD_NUM";
    private static final String ARG_SHOW_TERM = "ARG_SHOW_TERM";
    private CustomViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<Term> mTerms;
    private ProgressDialog pd = null;
    private Context mContext = this;
    private String mSetTitle;
    private int mSetCount;
    private Boolean mShowTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mSetId = getIntent().getStringExtra(EXTRA_SET_ID);
        mSetTitle = getIntent().getStringExtra(EXTRA_SET_TITLE);
        mShowTerm = Utility.getShowTermBoolean(mContext);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        if(pd == null) {
            pd = new ProgressDialog(mContext);
            pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
            pd.show();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(QuizletTermsAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuizletTermsAPI service = retrofit.create(QuizletTermsAPI.class);
        Call<TermList> call = service.getFeed(mSetId);
        call.enqueue(new Callback<TermList>(){
            @Override
            public void onResponse(Call<TermList> call, Response<TermList> response){
                mTerms = response.body().terms;
                mSetCount = mTerms.size();
                // Instantiate a ViewPager and a PagerAdapter.
                mPager = (CustomViewPager) findViewById(R.id.pager);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
                if(pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure (Call<TermList> call, Throwable t){
                Log.e(TAG, t.toString());
            }
        });
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Log.v("ACTIONBAR", "DETAIL_ACTIONBAR");
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Term term = mTerms.get(position);

            Bundle args = new Bundle();
            args.putParcelable(ARG_SET_ID, term);
            args.putInt(ARG_SET_COUNT, mSetCount);
            args.putInt(ARG_CARD_NUM, position);
            args.putString(ARG_SET_TITLE, mSetTitle);
            args.putBoolean(ARG_SHOW_TERM, mShowTerm);
            Fragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {

            return mTerms.size();
        }
    }

}

package net.coronite.quizlet_math_plus;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
    private static final String ARG_SET_ID = "arg_set_id";
    private CustomViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<Term> mTerms;
    private ProgressDialog pd = null;
    private Context mContext = this;
    private String mSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetId = getIntent().getStringExtra(EXTRA_SET_ID);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Term term = mTerms.get(position);

            Bundle args = new Bundle();
            args.putParcelable(ARG_SET_ID, term);
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

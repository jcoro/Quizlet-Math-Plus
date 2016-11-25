package net.coronite.quizlet_math_plus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import net.coronite.quizlet_math_plus.sync.FlashCardSyncAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_FINISHED_SYNC = "net.coronite.quizlet_math_plus.ACTION_FINISHED_SYNC";
    static FrameLayout mProgressOverlay;
    private String mUsername;
    static ViewPagerAdapter mViewPagerAdapter;
    private Context mContext = this;
    private FirebaseAnalytics mFirebaseAnalytics;
    static private AdView mAdView;
    SharedPreferences mPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener mSharedPrefsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        FlashCardSyncAdapter.initializeSyncAdapter(this);
        mUsername = Utility.getUsername(this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                String username = Utility.getUsername(mContext);
                if (username != null && !username.equals(mUsername)) {
                    showOverlay();
                    fetchData();
                    Log.d("USERNAME CHANGE", "INITIAL");
                    mUsername = username;
                }
            }
        };
        setContentView(R.layout.activity_main);
        mProgressOverlay = (FrameLayout)findViewById(R.id.progress_overlay);
        mProgressOverlay.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifySettings=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(modifySettings);
                //Let's track whether users prefer the menu button or the FAB to change settings.
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "fab_click");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });


        mAdView = (AdView) findViewById(R.id.adView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    @Override
    protected void onStart(){
        super.onStart();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(BuildConfig.TEST_DEVICE_ID)
                .build();
        mAdView.loadAd(adRequest);
    }

    /**
     * Because the MainActivity is set to: android:launchMode="singleTop"
     * OnResume is called when the user presses the Up or back button from the
     * SettingsActivity. If the user changed their username, the data is fetched.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mPrefs.registerOnSharedPreferenceChangeListener(mSharedPrefsListener);
        String username = Utility.getUsername(this);
        if(username != null && !username.equals(mUsername)) {
            Log.d("USERNAME", "Usernames are different");
            fetchData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(mSharedPrefsListener);

    }

    public void fetchData(){
        showOverlay();
        FlashCardSyncAdapter.syncImmediately(this);
    }

    /**
     * Show the overlay when the data is loading
     */
    static void showOverlay(){
        if (mProgressOverlay != null && mProgressOverlay.getVisibility() == View.GONE) {
            Log.d("SHOW OVERLAY", "SHOW OVERLAY");
            Utility.animateView(mProgressOverlay, View.VISIBLE, 0.4f, 200);
        }
    }

    /**
     * Dismiss the overlay (called in UserStudiedFragment when the data is finished downloading)
     */
    public static void dismissOverlay(){
        if (mProgressOverlay != null && mProgressOverlay.getVisibility() == View.VISIBLE) {
            Log.d("DISMISS OVERLAY", "DISMISS OVERLAY");
            Utility.animateView(mProgressOverlay, View.GONE, 0, 200);
        }
    }

    /**
     * On the first run of the app, show an Alert where the user can enter the username.
     */
    public void checkFirstRun() {
        boolean mFirstRun = Utility.getIsFirstRun(mContext);
        if (mFirstRun){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set up the input
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setMessage(getString(R.string.username_message))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final String mText = input.getText().toString();
                            Utility.setUsername(mContext, mText);
                            Utility.setIsFirstRun(mContext, false);

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Log.d("NOT FIRST RUN", "NOT FIRST RUN");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * When the user selects Settings from the options menu, log it with Firebase Analytics.
     * @param item the item selected from the options menu.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            //Let's track whether users prefer the menu button or the FAB to change settings.
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu_click");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method for adding Fragments to the ViewPagerAdapter, and setting the Adapter to the ViewPager
     * @param viewPager the ViewPager to which the Adapter is set
     */
    private void setupViewPager(ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragOne = new UserSetFragment();
        mViewPagerAdapter.addFragment(fragOne, getString(R.string.your_sets));

        Fragment fragTwo = new UserStudiedFragment();
        mViewPagerAdapter.addFragment(fragTwo, getString(R.string.studied_sets));

        viewPager.setAdapter(mViewPagerAdapter);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        // Here we can finally safely save a reference to the created
        // Fragment, no matter where it came from (either getItem() or
        // FragmentManger). Simply save the returned Fragment from
        // super.instantiateItem() into an appropriate reference depending
        // on the ViewPager position.
        /**
        //@Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    m1stFragment = (UserSetFragment) createdFragment;
                    break;
                case 1:
                    m2ndFragment = (UserStudiedFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }
         **/

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

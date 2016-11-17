package net.coronite.quizlet_math_plus;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import net.coronite.quizlet_math_plus.sync.FlashCardSyncAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static ProgressDialog pd = null;
    private String mUsername;
    private ViewPagerAdapter mAdapter;
    private Context mContext = this;
    private FirebaseAnalytics mFirebaseAnalytics;
    //private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        //FlashCardSyncAdapter.initializeSyncAdapter(this);
        mUsername = Utility.getUsername(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifySettings=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(modifySettings);
            }
        });
        setupViewPager(mViewPager);

        //mAdView = (AdView) findViewById(R.id.adView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /**
    //@Override
    protected void onStart(){
        super.onStart();
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(BuildConfig.TEST_DEVICE_ID)
                .build();
        mAdView.loadAd(adRequest);
    }
    **/


    static void dismissDialog(){
        try {
            if( pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            Log.e("MAIN_ACTIVITY", e.getMessage());
        } catch (final Exception e) {
            Log.e("MAIN_ACTIVITY_EXC", e.getMessage());
        } finally {
            pd = null;
        }
    }

    public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Remember To Set Your Quizlet Username In The Settings")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();
                            Intent intent = new Intent(mContext, SettingsActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {

            if (pd == null) {
                pd = new ProgressDialog(mContext);
                pd.setTitle("Please wait");
                pd.setMessage("Your Data is loading..");
                pd.show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        String username = Utility.getUsername(this);
        Log.d(username, mUsername);
        if (username != null && !username.equals(mUsername)) {
            FlashCardSyncAdapter.syncImmediately(this);
            mAdapter.notifyDataSetChanged();
            Log.d(username, mUsername);
            mUsername = username;
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragOne = new UserSetFragment();
        mAdapter.addFragment(fragOne, getString(R.string.your_sets));

        Fragment fragTwo = new UserStudiedFragment();
        mAdapter.addFragment(fragTwo, getString(R.string.studied_sets));

        viewPager.setAdapter(mAdapter);
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

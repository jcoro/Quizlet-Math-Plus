package net.coronite.quizlet_math_plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.coronite.quizlet_math_plus.data.QuizletSetsAPI;
import net.coronite.quizlet_math_plus.data.models.Set;
import net.coronite.quizlet_math_plus.data.models.SetList;
import net.coronite.quizlet_math_plus.data.models.StudiedSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String USER_CREATED_SETS = "USER_CREATED_SETS";
    private final String USER_STUDIED_SETS = "USER_STUDIED_SETS";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Set> mUserSets;
    private List<StudiedSet> mStudiedSets;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = Utility.getUsername(this);
        fetchSets();

        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent modifySettings=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(modifySettings);
            }
        });
    }

    private void fetchSets(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.quizlet.com/2.0/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        QuizletSetsAPI quizletSetsAPI = retrofit.create(QuizletSetsAPI.class);

        Call<SetList> call = quizletSetsAPI.loadSets(mUsername);
        //asynchronous call
        call.enqueue(new Callback<SetList>(){
            @Override
            public void onResponse(Call<SetList> call, Response<SetList> response){
                mUserSets = response.body().sets;
                mStudiedSets = response.body().studied;

                setupViewPager(viewPager);
            }

            @Override
            public void onFailure (Call<SetList> call, Throwable t){
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragOne = new UserSetFragment();
        if(mUserSets != null){
            ArrayList<Set> userArray = (ArrayList<Set>)mUserSets;
            Bundle argsUser = new Bundle();
            argsUser.putParcelableArrayList(USER_CREATED_SETS, userArray);
            fragOne.setArguments(argsUser);
        }
        adapter.addFragment(fragOne, getString(R.string.your_sets));

        Fragment fragTwo = new UserStudiedFragment();
        if(mStudiedSets != null){
            ArrayList<StudiedSet> studiedArray = (ArrayList<StudiedSet>)mStudiedSets;
            Bundle argsStudied = new Bundle();
            argsStudied.putParcelableArrayList(USER_STUDIED_SETS, studiedArray);
            fragTwo.setArguments(argsStudied);
        }
        adapter.addFragment(fragTwo, getString(R.string.studied_sets));

        viewPager.setAdapter(adapter);
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



    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    //}
    //
    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
    //    // Handle action bar item clicks here. The action bar will
    //    // automatically handle clicks on the Home/Up button, so long
    //    // as you specify a parent activity in AndroidManifest.xml.
    //    int id = item.getItemId();
    //
    //    //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
    //        startActivity(new Intent(this, SettingsActivity.class));
    //        return true;
    //    }
    //
    //    return super.onOptionsItemSelected(item);
    //}
}

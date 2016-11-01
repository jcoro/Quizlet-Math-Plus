package net.coronite.quizlet_math_plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String EXTRA_SET_ID = "SET_ID";

    private RecyclerView mSetRecyclerView;
    private SetAdapter mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        mSetRecyclerView = (RecyclerView) view
                .findViewById(R.id.card_recycler_view);
        mSetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.quizlet.com/2.0/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        QuizletSetsAPI quizletSetsAPI = retrofit.create(QuizletSetsAPI.class);

        Call<SetLists> call = quizletSetsAPI.loadSets();
        //asynchronous call
        call.enqueue(new Callback<SetLists>(){
            @Override
            public void onResponse(Call<SetLists> call, Response<SetLists> response){
                List<Set> sets = response.body().sets;
                mAdapter = new SetAdapter(sets);
                mSetRecyclerView.setAdapter(mAdapter);
                Log.v("MAIN_ACTIVITY", response.body().sets.toString());
            }

            @Override
            public void onFailure (Call<SetLists> call, Throwable t){
                Log.e("MAIN_ACTIVITY", t.toString());
            }
        });


    }

    private class SetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;

        private Set mSet;
        private String mSetId;

        public SetHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_set_title_text_view);
        }

        public void bindSet(Set set) {
            mSet = set;
            mSetId = mSet.getId();
            mTitleTextView.setText(mSet.getTitle());

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(EXTRA_SET_ID, mSetId);
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
            View view = layoutInflater.inflate(R.layout.list_item_card, parent, false);
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

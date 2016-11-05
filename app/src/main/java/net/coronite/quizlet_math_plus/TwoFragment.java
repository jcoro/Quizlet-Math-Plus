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


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {

    private static final String EXTRA_SET_ID = "SET_ID";

    private List<StudiedSet> mStudiedSetList;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mStudiedSetList = arguments.getParcelableArrayList("USER_STUDIED_SETS");
            Log.v("NOT NULL", Integer.toString(mStudiedSetList.size()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_two, container, false);
        RecyclerView mSetRecyclerView = (RecyclerView) view
                .findViewById(R.id.studied_sets_recycler_view);
        mSetRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StudiedSetAdapter mAdapter = new StudiedSetAdapter(mStudiedSetList);
        mSetRecyclerView.setAdapter(mAdapter);

        return view;
    }
    private class StudiedSetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private Set mSet;
        private String mSetId;

        public StudiedSetHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_studied_set_title_text_view);
        }

        public void bindSet(StudiedSet studySet) {
            mSet = studySet.getSet();
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

    private class StudiedSetAdapter extends RecyclerView.Adapter<StudiedSetHolder> {

        private List<StudiedSet> mStudiedSets;

        public StudiedSetAdapter(List<StudiedSet> sets) {
            mStudiedSets = sets;
        }

        @Override
        public StudiedSetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.studied_set_list_item, parent, false);
            return new StudiedSetHolder(view);
        }

        @Override
        public void onBindViewHolder(StudiedSetHolder holder, int position) {
            StudiedSet set = mStudiedSets.get(position);
            holder.bindSet(set);
        }

        @Override
        public int getItemCount() {
            return mStudiedSets.size();
        }
    }

}

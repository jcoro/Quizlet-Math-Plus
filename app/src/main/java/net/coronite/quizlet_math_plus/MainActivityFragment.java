package net.coronite.quizlet_math_plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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
        SetMaker setMaker = SetMaker.get(getActivity());
        List<Set> sets = setMaker.getSets();

        mAdapter = new SetAdapter(sets);
        mSetRecyclerView.setAdapter(mAdapter);
    }

    private class SetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;

        private Set mSet;

        public SetHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_set_title_text_view);
        }

        public void bindSet(Set set) {
            mSet = set;
            mTitleTextView.setText(mSet.getTitle());

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
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
            Set card = mSets.get(position);
            holder.bindSet(card);
        }

        @Override
        public int getItemCount() {
            return mSets.size();
        }
    }
}

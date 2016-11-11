package net.coronite.quizlet_math_plus.adapters;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coronite.quizlet_math_plus.DetailActivity;
import net.coronite.quizlet_math_plus.R;
import net.coronite.quizlet_math_plus.data.models.Set;

public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder>{

    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";

    public MyListCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private Set mSet;
        private String mSetId;
        private String mSetTitle;

        public ViewHolder(View itemView) {
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
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(EXTRA_SET_ID, mSetId);
            intent.putExtra(EXTRA_SET_TITLE, mSetTitle);
            context.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.users_set_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Set set = Set.fromCursor(cursor);
        viewHolder.bindSet(set);
    }
}

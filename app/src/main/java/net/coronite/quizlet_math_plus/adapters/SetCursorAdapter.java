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
import net.coronite.quizlet_math_plus.Utility;
import net.coronite.quizlet_math_plus.data.models.Set;

/**
 * {@code SetCursorAdapter} exposes flash card {@code Set} data from a Cursor to a RecyclerView.
 */

public class SetCursorAdapter extends CursorRecyclerViewAdapter<SetCursorAdapter.ViewHolder>{

    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";
    private static final String EXTRA_SET_CREATED_BY = "SET_CREATED_BY";
    private static final String EXTRA_SET_TERM_COUNT = "SET_TERM_COUNT";

    private Context mContext;

    /**
     * Constructor for {@code SetCursorAdapter}
     * @param context - the {@code Context} of the adapter.
     * @param cursor - the {@code Cursor} from which data is obtained.
     */
    public SetCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;
    }

    /**
     * A {@code RecyclerView.ViewHolder } for listing the {@code Set}s in the {@code MainActivity}.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mSetCreatedByTextView;
        private Set mSet;
        private String mSetId;
        private String mSetTitle;
        private String mSetCreatedBy;
        private String mSetTermCount;


        /**
         * Constructor for {@code ViewHolder}
         * @param itemView - The {@code View} to which the data is set.
         */
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_set_title_text_view);
            mSetCreatedByTextView = (TextView) itemView.findViewById(R.id.created_by);
        }

        /**
         * Binds the {@code Set} data to the {@code View}.
         * @ param set - the {@code Set} to bind.
         */
        void bindSet(Set set) {
            mSet = set;
            mSetId = mSet.getQuizletSetId();
            mSetTitle = mSet.getTitle();
            mSetCreatedBy = mSet.getCreatedBy();
            mSetTermCount = mSet.getTerm_count();
            mTitleTextView.setText(mSetTitle);
            mSetCreatedByTextView.setText(Utility.getCreatedByString(mContext, mSetCreatedBy));
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(EXTRA_SET_ID, mSetId);
            intent.putExtra(EXTRA_SET_TITLE, mSetTitle);
            intent.putExtra(EXTRA_SET_CREATED_BY, mSetCreatedBy);
            intent.putExtra(EXTRA_SET_TERM_COUNT, mSetTermCount);
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

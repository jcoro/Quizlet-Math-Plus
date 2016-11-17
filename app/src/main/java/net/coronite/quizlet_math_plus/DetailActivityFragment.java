package net.coronite.quizlet_math_plus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.coronite.mathview.MathView;
import net.coronite.quizlet_math_plus.data.models.Term;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Term mTerm;
    private static final String ARG_SET_COUNT = "SET_COUNT";
    private static final String ARG_SET_ID = "arg_set_id";
    private static final String ARG_CARD_NUM = "CARD_NUM";
    private static final String ARG_SET_TITLE = "ARG_SET_TITLE";
    private static final String ARG_SHOW_TERM = "ARG_SHOW_TERM";
    private int mSetCount;
    private int mCardNumber;
    private String mSetTitle;
    private Boolean mShowTerm;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTerm = arguments.getParcelable(ARG_SET_ID);
            mSetCount = arguments.getInt(ARG_SET_COUNT);
            mCardNumber = arguments.getInt(ARG_CARD_NUM);
            mSetTitle = arguments.getString(ARG_SET_TITLE);
            mShowTerm = arguments.getBoolean(ARG_SHOW_TERM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            String title = String.format(Locale.US, "%1$s Card %2$d of %3$d", mSetTitle, mCardNumber, mSetCount);
            actionBar.setTitle(title);
        }
        MathView mv = (MathView) view.findViewById(R.id.mathview_text);
            if (mTerm != null) {
                mv.setData(mTerm.getTerm(), mTerm.getDefinition(), mShowTerm);
            }



        return view;
    }
}

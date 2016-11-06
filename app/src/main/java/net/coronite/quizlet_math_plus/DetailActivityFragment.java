package net.coronite.quizlet_math_plus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.coronite.mathview.MathView;
import net.coronite.quizlet_math_plus.data.models.Term;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Term mTerm;
    private static final String ARG_SET_ID = "arg_set_id";

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTerm = arguments.getParcelable(ARG_SET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        MathView mv = (MathView) view.findViewById(R.id.mathview_text);
            if (mTerm != null) {
                mv.setData(mTerm.getTerm(), mTerm.getDefinition());
            }


        return view;
    }
}

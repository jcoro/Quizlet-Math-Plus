package net.coronite.quizlet_math_plus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Term mTerm;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tv = (TextView) view.findViewById(R.id.term_text_view);
        if (arguments != null) {
            mTerm = arguments.getParcelable("arg_set_id");
            if (mTerm != null){
                tv.setText(mTerm.getTerm());
            }
        }

        return view;
    }
}

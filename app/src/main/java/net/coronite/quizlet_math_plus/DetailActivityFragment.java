package net.coronite.quizlet_math_plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private ShareActionProvider mShareActionProvider;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mTerm != null) {
            mShareActionProvider.setShareIntent(createShareTermIntent());
        }
    }

    private Intent createShareTermIntent() {
        String shareTerm = String.format(Locale.US, "Term: \n%1$s\n Definition: \n%2$s", mTerm.getTerm(), mTerm.getDefinition());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTerm);
        return shareIntent;
    }
}

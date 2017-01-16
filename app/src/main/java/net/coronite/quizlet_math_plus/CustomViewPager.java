package net.coronite.quizlet_math_plus;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * We set an adapter on this {@code CustomViewPager} in the {@code DetailActivity} to allow
 * the user to swipe through Fragments containing each flash card.
 */
public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}

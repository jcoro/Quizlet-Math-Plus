package net.coronite.quizlet_math_plus;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewPropertyAnimator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PreferenceManager.class)
public class UtilityTest {

    private static final String CREATED_BY = "Created By: someUsername";
    private static final String TERM = "A Test Term";
    private static final String DEFINITION = "A Test Definition";

    @Mock
    Context mContext;

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences.Editor mEditor;

    @Mock
    View mMockedView;

    @Mock
    ViewPropertyAnimator mMockedViewPropertyAnimator;

    @Mock
    Utility.CustomAnimatorListenerAdapter mMockedAnimatorListenerAdapter;

    @Mock
    Animator mMockAnimation;

    @Mock
    Utility.CustomAnimatorListenerAdapter mGoneMockedAnimatorListenerAdapter;


    @Test
    public void testGetCreatedBy(){
        String actual = CREATED_BY;
        when(mContext.getString(R.string.created_by))
                .thenReturn(CREATED_BY);
        String expected = Utility.getCreatedByString(mContext, "someUsername");
        assertEquals("Created By Test", expected, actual);
    }

    @Test
    public void testGetShareString(){
        String actual = "Term: " + TERM + "Definition: " + DEFINITION;
        when(mContext.getString(R.string.share_string))
                .thenReturn("Term: " + TERM + "Definition: " + DEFINITION);
        String expected = Utility.getShareString(mContext, TERM, DEFINITION);
        assertEquals("message", actual, expected);
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    public void testSetIsFirstRun() {
        mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences(any(Context.class))).thenReturn((mMockSharedPreferences));
        when(mMockSharedPreferences.edit()).thenReturn(mEditor);
        when(mEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mEditor);
        doNothing().when(mEditor).apply();

        Utility.setIsFirstRun(mContext, true);

        verify(mEditor).apply();
        verifyStatic();

    }

    @Test
    public void testGetIsFirstRunTrue() {
        mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences(any(Context.class))).thenReturn((mMockSharedPreferences));
        when(mMockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(true);
        Boolean result = Utility.getIsFirstRun(mContext);
        assertSame(result, true);
        verify(mMockSharedPreferences).getBoolean(anyString(), anyBoolean());
        verifyStatic();

    }

    @Test
    public void testGetIsFirstRunFalse() {
        mockStatic(PreferenceManager.class);
        when(PreferenceManager.getDefaultSharedPreferences(any(Context.class))).thenReturn((mMockSharedPreferences));
        when(mMockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false);
        Boolean result = Utility.getIsFirstRun(mContext);
        assertSame(result, false);
        verify(mMockSharedPreferences).getBoolean(anyString(), anyBoolean());
        verifyStatic();

    }

    @Test
    public void testAnimateViewVisible(){
        when(mMockedView.animate()).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.setDuration(anyLong())).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.alpha(anyFloat())).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.setListener(any(Animator.AnimatorListener.class))).thenReturn(mMockedViewPropertyAnimator);
        mMockedAnimatorListenerAdapter.onAnimationEnd(mMockAnimation);
        Utility.animateView(mMockedView, View.VISIBLE, 0.4f, 200);
        verify(mMockedView).setAlpha(0);
        verify(mMockedView).setVisibility(View.VISIBLE );
        verify(mMockedView).animate();
        verify(mMockedViewPropertyAnimator).setDuration(anyLong());
        verify(mMockedViewPropertyAnimator).alpha(anyFloat());
        verify(mMockedViewPropertyAnimator).setListener(any(Animator.AnimatorListener.class));
        verify(mMockedAnimatorListenerAdapter).onAnimationEnd(mMockAnimation);
    }


    @Test
    public void testAnimateViewGone(){
        when(mMockedView.animate()).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.setDuration(anyLong())).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.alpha(anyFloat())).thenReturn(mMockedViewPropertyAnimator);
        when(mMockedViewPropertyAnimator.setListener(any(Utility.CustomAnimatorListenerAdapter.class))).thenReturn(mMockedViewPropertyAnimator);
        mGoneMockedAnimatorListenerAdapter.onAnimationEnd(mMockAnimation);
        Utility.animateView(mMockedView, View.GONE, 0, 200);
        verify(mMockedView).animate();
        verify(mMockedViewPropertyAnimator).setDuration(anyLong());
        verify(mMockedViewPropertyAnimator).alpha(anyFloat());
        verify(mMockedViewPropertyAnimator).setListener(any(Animator.AnimatorListener.class));
        verify(mGoneMockedAnimatorListenerAdapter).onAnimationEnd(mMockAnimation);
    }

    @Test
    public void testMyAnimatorListenerAdapter(){
        mMockedAnimatorListenerAdapter.setView(mMockedView);
        mMockedAnimatorListenerAdapter.setVisibility(View.GONE);
        mMockedAnimatorListenerAdapter.onAnimationEnd(mMockAnimation);
        verify(mMockedAnimatorListenerAdapter).setView(mMockedView);
        verify(mMockedAnimatorListenerAdapter).setVisibility(View.GONE);
        verify(mMockedAnimatorListenerAdapter).onAnimationEnd(mMockAnimation);

    }

    @Test
    public void testSetVisibility(){
        Utility.CustomAnimatorListenerAdapter realAdapter = new Utility.CustomAnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                mMockedView.setVisibility(View.GONE);

            }
        };
        realAdapter.setView(mMockedView);
        realAdapter.setVisibility(View.GONE);
        mMockedViewPropertyAnimator.setListener(realAdapter);
        realAdapter.onAnimationEnd(mMockAnimation);
        verify(mMockedView).setVisibility(View.GONE);
    }



}
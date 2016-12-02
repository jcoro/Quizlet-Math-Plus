package net.coronite.quizlet_math_plus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
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

    @Test
    public void testCreatedBy(){
        String actual = CREATED_BY;
        when(mContext.getString(R.string.created_by))
                .thenReturn(CREATED_BY);
        String expected = Utility.getCreatedByString(mContext, "someUsername");
        assertEquals("Created By Test", expected, actual);
    }

    @Test
    public void testShareString(){
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

}
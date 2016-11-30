package net.coronite.quizlet_math_plus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilityTest {

    private static final String CREATED_BY = "Created By: someUsername";
    private static final String TERM = "A Test Term";
    private static final String DEFINITION = "A Test Definition";
    private static final String TEST_USERNAME = "testUsername";

    @Mock
    Context mContext;


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

}
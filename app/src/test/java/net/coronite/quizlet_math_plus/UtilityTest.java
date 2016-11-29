package net.coronite.quizlet_math_plus;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilityTest {

    private static final String CREATED_BY = "Created By: someUsername";

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

}
package net.coronite.quizlet_math_plus;

import android.net.Uri;

import net.coronite.quizlet_math_plus.data.FlashCardContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Uri.class})
public class FlashCardContractTest {

    private static final long SET_ID = 12345;

    @Mock
    Uri mockSetUri;

    @Before
    public void setup() throws Exception{
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
        mockSetUri = FlashCardContract.SetEntry.buildSetUri(SET_ID);
    }

    @Test
    public void testBuildSetUri(){
        String setIdString = Long.toString(SET_ID);
        assertNotNull("Error: Null Uri returned", mockSetUri);
        assertEquals("Error: id not appended", setIdString, mockSetUri.getLastPathSegment());
        assertEquals("Error: Unexpected Uri", mockSetUri.toString(), "content://net.coronite.quizlet_math_plus/set/12345");
    //assertEquals("test", true, true);
    }

}
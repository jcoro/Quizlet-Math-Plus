package net.coronite.quizlet_math_plus.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

class TestUtilities {


    static ContentValues createTermValues() {
        ContentValues TermValues = new ContentValues();
        TermValues.put(FlashCardContract.TermEntry.COLUMN_SET_ID, "123");
        TermValues.put(FlashCardContract.TermEntry.COLUMN_TERM, "test term");
        TermValues.put(FlashCardContract.TermEntry.COLUMN_DEFINITION, "test definition");
        TermValues.put(FlashCardContract.TermEntry.COLUMN_IMAGE, "image.jpg");
        TermValues.put(FlashCardContract.TermEntry.COLUMN_RANK, "1");

        return TermValues;
    }

    static ContentValues createSetValues() {
        // Create a new map of values, where column names are the keys
        ContentValues setValues = new ContentValues();
        setValues.put(FlashCardContract.SetEntry.COLUMN_SET_ID, "456");
        setValues.put(FlashCardContract.SetEntry.COLUMN_SET_STUDIED, "1");
        setValues.put(FlashCardContract.SetEntry.COLUMN_SET_URL, "testurl");
        setValues.put(FlashCardContract.SetEntry.COLUMN_SET_TITLE, "some set title");
        setValues.put(FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY, "created by");

        return setValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

}
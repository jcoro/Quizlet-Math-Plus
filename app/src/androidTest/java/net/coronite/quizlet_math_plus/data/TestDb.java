package net.coronite.quizlet_math_plus.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TestDb {
    private Context mContext;

    @Before
    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mContext.deleteDatabase(FlashCardDbHelper.DATABASE_NAME);
    }


    @Test
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(FlashCardContract.SetEntry.TABLE_NAME);
        tableNameHashSet.add(FlashCardContract.TermEntry.TABLE_NAME);

        mContext.deleteDatabase(FlashCardDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FlashCardDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that the database doesn't contain both the Set entry
        // and Term entry tables
        assertTrue("Error: Your database was created without both the Term entry and Set entry tables",
                tableNameHashSet.isEmpty());

        // do the tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + FlashCardContract.SetEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for in the Set table
        final HashSet<String> setColumnHashSet = new HashSet<>();
        setColumnHashSet.add(FlashCardContract.SetEntry._ID);
        setColumnHashSet.add(FlashCardContract.SetEntry.COLUMN_SET_ID);
        setColumnHashSet.add(FlashCardContract.SetEntry.COLUMN_SET_STUDIED);
        setColumnHashSet.add(FlashCardContract.SetEntry.COLUMN_SET_URL);
        setColumnHashSet.add(FlashCardContract.SetEntry.COLUMN_SET_TITLE);
        setColumnHashSet.add(FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            setColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required Set
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required Set entry columns",
                setColumnHashSet.isEmpty());
        db.close();
        c.close();
    }

    @Test
    public void testSetTable() {
        insertSet();
    }

    @Test
    public void testTermTable() {

        // First step: Get reference to writable database
        FlashCardDbHelper dbHelper = new FlashCardDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create Term values
        ContentValues termValues = TestUtilities.createTermValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long termRowId = db.insert(FlashCardContract.TermEntry.TABLE_NAME, null, termValues);
        assertTrue(termRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        Cursor termCursor = db.query(
                FlashCardContract.TermEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from Term query", termCursor.moveToFirst() );

        // Fifth Step: Validate the location Query
        TestUtilities.validateCurrentRecord("testInsertReadDb TermEntry failed to validate",
                termCursor, termValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from Term query",
                termCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        termCursor.close();
        dbHelper.close();
    }


    /**
     * A Helper method for inserting Sets into the database.
     * @return the RowId
     */
    public long insertSet() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        FlashCardDbHelper dbHelper = new FlashCardDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testSetValues = TestUtilities.createSetValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long SetRowId;
        SetRowId = db.insert(FlashCardContract.SetEntry.TABLE_NAME, null, testSetValues);

        // Verify we got a row back.
        assertTrue(SetRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        Cursor cursor = db.query(
                FlashCardContract.SetEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from Set query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testSetValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from set query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return SetRowId;
    }

}

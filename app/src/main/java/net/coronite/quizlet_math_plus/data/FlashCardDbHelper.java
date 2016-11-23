package net.coronite.quizlet_math_plus.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.coronite.quizlet_math_plus.data.FlashCardContract.SetEntry;
import net.coronite.quizlet_math_plus.data.FlashCardContract.TermEntry;

public class FlashCardDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "flashcard.db";

    public FlashCardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold set data.
        final String SQL_CREATE_SET_TABLE = "CREATE TABLE " + SetEntry.TABLE_NAME + " (" +
                SetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SetEntry.COLUMN_SET_ID + " TEXT UNIQUE NOT NULL, " +
                SetEntry.COLUMN_SET_STUDIED + " INTEGER NOT NULL, " +
                SetEntry.COLUMN_SET_URL + " TEXT, " +
                SetEntry.COLUMN_SET_TITLE + " TEXT, " +
                SetEntry.COLUMN_SET_CREATED_BY + " TEXT " +
                " );";
        // Create a table to hold Term data i.e., data for each card.
        final String SQL_CREATE_TERM_TABLE = "CREATE TABLE " + TermEntry.TABLE_NAME + " (" +
                 TermEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 TermEntry.COLUMN_SET_ID + " TEXT NOT NULL, " +
                 TermEntry.COLUMN_TERM + " TEXT, " +
                 TermEntry.COLUMN_DEFINITION + " TEXT, " +
                 TermEntry.COLUMN_IMAGE + " TEXT, " +
                 TermEntry.COLUMN_RANK + " INTEGER " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_SET_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TERM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Data can always be obtained from the API, so the tables can be dropped if the
        // database is upgraded.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SetEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TermEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

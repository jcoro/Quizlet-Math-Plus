package net.coronite.quizlet_math_plus.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class FlashCardProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FlashCardDbHelper mDbHelper;

    static final int SETS = 100;
    static final int TERMS = 102;
    static final int SETS_TYPE = 103;
    static final int TERMS_OF_SET = 104;

    private static final SQLiteQueryBuilder sSetQueryBuilder;
    private static final SQLiteQueryBuilder sTermQueryBuilder;

    static {
        sSetQueryBuilder = new SQLiteQueryBuilder();
        sSetQueryBuilder.setTables(FlashCardContract.SetEntry.TABLE_NAME);
        sTermQueryBuilder = new SQLiteQueryBuilder();
        sTermQueryBuilder.setTables(FlashCardContract.TermEntry.TABLE_NAME);
    }

    private static final String getSetsOfTypeSelection = FlashCardContract.SetEntry.TABLE_NAME+
            "." + FlashCardContract.SetEntry.COLUMN_SET_STUDIED + " = ? ";

    private static final String getAllSetsSelection = FlashCardContract.SetEntry.TABLE_NAME+
            "." + FlashCardContract.SetEntry.COLUMN_SET_STUDIED + " = ? ";

    private static final String getAllTermsSelection = FlashCardContract.TermEntry.TABLE_NAME+
            "." + FlashCardContract.TermEntry.COLUMN_SET_ID + " = ? ";

    private static final String getTermsofSetSelection = FlashCardContract.TermEntry.TABLE_NAME+
            "." + FlashCardContract.TermEntry.COLUMN_SET_ID + " = ? ";

    private Cursor getAllSetsSetting(String[] projection, String[] selectionArgs, String sortOrder) {
        String selection = getAllSetsSelection;
        return sSetQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection, //columns: quizlet_set_id, title (null = all)
                selection,  // set.set_studied = ? (null = all)
                selectionArgs, // fill in ?s set_studied = 0 or 1;
                null,
                null,
                sortOrder);

    }

    private Cursor getAllTermsSetting(String[] projection, String[] selectionArgs, String sortOrder) {
        String selection = getAllTermsSelection;
        return sTermQueryBuilder.query(
                mDbHelper.getReadableDatabase(),                // Database
                projection,                                     // columns: term, definition, image, rank (null = all)
                selection,                                      // (null = all)
                selectionArgs,                                  // will be null
                null,
                null,
                sortOrder);

    }

    private Cursor getSetsOfTypeSetting(Uri uri, String[] projection, String[] selectionArgs, String sortOrder ) {
        String selection = getSetsOfTypeSelection;
        // FlashCardContract.SetEntry.TABLE_NAME+ "." + FlashCardContract.SetEntry.COLUMN_SET_STUDIED + " = ? ";
        return sSetQueryBuilder.query(
                mDbHelper.getReadableDatabase(),                // SQLiteDatabase Database
                projection,                                     // String[] columns (null = all)
                selection,                                      // String "set.set_studied = ?"
                selectionArgs,                                  // String[] fill in ?s studied_sets = 0 or 1;
                null,
                null,
                sortOrder);
    }

    private Cursor getTermsOfSetSetting(Uri uri, String[] projection, String[] selectionArgs, String sortOrder ) {
        String selection = getTermsofSetSelection;
        // FlashCardContract.TermEntry.TABLE_NAME+ "." + FlashCardContract.TermEntry.COLUMN_SET_ID + " = ? ";
        return sTermQueryBuilder.query(                         // SQLiteQueryBuilder.query
                mDbHelper.getReadableDatabase(),                // SQLiteDatabase Database
                projection,                                     // String[] columns (null = all)
                selection,                                      // String filter "term.set_id = ?"
                selectionArgs,                                  // String[] fill in ?s set_id = ?;
                null,
                null,
                sortOrder);
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FlashCardContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, "set", SETS);
        matcher.addURI(authority, "term", TERMS);
        matcher.addURI(authority, "set/*", SETS_TYPE);
        matcher.addURI(authority, "term/*", TERMS_OF_SET);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case SETS:
                return FlashCardContract.SetEntry.CONTENT_TYPE;
            case TERMS:
                return FlashCardContract.TermEntry.CONTENT_TYPE;
            case SETS_TYPE:
                return FlashCardContract.TermEntry.CONTENT_TYPE;
            case TERMS_OF_SET:
                return FlashCardContract.TermEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FlashCardDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case SETS:
                Log.d("Provider - SETS", uri.toString());
                cursor = mDbHelper.getReadableDatabase().query( // SQLiteDatabase.query
                        FlashCardContract.SetEntry.TABLE_NAME,  // String table name;
                        projection,                             // String[] columns
                        selection,                              // String rows i.e., Where clause
                        selectionArgs,                          // String[] Replace ?s in where clause
                        null,                                   // String GroupBy
                        null,                                   // String Having
                        sortOrder                               // String Sort Order e.g., ASC DESC
                );
                break;
            case TERMS:
                cursor = mDbHelper.getReadableDatabase().query(
                        FlashCardContract.TermEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SETS_TYPE:
                Log.d("Provider - SETS_TYPE", uri.toString());
                cursor = getSetsOfTypeSetting(uri, projection, selectionArgs, sortOrder);
                break;
            case TERMS_OF_SET:
                cursor = getTermsOfSetSetting(uri, projection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null){
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case TERMS: {
                long _id = db.insert(FlashCardContract.TermEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FlashCardContract.TermEntry.buildTermUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SETS: {
                long _id = db.insert(FlashCardContract.SetEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FlashCardContract.SetEntry.buildSetUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match){
            case TERMS: {
                rowsDeleted = db.delete(FlashCardContract.TermEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SETS: {
                rowsDeleted = db.delete(FlashCardContract.SetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case TERMS: {
                rowsUpdated = db.update(FlashCardContract.TermEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case SETS: {
                rowsUpdated = db.update(FlashCardContract.SetEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case TERMS: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FlashCardContract.TermEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return returnCount;
            }
            case SETS: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FlashCardContract.SetEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return returnCount;
            }
            default: return super.bulkInsert(uri, values);

        }

    }
}

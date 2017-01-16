package net.coronite.quizlet_math_plus.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table, column and URI names for the database.
 */

public class FlashCardContract {

    static final String CONTENT_AUTHORITY = "net.coronite.quizlet_math_plus";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_SET = "set";
    private static final String PATH_TERM = "term";

    /**
     * The {@code SetEntry} class defines the URIs, columns, and content type for
     * the flash card sets.
     */
    public static final class SetEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SET).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET;
        //public static final String CONTENT_ITEM_TYPE =
        //        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET;

        static final String TABLE_NAME = "CARDSET";
        public static final String ID = "_id";
        public static final String COLUMN_SET_ID = "quizlet_set_id";
        public static final String COLUMN_SET_STUDIED = "set_studied";
        public static final String COLUMN_SET_URL = "set_url";
        public static final String COLUMN_SET_TITLE = "set_title";
        public static final String COLUMN_SET_CREATED_BY = "set_created_by";

        /**
         * Builds a {@code Uri} for an individual flash card set.
         * @param id - the unique id for the set.
         * @return - the {@code Uri} for the flash card set.
         */
        static Uri buildSetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
            // content://net.coronite.quizlet_math_plus/set/#
        }
    }

    /**
     * The {@code TermEntry} class defines the URIs, columns, and content type for
     * the individual flash card terms.
    */
    public static final class TermEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TERM).build();

        static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TERM;
        // public static final String CONTENT_ITEM_TYPE =
        //        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TERM;

        static final String TABLE_NAME = "TERM";
        public static final String ID = "_id";
        public static final String COLUMN_SET_ID = "quizlet_set_id";
        public static final String COLUMN_TERM = "term";
        public static final String COLUMN_DEFINITION = "definition";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_RANK = "rank";

        /**
         * Builds a {@code Uri} for an individual flash card term.
         * @param id - the unique id for the term
         * @return - the {@code Uri} for the flash card term.
         */
        static Uri buildTermUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
            // content://net.coronite.quizlet_math_plus/term/#
        }

    }
}

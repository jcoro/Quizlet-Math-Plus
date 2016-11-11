package net.coronite.quizlet_math_plus.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FlashCardContract {

    public static final String CONTENT_AUTHORITY = "net.coronite.quizlet_math_plus";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SET = "sets";
    public static final String PATH_TERM = "terms";

    public static final class SetEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SET).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SET;

        public static final String TABLE_NAME = "set";
        public static final String COLUMN_SET_ID = "quizlet_set_id";
        public static final String COLUMN_SET_STUDIED = "set_studied";
        public static final String COLUMN_SET_URL = "set_url";
        public static final String COLUMN_SET_TITLE = "set_title";

        public static Uri buildSetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // public static Uri buildUserSetUri() {
        //     return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_SET_STUDIED, "0").build();
        // }
        // public static Uri buildStudiedSetUri() {
        //     return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_SET_STUDIED, "1").build();
        // }
    }

    public static final class TermEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TERM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TERM;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TERM;

        public static final String TABLE_NAME = "term";
        public static final String COLUMN_SET_ID = "quizlet_set_id";
        public static final String COLUMN_TERM = "term";
        public static final String COLUMN_DEFINITION = "definition";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_RANK = "rank";

        public static Uri buildTermUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTermUriWithSetId(String setId) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_SET_ID, setId).build();
        }
    }
}

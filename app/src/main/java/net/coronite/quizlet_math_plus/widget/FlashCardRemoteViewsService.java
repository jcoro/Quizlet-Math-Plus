package net.coronite.quizlet_math_plus.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import net.coronite.quizlet_math_plus.R;
import net.coronite.quizlet_math_plus.data.FlashCardContract;


/**
 * The {@code FlashCardRemoteViewsService } allows our remote adapter to request remote views
 * for creating a list in our home page widget.
 */
public class FlashCardRemoteViewsService extends RemoteViewsService {

    private static final String EXTRA_SET_ID = "SET_ID";
    private static final String EXTRA_SET_TITLE = "SET_TITLE";
    private static final String EXTRA_SET_CREATED_BY = "SET_CREATED_BY";

    private static final String[] SET_COLUMNS = new String[]{
            FlashCardContract.SetEntry.ID,
            FlashCardContract.SetEntry.COLUMN_SET_ID,
            FlashCardContract.SetEntry.COLUMN_SET_STUDIED,
            FlashCardContract.SetEntry.COLUMN_SET_URL,
            FlashCardContract.SetEntry.COLUMN_SET_TITLE,
            FlashCardContract.SetEntry.COLUMN_SET_CREATED_BY
    };

    // these indices must match the projection
    public static final int INDEX_COLUMN_AUTO_ID = 0;
    public static final int INDEX_COLUMN_SET_ID = 1;
    @SuppressWarnings("unused")
    public static final int INDEX_COLUMN_SET_STUDIED = 2;
    @SuppressWarnings("unused")
    public static final int INDEX_COLUMN_SET_URL = 3;
    public static final int INDEX_COLUMN_SET_TITLE = 4;
    public static final int INDEX_COLUMN_SET_CREATED_BY = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // Reset the identity of the incoming IPC on the current thread.
                // so any of our other concurrent permission checks pass
                final long identityToken = Binder.clearCallingIdentity();

                Uri uri = FlashCardContract.SetEntry.CONTENT_URI;

                data = getContentResolver().query(
                        uri,
                        SET_COLUMNS,    // projection
                        null,           // selection
                        null,           // selectionArgs
                        null);          // sort order

                //Restore the identity of the incoming IPC on the current thread
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);

                views.setTextViewText(R.id.widget_created_by, data.getString(INDEX_COLUMN_SET_CREATED_BY));
                views.setTextViewText(R.id.widget_set_title, data.getString(INDEX_COLUMN_SET_TITLE));

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(EXTRA_SET_ID, data.getString(INDEX_COLUMN_SET_ID));
                fillInIntent.putExtra(EXTRA_SET_TITLE, data.getString(INDEX_COLUMN_SET_TITLE));
                fillInIntent.putExtra(EXTRA_SET_CREATED_BY, data.getString(INDEX_COLUMN_SET_CREATED_BY));
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                // Get the row ID for the view at the specified position
                if (data != null && data.moveToPosition(position)) {
                    return data.getLong(INDEX_COLUMN_AUTO_ID);
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
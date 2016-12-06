package net.coronite.quizlet_math_plus.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import net.coronite.quizlet_math_plus.DetailActivity;
import net.coronite.quizlet_math_plus.MainActivity;
import net.coronite.quizlet_math_plus.R;
import net.coronite.quizlet_math_plus.sync.FlashCardSyncAdapter;

/**
 * AppWidgetProvider extends BroadcastReceiver and will respond to Broadcasts from the SyncAdapter
 */
public class FlashCardAppWidgetProvider extends AppWidgetProvider {

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        // Tapping the widget (but not a particular item [below] launches the MainActivity)
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_container);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        setRemoteAdapter(context, views);

        Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)  //clickIntentTemplate will be topmost activity in synthesized task stack
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);

        // .getPending intent creates the PendingIntent from the TaskStack Builder
        // FLAG_UPDATE_CURRENT allows the extras to be different for each intent in the list.

        // .setPendingIntentTemplate is used to set a single PendingIntent on the collection and individual
        // items can differentiate their on-click behavior using setOnClickFillInIntent(int, Intent) (In FlashCardRemoteViewsService).

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (FlashCardSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, FlashCardRemoteViewsService.class));
    }
}


package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

/**
 * Created by roide on 1/16/16.
 */
public class ScoreWidgetProvider extends AppWidgetProvider
{
    private static final String LOG_TAG = ScoreWidgetProvider.class.getSimpleName();

    public static final String TOAST_ACTION = "barqsoft.footballscores.TOAST_ACTION";
    public static final String EXTRA_ITEM = "barqsoft.footballscores.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d(LOG_TAG, "onUpdate::" + appWidgetIds.length);
        for(int i = 0; i < appWidgetIds.length; ++ i)
        {

            Intent intent = new Intent(context, ScoreWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout
                    .scores_widget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.widget_list_view, intent);

            rv.setEmptyView(R.id.widget_list_view, R.id.widget_empty);

            Intent toastIntent = new Intent(context, ScoreWidgetProvider.class);
            toastIntent.setAction(ScoreWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int
            appWidgetId, Bundle newOptions)
    {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "onReceive");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(TOAST_ACTION))
        {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Log.d(LOG_TAG, "Touched view " + viewIndex);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds)
    {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}

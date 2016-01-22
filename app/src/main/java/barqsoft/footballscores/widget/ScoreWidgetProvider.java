package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.util.AlarmUtil;
import barqsoft.footballscores.util.AppSharedPref;
import barqsoft.footballscores.util.Utilies;

/**
 * Created by roide on 1/16/16.
 */
public class ScoreWidgetProvider extends AppWidgetProvider
{
    public static final String TOAST_ACTION = "barqsoft.footballscores.TOAST_ACTION";
    public static final String EXTRA_ITEM = "barqsoft.footballscores.EXTRA_ITEM";
    private static final String LOG_TAG = ScoreWidgetProvider.class.getSimpleName();
    private static final String ACTION_BACK = "barqsoft.footballscores.BACK_ACTION";
    private static final String ACTION_NEXT = "barqsoft.footballscores.NEXT_ACTION";
    private static final String ARG_WIDGET_ID = "barqsoft.footballscores.WID";

    private void setAdapter(int wid, long time, AppWidgetManager appWidgetManager, Context context)
    {
        Log.d(LOG_TAG, "widId=" + wid);
        int dateIndex = AppSharedPref.getWidgetDateIndex(wid, context);

        Intent intent = new Intent(context, ScoreWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, wid);
        intent.putExtra(ScoreWidgetRemoteViewFactory.ARG_DATE_TIME, time);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.scores_widget_layout);
        rv.setRemoteAdapter(R.id.widget_list_view, intent);

        rv.setEmptyView(R.id.widget_list_view, R.id.widget_empty);
        rv.setTextViewText(R.id.widget_date, Utilies.getDayName(context, getTime(dateIndex)));
        setNextButton(rv, wid, null, context);
        setPrevButton(rv, wid, null, context);

        Intent toastIntent = new Intent(context, ScoreWidgetProvider.class);
        toastIntent.setAction(ScoreWidgetProvider.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, wid);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widget_list_view, toastPendingIntent);

        if(dateIndex >= 2)
        {
            rv.setViewVisibility(R.id.widget_next_button, View.INVISIBLE);
        }
        else if(dateIndex <= - 2)
        {
            rv.setViewVisibility(R.id.widget_prev_button, View.INVISIBLE);
        }
        else
        {
            rv.setViewVisibility(R.id.widget_next_button, View.VISIBLE);
            rv.setViewVisibility(R.id.widget_prev_button, View.VISIBLE);
        }
        appWidgetManager.updateAppWidget(wid, rv);
    }

    private void setNextButton(RemoteViews remoteView, int wid, String buttonText, Context context)
    {
        remoteView.setOnClickPendingIntent(R.id.widget_next_button, getNextButtonIntent(wid,
                context));
    }

    private void setPrevButton(RemoteViews remoteView, int wid, String buttonText, Context context)
    {
        remoteView.setOnClickPendingIntent(R.id.widget_prev_button, getPrevButtonIntent(wid,
                context));
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "onReceive::" + intent.getAction());
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(TOAST_ACTION))
        {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Log.d(LOG_TAG, "Touched view " + viewIndex);
        }
        else if(intent.getAction().equals(ACTION_BACK))
        {
            triggerBackDateChange(intent.getIntExtra(ARG_WIDGET_ID, - 1), context);
        }
        else if(intent.getAction().equals(ACTION_NEXT))
        {
            triggerNextDayChange(intent.getIntExtra(ARG_WIDGET_ID, - 1), context);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d(LOG_TAG, "onUpdate::" + appWidgetIds.length);
        for(int i = 0; i < appWidgetIds.length; ++ i)
        {
            AppSharedPref.setWidgetDateIndex(appWidgetIds[i], 0, context);
            setAdapter(appWidgetIds[i], System.currentTimeMillis(), appWidgetManager, context);
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
        Log.d(LOG_TAG, "onDelete");
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
        Log.d(LOG_TAG, "widget:Enabled");
        AppSharedPref.setHasWidgets(true, context);
        AlarmUtil.enableBootReceiver(true, context);
        AlarmUtil.setupAlarm(context);
    }

    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
        Log.d(LOG_TAG, "Widget:Disabled");
        AlarmUtil.enableBootReceiver(false, context);
        AlarmUtil.cancelAlarm(context);
        AppSharedPref.setHasWidgets(false, context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds)
    {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    private void triggerBackDateChange(int wid, Context context)
    {
        Log.d(LOG_TAG, "triggerBackChange=" + wid);
        if(wid == - 1)
        {
            return;
        }
        int dateIndex = AppSharedPref.getWidgetDateIndex(wid, context);
        if(dateIndex == - 2)
        {
            //not allowed - should never happen
            return;
        }
        AppSharedPref.setWidgetDateIndex(wid, -- dateIndex, context);
        long prevTime = getTime(dateIndex);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        setAdapter(wid, prevTime, manager, context);
        Log.d(LOG_TAG, "dateIndex=" + dateIndex);
    }

    private void triggerNextDayChange(int wid, Context context)
    {
        Log.d(LOG_TAG, "triggerNextDayChange=" + wid);
        if(wid == - 1)
        {
            return;
        }
        int dateIndex = AppSharedPref.getWidgetDateIndex(wid, context);
        if(dateIndex == 2)
        {
            //not allowed - should never happen
            return;
        }
        AppSharedPref.setWidgetDateIndex(wid, ++ dateIndex, context);
        long nextTime = getTime(dateIndex);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        setAdapter(wid, nextTime, manager, context);
        Log.d(LOG_TAG, "dateIndex=" + dateIndex);
    }

    private long getTime(int dateIndex)
    {
        long time = System.currentTimeMillis() + (dateIndex * 86400000);
        return time;
    }

    private PendingIntent getPrevButtonIntent(int wid, Context context)
    {
        Intent intent = new Intent(context, ScoreWidgetProvider.class);
        intent.setAction(ACTION_BACK);
        intent.putExtra(ARG_WIDGET_ID, wid);
        return PendingIntent.getBroadcast(context, wid, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private PendingIntent getNextButtonIntent(int wid, Context context)
    {
        Intent intent = new Intent(context, ScoreWidgetProvider.class);
        intent.setAction(ACTION_NEXT);
        intent.putExtra(ARG_WIDGET_ID, wid);
        return PendingIntent.getBroadcast(context, wid, intent, PendingIntent.FLAG_ONE_SHOT);
    }
}

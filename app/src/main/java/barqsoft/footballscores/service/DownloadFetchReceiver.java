package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import barqsoft.footballscores.R;
import barqsoft.footballscores.widget.ScoreWidgetProvider;

/**
 * Created by roide on 1/17/16.
 */
public class DownloadFetchReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = DownloadFetchReceiver.class.getSimpleName();
    private static DownloadFetchReceiver mFetchReceiver;

    private DownloadFetchReceiver()
    {
        //do nothing
    }

    public static DownloadFetchReceiver getInstance()
    {
        if(mFetchReceiver == null)
        {
            mFetchReceiver = new DownloadFetchReceiver();
        }
        return mFetchReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "onReceive" + intent);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, ScoreWidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(componentName);
        Log.d(LOG_TAG, "widgetLen=" + ids.length);
        for(int i = 0; i < ids.length; i++)
        {
            int widgetId = ids[i];
            Log.d(LOG_TAG, "Update::widgetId=" + widgetId);
            manager.notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view);
        }

    }
}

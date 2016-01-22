package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.util.AlarmUtil;
import barqsoft.footballscores.util.AppSharedPref;

/**
 * Created by roide on 1/18/16.
 */
public class AppBootReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = AppBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "onReceive");
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            // Set the alarm here.
            Log.d(LOG_TAG, "boot Received - set Alarm");
            //find out if there are any widgets
            //if there are widgets set up the alarm
            //if there are no widgets - alarms are not required
            if(AppSharedPref.hasWidgets(context))
            {
                AlarmUtil.setupAlarm(context);
            }
        }
    }
}

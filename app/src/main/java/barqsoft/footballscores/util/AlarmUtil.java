package barqsoft.footballscores.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;

import barqsoft.footballscores.service.AppBootReceiver;
import barqsoft.footballscores.service.MidnightAlarmReceiver;

/**
 * Created by roide on 1/17/16.
 */
public class AlarmUtil
{
    private static final int ALARM_REQUEST_CODE = 101;

    public static void setupAlarm(Context context)
    {
        Intent intent = new Intent(context, MidnightAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE,
                intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 01);
        calendar.set(Calendar.SECOND, 02);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmMgr != null)
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, MidnightAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE,
                intent, 0);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmMgr != null)
        {
            alarmMgr.cancel(alarmIntent);
            Log.d("kaushik", "Repeat Alarm Cancel");
        }

    }

    public static void enableBootReceiver(boolean enable, Context context)
    {
        ComponentName receiver = new ComponentName(context, AppBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        if(enable)
        {
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
        else
        {
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}

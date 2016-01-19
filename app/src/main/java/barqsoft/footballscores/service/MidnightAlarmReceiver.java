package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by roide on 1/17/16.
 */
public class MidnightAlarmReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = MidnightAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "alarmOnReceive");
    }
}

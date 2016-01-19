package barqsoft.footballscores.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import barqsoft.footballscores.service.DownloadFetchReceiver;
import barqsoft.footballscores.service.ScoreFetchService;

/**
 * Created by roide on 1/17/16.
 */
public class ScoreUpdateUtil
{
    public static void update(Context context)
    {
        IntentFilter intentFilter = new IntentFilter(ScoreFetchService.BROADCAST_ACTION);
        DownloadFetchReceiver receiver = DownloadFetchReceiver.getInstance();
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
        Intent service_start = new Intent(context, ScoreFetchService.class);
        context.startService(service_start);
    }
}

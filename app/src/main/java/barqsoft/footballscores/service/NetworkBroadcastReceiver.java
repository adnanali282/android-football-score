package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import barqsoft.footballscores.util.ScoreUpdateUtil;

/**
 * Created by roide on 1/17/16.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = NetworkBroadcastReceiver.class.getSimpleName();

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        mContext = context;
        if(intent.getExtras() != null)
        {
            final ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnectedOrConnecting())
            {
                Log.i(LOG_TAG, "Network " + networkInfo.getTypeName() + " connected");
                triggerConnected();
            }
            else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean
                    .FALSE))
            {
                Log.d(LOG_TAG, "There's no network connectivity");
            }
        }
    }

    private Context getContext()
    {
        return mContext;
    }

    private void triggerConnected()
    {
        ScoreUpdateUtil.update(getContext());
    }
}

package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.db.ScoresAdapter;
import barqsoft.footballscores.util.Utilies;

/**
 * Created by roide on 1/16/16.
 */
public class ScoreWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory
{
    private static final String LOG_TAG = ScoreWidgetRemoteViewFactory.class.getSimpleName();
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private int mAppWidgetId;
    private Context mContext;
    private Cursor mCursor;
    private int mCount;

    public ScoreWidgetRemoteViewFactory(Context context, Intent intent)
    {
        Log.d(LOG_TAG, "ScoreWidgetRemoteViewFactory");
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate()
    {
        Log.d(LOG_TAG, "onCreate");
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
    }

    @Override
    public void onDataSetChanged()
    {
        Log.d(LOG_TAG, "onDataSetChanged");
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
        Date fragmentdate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        String[] selectionArgDate = new String[1];
        selectionArgDate[0] = mformat.format(fragmentdate);

        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
        mCursor = mContext.getContentResolver().query(uri, null, null, selectionArgDate, null);

        if(mCursor == null)
        {
            Log.d(LOG_TAG, "Widget - Cursor null");
            return;
        }
        mCount = mCursor.getCount();
    }

    @Override
    public void onDestroy()
    {
        Log.d(LOG_TAG, "onDestroy");
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        mWidgetItems.clear();
    }

    @Override
    public int getCount()
    {
        Log.d(LOG_TAG, "getCount::" + mCount);
        return mCount;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d(LOG_TAG, "getViewAt::" + position);

        mCursor.moveToPosition(position);
        // position will always range from 0 to getCount() - 1.

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.scores_widget_item);
        rv.setTextViewText(R.id.widget_home_name, mCursor.getString(ScoresAdapter.COL_HOME));
        rv.setTextViewText(R.id.widget_away_name, mCursor.getString(ScoresAdapter.COL_AWAY));
        rv.setTextViewText(R.id.widget_date_textview, mCursor.getString(ScoresAdapter.COL_MATCHTIME));
        //rv.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(mCursor.getString(ScoresAdapter.COL_HOME)));
        //rv.setImageViewResource(R.id.widget_away_crest, Utilies.getTeamCrestByTeamName(mCursor.getString(ScoresAdapter.COL_AWAY)));
        String scoreText = Utilies.getScores(mCursor.getInt(ScoresAdapter.COL_HOME_GOALS), mCursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
        rv.setTextViewText(R.id.widget_score_textview, scoreText);

        Log.d(LOG_TAG, "homeTeam=" + mCursor.getString(ScoresAdapter.COL_HOME));

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(ScoreWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        //rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            System.out.println("Loading view " + position);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the remote views object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}

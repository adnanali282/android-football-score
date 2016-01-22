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
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.db.ScoresAdapter;
import barqsoft.footballscores.util.Utilies;

/**
 * Created by roide on 1/16/16.
 */
public class ScoreWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory
{
    public static final String ARG_DATE_TIME = "arg-date-time";
    private static final String LOG_TAG = ScoreWidgetRemoteViewFactory.class.getSimpleName();
    private long mDateTime;
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
        mDateTime = intent.getLongExtra(ARG_DATE_TIME, System.currentTimeMillis());
    }

    @Override
    public void onCreate()
    {
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onDataSetChanged()
    {
        Log.d(LOG_TAG, "onDataSetChanged::");
        if(mCursor != null)
        {
            mCursor.close();
            mCursor = null;
        }

        Date fragmentdate = new Date(mDateTime);
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        final String[] selectionArgDate = new String[1];
        selectionArgDate[0] = mformat.format(fragmentdate);

        Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
        mCursor = mContext.getContentResolver().query(uri, null, null, selectionArgDate, null);
        mCount = mCursor.getCount();
    }

    @Override
    public void onDestroy()
    {
        if(mCursor != null)
        {
            mCursor.close();
        }
        mCursor = null;
    }

    @Override
    public int getCount()
    {
        return mCount;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        mCursor.moveToPosition(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.scores_widget_item);
        rv.setTextViewText(R.id.widget_home_name, mCursor.getString(ScoresAdapter.COL_HOME));
        rv.setTextViewText(R.id.widget_away_name, mCursor.getString(ScoresAdapter.COL_AWAY));
        rv.setTextViewText(R.id.widget_date_textview, mCursor.getString(ScoresAdapter
                .COL_MATCHTIME));
        String scoreText = Utilies.getScores(mCursor.getInt(ScoresAdapter.COL_HOME_GOALS),
                mCursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
        rv.setTextViewText(R.id.widget_score_textview, scoreText);

        Bundle extras = new Bundle();
        extras.putInt(ScoreWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

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

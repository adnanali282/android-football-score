package barqsoft.footballscores.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by roide on 1/16/16.
 */
public class ScoreWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new ScoreWidgetRemoteViewFactory(getApplicationContext(), intent);
    }
}

package barqsoft.footballscores.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by roide on 1/18/16.
 */
public class AppSharedPref
{
    private static final String PREFS_NAME = "pref";
    private static final String HAS_WIDGETS = "has-widgets";

    public static boolean hasWidgets(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        boolean alarmSet = settings.getBoolean(HAS_WIDGETS, false);
        return alarmSet;
    }

    public static void setHasWidgets(boolean hasWidgets, Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(HAS_WIDGETS, hasWidgets);
        editor.apply();
    }

    public static int getWidgetDateIndex(int wid, Context context)
    {
        String widIdKey = String.valueOf(wid);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        int dateIndex = settings.getInt(widIdKey, 0);
        return dateIndex;
    }

    public static void setWidgetDateIndex(int wid, int index, Context context)
    {
        String widIdKey = String.valueOf(wid);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(widIdKey, index);
        editor.apply();
    }
}

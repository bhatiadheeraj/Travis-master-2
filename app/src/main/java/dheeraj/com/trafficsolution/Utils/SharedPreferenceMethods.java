package dheeraj.com.trafficsolution.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceMethods
{
    public static Context appContext;
    private static String PREFERENCE="travis_trafficApp_SharedPreference";
    public static final String SHARED_PREFERENCE_NAME="travis_trafficApp_SharedPreference";

    // Variables
    public static final String IS_LOGGED_IN = "app_name";
    public static final boolean PEDESTRIAN_MODE = true;

    public static SharedPreferences.Editor getEditor(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return editor;
    }
    public static SharedPreferences getSharedPreference(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    public static boolean getBoolean(Context context , String name)
    {
        SharedPreferences sharedPreferences=getSharedPreference(context);
        return sharedPreferences.getBoolean(name,false);
    }
    public static void setBoolean(Context context , String name , boolean value)
    {
        SharedPreferences.Editor editor=getEditor(context);
        editor.putBoolean(name,value);
        editor.commit();
    }
    public static String getString(Context context , String name)
    {
        SharedPreferences sharedPreferences=getSharedPreference(context);
        return sharedPreferences.getString(name, "");
    }
    public static void setString(Context context , String name , String value)
    {
        SharedPreferences.Editor editor=getEditor(context);
        editor.putString(name, value);
        editor.commit();
    }

    // for username string preferences
    public static void setDoubleSharedPreference(Context context, String name,double value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(name, (float) value);
        editor.commit();
    }

    public static Double getDoubleSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return (double)settings.getFloat (name, 0.0f);
    }

}

package net.validcat.fishing.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.weather.WeatherSyncFetcher;

/**
 * Created by Dobrunov on 11.12.2015.
 */
public class PrefUtils {

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key), context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }

    public static String formatTemperature(Context context, double temperature) {
        // Data stored in Celsius by default.  If user prefers to see in Fahrenheit, convert
        // the values here.
        String suffix = "\u00B0";
        if (!isMetric(context)) {
            temperature = (temperature * 1.8) + 32;
        } else {
            temperature = temperature - 273;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    /**
     *
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
    @SuppressWarnings("ResourceType")
    static public @WeatherSyncFetcher.LocationStatus
    int getLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_location_status_key), Constants.LOCATION_STATUS_UNKNOWN);
    }

    /**
     * Resets the location status.  (Sets it to WeatherSyncFetcher.LOCATION_STATUS_UNKNOWN)
     * @param c Context used to get the SharedPreferences
     */
    static public void resetLocationStatus(Context c){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_location_status_key), Constants.LOCATION_STATUS_UNKNOWN);
        spe.apply();
    }

    public static int formatWeatherIdToIconsCode(int id) {
        int res = R.drawable.ic_sunny_check;

        if (isBetween(id, 200, 299))
            res = R.drawable.ic_thunder_check;
        else if (isBetween(id, 300, 499))
            res = R.drawable.ic_rain_check;
        else if (isBetween(id, 600, 699))
            res = R.drawable.ic_snow_check;
        else if (isBetween(id, 700, 799))
            res = R.drawable.ic_mist_check;
//        else if (id == 800)
//            res = R.drawable.ic_sunny_check;
        else if (id == 801)
            res = R.drawable.ic_partly_cloud_check;
        else if (isBetween(id, 802, 804))
            res = R.drawable.ic_cloudy_check;
        else if (isBetween(id, 900, 999))
            res = R.drawable.ic_tornado_check;

        return res;
    }

    public static int formatWeatherSeletedToIconsCode(int selected) {
        switch (selected) {
            case 0:
                return R.drawable.ic_sunny_check;
            case 1:
                return R.drawable.ic_partly_cloud_check;
            case 2:
                return R.drawable.ic_cloudy_check;
            case 3:
                return R.drawable.ic_rain_check;
            case 4:
                return R.drawable.ic_snow_check;
            case 5:
                return R.drawable.ic_mist_check;
            case 6:
                return R.drawable.ic_thunder_check;
            case 7:
                return R.drawable.ic_tornado_check;
            default:
                return R.drawable.ic_sunny;
        }
    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }


}

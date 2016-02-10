package net.validcat.fishing.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.weather.WeatherSyncFetcher;

public class PrefUtils {

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key), context.getString(R.string.pref_location_default));
    }

    public static double[] getCurrentLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        double latitude = Double.longBitsToDouble(
                prefs.getLong(Constants.PREFERENCES_LOCATION_LATITUDE, 0));
        double longitude = Double.longBitsToDouble(
                prefs.getLong(Constants.PREFERENCES_LOCATION_LONGITUDE, 0));

        double[] location = {latitude, longitude};

        return location;
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }

    public static String getFormattedTemp(Context context, int weatherTemp) {
        return context.getString(PrefUtils.isMetric(context) ?
                R.string.temp_formatted_cel : R.string.temp_formatted_far, weatherTemp);
    }

    public static String formatTemperature(Context context, double kelvin) {
        double celsiusTemp = kelvin - 273;
        double fahrenheitTemp = (celsiusTemp * 1.8) + 32;
//        return String.format(context.getString(R.string.format_temperature), isMetric(context) ? temperature - 273 : temperature);
        return String.format(context.getString(R.string.format_temperature), isMetric(context) ? celsiusTemp : fahrenheitTemp);
    }

    public static double formatTemperatureToMetrics(Context context, double kelvin) {
        double celsiusTemp =  kelvin - 273;
        double fahrenheitTemp = (celsiusTemp * 1.8) + 32;
            //temperature = (temperature * 1.8) + 32;
//        return isMetric(context) ? temperature - 273 : temperature;
          return isMetric(context) ? celsiusTemp : fahrenheitTemp;
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

    public static int formatWeatherIdToSelection(int id) {
        int sel = 0; // sunny

        if (isBetween(id, 200, 299))
            sel = 6;
        else if (isBetween(id, 300, 499))
            sel = 3;
        else if (isBetween(id, 600, 699))
            sel = 4;
        else if (isBetween(id, 700, 799))
            sel = 5;
        else if (id == 801)
            sel = 1;
        else if (isBetween(id, 802, 804))
            sel = 2;
        else if (isBetween(id, 900, 999))
            sel = 7;

        return sel;
    }

    public static int formatResIdToSelection(int id) {
        switch (id) {
            case R.drawable.ic_sunny_check:
                return 0;
            case R.drawable.ic_partly_cloud_check:
                return 1;
            case R.drawable.ic_cloudy_check:
                return 2;
            case R.drawable.ic_rain_check:
                return 3;
            case R.drawable.ic_snow_check:
                return 4;
            case R.drawable.ic_mist_check:
                return 5;
            case R.drawable.ic_thunder_check:
                return 6;
            case R.drawable.ic_tornado_check:
                return 7;
            default:
                return 0;
        }
    }

    public static int formatTacleSeletedToIconsCode(int selected) {
        switch (selected) {
            case 0:
                return R.drawable.ic_rod_check;
            case 1:
                return R.drawable.ic_spinning_check;
            case 2:
                return R.drawable.ic_feeder_check;
            case 3:
                return R.drawable.ic_distance_casting_check;
            case 4:
                return R.drawable.ic_ice_fishing_check;
            case 5:
                return R.drawable.ic_tip_up_check;
            case 6:
                return R.drawable.ic_hand_line_check;
            case 7:
                return R.drawable.ic_fly_fishing_check;
            default:
                return R.drawable.ic_rod_check;
        }
    }

    public static int formatTacleSeletedToTextView(int selected) {
        switch (selected) {
            case 0:
                return R.string.rod;
            case 1:
                return R.string.spinning;
            case 2:
                return R.string.feeder;
            case 3:
                return R.string.distance_casting;
            case 4:
                return R.string.ice_fishing_rod;
            case 5:
                return R.string.tip_up;
            case 6:
                return R.string.hand_line;
            case 7:
                return R.string.fly_fishing;
            default:
                return R.string.rod;
        }
    }

}

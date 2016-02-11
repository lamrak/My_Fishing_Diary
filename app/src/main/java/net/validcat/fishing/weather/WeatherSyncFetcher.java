package net.validcat.fishing.weather;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import net.validcat.fishing.BuildConfig;
import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.tools.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherSyncFetcher {
    public final String LOG_TAG = WeatherSyncFetcher.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Constants.LOCATION_STATUS_OK, Constants.LOCATION_STATUS_SERVER_DOWN, Constants.LOCATION_STATUS_SERVER_INVALID,  Constants.LOCATION_STATUS_UNKNOWN, Constants.LOCATION_STATUS_INVALID})
    public @interface LocationStatus {}

    private Context context;

    public WeatherSyncFetcher(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String onPerformSync(double lat, double lon) {
        Log.d(LOG_TAG, "Starting sync");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            Uri.Builder uri = new Uri.Builder()
                    .scheme(Constants.HTTP_SCHEME)
                    .authority(Constants.FORECAST_BASE_URL)
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("weather");
            if (lat != 0 && lon != 0) {
                Log.d(LOG_TAG, "fetch weather by current location");
                uri.appendQueryParameter(Constants.LAT_PARAM, String.valueOf(lat))
                    .appendQueryParameter(Constants.LON_PARAM, String.valueOf(lon));
            } else {
                Log.d(LOG_TAG, "fetch weather by preferences location");
                uri.appendQueryParameter(Constants.QUERY_PARAM, PrefUtils.getPreferredLocation(getContext()));
            }
            Uri builtUri = uri.appendQueryParameter(Constants.APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                setLocationStatus(getContext(), Constants.LOCATION_STATUS_SERVER_DOWN);
                return null ;
            }
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            setLocationStatus(getContext(), Constants.LOCATION_STATUS_SERVER_DOWN);
//        }
//        catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//            setLocationStatus(getContext(), Constants.LOCATION_STATUS_SERVER_INVALID);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;
    }

    public void getCurrentWeatherDataFromJson(String forecastJsonStr, IWeatherListener listener)
            throws JSONException {
        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            if (forecastJson.has(Constants.OWM_MESSAGE_CODE)) {
                int errorCode = forecastJson.getInt(Constants.OWM_MESSAGE_CODE);

                switch (errorCode) {
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        setLocationStatus(getContext(), Constants.LOCATION_STATUS_INVALID);
                        return;
                    default:
                        setLocationStatus(getContext(), Constants.LOCATION_STATUS_SERVER_DOWN);
                        return;
                }
            }

            int weatherId;

            JSONObject weatherObject = (JSONObject) forecastJson.getJSONArray(Constants.OWM_WEATHER).get(0);
            JSONObject mainObject = forecastJson.getJSONObject(Constants.OWM_DESCRIPTION);
            weatherId = weatherObject.getInt(Constants.OWM_WEATHER_ID);
            double temp = mainObject.getDouble(Constants.OWM_TEMPERATURE);
            double high = mainObject.getDouble(Constants.OWM_MAX);
            double low = mainObject.getDouble(Constants.OWM_MIN);

            setLocationStatus(getContext(), Constants.LOCATION_STATUS_OK);
            listener.onWeatherResult(weatherId, temp, high, low);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            setLocationStatus(getContext(), Constants.LOCATION_STATUS_SERVER_INVALID);
        }
    }

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param locationSetting The location string used to request updates from the server.
     * @param cityName A human-readable city name, e.g "Mountain View"
     * @param lat the latitude of the city
     * @param lon the longitude of the city
     * @return the row ID of the added location.
     */
    long addLocation(String locationSetting, String cityName, double lat, double lon) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = getContext().getContentResolver().query(
                FishingContract.LocationEntry.CONTENT_URI,
                new String[]{FishingContract.LocationEntry._ID},
                FishingContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor != null && locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(FishingContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(FishingContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(FishingContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(FishingContract.LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(FishingContract.LocationEntry.COLUMN_COORD_LONG, lon);

            // Finally, insert location data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    FishingContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        if (locationCursor != null)
            locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }

    /**
     * Sets the location status into shared preference.  This function should not be called from
     * the UI thread because it uses commit to write to the shared preferences.
     * @param c Context to get the PreferenceManager from.
     * @param locationStatus The IntDef value to set
     */
    static private void setLocationStatus(Context c, @LocationStatus int locationStatus){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();
        spe.putInt(c.getString(R.string.pref_location_status_key), locationStatus);
        spe.apply(); //commit()
    }

    public interface IWeatherListener {
        void onWeatherResult(int id, double temp, double high, double low);
    }
}
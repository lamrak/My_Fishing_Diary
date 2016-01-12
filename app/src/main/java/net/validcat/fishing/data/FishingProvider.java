package net.validcat.fishing.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FishingProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FishingDbHelper mOpenHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int FISHING = 200;
    static final int FISHING_BY_ID = 201;
    static final int LOCATION = 300;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static{
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();
        
        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                FishingContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
                        FishingContract.LocationEntry.TABLE_NAME +
                        " ON " + FishingContract.WeatherEntry.TABLE_NAME +
                        "." + FishingContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + FishingContract.LocationEntry.TABLE_NAME +
                        "." + FishingContract.LocationEntry._ID);
    }

    //location.location_setting = ?
    private static final String sLocationSettingSelection =
            FishingContract.LocationEntry.TABLE_NAME +
                    "." + FishingContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            FishingContract.LocationEntry.TABLE_NAME +
                    "." + FishingContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    FishingContract.WeatherEntry.COLUMN_DATE + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            FishingContract.LocationEntry.TABLE_NAME +
                    "." + FishingContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    FishingContract.WeatherEntry.COLUMN_DATE + " = ? ";

    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = FishingContract.WeatherEntry.getLocationSettingFromUri(uri);
        long startDate = FishingContract.WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == 0) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{locationSetting};
        } else {
            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
            selection = sLocationSettingWithStartDateSelection;
        }

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherByLocationSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = FishingContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date = FishingContract.WeatherEntry.getDateFromUri(uri);

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Weather
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_WEATHER, WEATHER);
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);
        // Location
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_LOCATION, LOCATION);
        // Fishing
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_FISHING, FISHING);
        sUriMatcher.addURI(FishingContract.CONTENT_AUTHORITY, FishingContract.PATH_FISHING + "/#", FISHING_BY_ID);

        return sUriMatcher;
    }

    /*
        Students: We've coded this for you.  We just create a new FishingDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new FishingDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            // Student: Uncomment and fill out these two cases
            case WEATHER_WITH_LOCATION_AND_DATE:
                return FishingContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return FishingContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER:
                return FishingContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return FishingContract.LocationEntry.CONTENT_TYPE;
            case FISHING:
                return FishingContract.FishingEntry.CONTENT_TYPE;
            case FISHING_BY_ID:
                return FishingContract.FishingEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case WEATHER_WITH_LOCATION_AND_DATE: {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            } // "weather/*"
            case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                break;
            }
            case WEATHER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FishingContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            } case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FishingContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            } case FISHING: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FishingContract.FishingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            } case FISHING_BY_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FishingContract.FishingEntry.TABLE_NAME,
                        projection,
                        FishingContract.FishingEntry.TABLE_NAME +
                                "." + FishingContract.FishingEntry._ID + " = ? ",
                        new String[]{FishingContract.FishingEntry.getFishingIdFromUri(uri)},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WEATHER: {
                normalizeDate(FishingContract.WeatherEntry.COLUMN_DATE, values);
                long _id = db.insert(FishingContract.WeatherEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FishingContract.WeatherEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LOCATION: {
                long _id = db.insert(FishingContract.LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FishingContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FISHING: {
//                normalizeDate(FishingContract.FishingEntry.COLUMN_DATE, values);
                long _id = db.insert(FishingContract.FishingEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FishingContract.FishingEntry.buildFishingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;
        if (null == selection) selection = "1";
        switch (match) {
            case WEATHER: {
                deletedRows = db.delete(FishingContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case LOCATION: {
                deletedRows = db.delete(FishingContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case FISHING_BY_ID:
                String id = uri.getLastPathSegment();
                selection = FishingContract.FishingEntry._ID + " = " +id;
                deletedRows = db.delete(FishingContract.FishingEntry.TABLE_NAME,selection,null);
                break;
            case FISHING: {
                deletedRows = db.delete(FishingContract.FishingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (deletedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }

    private void normalizeDate(String key, ContentValues values) {
        // normalize the date value
        if (values.containsKey(key)) {
            long dateValue = values.getAsLong(key);
            values.put(key, FishingContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case WEATHER:
                normalizeDate(FishingContract.WeatherEntry.COLUMN_DATE, values);
                rowsUpdated = db.update(FishingContract.WeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(FishingContract.LocationEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FISHING_BY_ID:
            case FISHING:
//                normalizeDate(FishingContract.FishingEntry.COLUMN_DATE, values);
                rowsUpdated = db.update(FishingContract.FishingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(FishingContract.WeatherEntry.COLUMN_DATE, value);
                        long _id = db.insert(FishingContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
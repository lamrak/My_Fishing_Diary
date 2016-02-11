package net.validcat.fishing.data;

public class Constants {
    public static final int ITEM_REQUEST = 0;
    public static final String FOLDER_NAME = "MyFishing";
    public static final String KEY_DATA = "data";
    public static final String EXTENSION_JPG = ".jpg";
    public static final int REQUEST_CODE_PHOTO = 301;
    public static final String DETAIL_KEY = "detail_id";

    public static final String DIALOG_KEY = "dialog";
    public static final String DELETE = "delete";
    public static final int REQUEST_TEMPERATURE = 101;
    public static final String EXTRA_TEMPERATURE = "temperature";
    public static final String VALIDATION_ERROR = "Add your fishing place";
    public static final String EXTRA_IMAGE_KEY = "imageKey";

    public static final String ACTION_DATA_UPDATED =
            "net.validcat.fishing.ACTION_DATA_UPDATED";
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public static final int LOCATION_STATUS_OK = 0;
    public static final int LOCATION_STATUS_SERVER_DOWN = 1;
    public static final int LOCATION_STATUS_SERVER_INVALID = 2;
    public static final int LOCATION_STATUS_UNKNOWN = 3;
    public static final int LOCATION_STATUS_INVALID = 4;
    public static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    public static final long LOCATION_MIN_TIME = 400;
    public static final float LOCATION_MIN_DISTANCE = 1;

    //Permissions
    public static final int PERMISSIONS_REQUEST_CAMERA = 191;
    public static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 192;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 193;

    //Weather
    public static final String HTTP_SCHEME = "http";
    public static final String OWM_TEMPERATURE = "temp";
    public static final String OWM_MAX = "temp_max";
    public static final String OWM_MIN = "temp_min";
    public static final String OWM_WEATHER = "weather";
    public static final String OWM_DESCRIPTION = "main";
    public static final String OWM_WEATHER_ID = "id";
    public static final String OWM_MESSAGE_CODE = "cod";
    public final static String FORECAST_BASE_URL = "api.openweathermap.org";
    public final static String QUERY_PARAM = "q";
    public final static String LAT_PARAM = "lat";
    public final static String LON_PARAM = "lon";
    public final static String APPID_PARAM = "APPID";

    public static final String PHOTO_DIALOG_KEY = "photo_dialog";
    public static final String IMAGE_URI = "image_uri";
    public static final int REQUEST_TAKE_PHOTO = 102;
    public static final int REQUEST_PICK_PHOTO = 103;
    public static final int PICK_PHOTO = 104;
    public static final String SPLIT_IMAGE_PATH_PATTERN = "\\s*,\\s*";
    public static final int THUMB_SIZE = 64;
    public static final String KEY_TITLE = "title";
    public static final String CAMERA_TIME_PATTERN = "yyyyMMdd_HHmmss";

    public static final String TACKLE_DIALOG_KEY = "tackle_dialog";
    public static final int REQUEST_TACKLE = 105;
    public static final String EXTRA_TACKLE_IMAGE_KEY = "tackle_imageKey";

    //Preferences
    public static final String PREFERENCES_LOCATION_LATITUDE = "latitude";
    public static final String PREFERENCES_LOCATION_LONGITUDE = "longitude";
}

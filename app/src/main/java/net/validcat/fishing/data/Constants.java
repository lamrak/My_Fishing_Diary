package net.validcat.fishing.data;

/**
 * Created by Dobrunov on 09.07.2015.
 */
public class Constants {
    public static final int ITEM_REQUEST = 0;
    public static final String FOLDER_NAME = "MyFishing";
    public static final String KEY_DATA = "data";
    public static final String EXTENSION_JPG = ".jpg";
    public static final int REQUEST_CODE_PHOTO = 301;
    public static final String DETAIL_KEY = "detail_id";
    public static final int HEIGHT_BITMAP = 200;

    public static final String DIALOG_KEY = "dialog";
    public static final String DELETE = "delete";
    public static final int REQUEST_TEMPERATURE = 101;
    public static final String EXTRA_TEMPERATURE = "temperature";



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
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

}

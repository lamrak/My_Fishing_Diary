package net.validcat.fishing.models;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.validcat.fishing.R;
import net.validcat.fishing.data.FishingContract;

public class FishingItem {
    private static final String LOG_TAG = FishingItem.class.getSimpleName();
    public final static String PLACE = "place";
    public final static String DATE = "date";
    public final static String ID = "id";
    public final static String DESCRIPTION = "description";
    public final static String PHOTO = "photo";

    public static String[] COLUMNS = {
        FishingContract.FishingEntry._ID,
        FishingContract.FishingEntry.COLUMN_PLACE,
        FishingContract.FishingEntry.COLUMN_DATE,
        FishingContract.FishingEntry.COLUMN_WEATHER,
        FishingContract.FishingEntry.COLUMN_DESCRIPTION,
        FishingContract.FishingEntry.COLUMN_PRICE,
        FishingContract.FishingEntry.COLUMN_IMAGE,
        FishingContract.FishingEntry.COLUMN_WEATHER_IMAGE
    };

    public static final int COL_ID = 0;
    public static final int COL_PLACE = 1;
    public static final int COL_DATE = 2;
    public static final int COL_WEATHER = 3;
    public static final int COL_DESCRIPTION = 4;
    public static final int COL_PRICE = 5;
    public static final int COL_IMAGE = 6;
    public static final int COL_WEATHER_ICON = 7;

    long id = -1;
    String place;
    long date; //Date
    //int weather
    String weather;
    String description;
//    String catches;
    String price;
//    byte[] cameraPhoto;
    Bitmap photoBitmap;
    int weatherIcon;

    public FishingItem() {}
    // constructor bdObject
    public FishingItem(int id, String place, long date, String description, Bitmap photoBitmap) {
        this.id = id;
        this.place = place;
        this.date = date;
        this.description = description;
        this.photoBitmap = photoBitmap;
    }

    // constructor
    public FishingItem(Intent intent) {
        place = intent.getStringExtra(FishingItem.PLACE);
        date = intent.getLongExtra(FishingItem.DATE, 0);
        id = intent.getLongExtra(FishingItem.ID, -1L);
        description = intent.getStringExtra(FishingItem.DESCRIPTION);
        photoBitmap = intent.getParcelableExtra(FishingItem.PHOTO);
    }

    public static void packageIntent(Intent data, String place, long date,
                                     long id, String description, Bitmap myPhoto) {
        data.putExtra(FishingItem.PLACE, place);
        data.putExtra(FishingItem.DATE, date);
        data.putExtra(FishingItem.ID, id);
        data.putExtra(FishingItem.DESCRIPTION, description);
        data.putExtra(FishingItem.PHOTO, myPhoto);
    }

    public static void packageIntentFromItem(Intent data, FishingItem item) {
        data.putExtra(FishingItem.PLACE, item.getPlace());
        data.putExtra(FishingItem.DATE, item.getDate());
        data.putExtra(FishingItem.ID, item.getId());
        data.putExtra(FishingItem.DESCRIPTION, item.getDescription());
//        data.putExtra(FishingItem.PHOTO, item.getPhoto());

    }

    // data which are set in the formation of a list item
    public long getId() {
        return id;
    }
    public long getDate() {
        return date;
    }
    public String getPlace() {
        return place;
    }
    public String getWeather() {
        return weather;
    }
    public String getDescription() {
        return description;
    }
    public String getPrice() {
        return price;
    }
    public Bitmap getBitmap(){
        return photoBitmap;
    }
    public int getWeatherIcon(){
        Log.d(LOG_TAG,"getWeatherIcon = " + weatherIcon);
        return weatherIcon;}
    public void setPlace(String place) {
        this.place = place;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(String price) {
        this.price = price;
    }
//    public void setPhoto(byte[] photo) {
//        cameraPhoto = photo;
//    }
    public void setBitmap (Bitmap photo){
        photoBitmap = photo;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setWeatherIcon (int icon) {weatherIcon = icon;
    Log.d(LOG_TAG,"setWeatherIcon = "+weatherIcon);}

    @Override
    public String toString() {
        return "Item[id:" + id + "]";
    }

    public static FishingItem createFishingItemFromCursor(Context context, Cursor data) {
        FishingItem item = new FishingItem();
        item.id = data.getLong(COL_ID);
        item.place = data.getString(COL_PLACE);
        item.date = data.getLong(COL_DATE);
        Log.d("ITEMS", "ITEMS=" + item.place + "=" + item.date);
        item.weather = data.getString(COL_WEATHER);
        item.description = data.getString(COL_DESCRIPTION);
        item.price = data.getString(COL_PRICE);
        item.photoBitmap = convertBlobToPhotoBitmap(context, data.getBlob(COL_IMAGE));
        item.weatherIcon = data.getInt(COL_WEATHER_ICON);
        return item;
    }

    private static Bitmap convertBlobToPhotoBitmap(Context context, byte[] byteArray) {
        return (byteArray == null || byteArray.length == 0) ?
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_no_photo)
                : BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

}

package net.validcat.fishing.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import net.validcat.fishing.camera.CameraManager;
import net.validcat.fishing.models.FishingItem;

/**
 * Data access manager for storing and retrieving data from storage
 */

public class DataObjectManager implements IDataObjectManager {

    public void storeNewFishing(Context context, CameraManager cm, FishingItem item,
                                String thumbPath, String photoPath, boolean isThingsListExists,
                                String thingsListReference, boolean isUpdate) {
        ContentValues cv = new ContentValues();
        if (item.getId() != -1)
            cv.put(FishingContract.FishingEntry._ID, item.getId());

        cv.put(FishingContract.FishingEntry.COLUMN_PLACE, item.getPlace());
        cv.put(FishingContract.FishingEntry.COLUMN_DATE, item.getDate());
        cv.put(FishingContract.FishingEntry.COLUMN_WEATHER, item.getWeather());
        cv.put(FishingContract.FishingEntry.COLUMN_DESCRIPTION, item.getDescription());
        cv.put(FishingContract.FishingEntry.COLUMN_PRICE, item.getPrice());

        if (!TextUtils.isEmpty(thumbPath) && !TextUtils.isEmpty(photoPath)) {
            cv.put(FishingContract.FishingEntry.COLUMN_IMAGE, photoPath);
            cv.put(FishingContract.FishingEntry.COLUMN_THUMB, thumbPath);
        }
        cv.put(FishingContract.FishingEntry.COLUMN_WEATHER_ICON, item.getWeatherIcon());
//            cv.put(FishingEntry.COLUMN_TACKLE, tvTackle.getText().toString());
        cv.put(FishingContract.FishingEntry.COLUMN_BAIT, item.getBait());
        cv.put(FishingContract.FishingEntry.COLUMN_FISH_FEED, item.getFishFeed());
        cv.put(FishingContract.FishingEntry.COLUMN_LATITUDE, item.getLatitude());
        cv.put(FishingContract.FishingEntry.COLUMN_LONGITUDE, item.getLongitude());
//            cv.put(FishingEntry.COLUMN_CATCH, etCatch.getText().toString());
        if (isThingsListExists) {
            cv.put(FishingContract.FishingEntry.COLUMN_THINGS_KEY, thingsListReference);
        }
        if (isUpdate) {
            context.getContentResolver().update(FishingContract.FishingEntry.CONTENT_URI, cv, null, null);
        } else {
            context.getContentResolver().insert(FishingContract.FishingEntry.CONTENT_URI, cv);
        }

    }

    public FishingItem retrieveFishingItem(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver()
                .query(uri, FishingContract.FishingEntry.PROJECTION, null, null, null);
        if (cursor == null)
            return null;

        FishingItem item = new FishingItem();

        if (cursor.moveToFirst()) {
            item.setPlace(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PLACE)));
            item.setPrice(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PRICE)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DESCRIPTION)));
            item.setBait(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_BAIT)));
            item.setFishFeed(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_FISH_FEED)));
            item.setPhotoPath(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_IMAGE)));
            item.setDate(cursor.getLong(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DATE)));
            item.setWeatherIcon(cursor.getInt(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_WEATHER_ICON)));
            item.setWeatherIcon(cursor.getInt(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_WEATHER)));
            item.setLatitude(cursor.getDouble(FishingContract.FishingEntry.INDEX_LATITUDE));
            item.setLongitude(cursor.getDouble(FishingContract.FishingEntry.INDEX_LONGITUDE));
        }

        cursor.close();

        return item;
    }
}

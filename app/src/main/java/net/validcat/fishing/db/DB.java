package net.validcat.fishing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.data.FishingContract;
import net.validcat.fishing.data.FishingDbHelper;

import java.util.List;

@Deprecated
public class DB {
    public static final String LOG_TAG = DB.class.getSimpleName();

    private final Context context;
    private FishingDbHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        context = ctx;
    }

    // open connection
    public SQLiteDatabase open() {
        mDBHelper = new FishingDbHelper(context);
        mDB = mDBHelper.getWritableDatabase();

        return mDB;
    }

    // close connection
    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    // get all data from the table DB_TABLE
    public Cursor getAllData() {
        return mDB.query(FishingContract.FishingEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    // removes the entry from the table DB_TABLE
    public void delRec(long id) {
        mDB.delete(FishingContract.FishingEntry.TABLE_NAME, FishingContract.FishingEntry._ID + " = " + id, null);
    }

//	public long getId() {
//		return ID;
//	}

    public List<FishingItem> getData(List<FishingItem> dbInnerList,
                                     Cursor cursor) {
        int id = cursor.getColumnIndex(FishingContract.FishingEntry._ID);
        int dbPlaceKey = cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PLACE);
        int dbDateKey = cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DATE);
        int dbDescriptionKey = cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DESCRIPTION);
        int dbPhotoKey = cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_IMAGE);

        do {
            byte[] photo = cursor.getBlob(dbPhotoKey);
            Bitmap dbPhoto = (photo != null) ? BitmapFactory.decodeByteArray(photo, 0, photo.length)
            : BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_no_photo);
            FishingItem dbItem = new FishingItem(cursor.getInt(id), cursor.getString(dbPlaceKey),
                    cursor.getString(dbDateKey), cursor.getString(dbDescriptionKey), dbPhoto);
            dbInnerList.add(dbItem);
            Log.d(LOG_TAG, " --- dbList --- " + dbInnerList);
        } while (cursor.moveToNext());

        return dbInnerList;
    }

    public FishingItem getFishingItemById(long id) {
        FishingItem item = new FishingItem();
        Cursor cursor;
//		if (ID > 0) {
//			cursor = mDB.query(DB.DB_TABLE, allColumns, DB.COLUMN_ID + " = "
//					+ ID, null, null, null, null);
//		} else
        String[] projection = {FishingContract.FishingEntry._ID, FishingContract.FishingEntry.COLUMN_PLACE,
                FishingContract.FishingEntry.COLUMN_DATE,
                FishingContract.FishingEntry.COLUMN_WEATHER,
                FishingContract.FishingEntry.COLUMN_DESCRIPTION,
                FishingContract.FishingEntry.COLUMN_PRICE,
                FishingContract.FishingEntry.COLUMN_IMAGE};

        cursor = mDB.query(FishingContract.FishingEntry.TABLE_NAME,
                projection,
                FishingContract.FishingEntry._ID + " = "
                + id, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                item.setId(cursor.getLong(cursor.getColumnIndex(FishingContract.FishingEntry._ID)));
                item.setPlace(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PLACE)));
                item.setDate(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DATE)));
                item.setWeather(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_WEATHER)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_DESCRIPTION)));
                item.setPrice(cursor.getString(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_PRICE)));
//TODO                item.setPhoto(cursor.getBlob(cursor.getColumnIndex(FishingContract.FishingEntry.COLUMN_IMAGE)));
            } else
                Log.d(LOG_TAG, " --- row 0 --- ");
        } else
            Log.d(LOG_TAG, " --- cursor = null --- ");
        cursor.close();

        return item;
    }

    public long saveFishingItem(FishingItem item) {
        ContentValues cv = new ContentValues();
        if (item.getId() != -1)
            cv.put(FishingContract.FishingEntry._ID, item.getId());
        cv.put(FishingContract.FishingEntry.COLUMN_PLACE, item.getPlace());
        cv.put(FishingContract.FishingEntry.COLUMN_DATE, item.getDate());
        cv.put(FishingContract.FishingEntry.COLUMN_WEATHER, item.getWeather());
        cv.put(FishingContract.FishingEntry.COLUMN_DESCRIPTION, item.getDescription());
        cv.put(FishingContract.FishingEntry.COLUMN_PRICE, item.getPrice());
//TODO        cv.put(DB.COLUMN_IMAGE, item.getPhoto());

        long id;
        if (item.getId() == -1) {
            id = mDB.insert(FishingContract.FishingEntry.TABLE_NAME, null, cv);
        } else {
            id = mDB.update(FishingContract.FishingEntry.TABLE_NAME, cv,
                    FishingContract.FishingEntry._ID + "=?", new String[]{Long.toString(item.getId())});
            Log.d(LOG_TAG, "long id = " + id);
        }
        return id;
    }

}
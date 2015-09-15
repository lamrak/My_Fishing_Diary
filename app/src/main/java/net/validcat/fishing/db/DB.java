package net.validcat.fishing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.validcat.fishing.FishingItem;

import java.util.List;

public class DB {
	public static final String LOG_TAG = DB.class.getSimpleName();
	private static final String DATABASE_NAME = "den_database1.db";
	public static final String DB_TABLE = "mytab";
	private static final int DB_VERSION = 1;

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLACE = "place";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_WEATHER = "weather";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PRICE = "price";

	private static final String DB_CREATE = "create table " + DB_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_PLACE
			+ " text, " + COLUMN_DATE + " text, " + COLUMN_WEATHER + " text, "
			+ COLUMN_DESCRIPTION + " text, " + COLUMN_PRICE + " text" + ");";

	private final Context mCtx;
	private DBHelper mDBHelper;
	public SQLiteDatabase mDB;
	long ID;

	private String[] allColumns = { DB.COLUMN_ID, DB.COLUMN_PLACE,
			DB.COLUMN_DATE, DB.COLUMN_WEATHER, DB.COLUMN_DESCRIPTION,
			DB.COLUMN_PRICE};

	public DB(Context ctx) {
		mCtx = ctx;
	}

	// open connection
	public SQLiteDatabase open() {
		mDBHelper = new DBHelper(mCtx, DATABASE_NAME, null, DB_VERSION);
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
		return mDB.query(DB_TABLE, null, null, null, null, null, null);
	}

	// removes the entry from the table DB_TABLE
	public void delRec(long id) {
		mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
	}

	public long getId() {
		return ID;
	}

	public List<FishingItem> getData(List<FishingItem> dbInnerList,
			Cursor cursor) {

		int idBdKey = cursor.getColumnIndex(DB.COLUMN_ID);
		int dbPlaceKey = cursor.getColumnIndex(DB.COLUMN_PLACE);
		int dbDateKey = cursor.getColumnIndex(DB.COLUMN_DATE);
		int dbDiscriptionKey = cursor.getColumnIndex(DB.COLUMN_DESCRIPTION);

		do {
			int idBd = cursor.getInt(idBdKey);
			String dbPlace = cursor.getString(dbPlaceKey);
			String dbDate = cursor.getString(dbDateKey);
			String dbDiscription = cursor.getString(dbDiscriptionKey);

			FishingItem dbItem = new FishingItem(idBd, dbPlace, dbDate, dbDiscription);
			dbInnerList.add(dbItem);
			Log.d(LOG_TAG, " --- dbList --- " + dbInnerList);
		} while (cursor.moveToNext());

		return dbInnerList;
	}

	private class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
			Log.d(LOG_TAG, " --- mytable --- " + DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

	public FishingItem getFishingItemById(long id) {
		FishingItem item = new FishingItem();
		Cursor cursor;
		if (ID > 0) {
			cursor = mDB.query(DB.DB_TABLE, allColumns, DB.COLUMN_ID + " = "
					+ ID, null, null, null, null);
		} else
			cursor = mDB.query(DB.DB_TABLE, allColumns, DB.COLUMN_ID + " = "
					+ id, null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				// if (cursor.moveToPosition(ID-1)) {
				// TODO
				item.setPlace(cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_PLACE)));
				item.setDate(cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DATE)));
				item.setWeather(cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_WEATHER)));
				item.setDescription(cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DESCRIPTION)));
				item.setPrice(cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_PRICE)));
				// TODO
			} else
				Log.d(LOG_TAG, " --- row 0 --- ");
		} else
			Log.d(LOG_TAG, " --- cursor = null --- ");
		cursor.close();
		return item; // item
	}

	public long saveFishingItem(FishingItem item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.COLUMN_PLACE, item.getPlace());
		cv.put(DB.COLUMN_DATE, item.getDate());
		cv.put(DB.COLUMN_WEATHER, item.getWeather());
		cv.put(DB.COLUMN_DESCRIPTION, item.getDescription());
		cv.put(DB.COLUMN_PRICE, item.getPrice());
		// TODO
		return mDB.insert(DB.DB_TABLE, null, cv);
	}

}

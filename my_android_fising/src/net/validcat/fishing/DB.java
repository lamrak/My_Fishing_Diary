package net.validcat.fishing;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {
	public static final String LOG_TAG = "myLogs";
	private static final String DATABASE_NAME = "den_database1.db";
	public static final String DB_TABLE = "mytab";
	private static final int DB_VERSION = 1;

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLACE = "place";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_WEATHER = "weather";
	public static final String COLUMN_PROCESS = "process";
	public static final String COLUMN_CATCH = "catch";

	private static final String DB_CREATE = "create table " + DB_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, " + COLUMN_PLACE
			+ " text, " + COLUMN_DATE + " text, " + COLUMN_WEATHER + " text, "
			+ COLUMN_PROCESS + " text, " + COLUMN_CATCH + " text" + ");";

	private final Context mCtx;
	private DBHelper mDBHelper;
	public SQLiteDatabase mDB;
	long ID;
	

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

	// add data in DB_TABLE
//	public void addRec(int i, String txt) {
//		 cv = new ContentValues();
//		
//		if (i==1)
//			cv.put(COLUMN_PLACE, txt);
//		if (i==2)
//			cv.put(COLUMN_DATE, txt);
//		if (i==3)
//			cv.put(COLUMN_WEATHER, txt);
//		if (i==4)
//			cv.put(COLUMN_PROCESS, txt);
//		if (i==5)
//			cv.put(COLUMN_CATCH, txt);		 			
//	}
//	public void insertRec(){
//		ID = mDB.insert(DB_TABLE, null, cv);
//		Log.d(LOG_TAG, " --- DB ID --- " + ID);
//	}

	// removes the entry from the table DB_TABLE
	public void delRec(long id) {
		mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
	}

	public long getId() {
		return ID;
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
}

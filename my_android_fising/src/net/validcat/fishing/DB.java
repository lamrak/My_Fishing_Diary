package net.validcat.fishing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {
	public static final String LOG_TAG = "myLogs";
	private static final String DATABASE_NAME = "den_database1.db";
	private static final String DB_TABLE = "mytab";
	private static final int DB_VERSION = 1;

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WEATHER = "weather";

	private static final String DB_CREATE = "create table " + DB_TABLE + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_WEATHER + " text" + ");";

	private final Context mCtx;
	private DBHelper mDBHelper;
	public SQLiteDatabase mDB;

	public DB(Context ctx) {
		mCtx = ctx;
	}

	// open connection
	public void open() {
		mDBHelper = new DBHelper(mCtx, DATABASE_NAME, null, DB_VERSION);
		mDB = mDBHelper.getWritableDatabase();
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
	public void addRec(int i, String txt) {
		ContentValues cv = new ContentValues();
		if (i == 3) {
			cv.put(COLUMN_WEATHER, txt);
		}
		long ID = mDB.insert(DB_TABLE, null, cv);
		Log.d(LOG_TAG, " --- Rows in my table --- " +ID );
	}

	// removes the entry from the table DB_TABLE
	public void delRec(long id) {
		mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}
}

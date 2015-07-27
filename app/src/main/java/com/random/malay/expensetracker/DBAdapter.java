package com.random.malay.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter"; //used for logging database version changes
			

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_AMOUNT = "amount";
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_DATE, KEY_DESCRIPTION, KEY_CATEGORY, KEY_AMOUNT};

	public static final int COL_ROWID = 0;
	public static final int COL_DATE = 1;
	public static final int COL_DESCRIPTION = 2;
	public static final int COL_CATEGORY = 3;
	public static final int COL_Amount = 4;


	public static final String DATABASE_NAME = "dbExpense";
	public static final String DATABASE_TABLE = "tableDbExpense";
	public static final int DATABASE_VERSION = 1;
		

	private static final String DATABASE_CREATE_SQL = 
			"CREATE TABLE " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_DATE + " TEXT, "
			+ KEY_DESCRIPTION + " TEXT"
			+ KEY_CATEGORY + " TEXT"
			+ KEY_AMOUNT+ " REAL"
			+ ");";
	
	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDBHelper.close();
	}

	public long insertRow(String date, String description, String category, Integer amount) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_DESCRIPTION, description);
		initialValues.put(KEY_CATEGORY, category);
		initialValues.put(KEY_AMOUNT, amount);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}

	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public boolean updateRow(long rowId, String date, String description, String category, Integer amount) {
		String where = KEY_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_DATE, date);
		newValues.put(KEY_DESCRIPTION, description);
		newValues.put(KEY_CATEGORY, category);
		newValues.put(KEY_AMOUNT, amount);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}

	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}


}


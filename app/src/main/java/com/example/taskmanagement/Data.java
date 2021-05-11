package com.example.taskmanagement;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class Data extends SQLiteOpenHelper { 
	private static final String LOGCAT = null; 
	public Data(Context applicationcontext) {
		super(applicationcontext, "Task.db", null, 1);
		Log.d(LOGCAT,"Created");
		}
	@Override public void onCreate(SQLiteDatabase database) { 
		String query; 
		query = "create table IF NOT EXISTS Task(" +
				" _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ "tname text, timportance text,tcomplete text" +
				",tcreated text," +
				"tduedate text)";
		database.execSQL(query); 
		Log.d(LOGCAT,"Tables Created"); 
		} 
	@Override public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query; query = "DROP TABLE IF EXISTS Task"; 
		database.execSQL(query); onCreate(database);
		}
	
	public void AddRecord(String taskName,String taskImportance,
			String taskComplete, String taskCreatedDate, String taskDueDate) { 
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("tcreated", taskCreatedDate);
		values.put("timportance", taskImportance);
		values.put("tname", taskName);
		values.put("tcomplete", taskComplete);
		values.put("tduedate", taskDueDate);
		database.insert("Task", null, values); 
		database.close(); 
		}      
}


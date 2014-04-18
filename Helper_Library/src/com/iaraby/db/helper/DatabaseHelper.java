/*
The MIT License (MIT)

Copyright (c) 2014 Ahmad Barqawi (github.com/Barqawiz)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.iaraby.db.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Database helper to manage database assets file version and creation
 * 
 * Helper make it simple to move the database row file from assets to device
 * just call createDatabase() then openWritableDatabase() or
 * openReadableDatabase()
 * 
 * Note: you can't do operations on database file in the assets folder so you
 * have to move it to private folder in the device, this claass will manage this
 * for you
 * 
 * @authors Ahmad Barqawi
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	
	private SQLiteDatabase dbManager;
	private Config config;

	public DatabaseHelper(Config config) {
		super(config.getContext(), config.getName(), null, config.getVersion());
		this.config = config;
	} // method: constructor

	/**
	 * Open the database with write permission after moving it to the device
	 * 
	 * Important: Make sure you always call createDatabase before it to double
	 * check the database is moved
	 * 
	 * @return SQLiteDatabase to manage database commands
	 */
	public SQLiteDatabase openWritableDatabase() {

		if (dbManager != null && dbManager.isOpen() && !dbManager.isReadOnly())
			return dbManager;

		dbManager = SQLiteDatabase.openDatabase(config.getFullDatabasePath(),
				null, SQLiteDatabase.OPEN_READWRITE);

		return dbManager;
	}

	/**
	 * Open read only database after moving it to the device
	 * 
	 * Important: Make sure you always call createDatabase before it to double
	 * check the database is moved
	 * 
	 * @return SQLiteDatabase to manage database commands
	 */
	public SQLiteDatabase openReadableDatabase() {

		if (dbManager != null && dbManager.isOpen() && dbManager.isReadOnly())
			return dbManager;

		dbManager = SQLiteDatabase.openDatabase(config.getFullDatabasePath(),
				null, SQLiteDatabase.OPEN_READWRITE);

		return dbManager;
	}

	/**
	 * Close the opened database instance
	 */
	public void closeDatabase() {
		if (dbManager != null) {
			dbManager.close();
			dbManager = null;
		} // make sure the database object not null
	}

	/**
	 * Create the database by moving it from assets to destination folder
	 * 
	 * Don't worry: if the database with same version is already moved will not
	 * be moved again so make sure you always call it for double check.
	 * 
	 * Note that: for current version if the version changed will move the new
	 * database and old database will be deleted
	 * 
	 */
	public synchronized void createDatabase() throws IOException{
		
		if (isDatabaseExist()) {
			
		} else {
			//create empty database to create the path 
			this.getReadableDatabase().close();
			
			try {
				copyDatabase();
				Util.saveValue(config.getVersionTag(), config.getVersion(), config.getContext());
				this.close();
				
			} catch(IOException ex) {
				throw new Error("Error while coping the database:", ex);
			} //try to copy the database
		} //check if database with current version already exists or not
	} // method: move the database to device

	
	/**
	 * this method check if the database with the current version already exisit
	 * in the device
	 * 
	 * @return true if already moved, false otherwise
	 */
	public boolean isDatabaseExist() {
		boolean res = false;
		SQLiteDatabase tempDB = null;
		try {
			 tempDB = SQLiteDatabase.openDatabase(
					config.getFullDatabasePath(), null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			//not error just to check if database exists or not
			Log.i("Database Helper", "Database does not exits");
		} //try-catch
		
		if (tempDB != null) {
			res = true;
			tempDB.close();
		} //make sure the database not null
		
		if (res) {
			//check the version of the database
			SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(config.getContext());
			int currentVersion = prefManager.getInt(config.getVersionTag(), Config.DEFAULT_VERSION);
			res = (currentVersion == config.getVersion());
			
		}
		
		return res;
	} // method: check if the database with x version already exits

	private void copyDatabase() throws IOException {
		InputStream input = config.getContext().getAssets().open(config.getName());
		OutputStream output = new FileOutputStream(config.getFullDatabasePath());
		Util.copyFile(input, output);
		//close input-output
		output.flush();
		output.close();
		input.close();
	}
	
	
	/*---------------------*/

	/** called when create data base */
	public void onCreate(SQLiteDatabase db) {

	} // method: on create

	/**
	 * called when change the version of database in config during update
	 * process
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("Database Helper",
				"if the database version cahnge in config file this will cause to delete the old database");
		Log.w("Database Helper",
				"current version does not support keep old data when move new database yet !");
	} // method: on upgrade

}

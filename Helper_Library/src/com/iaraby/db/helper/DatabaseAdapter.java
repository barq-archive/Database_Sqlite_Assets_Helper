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

import java.io.IOException;

import com.iaraby.db.helper.DatabaseHelper.DBListener;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Database adapter that use the DatabaseHelper with extra query and insert
 * methods
 * 
 * HowTo: Call getInstance() then open to use this adapter
 * 
 * Custom:to custom the adapter and write your layer of methods extend this
 * adapter in your project
 * 
 * Important: make sure to call open() method at least once in your app
 * 
 * @authors Ahmad Barqawi
 * 
 */
public class DatabaseAdapter {

	private static DatabaseAdapter instance;

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	/**
	 * Get instance from database adapter
	 * 
	 * @return DatabaseAdapter instance
	 */
	public static DatabaseAdapter getInstance() {
		if (instance == null)
			instance = new DatabaseAdapter();
		return instance;
	}

	/**
	 * Check if the database is already opened
	 * @return true if open
	 */
	public boolean isOpen() {
		if (dbHelper != null && db != null && db.isOpen())
			return true;
		else
			return false;
	} //method: check if the dataasse is opened

	/**
	 * Open and create the database. Make sure to call it at least once with
	 * each adapter instance
	 * 
	 * @throws IOException
	 */
	public void open(Config config) throws IOException {
		open(config, null);
	} // method: open the database to be ready for operations

	public void open(Config config, DBListener listener) throws IOException {
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(config);
			if (listener != null)
				dbHelper.setListener(listener);
			dbHelper.createDatabase();
		}

		if (db == null || !db.isOpen()) {
			db = dbHelper.getWritableDatabase();
			dbHelper.notifyDatabaseOepend();
		}
	} // method: open the database to be ready for operations
	
	public void close() {
		if (db != null) {
			db.close();
		} // close the database
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		} // close database helper
	} // method: close the database

	/* Database select, insert and update operations */

	/* Query */
	/**
	 * Fetch all data from received table
	 * 
	 * @param String
	 *            table name
	 * @param String
	 *            name of columns to return
	 * @return Cursor object positioned before the first object, not that
	 *         cursors are not synchronized
	 */
	public Cursor fetchData(String tableName, String... cols) {
		return fetchDataWhere(tableName, null, null, null, cols);
	}

	/**
	 * Fetch specific rows from received table
	 * 
	 * @param String
	 *            table name
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @param String
	 *            order by column
	 * @param String
	 *            name of columns to return
	 * @return Cursor object positioned before the first object, not that
	 *         cursors are not synchronized
	 */
	public Cursor fetchDataWhere(String tableName, String selection,
			String[] where, String orderby, String... cols) {
		return db.query(tableName, cols, selection, where, null, null, orderby);
	}

	/* Insert */
	/**
	 * Insert data into received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 * @return number of effected fields
	 */
	public long insert(String tableName, ContentValues rowValues) {
		return insert(tableName, rowValues, null);
	}

	/**
	 * Insert data into received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 * @param String
	 *            replace any null field with received value
	 * @return number of effected fields
	 */
	public long insert(String tableName, ContentValues rowValues,
			String replaceNullWith) {
		if (db == null) {
			Log.e("Database Helper",
					"Database is close, please call open method");
			return 0;
		}
		return db.insert(tableName, replaceNullWith, rowValues);
	}

	/* Update */
	/**
	 * Update fields for received table
	 * 
	 * @param String
	 *            table name
	 * @param ContentValues
	 *            (column names to return)
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @return number of effected fields
	 */
	public int update(String tableName, ContentValues rowValues,
			String selection, String[] where) {
		return db.update(tableName, rowValues, selection, where);
	}

	/**
	 * Delete fields for received table
	 * 
	 * @param String
	 *            table name
	 * @param String
	 *            field declare which rows to return
	 * @param String
	 *            [] you may include ? in selection which will be replaced with
	 *            values in this array
	 * @return number of effected fields
	 */
	public int delete(String tableName, String selection, String[] where) {
		return db.delete(tableName, selection, where);
	}

	public void setListener(DBListener listener) {
		if (dbHelper != null)
			dbHelper.setListener(listener);
	}
} // class: database adapter

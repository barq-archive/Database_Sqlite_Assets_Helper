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
package com.iaraby.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.iaraby.db.helper.DatabaseAdapter;

/**
 * Example on best practice how to use DatabaseAdapter in the library
 * 
 * @author Ahmad Barqawi
 * 
 */
public class DBAdapter extends DatabaseAdapter {

	private static DBAdapter instance;

	public static DBAdapter getInstance() {
		if (instance == null)
			instance = new DBAdapter();
		return instance;
	}

	public Cursor getKeep() {
		return fetchData(KEEP_TABLE.TABLE_NAME, KEEP_TABLE.COL_ID,
				KEEP_TABLE.COL_DATA, KEEP_TABLE.COL_COLOR);
	}

	public void addKeep(String data, String color) {
		ContentValues rowValues = KEEP_TABLE.createContentValues(data,color,
				KEEP_TABLE.ACTIVE);
		insert(KEEP_TABLE.TABLE_NAME, rowValues);
	}

	public void removeKeep(long id) {
		String slection = KEEP_TABLE.COL_ID + "= ?";
		String where[] = new String[] { String.valueOf(id) };
		delete(KEEP_TABLE.TABLE_NAME, slection, where);
	}

	/* Table Information to unify using the fields from once place 
	 * You can use the library without implementing this but make sure you always send the right 
	 * table name and columns names*/
	public static class KEEP_TABLE {
		public final static String TABLE_NAME = "keep";
		public final static String COL_ID = "_id";
		public final static String COL_DATA = "data";
		public final static String COL_COLOR = "color";
		public final static String COL_ACTIVE = "active";
		

		public final static int ACTIVE = 1;
		public final static int NOT_ACTIVE = 0;

		public static ContentValues createContentValues(String data, String color, int active) {
			ContentValues values = new ContentValues();
			values.put(COL_DATA, data);
			values.put(COL_COLOR, color);
			values.put(COL_ACTIVE, active);
			return values;
		} // method: create content values
	} // class: keep table info

} // class: Database adapter class

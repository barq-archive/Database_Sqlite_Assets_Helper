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

import android.content.Context;

/**
 * Class to intial the configuration for the database helper
 * 
 * @authors Ahmad BArqawi
 * 
 */
public class Config {

	private final static String DB_FOLDER = "/databases/";
	private String databasePath;
	private Context context;
	/** name of the database in the assets folder */
	private String name;
	/**
	 * Database version, changing it will move new database from assets folder
	 * and this will delete user saved data in the old database
	 */
	private int version;
	/** default database version */
	public static int DEFAULT_VERSION = 0;
	private String versionTag;

	/**
	 * Initial database configurations to move file to the default application
	 * private folder
	 * 
	 * IMPORTANT: change the version will override user database with new one
	 * from assets and this will clear the data for the user !
	 * 
	 * @param Context
	 * @param String
	 *            name of database file in the assets
	 * @param int version of database, for every new version will move the
	 *        database from assets folder and this will cause to clear the old
	 *        database data !
	 * 
	 */
	public Config(String dbNamer, int dbVersion, Context context) {
		this.context = context;
		this.name = dbNamer;
		this.version = dbVersion;
		this.databasePath = context.getApplicationInfo().dataDir;
		this.versionTag = dbNamer + ":" + version;
		if (!databasePath.endsWith(DB_FOLDER))
			this.databasePath += DB_FOLDER;
	} // constructor

	public static String getDbFolder() {
		return DB_FOLDER;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public Context getContext() {
		return context;
	}

	public String getName() {
		return name;
	}

	public int getVersion() {
		return version;
	}

	public String getFullDatabasePath() {
		return databasePath + name;
	}

	public String getVersionTag() {
		return versionTag;
	}

} // class

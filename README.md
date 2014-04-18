Android Database Assets Helper
======
Android library to manage sqlite database version and creation from the assets folder

With this library you can load your app with sqlite database content and manage it with simple methods



Methods
-----
* `Config` simple class to setup the configuration for the library
* `DatabaseHelper` class extends the SQLiteOpenHelper to manage the database version and creation
* `DatabaseAdapter` (optional) adapter class to use in your code so it will save time on you
* `SimpleCursorLoader` (optional) utility to manage the cursor life cycle inside a listview in case you don’t want implement content provider


Setup
-----
Add the [jar][0] file in your project lib folder 
OR Import the project “Helper_Library” and add it in your project build path as library (in Eclipse: right click on your project > Properties > Android > Add..) <br/>

HowTo
-----
1. Add sqlite database in the assets folder

2. Initial the `Config` (make sure the name of database match the file in assets)<br/>
   ``` Config config = new Config(“database_name”, 1/*version*/, this);  ```

3. Open the database, make sure it is called at least once in the application, don’t worry the database will moved only once for every version number even if you called this method many times:
   ``` DBAdapter.getInstance().open(config); ```


Sample
------------
Check **Keep_Sample_Demo** to see how to use the library, following code it from the sample adapter, it is extend the library adapter and write a layer of custom database operations code:
```
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

	/* Table Information to unify using the fields from once place */
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
```

This is screenshot from sample application that use **Helper_Library** <br/><br/>
 ![alt tag](https://raw.github.com/Barqawiz/Database_Sqlite_Assets_Helper/master/Demo/screenshot.png) 


Credit
------------
###Author
* **Ahmad Barqawi** (@Barqawi88)


###Contributors
* **Cristian** *SimpleCursorLoader*

*Contributors need to maintain version management and keep user data when copy new version database*

License
-------
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

[0]: https://raw.github.com/Barqawiz/Database_Sqlite_Assets_Helper/master/DBHelper.jar
 

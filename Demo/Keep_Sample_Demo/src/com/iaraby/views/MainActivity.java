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
package com.iaraby.views;

import java.io.IOException;

import com.iaraby.data.DBAdapter;
import com.iaraby.db.helper.Config;
import com.iaraby.keep.R;
import com.iaraby.keep.R.id;
import com.iaraby.keep.R.layout;
import com.iaraby.keep.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initial
        //make sure the database is opened
        if (!DBAdapter.getInstance().isOpen()) {
			try {
				Config config = new Config("items.sqlite", 1, this);
				DBAdapter.getInstance().open(config);
			} catch (IOException e) {
				Log.e(getString(R.string.app_name), "Error opening the database", e);
			} //try to open the database
		} //check if the data base is opened
        
        //add the fragment 
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new KeepFragment())
                    .commit();
        } 
       
    } //method: on create

    private void addItem() {
    	KeepFragment frag = (KeepFragment) getSupportFragmentManager().findFragmentById(R.id.container);
    	if (frag != null) {
    		AddDialog dialog = AddDialog.getInstatnce(frag);
    		dialog.show(getSupportFragmentManager(), "add");
    	}
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } //Method: create menu 

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
        	
        	addItem(); 
        	
            return true;
        } else if (id == R.id.action_info) {
        	Toast.makeText(MainActivity.this, getString(R.string.About), Toast.LENGTH_LONG).show();
        	return true;
        } //check selected men item
        return super.onOptionsItemSelected(item);
    } //Method: on menu item selected


} //class: Main

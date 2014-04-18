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


import com.iaraby.data.DBAdapter;
import com.iaraby.db.helper.lib.SimpleCursorLoader;
import com.iaraby.keep.R;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class KeepFragment extends ListFragment 
								implements LoaderCallbacks<Cursor>, OnItemLongClickListener{

	private SimpleCursorAdapter adapter;
	private SimpleCursorLoader loader;
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initial
		customUI();
		getListView().setOnItemLongClickListener(this);
		// Control
		populateList();
	} // method: on create

	public void populateList() {
		
		String[] from = new String[] { DBAdapter.KEEP_TABLE.COL_DATA, DBAdapter.KEEP_TABLE.COL_COLOR };
		int[] to = new int[] { R.id.textView1, R.id.item_color};
		
		getLoaderManager().initLoader(1, null, this);
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.keep_item, null, from, to, 
						CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		adapter.setViewBinder(new Binder());
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		
	}

	private void customUI() {
		getListView().setCacheColorHint(0);
		getListView().setDivider(null);
		getListView().setDividerHeight((int)getResources().getDimension(R.dimen.devider_padding));
		getListView().setVerticalScrollBarEnabled(false);
		getListView().setHorizontalScrollBarEnabled(false);
		getListView().setSelector(R.drawable.list_selector);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (DBAdapter.getInstance().isOpen() && id > 0) {
			DBAdapter.getInstance().removeKeep(id);
			loader.onContentChanged();
			Toast.makeText(getActivity(), getString(R.string.deleted), Toast.LENGTH_LONG).show();
		} 
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//when receive 10 and 10 from add dialog thats mean refresh the list
		if (resultCode == 10 && requestCode == 10) {
			
			if (loader != null)
				loader.onContentChanged();
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/*Cursor loader*/
	@Override
	public Loader<Cursor> onCreateLoader(int args0, Bundle bundel) {
		loader = new SimpleCursorLoader(getActivity()) {
			@Override
			public Cursor loadInBackground() {
				Cursor cursor = DBAdapter.getInstance().getKeep();
				return cursor;
			}
		};
		
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (adapter != null && cursor != null)
			adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (adapter != null)
			adapter.swapCursor(null);
	}
	
	/*Bind*/
	class Binder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int index) {
			if (view.getId() == R.id.item_color) {
				String color = cursor.getString(cursor.getColumnIndex(DBAdapter.KEEP_TABLE.COL_COLOR));
				if (color != null)
					((ImageView)view).setBackgroundColor(Color.parseColor(color));
			}
			return false;
		}
		
	} //class: handle list view bind

	
} // class: Fragment
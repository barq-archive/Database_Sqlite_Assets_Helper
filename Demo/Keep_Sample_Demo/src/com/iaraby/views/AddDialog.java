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
import com.iaraby.keep.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class AddDialog extends DialogFragment implements OnClickListener{
	
	private static AddDialog instance;
	private EditText edText;
	private int color = R.color.item_color1;
	
	public static AddDialog getInstatnce(Fragment fragment){
		if (instance == null || instance.getDialog() == null
				|| !instance.getDialog().isShowing()) {
			instance = new AddDialog();
			//this is to hack onActivityResult to send back the result from the dialog
			instance.setTargetFragment(fragment, 1);
		}
		return instance;
	}	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//**Initial
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(false);
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		//**UI
		View items = inflater.inflate(R.layout.add_item, container, false);	
		edText = (EditText) items.findViewById(R.id.text_to_add);
		
		// **click listener
		items.findViewById(R.id.button_add).setOnClickListener(this);
		items.findViewById(R.id.item_color1).setOnClickListener(this);
		items.findViewById(R.id.item_color2).setOnClickListener(this);
		items.findViewById(R.id.item_color3).setOnClickListener(this);
		
		
		return items;
	}

	private void addKeep(String text, int color) {
		if (DBAdapter.getInstance().isOpen())
			DBAdapter.getInstance().addKeep(text, getString(color));
	}
	
	public void onClick(View v) {
		
		if (v.getId() == R.id.button_add && edText != null) {
			if (edText.getText().toString().length() == 0) {
				Toast.makeText(getActivity(), "Plese enter thoughts into text !", Toast.LENGTH_LONG).show();
				return;
			}
			addKeep(edText.getText().toString(), color);
			//this is to hack onActivityResult to send back the result from the dialog
			getTargetFragment().onActivityResult(10, 10, null);
			dismiss();
		}  else {
			setColor(v);
		}
	} // method: handle click events


	public void setColor(View view) {
		switch (view.getId()) {
		case R.id.item_color2:
			color = R.color.item_color2;
			break;
		case R.id.item_color3:
			color = R.color.item_color3;
			break;
		default:
			color = R.color.item_color1;
			break;
		}
		if (edText != null)
			edText.setBackgroundColor(getResources().getColor(color));
	} //method: handle on set color request
	
} // class: login dialog


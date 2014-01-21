package edu.neu.madcourse.dushyantdeshmukh.dictionary;

import edu.neu.madcourse.dushyantdeshmukh.About;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.sudoku.Sudoku;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class Dictionary extends Activity implements OnClickListener {

	public Dictionary() {
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dictionary_main);

		// Set up click listeners for all the buttons
		EditText editText = (EditText) findViewById(R.id.dictionary_editText);
		 
		// add a keylistener to keep track user input
		editText.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return false;
			}
			
		});
		
		View clearButton = findViewById(R.id.dictionary_clear_button);
		clearButton.setOnClickListener(this);

		View returnButton = findViewById(R.id.dictionary_return_button);
		returnButton.setOnClickListener(this);

		View ackButton = findViewById(R.id.dictionary_ack_button);
		ackButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dictionary_clear_button:
			
			break;
		case R.id.dictionary_return_button:
			break;
		case R.id.dictionary_ack_button:
//			Intent i3 = new Intent(this, Dictionary.class);
//			startActivity(i3);
			break;
		}
	}

}

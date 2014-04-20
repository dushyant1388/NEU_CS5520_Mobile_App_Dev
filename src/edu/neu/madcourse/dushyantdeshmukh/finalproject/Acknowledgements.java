package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.dushyantdeshmukh.R;

public class Acknowledgements extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_proj_acknowledgements);

		// Set up click listeners for all the buttons
		View okButton = findViewById(R.id.ok_button);
		okButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_button:
			finish();
			break;
		}
	}
}

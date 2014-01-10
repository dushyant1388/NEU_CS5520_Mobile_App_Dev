package edu.neu.madcourse.dushyantdeshmukh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class About extends Activity implements OnClickListener {
	 @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.about);
	      
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

package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Home extends Activity implements OnClickListener{
	
	public Home(){
		
	}

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.final_proj_home);

	    // Set up click listeners for all the buttons
	    View dualPhoneModeButton = findViewById(R.id.final_proj_dual_phone_mode_button);
	    dualPhoneModeButton.setOnClickListener(this);

	    View singlePhoneModeButton = findViewById(R.id.final_proj_single_phone_mode_button);
	    singlePhoneModeButton.setOnClickListener(this);
	    
	    View exitGameButton = findViewById(R.id.final_proj_exit_game_button);
	    exitGameButton.setOnClickListener(this);
	    
	  }
	
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		    case R.id.final_proj_dual_phone_mode_button:
		      Intent twoPlayerConnectionIntent = new Intent(this,Connection.class);
		      startActivity(twoPlayerConnectionIntent);
		      break;
		    case R.id.final_proj_single_phone_mode_button:
		      break;
		    case R.id.final_proj_exit_game_button:
		      finish();
		      break;
		    }
	}
}

package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener{
	
	Context context;
	AlertDialog alertDialog;
	Button dualPhoneModeButton,singlePhoneModeButton,exitGameButton;
	boolean isDualPhoneModeSelected = false; 
	
	public Home(){		
	}

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.final_proj_home);
	    
	    context = this;
	    
	    // Set up click listeners for all the buttons
	    dualPhoneModeButton = (Button)findViewById(R.id.final_proj_dual_phone_mode_button);
	    dualPhoneModeButton.setOnClickListener(this);

	    singlePhoneModeButton = (Button) findViewById(R.id.final_proj_single_phone_mode_button);
	    singlePhoneModeButton.setOnClickListener(this);
	    
	    exitGameButton = (Button) findViewById(R.id.final_proj_exit_game_button);
	    exitGameButton.setOnClickListener(this);
	    
	  }
	
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
		    case R.id.final_proj_dual_phone_mode_button:
		    	Intent dualPhoneIntent = new Intent(this, Connection.class);
		    	startActivity(dualPhoneIntent);
		    	break;
		    case R.id.final_proj_single_phone_mode_button:
		    	break;
		    case R.id.final_proj_exit_game_button:
		    	finish();
		    	break;
		    }
	}
	
}

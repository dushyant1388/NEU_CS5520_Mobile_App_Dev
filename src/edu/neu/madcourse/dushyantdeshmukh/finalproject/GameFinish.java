package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.util.HashMap;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame.Constants;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameFinish extends Activity implements OnClickListener{

	protected static final String TAG = "GAME FINISH ACTIVITY";
	
	private TextView finalScoreText;
	private Button mainMenuButton;
	private SharedPreferences projPreferences;
	Context context;
	
	boolean isOpponentGameOver;
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.final_proj_game_finish);

	    context = this;

	    // Set up click listeners for all the buttons
	    mainMenuButton = (Button) findViewById(R.id.final_proj_main_menu_button);
	    mainMenuButton.setOnClickListener(this);
	    finalScoreText = (TextView) findViewById(R.id.final_proj_show_result);
	    
	    projPreferences = getSharedPreferences();
	  }
	  
	  @Override
	  protected void onResume() {
	    super.onResume();
	    showFinalResultToPlayer();
	  }

	  private void showFinalResultToPlayer() {
		String playerTime = projPreferences.getString(ProjectConstants.PLAYER_TIME, null);
		String oppTime = projPreferences.getString(ProjectConstants.OPPONENT_TIME, null);
		String playerImageCount = projPreferences.getString(ProjectConstants.PLAYER_IMAGE_COUNT, null);
		String oppImageCount = projPreferences.getString(ProjectConstants.PLAYER_IMAGE_COUNT, null);
		if(playerTime != null || oppTime != null || playerImageCount != null || oppImageCount != null){
			if(Integer.parseInt(playerTime) > Integer.parseInt(oppTime)){
				finalScoreText.setText("You lost!!! \n You captured " + playerImageCount + " out of " 
						+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + playerTime + "\n" +
						"Your opponent captured " + oppImageCount +  " out of " 
						+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + oppTime);
			}else if(Integer.parseInt(playerTime) < Integer.parseInt(oppTime)){
				finalScoreText.setText("You won!!! \n You captured " + playerImageCount + " out of " 
						+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + playerTime + "\n" +
						"Your opponent captured " + oppImageCount +  " out of " 
						+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + oppTime);
			}else{
				if(Integer.parseInt(playerImageCount) > Integer.parseInt(oppImageCount)){
					finalScoreText.setText("You lost!!! \n You captured " + playerImageCount + " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + playerTime + "\n" +
							"Your opponent captured " + oppImageCount +  " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + oppTime);
				}else if(Integer.parseInt(playerImageCount) < Integer.parseInt(oppImageCount)){
					finalScoreText.setText("You won!!! \n You captured " + playerImageCount + " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + playerTime + "\n" +
							"Your opponent captured " + oppImageCount +  " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + oppTime);
				}else{
					finalScoreText.setText("Scores Tied!!! You captured " + playerImageCount + " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + playerTime + "\n" +
							"Your opponent captured " + oppImageCount +  " out of " 
							+ ProjectConstants.TOTAL_NO_OF_IMAGES + " in " + oppTime);
				}
			}
		}else{
			finalScoreText.setText("Problem in calculating scores!");
		}
		Editor editor = projPreferences.edit();
		editor.putBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, false);
		editor.commit();
	  }

	@Override
	  protected void onPause() {
	    super.onPause();
	  }

	  @Override
	  public void onClick(View v) {
	    switch (v.getId()) {
	    case R.id.final_proj_main_menu_button:
	    	 Intent mainMenuIntent = new Intent(context,Home.class);
	    	 mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    	 startActivity(mainMenuIntent);
	    	 break;
	    }
	  }
	  
	 private SharedPreferences getSharedPreferences() {
	        return getSharedPreferences(ProjectConstants.FINAL_PROJECT,Context.MODE_PRIVATE);
	  }
}

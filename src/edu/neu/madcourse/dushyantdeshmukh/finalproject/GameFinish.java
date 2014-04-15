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
	private BroadcastReceiver receiver;
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

	    // This will handle the broadcast
	    receiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	        Log.d(TAG,"Inside onReceive of Broadcast receiver of ChooseOpponent.class");
	        String action = intent.getAction();
	        if (action.equals(ProjectConstants.INTENT_ACTION_CONNECTION)) {
	          String data = intent.getStringExtra("data");
	          Log.d(TAG, "data = " + data);
	          handleOpponentResponse(data);
	        }
	      }
	    };
	  }
	  
	  @Override
	  protected void onResume() {
	    super.onResume();
	    
	    // This needs to be in the activity that will end up receiving the broadcast
	    registerReceiver(receiver, new IntentFilter(ProjectConstants.INTENT_ACTION_CONNECTION));
	    handleNotification(projPreferences);
	    
	    isOpponentGameOver = projPreferences.getBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, false);
	    if(isOpponentGameOver){
	    	showFinalResultToPlayer();
	    }else{
	    	showPendingResultToPlayer();
	    }
	  }
	  	  
	  private void showPendingResultToPlayer() {
		  
		  finalScoreText.setText("Waiting for the opponent to finish game to show final result");
	  }

	  private void showFinalResultToPlayer() {
		
		finalScoreText.setText("Final Score is ready!!!");
	  }

	@Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(receiver);
	  }

	  @Override
	  public void onClick(View v) {
	    switch (v.getId()) {
	    case R.id.final_proj_main_menu_button:
	    	 Intent mainMenuIntent = new Intent(context,Home.class);
	    	 mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	    	 startActivity(mainMenuIntent);
	    	 break;
	    }
	  }
	  
	  
	  private void handleNotification(SharedPreferences sp) {
	    String data = sp.getString(ProjectConstants.KEY_NOTIFICATION_DATA, "");
	    if (!data.equals("")) {
	      handleOpponentResponse(data);
	      sp.edit().putString(ProjectConstants.KEY_NOTIFICATION_DATA, "").commit();
	    }
	  }
	  
	  protected void handleOpponentResponse(String data) {
		    Log.d(TAG, "Inside handleOpponentResponse()");
		    HashMap<String, String> dataMap = Util.getDataMap(data, TAG);
		    if (dataMap.containsKey(Constants.KEY_MSG_TYPE)) {
		      String msgType = dataMap.get(Constants.KEY_MSG_TYPE);
		      Log.d(TAG, Constants.KEY_MSG_TYPE + ": " + msgType);
		      if (msgType.equals(ProjectConstants.MSG_TYPE_FP_GAME_OVER)) {
		        Log.d(TAG, "Inside MSG_TYPE_FP_GAME_OVER = " + ProjectConstants.MSG_TYPE_FP_GAME_OVER);
		        Editor editor = projPreferences.edit();
		        editor.putBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, true);
		        editor.commit();
		        showFinalResultToPlayer();
		      }
		    }
		  }
	
	
	 private SharedPreferences getSharedPreferences() {
	        return getSharedPreferences(ProjectConstants.FINAL_PROJECT,Context.MODE_PRIVATE);
	  }
}

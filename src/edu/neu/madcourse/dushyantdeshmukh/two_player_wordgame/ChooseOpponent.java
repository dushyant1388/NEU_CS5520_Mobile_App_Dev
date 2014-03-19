package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseOpponent extends Activity implements OnClickListener {

  protected static final String TAG = "CHOOSE OPPONENT ACTIVITY";
  private Intent i;
  private String username, regId, oppName, oppRegId;
  Context context;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.two_player_wordgame_choose_opponent);
    
    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View findOpponentButton = findViewById(R.id.two_player_wordgame_find_opponent_button);
    findOpponentButton.setOnClickListener(this);
    
    View randomOpponentButton = findViewById(R.id.two_player_wordgame_random_opponent_button);
    randomOpponentButton.setOnClickListener(this);
    
    View backButton = findViewById(R.id.two_player_wordgame_back_button);
    backButton.setOnClickListener(this);
  
  }
  

  @Override
  protected void onStart() {
    super.onStart();
    //  Put username in AVAILABLE_USERS_LIST
    SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREF_CONST, context.MODE_PRIVATE);
    this.username = sp.getString(Constants.PREF_USERNAME, "");
    this.regId = sp.getString(Constants.PREF_REG_ID, "");
    
    Util.addValuesToKeyOnServer(Constants.AVAILABLE_USERS_LIST, this.username, this.regId);
  }
  
  @Override
  protected void onStop() {
    super.onStop();
    //  Remove username from AVAILABLE_USERS_LIST    
    Util.removeValuesFromKeyOnServer(Constants.AVAILABLE_USERS_LIST, this.username, this.regId);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.two_player_wordgame_find_opponent_button:
      EditText opponentEditText = (EditText) findViewById(R.id.two_player_wordgame_opponent_name_edittext);
      break;
    case R.id.two_player_wordgame_random_opponent_button:
      
      connectToRandomOpponent(this.username, this.regId);
      //  go to WaitingForOpponent activity dialog
//      i = new Intent(this, Game.class);
//      i.putExtra(Game.CONTINUE_GAME, false);
//      startActivity(i);
      break;
    case R.id.two_player_wordgame_back_button:
      finish();
      break;
    }
  }

  private void connectToRandomOpponent(String uname, String rId) {
 // check if user is waiting
    // If user waiting, pair with that user (Go to AwaitingConnection activity)
    // Else, add yourself to waiting user list and show toast (no random opponent available)
    
    new AsyncTask<String, Integer, String>() {
      @Override
      protected String doInBackground(String... params) {
        String retVal = "";
        String result = "";
        boolean foundOpponent = false;
        if (KeyValueAPI.isServerAvailable()) {
          String availableUsersList = KeyValueAPI.get(Constants.TEAM_NAME, Constants.PASSWORD,
              Constants.AVAILABLE_USERS_LIST);

          if (availableUsersList.contains("Error: No Such Key")) {
            // No player waiting... put your own regId
             retVal = "Error while putting your regId on server: "
                  + result;
          } else {
            String usersArr[] = availableUsersList.split(",");
            //  Iterate over list of entries in key 'keyname'and check for val1
            for (int i = 0; i < usersArr.length; i++) {
              String tempArr[] = usersArr[i].split("::");
              String oppName = tempArr[0];
              String oppRegId = tempArr[1];

              if (!oppRegId.equals(regId)) {
                Log.d(TAG, "\noppRegId= " + oppRegId + "\n");
                Log.d(TAG, "\nregId= " + regId + "\n");
                
                // Get opponents regId and connect
                Log.d(TAG, "Sending connect request to opponent'"
                    + "opponentName= " + oppName + ", opponentRegId= "
                    + oppRegId);
                
                try {
                  result = Util.sendPost("data." + Constants.KEY_MSG_TYPE + "="
                      + Constants.MSG_TYPE_CONNECT + "&data." + Constants.KEY_REG_ID + "="
                      + regId + "&data." + Constants.KEY_USERNAME + "=" + username, oppRegId);
                  Log.d(TAG, "Result of HTTP POST: " + result);
                  // displayMsg("Connected to user:" + oppName + " (" +
                  // oppRegId + ")");
                  retVal = "Sent connect request to opponent:" + oppName + " ("
                      + oppRegId + ")";
                  // sendPost("data=" + myRegId);
                } catch (Exception e) {
                  // TODO Auto-generated catch block
                  // displayMsg("Error occurred while making an HTTP post call.");
                  retVal = "Error occured while making an HTTP post call.";
                  e.printStackTrace();
                }
                foundOpponent = true;
                break;
              }
            }
          }
        }
        if (!foundOpponent) {
          retVal = "No player is available online. Please try again later.";
        }
        Log.d(TAG, "retVal: " + retVal);
        return retVal;
      }

      @Override
      protected void onPostExecute(String result) {
        // mDisplay.append(msg + "\n");
        Toast t = Toast.makeText(getApplicationContext(), result, 2000);
        t.show();
        Log.d(TAG, "\n===================================================\n");
        Log.d(TAG, "result: " + result);
      }
    }.execute(null, null, null);
  }

 
}

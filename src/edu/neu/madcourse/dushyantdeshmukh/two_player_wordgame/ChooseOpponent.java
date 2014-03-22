package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

import java.util.HashMap;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
  BroadcastReceiver receiver;

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

//    // This will handle the broadcast
//    receiver = new BroadcastReceiver() {
//      // @Override
//      public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "Inside onReceive of Broadcast receiver");
//        String action = intent.getAction();
//        if (action.equals("INTENT_ACTION")) {
//          String data = intent.getStringExtra("data");
//          Log.d(TAG, "data = " + data);
//          handleOpponentResponse(data);
//        }
//      }
//    };
  }

  @Override
  protected void onStart() {
    super.onStart();
    // Put username in AVAILABLE_USERS_LIST
    SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREF_CONST,
        context.MODE_PRIVATE);
    this.username = sp.getString(Constants.PREF_USERNAME, "");
    this.regId = sp.getString(Constants.PREF_REG_ID, "");

    Util.addValuesToKeyOnServer(Constants.AVAILABLE_USERS_LIST, this.username,
        this.regId);
  }

  @Override
  protected void onStop() {
    super.onStop();
    // Remove username from AVAILABLE_USERS_LIST
    Util.removeValuesFromKeyOnServer(Constants.AVAILABLE_USERS_LIST,
        this.username, this.regId);
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    // This needs to be in the activity that will end up receiving the broadcast
//    registerReceiver(receiver, new IntentFilter("INTENT_ACTION"));

    handleNotification(getSharedPreferences(Constants.SHARED_PREF_CONST,
        Context.MODE_PRIVATE));
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
//    unregisterReceiver(receiver);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.two_player_wordgame_find_opponent_button:
      EditText opponentEditText = (EditText) findViewById(R.id.two_player_wordgame_opponent_name_edittext);
      break;
    case R.id.two_player_wordgame_random_opponent_button:
      connectToRandomOpponent(this.username, this.regId);
      break;
    case R.id.two_player_wordgame_back_button:
      finish();
      break;
    }
  }

  private void connectToRandomOpponent(String uname, String rId) {
    // check if user is waiting
    // If user waiting, pair with that user (send game request)
    // Else, add yourself to waiting user list and show toast (no random
    // opponent available)

    new AsyncTask<String, Integer, String>() {
      @Override
      protected String doInBackground(String... params) {
        String retVal = "";
        String result = "";
        boolean foundOpponent = false;
        if (KeyValueAPI.isServerAvailable()) {
          String availableUsersList = KeyValueAPI.get(Constants.TEAM_NAME,
              Constants.PASSWORD, Constants.AVAILABLE_USERS_LIST);

          if (availableUsersList.contains("Error: No Such Key")) {
            // No player waiting... put your own regId
            retVal = "Error while putting your regId on server: " + result;
          } else {
            if (availableUsersList.trim() != "") {
              String usersArr[] = availableUsersList.split(",");
              // Iterate over list of entries in key 'keyname'and check for val1
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
                    result = Util.sendPost("data." + Constants.KEY_MSG_TYPE
                        + "=" + Constants.MSG_TYPE_2P_CONNECT + "&data."
                        + Constants.KEY_REG_ID + "=" + regId + "&data."
                        + Constants.KEY_USERNAME + "=" + username, oppRegId);
                    Log.d(TAG, "Result of HTTP POST: " + result);
                    // displayMsg("Connected to user:" + oppName + " (" +
                    // oppRegId + ")");
                    retVal = "Sent connect request to opponent:" + oppName
                        + " (" + oppRegId + ")";
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
        }
        if (!foundOpponent) {
          retVal = Constants.NO_PLAYER_ONLINE;
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
        if (!result.equals(Constants.NO_PLAYER_ONLINE)) {
          // go to WaitingForOpponent activity dialog
          Intent i = new Intent(getApplicationContext(),
              WaitingForOpponent.class);
          startActivity(i);
        }
      }
    }.execute(null, null, null);
  }

  protected void handleOpponentResponse(String data) {
    Log.d(TAG, "Inside handleOpponentResponse()");
    HashMap<String, String> dataMap = Util.getDataMap(data, TAG);
    if (dataMap.containsKey(Constants.KEY_MSG_TYPE)) {
      String msgType = dataMap.get(Constants.KEY_MSG_TYPE);
      Log.d(TAG, Constants.KEY_MSG_TYPE + ": " + msgType);
      if (msgType.equals(Constants.MSG_TYPE_2P_ACK_REJECT)) {
        // Show reject msg and return to ChooseOpponent activity
        String opponentName = dataMap.get(Constants.KEY_USERNAME);
        displayMsg("Game request denied by user '" + opponentName + "'.");
        i = new Intent(this, ChooseOpponent.class);
        startActivity(i);

      }
    }
  }

  private void handleNotification(SharedPreferences sp) {
    String data = sp.getString(Constants.KEY_NOTIFICATION_DATA, "");
    if (!data.equals("")) {
      handleOpponentResponse(data);
      sp.edit().putString(Constants.KEY_NOTIFICATION_DATA, "").commit();
    }
  }

  private void displayMsg(String msg) {
    Toast t = Toast.makeText(getApplicationContext(), msg, 2000);
    t.show();
    Log.d(TAG, "\n===================================================\n");
    Log.d(TAG, msg);
    Log.d(TAG, "\n===================================================\n");
    // TextView msgTxtView = (TextView)
    // findViewById(R.id.communication_interphone_comm_msg_textview);
    // msgTxtView.setText(msg);
  }

}

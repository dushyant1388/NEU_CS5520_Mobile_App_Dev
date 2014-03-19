package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public class RequestFromOpponent extends Activity implements OnClickListener {

  protected static final String TAG = "REQUEST FROM OPPONENT ACTIVITY";
  private Intent i;
  Context context;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.two_player_wordgame_request_from_opponent);
    
    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View acceptButton = findViewById(R.id.two_player_wordgame_accept_button);
    acceptButton.setOnClickListener(this);
    
    View rejectButton = findViewById(R.id.two_player_wordgame_reject_button);
    rejectButton.setOnClickListener(this);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.two_player_wordgame_accept_button:
      //  Go to ConnectedToOponent activity
      Intent intent = getIntent();
      String oppName = intent.getStringExtra(Constants.EXTRA_OPPONENT_NAME);
      String oppRegId = intent.getStringExtra(Constants.EXTRA_OPPONENT_REDID);
      
      // Store opponent name and regId in SP
      Util.storeOppnentInSharedpref(getSharedPreferences(Constants.SHARED_PREF_CONST,
          Context.MODE_PRIVATE), oppName, oppRegId);
      
      i = new Intent(this, MsgFromOpponent.class);
      i.putExtra(Constants.EXTRA_MSG, "Connected to '" + oppName + "'.");
      startActivity(i);
      break;
    case R.id.two_player_wordgame_reject_button:
      finish();
    }
  }
}

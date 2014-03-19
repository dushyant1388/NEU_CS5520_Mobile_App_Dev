package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.dushyantdeshmukh.R;

public class MsgFromOpponent extends Activity implements OnClickListener {

  protected static final String TAG = "MSG FROM OPPONENT ACTIVITY";
  private Intent i;
  Context context;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.two_player_wordgame_msg_from_opponent);
    
    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View continueButton = findViewById(R.id.two_player_wordgame_play_button);
    continueButton.setOnClickListener(this);
    
    TextView msgTextView = (TextView) findViewById(R.id.two_player_wordgame_msg_from_opponent_textview);
    Intent intent = getIntent();
    String msgTxt = intent.getStringExtra(Constants.EXTRA_MSG);
    msgTextView.setText(msgTxt);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.two_player_wordgame_play_button:
      //  start Game activity
      Toast.makeText(getApplicationContext(), "start Game activity", 2000).show();
      Log.d(TAG, "start Game activity");
    }
  }
}

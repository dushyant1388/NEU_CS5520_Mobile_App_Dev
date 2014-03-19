package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.dushyantdeshmukh.R;

public class WaitingForOpponent extends Activity implements OnClickListener {

  protected static final String TAG = "WAITING FOR OPPONENT ACTIVITY";
  private Intent i;
  Context context;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.two_player_wordgame_choose_opponent);
    
    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View backButton = findViewById(R.id.two_player_wordgame_back_button);
    backButton.setOnClickListener(this);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.two_player_wordgame_back_button:
      finish();
    }
  }
}

package edu.neu.madcourse.dushyantdeshmukh.wordgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.wordgame.Game;
import edu.neu.madcourse.dushyantdeshmukh.wordgame.Acknowledgements;
import android.view.View.OnClickListener;

public class WordGame extends Activity implements OnClickListener {

  private static final String TAG = "Word Game";
  
  public WordGame() {
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wordgame_main);
    
    // Set up click listeners for all the buttons
    View newgameButton = findViewById(R.id.wordgame_newgame_button);
    newgameButton.setOnClickListener(this);
    
    View continueButton = findViewById(R.id.wordgame_continue_button);
    continueButton.setOnClickListener(this);
    
    View returnButton = findViewById(R.id.wordgame_return_button);
    returnButton.setOnClickListener(this);
    
    View ackButton = findViewById(R.id.wordgame_ack_button);
    ackButton.setOnClickListener(this);
  }
  
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.wordgame_newgame_button:
      startGame(1);
      break;
    case R.id.wordgame_continue_button:
      break;
    case R.id.wordgame_return_button:
      finish();
      break;
    case R.id.wordgame_ack_button:
      Intent i = new Intent(this, Acknowledgements.class);
      startActivity(i);
      break;
    }
  }
  
  /** Start a new game with the given difficulty level */
  private void startGame(int i) {
     Log.d(TAG, "clicked on " + i);
     Intent intent = new Intent(this, Game.class);
     intent.putExtra(Game.KEY_DIFFICULTY, i);
     startActivity(intent);
  }
}

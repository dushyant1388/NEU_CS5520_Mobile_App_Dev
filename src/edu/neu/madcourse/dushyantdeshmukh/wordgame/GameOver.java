package edu.neu.madcourse.dushyantdeshmukh.wordgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.dushyantdeshmukh.R;

public class GameOver extends Activity implements OnClickListener {

	public GameOver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordgame_gameover);

		// Set up click listeners for all the buttons
		View restartButton = findViewById(R.id.wordgame_gameover_restart_button);
		restartButton.setOnClickListener(this);
		
		View quitButton = findViewById(R.id.wordgame_gameover_quit_button);
		quitButton.setOnClickListener(this);
		
		showDetails();
	}

	private void showDetails() {
        // TODO Auto-generated method stub
        
    }

    public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wordgame_gameover_restart_button:
			WordGame wordGame = new WordGame();
			wordGame.startGame(1);
			break;
		case R.id.wordgame_gameover_quit_button:
            finish();
            finish();
            break;
		}
	}
}

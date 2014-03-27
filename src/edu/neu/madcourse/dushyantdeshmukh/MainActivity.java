package edu.neu.madcourse.dushyantdeshmukh;

//import org.example.sudoku.About;
//import org.example.sudoku.Game;
//import org.example.sudoku.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.dushyantdeshmukh.communication.Communication;
import edu.neu.madcourse.dushyantdeshmukh.dictionary.Dictionary;
import edu.neu.madcourse.dushyantdeshmukh.sudoku.*;
import edu.neu.madcourse.dushyantdeshmukh.trickiestpart.TrickiestPart;
import edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame.TwoPlayerWordGame;
import edu.neu.madcourse.dushyantdeshmukh.wordgame.WordGame;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up click listeners for all the buttons

		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);

		View genErrorButton = findViewById(R.id.gen_error_button);
		genErrorButton.setOnClickListener(this);
		
		View sudokuButton = findViewById(R.id.sudoku_button);
		sudokuButton.setOnClickListener(this);
		
		View dictionaryButton = findViewById(R.id.dictionary_button);
		dictionaryButton.setOnClickListener(this);
		
		View wordgameButton = findViewById(R.id.wordgame_button);
		wordgameButton.setOnClickListener(this);
		
		View communicationButton = findViewById(R.id.communication_button);
		communicationButton.setOnClickListener(this);
		
		View two_player_wordgameButton = findViewById(R.id.two_player_wordgame_button);
		two_player_wordgameButton.setOnClickListener(this);

		View trickiestButton = findViewById(R.id.trickiest_part_button);
		trickiestButton.setOnClickListener(this);
		
		View quitButton = findViewById(R.id.quit_button);
		quitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.gen_error_button:
			throw new RuntimeException("Test runtime exception thrown by the 'Generate Error' button.");
		case R.id.sudoku_button:
			Intent i2 = new Intent(this, Sudoku.class);
			startActivity(i2);
			break;
		case R.id.dictionary_button:
			Intent i3 = new Intent(this, Dictionary.class);
			startActivity(i3);
			break;
		case R.id.wordgame_button:
      Intent i4 = new Intent(this, WordGame.class);
      startActivity(i4);
      break;
		case R.id.communication_button:
      Intent i5 = new Intent(this, Communication.class);
      startActivity(i5);
      break;
		case R.id.two_player_wordgame_button:
      Intent i6 = new Intent(this, TwoPlayerWordGame.class);
      startActivity(i6);
      break;
		case R.id.trickiest_part_button:
      Intent i7 = new Intent(this, TrickiestPart.class);
      startActivity(i7);
      break;
		case R.id.quit_button:
			finish();
			break;
		}
	}
}

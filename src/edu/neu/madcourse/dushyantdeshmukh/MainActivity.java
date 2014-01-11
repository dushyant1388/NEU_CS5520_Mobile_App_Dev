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
import edu.neu.madcourse.dushyantdeshmukh.sudoku.*;

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
		case R.id.quit_button:
			finish();
			break;
		}
	}
}

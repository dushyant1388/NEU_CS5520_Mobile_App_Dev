package edu.neu.madcourse.dushyantdeshmukh.wordgame;

import java.util.HashSet;
import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Game extends Activity implements OnClickListener {

    private static final String TAG = "Word Game";
    public static final String KEY_DIFFICULTY = "wordgame_dificulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;
    protected static final int DIFFICULTY_CONTINUE = -1;

    public int total_rows = 7;
    public int total_cols = 5;

    public char board[][];
    private boolean isPaused = false;

    private String currWord = "";
    private HashSet<String> currSelections = new HashSet<String>();

    public Game() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        board = getInitialBoard(diff);
        // calculateUsedTiles();
        // boardView = new BoardView(this);
        // setContentView(boardView);
        setContentView(R.layout.wordgame_game);
        // boardView.requestFocus();

        // Set up click listeners for all the buttons

        View clearButton = findViewById(R.id.wordgame_clear_button);
        clearButton.setOnClickListener(this);

        View currwordButton = findViewById(R.id.wordgame_currword_button);
        currwordButton.setOnClickListener(this);

        View pauseButton = findViewById(R.id.wordgame_pause_button);
        pauseButton.setOnClickListener(this);

        View hintButton = findViewById(R.id.wordgame_hint_button);
        hintButton.setOnClickListener(this);

        View quitButton = findViewById(R.id.wordgame_quit_button);
        quitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.wordgame_clear_button:
            Button currwordButton = (Button) findViewById(R.id.wordgame_currword_button);
            currwordButton.setText("");
            this.currWord = "";
            this.currSelections.clear();
            break;
        case R.id.wordgame_currword_button:
            String currWord = ((Button) v).getText().toString();
            break;
        case R.id.wordgame_pause_button:
            if (isPaused) {
                isPaused = false;
                ((Button) v).setText(R.string.wordgame_pause);
            } else {
                isPaused = true;
                ((Button) v).setText(R.string.wordgame_resume);
            }
            break;
        case R.id.wordgame_hint_button:
            break;
        case R.id.wordgame_quit_button:
            finish();
            break;
        }
    }

    private char[][] getInitialBoard(int diff) {
        board = new char[total_rows][total_cols];
        // for (int i = 0; i < total_rows; i++) {
        // for (int j = 0; j < total_cols; j++) {
        // board[i][j] = 'o';
        // }
        // }
        board[0][0] = 'D';
        board[4][2] = 'K';
        board[3][4] = 'V';
        board[3][3] = 'A';
        board[1][4] = 'M';
        board[1][3] = 'R';
        board[1][2] = 'A';
        board[5][1] = 'O';
        board[5][3] = 'E';
        board[6][2] = 'H';
        return board;
    }

    public void selectLetter(int row, int col) {
        // Check if letter exists and not already selected
        if (board[row][col] != '\u0000'
                && !currSelections.contains(row + "," + col)) {
            Log.d(TAG, "Selected letter '" + board[row][col] + "'");
            // add letter to currWord
            Button currWordBtn = (Button) findViewById(R.id.wordgame_currword_button);
            currWord = currWordBtn.getText().toString();
            currWord += board[row][col];
            currWordBtn.setText(currWord);

            // store (r, c) in currSelections list
            currSelections.add(row + "," + col);
        }
    }
}

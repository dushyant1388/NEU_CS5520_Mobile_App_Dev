package edu.neu.madcourse.dushyantdeshmukh.wordgame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Game extends Activity {

  private static final String TAG = "Word Game";
  public static final String KEY_DIFFICULTY = "wordgame_dificulty";
  public static final int DIFFICULTY_EASY = 0;
  public static final int DIFFICULTY_MEDIUM = 1;
  public static final int DIFFICULTY_HARD = 2;
  protected static final int DIFFICULTY_CONTINUE = -1;

  public int total_rows = 7;
  public int total_cols = 5;

  public char board[][];
  private BoardView boardView;

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
    boardView = new BoardView(this);
    setContentView(boardView);
    boardView.requestFocus();
  }

  private char[][] getInitialBoard(int diff) {
    board = new char[total_rows][total_cols];
//    for (int i = 0; i < total_rows; i++) {
//      for (int j = 0; j < total_cols; j++) {
//        board[i][j] = 'o';
//      }
//    }
    board[0][0] = 'D';
    board[4][2] = 'K';
    board[3][4] = 'V';
    return board;
  }
}

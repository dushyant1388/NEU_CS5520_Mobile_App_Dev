package edu.neu.madcourse.dushyantdeshmukh.wordgame;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.util.BloomFilter;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
    
    private BloomFilter<String> bloomFilter;
    private HashSet<String> wordList = new HashSet<String>();
    
    private boolean isCurrWordValid = false;
    private int currScore = 0;

    private char letterSet1[] = {'B', 'C', 'D', 'G', 'H','K', 'L', 
                                'M', 'N', 'P', 'R', 'S', 'T'};
    private char letterSet2[] = {'A', 'E', 'I', 'O', 'U'};
    private char letterSet3[] = {'F', 'J', 'V', 'Q', 'W', 'X', 'Y', 'Z'};
    private int letterSetCount = 0;
    
    final Handler myHandler = new Handler();
    
    final Runnable myRunnable = new Runnable() {
        public void run() {
            //  introduce a new letter
            char newLetter = getNewLetter();
            Log.d(TAG, "Inserting new letter: " + newLetter);
            insertNewLetter(newLetter);
        }
     };
    
    public Game() {
        // TODO Auto-generated constructor stub
    }

    protected void insertNewLetter(char newLetter) {
        for (int i = total_rows - 1; i >= 0; i--) {
            for (int j = 0; j < total_cols; j++) {
                if (board[i][j] == '\u0000'){
                    board[i][j] = newLetter;
                    BoardView boardView = (BoardView) findViewById(R.id.wordgame_board_view);
                    boardView.clearRect(j, i);
                    return;
                }
            }
        }
        //  Game over!!!
    }

    /**
     * Selects a letter from the 3 sets in the folowing ratio
     *  letterSet1 : letterSet2 : letterSet3 = 4 : 2 : 1
     * @return
     */
    protected char getNewLetter() {
        char retChar;
        letterSetCount = (letterSetCount + 1) % 7;
        if (letterSetCount == 1 || letterSetCount == 2 
                || letterSetCount == 4 || letterSetCount == 5) {
            //  get next letter from set 1
            retChar = getRandomChar(this.letterSet1);
        } else if (letterSetCount == 3 || letterSetCount == 6) {
            //  get next letter from set 2
            retChar = getRandomChar(this.letterSet2);
        } else {
            //  get next letter from set 3
            retChar = getRandomChar(this.letterSet3);
        }
        return retChar;
    }

    private char getRandomChar(char[] letterSet) {
        int randomIndex = (int)(Math.random() * letterSet.length); 
        return letterSet[randomIndex];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        loadBitsetFromFile("compressedWordlist.txt");
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
    protected void onResume() {
        super.onResume();
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
           @Override
           public void run() {
               myHandler.post(myRunnable);
           }
        }, 0, 5000);
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
            if (isCurrWordValid) {
                //checkWord(currWord);
                addWord(currWord);
                ((Button) v).setText("");
                this.currWord = "";
                currScore += currWord.length();
                TextView currScoreView = (TextView) findViewById(R.id.wordgame_currscore);
                currScoreView.setText("Score: " + currScore);
                removeCurrWordLetters();
            } else {
                ((Button) v).setTextColor(Color.RED);
            }
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

    private void removeCurrWordLetters() {
        Iterator<String> iterator = this.currSelections.iterator();
        BoardView boardView = (BoardView) findViewById(R.id.wordgame_board_view);
        while (iterator.hasNext()) {
            String tempStrArr[] = iterator.next().split(",");
            int i = Integer.parseInt(tempStrArr[0]);
            int j = Integer.parseInt(tempStrArr[1]);
            board[i][j] = '\u0000';
            boardView.clearRect(j, i);
        }
        this.currSelections.clear();
        
        
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
        
//        board[1][3] = 'R';
//        board[1][2] = 'A';
//        board[5][1] = 'O';
//        board[5][3] = 'E';
//        board[6][2] = 'H';
//        
//        board[1][0] = 'P';
//        board[1][2] = 'L';
//        board[5][4] = 'O';
//        board[5][3] = 'E';
//        board[6][4] = 'U';
//        
//        board[0][3] = 'S';
//        board[4][2] = 'C';
//        board[3][1] = 'T';
//        board[3][2] = 'A';
//        board[1][1] = 'W';
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
            if (currWord.length() > 2 && isWordValid(currWord)) {
                currWordBtn.setTextColor(Color.GREEN);
                isCurrWordValid = true;
            } else {
                isCurrWordValid = false;
                currWordBtn.setTextColor(getResources().getColor(R.color.board_letter));
            }
        }
    }
    
    protected boolean isWordValid(String ipWord) {
        ipWord = ipWord.toLowerCase();
        if (bloomFilter.contains(ipWord) && !wordList.contains(ipWord)) {
            Log.d(TAG, ipWord + " is a valid word.");
            //playValidWordBeep();
            //addWord(ipWord);
            return true;
        } else {
            Log.d(TAG, ipWord + " is an invalid word.");
            return false;
        }
    }
    
    private void addWord(String ipWord) {
        wordList.add(ipWord);
        //renderWorList();
    }
    
    private BloomFilter<String> loadBitsetFromFile(String filepath) {
        try {
            AssetManager am = this.getAssets();
            int fileLength = (int) am.openFd(filepath).getLength();
            Log.d(TAG, "compressed file length = " + fileLength);
            InputStream is = am.open(filepath);

            byte[] fileData = new byte[fileLength];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(fileData);
            dis.close();
            bloomFilter = new BloomFilter<String>(0.0001, 450000);
            bloomFilter = BloomFilter.loadBitsetWithByteArray(fileData,
                    bloomFilter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bloomFilter;
    }
}

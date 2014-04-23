package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public class GameFinish extends Activity implements OnClickListener {

  protected static final String TAG = "GAME FINISH ACTIVITY";

  private TextView finalScoreText;
  private Button mainMenuButton;
  private SharedPreferences projPreferences;
  Context context;
  boolean isSinglePhoneMode;
  boolean isOpponentGameOver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_proj_game_finish);

    context = this;
    projPreferences = getSharedPreferences();
    isSinglePhoneMode = projPreferences.getBoolean(
        ProjectConstants.IS_SINGLE_PHONE_MODE, false);

    // Set up click listeners for all the buttons
    mainMenuButton = (Button) findViewById(R.id.final_proj_main_menu_button);
    mainMenuButton.setOnClickListener(this);
    finalScoreText = (TextView) findViewById(R.id.final_proj_show_result);

    projPreferences = getSharedPreferences();
  }

  @Override
  protected void onResume() {
    super.onResume();
    showFinalResultToPlayer();
  }

  private void showFinalResultToPlayer() {
    Log.d(TAG, "Inside isSinglePhoneMode(), isSinglePhoneMode = "
        + isSinglePhoneMode);

    String resultMsg;
    String resultDetailsMsg;

    if (isSinglePhoneMode) {
      int p1Time = projPreferences.getInt(ProjectConstants.PLAYER_1_TIME, -1);
      int p2Time = projPreferences.getInt(ProjectConstants.PLAYER_1_TIME, -1);
      int p1ImageCount = projPreferences.getInt(
          ProjectConstants.PLAYER_2_IMAGE_COUNT, -1);
      int p2ImageCount = projPreferences.getInt(
          ProjectConstants.PLAYER_2_IMAGE_COUNT, -1);

      resultMsg = getSinglePhoneResultMsg(p1Time, p2Time, p1ImageCount,
          p2ImageCount);
      resultDetailsMsg = getResultDetailMsg("Player 1", "Player 2", p1Time,
          p2Time, p1ImageCount, p2ImageCount);
    } else {
      int playerTime = projPreferences.getInt(ProjectConstants.PLAYER_TIME, -1);
      int oppTime = projPreferences.getInt(ProjectConstants.OPPONENT_TIME, -1);
      int playerImageCount = projPreferences.getInt(
          ProjectConstants.PLAYER_IMAGE_COUNT, -1);
      int oppImageCount = projPreferences.getInt(
          ProjectConstants.PLAYER_IMAGE_COUNT, -1);

      resultMsg = getDualPhoneResultMsg(playerTime, oppTime, playerImageCount,
          oppImageCount);
      resultDetailsMsg = getResultDetailMsg("You", "Your opponent", playerTime,
          oppTime, playerImageCount, oppImageCount);
    }
    finalScoreText.setText(resultMsg + "\n" + resultDetailsMsg);
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.final_proj_main_menu_button:
      Intent mainMenuIntent = new Intent(context, Home.class);
      mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(mainMenuIntent);
      break;
    }
  }

  private String getSinglePhoneResultMsg(int p1Time, int p2Time,
      int p1ImageCount, int p2ImageCount) {
    String resultMsg = "";
    if (p1ImageCount == p2ImageCount && p1Time == p2Time) {
      resultMsg = "Scores tied!";
    } else if (p1ImageCount > p2ImageCount) {
      resultMsg = "Player 1 Won!";
    } else if (p1ImageCount < p2ImageCount) {
      resultMsg = "Player 2 Won!";
    } else if (p1Time < p2Time) {
      resultMsg = "You Won!";
    } else {
      resultMsg = "Player 2 Won!";
    }
    return resultMsg;
  }

  private String getDualPhoneResultMsg(int playerTime, int oppTime,
      int playerImageCount, int oppImageCount) {
    String resultMsg = "";
    if (playerImageCount == oppImageCount && playerTime == oppTime) {
      resultMsg = "Scores tied!";
    } else if (playerImageCount > oppImageCount) {
      resultMsg = "You Won!";
    } else if (playerImageCount < oppImageCount) {
      resultMsg = "You Lost!";
    } else if (playerTime < oppTime) {
      resultMsg = "You Won!";
    } else {
      resultMsg = "You Lost!";
    }
    return resultMsg;
  }

  public static String getResultDetailMsg(String p1Name, String p2Name,
      int p1Time, int p2Time, int p1ImageCount, int p2ImageCount) {
    String msg = p1Name + " captured " + p1ImageCount + " out of "
        + ProjectConstants.TOTAL_NO_OF_IMAGES + " images in "
        + Util.getTimeStr(p1Time) + " mins \n" + p2Name + " captured "
        + p2ImageCount + " out of " + ProjectConstants.TOTAL_NO_OF_IMAGES
        + " images in " + Util.getTimeStr(p2Time) + " mins";

    return msg;
  }

  private SharedPreferences getSharedPreferences() {
    return getSharedPreferences(ProjectConstants.FINAL_PROJECT,
        Context.MODE_PRIVATE);
  }
}
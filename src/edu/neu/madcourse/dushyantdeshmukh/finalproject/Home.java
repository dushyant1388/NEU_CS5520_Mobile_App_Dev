package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public class Home extends Activity implements OnClickListener {

  private static final String TAG = "HOME ACIVITY";
  Context context;
  AlertDialog alertDialog;
  ImageButton dualPhoneModeButton, singlePhoneModeButton, exitGameButton;
  boolean isDualPhoneModeSelected = false;
  private SharedPreferences projPreferences;

  public Home() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_proj_home);

    context = this;
    projPreferences = getSharedPreferences();

    // Set up click listeners for all the buttons
    dualPhoneModeButton = (ImageButton) findViewById(R.id.final_proj_dual_phone_mode_button);
    dualPhoneModeButton.setOnClickListener(this);

    singlePhoneModeButton = (ImageButton) findViewById(R.id.final_proj_single_phone_mode_button);
    singlePhoneModeButton.setOnClickListener(this);

    exitGameButton = (ImageButton) findViewById(R.id.final_proj_exit_game_button);
    exitGameButton.setOnClickListener(this);

  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onClick(View v) {
    boolean skipTutorial = projPreferences.getBoolean(
        ProjectConstants.PREF_SKIP_TUTORIAL, false);
    switch (v.getId()) {
    case R.id.final_proj_single_phone_mode_button:
      initiateGameInSinglePhoneMode();
      if (skipTutorial) {
        Util.showSinglePhoneDialog(this, ProjectConstants.SINGLE_PHONE_P1_CAPTURE_STATE);
      } else {
        Intent tutorialIntent = new Intent(this, Tutorial.class);
        startActivity(tutorialIntent);
      }
      break;
    case R.id.final_proj_dual_phone_mode_button:
      initiateGameInDualPhoneMode();
      if (skipTutorial) {
        Intent dualPhoneIntent = new Intent(this, Connection.class);
        startActivity(dualPhoneIntent);
      } else {
        Intent tutorialIntent = new Intent(this, Tutorial.class);
        startActivity(tutorialIntent);
      }
      break;
    case R.id.final_proj_exit_game_button:
      finish();
      break;
    }
  }

  /**
   * Initializes vars in shared preferences and starts a game in single phone mode
   */
  private void initiateGameInSinglePhoneMode() {
    Editor e = projPreferences.edit();
    e.putBoolean(ProjectConstants.IS_SINGLE_PHONE_MODE, true);
    e.putInt(ProjectConstants.SINGLE_PHONE_CURR_STATE, ProjectConstants.SINGLE_PHONE_P1_CAPTURE_STATE);
    e.commit();
    Log.d(TAG, "isSinglePhoneMode = " + projPreferences.getBoolean(ProjectConstants.IS_SINGLE_PHONE_MODE, false));
  }
  
  /**
   * Initializes vars in shared preferences and starts a game in dual phone mode
   */
  private void initiateGameInDualPhoneMode() {
    Editor e = projPreferences.edit();
    e.putBoolean(ProjectConstants.IS_SINGLE_PHONE_MODE, false);
    e.commit();
    Log.d(TAG, "isSinglePhoneMode = " + projPreferences.getBoolean(ProjectConstants.IS_SINGLE_PHONE_MODE, false));
  }

  private SharedPreferences getSharedPreferences() {
    return getSharedPreferences(ProjectConstants.FINAL_PROJECT,
        Context.MODE_PRIVATE);
  }

}

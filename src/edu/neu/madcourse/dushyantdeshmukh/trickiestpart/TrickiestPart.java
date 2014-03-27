package edu.neu.madcourse.dushyantdeshmukh.trickiestpart;

import java.util.HashMap;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.AccelerometerListener;
import edu.neu.madcourse.dushyantdeshmukh.utilities.AccelerometerManager;
import edu.neu.madcourse.dushyantdeshmukh.utilities.InternetConnUtil;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TrickiestPart extends Activity implements OnClickListener {

  protected static final String TAG = "TRICKIEST PART ACTIVITY";
  private Intent i;
  Context context;

  static {
    System.loadLibrary("image_detection");
  }
  
  /* A native method that is implemented by the
   * 'hello-jni' native library, which is packaged
   * with this application.
   */
  public native String stringFromJNI();

  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.trickiest_part_main);

    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View testButton = findViewById(R.id.trickiest_part_test_button);
    testButton.setOnClickListener(this);

    View quitButton = findViewById(R.id.trickiest_part_quit_button);
    quitButton.setOnClickListener(this);

  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();

  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.trickiest_part_test_button:
      TextView msgText = (TextView) findViewById(R.id.trickiest_part_msg);
      msgText.setText(stringFromJNI());
      break;
    case R.id.trickiest_part_quit_button:
      finish();
      break;
    }
  }

  private void displayMsg(String msg) {
    // Toast t = Toast.makeText(getApplicationContext(), msg, 2000);
    // t.show();
    Log.d(TAG, "\n===================================================\n");
    Log.d(TAG, msg);
    Log.d(TAG, "\n===================================================\n");
    TextView msgTxtView = (TextView) findViewById(R.id.trickiest_part_msg);
    msgTxtView.setText(msg);
  }

}

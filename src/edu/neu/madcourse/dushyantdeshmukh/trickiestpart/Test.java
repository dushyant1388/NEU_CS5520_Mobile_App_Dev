package edu.neu.madcourse.dushyantdeshmukh.trickiestpart;

import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Test extends Activity implements OnClickListener {

  protected static final String TAG = "TRICKIEST PART TEST ACTIVITY";
  private Intent i;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Inside onCreate()");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.trickiest_part_test);

    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View testButton = findViewById(R.id.trickiest_part_capture_img_button);
    testButton.setOnClickListener(this);

    View ackButton = findViewById(R.id.trickiest_part_match_img_button);
    ackButton.setOnClickListener(this);

    View instructionsButton = findViewById(R.id.trickiest_part_clear_img_button);
    instructionsButton.setOnClickListener(this);

    View quitButton = findViewById(R.id.trickiest_part_quit_button);
    quitButton.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    Log.d(TAG, "Inside onClick()");
    switch (v.getId()) {
    case R.id.trickiest_part_capture_img_button:

      break;
    case R.id.trickiest_part_match_img_button:

      break;
    case R.id.trickiest_part_clear_img_button:

      break;
    case R.id.trickiest_part_quit_button:
      finish();
      break;
    }
  }

}

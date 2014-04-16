package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Tutorial extends Activity implements OnClickListener {

  Context context;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.final_proj_tutorial);

    context = this;
    Button practiceButton, skipButton;

    // Set up click listeners for all the buttons
    practiceButton = (Button) findViewById(R.id.final_proj_practice);
    practiceButton.setOnClickListener(this);

    skipButton = (Button) findViewById(R.id.final_proj_skip);
    skipButton.setOnClickListener(this);

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
    switch (v.getId()) {
    case R.id.final_proj_practice:
      Intent practiceIntent = new Intent(this, Practice.class);
      practiceIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(practiceIntent);
      break;
    case R.id.final_proj_skip:
      //  Go to capture activity
      Intent captureIntent = new Intent(context, CaptureImage.class);
      captureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(captureIntent); 
      break;
    }
  }

}

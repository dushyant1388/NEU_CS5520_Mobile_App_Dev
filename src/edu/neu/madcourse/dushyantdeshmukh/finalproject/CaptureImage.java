package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public class CaptureImage extends BaseCameraActivity {

  protected static final String TAG = "CAPTURE ACTIVITY";
  LayoutInflater controlInflater = null;
  View captureButton, acceptButton, rejectButton;
  TextView imgCountView;
  ImageView capturedImgView;
  byte[] currImgData;
  Bitmap imgArr[];
  int currImgNo = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // set final_proj_capture layout as an overlayed layout
    // on top of the camera preview layout
    controlInflater = LayoutInflater.from(getBaseContext());
    View viewControl = controlInflater.inflate(R.layout.final_proj_capture,
        null);
    LayoutParams layoutParamsControl = new LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    this.addContentView(viewControl, layoutParamsControl);

    // Set up click listeners for all the buttons
    captureButton = findViewById(R.id.final_proj_capture);
    captureButton.setOnClickListener(this);

    acceptButton = findViewById(R.id.final_proj_accept);
    acceptButton.setOnClickListener(this);

    rejectButton = findViewById(R.id.final_proj_reject);
    rejectButton.setOnClickListener(this);

    imgCountView = (TextView) findViewById(R.id.img_count);
    capturedImgView = (ImageView) findViewById(R.id.captured_image);

    // Show capture btn and hide accept/reject btns
    // Show camera preview and hide the captured img imageView
    showCapturedImg(false);

    imgArr = new Bitmap[totalNoOfImgs];
    
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  public void onClick(View v) {
    Log.d(TAG, "Inside onClick()");
    switch (v.getId()) {
    case R.id.final_proj_capture:
      Log.d(TAG, "Clicked on Capture button... taking picture...");
      takePicture();
      break;
    case R.id.final_proj_accept:
      storeCurrImg();
      showCapturedImg(false);
      break;
    case R.id.final_proj_reject:
      showCapturedImg(false);
      break;
    }
  }

  private void storeCurrImg() {
    imgCountView.setText("Img Count: " + currImgNo + "/" + totalNoOfImgs);
    if (imgArr == null) {
      imgArr = new Bitmap[totalNoOfImgs];
    }
    imgArr[currImgNo - 1] = currBmpImg;
    Util.storeImg(currImgData, currImgNo, context);
    currImgNo++;
    
    if (currImgNo > totalNoOfImgs) {
      // finished capturing images
      Util.showToast(context, "Finished capturing " + totalNoOfImgs + " images",
          3000);
      Util.showSwapPhonesAlertDialog(context,this,false);
    }
  }

  protected CharSequence getTakePictureWaitMsg() {
    return ProjectConstants.CAPTURE_WAIT_MSG;
  }

  private void showCaptureBtn(boolean show) {
    if (show) {
      captureButton.setVisibility(View.VISIBLE);
      acceptButton.setVisibility(View.GONE);
      rejectButton.setVisibility(View.GONE);
    } else {
      captureButton.setVisibility(View.GONE);
      acceptButton.setVisibility(View.VISIBLE);
      rejectButton.setVisibility(View.VISIBLE);
    }
  }

  private void showCapturedImg(boolean show) {
    if (show) {
      capturedImgView.setVisibility(View.VISIBLE);
      preview.setVisibility(View.GONE);
    } else {
      capturedImgView.setVisibility(View.GONE);
      preview.setVisibility(View.VISIBLE);
    }
    showCaptureBtn(!show);
  }

  public void startMatchActivity() {
	  Intent captureIntent = new Intent(context,MatchImage.class);
	  captureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	  startActivity(captureIntent);		
  }

  @Override
  protected void processCapturedPicture(byte[] data) {
    currImgData = data;

    // show captured image in image view
    capturedImgView.setImageBitmap(currBmpImg);
    showCapturedImg(true);
    progress.cancel();
  }

  /*
   * new AsyncTask<String, Integer, String>() {
   * 
   * @Override protected void onPreExecute() { super.onPreExecute();
   * progress.setMessage("Processing captured image..."); progress.show(); }
   * 
   * @Override protected String doInBackground(String... params) { String retVal
   * = "";
   * 
   * Log.d(TAG, "retVal: " + retVal); return retVal; }
   * 
   * @Override protected void onPostExecute(String result) {
   * super.onPostExecute(result); progress.cancel(); } }.execute(null, null,
   * null);
   */
}

package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.io.IOException;

import org.opencv.android.OpenCVLoader;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.trickiestpart.CameraPreview;
import edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame.Constants;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CaptureImage extends Activity implements OnClickListener {

  protected static final String TAG = "CAPTURE ACTIVITY";
  Context context;
  LayoutInflater controlInflater = null;
  private Camera mCamera;
  private CameraPreview mPreview;
  private FrameLayout preview;
  View captureButton, acceptButton, rejectButton;
  TextView imgCountView;
  ProgressDialog progress;
  ImageView capturedImgView;
  Bitmap currImg;
  Bitmap imgArr[];
  private Bitmap bmpImg;
  private int scale = 8;  //  lesser the value clearer the img

  int currImgNo = 1;
  int noOfImgs = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    context = getApplicationContext();

    // set camera preview as main layout for this activity
    setContentView(R.layout.final_proj_cam_preview);

    // Create an instance of Camera
    mCamera = getCameraInstance();

    // Create our Preview view and set it as the content of our activity.
    mPreview = new CameraPreview(this, mCamera);
    preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);

    try {
      mCamera.setPreviewDisplay(mPreview.getHolder());
    } catch (IOException ex) {
      Log.e(TAG, "Error setting camera preview display");
      ex.printStackTrace();
      Util.showToast(context, "Error setting camera preview display", 3000);
    }

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

    imgArr = new Bitmap[noOfImgs];
    // Initialize ProgressDialog
    progress = new ProgressDialog(this);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "Inside onResume()");

    if (mCamera == null) {
      mCamera = getCameraInstance();
      mPreview = new CameraPreview(this, mCamera);
      preview.addView(mPreview);
    }
    mCamera.startPreview();

  }

  @Override
  protected void onPause() {
    super.onPause();
    releaseCamera();
    preview.removeView(mPreview);
  }

  private void releaseCamera() {
    if (mCamera != null) {
      mCamera.release(); // release the camera for other applications
      mCamera = null;
    }
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
    imgCountView.setText("Img Count: " + currImgNo);
    if (imgArr == null) {
      imgArr = new Bitmap[noOfImgs];
    }
    imgArr[currImgNo - 1] = currImg;
    currImgNo++;
    
    if (currImgNo > noOfImgs) {
      // fnished capturing images
      Util.showToast(context, "Finished capturing " + noOfImgs + " images",
          3000);
    }
  }

  private void takePicture() {
    new AsyncTask<String, Integer, String>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        progress.setMessage("Processing captured image...");
        progress.show();
      }

      @Override
      protected String doInBackground(String... params) {
        String retVal = "";
        mCamera.takePicture(null, null, mPicture);
        Log.d(TAG, "retVal: " + retVal);
        return retVal;
      }

      @Override
      protected void onPostExecute(String result) {
        super.onPostExecute(result);

      }
    }.execute(null, null, null);
  }

  /** A safe way to get an instance of the Camera object. */
  public static Camera getCameraInstance() {
    Camera c = null;
    try {
      c = Camera.open(); // attempt to get a Camera instance
    } catch (Exception e) {
      Log.e(TAG, "Camera is not available (in use or does not exist)");
      // Camera is not available (in use or does not exist)
    }
    return c; // returns null if camera is unavailable
  }

  // ////////////////////////////////
  private PictureCallback mPicture = new PictureCallback() {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "Inside onPictureTaken()");

      mCamera.startPreview();

      // Replace Capture btn with Accept and reject btns
      // showCaptureBtn(false);

      BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;

      if (bmpImg != null) {
        bmpImg.recycle();
      }
      bmpImg = BitmapFactory.decodeByteArray(data, 0, data.length, o2);
      // show captured image in image view
      capturedImgView.setImageBitmap(bmpImg);
      showCapturedImg(true);
      // dislayCapturedImg(bmp);
      progress.cancel();
    }
  };

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

  private void dislayCapturedImg(Bitmap bmImg) {

    capturedImgView.setImageBitmap(bmImg);
    capturedImgView.setVisibility(View.VISIBLE);
    // capturedImgView.setAlpha(75);

    // Log.d(TAG, "\n Cancelling progress dialog... \n");
    // progress.cancel();
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

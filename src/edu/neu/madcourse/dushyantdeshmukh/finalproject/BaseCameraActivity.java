package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.trickiestpart.CameraPreview;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public abstract class BaseCameraActivity extends Activity implements OnClickListener {

  protected static final String TAG = "BASE CAMERA ACTIVITY";
  protected Context context;
  protected Camera mCamera;
  protected CameraPreview mPreview;
  protected FrameLayout preview;
  protected ProgressDialog progress;
  protected Bitmap currBmpImg;
  protected int totalNoOfImgs = ProjectConstants.TOTAL_NO_OF_IMAGES;
  protected SharedPreferences projPreferences;
  protected static WindowManager windowManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeControls();
  }

  private SharedPreferences getSharedPreferences() {
    return getSharedPreferences(ProjectConstants.FINAL_PROJECT,
        Context.MODE_PRIVATE);
  }

  private void initializeControls() {
    Log.d(TAG, "Inside onCreate()");

    context = this;
    projPreferences = getSharedPreferences();

    // set camera preview as main layout for this activity
    setContentView(R.layout.final_proj_cam_preview);

    // initialize WindowManager instance
    windowManager = getWindowManager();

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

  protected void takePicture() {
    new AsyncTask<String, Integer, String>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        progress.setMessage(getTakePictureWaitMsg());
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
  
  private PictureCallback mPicture = new PictureCallback() {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "Inside onPictureTaken()");

      mCamera.startPreview();

      if (currBmpImg != null) {
        currBmpImg.recycle();
      }
      currBmpImg = Util.convertByteArrToBitmap(data);
      
      processCapturedPicture(data);

    }
  };

  /** A safe way to get an instance of the Camera object. */
  public Camera getCameraInstance() {
    Camera camera = null;
    try {
      camera = Camera.open(); // attempt to get a Camera instance
    } catch (Exception e) {
      Log.e(TAG, "Camera is not available (in use or does not exist)");
      // Camera is not available (in use or does not exist)
      Util.showToast(context, "Camera is not available (in use or does not exist)", 4000);
      return camera;
    }
      Parameters parameters = camera.getParameters();
      
      DisplayMetrics displaymetrics = new DisplayMetrics();
      windowManager.getDefaultDisplay().getMetrics(displaymetrics);
      int screenHeight = displaymetrics.heightPixels;
      int screenWidth = displaymetrics.widthPixels;
      
      Log.d(TAG, "Inside getcameraInstance(), setting picture size to screenWidth X screenHeight = " 
            + screenWidth + " X " + screenHeight);
      parameters.setPictureSize(screenWidth, screenHeight);
      camera.setParameters(parameters);
    return camera; // returns null if camera is unavailable
  }

  @Override
  public abstract void onClick(View arg0);
  
  protected abstract CharSequence getTakePictureWaitMsg();
  
  protected abstract void processCapturedPicture(byte[] data);
 
}

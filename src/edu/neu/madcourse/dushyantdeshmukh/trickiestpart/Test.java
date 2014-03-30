package edu.neu.madcourse.dushyantdeshmukh.trickiestpart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.dushyantdeshmukh.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class Test extends Activity implements OnClickListener {

  protected static final String TAG = "TRICKIEST PART TEST ACTIVITY";
  private Intent i;
  Context context;
  private Camera mCamera;
  private CameraPreview mPreview;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Inside onCreate()");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.trickiest_part_test);

    context = getApplicationContext();

    // Set up click listeners for all the buttons
    View captureImgButton = findViewById(R.id.trickiest_part_capture_img_button);
    captureImgButton.setOnClickListener(this);

    View matchButton = findViewById(R.id.trickiest_part_match_img_button);
    matchButton.setOnClickListener(this);

    View clearButton = findViewById(R.id.trickiest_part_clear_img_button);
    clearButton.setOnClickListener(this);

    View quitButton = findViewById(R.id.trickiest_part_quit_button);
    quitButton.setOnClickListener(this);

    // Create an instance of Camera
    mCamera = getCameraInstance();

    // Create our Preview view and set it as the content of our activity.
    mPreview = new CameraPreview(this, mCamera);
    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    preview.addView(mPreview);

    try {
      mCamera.setPreviewDisplay(mPreview.getHolder());
    } catch (IOException ex) {
      Log.e(TAG, "Error setting preview display");
      ex.printStackTrace();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    mCamera.startPreview();
  }

  private PictureCallback mPicture = new PictureCallback() {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "Inside onPictureTaken()");

      mCamera.startPreview();

      File pictureFile = getOutputMediaFile();
      if (pictureFile == null) {
        Log.d(TAG, "Error creating media file, check storage permissions!");
        return;
      }

      try {
        FileOutputStream fos = new FileOutputStream(pictureFile);
        fos.write(data);
        fos.close();
      } catch (FileNotFoundException e) {
        Log.d(TAG, "File not found: " + e.getMessage());
      } catch (IOException e) {
        Log.d(TAG, "Error accessing file: " + e.getMessage());
      }
    }
  };

  @Override
  public void onClick(View v) {
    Log.d(TAG, "Inside onClick()");
    switch (v.getId()) {
    case R.id.trickiest_part_capture_img_button:
      // get an image from the camera
      Log.d(TAG, "Clicked on Capture button... taking picture...");
      try {
        mCamera.takePicture(null, null, mPicture);
      } catch (Exception ex) {
        Log.e(TAG, ex.getMessage());
        ex.printStackTrace();
      }
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

  @Override
  protected void onPause() {
    super.onPause();
    releaseCamera();
  }

  private void releaseCamera() {
    if (mCamera != null) {
      mCamera.release(); // release the camera for other applications
      mCamera = null;
    }
  }

  /** Create a file Uri for saving an image or video */
  private Uri getOutputMediaFileUri(int type) {
    return Uri.fromFile(getOutputMediaFile());
  }

  /** Create a File for saving an image or video */
  private File getOutputMediaFile() {
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.

    File mediaStorageDir = new File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "MyCameraApp");
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        Log.d("MyCameraApp", "failed to create directory");
        return null;
      }
    }

    // Create a media file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
        .format(new Date());
    File mediaFile = new File(mediaStorageDir.getPath() + File.separator
        + "IMG_" + timeStamp + ".jpg");

    return mediaFile;
  }

}

package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.io.IOException;

import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.trickiestpart.CameraPreview;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;

public class CaptureImage extends Activity implements OnClickListener {

  protected static final String TAG = "CAPTURE ACTIVITY";
  Context context;
  LayoutInflater controlInflater = null;
  private Camera mCamera;
  private CameraPreview mPreview;
  private FrameLayout preview;
  View captureButton, acceptButton, rejectButton;
  
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
    
    //  set final_proj_capture layout as an overlayed layout 
    //  on top of the camera preview layout
    controlInflater = LayoutInflater.from(getBaseContext());
    View viewControl = controlInflater.inflate(R.layout.final_proj_capture, null);
    LayoutParams layoutParamsControl
     = new LayoutParams(LayoutParams.FILL_PARENT,
     LayoutParams.FILL_PARENT);
    this.addContentView(viewControl, layoutParamsControl);
  }
  
  @Override
  public void onClick(View v) {
    
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

}

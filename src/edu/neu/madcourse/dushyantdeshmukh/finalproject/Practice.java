package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import edu.neu.madcourse.dushyantdeshmukh.R;
import edu.neu.madcourse.dushyantdeshmukh.R.id;
import edu.neu.madcourse.dushyantdeshmukh.trickiestpart.CameraPreview;
import edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame.Constants;
import edu.neu.madcourse.dushyantdeshmukh.utilities.Util;

public class Practice extends Activity implements OnClickListener {

  protected static final String TAG = "PRACTICE ACTIVITY";
  private static final String IMG_1_NAME = "IMG_1.jpg";
  private static final String IMG_2_NAME = "IMG_2.jpg";
  Context context;
  LayoutInflater controlInflater = null;
  private Camera mCamera;
  private CameraPreview mPreview;
  private FrameLayout preview;
  View captureButton, matchButton, clearButton, quitButton;
  ProgressDialog progress;
  ImageView capturedImgView, imgToMatchView;
  Bitmap currImg;
  private Bitmap bmpImg;
  protected boolean isImg1Present = false;
  private int scale = 8; // lesser the value clearer the img
  private SharedPreferences projPreferences;


  private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    @Override
    public void onManagerConnected(int status) {
      switch (status) {
      case LoaderCallbackInterface.SUCCESS: {
        Log.i(TAG, "OpenCV loaded successfully");
        // mOpenCvCameraView.enableView();
      }
        break;
      default: {
        super.onManagerConnected(status);
      }
        break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "Inside onCreate()");
    super.onCreate(savedInstanceState);

    context = this;
    projPreferences = getSharedPreferences();

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

    // set final_proj_image_to_match and final_proj_match layouts as
    // overlayed layouts on top of the camera preview layout
    controlInflater = LayoutInflater.from(getBaseContext());
    View imgToMatchViewControl = controlInflater.inflate(
        R.layout.final_proj_img_to_match, null);
    View practiceViewControl = controlInflater.inflate(R.layout.final_proj_practice,
        null);

    LayoutParams layoutParamsControl = new LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

    this.addContentView(imgToMatchViewControl, layoutParamsControl);
    this.addContentView(practiceViewControl, layoutParamsControl);

    // Set up click listeners for all the buttons
    captureButton = findViewById(R.id.final_proj_capture);
    captureButton.setOnClickListener(this);
    
    matchButton = findViewById(R.id.final_proj_match);
    matchButton.setOnClickListener(this);

    clearButton = findViewById(R.id.final_proj_clear);
    clearButton.setOnClickListener(this);

    quitButton = findViewById(R.id.final_proj_quit);
    quitButton.setOnClickListener(this);

    //  read and display Image1 if present
    Log.d(TAG, "isImg1Present = " + isImg1Present);
    if (isImg1Present) {
      dislayImg(readImgBmp(IMG_1_NAME));
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

    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
        mLoaderCallback);

    if (mCamera == null) {
      mCamera = getCameraInstance();
      mPreview = new CameraPreview(this, mCamera);
      preview.addView(mPreview);
    }
    mCamera.startPreview();
    
    showMatchBtn(isImg1Present);
  }

  private void showMatchBtn(boolean show) {
    if (show) {
      captureButton.setVisibility(View.GONE);
      matchButton.setVisibility(View.VISIBLE);
    } else {
      captureButton.setVisibility(View.VISIBLE);
      matchButton.setVisibility(View.GONE);
    }
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
      progress.setMessage("Storing image...");
      progress.show();
      Log.d(TAG, "Clicked on Capture button... taking picture...");
        mCamera.takePicture(null, null, mPicture);
      break;
    case R.id.final_proj_match:
      progress.setMessage("Matching image... ");
      progress.show();
      Log.d(TAG, "Clicked on Match button... taking & matching picture...");
      mCamera.takePicture(null, null, mPicture);
      break;
    case R.id.final_proj_clear:
      if (imgToMatchView != null) {
        Log.d(TAG, "Clearing captured image 1...");
        imgToMatchView.setVisibility(View.GONE);
        isImg1Present = false;
      }
      showMatchBtn(false);
      break;
    case R.id.final_proj_quit:
      //    Go to capture activity
      Intent captureIntent = new Intent(context, CaptureImage.class);
      captureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(captureIntent); 
      break;
    }
  }
  
  private void takePicture() {
    new AsyncTask<String, Integer, String>() {
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        progress.setMessage(ProjectConstants.MATCH_WAIT_MSG);
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

      if (isImg1Present) {
        Log.d(TAG, "\n Matching image... \n");
        // Match the current Img with Img1
        storeImg(IMG_2_NAME, data);
        matchImages(IMG_1_NAME, IMG_2_NAME);
      } else {
        Log.d(TAG, "\n Storing image... \n");
      // Store current Img as Img1

        storeImg(IMG_1_NAME, data);
        isImg1Present = true;
        
        //  Replace Capture btn with Match btn
        showMatchBtn(true);
        
        //  show IMG 1 in image view
        dislayImg(readImgBmp(IMG_1_NAME));
      }
      
      /*BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;

      if (bmpImg != null) {
        bmpImg.recycle();
      }
      bmpImg = BitmapFactory.decodeByteArray(data, 0, data.length, o2);

      // match imgsToMatchArr[currImgNo - 1] with bmpImg
      Mat imgMat1 = Util.convertBmpToMat(bmpImg);
      Mat imgMat2 = Util.convertBmpToMat(imgsToMatchArr[currImgIndex]);

      if (Util.imagesMatch(imgMat1, imgMat2)) {
        // if match successful, increment img count and set
        // isImgMatchedArr[currImgNo]

        progress.cancel();
        Util.showToast(context, ProjectConstants.MATCH_SUCCESS_MSG, 1500);
        
        // Send game move message to opponent.
        String oppRegId = projPreferences.getString(ProjectConstants.PREF_OPPONENT_REG_ID, null);
        if(oppRegId != null){
        	sendGameMoveOrFinishToOpponent(true,oppRegId,imagesMatched,0);
        }*/
        progress.cancel();
      
    }
  };
  
    
  /*Editor editor = projPreferences.edit();
  editor.putBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, true);
  editor.commit();*/
  
  
  private SharedPreferences getSharedPreferences() {
      return getSharedPreferences(ProjectConstants.FINAL_PROJECT,Context.MODE_PRIVATE);
}
  
  private Bitmap readImgBmp(String imgName) {
    File mediaStorageDir = new File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "MyCameraApp");
    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap bitmap = BitmapFactory.decodeFile(mediaStorageDir.getPath() + File.separator
        + imgName, options);
    return bitmap;
  }

  private void dislayImg(Bitmap bmImg) {
    imgToMatchView = (ImageView) findViewById(R.id.image_to_match);
    imgToMatchView.setImageBitmap(bmImg);
    imgToMatchView.setAlpha(ProjectConstants.IMG_ALPHA);
   
    Log.d(TAG, "\n Cancelling progress dialog... \n");
    progress.cancel();
  }

  protected void matchImages(String img1Name, String img2Name) {
    File mediaStorageDir = new File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "MyCameraApp");
    String img1Path = mediaStorageDir.getPath()
        + File.separator + img1Name;
    String img2Path = mediaStorageDir.getPath()
        + File.separator + img2Name;
    
    boolean isMatching = false;
    Mat imageMatrix1 = Highgui.imread(img1Path);
      Mat imageMatrix2 = Highgui.imread(img2Path);
       
      double psnr = getPSNR(imageMatrix1, imageMatrix2);
      
      if(psnr >= 14.0){
        isMatching = true;
      }else{
        isMatching = false;
      }
      Log.d(TAG, "\n Cancelling progress dialog... \n");
      progress.cancel();
      showResult(isMatching);
  }

  private void showResult(boolean isMatching) {
    String msg = (isMatching? "Images matched!" : "Images did NOT match!"); 
    Toast t = Toast.makeText(getApplicationContext(), msg, 3000);
    t.show();
    Log.d(TAG, "\n===================================================\n");
    Log.d(TAG, (msg));
    Log.d(TAG, "\n===================================================\n");
  }

  protected void storeImg(String imgName, byte[] data) {
    File pictureFile = getOutputMediaFile(imgName);
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
  
  /** Create a File for saving an image or video */
  private File getOutputMediaFile(String imgName) {
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
    // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
    // .format(new Date());
    File mediaFile = new File(mediaStorageDir.getPath() + File.separator
        +imgName);
    Log.d(TAG, "Storing img at path: " + mediaStorageDir.getPath()
        + File.separator + imgName);
    return mediaFile;
  }

  double getPSNR(Mat i1, Mat i2)
  {
    Log.d(TAG, "Inside getPSNR()");
    Log.d(TAG, "Rows i1: "+ i1.rows() + " Cols i1: "+ i1.cols());
    Log.d(TAG, "Rows i2: "+ i2.rows() + " Cols i2: "+ i2.cols());
   Mat s1= new Mat();
   Core.absdiff(i1, i2, s1);       // |I1 - I2|
   s1.convertTo(s1, CvType.CV_32F);  // cannot make a square on 8 bits
   s1 = s1.mul(s1);           // |I1 - I2|^2

   Scalar s = Core.sumElems(s1);         // sum elements per channel

   double sse = s.val[0] + s.val[1] + s.val[2]; // sum channels

   Log.d(TAG, "sse: " + sse);
   
   if( sse <= 1e-10) // for small values return zero
       return 0;
   else
   {
       double  mse =sse /(double)(i1.channels() * i1.total());
       double psnr = 10.0* Math.log10((255*255)/mse);
       Log.d(TAG, "psnr: " + psnr);
       return psnr;
   }
  }
}

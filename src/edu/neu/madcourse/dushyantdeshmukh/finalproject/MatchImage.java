package edu.neu.madcourse.dushyantdeshmukh.finalproject;

import java.io.IOException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

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
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

public class MatchImage extends Activity implements OnClickListener {

  protected static final String TAG = "MATCH ACTIVITY";
  private Context context;
  private LayoutInflater controlInflater = null;
  private Camera mCamera;
  private CameraPreview mPreview;
  private FrameLayout preview;
  private View matchButton, skipButton, endButton;
  private TextView imgCountView, timeElapsedView;
  private ProgressDialog progress;
  private ImageView capturedImgView, imgToMatchView;
  private Bitmap currImg;
  private Bitmap imgsToMatchArr[];
  private boolean isImgMatchedArr[];
  private Bitmap bmpImg;
  private static WindowManager windowManager;
  private BroadcastReceiver receiver;
  private SharedPreferences projPreferences;

  int timeElapsed = 0; // in secs
  int imagesMatched = 0, currImgIndex = 0;
  int totalNoOfImgs = ProjectConstants.TOTAL_NO_OF_IMAGES;

  Timer myTimer = new Timer();
  final Handler myTimerHandler = new Handler();

  TimerTask timeElapsedTimerTask;

  final Runnable timeElapsedRunnable = new Runnable() {
    public void run() {
      // increment & update time elapsed text view
      timeElapsedView.setText(Util.getTimeStr(++timeElapsed));
    }
  };

  private void startTimeElapsedTimer() {
    timeElapsedTimerTask = new TimerTask() {
      @Override
      public void run() {
        myTimerHandler.post(timeElapsedRunnable);
      }
    };
    myTimer.schedule(timeElapsedTimerTask, 0, 1000);
  }

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
    
    //  initialize WindowManager instance
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

    // set final_proj_image_to_match and final_proj_match layouts as
    // overlayed layouts on top of the camera preview layout
    controlInflater = LayoutInflater.from(getBaseContext());
    View imgToMatchViewControl = controlInflater.inflate(
        R.layout.final_proj_img_to_match, null);
    View matchViewControl = controlInflater.inflate(R.layout.final_proj_match,
        null);

    LayoutParams layoutParamsControl = new LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

    this.addContentView(imgToMatchViewControl, layoutParamsControl);
    this.addContentView(matchViewControl, layoutParamsControl);

    // Set up click listeners for all the buttons
    matchButton = findViewById(R.id.final_proj_match);
    matchButton.setOnClickListener(this);

    skipButton = findViewById(R.id.final_proj_skip);
    skipButton.setOnClickListener(this);

    endButton = findViewById(R.id.final_proj_end);
    endButton.setOnClickListener(this);

    imgCountView = (TextView) findViewById(R.id.img_count);
    timeElapsedView = (TextView) findViewById(R.id.time_elapsed);
    capturedImgView = (ImageView) findViewById(R.id.captured_image);
    imgToMatchView = (ImageView) findViewById(R.id.image_to_match);

    imgsToMatchArr = Util.getImgsToMatch(totalNoOfImgs, context);
    Log.d(TAG, "imgArr.length = " + imgsToMatchArr.length
        + "\n\n totalNoOfImgs = " + totalNoOfImgs);
    isImgMatchedArr = new boolean[totalNoOfImgs];

    // This will handle the broadcast
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Inside onReceive of Broadcast receiver of ChooseOpponent.class");
        String action = intent.getAction();
        if (action.equals(ProjectConstants.INTENT_ACTION_GAME_MOVE_AND_FINISH)) {
          String data = intent.getStringExtra("data");
          Log.d(TAG, "data = " + data);
          handleOpponentResponse(data);
        }
      }
    };
    
    // render first image to match
    renderImgToMatch(0);

    // Initialize ProgressDialog
    progress = new ProgressDialog(this);
    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progress.setIndeterminate(true);

    // start timer
    startTimeElapsedTimer();
  }

  private void renderImgToMatch(int imgIndex) {
    imgToMatchView.setImageBitmap(imgsToMatchArr[imgIndex]);
    imgToMatchView.setAlpha(ProjectConstants.IMG_ALPHA);
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
    
 // This needs to be in the activity that will end up receiving the broadcast
    registerReceiver(receiver, new IntentFilter(ProjectConstants.INTENT_ACTION_GAME_MOVE_AND_FINISH));
    handleNotification(projPreferences);
    
  }

  @Override
  protected void onPause() {
    super.onPause();
    releaseCamera();
    preview.removeView(mPreview);
    unregisterReceiver(receiver);
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
    case R.id.final_proj_match:
      // Log.d(TAG, "Clicked on Capture button... taking picture...");
      takePicture();
      break;
    case R.id.final_proj_skip:
      skipToNextimg();
      break;
    case R.id.final_proj_end:
      // showCapturedImg(false);
      break;
    }
  }

  private void skipToNextimg() {
    if (imagesMatched == (totalNoOfImgs - 1)) {
      Util.showToast(context, ProjectConstants.SKIP_FAIL_MSG, 1500);
    } else {
      // Show next image to match
      currImgIndex = getNextImgIndex(currImgIndex);
      renderImgToMatch(currImgIndex);
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

  private PictureCallback mPicture = new PictureCallback() {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "Inside onPictureTaken()");

      mCamera.startPreview();

      if (bmpImg != null) {
        bmpImg.recycle();
      }
      bmpImg = Util.convertByteArrToBitmap(data);
      
      // match imgsToMatchArr[currImgNo - 1] with bmpImg
      Mat imgMat1 = Util.convertBmpToMat(bmpImg);
      Mat imgMat2 = Util.convertBmpToMat(imgsToMatchArr[currImgIndex]);

      if (Util.imagesMatch(imgMat1, imgMat2)) {
        // if match successful, increment img count and set isImgMatchedArr[currImgNo]

        progress.cancel();
        Util.showToast(context, ProjectConstants.MATCH_SUCCESS_MSG, 1500);
        
        // Send game move message to opponent.
        String oppRegId = projPreferences.getString(ProjectConstants.PREF_OPPONENT_REG_ID, null);
        if(oppRegId != null){
        	sendGameMoveOrFinishToOpponent(true,oppRegId,imagesMatched,0);
        }
        
        imagesMatched++;
        isImgMatchedArr[currImgIndex] = true;
        imgCountView.setText("Img Count: " + (imagesMatched) + "/"
            + totalNoOfImgs);

        if (imagesMatched == totalNoOfImgs) {
        	// stop timer and save time.
        	myTimer.cancel();
          // finished matching images
          Util.showToast(context, "Finished matching " + totalNoOfImgs
              + " images", 3000);
          
          Editor editor = projPreferences.edit();
          //TODO: change time
          editor.putString(ProjectConstants.PLAYER_TIME, Integer.toString(timeElapsed));
          editor.putString(ProjectConstants.PLAYER_IMAGE_COUNT, Integer.toString(imagesMatched));
          editor.commit();
          
          Log.d(TAG, "PLAYER TIME: " + timeElapsed + "PLAYER_IMAGE_COUNT: " + imagesMatched);
          
          sendGameMoveOrFinishToOpponent(false, oppRegId, imagesMatched, timeElapsed);
          
          //TODO: Check flag and call the activity
          boolean isOppGameOver = projPreferences.getBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, false);
          if(isOppGameOver){
        	  startGameFinishActivity();
          }else{
        	  //TODO: Dialog...
          }
        } else {
          // Show next image to match
          currImgIndex = getNextImgIndex(currImgIndex);
          renderImgToMatch(currImgIndex);
        }
      } else {
        progress.cancel();
        Util.showToast(context, ProjectConstants.MATCH_FAIL_MSG, 1500);
      }

    }
  };

  /**
   * Given the index of currently matched image, returns the index of next image
   * to be matched
   * @param currMatchedImgIndex
   * @return
   */
  protected int getNextImgIndex(int currMatchedImgIndex) {
    int nextImgIndex = (currMatchedImgIndex + 1) % totalNoOfImgs;
    while (isImgMatchedArr[nextImgIndex]) {
      nextImgIndex = (nextImgIndex + 1) % totalNoOfImgs;
    }
    Log.d(TAG, "inside getNextImgIndex() \n" + "currMatchedImgIndex = "
        + currMatchedImgIndex + ", nextImgIndex = " + nextImgIndex);
    return nextImgIndex;
  }
  
  private void handleNotification(SharedPreferences sp) {
	    String data = sp.getString(ProjectConstants.KEY_NOTIFICATION_DATA, "");
	    if (!data.equals("")) {
	      handleOpponentResponse(data);
	      sp.edit().putString(ProjectConstants.KEY_NOTIFICATION_DATA, "").commit();
	    }
	  }
	  
  protected void handleOpponentResponse(String data) {
	Log.d(TAG, "Inside handleOpponentResponse()");
    HashMap<String, String> dataMap = Util.getDataMap(data, TAG);
    if (dataMap.containsKey(Constants.KEY_MSG_TYPE)) {
      String msgType = dataMap.get(Constants.KEY_MSG_TYPE);
      Log.d(TAG, Constants.KEY_MSG_TYPE + ": " + msgType);
      if (msgType.equals(ProjectConstants.MSG_TYPE_FP_GAME_OVER)) {
        Log.d(TAG, "Inside MSG_TYPE_FP_GAME_OVER = " + ProjectConstants.MSG_TYPE_FP_GAME_OVER);
        String opponent_num_of_images = dataMap.get(ProjectConstants.NUMBER_OF_IMAGES);
        String opponent_matchingTime = dataMap.get(ProjectConstants.TOTAL_MATCHING_TIME);
        
        Editor editor = projPreferences.edit();
        editor.putBoolean(ProjectConstants.IS_OPPONENT_GAME_OVER, true);
        editor.putString(ProjectConstants.OPPONENT_TIME, opponent_matchingTime);
        editor.putString(ProjectConstants.OPPONENT_IMAGE_COUNT, opponent_num_of_images);
        editor.commit();
        
        if(imagesMatched == totalNoOfImgs){
        	startGameFinishActivity();
        }
      }else if(msgType.equals(ProjectConstants.MSG_TYPE_FP_MOVE)) {
    	  Log.d(TAG, "Inside MSG_TYPE_FP_MOVE = " + ProjectConstants.MSG_TYPE_FP_MOVE);
    	  // Show toast that opponent found out new Image
    	  Util.showToast(this, "Opponent matched new Image!", Toast.LENGTH_LONG);
    	  
      }
    }
    
  }
  
  public void startGameFinishActivity(){
      Intent gameFinishIntent = new Intent(context,GameFinish.class);
  	  gameFinishIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
  	  startActivity(gameFinishIntent);
    }
  
  
  private void sendGameMoveOrFinishToOpponent(boolean isGameMoveEvent, String oppRegId, int imagesMatched, int timeForMatching) {
	    Log.d(TAG, "Sending request ack: " 
	  + (isGameMoveEvent? ProjectConstants.MSG_TYPE_FP_MOVE : ProjectConstants.MSG_TYPE_FP_GAME_OVER));
	    new AsyncTask<String, Integer, String>() {
	      @Override
	      protected String doInBackground(String... params) {
	        String retVal;
	        boolean isGameMove = Boolean.parseBoolean(params[0]);
	        int imagesMatched = Integer.parseInt(params[1]);
	        int matchingTime = Integer.parseInt(params[2]);
	        String oppRegId = params[3];
	        try {
	          retVal = Util.sendPost("data." + Constants.KEY_MSG_TYPE
	              + "=" + (isGameMove? ProjectConstants.MSG_TYPE_FP_MOVE : ProjectConstants.MSG_TYPE_FP_GAME_OVER) 
	              + "&data." + ProjectConstants.NUMBER_OF_IMAGES + "=" + imagesMatched 
	              + (isGameMove?"":"&data."+ ProjectConstants.TOTAL_MATCHING_TIME + "=" +matchingTime),
	              oppRegId);
	          Log.d(TAG, "Result of HTTP POST: " + retVal);
	          // displayMsg("Connected to user:" + oppName + " (" +
	          // oppRegId + ")");
	          retVal = "Sent game message to opponent:"
	              + " (" + oppRegId + ")";
	          // sendPost("data=" + myRegId);
	        } catch (Exception e) {
	          retVal = "Error occured while making an HTTP post call.";
	          e.printStackTrace();
	        }
	        return retVal;
	      }
	      
	      @Override
	      protected void onPostExecute(String result) {
//	        Toast t = Toast.makeText(getApplicationContext(), result, 2000);
//	        t.show();
	        Log.d(TAG, "\n===================================================\n");
	        Log.d(TAG, "result: " + result);
	      }
	    }.execute(String.valueOf(isGameMoveEvent),String.valueOf(imagesMatched),String.valueOf(timeForMatching),oppRegId, null);
	  }
  
  
  
  
  
  private SharedPreferences getSharedPreferences() {
      return getSharedPreferences(ProjectConstants.FINAL_PROJECT,Context.MODE_PRIVATE);
}

}

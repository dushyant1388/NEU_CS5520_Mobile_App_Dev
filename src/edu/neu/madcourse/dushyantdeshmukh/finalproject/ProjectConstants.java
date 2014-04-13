package edu.neu.madcourse.dushyantdeshmukh.finalproject;

public class ProjectConstants {
	
	 //  Shared Preference ProjectConstants
	  public static final String SHARED_DUAL_PHONE_MODE_ON = "isTwoPhoneModeOn";
	  public static final String USER_NAME = "USER_NAME";
	  public static final String USER_REG_ID = "USER_REG_ID";
	  
	 //   Connection ProjectConstants
//	  public static final String KEY_MSG_TYPE = "MSG_TYPE";
	  public static final String MSG_TYPE_FP_MOVE = "FP_MOVE";
	  public static final String MSG_TYPE_FP_CONNECT = "FP_CONNECT";
	  public static final String MSG_TYPE_FP_ACK_ACCEPT = "FP_ACK_ACC";
	  public static final String MSG_TYPE_FP_ACK_REJECT = "FP_ACK_REJ";
	  public static final String MSG_TYPE_FP_GAME_OVER = "FP_GAME_OVER";
	  public static final String FINAL_PROJECT = "FINAL_PROJ";
	  
	//  mHealth server constants
	  	public static final String REGISTERED_USERS_LIST = "REGISTERED_USERS_LIST";
		public static final String PASSWORD = "numad14s";
		public static final String TEAM_NAME = "Dushyant";
		
		public static final String KEY_REG_ID = "REG_ID";
	    public static final String KEY_USERNAME = "USERNAME";
	    
	    public static final String PREF_OPPONENT_REG_ID = "OPPONENT_REG_ID";
	    public static final String PREF_OPPONENT_NAME = "OPPONENT_NAME";
	  
	// GCM constants
		public static final String PROPERTY_REG_ID = "registration_id";
		public static final String PROPERTY_APP_VERSION = "appVersion";
		public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;		
		public static final String SENDER_ID = "94466405712";

		public static final String INTENT_ACTION_CONNECTION = "INTENT_ACTION_CONNECTION";
		public static final String OPPONENT_NOT_FOUND = "Unable to find specified opponent."
			      + "Please search for another user or connect to a random opponent.";
	 
		
		public static final String KEY_NOTIFICATION_DATA = "NOTIFICATION_DATA";
		
		// Match and Capture constants
		
		public static double PSNR_THRESHOLD = 14.0;
		  public static int SCALE = 8;  //  lesser the value clearer the img
		  public static String IMG_DIR_NAME = "images_to_match";
		  public static String IMG_NAME_PREFIX = "IMAGE_NO_";
		  public static final int IMG_ALPHA = 85; // larger value indicates more focus on img to match
      public static final CharSequence CAPTURE_SWAP_MSG = "Swap Phone and ask opponent to press start to capture images";
      public static final CharSequence MATCH_SWAP_MSG = "Swap Phone and ask opponent to start matching challenge";
      public static final CharSequence START = "Start";
      public static final CharSequence SWAP_TITLE = "Swap Phones";
		  
		  //  Messages
		  public static String MATCH_WAIT_MSG = "Matching image...";
		  public static String CAPTURE_WAIT_MSG = "Processing captured image...";
		  public static String MATCH_SUCCESS_MSG = "Images matched successfully.";
		  public static String MATCH_FAIL_MSG = "Images did not match. Try again.";
		  public static String SKIP_FAIL_MSG = "Cannot skip. Only one image is left to match.";
		  
		  public static int TOTAL_NO_OF_IMAGES = 2;
	  
}

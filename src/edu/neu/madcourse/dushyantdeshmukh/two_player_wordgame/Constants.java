package edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame;

public class Constants {  
  public static final String PASSWORD = "numad14s";
  public static final String TEAM_NAME = "Dushyant";
  public static final String PROPERTY_APP_VERSION = "appVersion";
  public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  public static final String SERVER_URL = "https://android.googleapis.com/gcm/send";
  public static final String BROWSER_API_KEY = "AIzaSyBu3zhNf6EIipgPtgE00kFZ3PQv86cSsys";
  public static final String SENDER_ID = "94466405712";

  //  mHealth server constants
  public static final String AVAILABLE_USERS_LIST = "AVAILABLE_USERS_LIST";
  public static final String TOTAL_USERS_LIST = "TOTAL_USERS_LIST";
  public static final String TOP_SCORERS_LIST = "TOP_SCORERS_LIST";
  
  //  Shared Preference Constants
  public static final String SHARED_PREF_CONST = "TWO_PLAYER_WORD_GAME";
  public static final String ACTIVITY_ACTIVE_PREF = "ACTIVITY_ACTIVE";
  public static final String PREF_USERNAME = "USERNAME";
  public static final String PREF_REG_ID = "registration_id";
  public static final String PREF_OPPONENT_REG_ID = "OPPONENT_REG_ID";
  public static final String PREF_OPPONENT_NAME = "OPPONENT_NAME";
  public static final String PREF_CURR_SCOREBOARD = "CURR_SCOREBOARD";
  public static final String PREF_CURR_ROUND_NO = "CURR_ROUND_NO";
  public static final String PREF_HIGHEST_SCORE = "HIGHEST_SCORE";
  
  //  
    
  public static final String KEY_MSG_TYPE = "MSG_TYPE";
  public static final String MSG_TYPE_2P_MOVE = "2P_MOVE";
  public static final String MSG_TYPE_2P_CONNECT = "2P_CONNECT";
  public static final String MSG_TYPE_2P_ACK_ACCEPT = "ACK_ACCEPT";
  public static final String MSG_TYPE_2P_ACK_REJECT = "ACK_REJECT";
  
  public static final String KEY_REG_ID = "REG_ID";
  public static final String KEY_USERNAME = "USERNAME";
  public static final String KEY_MESSAGE = "MESSAGE";
  
  public static final String EXTRA_OPPONENT_NAME = "OPPONENT_NAME";
  public static final String EXTRA_OPPONENT_REDID = "OPPONENT_REGID";
  public static final String EXTRA_MSG = "MSG";
  public static final String EXTRA_ROUND = "ROUND";
  public static final String EXTRA_OPP_CURR_SCORE = "OPP_CURR_SCORE";

  
  public static final String KEY_NOTIFICATION_DATA = "NOTIFICATION_DATA";
  public static final String NETWORK_UNAVAILABLE_MSG = "Network unavailable. Please make sure you are connected to the internet.";
  protected static final String NO_PLAYER_ONLINE = "No player is available online. Please try again later.";
  
  //  Game constants
  public static final int NO_OF_ROUNDS = 5;
  public static final int TIME_PER_ROUND_IN_SECS = 20;
  
}

package edu.neu.madcourse.dushyantdeshmukh.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import edu.neu.madcourse.dushyantdeshmukh.two_player_wordgame.Constants;
import edu.neu.mhealth.api.KeyValueAPI;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Util {

  private static final String TAG = "Utility class";

  public static HashMap<String, String> getDataMap(String bundlesStr, String tag) {
    HashMap<String, String> dataMap = new HashMap<String, String>();
    bundlesStr = bundlesStr.substring(8, bundlesStr.length() - 2);
    String keyValArr[] = bundlesStr.split(", ");
    for (String str : keyValArr) {
      String tempArr[] = str.split("=");
      String tempKey = tempArr[0];
      String tempVal = tempArr[1];
      dataMap.put(tempKey, tempVal);
      Log.d(tag, "'" + tempKey + "' : '" + tempVal + "'");
    }
    return dataMap;
  }
  
  public static void playSound(Context context, MediaPlayer mp, int soundResId, boolean loop) {
    // Release any resources from previous MediaPlayer
    if (mp != null) {
      mp.release();
    }
    // Create a new MediaPlayer to play this sound
    mp = MediaPlayer.create(context, soundResId);
    mp.start();
    mp.setLooping(loop);
  }

  public static BloomFilter<String> loadBitsetFromFile(String filepath, AssetManager am) {
    BloomFilter<String> bloomFilter = null;;
    try {
      int fileLength = (int) am.openFd(filepath).getLength();
      Log.d(TAG, "compressed file length = " + fileLength);
      InputStream is = am.open(filepath);

      byte[] fileData = new byte[fileLength];
      DataInputStream dis = new DataInputStream(is);
      dis.readFully(fileData);
      dis.close();
      bloomFilter = new BloomFilter<String>(0.0001, 450000);
      bloomFilter = BloomFilter.loadBitsetWithByteArray(fileData, bloomFilter);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bloomFilter;
  }

  public static void addValuesToKeyOnServer(String keyname, String val1, String val2) {
    new AsyncTask<String, Integer, String>() {
      @Override
      protected String doInBackground(String... params) {
        Log.d(TAG, "inside addValuesToKeyOnServer():doInBackground()");
        String keyname = params[0];
        String val1 = params[1];
        String val2 = params[2];
        String retVal = "";
        String result = "";
        if (KeyValueAPI.isServerAvailable()) {
          String availableUsersVal = KeyValueAPI.get(Constants.TEAM_NAME, Constants.PASSWORD,
              keyname);

          if (availableUsersVal.contains("Error: No Such Key")) {
            Log.d(TAG, "no such key: " + keyname);
            // No player waiting... put your own regId
            result = KeyValueAPI.put(Constants.TEAM_NAME, Constants.PASSWORD,
                keyname, val1 + "::" + val2);
            if (!result.contains("Error")) {
              // displayMsg("No player waiting... putting your regId "
              // + myRegId + " in WAITING_PLAYER.");
              retVal = "No player waiting... storing your Username::RegistrationId "
                  + val1 + "::" + val2 + " on server.";
            } else {
              // displayMsg("Error while putting your regId on server: "
              // + result);
              retVal = "Error while putting your username::regId on server: "
                  + result;
            }
          } else {
            Log.d(TAG, "key exists: " + keyname);
            String usersArr[] = availableUsersVal.split(",");
            boolean valuePresent = false;
            //  Iterate over list of entries in key 'keyname'and check for val1
            for (int i = 0; i < usersArr.length; i++) {
              Log.d(TAG, "usersArr[i] = " + usersArr[i]);
              String tempArr[] = usersArr[i].split("::");
              String tempUsername = tempArr[0];
              String tempRegId = tempArr[1];
              if (tempUsername.equals(val1)){
                valuePresent = true;
                break;
              }
            }
            if (!valuePresent) {
              // append val1-val2 to value of key 'keyname'
              availableUsersVal += "," + val1 + "::" + val2;
              
              // store on server
              result = KeyValueAPI.put(Constants.TEAM_NAME, Constants.PASSWORD,
                  keyname, availableUsersVal);
              
              if (!result.contains("Error")) {
                // displayMsg("Stored your Username-RegistrationId "
//                    + val1 + "::" + val2 + " on server.");
                retVal = "Stored your Username::RegistrationId "
                    + val1 + "::" + val2 + " on server.";
              } else {
                // displayMsg("Error while putting your username-regId on server: "
                // + result);
                retVal = "Error while putting your username::regId on server: "
                    + result;
              }
            }
          }
        }
        return retVal;
      }

      @Override
      protected void onPostExecute(String result) {
        // mDisplay.append(msg + "\n");
        Log.d(TAG, "addValuesToKeyOnServer" + result);
      }
    }.execute(keyname, val1, val2);
  }
  
  public static void removeValuesFromKeyOnServer(String keyname, String val1, String val2) {
    new AsyncTask<String, Integer, String>() {
      @Override
      protected String doInBackground(String... params) {
        String keyname = params[0];
        String val1 = params[1];
        String val2 = params[2];
        String retVal = "";
        String result = "";
        if (KeyValueAPI.isServerAvailable()) {
          String availableUsersVal = KeyValueAPI.get(Constants.TEAM_NAME, Constants.PASSWORD,
              keyname);

          if (availableUsersVal.contains("Error: No Such Key")) {
            // Specified key does not exist on server
              retVal = "Specified key does not exist on server.";
          } else {
            String usersArr[] = availableUsersVal.split(",");
            StringBuilder newVal = new StringBuilder();
            //  Iterate over list of entries in key 'keyname'and check for val1
            for (int i = 0; i < usersArr.length; i++) {
              String tempArr[] = usersArr[i].split("::");
              String tempUsername = tempArr[0];
              String tempRegId = tempArr[1];
              if (!tempUsername.equals(val1) || !tempRegId.equals(val2)){
                newVal.append(usersArr[i]);
                newVal.append(",");
              }
            }
            String newStrVal = newVal.substring(0, newVal.length());
            
              // store new val on server
              result = KeyValueAPI.put(Constants.TEAM_NAME, Constants.PASSWORD,
                  keyname, newStrVal);
              
              if (!result.contains("Error")) {
                // displayMsg("Removed your val1-val2 from  "
//                    + val1 + "-" + val2 + " on server.");
                retVal = "Removed your val1::val2 from "
                    + val1 + "::" + val2 + " on server.";
              } else {
                // displayMsg("Error while removing val1-val2 from server: "
                // + result);
                retVal = "Error while removing val1::val2 from server: "
                    + result;
              }
          }
        }
        return retVal;
      }

      @Override
      protected void onPostExecute(String result) {
        // mDisplay.append(msg + "\n");
        Log.d(TAG, "addValuesToKeyOnServer" + result);
      }
    }.execute(keyname, val1, val2);
  }
  
  
//HTTP POST request
 public static String sendPost(String dataStr, String opponentRegId) throws Exception {

   String url = "https://selfsolve.apple.com/wcResults.do";
   URL obj = new URL(Constants.SERVER_URL);
   HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

   // add reuqest header
   con.setRequestMethod("POST");
   con.setRequestProperty("Authorization", "key=" + Constants.BROWSER_API_KEY);
   // con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

   String urlParameters = dataStr + "&registration_id=" + opponentRegId;

   // Send post request
   con.setDoOutput(true);
   DataOutputStream wr = new DataOutputStream(con.getOutputStream());
   wr.writeBytes(urlParameters);
   wr.flush();
   wr.close();

   int responseCode = con.getResponseCode();
   System.out.println("\nSending 'POST' request to URL : " + url);
   System.out.println("Post parameters : " + urlParameters);
   System.out.println("Response Code : " + responseCode);

   BufferedReader in = new BufferedReader(new InputStreamReader(
       con.getInputStream()));
   String inputLine;
   StringBuffer response = new StringBuffer();

   while ((inputLine = in.readLine()) != null) {
     response.append(inputLine);
   }
   in.close();

   // print result
   // displayMsg("\n HTTP Post response: " + response.toString());
   // Log.d(TAG, "HTTP POST response" + response.toString());
   return response.toString();
 }
 
 public static void storeOppnentInSharedpref(SharedPreferences sp, 
     String opponentName, String opponentRegId) {
   // Store opponent name and regId in SP
   Editor ed = sp.edit();
   ed.putString(Constants.PREF_OPPONENT_REG_ID, opponentRegId);
   ed.putString(Constants.PREF_OPPONENT_NAME, opponentName);
   ed.commit();
   Log.d(TAG, "Message sent to displayMsg() => Connected to opponent:"
       + opponentName + " (" + opponentRegId + ")");
 }
}

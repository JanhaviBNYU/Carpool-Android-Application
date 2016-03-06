package com.example.janhavibagwe.carpoolapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Hashtable;
import java.util.Map;


public class LoginScreen extends Activity {

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    String userName;
    String userID;

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.d("CARPOOLCHECK:","LoginScreen: Successful Login to Facebook");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            Log.d("CARPOOLCHECK:","LoginScreen: Get user Name and ID");
            userName = profile.getFirstName() + " " + profile.getLastName();
            userID = profile.getId();

            Log.d("CARPOOLCHECK:","LoginScreen: Adding user Name and ID to SharedPref");
            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.SharedPrefName), MODE_PRIVATE).edit();
            editor.putString("UserName", userName);
            editor.putString("UserID", userID);
            editor.commit();

            Log.d("CARPOOLCHECK:","LoginScreen: Intent MainScreen");
            Intent iMainScreen = new Intent(getApplicationContext(), MainScreen.class);
            iMainScreen.putExtra("userName",userName);
            iMainScreen.putExtra("userId",userID);

            startActivity(iMainScreen);
        }

        @Override
        public void onCancel() {
            Log.d("CARPOOLCHECK", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("CARPOOLCHECK", "onError " + e);
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mCallbackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        Log.d("CARPOOLCHECK:","LoginScreen: Configure FB Login");
        LoginButton mButtonLogin = (LoginButton) findViewById(R.id.fb_login_button);
        mButtonLogin.setReadPermissions("user_friends");
        mButtonLogin.registerCallback(mCallbackManager, mFacebookCallback);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("UserName", null);
        userID = prefs.getString("UserID",null);

        if(userName != null && userID != null){

            Log.d("CARPOOLCHECK:","LoginScreen: No User Name n ID in SharedPref");
            Intent iMainScreen = new Intent(getApplicationContext(), MainScreen.class);
            iMainScreen.putExtra("userName",userName);
            iMainScreen.putExtra("userId",userID);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("CARPOOLCHECK", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("CARPOOLCHECK", "" + currentProfile);

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
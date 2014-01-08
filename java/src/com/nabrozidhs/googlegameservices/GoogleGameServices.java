package com.nabrozidhs.googlegameservices;

import android.app.Activity;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.games.GamesClient;
import com.unity3d.player.UnityPlayer;

public final class GoogleGameServices
        implements GooglePlayServicesClient.ConnectionCallbacks,
                GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = GoogleGameServices.class.getSimpleName();
    
    private enum State {SIGNING_IN, CONNECTING, CONNECTED, NOT_CONNECTED};
    
    // Request code we use when invoking other Activities to complete the sign-in flow.
    private static final int RC_RESOLVE = 666;
    
    private final Activity mActivity;
    private final GamesClient mGamesClient;
    private final String mCallbackName;
    
    private State mState;
    
    public GoogleGameServices(final Activity activity, final String callbackName) {
        mActivity = activity;
        mCallbackName = callbackName;
        
        mGamesClient = new GamesClient
                .Builder(mActivity, this, this)
                .setGravityForPopups(Gravity.TOP | Gravity.RIGHT)
                .create();
        
        mState = State.NOT_CONNECTED;
    }

    /**
     * Starts a connection process to Google Play Game Services.
     */
    public void connect() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity) != ConnectionResult.SUCCESS) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available.");
            return;
        }
        
        mState = State.CONNECTING;
        mGamesClient.connect();
    }
    
    /**
     * Signs the user to Google Play Game Services (this is needed before being able to connect.
     */
    public void signIn() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity) != ConnectionResult.SUCCESS) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available.");
            return;
        }
        
        mState = State.SIGNING_IN;
        mGamesClient.connect();
    }
    
    /**
     * Disconnects from Google Play Game Services.
     */
    public void disconnect() {
        if (!isGamesServiceAvailable(mActivity, mGamesClient)) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available or not connected.");
            return;
        }
        
        mState = State.NOT_CONNECTED;
        mGamesClient.disconnect();
    }
    
    /**
     * Submit a score to the specified leaderboard.
     * 
     * @param leaderboardId leaderboard identifier.
     * @param score score to submit.
     */
    public void submitScore(final String leaderboardId, final long score) {
        if (!isGamesServiceAvailable(mActivity, mGamesClient)) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available or not connected.");
            return;
        }
        
        mGamesClient.submitScore(leaderboardId, score);
    }
    
    /**
     * Unlocks the achievement specified.
     * 
     * @param achievementId the achievement to unlock.
     */
    public void unlockAchievement(final String achievementId) {
        if (!isGamesServiceAvailable(mActivity, mGamesClient)) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available or not connected.");
            return;
        }
        
        mGamesClient.unlockAchievement(achievementId);
    }
    
    /**
     * Shows the achievements screen.
     */
    public void showAchievements() {
        if (!isGamesServiceAvailable(mActivity, mGamesClient)) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available or not connected.");
            return;
        }
        
        mActivity.startActivityForResult(mGamesClient.getAchievementsIntent(), RC_RESOLVE);
    }
    
    /**
     * Shows the leaderboards screen.
     */
    public void showLeaderboards() {
        if (!isGamesServiceAvailable(mActivity, mGamesClient)) {
            // Google Play Services not available.
            Log.d(TAG, "Google Play Services not available or not connected.");
            return;
        }
        
        mActivity.startActivityForResult(mGamesClient.getAllLeaderboardsIntent(), RC_RESOLVE);
    }
    
    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        if (connectionResult.hasResolution() && mState == State.SIGNING_IN) {
            mState = State.CONNECTING;
            try {
                connectionResult.startResolutionForResult(mActivity, RC_RESOLVE);
            } catch (SendIntentException e) {
                // According to Google I should retry here...
                Log.e(TAG, "" + e.getMessage(), e);
                connect();
            }
            
            return;
        }
        
        mState = State.NOT_CONNECTED;
        
        if (mCallbackName != null) {
            UnityPlayer.UnitySendMessage(mCallbackName, "OnConnectionFailed", "");
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mState = State.CONNECTED;
        
        if (mCallbackName != null) {
            UnityPlayer.UnitySendMessage(mCallbackName, "OnConnected", "");
        }
    }

    @Override
    public void onDisconnected() {
        mState = State.NOT_CONNECTED;
        
        if (mCallbackName != null) {
            UnityPlayer.UnitySendMessage(mCallbackName, "OnDisconnected", "");
        }
    }
    
    /**
     * Checks if the games services are available for the current user.
     * 
     * @param activity
     * @param gamesClient
     * 
     * @return true if the games services are available, false otherwise.
     */
    private static boolean isGamesServiceAvailable(
            final Activity activity, final GamesClient gamesClient) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS
                && gamesClient.isConnected();
    }
}

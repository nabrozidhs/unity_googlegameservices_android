using System;
using UnityEngine;

public sealed class GoogleGameServicesPlugin : MonoBehaviour {

	private const string CLASS_NAME = "com.nabrozidhs.googlegameservices.GoogleGameServices";

    private const string CALL_CONNECT = "connect";
	private const string CALL_DISCONNECT = "disconnect";
	private const string CALL_SIGNIN = "signIn";
	private const string CALL_SHOWACHIEVEMENTS = "showAchievements";
	private const string CALL_UNLOCKACHIEVEMENT = "unlockAchievement";

	public static event Action Connected = delegate {};
	public static event Action Disconnected = delegate {};
	public static event Action ConnectionFailed = delegate {};

#if UNITY_ANDROID
    private AndroidJavaObject plugin;
#endif

    /// <summary>
    /// Bind this instance.
    /// </summary>
    public void Bind() {
#if UNITY_ANDROID
        plugin = new AndroidJavaObject(
			CLASS_NAME,
			new AndroidJavaClass("com.unity3d.player.UnityPlayer")
				.GetStatic<AndroidJavaObject>("currentActivity"),
			gameObject.name);
#endif
    }

	/// <summary>
	/// Connects to the Game Services.
	/// </summary>
	public void Connect() {
#if UNITY_ANDROID
		if (plugin != null) {
			plugin.Call(CALL_CONNECT, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Disconnects from the Game Services.
	/// </summary>
	public void Disconnect() {
#if UNITY_ANDROID
		if (plugin != null) {
			plugin.Call(CALL_DISCONNECT, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Initiates a sign in process.
	/// </summary>
	public void SignIn() {
#if UNITY_ANDROID
		if (plugin != null) {
			plugin.Call(CALL_SIGNIN, new object[0]);
		}
#endif
	}

	/// <summary>
	/// Unlocks the achievement specified.
	/// </summary>
	/// <param name="achievementId">
	/// Achievement identifier.
	/// </param>
	public void UnlockAchievement(string achievementId) {
#if UNITY_ANDROID
		if (plugin != null) {
			plugin.Call(CALL_UNLOCKACHIEVEMENT, new object[] {achievementId});
		}
#endif
	}

	/// <summary>
	/// Shows the achievements screen.
	/// </summary>
	public void ShowAchievements() {
#if UNITY_ANDROID
		if (plugin != null) {
			plugin.Call(CALL_SHOWACHIEVEMENTS, new object[0]);
		}
#endif
	}

	public void OnConnectionFailed() {
		ConnectionFailed();
	}

	public void OnConnected() {
		Connected();
	}

	public void OnDisconnected() {
		Disconnected();
	}

	void OnDestroy() {
		// Disconnect from the game services service.
		Disconnect();
	}
}

using UnityEngine;
using System.Collections;

[RequireComponent(typeof(GoogleGameServicesPlugin))]
public class Menu : MonoBehaviour {

	private const string ACHIEVEMENT = "YOUR_ACHIEVEMENT";

    private static Vector2 BUTTON_SIZE = new Vector2(100, 50);
    
    private Rect buttonPositionConnect;
	private Rect buttonPositionAchievements;
	private Rect buttonPositionUnlockAchievement;

    private GoogleGameServicesPlugin gameServices;

	private bool showAchievements;

	void Awake() {
		showAchievements = false;

		#if UNITY_ANDROID
		gameServices = GetComponent<GoogleGameServicesPlugin>();
		GoogleGameServicesPlugin.Connected += HandleConnected;
		GoogleGameServicesPlugin.Disconnected += HandleDisconnected;
		GoogleGameServicesPlugin.ConnectionFailed += HandleConnectionFailed;
		gameServices.Bind();
		#endif
	}

    void Start() {
        buttonPositionConnect = new Rect(
            (Screen.width - BUTTON_SIZE.x) / 2,
            (Screen.height - BUTTON_SIZE.y) / 2,
            BUTTON_SIZE.x, BUTTON_SIZE.y);

		buttonPositionAchievements = new Rect(
			buttonPositionConnect.x, buttonPositionConnect.y + BUTTON_SIZE.y * 3 / 2,
			buttonPositionConnect.width, buttonPositionConnect.height);

		buttonPositionUnlockAchievement = new Rect(
			buttonPositionAchievements.x, buttonPositionAchievements.y + BUTTON_SIZE.y * 3 / 2,
			buttonPositionAchievements.width, buttonPositionAchievements.height);
    }

	void OnApplicationPause(bool pauseStatus) {
		if (pauseStatus) {
			#if UNITY_ANDROID
			gameServices.Disconnect();
			#endif
		} else {
			#if UNITY_ANDROID
			gameServices.Connect();
			#endif
		}
	}

    void OnGUI () {
        if (showAchievements) {
			if (GUI.Button(buttonPositionAchievements, "Show Achievements")) {
				gameServices.ShowAchievements();
			}

			if (GUI.Button(buttonPositionUnlockAchievement, "Unlock Achievements")) {
				gameServices.UnlockAchievement(ACHIEVEMENT);
			}
		} else {
			if (GUI.Button(buttonPositionConnect, "Sign in")) {
				#if UNITY_ANDROID
				gameServices.SignIn();
				#endif
			}
		}
    }

    /// <summary>
    /// Handles the completed event from GoogleGameServicesPlugin.
    /// </summary>
    public void HandleConnected() {
		showAchievements = true;
		Debug.Log("connected");
    }

	public void HandleDisconnected() {
		Debug.Log("disconnected");
	}

	public void HandleConnectionFailed() {
		Debug.Log("connectionFailed");
	}
}

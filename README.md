# Unity Google Game Services for Android (deprecated)

Google released a plugin for Google Play Game Services, I would recommend switching to that ([playgameservices/play-games-plugin-for-unity](https://github.com/playgameservices/play-games-plugin-for-unity)).

## Overview

(Really) Basic Unity3D plugin for Google Play Game Services.


## Integration

### Importing

Just import the file `googlegameservices.unitypackage` in your Unity project.

### Project setup

Open your app's manifest file and add the following tag as a child of the `<application>` element:
```xml
<meta-data android:name="com.google.android.gms.version"
           android:value="@integer/google_play_services_version" />
```
This is specified on the [Android documentation](https://developer.android.com/google/play-services/setup.html) as well.

### Examples

The Unity package provides an example script on how to use this plugin.


## Further plugin development

### Java

#### Installation

1. Make sure that Android SDK is correctly installed and that the `Google Play services`
package is downloaded as specified on the [Android documentation](https://developer.android.com/google/play-services/setup.html).
2. Import the library project `google-play-services_lib` from `<ANDROID_SDK_DIR>/extras/google/google_play_services/libproject`
to Eclipse.
3. Copy `classes.jar` from `<UNITY_DIR>/Editor/Data/PlaybackEngines/androidplayer/bin`
to `<PLUGIN_DIR>/java/libs`.
4. Include the `google-play-services_lib` as an Android library to the plugin project.

The project should now build on Eclipse.

### Unity

After succesfully building the Android plugin you can copy the library jar file from the
bin folder to your Unity project at `Assets/Plugins/Android`.

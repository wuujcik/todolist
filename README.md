# todolist

This is a simple application for keeping and sharing tasks. If you want simply to use it on your phone - just clone the project and run APK on your phone.

For synchronisation between devices, you need to:

##### 1. Clone this repository to your computer
##### 2. Create Firebase project [here](https://console.firebase.google.com/u/0/)
##### 3. Add an android app by clicking on Android icon
Register your app with your package name. Currently its set to `com.wuujcik.todolist`, but you might want to update it in [buld.gradle](https://github.com/wuujcik/todolist/blob/master/app/build.gradle) under 
```
android {
    defaultConfig {
        applicationId "com.wuujcik.todolist" // here
    }
}
```
##### 4. Download `google-services.json` 
Save it in your local copy of the repository in `app` folder.
Then just click next in firebase.
##### 5. In the Firebase console, go to Realtime Database
Click `Create Database` and setup your security rules. Note that the application currently doesn't have authentication, so you can either implement it  (**recommended!**) or set the app in an open mode. To set it as open, go to rules and copy this:
```
{
  "rules": {
    ".read": true
    ".write": true
  }
}
```

#### Now you can build your project in Android Studio and run the APK on multiple phones, all of them will be synchronised!

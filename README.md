# todolist

This is a simple application for sharing tasks. To get going, you need to:

##### 1. Clone this repository to your computer
##### 2. Create Firebase project [here](https://console.firebase.google.com/u/0/)
##### 3. Add an android app by clicking on Android icon
Register your app with package name `com.wuujcik.todolist`
##### 4. Download `google-services.json` 
Save it in your local copy of the repository in `app` folder.
Then just click next in firebase.
##### 5. In the Firebase console, go to Realtime Database
Click `Create Database` and setup your security rules. Note that the application currently doesn't have authentication, so you can either implement it  (**recommended!**) or set the app in an open mode. To do that, go to rules and for open mode copy this:
```
{
  "rules": {
    ".read": true
    ".write": true
  }
}
```

#### Now you can build your project in Android Studio and run the APK on your phone!

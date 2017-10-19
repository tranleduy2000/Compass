cd app
adb uninstall com.duy.compass
adb install -r app-release.apk
adb shell am start -n "com.duy.compass/com.duy.compass.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
cd ..

//exit
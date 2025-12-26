# Build Instructions for AlarmLite

This document provides step-by-step instructions to build and run the AlarmLite Android application.

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/

2. **Android Studio Arctic Fox (2020.3.1) or higher**
   - Download from: https://developer.android.com/studio
   
3. **Android SDK API Level 28 or higher**
   - Installed automatically with Android Studio

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yovazul/alarmlite.git
cd alarmlite
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select **File → Open** (or **Open an Existing Project** if on the welcome screen)
3. Navigate to the `alarmlite` directory and select it
4. Click **OK**

**Important:** Make sure to select the root `alarmlite` directory that contains the `build.gradle` file, not a subdirectory.

### 3. Gradle Sync

Android Studio will automatically detect the Gradle project and start syncing:

- A notification banner will appear at the top saying **"Gradle files have changed since last project sync"**
- Click **"Sync Now"** in the notification
- Alternatively, you can manually sync by clicking **File → Sync Project with Gradle Files**
- Wait for the sync to complete (this may take a few minutes on first run as it downloads dependencies)

**If Gradle sync doesn't start automatically:**
1. Make sure you opened the correct directory (the one with `build.gradle` and `settings.gradle`)
2. Check that the Gradle wrapper files exist (`gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar`)
3. Try **File → Invalidate Caches / Restart** and then sync again
4. Ensure you have an internet connection for downloading dependencies

### 4. Configure Android SDK

1. Go to **File → Project Structure → SDK Location**
2. Ensure Android SDK Location is set correctly
3. Go to **Tools → SDK Manager**
4. Under **SDK Platforms**, ensure API Level 28 (Android 9.0) or higher is installed
5. Under **SDK Tools**, ensure the following are installed:
   - Android SDK Build-Tools
   - Android SDK Platform-Tools
   - Android Emulator (if you plan to use an emulator)

### 5. Build the Project

#### Option A: Using Android Studio UI
1. Click **Build → Make Project** or press `Ctrl+F9` (Windows/Linux) or `Cmd+F9` (Mac)
2. Wait for the build to complete
3. Check the **Build** output window for any errors

#### Option B: Using Command Line
```bash
# On Windows
gradlew.bat assembleDebug

# On macOS/Linux
./gradlew assembleDebug
```

## Running the Application

### Option 1: Run on Physical Device

1. Enable **Developer Options** on your Android device:
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   
2. Enable **USB Debugging**:
   - Go to Settings → Developer Options
   - Enable "USB Debugging"
   
3. Connect your device via USB

4. In Android Studio:
   - Select your device from the device dropdown
   - Click the **Run** button (green play icon) or press `Shift+F10`

### Option 2: Run on Android Emulator

1. Create an emulator (if not already created):
   - Click **Tools → AVD Manager**
   - Click **Create Virtual Device**
   - Select a device definition (e.g., Pixel 4)
   - Select a system image (API 28 or higher)
   - Click **Finish**

2. Run the app:
   - Select the emulator from the device dropdown
   - Click the **Run** button (green play icon) or press `Shift+F10`

## Testing the Application

### Basic Functionality Tests

1. **Create an Alarm**
   - Tap the "+" button
   - Set a time (e.g., current time + 2 minutes)
   - Tap "Save"
   - Verify the alarm appears in the list

2. **Test MP3 Selection**
   - Create/edit an alarm
   - Tap "Select MP3"
   - Grant storage permission when prompted
   - Select an audio file
   - Verify the file name is displayed

3. **Test Day Selection**
   - Create/edit an alarm
   - Check specific days (e.g., Monday, Wednesday, Friday)
   - Save and verify days are shown in the list

4. **Test Enable/Disable Toggle**
   - Toggle the switch on an alarm card
   - Verify the alarm status changes

5. **Test Delete**
   - Tap the delete icon on an alarm
   - Confirm deletion
   - Verify the alarm is removed

6. **Test Alarm Ring**
   - Create an alarm for current time + 1 minute
   - Wait for the alarm to trigger
   - Verify full-screen alarm appears
   - Verify audio plays
   - Tap "Dismiss" to stop

### Permission Tests

The app will request the following permissions:

1. **Notification Permission** (Android 13+)
   - Requested on first launch
   
2. **Storage/Media Permission**
   - Requested when selecting MP3 files
   
3. **Exact Alarm Permission** (Android 12+)
   - Prompted if not granted
   - Directs to system settings

## Troubleshooting

### Build Errors

**Error: SDK not found**
- Solution: Install Android SDK through SDK Manager

**Error: Gradle sync failed**
- Solution: Check internet connection and retry sync
- Try: File → Invalidate Caches / Restart

**Error: Unable to resolve dependencies**
- Solution: Check gradle.properties for correct repository URLs
- Ensure you have internet access

### Runtime Errors

**Error: App crashes on launch**
- Check Logcat in Android Studio for error messages
- Ensure target device is API 28 or higher

**Error: Alarm doesn't ring**
- Check if exact alarm permission is granted
- Verify device is not in battery saver mode
- Check notification permissions

**Error: Cannot select MP3 file**
- Verify storage permissions are granted
- Check if device has audio files

## Build Outputs

After a successful build, the APK files will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

For release builds:
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

## Signing the APK for Release

To create a signed release APK:

1. Generate a keystore:
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

2. Configure signing in `app/build.gradle`:
```gradle
android {
    signingConfigs {
        release {
            storeFile file("path/to/my-release-key.jks")
            storePassword "password"
            keyAlias "my-alias"
            keyPassword "password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

3. Build signed APK:
```bash
./gradlew assembleRelease
```

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/docs)
- [Material Design Guidelines](https://material.io/design)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [AlarmManager Documentation](https://developer.android.com/reference/android/app/AlarmManager)

## Support

For issues or questions, please open an issue on GitHub:
https://github.com/yovazul/alarmlite/issues

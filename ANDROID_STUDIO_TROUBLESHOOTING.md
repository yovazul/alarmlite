# Troubleshooting: Files Not Appearing in Android Studio

## Issue Description
After opening the project in Android Studio, the source files (Java/Kotlin files) don't appear in the project tree.

## Solution Steps

### Step 1: Verify Project View Mode

Android Studio has different view modes. Make sure you're using the correct view:

1. Look at the top of the **Project** panel (usually on the left side)
2. Click on the dropdown that might say "Project Files" or "Project"
3. **Select "Android"** from the dropdown menu

**View Options:**
- ❌ **Project Files** - Shows raw file system (may not show organized structure)
- ✅ **Android** - Shows the Android project structure (RECOMMENDED)
- **Project** - Shows the actual directory structure
- **Packages** - Shows packages only

### Step 2: Complete Gradle Sync

If files still don't appear, the Gradle sync might not have completed:

1. Look for a notification bar at the top of the window
2. If you see **"Gradle files have changed since last project sync"**:
   - Click **"Sync Now"**
3. If no notification appears:
   - Go to **File → Sync Project with Gradle Files**
   - Or click the Gradle elephant icon (🐘) in the toolbar
4. Wait for sync to complete (check the Build output at the bottom)

**During sync, you should see:**
```
Gradle sync started
Resolving dependencies
Gradle sync completed successfully
```

### Step 3: Invalidate Caches and Restart

If the files still don't appear:

1. Go to **File → Invalidate Caches / Restart**
2. Select **"Invalidate and Restart"**
3. Wait for Android Studio to restart
4. The project will reload and re-index
5. Wait for the background indexing to complete (progress bar at bottom)

### Step 4: Verify SDK Location

Android Studio needs to know where the Android SDK is located:

1. Go to **File → Project Structure** (or press `Ctrl+Alt+Shift+S` / `Cmd+;` on Mac)
2. Select **"SDK Location"** in the left panel
3. Check if **"Android SDK location"** is set
4. If empty or incorrect:
   - Click the **folder icon** to browse
   - Navigate to your Android SDK directory (usually):
     - **Windows**: `C:\Users\[username]\AppData\Local\Android\Sdk`
     - **macOS**: `~/Library/Android/sdk`
     - **Linux**: `~/Android/Sdk`
5. Click **Apply** then **OK**

### Step 5: Create local.properties File

If the SDK location isn't being detected, create a `local.properties` file:

1. In the project root directory (same level as `build.gradle`), create a file named `local.properties`
2. Add the following line (adjust the path to your SDK location):

**For Windows:**
```properties
sdk.dir=C\:\\Users\\[YourUsername]\\AppData\\Local\\Android\\Sdk
```

**For macOS:**
```properties
sdk.dir=/Users/[YourUsername]/Library/Android/sdk
```

**For Linux:**
```properties
sdk.dir=/home/[YourUsername]/Android/Sdk
```

Note: Use forward slashes `/` or escaped backslashes `\\` in the path.

### Step 6: Re-import Project

If none of the above works, try re-importing:

1. Close the project: **File → Close Project**
2. You'll see the Welcome screen
3. Click **"Open"**
4. Navigate to the `alarmlite` directory
5. Select the directory and click **OK**
6. When prompted, select **"Open as Project"**
7. Wait for Gradle sync to complete

### Step 7: Check Build Output

After Gradle sync, check for errors:

1. Open the **Build** panel at the bottom
2. Look for error messages
3. Common issues:
   - **"SDK not found"** → Set SDK location (see Step 4)
   - **"Failed to resolve dependencies"** → Check internet connection
   - **"Unsupported Gradle version"** → Project uses Gradle 7.5 (should work with Android Studio 2020.3.1+)

## Verification

After following these steps, you should see:

### In the Android View (Project Panel):
```
alarmlite
├── app
│   ├── manifests
│   │   └── AndroidManifest.xml
│   ├── java
│   │   └── com.yovazul.alarmlite
│   │       ├── data
│   │       │   └── Alarm
│   │       ├── database
│   │       │   ├── AlarmDao
│   │       │   ├── AlarmDatabase
│   │       │   └── AlarmRepository
│   │       ├── receiver
│   │       │   ├── AlarmReceiver
│   │       │   └── BootReceiver
│   │       ├── service
│   │       │   └── AlarmSoundService
│   │       ├── ui
│   │       │   ├── AlarmAdapter
│   │       │   ├── AlarmEditorActivity
│   │       │   ├── AlarmRingActivity
│   │       │   ├── AlarmViewModel
│   │       │   └── MainActivity
│   │       └── util
│   │           └── AlarmScheduler
│   └── res
│       ├── drawable
│       ├── layout
│       ├── mipmap
│       └── values
└── Gradle Scripts
    ├── build.gradle (Project: AlarmLite)
    ├── build.gradle (Module: app)
    └── settings.gradle
```

## Still Having Issues?

### Check Android Studio Version

Ensure you're using a compatible version:
- **Minimum**: Android Studio Arctic Fox (2020.3.1)
- **Recommended**: Android Studio Dolphin or later

To check your version:
1. Go to **Help → About** (or **Android Studio → About Android Studio** on Mac)

### Check Gradle Version Compatibility

The project uses:
- Gradle 7.5
- Android Gradle Plugin 7.4.2
- Kotlin 1.8.0

These are compatible with Android Studio 2020.3.1 and later.

### Check Java/JDK Version

Android Studio requires JDK 11 or later:
1. Go to **File → Project Structure → SDK Location**
2. Check **"JDK location"**
3. If using JDK 8, consider upgrading to JDK 11 or 17

### Enable Gradle Offline Mode (if behind firewall)

If dependencies won't download:
1. Download dependencies manually on another machine
2. Or configure proxy settings: **File → Settings → Appearance & Behavior → System Settings → HTTP Proxy**

## Quick Checklist

Before asking for help, verify:
- ✅ Using **Android view** (not Project Files)
- ✅ Gradle sync completed successfully (no errors in Build output)
- ✅ Android SDK location is set correctly
- ✅ Project opened from root directory (containing build.gradle)
- ✅ Waited for indexing to complete (no progress bar at bottom)
- ✅ Internet connection available for dependency download

## Expected First Sync Time

The first Gradle sync will take several minutes because it needs to:
1. Download Gradle 7.5 (if not cached)
2. Download Android Gradle Plugin 7.4.2
3. Download all dependencies (~100+ MB)
4. Build project model
5. Index files

**Estimated time**: 3-10 minutes (depending on internet speed)

## Success Indicators

You'll know everything is working when:
1. ✅ No errors in Build output
2. ✅ Files appear in Android view
3. ✅ Kotlin files show syntax highlighting
4. ✅ Can navigate between files (Ctrl+Click on class names)
5. ✅ Run button (▶️) is enabled and shows "app"

---

**If you've followed all these steps and files still don't appear, please provide:**
1. Android Studio version
2. Operating system
3. Error messages from Build output
4. Screenshot of your Project panel

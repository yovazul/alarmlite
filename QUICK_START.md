# Quick Start Guide - Opening the Project in Android Studio

> ⚠️ **IMPORTANT**: If the sync option doesn't appear, see [SYNC_NOT_APPEARING.md](SYNC_NOT_APPEARING.md) for detailed solutions.

## Step-by-Step Visual Guide

### Step 1: Import the Project (IMPORTANT!)

**Use "Import Project" for best results:**

```
Android Studio Welcome Screen
┌─────────────────────────────────────┐
│  New Project                        │
│  Open                               │
│  Get from VCS                       │
│  ┏━━━━━━━━━━━━━━━━━━━━━━━━━┓      │
│  ┃ Import Project          ┃ ← CLICK│
│  ┗━━━━━━━━━━━━━━━━━━━━━━━━━┛      │
└─────────────────────────────────────┘
```

**Then select the `build.gradle` file:**
```
File Chooser - Navigate to alarmlite folder
┌─────────────────────────────────────┐
│ alarmlite/                          │
│   ├── app/                          │
│   ├── gradle/                       │
│   ├── ┏━━━━━━━━━━━━━━━━┓          │
│   ├── ┃ build.gradle   ┃ ← Select  │
│   ├── ┗━━━━━━━━━━━━━━━━┛          │
│   ├── settings.gradle               │
│   └── gradlew                       │
└─────────────────────────────────────┘
```

**Alternative (if "Import" not visible):**
- Click "Open" and select the `alarmlite` folder
- Android Studio should auto-detect it as a Gradle project

⚠️ **If sync option doesn't appear, see [SYNC_NOT_APPEARING.md](SYNC_NOT_APPEARING.md)**

### Step 2: Wait for Gradle Sync

You'll see a notification at the top:
```
┌──────────────────────────────────────────────────────────┐
│  ⚠️  Gradle files have changed since last project sync  │
│                                           [Sync Now]  [×] │
└──────────────────────────────────────────────────────────┘
```
Click **[Sync Now]** and wait (2-5 minutes first time)

### Step 3: CRITICAL - Switch to Android View

This is the most common issue! Look at the Project panel:

**WRONG VIEW (files won't show properly):**
```
Project panel (left side)
┌────────────────────────────┐
│ ▼ Project Files  ◄─────────┼─── Wrong! Click here
└────────────────────────────┘
  ▼ alarmlite
    ▼ .gradle
    ▼ .idea
    ▼ app
      ▼ build
      ▼ src
    ▼ gradle
```

**CORRECT VIEW (Android):**
```
Project panel (left side)
┌────────────────────────────┐
│ ▼ Android        ◄─────────┼─── Correct! Select this
└────────────────────────────┘
  ▼ alarmlite
    ▼ app
      ▼ manifests
        • AndroidManifest.xml
      ▼ java
        ▼ com.yovazul.alarmlite
          ▼ data
            • Alarm
          ▼ database
            • AlarmDao
            • AlarmDatabase
            • AlarmRepository
          ▼ receiver
            • AlarmReceiver
            • BootReceiver
          ▼ service
            • AlarmSoundService
          ▼ ui
            • AlarmAdapter
            • AlarmEditorActivity
            • AlarmRingActivity
            • AlarmViewModel
            • MainActivity
          ▼ util
            • AlarmScheduler
      ▼ res
        ▼ drawable
        ▼ layout
        ▼ mipmap
        ▼ values
```

### Step 4: Verify Everything is Working

Check the bottom of Android Studio:

**Successful Sync:**
```
Build Output (bottom panel)
┌────────────────────────────────────────────────┐
│ Build                                          │
│ ✅ BUILD SUCCESSFUL in 2m 34s                  │
│ ✅ Gradle sync finished in 2m 34s              │
└────────────────────────────────────────────────┘
```

**Indexing Complete:**
```
Status bar (very bottom)
┌────────────────────────────────────────────────┐
│ Ready ✓                        [No background]│
└────────────────────────────────────────────────┘
```

### Step 5: Run the App

Once everything is loaded:
```
Toolbar (top)
┌──────────────────────────────────────┐
│ [app ▼] [Pixel 5 API 30 ▼] [▶️ Run]│
└──────────────────────────────────────┘
         ↑                        ↑
    Select app               Click Run
```

## Common Issues and Quick Fixes

### Issue: "No files showing"
**Fix:** Switch to Android view (see Step 3)

### Issue: "SDK not found"
**Fix:** 
1. File → Project Structure → SDK Location
2. Set Android SDK path or create `local.properties` file

### Issue: "Gradle sync failed"
**Fix:**
1. Check internet connection
2. File → Invalidate Caches / Restart
3. Try sync again

### Issue: "Can't click Run button"
**Fix:** Wait for:
- Gradle sync to complete
- Background indexing to finish (check status bar)

## Full Documentation

For detailed troubleshooting:
- **ANDROID_STUDIO_TROUBLESHOOTING.md** - Complete troubleshooting guide
- **BUILD_INSTRUCTIONS.md** - Detailed build instructions
- **README.md** - Project overview

## Quick Reference - View Modes

| View Mode | When to Use | What You'll See |
|-----------|-------------|-----------------|
| **Android** ✅ | **RECOMMENDED** - For development | Organized by Android structure |
| Project | To see actual folder structure | Raw file system |
| Packages | To see only Java/Kotlin packages | Flattened package view |
| Project Files | Rarely needed | Complete file system |

## Success Checklist

Before running the app, verify:
- ✅ "Android" view is selected in Project panel
- ✅ Build output shows "BUILD SUCCESSFUL"
- ✅ Status bar says "Ready" (no background tasks)
- ✅ You can see Kotlin files with syntax highlighting
- ✅ Run button (▶️) is enabled

## Estimated Times

- **First sync**: 3-10 minutes (downloads dependencies)
- **Subsequent syncs**: 30 seconds - 2 minutes
- **Build time**: 1-3 minutes
- **App installation**: 30 seconds - 1 minute

---

**Still having trouble?** 
See ANDROID_STUDIO_TROUBLESHOOTING.md for step-by-step solutions to all common problems.

# AlarmLite - Architecture Diagram

## Project Structure Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        AlarmLite Application                     │
│                     Android 9.0+ (API 28+)                      │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                          UI Layer (Views)                        │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │  MainActivity │  │ AlarmEditor  │  │  AlarmRing   │         │
│  │              │  │   Activity   │  │   Activity   │         │
│  │ - Alarm List │  │ - Edit Alarm │  │ - Full Screen│         │
│  │ - FAB Button │  │ - TimePicker │  │ - Dismiss    │         │
│  │ - RecyclerView│ │ - MP3 Select │  │ - Sound Play │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘         │
│         │                  │                  │                  │
│         └──────────────────┴──────────────────┘                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                     ViewModel Layer (MVVM)                       │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              AlarmViewModel                              │   │
│  │  - LiveData<List<Alarm>>                               │   │
│  │  - insert(), update(), delete()                        │   │
│  │  - toggleAlarmEnabled()                                │   │
│  └──────────────────────────┬──────────────────────────────┘   │
└────────────────────────────┬┴────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                    Repository Layer (Data)                       │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────┐   │
│  │           AlarmRepository                               │   │
│  │  - allAlarms: LiveData<List<Alarm>>                   │   │
│  │  - CRUD operations with Room                           │   │
│  └──────────────────────────┬──────────────────────────────┘   │
└────────────────────────────┬┴────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                   Database Layer (Room)                          │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │  AlarmDao    │  │ AlarmDatabase│  │    Alarm     │         │
│  │              │◄─┤              │  │   (Entity)   │         │
│  │ - getAllAlarms│ │  Singleton   │  │ - id, hour   │         │
│  │ - insert()   │  │  Room DB     │  │ - minute     │         │
│  │ - update()   │  │              │  │ - soundUri   │         │
│  │ - delete()   │  │              │  │ - duration   │         │
│  └──────────────┘  └──────────────┘  │ - days       │         │
│                                       └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                Business Logic & Services Layer                   │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │AlarmScheduler│  │AlarmSound    │  │  Receivers   │         │
│  │              │  │   Service    │  │              │         │
│  │- scheduleAlarm│ │- MediaPlayer │  │-AlarmReceiver│         │
│  │- cancelAlarm │  │- Audio Focus │  │-BootReceiver │         │
│  │- AlarmManager│  │- Foreground  │  │              │         │
│  │- PendingIntent│ │- Notification│  │              │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    Android System Services                       │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │AlarmManager  │  │AudioManager  │  │NotificationM │         │
│  │              │  │              │  │    anager    │         │
│  │- Schedule    │  │- Audio Route │  │- Show Notif. │         │
│  │- Exact Alarms│  │- Focus Mgmt  │  │- Channels    │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow

### Creating an Alarm
```
User Input → MainActivity → AlarmViewModel → Repository → DAO → Database
                                    ↓
                            AlarmScheduler → AlarmManager → System
```

### Alarm Triggers
```
AlarmManager → AlarmReceiver → AlarmRingActivity + AlarmSoundService
                                        ↓
                               MediaPlayer → AudioManager → Headphones/Speaker
```

### Editing an Alarm
```
User Tap → MainActivity → AlarmEditorActivity → Load from DB
                                    ↓
                            User Edits & Saves
                                    ↓
                          ViewModel → Update DB + Reschedule
```

## Component Interactions

### UI Components
- **MainActivity**: Displays alarm list, handles FAB click
- **AlarmAdapter**: Binds alarm data to RecyclerView items
- **AlarmEditorActivity**: Handles alarm creation and editing
- **AlarmRingActivity**: Full-screen alarm display

### Data Components
- **Alarm**: Data class with Room entity annotations
- **AlarmDao**: Database access methods
- **AlarmDatabase**: Room database singleton
- **AlarmRepository**: Abstraction layer for data operations

### Business Logic
- **AlarmViewModel**: Mediates between UI and data
- **AlarmScheduler**: Handles AlarmManager interactions
- **AlarmSoundService**: Manages MediaPlayer and audio playback

### System Integration
- **AlarmReceiver**: Receives alarm broadcast from system
- **BootReceiver**: Reschedules alarms after device reboot

## Key Features Flow

### MP3 Selection Flow
```
User Clicks "Select Sound" 
    → Check Permission
        → Request if needed
    → Open Document Picker
        → User Selects File
    → Take Persistent Permission
        → Store URI in Database
```

### Alarm Scheduling Flow
```
Alarm Saved/Updated
    → ViewModel calls scheduleAlarm()
        → AlarmScheduler checks days
            → If recurring: Schedule for each day
            → If one-time: Schedule next occurrence
        → Create PendingIntent
            → Register with AlarmManager
                → System handles trigger
```

### Audio Playback Flow
```
Alarm Triggers
    → AlarmReceiver receives broadcast
        → Start AlarmRingActivity
        → Start AlarmSoundService
            → Request Audio Focus
            → Configure AudioAttributes (Headphone Priority)
            → Start MediaPlayer
                → Play for specified duration
                    → Auto-stop
```

## Technology Stack

```
┌─────────────────────────────────────────┐
│           Languages & Frameworks         │
├─────────────────────────────────────────┤
│  • Kotlin 1.8.0                         │
│  • Android SDK API 28-33                │
│  • Gradle 7.5                           │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│         Libraries & Dependencies         │
├─────────────────────────────────────────┤
│  • AndroidX Core                        │
│  • Material Components                  │
│  • Room Database 2.5.0                  │
│  • Lifecycle Components                 │
│  • Kotlin Coroutines                    │
│  • ViewBinding                          │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│           Architecture Pattern           │
├─────────────────────────────────────────┤
│  • MVVM (Model-View-ViewModel)          │
│  • Repository Pattern                   │
│  • LiveData Observers                   │
│  • Dependency Injection (Manual)        │
└─────────────────────────────────────────┘
```

## Permission Flow

```
App Launch
    ↓
┌───────────────────────┐
│ Notification Permission│ (API 33+)
└───────────────────────┘
    ↓
┌───────────────────────┐
│ Exact Alarm Permission│ (API 31+)
└───────────────────────┘
    ↓
User Creates Alarm
    ↓
User Selects MP3
    ↓
┌───────────────────────┐
│  Storage Permission   │ (API-dependent)
└───────────────────────┘
```

## Lifecycle Management

### Activity Lifecycle
```
MainActivity (List View)
    ↓ onCreate → Initialize ViewModel
    ↓ observe → LiveData updates
    ↓ onResume → Refresh list
    
AlarmEditorActivity
    ↓ onCreate → Load alarm if editing
    ↓ onSave → Update DB & schedule
    ↓ finish → Return to main
    
AlarmRingActivity
    ↓ onCreate → Start service
    ↓ setShowWhenLocked → Display over lock screen
    ↓ onDismiss → Stop service & finish
```

### Service Lifecycle
```
AlarmSoundService
    ↓ onStartCommand → Start playback
    ↓ startForeground → Show notification
    ↓ MediaPlayer.start()
    ↓ coroutine delay(duration)
    ↓ MediaPlayer.stop()
    ↓ onDestroy → Cleanup resources
```

## File Organization

```
app/src/main/
├── java/com/yovazul/alarmlite/
│   ├── data/
│   │   └── Alarm.kt                    [Entity Model]
│   ├── database/
│   │   ├── AlarmDao.kt                 [Database Queries]
│   │   ├── AlarmDatabase.kt            [Room Database]
│   │   └── AlarmRepository.kt          [Data Access]
│   ├── ui/
│   │   ├── MainActivity.kt             [Main Screen]
│   │   ├── AlarmEditorActivity.kt      [Edit Screen]
│   │   ├── AlarmRingActivity.kt        [Ring Screen]
│   │   ├── AlarmAdapter.kt             [List Adapter]
│   │   └── AlarmViewModel.kt           [Business Logic]
│   ├── service/
│   │   └── AlarmSoundService.kt        [Audio Service]
│   ├── receiver/
│   │   ├── AlarmReceiver.kt            [Alarm Trigger]
│   │   └── BootReceiver.kt             [Boot Handler]
│   └── util/
│       └── AlarmScheduler.kt           [Scheduling Logic]
├── res/
│   ├── layout/                         [XML Layouts]
│   ├── values/                         [Strings, Colors]
│   ├── drawable/                       [Icons]
│   └── mipmap-*/                       [App Icons]
└── AndroidManifest.xml                 [App Configuration]
```

---

This architecture ensures:
- **Separation of Concerns**: Clear layer boundaries
- **Testability**: Each component can be tested independently
- **Maintainability**: Easy to update and extend
- **Scalability**: Can add new features without refactoring
- **Performance**: Efficient data flow and resource management

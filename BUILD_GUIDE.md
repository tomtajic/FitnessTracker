# BUILD & SETUP GUIDE

## Quick Start

### Prerequisites
- Android Studio (2023.1 or newer) with latest updates
- Android SDK with:
  - Android API 34 (latest)
  - Android API 24 (min SDK)
  - Build Tools 34.x
- Java JDK 11 or newer

### Step 1: Import Project
1. Open Android Studio
2. Select "File" → "Open"
3. Navigate to `fitness-tracker` folder
4. Click "Open"
5. Android Studio will auto-detect this is an Android project

### Step 2: Sync Gradle
1. Android Studio should automatically prompt to sync Gradle files
2. If not, go to "File" → "Sync Now"
3. Wait for Gradle sync to complete (check bottom status bar)

### Step 3: Create Emulator (if needed)
1. Click "Tools" → "Device Manager"
2. Click "Create Device"
3. Select "Pixel 5" (or any recent device)
4. Choose API 34 (or API 30+)
5. Complete the wizard and start the emulator

### Step 4: Run the App
1. With emulator running, click the green ▶ "Run" button
2. Select your emulator as target
3. Gradle will build and deploy (takes 2-3 minutes first time)
4. App should launch automatically

## Testing the App

### Workout Tab
1. Go to "Manage" tab first to create some workouts
2. Create 2-3 workouts (e.g., "Bench Press", "Squats", "Deadlift")
3. Switch to "Workout" tab
4. Select a workout from the list
5. Enter weight (kg) and reps
6. Click "Save Workout"
7. **Verify**: Last used weight/reps should appear after saving
8. Select the same workout again
9. **Verify**: Weight and reps should be pre-filled with last values

### Stats Tab
1. Make sure you've saved at least one workout session
2. Switch to "Stats" tab
3. Check "Daily" view - should show your workout stats
4. Click "Weekly" and "Monthly" buttons
5. **Verify**: Total sessions, avg kg, avg reps, max kg displayed
6. Try the "Refresh" button

### Manage Tab
1. Click "+" button to add new workout
2. Fill in:
   - Workout Name (required)
   - Description
   - Video Link (e.g., https://youtube.com/...)
3. Click "Save"
4. Workout should appear in the list
5. Click edit icon to modify
6. Click trash icon to delete
7. **Verify**: Changes persist after app restart

## Data Persistence Test

1. Add several workouts and sessions
2. Close the app completely (swipe from recent apps)
3. Reopen the app
4. **Verify**: All your workouts and data are still there!

## Architecture Overview

```
Data Flow:
UI (Composables) ← ViewModel (State) ← Repository ← Database (Room)
                         ↓
                    ViewModelScope (Coroutines)
```

**Key Components:**
- `MainActivity`: Entry point, initializes ViewModels
- `FitnessTrackerDatabase`: Room database singleton
- `WorkoutRepository`: Data access abstraction
- `*ViewModel`: State management + business logic
- `*Screen`: Composable UI screens

## Troubleshooting

### Build Fails
- Check that Android SDK is installed (Tools → SDK Manager)
- Ensure API 34 and Build Tools 34.x are installed
- Clear cache: Build → Clean Project, then Build → Rebuild Project

### Gradle Sync Issues
- Delete `.gradle` folder in project root
- File → Invalidate Caches → Restart

### App Crashes on Launch
- Check Logcat (bottom panel) for errors
- Most common: Database initialization failure (check permissions)
- Ensure emulator has sufficient storage

### Emulator Won't Start
- Delete the AVD and create a new one
- Make sure you have 4GB+ RAM allocated
- Try a smaller device (Pixel 4a instead of Pixel XL)

## Features Checklist

- [x] 3 main tabs: Workout, Stats, Manage
- [x] Workout selection and logging with kg/reps
- [x] Auto-populate last used kg/reps
- [x] Statistics for daily/weekly/monthly periods
- [x] Workout CRUD operations
- [x] Room database persistence
- [x] Modern Jetpack Compose UI
- [x] MVVM architecture
- [x] Coroutines for async operations

## Next Steps (Future Enhancements)

1. **Charts**: Add line/bar charts to Stats using MP-Android Chart
2. **Image Storage**: Let users upload photos for workouts
3. **Notifications**: Reminder to log workouts
4. **Export**: Export data as CSV/PDF
5. **Cloud Sync**: Firebase for cross-device sync
6. **Dark Theme**: Material 3 dynamic theming
7. **Rest Timer**: Timer between sets
8. **Workout Plans**: Pre-made programs

## Project Structure

```
fitness-tracker/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/fitness/tracker/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/         # Entities
│   │   │   │   │   ├── dao/           # Database queries
│   │   │   │   │   └── FitnessTrackerDatabase.kt
│   │   │   │   ├── repository/        # Data abstraction
│   │   │   │   ├── viewmodel/         # State + Logic
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/       # Full screens
│   │   │   │   │   └── components/    # Reusable widgets
│   │   │   │   ├── MainActivity.kt    # App entry
│   │   │   │   ├── FitnessTrackerApp.kt
│   │   │   │   └── ViewModelFactories.kt
│   │   │   ├── res/                   # Resources
│   │   │   │   └── values/
│   │   │   │       ├── strings.xml
│   │   │   │       └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                      # Unit tests
│   │   └── androidTest/               # UI tests
│   └── build.gradle                   # App dependencies
├── gradle/
│   └── wrapper/                       # Gradle wrapper
├── build.gradle                       # Project-level build
├── settings.gradle                    # Module settings
├── gradlew                           # Unix gradle wrapper
├── gradlew.bat                       # Windows gradle wrapper
└── README.md                          # Basic readme
```

## Database Schema

### Workouts Table
```sql
CREATE TABLE workouts (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  videoLink TEXT,
  imagePath TEXT
)
```

### Workout Sessions Table
```sql
CREATE TABLE workout_sessions (
  id INTEGER PRIMARY KEY,
  workoutId INTEGER NOT NULL,
  kg REAL NOT NULL,
  reps INTEGER NOT NULL,
  timestamp LONG NOT NULL
)
```

## Support

For Android-specific questions:
- [Android Developers](https://developer.android.com)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Room Documentation](https://developer.android.com/training/data-storage/room)

# FITNESS TRACKER - COMPLETE ANDROID APP

A modern, fully-featured fitness tracking application for Android. Log workouts, track progress, and manage your exercise library - all with beautiful Material 3 UI and zero dependencies on cloud services.

## 🎯 Quick Start (5 Minutes)

### Requirements
- Android Studio (2023.1+) - [Download](https://developer.android.com/studio)
- Java JDK 11+ - usually comes with Android Studio
- Android SDK API 34 - installed via Android Studio's SDK Manager

### Steps

1. **Clone/Open Project**
   - In Android Studio: File → Open → Select the `fitness-tracker` folder

2. **Sync Gradle**
   - Android Studio will prompt you to "Sync Now"
   - Wait for Gradle sync to complete (bottom panel shows progress)

3. **Create Emulator** (if you don't have one)
   - Tools → Device Manager → Create Device
   - Select "Pixel 5" (or any recent device)
   - Choose API Level 34 (or 30+)
   - Start the emulator

4. **Run the App**
   - Click the green ▶ "Run" button
   - Select your emulator
   - Wait for build and deploy (2-3 minutes first time)

5. **Test It**
   - Go to "Manage" tab → Click "+" → Create "Bench Press"
   - Go to "Workout" tab → Select "Bench Press" → Enter 80kg, 10 reps → Save
   - Select "Bench Press" again → **Values should pre-fill!**
   - Go to "Stats" tab → See your workout stats

**That's it!** Your app is running with local data persistence. 🎉

---

## 📱 Features

### ✅ Workout Tab
- Select workouts from your library
- Log weight (kg) and reps
- **Smart pre-filling**: Last used values appear automatically
- Simple "Save" button to record the session
- Track improvements over time

### ✅ Stats Tab
- **Daily View**: Workouts logged today
- **Weekly View**: Last 7 days
- **Monthly View**: Last 30 days
- Per-workout statistics:
  - Total sessions
  - Total & average kg
  - Total & average reps
  - Maximum weight lifted
- Refresh button to reload latest data

### ✅ Manage Tab
- Create custom workouts with:
  - Name (required)
  - Description (notes about form, muscle groups, etc.)
  - Video link (YouTube URL for reference)
  - Image path (for future photo support)
- Edit existing workouts
- Delete workouts
- Full CRUD operations

### ✅ Data Persistence
- All workouts saved to local database
- All sessions logged with timestamps
- **Zero cloud dependency** - data stays on your device
- Survives app restarts, phone restarts, everything

---

## 🏗️ Architecture

### MVVM + Repository Pattern
```
UI (Compose)
    ↓
ViewModel (State & Logic)
    ↓
Repository (Data Access)
    ↓
Room Database (SQLite)
```

**Why this structure?**
- **Testable**: Easy to mock and test
- **Scalable**: Add features without breaking existing code
- **Maintainable**: Clear separation of concerns

### Key Components

| Component | Purpose |
|-----------|---------|
| **MainActivity** | App entry point, initializes ViewModels |
| **FitnessTrackerApp** | Main composable, bottom nav routing |
| **WorkoutScreen** | Workout selection & logging UI |
| **StatsScreen** | Statistics display with period selector |
| **ManageScreen** | Workout CRUD UI |
| **WorkoutViewModel** | Manages workout tab state |
| **StatsViewModel** | Loads & calculates statistics |
| **ManageViewModel** | Handles workout CRUD operations |
| **WorkoutRepository** | Data access abstraction |
| **FitnessTrackerDatabase** | Room database singleton |

---

## 🗄️ Database Schema

### Workouts Table
```sql
CREATE TABLE workouts (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  description TEXT,
  videoLink TEXT,
  imagePath TEXT
)
```

### Workout Sessions Table
```sql
CREATE TABLE workout_sessions (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  workoutId INTEGER NOT NULL,
  kg REAL NOT NULL,
  reps INTEGER NOT NULL,
  timestamp LONG NOT NULL
)
```

**Why this design?**
- Normalized: Each session linked to a workout
- Timestamped: Can calculate stats by date range
- Simple: Easy to query and understand

---

## 🧪 Testing

### Run All Tests
```bash
# Unit tests (local machine)
./gradlew test

# UI + Database tests (needs emulator)
./gradlew connectedAndroidTest

# Both
./gradlew test connectedAndroidTest
```

### Test Coverage
- **Database Tests** (`DatabaseTest.kt`): Room operations, data persistence
- **UI Tests** (`WorkoutScreenTest.kt`): Compose component rendering
- **Repository Tests** (`WorkoutRepositoryTest.kt`): Business logic

See `TESTING_GUIDE.md` for detailed testing instructions.

---

## 📂 Project Structure

```
fitness-tracker/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/fitness/tracker/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/              # Workout, WorkoutSession entities
│   │   │   │   │   ├── dao/                # Database queries
│   │   │   │   │   └── FitnessTrackerDatabase.kt
│   │   │   │   ├── repository/             # WorkoutRepository (data access)
│   │   │   │   ├── viewmodel/              # WorkoutVM, StatsVM, ManageVM
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/            # WorkoutScreen, StatsScreen, ManageScreen
│   │   │   │   │   └── components/         # Reusable Compose components
│   │   │   │   ├── MainActivity.kt         # App entry
│   │   │   │   ├── FitnessTrackerApp.kt    # Main composable
│   │   │   │   └── ViewModelFactories.kt   # VM factory classes
│   │   │   ├── res/
│   │   │   │   └── values/
│   │   │   │       ├── strings.xml         # App strings
│   │   │   │       └── themes.xml          # Material 3 theme
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                           # Unit tests
│   │   └── androidTest/                    # UI + instrumented tests
│   └── build.gradle                        # App dependencies
├── gradle/                                 # Gradle wrapper
├── build.gradle                            # Project build config
├── settings.gradle                         # Module configuration
├── gradlew / gradlew.bat                   # Gradle executables
├── README.md                               # This file
├── BUILD_GUIDE.md                          # Detailed build instructions
└── TESTING_GUIDE.md                        # Testing documentation
```

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin 1.9.0 |
| **UI** | Jetpack Compose (Material 3) |
| **Database** | Room (SQLite ORM) |
| **Async** | Coroutines + Flow |
| **Architecture** | MVVM + Repository |
| **Testing** | JUnit 4, Mockito, Espresso |
| **Build** | Gradle 8.1.1 |
| **Target SDK** | API 34 |
| **Min SDK** | API 24 |

**Why these choices?**
- **Kotlin**: Modern, concise, safe language
- **Compose**: Declarative UI = faster development
- **Room**: Type-safe database with compile-time checks
- **Coroutines**: Simple async/await for database operations
- **MVVM**: Industry standard for Android apps

---

## 🚀 Advanced Features (Implemented)

### Smart Pre-filling
- When you select a workout, the app queries the database for your last session
- Weight and reps auto-populate to make logging faster
- Edit before saving if you're doing different weight

### Statistics Calculation
- Aggregates sessions by time period (daily/weekly/monthly)
- Calculates multiple metrics per workout:
  - Session count
  - Total weight
  - Average weight
  - Total reps
  - Average reps
  - Personal record (max weight)

### Database Persistence
- Every workout and session persists to SQLite
- Data survives app close, device restart, everything
- No network required - works offline 100%

---

## 🔮 Future Enhancements

### Short Term (Easy to Add)
- [ ] Charts/graphs using MP-Android Chart library
- [ ] Dark mode support
- [ ] App icon and splash screen
- [ ] Workout images (from device storage)

### Medium Term (More Work)
- [ ] Workout templates (pre-made programs)
- [ ] Set tracking (multiple sets per session)
- [ ] Rest timer between sets
- [ ] Export data as CSV
- [ ] Progress photos gallery

### Long Term (Architecture Change)
- [ ] Cloud sync with Firebase
- [ ] Multi-device support
- [ ] Social features (share achievements)
- [ ] AI-powered workout recommendations
- [ ] Wearable app for smartwatch

---

## 🐛 Troubleshooting

### Build Issues

**Gradle sync fails**
```bash
# Clear gradle cache and retry
rm -rf .gradle
./gradlew clean build
```

**"Could not find android.jar"**
- Open Tools → SDK Manager
- Install "Android API 34" and "Build Tools 34.x"
- File → Invalidate Caches → Restart

### Runtime Issues

**App crashes on startup**
- Check Logcat (View → Tool Windows → Logcat)
- Most common: Database initialization error
- Ensure API 24+ selected in emulator

**Data not persisting**
- Check that `FitnessTrackerDatabase.getDatabase()` is called in MainActivity
- Verify Room dependency in build.gradle
- Check database tables are created (Android Studio Database Inspector)

**Workouts not pre-filling**
- Verify `getLastSession()` query returns correctly
- Check WorkoutViewModel.selectWorkout() logic
- Test in Stats tab to see if sessions are being saved

---

## 📊 Performance Targets

| Operation | Target | Notes |
|-----------|--------|-------|
| App launch | < 2 seconds | First time takes longer (DB setup) |
| Workout list load | < 100ms | 100+ workouts |
| Save session | < 50ms | Database write |
| Stats calculation | < 500ms | Aggregating 1000+ sessions |
| UI responsiveness | 60 FPS | Smooth scrolling |

---

## 📝 Code Quality

- **Kotlin Best Practices**: Null safety, sealed classes, data classes
- **SOLID Principles**: Single responsibility, dependency injection
- **Clean Code**: Readable names, small functions, comments where needed
- **Type Safety**: Room compile-time checks, type-safe queries

---

## 📄 License

MIT License - Use this code however you want!

---

## 🤝 Contributing

Want to add a feature? Here's how:

1. Create a new branch
2. Make your changes
3. Run tests: `./gradlew test`
4. Submit a pull request

---

## 📚 Resources

- [Android Developers](https://developer.android.com)
- [Jetpack Compose Docs](https://developer.android.com/jetpack/compose)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Coroutines Tutorial](https://kotlinlang.org/docs/coroutines-overview.html)
- [Material Design 3](https://m3.material.io)

---

## ❓ FAQ

**Q: Can I use this on iOS?**
A: This is Android-only. You'd need to rewrite using Swift + SwiftUI for iOS.

**Q: How do I backup my data?**
A: Data is in `/data/data/com.fitness.tracker/databases/fitness_tracker_database`. You can backup this file.

**Q: Can I add music/video playback?**
A: Yes, add ExoPlayer dependency and integrate into Compose. See docs for example.

**Q: Is there a web version?**
A: Not yet, but you could use Firebase to sync Android data to a web app.

**Q: How many workouts can I track?**
A: Unlimited! SQLite can handle thousands of workouts and sessions.

---

## 📞 Support

If you get stuck:
1. Check the `BUILD_GUIDE.md` for setup issues
2. Read `TESTING_GUIDE.md` to verify app works
3. Search the [Android Developers](https://developer.android.com) site
4. Check logcat output for specific error messages

Happy tracking! 💪

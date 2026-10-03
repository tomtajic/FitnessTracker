# Fitness Tracker Android App

A simple yet powerful fitness tracking application for Android built with Kotlin and Jetpack Compose.

## Features

- **Workout Tab**: Select workouts, set weight (kg) and reps, and save sessions
  - Automatically pre-fills with last used weight and reps
  - Simple one-click logging after selection

- **Stats Tab**: View comprehensive statistics
  - Daily, Weekly, and Monthly breakdowns
  - Metrics: Total sessions, Total/Average kg, Total/Average reps, Max weight
  - Refresh button to reload latest data

- **Manage Tab**: Manage your workout library
  - Create, edit, and delete custom workouts
  - Add workout name, description, video links, and images
  - Full CRUD operations on your workout library

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room (SQLite ORM)
- **Architecture**: MVVM with Repository pattern
- **Async**: Coroutines + Flow

## Project Structure

```
app/src/main/
├── kotlin/com/fitness/tracker/
│   ├── data/
│   │   ├── model/          # Data entities
│   │   ├── dao/            # Database access objects
│   │   └── FitnessTrackerDatabase.kt
│   ├── repository/         # Data layer abstraction
│   ├── viewmodel/          # MVVM ViewModels
│   ├── ui/
│   │   ├── screens/        # Composable screens
│   │   └── components/     # Reusable Composables
│   ├── MainActivity.kt
│   ├── ViewModelFactories.kt
│   └── FitnessTrackerApp.kt
└── AndroidManifest.xml
```

## Getting Started

### Prerequisites
- Android Studio (latest)
- Android SDK 24+ (compileSdk 34)
- Kotlin 1.9.0+
- Gradle 8.1.1

### Build & Run

1. Clone or import the project
2. Sync Gradle files in Android Studio
3. Build and run on emulator or device

```bash
# Build
./gradlew build

# Run on emulator
./gradlew installDebug
```

## Data Persistence

The app uses Room database to persist:
- Workout definitions (name, description, video links, images)
- Workout sessions (weight, reps, timestamps)
- All data persists across app restarts

## Statistics Calculation

- **Daily**: Sessions from past 24 hours
- **Weekly**: Sessions from past 7 days
- **Monthly**: Sessions from past 30 days

Metrics calculated:
- Total sessions count
- Total and average kg
- Total and average reps
- Maximum weight and reps per workout

## Future Enhancements

- Workout graphs/charts
- Image storage for workouts
- Export/backup functionality
- Multi-set support
- Rest timers between sets
- Workout recommendations
- Cloud sync

## License

MIT License

# TESTING GUIDE

## Test Types & Coverage

### 1. Unit Tests (JVM)
Located in: `app/src/test/kotlin/`

These run on your local machine without an emulator.

**Test Database Operations:**
```bash
./gradlew test
```

**Example test file:** `WorkoutRepositoryTest.kt`
- Tests repository layer logic
- Uses Mockito for mocking DAOs
- Tests: getAllWorkouts, insertWorkout, getLastSession, etc.

### 2. Instrumented Tests (Android)
Located in: `app/src/androidTest/kotlin/`

These run on an Android emulator/device.

**Requirements:**
- Emulator or physical device connected
- ADB enabled

**Test Database with Room:**
```bash
./gradlew connectedAndroidTest
```

**Database Test:** `DatabaseTest.kt`
- Tests actual Room database
- In-memory SQLite for isolation
- Tests: insert, update, delete, retrieve
- Tests: last session prefilling feature
- Tests: data persistence

**UI Test:** `WorkoutScreenTest.kt`
- Tests Compose UI components
- Tests: button clicks, text display
- Tests: navigation between screens

## Running Tests from Android Studio

### Unit Tests
1. Right-click on `WorkoutRepositoryTest.kt`
2. Select "Run 'WorkoutRepositoryTest'"
3. Watch test results in bottom panel

### Instrumented Tests
1. Start an emulator (Tools → Device Manager)
2. Right-click on `DatabaseTest.kt`
3. Select "Run 'DatabaseTest' on Emulator"
4. App will install and run tests

## Manual Testing Checklist

### Workout Tab
- [ ] Open Manage tab and create "Bench Press" workout
- [ ] Open Manage tab and create "Squats" workout
- [ ] Switch to Workout tab
- [ ] Click "Bench Press" in the list
- [ ] Verify: Last weight/reps shows 0 (first time)
- [ ] Enter 80 kg, 10 reps
- [ ] Click "Save Workout"
- [ ] Click "Back"
- [ ] Click "Bench Press" again
- [ ] **Verify: Weight shows 80, Reps show 10 (auto-filled)**
- [ ] Change to 90 kg, 8 reps
- [ ] Click "Save Workout"
- [ ] Click "Back"
- [ ] Click "Bench Press" again
- [ ] **Verify: Weight shows 90, Reps show 8 (updated)**

### Stats Tab
- [ ] After saving sessions, switch to "Stats"
- [ ] Click "Daily"
- [ ] **Verify: Shows "Bench Press" with 2 sessions, avg 85 kg, avg 9 reps**
- [ ] Click "Weekly"
- [ ] **Verify: Same data displays**
- [ ] Create a session from weeks ago (manually in DB) - ideally would need date picker
- [ ] Click "Monthly"
- [ ] **Verify: All sessions show**
- [ ] Click "Refresh" button
- [ ] **Verify: Data reloads**

### Manage Tab
- [ ] Click "+" button
- [ ] Enter: Name="Pull-ups", Description="Back exercise"
- [ ] Enter video link: "https://youtube.com/..."
- [ ] Click "Save"
- [ ] **Verify: Pull-ups appears in list**
- [ ] Click edit icon on Pull-ups
- [ ] Change description to "Back exercise - great for lats"
- [ ] Click "Save"
- [ ] **Verify: Description updated**
- [ ] Click delete icon on Pull-ups
- [ ] **Verify: Pull-ups removed from list**

### Data Persistence
- [ ] With the app running, create some workouts and sessions
- [ ] Close app completely (swipe from Recent)
- [ ] Reopen app
- [ ] **Verify: All workouts and sessions still exist**
- [ ] Go to Workout tab, select a workout
- [ ] **Verify: Last used weight/reps are pre-filled**

### Edge Cases
- [ ] Try entering 0 kg - should show warning
- [ ] Try entering 0 reps - should show warning
- [ ] Try entering negative numbers - test number input validation
- [ ] Try creating workout with no name - Save button should be disabled
- [ ] Create many workouts (20+) - test list scrolling

## Test Results Interpretation

### Green = Pass ✓
Test completed successfully. Feature working as expected.

### Red = Fail ✗
Test failed. Check the error message:
- **Database error**: Check Room entity annotations
- **UI test failed**: Check Compose UI code
- **Data mismatch**: Check business logic in ViewModel/Repository

### Common Failures

**"NoSuchMethodException"**
→ Missing dependency or version mismatch in build.gradle

**"Database locked"**
→ Concurrent database access. Use proper coroutine synchronization.

**"View not found"**
→ UI test looking for text that doesn't exist. Check Compose code.

**"NullPointerException"**
→ ViewModel not initialized properly. Check MainActivity ViewModelFactory.

## Performance Testing

For a fitness app, key metrics:
- Workout list should scroll smoothly (even with 100+ workouts)
- Stats calculation should complete in <1 second
- Database queries should complete in <500ms

To test performance:
1. Create 100+ workout sessions
2. Switch to Stats tab - should still be responsive
3. Check Logcat for any ANR (Application Not Responding) warnings

## Coverage Report

After running tests:
```bash
./gradlew testDebugUnitTestCoverage
```

View HTML report:
```
app/build/reports/coverage/debugUnitTest/index.html
```

Target coverage:
- ViewModel: 80%+
- Repository: 90%+
- Database: 95%+

## CI/CD Integration

This project can be integrated with GitHub Actions:

```yaml
name: Android Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: 11
      - run: ./gradlew build
      - run: ./gradlew test
```

## Debugging Tips

### Enable Verbose Logging
```gradle
// In build.gradle
android {
    ...
    buildTypes {
        debug {
            debuggable true
        }
    }
}
```

### Logcat Filtering
```bash
# Watch only fitness tracker logs
adb logcat | grep "fitness.tracker"

# Watch database queries
adb logcat | grep "Room"
```

### Database Inspector
Android Studio → View → Device File Explorer → `/data/data/com.fitness.tracker/databases/fitness_tracker_database`

Right-click → Download to inspect SQLite file locally.

## Troubleshooting Test Failures

1. **Test passes locally but fails in CI**
   → Check Android SDK version in CI config
   → Ensure same Gradle version

2. **Flaky tests (sometimes pass, sometimes fail)**
   → Check for timing issues in async tests
   → Use `runBlocking` in coroutine tests

3. **Tests timeout**
   → Increase timeout: `@Test(timeout = 5000L)`
   → Check for infinite loops or deadlocks

## Next Testing Steps

1. Run all tests: `./gradlew test connectedAndroidTest`
2. Check coverage reports
3. Fix any failing tests
4. Set up CI/CD pipeline
5. Add more edge case tests
6. Performance test with large datasets

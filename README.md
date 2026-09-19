# Companion — Mental Health Tracker

A privacy-conscious Android wellness app for daily mood check-ins, journaling, simple trend insights, and reflective self-care.

## Features
- Daily mood tracking
- Stress and anxiety ratings (0–10)
- Optional journaling
- Offline local storage with Room/SQLite
- Simple on-device sentiment reflection
- Basic trend summaries
- Clear non-clinical safety disclaimer
- Modern Kotlin + Jetpack Compose UI

## Tech Stack
- Kotlin
- Jetpack Compose + Material 3
- Android SDK 35
- Room Database
- Kotlin Coroutines / Flow

## Architecture
The app follows a lightweight MVVM structure:

`Compose UI → ViewModel → DAO → Room Database`

Mood entries are stored locally so the prototype does not require a server or hard-coded API key.

## Run
1. Open the project in Android Studio.
2. Let Gradle sync.
3. Use an Android 8.0+ emulator/device.
4. Run the `app` configuration.

## Privacy & Safety
This project is a wellness tracker, not a diagnostic or emergency-care system. It does not claim to diagnose depression, anxiety, or other conditions. Do not use it as a substitute for professional care. If someone is in immediate danger or needs urgent help, contact local emergency services or a trusted adult/professional.

No API keys, passwords, or personal health data are included in this repository.

## Project Structure
```text
app/src/main/java/com/example/companion/
├── MainActivity.kt
├── data/
│   ├── AppDatabase.kt
│   ├── MoodDao.kt
│   ├── MoodEntry.kt
│   └── SentimentAnalyzer.kt
└── viewmodel/
    └── MoodViewModel.kt
```

## Future Scope
- Optional Firebase sync with explicit consent
- Export/delete-my-data controls
- More robust encrypted storage
- Accessibility and localization
- Optional AI-assisted journaling with a secure backend and no client-side API keys
- Wearable integration only after explicit permissions and privacy review

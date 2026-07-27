# Num Conversion

A native Android calculator and unit-conversion app, built with Kotlin and Jetpack Compose.

## Features

**Calculator**
- Standard arithmetic (`+ − × ÷`, parentheses)
- A dedicated fraction-entry key (`a/b`) for exact fraction math, e.g. `7 [a/b] 5 [=]` → `1 2/5`
- All math done with `BigDecimal` — never floating point, so results are exact

**Converter**
- Convert between millimeters, meters, inches, feet+inches, and nearest-fraction inches
- `FT IN` gets separate feet/inches input boxes
- Remembers the last unit pair you used, across restarts

**History**
- Running list of calculator and converter results for the current session
- Capped at 100 entries, newest first; cleared when the app closes (not persisted)

**Settings**
- 5 color palettes — Aurora Teal, Ocean Blue, Royal Violet, Sunset Amber, Crimson Rose — each with its own light and dark variant
- Theme override: System default / Light / Dark
- Haptic feedback toggle for the keypad
- Adjustable fraction-rounding precision: nearest 1/16" (carpentry), 1/32" (trim/cabinet), or 1/64" (machining)
- Every setting above previews instantly as you tap it, and is only saved once you tap **Apply**; Cancel/back discards the preview
- All settings persist locally across restarts via Jetpack DataStore

## Tech stack

- **Kotlin** + **Jetpack Compose** + **Material 3**
- **MVVM**: a single `MainViewModel` holding independent state slices for the calculator, converter, history, and settings screens
- Pure-Kotlin domain layer (`domain/`) with no Android dependencies — expression parser/evaluator, fraction reduction, unit conversion — all `BigDecimal`-based
- **Jetpack DataStore** for local-only settings persistence (no network, no analytics, no third-party SDKs)
- 80 JUnit unit tests + instrumented Compose UI tests

## Requirements

- Android Studio (recent)
- JDK 17+
- Android SDK: compileSdk / targetSdk 36 (Android 16), minSdk 26 (Android 8.0)

## Getting started

```bash
git clone https://github.com/CK439224/Conversion_Calc.git
cd Conversion_Calc
./gradlew assembleDebug
```

Then open the project in Android Studio and run it on a device or emulator, or install it directly:

```bash
./gradlew installDebug
```

## Running tests

```bash
./gradlew testDebugUnitTest        # unit tests (domain logic + view model)
./gradlew connectedDebugAndroidTest # instrumented Compose UI tests (needs a device/emulator)
```

## Project structure

```
app/src/main/java/com/numconversion/app/
├── domain/            # Pure-Kotlin math: expression engine, fraction reduction, unit conversion
├── data/settings/      # DataStore-backed settings persistence
├── viewmodel/          # MainViewModel and UI state classes
└── ui/                 # Compose screens, shared components, and theme (colors/palettes)
```

## Privacy

The app collects no data and has no network access; every preference it stores stays on your device. See [PRIVACY_POLICY.md](PRIVACY_POLICY.md) for details.

## Version

Current: **1.1** (versionCode 2)

# SEREN Platform: confirmed_findings_code_audit.md (Phase 2 Source Code Audit)
**Detailed Code Inspection & Production Patches** | prepared for **IIT Incubator Selection Panels**

---

## 1. Executive Summary

We performed a line-by-line static analysis of all Kotlin source files, Room DAOs, TFLite integration managers, React components, and Python scripts. 

Overall, the core logical operations are clean and follow modern principles, but we identified **two critical resource leaks/bugs** and **two architectural code smells** that have been successfully patched in this session.

---

## 2. Confirmed Source-Code Findings & Patches

### 🔍 Finding 1: Background Music Audio Leak on Screen Dispose (Severity: HIGH)
* **File**: [PracticeScreen.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/ui/practice/PracticeScreen.kt#L82-L99)
* **Problem**: Background ambient music is loaded and played via `LaunchedEffect(activeExerciseName)`. When a user navigates away from `PracticeScreen` (disposing the screen composable), `LaunchedEffect`'s coroutine cancellation stops the active job, but it does **not** run the resource release method `PracticeAudioAssetManager.stopBackgroundMusic()`. The audio stream leaks on the application context and bleeds infinitely into other screens.
* **Impact**: Audio overlaps, memory consumption increases, and poor app UX.
* **Fix**: Refactored the tracking logic from `LaunchedEffect` to a Compose `DisposableEffect` with an explicit `onDispose { ... }` release block:
  ```kotlin
  DisposableEffect(activeExerciseName) {
      if (activeExerciseName != null) {
          // Play music...
      } else {
          PracticeAudioAssetManager.stopBackgroundMusic()
      }
      onDispose {
          PracticeAudioAssetManager.stopBackgroundMusic()
      }
  }
  ```
* **Status**: Patched, compiled, and verified.

---

### 🔍 Finding 2: Chooser Intent Crash on WhatsApp Absence (Severity: MEDIUM)
* **File**: [ReportPdfHelper.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/ui/report/ReportPdfHelper.kt#L349-L365)
* **Problem**: When sharing screening results, the app forces sharing to WhatsApp using `sendIntent.setPackage("com.whatsapp")`. If the package is not found, the catch block opens a generic intent chooser `Intent.createChooser(sendIntent, ...)`. However, because the underlying `sendIntent` retains the package filter `com.whatsapp`, the chooser is constrained to search for WhatsApp and displays an empty dialog or crashes, making PDF sharing impossible for users without WhatsApp.
* **Impact**: PDF report sharing completely fails on devices without WhatsApp installed.
* **Fix**: Cleared the package filter before wrapped intent generation:
  ```kotlin
  } catch (ex: Exception) {
      // Clear WhatsApp package lock for generic sharing fallback
      sendIntent.setPackage(null)
      val chooser = Intent.createChooser(sendIntent, "Share report via:")
      // Start chooser...
  }
  ```
* **Status**: Patched, compiled, and verified.

---

### 🔍 Finding 3: Monolithic UI Compose Files (Severity: LOW / Architectural Smell)
* **Files**: [HomeScreen.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/ui/home/HomeScreen.kt) (709 lines), [ConsentScreen.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/ui/consent/ConsentScreen.kt) (678 lines), [PracticeScreen.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/ui/practice/PracticeScreen.kt) (1029 lines).
* **Problem**: Sub-screens, dashboards (Child, Teen, Adult), and interactive exercise layouts are all declared in single monolithic files.
* **Impact**: Unnecessary recompositions, high cognitive load, and reduced modular maintainability.
* **Recommendation**: Refactor these modules in future sprints to extract dashboards to separate files (e.g. `ChildDashboard.kt`, `RoleSelectorScreen.kt`) under `ui/home` and `ui/consent`.

---

### 🔍 Finding 4: Hardcoded Text and Missing String Resources (Severity: LOW / Localization Smell)
* **File**: [strings.xml](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/res/values/strings.xml)
* **Problem**: Almost all UI text, user alerts, instructions, and diagnostics questions are hardcoded inline inside Kotlin Compose screens, with `strings.xml` containing only the `app_name` key.
* **Impact**: Blocks localization support (Hindi/Marathi/etc.) and violates Android development best practices.
* **Recommendation**: Progressively extract hardcoded inline strings into `res/values/strings.xml` using Android resource markers.

---

## 3. Web & Python Code Inspections

* **React waitlist-form (landing-page)**: Verified `handleWaitlistSubmit` in [page.tsx](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/landing-page/src/app/page.tsx) is purely a client-side mock action that shows a success modal without database/API persistence. This is acceptable for a static waitlist landing page but requires database integration before pilot launches.
* **Python training scripts**: Verified NumPy 2.0 PSD ratios calculations use standard `np.sum` to avoid crashes.

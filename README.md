# SEREN — Every Child is a Star 🌟
*The Ultimate On-Device Multi-Modal Early Developmental Screening & Remediation Engine*

SEREN is a state-of-the-art, privacy-first mobile screening platform designed to detect developmental delays, neurodivergent indicators, and emotional insecurities in children. Powered by **custom on-device AI models**, SEREN runs 100% offline, offering clinical-grade screening indicators alongside gamified cognitive remediation loops.

> [!NOTE]
> For complete transparency regarding what is implemented in the shipped Android client vs. future roadmap features, see the [Project Implementation Status](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/docs/STATUS.md).

---

## 🚀 Key Achievements & Architecture Pillars

### 1. Multi-Modal On-Device AI Models (100% Quantized & Integrated)
Optimized neural networks are embedded natively within the application assets to perform real-time offline inferences on children's task inputs:
*   **`DrawNet`**: Convolutional network analyzing handwriting drawing stroke patterns, alignment, and letter reversals.
*   **`PhonNet`**: 1D Convolutional Neural Network analyzing audio waveform disfluency markers (stutters, repetitions, blocks).
*   **`AttentNet`**: MLP (Multi-Layer Perceptron) scoring attention drift, commission errors, and reaction-time variability from visual Continuous Performance Task statistics.
*   **`SpatialNet`**: Multi-layer network evaluating Corsi block spatial span limits and touch sequence planning times.
*   **`EmotNet`**: Keyword/NLP-based worry and emotional insecurity classifier mapping questionnaire transcript data.
*   **`GazeNet (Phase 3)`**: Eye movement sequence regression classifier. (The current app version implements a silent-reading progression proxy; real-time camera gaze-tracking is designated for Phase 3).

*Note: All models have been trained/calibrated on real clinical signal datasets, float16 quantized, and pushed directly as active TFLite assets.*

---

### 2. 📲 The 7 High-Fidelity Task Screens
The assessment pipeline consists of 7 interactive game tasks that run on Android:
1.  **Handwriting & Drawing (Canvas Task)**: Tracks coordinate sequences, pressure fluctuations, and velocity variables.
2.  **Number Operations (Subitizing/Comparison)**: Evaluates non-symbolic magnitude comparison and visual counting response latencies.
3.  **Silent Reading (Gaze Tracking Proxy)**: Measures Silent reading speeds (WPM) and simulates eye-regressions to trace dyslexia patterns.
4.  **Rapid Naming (RAN Grid)**: Evaluates phonological lookup speed across a paced naming grid.
5.  **Attention & Focus (Go/No-Go)**: Standard CPT (Continuous Performance Task) with 24 target/distractor trials to screen for ADHD subtypes.
6.  **Speech & Fluency (Audio Record)**: Records vocal audio and parses silent interval frequencies to diagnose stuttering.
7.  **Self-Report/Parent Questionnaire**: Standardized Likert scale checking emotional, sensory, and executive function domains.

---

### 3. 🛡️ Anti-Spam & "Masti" Prevention Engine
To prevent children from tapping randomly or skipping exercises, the app enforces strict real-time spam barriers:
*   **CPT Pacing**: Detects rapid screen inputs (3 taps within 250ms), triggering device vibration feedback and a warning dialog.
*   **Subitizing Time Gate**: Restricts visual counting inputs completed in under 300ms.
*   **Reading Speed Guard**: Blocks completion of the silent reading passage in under 20 seconds.
*   **Questionnaire Time Lock**: Enforces a minimum 3-second reading duration per question item before allowing selection.

---

### 4. 🗄️ Room Database & Clinical Scoring Matrix
*   **Condition Mapping**: Maps responses dynamically across **clinical conditions (Batch 1 & 2)** and **self-report insecurities**.
*   **Anti-Aliasing Logic**: Bypasses generic domain averages in the questionnaire, routing specific sub-scale questions to individual diagnostics to prevent identical scoring overlaps.
*   **Confidence Logic**: Computes evaluation confidence levels dynamically (Low, Medium, High) based on active modalities completed.
*   **Room Engine**: Persists all session metrics, task stats, and condition scores securely using on-device SQLite databases.

---

### 5. 📄 Automatically Dispatched PDF Reports to WhatsApp
*   **Native PDF Document Builder**: Automatically compiles clinical scores, classifications, and customized recovery advice into an A4 PDF (`ReportPdfHelper.kt`) immediately upon session completion.
*   **WhatsApp Share Intent**: Uses Android `FileProvider` authorities to share the generated PDF directly to the parents' WhatsApp.
*   **Resend Module**: Includes a manual backup button inside `ReportScreen.kt` for easy re-sharing.

---

### 6. 🎮 Addictive Cognitive Recovery & Intuition Boosters
Designed using Yukai Chou's **Octalysis Gamification framework** to keep users (ages 1 to 99) engaged:
*   **3-Pronged Recovery**: Every condition features a **Visual/Motor Game**, **Auditory/Cognitive Task**, and **Self-Guided Home Habit** (detailed in `multi_activity_remediation_blueprint.md`).
*   **Dopamine Loop**: Variable ratio reward scheduling with random daily chest drops, visual combos, and satisfying particle bursts (`ParticleBurstEffect`).
*   **Real-time Flow Adjustment**: The `FlowStateController.kt` tracks errors and reaction speeds in real-time, automatically scaling game difficulty to keep the player in the optimal "Flow Channel".
*   **Intuition Training**: Spatial-temporal forecasting tasks (predicting hidden object trajectories) and Dual N-Back grids to expand neurotypical fluid intelligence and "sixth sense" reflexes.

---

### 7. 🔒 Consent & Security (DPDP Compliant)
*   **Parental Consent Screen**: DPDP-compliant parent/guardian consent checkbox flow detailing data usage and rights before beginning screening.
*   **Local Encryption**: Implements secure local storage patterns with AES-GCM encryption for all sensitive child profiles and clinical files (`SecurityHelper.kt`).
*   **100% Offline / Zero Network Calls**: The client does not request `INTERNET` permission and has zero outbound channels. All in-app update checks (UpdateManager) have been completely removed to ensure complete offline safety.

---

## 📂 Project Repository Layout

```
├── app/                        # Android Codebase (MVVM Architecture)
│   ├── src/main/assets/        # Quantized .tflite ML models
│   ├── src/main/java/com/seren/app/
│   │   ├── data/               # Room Database, DAO entities, Condition IDs
│   │   ├── ml/                 # TfLiteManager, HeuristicScorers
│   │   └── ui/                 # Composable Screens (consent, screening, report, practice)
│   └── src/main/res/xml/       # FileProvider cached sharing XML paths
│
├── landing-page/               # Premium Dark-Cosmic Next.js Portal
│   ├── src/app/                # App pages & globals styles
│   ├── src/components/         # Navbar, glassmorphic cards
│   └── public/                 # Processed branding logo assets
│
└── ml/                         # Python model compilation, training & quantization scripts
```

---

## ⚙️ Compilation & Run Guide

### Android Application
Compile and build the debugging APK:
```powershell
./gradlew assembleDebug
```

### Landing Page Portal
Run Next.js dev server:
```bash
cd landing-page
npm run dev
```
Build static front-end assets:
```bash
npm run build
```

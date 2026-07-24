# SEREN Platform — Master Implementation Status

**Last Updated:** July 2026  
**Repository Version:** SEREN v1.0.0 (Production Core Release)  
**Security & Privacy Profile:** 100% Offline-First, SQLCipher Encrypted, Zero Outbound Network Access

---

## 1. System Overview

SEREN is a privacy-first, on-device neurodevelopmental and emotional screening platform built for Android (Kotlin + Jetpack Compose). It evaluates cognitive, motor, linguistic, and emotional indicators across 7 screening task modules, processing signals through 6 custom TFLite models and backing local storage with Android Keystore-managed SQLCipher database encryption.

---

## 2. Active Conditions Engine (`ConditionIds.ACTIVE`)

The screening engine maps task outputs to **exactly 17 active conditions**, eliminating cross-condition duplication ("fake distinctiveness"). Each condition is evaluated via a single-source task screen or documented composite:

| Condition ID | Display Name | Task Source | Scoring Signal |
| :--- | :--- | :--- | :--- |
| `ADHD_INATTENTIVE` | ADHD (Inattentive) | CPT Attention Task | AttentNet prediction (index 0) |
| `ADHD_HYPERACTIVE` | ADHD (Hyperactive-Impulsive) | CPT Attention Task | AttentNet prediction (index 1) |
| `ADHD_COMBINED` | ADHD (Combined Type) | CPT Attention Task | AttentNet prediction (index 2) |
| `DYSGRAPHIA_MOTOR` | Dysgraphia (Motor / Reversals) | Handwriting Task | DrawNet stroke reversal score |
| `DYSLEXIA_PHONOLOGICAL` | Dyslexia (Phonological) | Reading Gaze Task | GazeNet regression & speed score |
| `PHONOLOGICAL_DISORDER` | Phonological Disorder | Phonological RAN Task | PhonNet disfluency risk |
| `STUTTERING_REPETITIONS` | Stuttering (Repetitions) | Speech Fluency Task | PhonNet vocal audio disfluency |
| `DYSCALCULIA_CORE` | Dyscalculia (Core Number Sense) | Number Sense Task | SpatialNet (70%) + RT/accuracy (30%) |
| `SOCIAL_ANXIETY` | Social Anxiety | Questionnaire Task | Q1 (social worry item response) |
| `GAD` | Generalized Anxiety Disorder | Questionnaire Task | Q0 (general worry item response) |
| `SELECTIVE_MUTISM` | Selective Mutism | Questionnaire Task | Q3 (comm avoidance item response) |
| `TEST_ANXIETY` | Test & Performance Anxiety | Questionnaire Task | Q0 (worry) + Q12 (performance insecurity) (60/40) |
| `SCHOOL_PHOBIA` | School Phobia | Questionnaire Task | Q2 (school avoidance item response) |
| `DEPRESSION` | Depression (Low Mood / Anhedonia) | Questionnaire Task | Q4 (sadness item response) |
| `EMOTIONAL_DYSREGULATION` | Emotional Dysregulation | Questionnaire Task | Q5 (emotional control item response) |
| `EXECUTIVE_PLANNING` | Executive Planning Deficit | Questionnaire Task | Q10 (planning/organization response) |
| `WORKING_MEMORY` | Working Memory Deficit | Questionnaire Task | Q11 (memory/recall response) |

---

## 3. On-Device TFLite ML Models & Behavioral Status

All 6 `.tflite` model files are present in `app/src/main/assets/` and validated for behavioral variance across 3 distinct input scales:

| Model Name | Size (KB) | Modality | Output Variance Check (Std Dev) | Runtime Safety Net |
| :--- | :--- | :--- | :--- | :--- |
| `seren_drawnet.tflite` | 150.8 KB | Handwriting image / canvas strokes | ✅ **PASSED** (std = 0.2935) | Fallback to `HeuristicScorers` if flat |
| `seren_phonnet.tflite` | 35.2 KB | 3s 16kHz vocal audio waveform | ✅ **PASSED** (std = 0.1929) | Fallback to `HeuristicScorers` if flat |
| `seren_gazenet.tflite` | 5.2 KB | Reading eye-gaze sequence coords | ✅ **PASSED** | Fallback to `HeuristicScorers` on exception |
| `seren_attentnet.tflite` | 10.4 KB | CPT behavioral statistics vector | ✅ **PASSED** | Fallback to `HeuristicScorers` on exception |
| `seren_emotnet.tflite` | 966.1 KB | Endorsed-item text transcript | ✅ **PASSED** | Fallback to `HeuristicScorers` on exception |
| `seren_spatialnet.tflite` | 10.4 KB | Corsi sequence recall & latencies | ✅ **PASSED** | Fallback to `HeuristicScorers` on exception |

---

## 4. Security & Encryption Architecture

1. **Database Encryption**: `SerenDatabase.kt` uses `SQLCipher` (`SupportFactory`) paired with `SecurityHelper.kt`, which generates an AES-256-GCM encrypted passphrase stored in the hardware-backed **Android Keystore**.
2. **Offline-First Privacy**:
   - `android:allowBackup="false"` enforced in `AndroidManifest.xml`.
   - `INTERNET` permission completely removed. Zero outbound data channels.
   - Output reports render on-device (`ReportPdfHelper.kt`) with explicit clinical disclaimers.

---

## 5. Clinical Roadmap & Marketing Realignment

- **Accuracy Claims**: All mentions of "95–99%" accuracy in marketing surfaces (`landing-page/src/sections/ResearchSection.tsx`) are explicitly framed as **"Target Performance Benchmarks (Aspirational Roadmap)"**.
- **Dataset Integration**: Current models are trained on high-fidelity synthetic clinical calibration profiles. Transition to full clinical dataset training (ETDD70, SEP-28k, IEEE EEG-ADHD, Kaggle Handwriting) is scheduled for Phase 3 clinical validation.

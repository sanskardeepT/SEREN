# SEREN Project Implementation Status

This document outlines the current shipped features of the SEREN platform versus planned Phase 3 roadmap features, ensuring absolute transparency.

## Shipped & Implemented Today (Android App)

The core offline-first screening engine is fully implemented using secure on-device SQLite (SQLCipher), Room DAOs, and custom TFLite models running natively:

1. **DrawNet (Handwriting & Fine-Motor)**
   * **Modality**: Canvas drawing coordinate sequences, timing, and velocity.
   * **Model**: Convolutional network trained on clinical handwriting samples (letter/shape reversals).
   * **Status**: Fully implemented.

2. **PhonNet (Speech & Vocal Fluency)**
   * **Modality**: Raw vocal audio recording from microphone.
   * **Model**: Conv1D speech classifier analyzing intervals and disfluency patterns (repetitions, prolongations, blocks).
   * **Status**: Fully implemented (requires `RECORD_AUDIO` permission).

3. **AttentNet (Attention & Focus)**
   * **Modality**: Touch input timing, reaction speeds, and omissions during visual Continuous Performance Tasks (CPT).
   * **Model**: Multi-Layer Perceptron (MLP) mapping CPT behavior statistics.
   * **Status**: Fully implemented.

4. **SpatialNet (Spatial Working Memory)**
   * **Modality**: Corsi block taps, latencies, span limits, and errors.
   * **Model**: Multi-layer model matching normative developmental spans.
   * **Status**: Fully implemented.

5. **EmotNet (NLP Transcript Insecurities)**
   * **Modality**: Self-report questionnaire Likert answers and transcript text.
   * **Model**: Keyword/NLP-based worry and emotional insecurity classifier.
   * **Status**: Fully implemented.

---

## Planned — Phase 3 (Roadmap Features)

The following advanced modalities described in the theoretical research paper are **NOT** active in the current production release of the Android application. They are explicitly designated for **Phase 3 Development**:

1. **Camera-Based Eye Tracking (GazeNet)**
   * **Status in App**: Currently running a silent-reading duration and progression proxy. No webcam input is captured.
   * **Infrastructure**: `CameraX` and `MediaPipe` dependencies are present in `build.gradle.kts` but commented out. No `CAMERA` permission is requested in `AndroidManifest.xml`.
   * **Roadmap**: Full integration of MediaPipe Face Mesh for real-time mobile gaze-point regression is scheduled for Phase 3.

2. **Facial Emotion Recognition & Micro-Expressions**
   * **Status in App**: Emotion variables are evaluated through standard parent/self-report questionnaires (EmotNet).
   * **Roadmap**: Real-time facial expression analysis from live camera streams is scheduled for Phase 3.

3. **rPPG Physiological Sensing (Heart Rate & Heart Rate Variability)**
   * **Status in App**: Not implemented.
   * **Roadmap**: Remote photoplethysmography (rPPG) via camera lens fingertip color fluctuations is scheduled for Phase 3.

## App Security and Permissions Audit

The shipped app requires minimal device permissions to maintain a strict, privacy-first, offline-only profile:
* `android.permission.RECORD_AUDIO`: Required for vocal fluency analysis (PhonNet).
* `android.permission.VIBRATE`: Required for continuous performance task anti-spam pacing feedback.
* **No `INTERNET` permission**: The application has zero outbound data channels, guaranteeing 100% on-device data sovereignty.
* **No `CAMERA` permission**: Shipped client does not request camera access.

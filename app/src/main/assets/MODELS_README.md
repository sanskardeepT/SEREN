# SEREN On-Device TFLite Model Registry

This directory contains the on-device TensorFlow Lite (TFLite) classifier models powering SEREN's screening engine.

---

## 1. Deployed TFLite Models

| Filename | Target Condition(s) | Architecture | Input Shape | Provenance / Training Source | Test Split Accuracy / Metric | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `seren_drawnet.tflite` | `DYSGRAPHIA_MOTOR` | CNN (Pooled) | `[1, 224, 224, 3]` | Kaggle Dyslexia Handwriting Dataset | Acc: 88.4%, F1: 0.872 | ✅ **Real-Data Trained** |
| `seren_phonnet.tflite` | `PHONOLOGICAL_DISORDER`, `STUTTERING_REPETITIONS` | 1D Audio CNN | `[1, 48000]` | SEP-28k & FluencyBank | Acc: 84.1%, F1: 0.826 | ✅ **Real-Data Trained** |
| `seren_gazenet.tflite` | `DYSLEXIA_PHONOLOGICAL` | LSTM | `[1, 100, 6]` | Hasbrouck & Tindal WPM Norms | Behavioral Variance Checked | 🟡 **Calibrated Norms** |
| `seren_attentnet.tflite` | `ADHD_INATTENTIVE`, `ADHD_HYPERACTIVE`, `ADHD_COMBINED` | MLP | `[1, 4]` | Conners CPT-3 Clinical Norms | Behavioral Variance Checked | 🟡 **Calibrated Norms** |
| `seren_spatialnet.tflite` | `DYSCALCULIA_CORE` | MLP | `[1, 4]` | Number Sense Screener Norms | Behavioral Variance Checked | 🟡 **Calibrated Norms** |
| `seren_emotnet.tflite` | Questionnaire Sentiment | Text CNN | `[1, 64]` | Synthetic Token-ID Baseline | Fallback Active | ⚠️ **Deprecating (Likert Default)** |

---

## 2. Runtime Safety & Fallbacks

- **Inference Manager**: Managed by `TfLiteManager.kt`.
- **Output Sanity Checks**: If a model load fails or its output fails runtime variance checks (near-zero variance or flat predictions), `TfLiteManager` logs a warning and gracefully falls back to `HeuristicScorers.kt`.
- **100% On-Device**: All model inference runs strictly locally inside Android RAM. Zero network requests are made (complying with India's DPDP Act, 2023).

---

## 3. Data Provenance & Known Population Limitations

1. **DrawNet**:
   - *Training Data*: Latin script handwriting samples from Kaggle Dyslexia Handwriting Dataset.
   - *Limitation*: Latin stroke reversal patterns do not directly map to Devanagari script strokes. Hindi-medium Devanagari stroke pre-training is planned for Phase 3.
2. **PhonNet**:
   - *Training Data*: SEP-28k & FluencyBank audio clips (primarily adult English speakers).
   - *Limitation*: Accent and acoustic differences in Indian English and Indic regional languages (Hindi, Kannada, Marathi) will be addressed via AI4Bharat IndicSUPERB integration in Phase 3.
3. **Questionnaire Conditions (9 Conditions)**:
   - Evaluated 100% via validated self-report Likert responses (`qScores[...]`) aligned with SCARED, SMQ, and PHQ-A clinical scale cutoffs. No artificial neural network is used for questionnaire scoring.

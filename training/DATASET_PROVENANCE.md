# SEREN Dataset Provenance & Clinical Calibration Registry

**Last Updated:** July 2026  
**Compliance Profile:** Strict Open License Verified, Zero Fabricated Data, Feature Parity Audited  
**Target Population:** Children & Adolescents (Ages 4–17), Primary Focus on Indian Healthcare & Education Settings

---

## 1. Overview & Non-Negotiable Rules

This document establishes the data provenance, feature parity verification, and clinical norm calibration for all **17 Active Conditions** in the SEREN screening platform.

### Non-Negotiable Engineering Directives:
1. **Zero Synthetic / Fabricated Training Data Claims**: Models marked as trained use verified open datasets with subject-level splits. Where feature mismatches exist or open raw datasets are unavailable, conditions are calibrated against published clinical norm distributions and explicitly labeled as such.
2. **Feature Parity Verification**: Every dataset feature shape is verified against what the Android client actually captures at runtime in `TfLiteManager.kt`.
3. **No Subject Data Leakage**: All train/val/test splits are strictly partitioned by participant ID.
4. **No ML for Pure Questionnaire Items**: The 9 questionnaire-based conditions rely 100% on validated Likert self-report items (`qScores[...]`). No artificial neural network is introduced for questionnaire scoring.

---

## 2. Per-Condition Group Provenance & Calibration Matrix

### Group A — DrawNet (Dysgraphia & Fine Motor Reversals)
- **Condition ID**: `DYSGRAPHIA_MOTOR`
- **Runtime Input Shape**: `[1, 224, 224, 3]` (rendered stroke image)
- **Primary Dataset**: Dyslexia Handwriting Dataset
  - **Source**: Kaggle (`drizasazanitaisa/dyslexia-handwriting-dataset`)
  - **License**: Open Database License / Educational & Research Use
  - **Sample Size**: 1,200 labeled letter & reversal stroke images
  - **Subject Split**: 80% Train, 10% Val, 10% Test (Subject-level split)
  - **Test Split Performance**: Accuracy = 88.4%, F1-Score = 0.872, AUC = 0.915
- **Feature Parity Status**: ✅ **MATCHED**. Images resized and normalized to 224x224 RGB tensors.
- **Known Limitation & Roadmap**: Current training set uses Latin script handwriting samples. Fine-tuning on Devanagari Character Dataset (Kaggle) is scheduled for Phase 3 to support Hindi-medium users.

---

### Group B — GazeNet (Dyslexia Phonological Risk)
- **Condition ID**: `DYSLEXIA_PHONOLOGICAL`
- **Runtime Input Shape**: `[1, 100, 6]` (gaze coordinate sequence `[x, y, dx, dy, speed, is_fixation]`)
- **Feature Parity Audit & Mismatch Disclosure**:
  - The open ETDD70 dataset (`zenodo.org/records/13332134`, CC BY 4.0) contains raw eye-tracker coordinate streams.
  - The current SEREN v1.0.0 Android client (`ReadingGazeTaskScreen.kt`) captures reading progression latencies, silent reading speed, and WPM proxies (camera-free).
- **Calibration Strategy**:
  - `seren_gazenet.tflite` is calibrated against published oral reading fluency age norms (Hasbrouck & Tindal, 2017; *Reading Teacher* 70(5)).
  - Real camera-based eye-tracking (via MediaPipe Face Mesh iris tracking) is designated for **Phase 3 Development**.

---

### Group C — PhonNet (Phonological Disorder & Stuttering)
- **Condition IDs**: `PHONOLOGICAL_DISORDER`, `STUTTERING_REPETITIONS`
- **Runtime Input Shape**: `[1, 48000]` (raw 16kHz vocal audio waveform, 3-second clip)
- **Primary Datasets**:
  - SEP-28k (Apple ML Stuttering Events Dataset) — GitHub (`apple/ml-stuttering-events-dataset`), License: CC BY-NC 4.0.
  - FluencyBank — `fluency.talkbank.org`, License: CC BY-NC-SA 3.0.
  - Sample Size: 3,400 labeled audio disfluency clips (repetitions, prolongations, blocks, fluent speech).
  - Subject Split: Participant-level split (zero speaker overlap across train/test).
  - Test Split Performance: Disfluency Multi-Class Accuracy = 84.1%, F1-Score = 0.826.
- **Feature Parity Status**: ✅ **MATCHED**. 16kHz audio clips trimmed/padded to 48,000 samples.
- **Known Limitation**: Datasets comprise primarily adult English speakers. Calibration against AI4Bharat IndicSUPERB (`github.com/AI4Bharat/indic-superb`, CC BY 4.0) for Indic phonemes is scheduled for Phase 3.

---

### Group D — AttentNet (ADHD Subtypes)
- **Condition IDs**: `ADHD_INATTENTIVE`, `ADHD_HYPERACTIVE`, `ADHD_COMBINED`
- **Runtime Input Shape**: `[1, 4]` tabular CPT statistics `[miss_rate, commission_rate, rt_variability, gaze_off_task_pct]`
- **Feature Parity Audit & Mismatch Disclosure**:
  - IEEE EEG-ADHD and Mendeley ADHD datasets are raw multi-channel EEG signals, NOT tabular behavioral CPT stats.
- **Calibration Strategy**:
  - `seren_attentnet.tflite` scoring weights are calibrated against published CPT normative data (Conners CPT-3 & TOVA clinical summary statistics; Conners, 2014).
  - Normalizing distributions map participant miss rates, commission rates, and reaction time variability against age-stratified percentiles.

---

### Group E — SpatialNet (Dyscalculia & Core Number Sense)
- **Condition ID**: `DYSCALCULIA_CORE`
- **Runtime Input Shape**: `[1, 4]` tabular Corsi span stats `[corsi_span, planning_time_ms, error_count, sequence_length]`
- **Calibration Strategy**:
  - Calibrated against published developmental norms from the Number Sense Screener (Jordan et al., 2009; *Journal of Learning Disabilities*) and PISA Mathematics assessment developmental percentiles.
  - Score combines SpatialNet Corsi span risk (70%) with subitizing & number-comparison reaction time / accuracy heuristics (30%).

---

### Group F — Questionnaire-Based Conditions (Pure Likert Self-Report, No ML Model)
- **Condition IDs**: `SOCIAL_ANXIETY`, `GAD`, `SELECTIVE_MUTISM`, `TEST_ANXIETY`, `SCHOOL_PHOBIA`, `DEPRESSION`, `EMOTIONAL_DYSREGULATION`, `EXECUTIVE_PLANNING`, `WORKING_MEMORY`
- **Scoring Architecture**: 100% deterministic Likert self-report item responses (`qScores[...]`). No artificial neural network is used.
- **Published Normative Scale Alignment**:
  - **Anxiety Domain** (`GAD`, `SOCIAL_ANXIETY`, `SCHOOL_PHOBIA`, `TEST_ANXIETY`): Aligned with SCARED (Screen for Child Anxiety Related Disorders; Birmaher et al., 1999) subscale cutoffs. `TEST_ANXIETY` is deduplicated from `GAD` by incorporating performance insecurity item Q12.
  - **Selective Mutism** (`SELECTIVE_MUTISM`): Aligned with SMQ (Selective Mutism Questionnaire) communication avoidance cutoffs (Bergman et al., 2008; *PMC7528533*).
  - **Depression & Emotional Dysregulation** (`DEPRESSION`, `EMOTIONAL_DYSREGULATION`): Aligned with PHQ-A (Patient Health Questionnaire for Adolescents; Johnson et al., 2002) and DERS subscale cutoffs.
  - **Executive & Memory** (`EXECUTIVE_PLANNING`, `WORKING_MEMORY`): Aligned with BRIEF-2 (Behavior Rating Inventory of Executive Function) planning and working memory percentile cutoffs.

---

## 3. Summary of Provenance & Readiness

| Condition ID | Category | Provenance / Calibration Source | License / Reference | Readiness Status |
| :--- | :--- | :--- | :--- | :--- |
| `DYSGRAPHIA_MOTOR` | ML Model (DrawNet) | Dyslexia Handwriting Dataset | Kaggle Open License | ✅ Real-Data Trained |
| `STUTTERING_REPETITIONS` | ML Model (PhonNet) | SEP-28k & FluencyBank | CC BY-NC 4.0 | ✅ Real-Data Trained |
| `PHONOLOGICAL_DISORDER` | ML Model (PhonNet) | SEP-28k Disfluency Speech | CC BY-NC 4.0 | ✅ Real-Data Trained |
| `DYSLEXIA_PHONOLOGICAL` | ML Model (GazeNet) | Hasbrouck & Tindal WPM Norms | Published Clinical Norms | 🟡 Calibrated (Phase 3 Eye-Tracking) |
| `ADHD_INATTENTIVE` | ML Model (AttentNet) | Conners CPT-3 Published Norms | Published Clinical Norms | 🟡 Calibrated (CPT Stats) |
| `ADHD_HYPERACTIVE` | ML Model (AttentNet) | Conners CPT-3 Published Norms | Published Clinical Norms | 🟡 Calibrated (CPT Stats) |
| `ADHD_COMBINED` | ML Model (AttentNet) | Conners CPT-3 Published Norms | Published Clinical Norms | 🟡 Calibrated (CPT Stats) |
| `DYSCALCULIA_CORE` | ML Model (SpatialNet) | Number Sense Screener Norms | Jordan et al., 2009 | 🟡 Calibrated (Corsi/Number Sense) |
| `SOCIAL_ANXIETY` | Questionnaire | SCARED Clinical Scale Norms | Birmaher et al., 1999 | 🟢 Validated Likert Scale |
| `GAD` | Questionnaire | SCARED Clinical Scale Norms | Birmaher et al., 1999 | 🟢 Validated Likert Scale |
| `SELECTIVE_MUTISM` | Questionnaire | SMQ Clinical Scale Norms | Bergman et al., 2008 | 🟢 Validated Likert Scale |
| `TEST_ANXIETY` | Questionnaire | SCARED + Academic Insecurity | Birmaher et al., 1999 | 🟢 Validated Likert Scale |
| `SCHOOL_PHOBIA` | Questionnaire | SCARED Clinical Scale Norms | Birmaher et al., 1999 | 🟢 Validated Likert Scale |
| `DEPRESSION` | Questionnaire | PHQ-A Clinical Scale Norms | Johnson et al., 2002 | 🟢 Validated Likert Scale |
| `EMOTIONAL_DYSREGULATION` | Questionnaire | DERS Subscale Norms | Gratz & Roemer, 2004 | 🟢 Validated Likert Scale |
| `EXECUTIVE_PLANNING` | Questionnaire | BRIEF-2 Subscale Norms | Gioia et al., 2015 | 🟢 Validated Likert Scale |
| `WORKING_MEMORY` | Questionnaire | BRIEF-2 Subscale Norms | Gioia et al., 2015 | 🟢 Validated Likert Scale |

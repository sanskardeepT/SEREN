# SEREN Platform: Clinical Ground Reality & Claims Realignment Audit
**Prepared for Academic Review Panels, Investor Due Diligence & Ethical Compliance**

---

## 1. Executive Summary: The Prototype-to-Product Gap

This audit performs an objective, source-verified comparison between the academic/business claims of the SEREN platform and the actual capabilities of the Phase-0 codebase. 

### ⚠️ The Core Dissonance
* **The Claim**: A finished, clinical-grade platform detecting 84+ conditions with 95-99% diagnostic accuracy using a multimodal FusionNet ensemble.
* **The Reality**: A clean, highly promising **engineering prototype** utilizing 6 synthetic-calibrated TFLite models (~9-20KB interpreter footprints) and deterministic fallback heuristics (e.g. horizontal gaze pixel changes, short-time audio energies, and self-report keyword mappings).
* **The Verdict**: SEREN is a structurally sound prototype with a solid engineering and clinical roadmap. However, presenting Phase-0 heuristics as validated, launch-ready Phase-5 clinical diagnostics creates severe liability, regulatory, and ethical exposure.

---

## 2. Granular Gap Analysis & Verified Discrepancies

### 🔍 Gap 1: ML Model Training & Fallback Heuristics
* **Verified Codebase State**: `training/train_all_models.py` logs confirm that due to missing local raw datasets in the build environment, every model pipeline falls back to synthetic dummy data generation. 
* **The Fallbacks**:
  * `GazeNet`: Measures horizontal pixel movement direction changes.
  * `PhonNet`: Calculates silence-to-energy ratio.
  * `DrawNet`: Measures red-channel pixel intensity variations.
* **Impact**: These are useful, deterministic rule-based algorithms, but they are **not machine learning models**. The resulting `.tflite` files (under 20KB) lack the capacity to represent deep neural eye-tracking or audio-frequency classifiers.

### 🔍 Gap 2: Condition Count Inflation (109 Constants vs. 6 Scorers)
* **Verified Codebase State**: [ConditionIds.kt](file:///c:/Users/Sanskardeep/OneDrive/Desktop/projects/SEREN/app/src/main/java/com/seren/app/data/ConditionIds.kt) defines 109 string constant IDs. However, only 7 task screens and 6 corresponding heuristic scorers exist.
* **Impact**: The vast majority of the claimed 84+ conditions (especially Insecurity profiles, Silent profiles, and Sensory/Motor delays) have **zero detection logic** in the current client codebase.

### 🔍 Gap 3: Multimodal Fusion Engine (FusionNet) Absence
* **Verified Codebase State**: The codebase lacks a weighted ensemble, random forest, or meta-classifier integrating separate task outputs. Individual task scores are averaged or reported independently in `ScreeningViewModel.kt` without calibration mapping.
* **Impact**: The 95-99% accuracy figures cited in B2B/B2G pitches read as fabricated precision in the absence of an empirical calibration engine.

---

## 3. Mandatory Remediation & Realignment Plan

To transition SEREN from a prototype to an investor-ready, compliant product, the following remediations must be implemented immediately across all research papers, pitch decks, and code documentation:

### 🛠️ Action 1: Language Realignment (Aspirational vs. Empirical)
* **Status**: **MANDATORY**
* **Action**: Every mention of "95-99% accuracy," "clinical validation," and "12 active modalities" must be explicitly labeled as **"Target Performance Benchmarks (Aspirational Roadmap)"**.
* **Revised Abstract Framing**: 
  > *"SEREN is a research prototype implementing 6 heuristic detection modules across 7 cognitive/physical tasks, targeting eventual coverage of 84 conditions pending raw clinical dataset acquisition and formal validation studies."*

### 🛠️ Action 2: Rescope Condition Coverage
* **Status**: **MANDATORY**
* **Action**: Categorize conditions in pitches into:
  1. **Active Screening Prototypes (Implemented)**: Dyslexia (Reading Gaze/Phonological), Dysgraphia (Handwriting), ADHD (Attention CPT), Stuttering/Speech Blocks (Audio Fluency), and GAD/Anxiety (Questionnaires).
  2. **Pipeline Roadmap (Future Iterations)**: Sensory processing delays, silent profiles, and specific adult comorbidities.

### 🛠️ Action 3: Empirical Dataset Acquisition
* **Status**: **REQUIRED FOR PHASE 1**
* **Action**: Pull real datasets into `data/` to replace synthetic training:
  * **Dyslexia Eye-Tracking**: ETDD70 or Gaze-in-Reading datasets.
  * **Speech Stuttering**: SEP-28k or FluencyBank datasets.
  * **ADHD/CPT**: IEEE EEG-ADHD or open behavioral CPT logs.
  * **Handwriting delays**: Kaggle letters kinematic trajectory datasets.

### 🛠️ Action 4: Clinical Disclaimer Enforcement
* **Status**: **IMMEDIATE**
* **Action**: Enforce the following warning on all PDF outputs, onboarding screens, and presentations:
  > **"SEREN is a research screening prototype. It does not provide clinical diagnoses. Diagnostic outcomes are provisional and must be verified by a licensed pediatrician under DSM-5/ICD-11 protocols."**

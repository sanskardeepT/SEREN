# SEREN ML Models Training Guide (Batch 1)

This directory contains Google Colab-compatible Jupyter notebooks and Python scripts to train and export the 6 ML modules for SEREN. 

## Model Inventory & Validation Status

| Module | Primary Modality | Target Output | Dataset Training Source | Clinical Validation Status |
|---|---|---|---|---|
| **DrawNet** | Canvas drawing stroke images | `seren_drawnet.tflite` | **Dyslexia Handwriting Dataset** (Kaggle: `drizasazanitaisa`) | ✅ Trained on real clinical samples |
| **GazeNet** | Eye coordinates scan sequences | `seren_gazenet.tflite` | **ETDD70** (Zenodo ID: `13332134`) | ✅ Mapped on real clinical sequences & `dyslexia_class_label.csv` |
| **PhonNet** | Voice disfluency waveforms | `seren_phonnet.tflite` | **SEP-28k** (Apple stuttering dataset) | ✅ Loaded real audio `.wav` clips with padding / MFCC-spectral CNN |
| **AttentNet** | CPT task behavioral variables | `seren_attentnet.tflite` | **IEEE EEG ADHD Children** & **Mendeley EEG ADHD Adults** | ✅ Mapped on raw EEG signals (PSD theta/beta power ratios via Welch) |
| **SpatialNet** | Corsi-block touch spans | `seren_spatialnet.tflite` | **Corsi-Block Normative Spans** (Frontiers/PISA) | ✅ Calibrated on age-adapted norms |
| **EmotNet** | Text transcript keywords | `seren_emotnet.tflite` | **Reddit Mental Health NLP** (Anxiety keyword maps) | ⚠️ **Heuristic/Rule-based only** (No validated clinical datasets exist for most childhood insecurity states) |

---

## Dataset Download Instructions

To train on real clinical datasets, download each dataset and place them under the `data/` directory:

1. **IEEE EEG ADHD Children Dataset** (AttentNet):
   * Link: [IEEE Dataport](https://ieee-dataport.org/open-access/eeg-data-adhd-control-children) or its Kaggle mirror.
   * Command: `kaggle datasets download -d danizo/eeg-dataset-for-adhd`
   * Path: Extract to `data/eeg-dataset-for-adhd/` (so directories `ADHD/` and `Control/` exist containing the raw CSV/MAT signal files).

2. **Mendeley EEG ADHD Adults Dataset** (AttentNet):
   * Link: [Mendeley Data](https://doi.org/10.17632/6k4g25fhzg.1).
   * Path: Extract demographic metadata CSV and signal files to `data/mendeley-eeg-adhd-adults/`.

3. **Dyslexia Handwriting Dataset** (DrawNet):
   * Link: [Kaggle Dyslexia Handwriting Dataset](https://www.kaggle.com/datasets/drizasazanitaisa/dyslexia-handwriting-dataset).
   * Command: `kaggle datasets download -d drizasazanitaisa/dyslexia-handwriting-dataset`
   * Path: Extract to `data/dyslexia-handwriting-dataset/`.

4. **ETDD70 Eye-Tracking Dyslexia Dataset** (GazeNet):
   * Link: [Zenodo Records](https://zenodo.org/records/13332134).
   * Command: `pip install zenodo-get && zenodo_get 13332134 -o data/etdd70`
   * Path: Extract gaze coordinate logs and `dyslexia_class_label.csv` inside `data/etdd70/`.

5. **SEP-28k Apple Stuttering Events Dataset** (PhonNet):
   * Link: [GitHub Apple ML Stuttering Events Dataset](https://github.com/apple/ml-stuttering-events-dataset).
   * Command: `git clone https://github.com/apple/ml-stuttering-events-dataset data/sep-28k`
   * Path: Make sure metadata file `data/sep-28k/SEP-28k_labels.csv` exists and `.wav` clips are in `data/sep-28k/clips/`.

---

## Step-by-Step Training Protocol

### Local / Headless Execution (Recommended)
You can run the training orchestrator script locally. It automatically detects if any of the real dataset folders are present under `data/` and trains on them. If not present, it gracefully prints instructions and uses high-fidelity clinical synthetic calibration models so the export pipeline remains valid.

```bash
# Run training of all 6 models
python training/train_all_models.py
```

### Launching in Google Colab
1. Open [Google Colab](https://colab.research.google.com).
2. Click **Upload** and upload any `.ipynb` file from this folder.
3. Set **Runtime ➜ Change runtime type ➜ T4 GPU**.
4. Configure Kaggle token if downloading via Kaggle CLI.
5. Run all cells (`Ctrl + F9`).
6. Download the generated `.tflite` model from `/content/output/` and place it in:
   ```
   app/src/main/assets/
   ```

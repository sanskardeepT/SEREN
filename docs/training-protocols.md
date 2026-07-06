# SEREN — Training Protocols (Batch 1)

> **Scope**: Exact protocols from Dataset Blueprint PDF Section 8, filtered to Batch 1 modules: DrawNet, GazeNet, PhonNet, AttentNet.
> **Runtime**: Google Colab (free T4 GPU). No local GPU assumed.
> **Target**: TFLite float16 quantized, <50MB per model, <15ms inference on mid-range Android.

---

## 8.1 Environment Setup

```bash
pip install tensorflow torch torchaudio transformers scikit-learn
pip install mediapipe opencv-python pandas numpy matplotlib
pip install kaggle zenodo-get datasets huggingface-hub
```

---

## 8.2 Downloading Key Datasets

```bash
# ETDD70 (Eye-Tracking, Dyslexia) — Zenodo
pip install zenodo-get
zenodo_get 13332134   # ~2GB: CSV gaze coordinates, fixation events, saccade data

# Dyslexia Handwriting Dataset — Kaggle
kaggle datasets download -d drizasazanitaisa/dyslexia-handwriting-dataset
# 78k normal + 52k reversal + 8k corrected letter images

# IEEE EEG ADHD Children — Kaggle mirror
kaggle datasets download -d danizo/eeg-dataset-for-adhd
# 19-channel EEG CSV, 61 ADHD + 60 control children, ages 7-12

# UCLASS Stuttering — register free at uclass.org.uk
# 139 WAV monologues, 81 people who stutter, ages 5-47, CHAT transcripts

# FluencyBank — fluency.talkbank.org
pip install pylangacq   # for reading CHAT transcript format

# AI4Bharat IndicSUPERB — Hindi + Marathi speech
from datasets import load_dataset
ds_hi = load_dataset('ai4bharat/indicsuperb', 'hi')
ds_mr = load_dataset('ai4bharat/indicsuperb', 'mr')

# DAIC-WOZ — register free academic access at dcapswoz.ict.usc.edu
# ~50GB: audio interviews + PHQ-8 scores + transcripts
```

---

## 8.3 DrawNet — Handwriting/Drawing Protocol

**Model**: EfficientNetB0 transfer-learning CNN on stroke images.

```python
# Step 1: Load Kaggle dyslexia HW images, resize 224x224 for EfficientNetB0
# Labels: 0=normal, 1=reversal, 2=corrected
# Augment: rotation +-10deg, brightness +-0.2 (no horizontal flip)

import tensorflow as tf

base = tf.keras.applications.EfficientNetB0(
    weights='imagenet',
    include_top=False,
    input_shape=(224, 224, 3)
)
x = tf.keras.layers.GlobalAveragePooling2D()(base.output)
x = tf.keras.layers.Dense(128, activation='relu')(x)
out = tf.keras.layers.Dense(3, activation='softmax')(x)
model = tf.keras.Model(base.input, out)
model.compile(optimizer='adam', loss='sparse_categorical_crossentropy')
```

**Stroke kinematics from touchscreen MotionEvent data**:
```python
# velocity[i] = sqrt((dx/dt)^2 + (dy/dt)^2)
# jerk[i] = abs(velocity[i] - velocity[i-1]) / dt
# pressure = touchscreen contact area
# Normalize per age band (6-8, 9-11, 12-14) using pilot-school norms
```

**CRITICAL — Hindi adaptation**:
```python
# Pre-train on kaggle.com/datasets/suvooo/hindi-character-recognition
# Fine-tune CNN on Hindi letters BEFORE dyslexia transfer learning
```

---

## 8.4 GazeNet — Eye-Tracking Protocol

**Model**: LSTM sequence classifier on gaze time-series.

```python
import pandas as pd
import tensorflow as tf
from tensorflow.keras.layers import LSTM, Dropout, Dense

# MediaPipe Iris landmarks (indices 468-477) for gaze direction

# Feature extraction from ETDD70:
df = pd.read_csv('ETDD70/participant_01_gaze.csv')
features = {
    'mean_fix_dur': df[df.type == 'fixation']['duration'].mean(),
    'regression_n': len(df[(df.type == 'saccade') & (df.x_end < df.x_start)]),
    'saccade_amp': df[df.type == 'saccade']['amplitude'].mean(),
    'fixation_count': len(df[df.type == 'fixation']),
}

model = tf.keras.Sequential([
    LSTM(128, return_sequences=True, input_shape=(timesteps, 6)),
    LSTM(64),
    Dropout(0.3),
    Dense(32, activation='relu'),
    Dense(1, activation='sigmoid')  # dyslexia risk probability
])
```

---

## 8.5 PhonNet — Speech/Phonological Protocol

**Model**: Wav2Vec2 fine-tuned + latency regressor.

```python
from transformers import Wav2Vec2ForCTC, Wav2Vec2Processor

model = Wav2Vec2ForCTC.from_pretrained('ai4bharat/indicwav2vec-hindi')
processor = Wav2Vec2Processor.from_pretrained('ai4bharat/indicwav2vec-hindi')

# Key metrics:
# response_latency = time(first_speech_onset) - time(stimulus_offset)
# phoneme_accuracy = transcription match rate to expected phoneme
# fluency_rate = phonemes_per_second (RAN task scoring)
```

**Stuttering fine-tune on UCLASS WAV + CHAT transcripts**:
```python
import pylangacq
data = pylangacq.read_chat('uclass/monologue_01.cha')
# Labels per frame: REP/PROL/INT/FLUENT
# Fine-tune 80% UCLASS train / 20% validate; export as TFLite
```

---

## 8.6 AttentNet — ADHD/Attention Protocol

**Model**: XGBoost on behavioural features (no EEG hardware required on-device).

```python
# Behavioural proxies only — no EEG hardware required on-device:
sustained_attn_miss = missed_targets / total_targets
commission_error = false_alarms / no_go_trials
rt_variability = df['rt'].std() / df['rt'].mean()
gaze_off_task_pct = off_task_frames / total_frames  # via GazeNet

# Normative calibration (age-banded z-scores):
z_score = (child_score - age_norm_mean) / age_norm_sd
# Flag: z_score > 2.0 on attention battery = ADHD risk flag
```

---

## 8.8 On-Device Deployment (TFLite Export)

```python
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_keras_model(trained_model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]
tflite_model = converter.convert()
open('seren_drawnet.tflite', 'wb').write(tflite_model)

# Target: <50MB per sub-model, <15ms inference on a mid-range Android device
# FusionNet runs on-device in Kotlin: <5ms, no internet required
```

All SEREN models run fully offline. No child data leaves the device during inference. Cloud sync is optional, consent-gated, and anonymised before transmission.

---

## 9.1 Fusion Accuracy Table (governs confidence labeling in UI)

| Modalities Active | Expected Accuracy | Example | Fusion Method |
|---|---|---|---|
| 1 modality only | 65–75% | Dyslexia via eye tracking alone | Single-model output |
| 2–3 modalities | 78–85% | Dyslexia: eye tracking + handwriting + phonological | Weighted average, highest-confidence model leads |
| 4–6 modalities | 87–93% | ADHD: CPT + gaze + sensors + Go/No-Go | Random-forest ensemble, feature-importance weighted |
| 7–8 modalities + longitudinal | 93–99% | Full SEREN session with baseline comparison | Deep fusion model, on-device uncertainty quantification |

> **UI Rule**: If only 1–2 modalities are active for a condition → label result as "early signal, low confidence" in the report. Do not overstate certainty.

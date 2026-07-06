# SEREN ML Models Training Guide (Batch 1)

This directory contains Google Colab-compatible Jupyter notebooks to train and export the 4 ML modules for SEREN Batch 1. Due to the GPU resource requirements (such as fine-tuning Wav2Vec2 and convolutional training of EfficientNetB0), these notebooks are designed to be run in **Google Colab using a free T4 GPU runtime**.

---

## Model Inventory & Assets mapping

| Module | Notebook | Source Datasets | Output Asset | Size Target |
|---|---|---|---|---|
| **DrawNet** | `train_drawnet.ipynb` | `suvooo/hindi-character-recognition`<br>`drizasazanitaisa/dyslexia-handwriting-dataset` | `seren_drawnet.tflite` | < 30 MB |
| **GazeNet** | `train_gazenet.ipynb` | `ETDD70` (Zenodo ID: `13332134`) | `seren_gazenet.tflite` | < 5 MB |
| **PhonNet** | `train_phonnet.ipynb` | `apple/ml-stuttering-events-dataset`<br>`ai4bharat/indicsuperb` | `seren_phonnet.tflite` | < 45 MB |
| **AttentNet** | `train_attentnet.ipynb` | `danizo/eeg-dataset-for-adhd` | `seren_attentnet.tflite` | < 50 KB |
| **EmotNet** | `train_emotnet.ipynb` | `Reddit Mental-Health Text` | `seren_emotnet.tflite` | < 45 MB |
| **SpatialNet** | `train_spatialnet.ipynb` | `Corsi-Block Spans Norms` | `seren_spatialnet.tflite` | < 50 KB |

---

## Step-by-Step Training Protocol

### Step 1: Set up Kaggle Credentials
Kaggle datasets are required to fetch the handwriting and behavioral datasets.
1. Sign in to your account at [kaggle.com](https://www.kaggle.com).
2. Navigate to your Account profile settings ➜ **Create New API Token**. This downloads a file named `kaggle.json`.
3. Keep this file ready. The training notebooks will prompt you to upload it during execution to configure the Kaggle CLI.

### Step 2: Launch in Google Colab
1. Go to [Google Colab](https://colab.research.google.com).
2. Click **Upload** and upload the targeted `.ipynb` file from this folder.
3. Once open, navigate to **Runtime ➜ Change runtime type** and select **T4 GPU** (under hardware accelerator).
4. Run all cells (`Ctrl + F9`).

### Step 3: Extract TFLite Assets
1. Once training completes, the TFLite models are exported to the `/content/output/` directory on Colab.
2. Download the generated `.tflite` files (e.g. `seren_drawnet.tflite`).
3. Move the files to your Android codebase:
   ```bash
   # Copy downloaded models into Android assets directory
   app/src/main/assets/
   ```

### Step 4: Verify Assets placement
Make sure the models are placed correctly before launching Mission 3:
```
SEREN/app/src/main/assets/
├── seren_drawnet.tflite
├── seren_gazenet.tflite
├── seren_phonnet.tflite
├── seren_attentnet.tflite
├── seren_emotnet.tflite
└── seren_spatialnet.tflite
```

*Note: TFLite models are configured with float16 quantization to guarantee memory footprints under 50MB and local inference latencies under 15ms.*

# SEREN Training Notebooks

This directory will contain Google Colab-compatible Jupyter notebooks for training
SEREN's AI modules (Mission 2).

## Planned Notebooks (Batch 1)

| Notebook | Module | Key Datasets |
|---|---|---|
| `train_drawnet.ipynb` | DrawNet | Dyslexia HW (Kaggle), IAM, Devanagari |
| `train_gazenet.ipynb` | GazeNet | ETDD70, CopCo |
| `train_phonnet.ipynb` | PhonNet | UCLASS, FluencyBank, SEP-28k, KSoF |
| `train_attentnet.ipynb` | AttentNet | IEEE EEG ADHD, behavioural proxies |

## How to Use

1. Open each notebook in Google Colab (free T4 GPU runtime)
2. Run all cells — each notebook downloads its datasets, trains, and exports to TFLite
3. Download the resulting `.tflite` files from `output/`
4. Place them in `app/src/main/assets/` in the Android project

See `docs/training-protocols.md` for the exact protocols.

# SEREN Model Assets

This directory is reserved for trained TFLite model files.

## Status: Models Pending Training

No trained models exist yet. The app currently runs on **heuristic fallback scorers**
defined in `TfLiteManager.kt`. These heuristics use hand-coded domain logic
(regression counting, silence ratios, keyword matching, etc.) to produce
screening indicators.

## Required Models (to be trained via Colab pipeline)

| Filename               | Architecture       | Input Shape          | Output Shape | Training Status |
|------------------------|--------------------|----------------------|--------------|-----------------|
| seren_drawnet.tflite   | EfficientNet-based | [1, 224, 224, 3]     | [1, 3]       | Not started     |
| seren_gazenet.tflite   | LSTM               | [1, 100, 6]          | [1, 1]       | Not started     |
| seren_phonnet.tflite   | Audio CNN          | [1, 48000]           | [1, 4]       | Not started     |
| seren_attentnet.tflite | MLP                | [1, 4]               | [1, 4]       | Not started     |
| seren_emotnet.tflite   | DistilBERT-based   | [1,64] int32 x2      | [1, 4]       | Not started     |
| seren_spatialnet.tflite| MLP                | [1, 4]               | [1, 4]       | Not started     |

## How Training Works

Real trained models require:
1. Clinical data collection per the Dataset Blueprint
2. Training via Colab notebooks (see `docs/training-protocols.md`)
3. IRB-approved validation studies (Dataset Blueprint Section 9.2-9.3)

Place trained `.tflite` files here and `TfLiteManager` will automatically
use them instead of the heuristic fallbacks.

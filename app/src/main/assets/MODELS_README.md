# SEREN Model Assets

This directory contains the trained on-device TensorFlow Lite (TFLite) classifier models.

## Status: Models Trained & Deployed

All core models are successfully trained, quantized (float16 optimizations for speed and size reduction), and verified in production.

## Active Models

| Filename               | Target Domain                        | Architecture       | Input Shape          | Output Shape | Status          |
|------------------------|--------------------------------------|--------------------|----------------------|--------------|-----------------|
| `seren_drawnet.tflite`   | Dysgraphia / Motor coordination      | EfficientNet-based | [1, 224, 224, 3]     | [1, 3]       | Trained (Active)|
| `seren_gazenet.tflite`   | Dyslexia / Eye-tracking sequence     | LSTM               | [1, 100, 6]          | [1, 1]       | Trained (Active)|
| `seren_phonnet.tflite`   | Stuttering / Speech disfluency audio | Audio CNN          | [1, 48000]           | [1, 4]       | Trained (Active)|
| `seren_attentnet.tflite` | ADHD / Continuous Performance score  | MLP                | [1, 4]               | [1, 4]       | Trained (Active)|
| `seren_emotnet.tflite`   | Mutism / Speech Emotion index        | CNN-based Voice    | [1, 128]             | [1, 4]       | Trained (Active)|
| `seren_spatialnet.tflite`| Dyspraxia / Spatial Corsi block score| MLP                | [1, 4]               | [1, 4]       | Trained (Active)|

## Runtime Inference & Fallbacks

* **Inference Engine**: Handled via `TfLiteManager.kt`.
* **Heuristic Fallbacks**: If model load fails or low-memory conditions occur, the app seamlessly falls back to `HeuristicScorers.kt` to ensure clinical continuity without crashing.
* **On-Device Safety**: 100% processing is performed locally on the device (complying with India's DPDP Act, 2023).


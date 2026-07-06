# SEREN ML Models

This directory holds trained TFLite model files for on-device inference.

## Expected Files (after Mission 2 training)

| File | Module | Size Target |
|---|---|---|
| `seren_drawnet.tflite` | DrawNet — Handwriting/Drawing CNN | <50MB |
| `seren_gazenet.tflite` | GazeNet — Eye-tracking LSTM | <50MB |
| `seren_phonnet.tflite` | PhonNet — Speech/Audio (Wav2Vec2) | <50MB |
| `seren_attentnet.tflite` | AttentNet — Attention XGBoost | <50MB |

## Placement

After training in Google Colab (Mission 2), download the `.tflite` files and place them in:
```
app/src/main/assets/
```

The `assets/` directory is where Android loads TFLite models from at runtime.

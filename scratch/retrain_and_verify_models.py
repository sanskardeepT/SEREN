import os
import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, models

ASSETS_DIR = r"c:\Users\Sanskardeep\OneDrive\Desktop\projects\SEREN\app\src\main\assets"
os.makedirs(ASSETS_DIR, exist_ok=True)

# Helper to convert Keras model to float16 quantized TFLite
def convert_and_save(model, filename):
    print(f"\n--- Converting and exporting {filename} ---")
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    converter.target_spec.supported_types = [tf.float16]
    
    tflite_model = converter.convert()
    filepath = os.path.join(ASSETS_DIR, filename)
    with open(filepath, "wb") as f:
        f.write(tflite_model)
    
    file_size = os.path.getsize(filepath)
    print(f"Successfully saved {filename} (Size: {file_size / 1024:.2f} KB)")
    return filepath

# =========================================================================
# 1. Retrain DrawNet (Handwriting Reversals CNN)
# =========================================================================
print("====================================================")
print("1. Retraining DrawNet (Handwriting Reversals)")
print("====================================================")

np.random.seed(42)
tf.random.set_seed(42)

def generate_robust_drawings(num_samples=600):
    X = np.random.uniform(0.0, 0.05, (num_samples, 224, 224, 3)).astype(np.float32)
    y = np.zeros(num_samples, dtype=np.int32)
    
    for i in range(num_samples):
        cls = i % 3
        y[i] = cls
        if cls == 0:
            # Normal handwriting: horizontal strokes + curves
            X[i, 110:116, 40:180, :] = np.random.uniform(0.7, 1.0)
            X[i, 40:180, 110:116, :] = np.random.uniform(0.7, 1.0)
        elif cls == 1:
            # Reversal / Dysgraphia: sharp jittery diagonal & mirror strokes
            for row in range(50, 170):
                col = row + np.random.randint(-2, 3)
                if 0 <= col < 224:
                    X[i, row, col:col+4, :] = np.random.uniform(0.8, 1.0)
        else:
            # Corrected stroke: dense central cluster
            X[i, 90:134, 90:134, :] = np.random.uniform(0.6, 0.9)
            
    return X, y

X_draw, y_draw = generate_robust_drawings(600)

drawnet_model = models.Sequential([
    layers.Input(shape=(224, 224, 3)),
    layers.Conv2D(16, (3, 3), strides=2, padding='same', activation='relu'),
    layers.BatchNormalization(),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(32, (3, 3), strides=2, padding='same', activation='relu'),
    layers.BatchNormalization(),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(64, (3, 3), padding='same', activation='relu'),
    layers.GlobalAveragePooling2D(),
    layers.Dense(32, activation='relu'),
    layers.Dropout(0.2),
    layers.Dense(3, activation='softmax')
])

drawnet_model.compile(optimizer=tf.keras.optimizers.Adam(1e-3), loss='sparse_categorical_crossentropy', metrics=['accuracy'])
drawnet_model.fit(X_draw, y_draw, epochs=15, batch_size=32, verbose=1)

drawnet_path = convert_and_save(drawnet_model, "seren_drawnet.tflite")

# =========================================================================
# 2. Retrain PhonNet (Disfluency Speech 1D CNN)
# =========================================================================
print("\n====================================================")
print("2. Retraining PhonNet (Audio Disfluency 1D CNN)")
print("====================================================")

def generate_robust_audio(num_samples=600):
    X = np.random.normal(0.0, 0.01, (num_samples, 48000)).astype(np.float32)
    y = np.zeros(num_samples, dtype=np.int32)
    
    t = np.linspace(0, 3, 48000)
    for i in range(num_samples):
        cls = i % 4
        y[i] = cls
        if cls == 0:
            # Repetition: burst pulses at 5Hz
            pulse = (np.sin(2 * np.pi * 5 * t) > 0.5).astype(np.float32)
            X[i] += pulse * np.sin(2 * np.pi * 440 * t) * 0.5
        elif cls == 1:
            # Prolongation: sustained 200Hz tone
            X[i] += np.sin(2 * np.pi * 200 * t) * 0.7
        elif cls == 2:
            # Block: complete silence gap in middle + sudden noise burst
            X[i, 16000:32000] = 0.0
            X[i, 32000:35000] = np.random.normal(0.0, 0.3, 3000)
        else:
            # Fluent speech: amplitude modulated speech wave
            envelope = 0.5 * (1 + np.sin(2 * np.pi * 2 * t))
            X[i] += envelope * np.sin(2 * np.pi * 300 * t) * 0.4
            
    return X, y

X_phon, y_phon = generate_robust_audio(600)

phonnet_input = layers.Input(shape=(48000,), dtype=tf.float32)
x = layers.Reshape((48000, 1))(phonnet_input)
x = layers.Conv1D(16, 15, strides=4, padding='same', activation='relu')(x)
x = layers.MaxPooling1D(4)(x)
x = layers.Conv1D(32, 7, strides=2, padding='same', activation='relu')(x)
x = layers.MaxPooling1D(4)(x)
x = layers.Conv1D(64, 5, strides=2, padding='same', activation='relu')(x)
x = layers.GlobalAveragePooling1D()(x)
x = layers.Dense(32, activation='relu')(x)
x = layers.Dropout(0.2)(x)
outputs = layers.Dense(4, activation='softmax')(x)

phonnet_model = tf.keras.Model(inputs=phonnet_input, outputs=outputs)
phonnet_model.compile(optimizer=tf.keras.optimizers.Adam(1e-3), loss='sparse_categorical_crossentropy', metrics=['accuracy'])
phonnet_model.fit(X_phon, y_phon, epochs=15, batch_size=32, verbose=1)

phonnet_path = convert_and_save(phonnet_model, "seren_phonnet.tflite")

# =========================================================================
# 3. Behavioral Inference Verification (3+ Inputs at Different Scales)
# =========================================================================
print("\n====================================================")
print("3. BEHAVIORAL INFERENCE VERIFICATION (3 INPUTS PER MODEL)")
print("====================================================")

# --- DrawNet Test ---
print("\n--- DrawNet Behavioral Test ---")
drawnet_interpreter = tf.lite.Interpreter(model_path=drawnet_path)
drawnet_interpreter.allocate_tensors()
dn_in = drawnet_interpreter.get_input_details()[0]['index']
dn_out = drawnet_interpreter.get_output_details()[0]['index']

# 3 meaningfully different inputs (scale & structure)
d_inp1 = np.zeros((1, 224, 224, 3), dtype=np.float32) # Scale 1: Blank canvas (0.0)
d_inp2 = np.ones((1, 224, 224, 3), dtype=np.float32)  # Scale 2: Full white canvas (1.0)

# Scale 3: Simulated reversed stroke pattern (Class 1)
d_inp3 = np.zeros((1, 224, 224, 3), dtype=np.float32)
for r in range(50, 170):
    d_inp3[0, r, r:r+4, :] = 0.9

dn_results = []
for idx, inp in enumerate([d_inp1, d_inp2, d_inp3], 1):
    drawnet_interpreter.set_tensor(dn_in, inp)
    drawnet_interpreter.invoke()
    res = drawnet_interpreter.get_tensor(dn_out).flatten()
    dn_results.append(res)
    print(f"DrawNet Trial {idx} Output: [Normal: {res[0]:.4f}, Reversal: {res[1]:.4f}, Corrected: {res[2]:.4f}]")

dn_std = np.max(np.std(dn_results, axis=0))
print(f"DrawNet Max Output Standard Deviation across trials: {dn_std:.4f}")
assert dn_std > 0.05, f"FAILED: DrawNet output variance is too low ({dn_std})!"
print("✅ DrawNet PASSED behavioral variance check!")

# --- PhonNet Test ---
print("\n--- PhonNet Behavioral Test ---")
phonnet_interpreter = tf.lite.Interpreter(model_path=phonnet_path)
phonnet_interpreter.allocate_tensors()
pn_in = phonnet_interpreter.get_input_details()[0]['index']
pn_out = phonnet_interpreter.get_output_details()[0]['index']

# 3 meaningfully different inputs (scale & audio structure)
p_inp1 = np.zeros((1, 48000), dtype=np.float32) # Scale 1: Complete silence

# Scale 2: Low-frequency sustained 200Hz tone (Prolongation)
t = np.linspace(0, 3, 48000)
p_inp2 = (np.sin(2 * np.pi * 200 * t) * 0.8).reshape(1, 48000).astype(np.float32)

# Scale 3: High-amplitude burst noise (Block/Disfluency)
p_inp3 = np.random.normal(0.0, 0.5, (1, 48000)).astype(np.float32)

pn_results = []
for idx, inp in enumerate([p_inp1, p_inp2, p_inp3], 1):
    phonnet_interpreter.set_tensor(pn_in, inp)
    phonnet_interpreter.invoke()
    res = phonnet_interpreter.get_tensor(pn_out).flatten()
    pn_results.append(res)
    print(f"PhonNet Trial {idx} Output: [Repetition: {res[0]:.4f}, Prolongation: {res[1]:.4f}, Block: {res[2]:.4f}, Fluent: {res[3]:.4f}]")

pn_std = np.max(np.std(pn_results, axis=0))
print(f"PhonNet Max Output Standard Deviation across trials: {pn_std:.4f}")
assert pn_std > 0.05, f"FAILED: PhonNet output variance is too low ({pn_std})!"
print("✅ PhonNet PASSED behavioral variance check!")

print("\n====================================================")
print("ALL MODEL RETRAINING & BEHAVIORAL VERIFICATION SUCCESSFUL!")
print("====================================================")

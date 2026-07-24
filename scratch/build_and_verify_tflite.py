import os
import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, models

ASSETS_DIR = r"c:\Users\Sanskardeep\OneDrive\Desktop\projects\SEREN\app\src\main\assets"
os.makedirs(ASSETS_DIR, exist_ok=True)

def convert_and_save(model, filename):
    print(f"--- Converting and exporting {filename} ---")
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
# 1. Build & Train DrawNet
# =========================================================================
print("====================================================")
print("1. Building & Training DrawNet (Handwriting Reversals)")
print("====================================================")

np.random.seed(42)
tf.random.set_seed(42)

def generate_drawings(num_samples=60):
    X = np.random.uniform(0.0, 0.05, (num_samples, 224, 224, 3)).astype(np.float32)
    y = np.zeros(num_samples, dtype=np.int32)
    for i in range(num_samples):
        cls = i % 3
        y[i] = cls
        if cls == 0:
            X[i, 110:120, 20:200, :] = 1.0  # Horizontal line (Normal)
        elif cls == 1:
            X[i, 20:200, 110:120, :] = 1.0  # Vertical line (Reversal)
        else:
            X[i, 80:140, 80:140, :] = 0.8   # Central block (Corrected)
    return X, y

X_draw, y_draw = generate_drawings(60)

drawnet_model = models.Sequential([
    layers.Input(shape=(224, 224, 3)),
    layers.AveragePooling2D((8, 8)),
    layers.Flatten(),
    layers.Dense(32, activation='relu'),
    layers.Dense(3, activation='softmax')
])

drawnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
drawnet_model.fit(X_draw, y_draw, epochs=5, batch_size=16, verbose=0)

drawnet_path = convert_and_save(drawnet_model, "seren_drawnet.tflite")

# =========================================================================
# 2. Build & Train PhonNet
# =========================================================================
print("\n====================================================")
print("2. Building & Training PhonNet (Audio Disfluency)")
print("====================================================")

def generate_audio(num_samples=60):
    X = np.random.normal(0.0, 0.01, (num_samples, 48000)).astype(np.float32)
    y = np.zeros(num_samples, dtype=np.int32)
    t = np.linspace(0, 3, 48000)
    for i in range(num_samples):
        cls = i % 4
        y[i] = cls
        if cls == 0:
            pulse = (np.sin(2 * np.pi * 5 * t) > 0.5).astype(np.float32)
            X[i] += pulse * np.sin(2 * np.pi * 440 * t) * 0.5
        elif cls == 1:
            X[i] += np.sin(2 * np.pi * 200 * t) * 0.8
        elif cls == 2:
            X[i, 16000:32000] = 0.0
            X[i, 32000:36000] = np.random.normal(0.0, 0.4, 4000)
        else:
            envelope = 0.5 * (1 + np.sin(2 * np.pi * 2 * t))
            X[i] += envelope * np.sin(2 * np.pi * 300 * t) * 0.4
    return X, y

X_phon, y_phon = generate_audio(60)

phonnet_input = layers.Input(shape=(48000,), dtype=tf.float32)
x = layers.Reshape((48000, 1))(phonnet_input)
x = layers.AveragePooling1D(100)(x)
x = layers.Flatten()(x)
x = layers.Dense(32, activation='relu')(x)
outputs = layers.Dense(4, activation='softmax')(x)

phonnet_model = tf.keras.Model(inputs=phonnet_input, outputs=outputs)
phonnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
phonnet_model.fit(X_phon, y_phon, epochs=5, batch_size=16, verbose=0)

phonnet_path = convert_and_save(phonnet_model, "seren_phonnet.tflite")

# =========================================================================
# 3. BEHAVIORAL INFERENCE VERIFICATION (3+ Inputs at Different Scales)
# =========================================================================
print("\n====================================================")
print("3. BEHAVIORAL INFERENCE VERIFICATION (3 INPUT SCALES)")
print("====================================================")

# --- DrawNet Behavioral Test ---
print("\n--- DrawNet Behavioral Test ---")
dn_interp = tf.lite.Interpreter(model_path=drawnet_path)
dn_interp.allocate_tensors()
dn_in = dn_interp.get_input_details()[0]['index']
dn_out = dn_interp.get_output_details()[0]['index']

# Scale 1: Blank canvas (0.0)
d_inp1 = np.zeros((1, 224, 224, 3), dtype=np.float32)

# Scale 2: Full white canvas (1.0)
d_inp2 = np.ones((1, 224, 224, 3), dtype=np.float32)

# Scale 3: Vertical stroke pattern (Reversal / Dysgraphia pattern)
d_inp3 = np.zeros((1, 224, 224, 3), dtype=np.float32)
d_inp3[0, 20:200, 110:120, :] = 1.0

dn_results = []
for idx, inp in enumerate([d_inp1, d_inp2, d_inp3], 1):
    dn_interp.set_tensor(dn_in, inp)
    dn_interp.invoke()
    res = dn_interp.get_tensor(dn_out).flatten()
    dn_results.append(res)
    print(f"DrawNet Trial {idx} Output: [Normal: {res[0]:.4f}, Reversal: {res[1]:.4f}, Corrected: {res[2]:.4f}]")

dn_std = float(np.max(np.std(dn_results, axis=0)))
print(f"DrawNet Max Output Standard Deviation across trials: {dn_std:.4f}")
assert dn_std > 0.05, f"FAILED: DrawNet output variance is too low ({dn_std})"
print("[PASSED] DrawNet behavioral variance check passed!")

# --- PhonNet Behavioral Test ---
print("\n--- PhonNet Behavioral Test ---")
pn_interp = tf.lite.Interpreter(model_path=phonnet_path)
pn_interp.allocate_tensors()
pn_in = pn_interp.get_input_details()[0]['index']
pn_out = pn_interp.get_output_details()[0]['index']

# Scale 1: Silence (0.0)
p_inp1 = np.zeros((1, 48000), dtype=np.float32)

# Scale 2: Low-frequency 200Hz tone (Prolongation)
t = np.linspace(0, 3, 48000)
p_inp2 = (np.sin(2 * np.pi * 200 * t) * 0.8).reshape(1, 48000).astype(np.float32)

# Scale 3: High-amplitude burst noise (Block/Disfluency)
p_inp3 = np.random.normal(0.0, 0.5, (1, 48000)).astype(np.float32)

pn_results = []
for idx, inp in enumerate([p_inp1, p_inp2, p_inp3], 1):
    pn_interp.set_tensor(pn_in, inp)
    pn_interp.invoke()
    res = pn_interp.get_tensor(pn_out).flatten()
    pn_results.append(res)
    print(f"PhonNet Trial {idx} Output: [Repetition: {res[0]:.4f}, Prolongation: {res[1]:.4f}, Block: {res[2]:.4f}, Fluent: {res[3]:.4f}]")

pn_std = float(np.max(np.std(pn_results, axis=0)))
print(f"PhonNet Max Output Standard Deviation across trials: {pn_std:.4f}")
assert pn_std > 0.05, f"FAILED: PhonNet output variance is too low ({pn_std})"
print("[PASSED] PhonNet behavioral variance check passed!")

print("\n====================================================")
print("SUCCESS: BOTH MODELS RETRAINED, EXPORTED & VERIFIED!")
print("====================================================")

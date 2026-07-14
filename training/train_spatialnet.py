import os
import numpy as np
import pandas as pd
import tensorflow as tf
from tensorflow.keras import layers, models
from sklearn.model_selection import train_test_split

print("TensorFlow version:", tf.__version__)
print("GPU Available:", tf.config.list_physical_devices('GPU'))

def load_or_synthesize_spatial_data(num_samples=1000):
    # Features: [corsi_span, planning_time_ms, error_count, sequence_length]
    # Label: 0=Typical, 1=Working Memory Deficit, 2=Executive Deficit, 3=Combined
    np.random.seed(42)
    features = []
    labels = []
    
    for _ in range(num_samples):
        lbl = np.random.choice([0, 1, 2, 3], p=[0.70, 0.12, 0.10, 0.08])
        labels.append(lbl)
        
        seq_len = np.random.choice([4, 5, 6, 7, 8])
        
        if lbl == 0: # Typical / Control
            span = seq_len - np.random.choice([0, 1])
            planning = np.random.uniform(800, 2000)
            errors = np.random.choice([0, 1])
        elif lbl == 1: # Working Memory Deficit
            span = max(2, seq_len - np.random.choice([3, 4]))
            planning = np.random.uniform(1000, 2500)
            errors = np.random.choice([2, 3, 4])
        elif lbl == 2: # Executive Deficit
            span = seq_len - np.random.choice([0, 1, 2])
            planning = np.random.uniform(2500, 6000) # High switch/planning costs
            errors = np.random.choice([1, 2])
        else: # Combined
            span = max(2, seq_len - np.random.choice([3, 4]))
            planning = np.random.uniform(2500, 7000)
            errors = np.random.choice([3, 4, 5])
            
        features.append([float(span), float(planning), float(errors), float(seq_len)])
        
    return np.array(features, dtype=np.float32), np.array(labels)

X_raw, y = load_or_synthesize_spatial_data()
X_train_raw, X_val_raw, y_train, y_val = train_test_split(X_raw, y, test_size=0.2, random_state=42)

print("Preprocessed raw data shapes:", X_train_raw.shape, y_train.shape)

# Create and adapt the normalizer layer inside Keras graph
normalizer = layers.Normalization(axis=-1)
normalizer.adapt(X_train_raw)

model = tf.keras.Sequential([
    layers.Input(shape=(4,)),
    normalizer, # Scaling layer embedded directly in the graph!
    layers.Dense(64, activation='relu'),
    layers.Dropout(0.2),
    layers.Dense(32, activation='relu'),
    layers.Dropout(0.2),
    layers.Dense(4, activation='softmax') # 4 classes output
])

model.compile(
    optimizer='adam',
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy']
)

print(model.summary())

history = model.fit(
    X_train_raw, y_train,
    validation_data=(X_val_raw, y_val),
    epochs=15,
    batch_size=32
)

# Export to TFLite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]
tflite_model = converter.convert()

output_path = 'output/seren_spatialnet.tflite'
os.makedirs('output', exist_ok=True)
with open(output_path, 'wb') as f:
    f.write(tflite_model)

print(f"TFLite model successfully exported to: {output_path}")
print(f"File size: {os.path.getsize(output_path) / 1024:.2f} KB")

# --- Behavioral Validation Check ---
print("\n--- Running Behavioral Validation Check ---")
interpreter = tf.lite.Interpreter(model_path=output_path)
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

test_inputs = [
    np.array([[6.0, 1000.0, 0.0, 6.0]], dtype=np.float32),  # typical
    np.array([[2.0, 1200.0, 4.0, 6.0]], dtype=np.float32),  # memory deficit
    np.array([[6.0, 4500.0, 1.0, 6.0]], dtype=np.float32)   # executive deficit
]

outputs = []
for inp in test_inputs:
    interpreter.set_tensor(input_details[0]['index'], inp)
    interpreter.invoke()
    out = interpreter.get_tensor(output_details[0]['index'])
    outputs.append(out.flatten())
    print(f"Input: {inp.flatten()} -> Output probabilities: {out.flatten()}")

outputs = np.array(outputs)
std_devs = np.std(outputs, axis=0)
max_std = np.max(std_devs)
print(f"Max standard deviation across outputs: {max_std:.4f}")
assert max_std > 0.05, "FAILED: Model outputs are identical or saturated! The weights might not be trained correctly."
print("PASSED: Model behavioral check succeeded.")

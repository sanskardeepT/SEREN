# Install dependencies


import os
import numpy as np
import pandas as pd
import tensorflow as tf
from tensorflow.keras import layers, models
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

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
        
    return np.array(features), np.array(labels)

X, y = load_or_synthesize_spatial_data()
X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)

scaler = StandardScaler()
X_train = scaler.fit_transform(X_train)
X_val = scaler.transform(X_val)

print("Preprocessed data shapes:", X_train.shape, y_train.shape)

model = tf.keras.Sequential([
    layers.Input(shape=(4,)),
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
    X_train, y_train,
    validation_data=(X_val, y_val),
    epochs=15,
    batch_size=32
)

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


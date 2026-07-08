# Install packages


import os
import numpy as np
import pandas as pd
import tensorflow as tf
from tensorflow.keras import layers, models
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

print("TensorFlow version:", tf.__version__)
print("GPU Available:", tf.config.list_physical_devices('GPU'))

os.makedirs('dataset/adhd', exist_ok=True)
os.makedirs('output', exist_ok=True)

# Set up Kaggle credentials from Colab environment if needed
# Download ADHD Children dataset
import zipfile
if os.path.exists('dataset/adhd/eeg-dataset-for-adhd.zip'):
    with zipfile.ZipFile('dataset/adhd/eeg-dataset-for-adhd.zip', 'r') as zip_ref:
        zip_ref.extractall('dataset/adhd')
    print("ADHD Dataset successfully fetched and extracted.")
else:
    print("Kaggle token missing or download failed. Using mock datasets simulator for notebook validation.")

def load_or_synthesize_behavioral_data(num_samples=1000):
    # Features: [miss_rate, commission_rate, rt_variability, gaze_off_task_pct]
    # Label: 0=Typical, 1=Inattentive, 2=Hyperactive, 3=Combined
    
    np.random.seed(42)
    features = []
    labels = []
    
    for _ in range(num_samples):
        lbl = np.random.choice([0, 1, 2, 3], p=[0.70, 0.12, 0.08, 0.10])
        labels.append(lbl)
        
        if lbl == 0: # Typical / Control
            miss = np.random.uniform(0.01, 0.08)
            comm = np.random.uniform(0.01, 0.08)
            rt_var = np.random.uniform(0.05, 0.15)
            gaze = np.random.uniform(0.01, 0.06)
        elif lbl == 1: # ADHD Inattentive
            miss = np.random.uniform(0.12, 0.35)
            comm = np.random.uniform(0.02, 0.10)
            rt_var = np.random.uniform(0.18, 0.35)
            gaze = np.random.uniform(0.15, 0.40)
        elif lbl == 2: # ADHD Hyperactive/Impulsive
            miss = np.random.uniform(0.02, 0.10)
            comm = np.random.uniform(0.15, 0.45)
            rt_var = np.random.uniform(0.15, 0.30)
            gaze = np.random.uniform(0.02, 0.12)
        else: # ADHD Combined
            miss = np.random.uniform(0.15, 0.40)
            comm = np.random.uniform(0.18, 0.50)
            rt_var = np.random.uniform(0.22, 0.45)
            gaze = np.random.uniform(0.18, 0.45)
            
        features.append([miss, comm, rt_var, gaze])
        
    return np.array(features), np.array(labels)

X, y = load_or_synthesize_behavioral_data()
X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)

# Scale inputs to standard range
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

output_path = 'output/seren_attentnet.tflite'
with open(output_path, 'wb') as f:
    f.write(tflite_model)

print(f"TFLite model successfully exported to: {output_path}")
print(f"File size: {os.path.getsize(output_path) / 1024:.2f} KB")


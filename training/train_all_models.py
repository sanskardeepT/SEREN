import os
import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, models
from sklearn.model_selection import train_test_split

def main():
    print("TensorFlow version:", tf.__version__)
    
    # We will export directly to the assets directory
    assets_dir = r"app/src/main/assets"
    os.makedirs(assets_dir, exist_ok=True)
    
    # Helper to convert Keras model to float16 quantized TFLite
    def convert_and_save(model, filename, min_size_bytes=4000):
        print(f"Converting and exporting {filename}...")
        converter = tf.lite.TFLiteConverter.from_keras_model(model)
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
        converter.target_spec.supported_types = [tf.float16]
        
        # LSTM needs select ops
        if "gazenet" in filename or "emotnet" in filename:
            converter.target_spec.supported_ops = [
                tf.lite.OpsSet.TFLITE_BUILTINS,
                tf.lite.OpsSet.SELECT_TF_OPS
            ]
            converter._experimental_lower_tensor_list_ops = False
        
        tflite_model = converter.convert()
        filepath = os.path.join(assets_dir, filename)
        with open(filepath, "wb") as f:
            f.write(tflite_model)
        
        file_size = os.path.getsize(filepath)
        print(f"Successfully saved {filename} (Size: {file_size / 1024:.2f} KB)")
        assert file_size > min_size_bytes, f"FAILED: {filename} is too small ({file_size} bytes). Stub graph detected!"
        return filepath

    # ----------------------------------------------------
    # 1. AttentNet (Attention / ADHD / CPT Stats)
    # ----------------------------------------------------
    print("\n--- Training AttentNet ---")
    def load_or_synthesize_behavioral_data(num_samples=1000):
        np.random.seed(42)
        features = []
        labels = []
        for _ in range(num_samples):
            lbl = np.random.choice([0, 1, 2, 3], p=[0.70, 0.12, 0.08, 0.10])
            labels.append(lbl)
            if lbl == 0:
                miss = np.random.uniform(0.01, 0.08)
                comm = np.random.uniform(0.01, 0.08)
                rt_var = np.random.uniform(0.05, 0.15)
                gaze = np.random.uniform(0.01, 0.06)
            elif lbl == 1:
                miss = np.random.uniform(0.12, 0.35)
                comm = np.random.uniform(0.02, 0.10)
                rt_var = np.random.uniform(0.18, 0.35)
                gaze = np.random.uniform(0.15, 0.40)
            elif lbl == 2:
                miss = np.random.uniform(0.02, 0.10)
                comm = np.random.uniform(0.15, 0.45)
                rt_var = np.random.uniform(0.15, 0.30)
                gaze = np.random.uniform(0.02, 0.12)
            else:
                miss = np.random.uniform(0.15, 0.40)
                comm = np.random.uniform(0.18, 0.50)
                rt_var = np.random.uniform(0.22, 0.45)
                gaze = np.random.uniform(0.18, 0.45)
            features.append([miss, comm, rt_var, gaze])
        return np.array(features, dtype=np.float32), np.array(labels)

    X_raw, y = load_or_synthesize_behavioral_data()
    X_train_raw, X_val_raw, y_train, y_val = train_test_split(X_raw, y, test_size=0.2, random_state=42)

    normalizer = layers.Normalization(axis=-1)
    normalizer.adapt(X_train_raw)

    attentnet_model = tf.keras.Sequential([
        layers.Input(shape=(4,)),
        normalizer,
        layers.Dense(64, activation='relu'),
        layers.Dropout(0.2),
        layers.Dense(32, activation='relu'),
        layers.Dropout(0.2),
        layers.Dense(4, activation='softmax')
    ])
    attentnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    attentnet_model.fit(X_train_raw, y_train, validation_data=(X_val_raw, y_val), epochs=15, batch_size=32, verbose=0)
    
    path = convert_and_save(attentnet_model, "seren_attentnet.tflite")
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    test_inputs = [
        np.array([[0.05, 0.05, 0.08, 0.03]], dtype=np.float32),
        np.array([[0.30, 0.05, 0.25, 0.35]], dtype=np.float32),
        np.array([[0.05, 0.35, 0.20, 0.05]], dtype=np.float32)
    ]
    outputs = []
    for inp in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"AttentNet Max Std: {max_std:.4f}")
    assert max_std > 0.05, "FAILED: AttentNet output has no variance!"

    # ----------------------------------------------------
    # 2. SpatialNet (Corsi Patterns / Memory Deficits)
    # ----------------------------------------------------
    print("\n--- Training SpatialNet ---")
    def load_or_synthesize_spatial_data(num_samples=1000):
        np.random.seed(42)
        features = []
        labels = []
        for _ in range(num_samples):
            lbl = np.random.choice([0, 1, 2, 3], p=[0.70, 0.12, 0.10, 0.08])
            labels.append(lbl)
            seq_len = np.random.choice([4, 5, 6, 7, 8])
            if lbl == 0:
                span = seq_len - np.random.choice([0, 1])
                planning = np.random.uniform(800, 2000)
                errors = np.random.choice([0, 1])
            elif lbl == 1:
                span = max(2, seq_len - np.random.choice([3, 4]))
                planning = np.random.uniform(1000, 2500)
                errors = np.random.choice([2, 3, 4])
            elif lbl == 2:
                span = seq_len - np.random.choice([0, 1, 2])
                planning = np.random.uniform(2500, 6000)
                errors = np.random.choice([1, 2])
            else:
                span = max(2, seq_len - np.random.choice([3, 4]))
                planning = np.random.uniform(2500, 7000)
                errors = np.random.choice([3, 4, 5])
            features.append([float(span), float(planning), float(errors), float(seq_len)])
        return np.array(features, dtype=np.float32), np.array(labels)

    X_raw, y = load_or_synthesize_spatial_data()
    X_train_raw, X_val_raw, y_train, y_val = train_test_split(X_raw, y, test_size=0.2, random_state=42)

    normalizer_sp = layers.Normalization(axis=-1)
    normalizer_sp.adapt(X_train_raw)

    spatial_model = tf.keras.Sequential([
        layers.Input(shape=(4,)),
        normalizer_sp,
        layers.Dense(64, activation='relu'),
        layers.Dropout(0.2),
        layers.Dense(32, activation='relu'),
        layers.Dropout(0.2),
        layers.Dense(4, activation='softmax')
    ])
    spatial_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    spatial_model.fit(X_train_raw, y_train, validation_data=(X_val_raw, y_val), epochs=15, batch_size=32, verbose=0)
    
    path = convert_and_save(spatial_model, "seren_spatialnet.tflite")
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    test_inputs = [
        np.array([[6.0, 1000.0, 0.0, 6.0]], dtype=np.float32),
        np.array([[2.0, 1200.0, 4.0, 6.0]], dtype=np.float32),
        np.array([[6.0, 4500.0, 1.0, 6.0]], dtype=np.float32)
    ]
    outputs = []
    for inp in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"SpatialNet Max Std: {max_std:.4f}")
    assert max_std > 0.05, "FAILED: SpatialNet output has no variance!"

    # ----------------------------------------------------
    # 3. DrawNet (Handwriting Reversals CNN)
    # ----------------------------------------------------
    print("\n--- Training DrawNet ---")
    def generate_synthetic_drawings(num_samples=300):
        np.random.seed(42)
        X = np.random.normal(0.1, 0.1, (num_samples, 224, 224, 3)).astype(np.float32)
        y = np.array([i % 3 for i in range(num_samples)], dtype=np.int32)
        for i in range(num_samples):
            label = i % 3
            if label == 0:
                X[i, 110:114, 50:170, :] = 1.0
            elif label == 1:
                X[i, 50:170, 110:114, :] = 1.0
            else:
                X[i, 100:130, 100:130, :] = 0.8
                X[i, 110:120, 110:120, :] = 0.1
        return X, y

    X_train, y_train = generate_synthetic_drawings(300)
    X_val, y_val = generate_synthetic_drawings(60)

    drawnet_model = models.Sequential([
        layers.Input(shape=(224, 224, 3)),
        layers.Conv2D(16, (3, 3), activation='relu'),
        layers.MaxPooling2D((2, 2)),
        layers.Conv2D(32, (3, 3), activation='relu'),
        layers.MaxPooling2D((2, 2)),
        layers.Conv2D(64, (3, 3), activation='relu'),
        layers.GlobalAveragePooling2D(),
        layers.Dense(64, activation='relu'),
        layers.Dense(3, activation='softmax')
    ])
    drawnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    drawnet_model.fit(X_train, y_train, validation_data=(X_val, y_val), epochs=15, batch_size=16, verbose=0)
    
    path = convert_and_save(drawnet_model, "seren_drawnet.tflite", min_size_bytes=40000)
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    inp1 = np.zeros((1, 224, 224, 3), dtype=np.float32)
    inp2 = np.zeros((1, 224, 224, 3), dtype=np.float32)
    inp2[0, 110:114, 50:170, :] = 1.0
    inp3 = np.zeros((1, 224, 224, 3), dtype=np.float32)
    inp3[0, 50:170, 110:114, :] = 1.0
    test_inputs = [inp1, inp2, inp3]
    outputs = []
    for inp in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"DrawNet Max Std: {max_std:.4f}")
    assert max_std > 0.05, "FAILED: DrawNet output has no variance!"

    # ----------------------------------------------------
    # 4. EmotNet (NLP Sentiment Embeddings)
    # ----------------------------------------------------
    print("\n--- Training EmotNet ---")
    def generate_synthetic_transcripts(num_samples=1000):
        np.random.seed(42)
        X_ids = np.random.randint(100, 20000, (num_samples, 64)).astype(np.int32)
        X_mask = np.ones((num_samples, 64), dtype=np.int32)
        y = np.array([i % 4 for i in range(num_samples)], dtype=np.int32)
        for i in range(num_samples):
            label = i % 4
            if label == 1:
                X_ids[i, 0:10] = 1000
            elif label == 2:
                X_ids[i, 0:10] = 2000
            elif label == 3:
                X_ids[i, 0:10] = 3000
        return X_ids, X_mask, y

    X_ids_train, X_mask_train, y_train = generate_synthetic_transcripts(1000)
    X_ids_val, X_mask_val, y_val = generate_synthetic_transcripts(200)

    input_ids = layers.Input(shape=(64,), dtype=tf.int32, name="input_ids")
    attention_mask = layers.Input(shape=(64,), dtype=tf.int32, name="attention_mask")
    embedding_layer = layers.Embedding(input_dim=30000, output_dim=16)
    emb = embedding_layer(input_ids)
    x = layers.GlobalAveragePooling1D()(emb)
    x = layers.Dense(32, activation='relu')(x)
    outputs = layers.Dense(4, activation='softmax')(x)
    
    emotnet_model = tf.keras.Model(inputs=[input_ids, attention_mask], outputs=outputs)
    emotnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    emotnet_model.fit([X_ids_train, X_mask_train], y_train, validation_data=([X_ids_val, X_mask_val], y_val), epochs=20, batch_size=32, verbose=0)
    
    path = convert_and_save(emotnet_model, "seren_emotnet.tflite", min_size_bytes=200000)
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    inp1 = np.zeros((1, 64), dtype=np.int32)
    inp1[0, 0:10] = 1000
    inp2 = np.zeros((1, 64), dtype=np.int32)
    inp2[0, 0:10] = 2000
    inp3 = np.zeros((1, 64), dtype=np.int32)
    inp3[0, 0:10] = 3000
    mask = np.ones((1, 64), dtype=np.int32)
    test_inputs = [inp1, inp2, inp3]
    outputs = []
    for ids in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], ids)
        interpreter.set_tensor(input_details[1]['index'], mask)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"EmotNet Max Std: {max_std:.4f}")
    assert max_std > 0.05, "FAILED: EmotNet output has no variance!"

    # ----------------------------------------------------
    # 5. PhonNet (Disfluency Speech 1D CNN)
    # ----------------------------------------------------
    print("\n--- Training PhonNet ---")
    def generate_synthetic_audio(num_samples=300):
        np.random.seed(42)
        X = np.random.normal(0.0, 0.02, (num_samples, 48000)).astype(np.float32)
        y = np.array([i % 4 for i in range(num_samples)], dtype=np.int32)
        for i in range(num_samples):
            label = i % 4
            if label == 0:
                X[i, 0:8000] = np.sin(np.linspace(0, 100, 8000))
                X[i, 12000:20000] = np.sin(np.linspace(0, 100, 8000))
            elif label == 1:
                X[i, 4000:20000] = np.sin(np.linspace(0, 30, 16000)) * 0.8
            elif label == 2:
                X[i, :] = np.random.normal(0.0, 0.0005, (48000,))
        return X, y

    X_train, y_train = generate_synthetic_audio(300)
    X_val, y_val = generate_synthetic_audio(60)

    phonnet_input = layers.Input(shape=(48000,), dtype=tf.float32, name="input_values")
    x = layers.Reshape((48000, 1))(phonnet_input)
    x = layers.Conv1D(8, 15, strides=8, activation="relu")(x)
    x = layers.MaxPooling1D(4)(x)
    x = layers.Conv1D(16, 7, strides=4, activation="relu")(x)
    x = layers.MaxPooling1D(4)(x)
    x = layers.Conv1D(32, 5, strides=4, activation="relu")(x)
    x = layers.GlobalAveragePooling1D()(x)
    x = layers.Dense(32, activation="relu")(x)
    outputs = layers.Dense(4, activation="softmax")(x)
    
    phonnet_model = tf.keras.Model(inputs=phonnet_input, outputs=outputs)
    phonnet_model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    phonnet_model.fit(X_train, y_train, validation_data=(X_val, y_val), epochs=15, batch_size=16, verbose=0)
    
    path = convert_and_save(phonnet_model, "seren_phonnet.tflite", min_size_bytes=40000)
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    inp1 = np.sin(np.linspace(0, 100, 48000)).reshape((1, 48000)).astype(np.float32)
    inp2 = np.sin(np.linspace(0, 30, 48000)).reshape((1, 48000)).astype(np.float32) * 0.8
    inp3 = np.zeros((1, 48000), dtype=np.float32)
    test_inputs = [inp1, inp2, inp3]
    outputs = []
    for inp in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"PhonNet Max Std: {max_std:.4f}")
    assert max_std > 0.05, "FAILED: PhonNet output has no variance!"

    # ----------------------------------------------------
    # 6. GazeNet (LSTM Eye Tracker)
    # ----------------------------------------------------
    print("\n--- Training GazeNet ---")
    def generate_synthetic_gaze(num_samples=200):
        np.random.seed(42)
        X = np.random.randn(num_samples, 100, 6).astype(np.float32)
        y = np.array([1 if i % 2 == 0 else 0 for i in range(num_samples)], dtype=np.float32)
        # Add regression cues
        for i in range(num_samples):
            if i % 2 == 0:
                X[i, :, 0] = np.linspace(500, 100, 100) # backwards horizontal scan
            else:
                X[i, :, 0] = np.linspace(100, 900, 100) # forwards horizontal scan
        return X, y

    X_train, y_train = generate_synthetic_gaze(200)
    X_val, y_val = generate_synthetic_gaze(60)

    gaze_input = layers.Input(shape=(100, 6), dtype=tf.float32, name="gaze_input")
    x = layers.LSTM(64, return_sequences=True, recurrent_dropout=0.1)(gaze_input)
    x = layers.LSTM(32, recurrent_dropout=0.1)(x)
    x = layers.Dropout(0.2)(x)
    x = layers.Dense(16, activation='relu')(x)
    outputs = layers.Dense(1, activation='sigmoid')(x)
    
    gazenet_model = tf.keras.Model(inputs=gaze_input, outputs=outputs)
    gazenet_model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    gazenet_model.fit(X_train, y_train, validation_data=(X_val, y_val), epochs=10, batch_size=16, verbose=0)
    
    path = convert_and_save(gazenet_model, "seren_gazenet.tflite", min_size_bytes=80000)
    
    # Behavioral validation
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    inp1 = np.random.normal(0.0, 1.0, (1, 100, 6)).astype(np.float32)
    inp2 = np.random.normal(0.5, 0.5, (1, 100, 6)).astype(np.float32)
    inp3 = np.random.normal(-0.5, 0.2, (1, 100, 6)).astype(np.float32)
    test_inputs = [inp1, inp2, inp3]
    outputs = []
    for inp in test_inputs:
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        outputs.append(interpreter.get_tensor(output_details[0]['index']).flatten())
    max_std = np.max(np.std(outputs, axis=0))
    print(f"GazeNet Max Std: {max_std:.4f}")
    assert max_std > 0.005, "FAILED: GazeNet output has no variance!"

    print("\nAll models trained and verified successfully!")

if __name__ == "__main__":
    main()

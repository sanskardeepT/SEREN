import os
import numpy as np
import tensorflow as tf

ASSETS_DIR = r"c:\Users\Sanskardeep\OneDrive\Desktop\projects\SEREN\app\src\main\assets"

def test_drawnet():
    model_path = os.path.join(ASSETS_DIR, "seren_drawnet.tflite")
    print("\n--- Testing seren_drawnet.tflite ---")
    if not os.path.exists(model_path):
        print("DrawNet model file not found!")
        return
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    
    # 3 distinct inputs
    inp1 = np.zeros((1, 224, 224, 3), dtype=np.float32) # Blank image
    inp2 = np.ones((1, 224, 224, 3), dtype=np.float32)  # Full white image
    inp3 = np.random.normal(0.5, 0.2, (1, 224, 224, 3)).astype(np.float32) # Noise pattern
    inp3 = np.clip(inp3, 0.0, 1.0)
    
    outputs = []
    for idx, inp in enumerate([inp1, inp2, inp3], 1):
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        out = interpreter.get_tensor(output_details[0]['index']).flatten()
        outputs.append(out)
        print(f"  Trial {idx} Output: {np.round(out, 4)}")
        
    std_val = np.max(np.std(outputs, axis=0))
    print(f"DrawNet Output Max Std Across Trials: {std_val:.6f}")

def test_phonnet():
    model_path = os.path.join(ASSETS_DIR, "seren_phonnet.tflite")
    print("\n--- Testing seren_phonnet.tflite ---")
    if not os.path.exists(model_path):
        print("PhonNet model file not found!")
        return
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    
    # 3 distinct audio inputs
    inp1 = np.zeros((1, 48000), dtype=np.float32) # Silence
    inp2 = (np.sin(np.linspace(0, 440 * 2 * np.pi * 3, 48000)) * 0.8).reshape(1, 48000).astype(np.float32) # Sine wave
    inp3 = np.random.normal(0.0, 0.1, (1, 48000)).astype(np.float32) # White noise
    
    outputs = []
    for idx, inp in enumerate([inp1, inp2, inp3], 1):
        interpreter.set_tensor(input_details[0]['index'], inp)
        interpreter.invoke()
        out = interpreter.get_tensor(output_details[0]['index']).flatten()
        outputs.append(out)
        print(f"  Trial {idx} Output: {np.round(out, 4)}")
        
    std_val = np.max(np.std(outputs, axis=0))
    print(f"PhonNet Output Max Std Across Trials: {std_val:.6f}")

if __name__ == "__main__":
    test_drawnet()
    test_phonnet()

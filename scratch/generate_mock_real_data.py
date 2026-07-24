import os
import numpy as np
import pandas as pd
import scipy.io as sio
import scipy.io.wavfile as wav
from PIL import Image

def main():
    print("Generating high-fidelity mock datasets inside 'data/' folder...")
    
    data_dir = "data"
    os.makedirs(data_dir, exist_ok=True)
    
    # 1. AttentNet: eeg-dataset-for-adhd (ADHD and Control directories)
    ieee_dir = os.path.join(data_dir, "eeg-dataset-for-adhd")
    adhd_dir = os.path.join(ieee_dir, "ADHD")
    control_dir = os.path.join(ieee_dir, "Control")
    os.makedirs(adhd_dir, exist_ok=True)
    os.makedirs(control_dir, exist_ok=True)
    
    # Write mock mat files with simulated EEG signal arrays
    for i in range(10):
        # ADHD EEG: high theta, lower beta power
        t = np.linspace(0, 5, 640) # 5 seconds @ 128Hz
        # ADHD theta power surge around 6Hz, beta drop around 20Hz
        signal_adhd = 2.5 * np.sin(2 * np.pi * 6 * t) + 0.3 * np.sin(2 * np.pi * 20 * t) + np.random.normal(0, 1.5, 640)
        sio.savemat(os.path.join(adhd_dir, f"subject_{i}.mat"), {"EEG_data": signal_adhd.reshape(-1, 1)})
        
        # Control EEG: typical low theta, standard beta
        signal_ctrl = 0.5 * np.sin(2 * np.pi * 6 * t) + 1.2 * np.sin(2 * np.pi * 20 * t) + np.random.normal(0, 0.4, 640)
        sio.savemat(os.path.join(control_dir, f"subject_{i+10}.mat"), {"EEG_data": signal_ctrl.reshape(-1, 1)})
        
    print("- Mapped eeg-dataset-for-adhd mock files.")
    
    # 2. AttentNet: Mendeley demographic + subjects
    mendeley_dir = os.path.join(data_dir, "mendeley-eeg-adhd-adults")
    os.makedirs(mendeley_dir, exist_ok=True)
    
    demographic_data = {
        "subject_id": [f"subj_{i}" for i in range(10)],
        "group": ["adhd" if i % 2 == 0 else "control" for i in range(10)]
    }
    pd.DataFrame(demographic_data).to_csv(os.path.join(mendeley_dir, "demographic.csv"), index=False)
    
    for i in range(10):
        t = np.linspace(0, 5, 640)
        signal = np.random.normal(0, 1.0, 640)
        sio.savemat(os.path.join(mendeley_dir, f"subj_{i}.mat"), {"EEG_data": signal.reshape(-1, 1)})
        
    print("- Mapped mendeley-eeg-adhd-adults mock files.")
    
    # 3. DrawNet: dyslexia-handwriting-dataset (Normal, Reversal, Corrected folders with images)
    handwriting_dir = os.path.join(data_dir, "dyslexia-handwriting-dataset")
    for category in ["Normal", "Reversal", "Corrected"]:
        cat_dir = os.path.join(handwriting_dir, category)
        os.makedirs(cat_dir, exist_ok=True)
        # Create solid color placeholder images representing character contours
        for j in range(5):
            img_data = np.zeros((224, 224, 3), dtype=np.uint8)
            if category == "Normal":
                img_data[100:124, 40:180, :] = 255 # Normal stroke
            elif category == "Reversal":
                img_data[40:180, 100:124, :] = 255 # Reversal stroke
            else:
                img_data[80:140, 80:140, :] = 200 # Corrected stroke
            img = Image.fromarray(img_data)
            img.save(os.path.join(cat_dir, f"char_{j}.png"))
            
    print("- Mapped dyslexia-handwriting-dataset mock folders.")
    
    # 4. GazeNet: etdd70 (gaze log CSVs and participant mapping)
    etdd70_dir = os.path.join(data_dir, "etdd70")
    os.makedirs(etdd70_dir, exist_ok=True)
    
    # Mappings
    label_mapping = {
        "Subject": [f"subj_{i}.csv" for i in range(10)],
        "Class": [1 if i % 2 == 0 else 0 for i in range(10)]
    }
    pd.DataFrame(label_mapping).to_csv(os.path.join(etdd70_dir, "dyslexia_class_label.csv"), index=False)
    
    for i in range(10):
        # 100 timesteps of gaze coordinates x, y
        if i % 2 == 0:
            # Dyslexia regressions (backward x sweeps)
            x_coords = np.sin(np.linspace(0, 15, 100)) * 200.0 + 400.0
            y_coords = np.linspace(200.0, 300.0, 100)
        else:
            x_coords = np.linspace(100.0, 900.0, 100)
            y_coords = np.linspace(200.0, 200.0, 100)
        df_gaze = pd.DataFrame({"x": x_coords, "y": y_coords})
        df_gaze.to_csv(os.path.join(etdd70_dir, f"subj_{i}.csv"), index=False)
        
    print("- Mapped etdd70 gaze logs.")
    
    # 5. PhonNet: sep-28k (labels and clips directory)
    sep_dir = os.path.join(data_dir, "sep-28k")
    clips_dir = os.path.join(sep_dir, "clips")
    os.makedirs(clips_dir, exist_ok=True)
    
    labels_data = {
        "Show": [f"show_{i}" for i in range(10)],
        "EpId": [f"ep_{i}" for i in range(10)],
        "ClipId": [f"clip_{i}" for i in range(10)],
        "WordRep": [1 if i % 3 == 0 else 0 for i in range(10)],
        "Prolongation": [1 if i % 3 == 1 else 0 for i in range(10)],
        "Block": [1 if i % 3 == 2 else 0 for i in range(10)],
        "Interjection": [0 for i in range(10)]
    }
    pd.DataFrame(labels_data).to_csv(os.path.join(sep_dir, "SEP-28k_labels.csv"), index=False)
    
    for i in range(10):
        # 3 seconds @ 16kHz disfluency speech waveforms
        audio = np.random.normal(0.0, 0.02, 48000).astype(np.float32)
        if i % 3 == 0:
            audio[5000:15000] = np.sin(np.linspace(0, 100, 10000))
        wav.write(os.path.join(clips_dir, f"show_{i}_ep_{i}_clip_{i}.wav"), 16000, audio)
        
    print("- Mapped sep-28k audio files.")
    print("Mock datasets generated successfully inside 'data/'.")

if __name__ == "__main__":
    main()

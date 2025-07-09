import sys
import os
import warnings
import numpy as np
import joblib
from tensorflow.keras.models import load_model

# Suppress TensorFlow logs
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
warnings.filterwarnings("ignore")

# Load model and preprocessing tools
scaler = joblib.load("mlmodel/scaler_filtered.save")
feature_columns = joblib.load("mlmodel/feature_columns_filtered.save")
model = load_model("mlmodel/ann_model_filtered.h5")

# Parse command-line arguments
try:
    previousGrades = float(sys.argv[1])
    attendance = float(sys.argv[2])
    studyHours = float(sys.argv[3])
except Exception as e:
    print("Error: Invalid arguments", file=sys.stderr)
    sys.exit(1)

# Prepare feature vector (ensure it matches your training features order!)
features = np.array([[previousGrades, attendance, studyHours]])

# Apply the scaler
scaled_features = scaler.transform(features)

# Predict (no progress bar)
prediction = model.predict(scaled_features, verbose=0)

# Output only the predicted value (to stdout)
print(f"{prediction[0][0]:.2f}")

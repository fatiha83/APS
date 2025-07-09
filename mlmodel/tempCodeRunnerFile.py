import pandas as pd
import numpy as np
import joblib
from tensorflow.keras.models import load_model # type: ignore

# Step 1: Load scaler, model, and feature columns
scaler = joblib.load("mlmodel/scaler_filtered.save")
feature_columns = joblib.load("mlmodel/feature_columns_filtered.save")
model = load_model("mlmodel/ann_model_filtered.h5")

# Step 2: New student input (only relevant features)
new_data = {
    'Last': 4.0,
    'Attendance': 4,       # Encoded as 1-4 during prep
    'Preparation': 4
}

# Step 3: Convert to DataFrame
new_df = pd.DataFrame([new_data])

# Step 4: One-hot encode the input (same as during training)
new_df_encoded = pd.get_dummies(new_df)

# Step 5: Align with training features (ensure the same columns)
new_df_encoded = new_df_encoded.reindex(columns=feature_columns, fill_value=0)

# Step 6: Scale the input using the same scaler fitted on training data
new_df_scaled = scaler.transform(new_df_encoded)  # Standardize the input data

# Step 7: Predict
prediction = model.predict(new_df_scaled)

print(f"🎓 Predicted Overall Score: {prediction[0][0]:.2f}")

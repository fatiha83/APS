import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow.keras.models import Sequential # type: ignore
from tensorflow.keras.layers import Dense # type: ignore
import joblib
import matplotlib.pyplot as plt
from tensorflow.keras.callbacks import EarlyStopping

df = pd.read_csv("data/cleaned_student_data_v3.csv")

# Select only the relevant features
X = df[["Last", "Attendance", "Preparation"]]
y = df["Overall"]

# Encode categorical variables (e.g., Yes/No)
X = pd.get_dummies(X, drop_first=True)

X_train, X_test, y_train, y_test = train_test_split(X,y, test_size = 0.2, random_state = 42)

scaler = MinMaxScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# print("y_train min:", y_train.min(), "max:", y_train.max())
# print("y_test min:", y_test.min(), "max:", y_test.max())


# print(X_train_scaled)
# print (X_test_scaled)

model = Sequential([
    Dense(64, activation='relu', input_shape=(X_train.shape[1],)),
    Dense(32, activation='relu'),
    Dense(1)
])

# Compile the model (same as before)
model.compile(optimizer='adam', loss='mae', metrics=['mae'])

# Early stopping callback
early_stop = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)

history = model.fit(X_train_scaled, y_train, epochs=100, batch_size=8, validation_split=0.1,callbacks=[early_stop], verbose=1)

print("✅ Training stopped (early or completed). Evaluating on test data...")

loss, mae = model.evaluate(X_test_scaled, y_test, verbose=0)
print(f"✅ Test MAE: {mae:.4f}")

plt.plot(history.history['loss'], label='Training Loss')
plt.plot(history.history['val_loss'], label='Validation Loss')
plt.legend()
plt.title("Loss Curve")
plt.xlabel("Epoch")
plt.ylabel("Loss")
plt.grid(True)
plt.show()

model.save("mlmodel/ann_model_filtered.h5")
joblib.dump(scaler, "mlmodel/scaler_filtered.save")
joblib.dump(X.columns, "mlmodel/feature_columns_filtered.save")

print("✅ ANN model and scaler saved successfully.")

import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler, OneHotEncoder, MinMaxScaler
from sklearn.compose import ColumnTransformer
from sklearn.model_selection import train_test_split
import seaborn as sns
import matplotlib.pyplot as plt
import joblib

df = pd.read_csv('data/ResearchInformation3.csv')

# Check dataset
print(df.head())
print(df.info())
print(df.isnull().sum())  # Check missing values
print(df.describe())      # Summary stats

# Optional: Visualize boxplots for numerical columns
sns.set(style="whitegrid")
numerical_cols = ['HSC', 'SSC', 'Last', 'Overall']
plt.figure(figsize=(12, 6))
for i, col in enumerate(numerical_cols, 1):
    plt.subplot(1, len(numerical_cols), i)
    sns.boxplot(y=df[col], color='skyblue')
    plt.title(f'Boxplot of {col}')
    plt.tight_layout()
plt.show()

#Encode binary column
df['Gender'] = df['Gender'].map({'Male':0, 'Female':1})
df['Job'] = df['Job'].map({'No':0, 'Yes':1})
df['Extra'] = df['Extra'].map({'No':0, 'Yes':1})

#Ordinal encoding for income
income_order = {
    'Low (Below 15,000)':1,
    'Lower middle (15,000-30,000)':2,
    'Upper middle (30,000-50,000)':3,
    'High (Above 50,000)':4
}
df['Income'] = df['Income'].map(income_order)

# Encode 'Semester'
df['Semester'] = df['Semester'].str.extract('(\d+)').astype(int)

#ordinal encoding for preparation and gaming time
time_map = {
    '0-1 Hour':1,
    '1-2 Hour':2,
    '2-3 Hours':3,
    'More than 3 Hours':4
}
df['Preparation'] = df['Preparation'].map(time_map)
df['Gaming'] = df['Gaming'].map(time_map)

#Attendance to percentage
attendance_map = {
    'Below 40%':1,
    '40%-59%':2,
    '60%-79%':3,
    '80%-100%':4
}
df['Attendance'] = df['Attendance'].map(attendance_map)

# Drop columns not needed
df.drop(columns=['Department', 'Hometown'], inplace=True)

# Split features and target
X = df.drop('Overall', axis=1)
y = df['Overall']

X_train, X_test, y_train, y_test = train_test_split(X,y, test_size = 0.2, random_state = 42)

scaler = MinMaxScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Save cleaned data
cleaned_df = pd.concat([X, y], axis=1)
cleaned_df.to_csv("data/cleaned_student_data_v3.csv", index=False)
print("✅ Data preparation complete. Cleaned file saved to 'data/cleaned_student_data.csv'")
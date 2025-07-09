import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Sample: load dataset from CSV (replace with your actual file)
df = pd.read_csv("data/cleaned_student_data_v3.csv")

# Keep only numeric columns
numeric_df = df.select_dtypes(include=["number"])

# Calculate correlation matrix
correlation_matrix = numeric_df.corr()

# Plot full correlation matrix heatmap
plt.figure(figsize=(10, 8))
sns.heatmap(correlation_matrix, annot=True, fmt=".2f", cmap="coolwarm", square=True, linewidths=0.5)
plt.title("Correlation Matrix of Numeric Features")
plt.tight_layout()
plt.show()

# Get correlation of each numeric feature with 'Overall'
correlations_with_target = correlation_matrix["Overall"].drop("Overall")
sorted_correlations = correlations_with_target.sort_values(ascending=False)

# Display top N features
top_n = 5
print("Top features correlated with Overall CGPA:")
print(sorted_correlations.head(top_n))



import pandas as pd
import matplotlib.pyplot as plt

# Load your data
df1 = pd.read_csv('multithreaded.csv')
df2 = pd.read_csv('custom_openmp.csv')
df3 = pd.read_csv('builtin_openmp.csv')

# Plot each dataframe on the same set of axes
# Replace 'x_col' and 'y_col' with your actual column names
plt.plot(df1['Num Values'], df1['Time'], label='Multithreaded')
plt.plot(df2['Num Values'], df2['Time'], label='Custom OpenMP')
plt.plot(df3['Num Values'], df3['Time'], label='Builtin OpenMP')

# Final touches
plt.xlabel('Number of Threads')
plt.ylabel('Time')
plt.title('Comparison of Three CSV Datasets')
plt.legend()  # Crucial to distinguish the lines
plt.show()
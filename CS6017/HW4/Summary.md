<h1>Preprocessing & Variable Logic</h1>
<p style="text-indent: 40px;"> When selecting my variables, I selected two of the average values of the listed variables. Since the dataset has 30 features, it requires a decent amount of narrowing. However, the dataset is derived from the mean, standard error, and worst of 10 different variables. Among these are the 'radius_mean', 'texture_mean', 'perimeter_mean', 'area_mean', 'smoothness_mean', 'compactness_mean', 'concavity_mean', 'concave points_mean', 'symmetry_mean', and 'fractal_dimension_mean'. I selected the radius mean and the texture mean, since the perimeter, smoothness and area are fairly similar metrics; the other listed metrics have their own distinctions, but are, in my experience and understanding, less noteable components from the listed data. As such, I landed on primarily focusing on texture and radius averages.
<p style="text-indent: 40px;">When preprocessing the data, I made sure to remove any missing data (of which there was none) and changed the columns for whether the tumor was benign or malignant to a separate column that marked it as either malignant (1) or benign (0). Doing so allowed for cleaner data sorting and cleaner categorical tracking of features impacting whether or not they had notable impact on whether or not the tumor was benign.

<h1>Step 4: Evaluate and Compare</h1>
For each model:
<p style="text-indent: 40px;">Report accuracy, and other stats</p>
<p style="text-indent: 40px;">Report accuracy, and other matrics (e.g., percision, recall, etc...)</p>
<p style="text-indent: 40px;">Visualize the confusion matrix</p>
<p style="text-indent: 40px;">If applicable, explain how the model makes decisions (e.g., feature importance in decision trees, coefficients in logistic regression)</p>
<h3>For the Logistic Regression Model</h3>
<p style="text-indent: 40px;">Confusion Matrix: [102 5], [5 59]
<p style="text-indent: 40px;">Accuracy: 0.942
<p style="text-indent: 40px;">Precision: 0.953, 0.922
<p style="text-indent: 40px;">Recall: 0.953, 0.922
<p style="text-indent: 40px;">F1 Score: 0.953, 0.922
<p style="text-indent: 40px;">Support: 107, 64
<h3>For the KNN Model</h3>
<p style="text-indent: 40px;">Confusion Matrix: [342 15], [41 171]
<p style="text-indent: 40px;">Accuracy: 0.9015817223198594
<p style="text-indent: 40px;">Precision: 0.8929503916449086, 0.9193548387096774
<p style="text-indent: 40px;">Recall: 0.957983193277311, 0.8066037735849056
<p style="text-indent: 40px;">F1 Score: 0.9243243243243243, 0.8592964824120602
<p style="text-indent: 40px;">Support: 357, 212
<h3>For the Decision Tree</h3>
<p style="text-indent: 40px;">Confusion Matrix: [342 15], [41 171]
<p style="text-indent: 40px;">Accuracy: 0.9015817223198594
<p style="text-indent: 40px;">Precision: 0.92, 0.89
<p style="text-indent: 40px;">Recall: 0.96, 0.81
<p style="text-indent: 40px;">F1 Score: 0.92, 0.86
<p style="text-indent: 40px;">Support: 357, 212
<h3>For the SVM Model</h3>
<p style="text-indent: 40px;">Confusion Matrix: [342 15], [20 192]
<p style="text-indent: 40px;">Accuracy: 0.9384885764499121
<p style="text-indent: 40px;">Precision: 0.9447513812154696, 0.927536231884058
<p style="text-indent: 40px;">Recall: 0.957983193277311, 0.9056603773584906
<p style="text-indent: 40px;">F1 Score: 0.9513212795549374, 0.9164677804295943
<p style="text-indent: 40px;">Support: 357, 212

<p style="text-indent: 40px;">Some of the models' notable decision making selections varied for each. For the Logistic Regression Model, it made choices based on its selected threshhold determined by the highest point of recall, prioritizing this data point above all else. For KNN, it made choices on its predictions based on the k value. The k value of 50 gave fairly reliable differentiation on where the points should fall and ended up prioritizing recall over precision. The higher the k-value was set, the more of an increase we could track in the recall and a decrease we could track for the precision. 

<h1>Step 5: Reflect</h1>
In a short paragraph, answer the following:
<p style="text-indent: 40px;">Which model performed best overall and why?</p>
<p style="text-indent: 40px;">Which model was easiest/hardest to interpret?</p>
<p style="text-indent: 40px;">Which model do you think would scale well with more data?</p>
<p style="text-indent: 40px;">What challenges did you face while working with the dataset?</p>

<p style="text-indent: 40px;">The KNN Model was our best performing model for this dataset because it had the highest recall score. Since this is medical data searching for breast cancer, we should prioritize recall to ensure we catch all positives since it is better to increase false positive and decrease false negatives. The hardest model to interpret was the KNN model, since parsing where the lines of each gradient should end and begin took a decent amount of analysis and tweaking where to find our highest recall also took some time. Inversely, the easiest model to interpret was the Decision Tree since it is easily parsed and clear with its logic. Logistic Regression would scale well since it has low complexity, is built to run quickly, and only stands to refine its metrics, the more data it is fed. Getting the dataset into a format that can be parsed into an SVM model was exceptionally difficult for me. Additionally, due to all the features of this data, I had some difficulty pinpointing which features I most wanted to focus on.
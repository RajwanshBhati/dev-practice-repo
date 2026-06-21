# Python Advanced Assignment 02 - Data Science and Visualization

## Overview

This repository contains solutions for Python Advanced Assignment 02. The assignment focuses on practical implementation of NumPy, Pandas, Matplotlib, and Seaborn for numerical computing, data analysis, data cleaning, and data visualization.

### Key Practices Followed

* Object-Oriented Programming (OOP)
* Single Responsibility Principle (SRP)
* Modular Project Structure
* PEP 8 Compliance
* Type Hinting
* Descriptive Naming Conventions
* Reusable Components
* Constants Management
* Function and Class Level Documentation
* Separation of Concerns

---

## Technologies Used

* Python 3
* NumPy
* Pandas
* Matplotlib
* Seaborn

---

## Project Structure

```text
python_advanced/
│
├── numpy_basics/
│   ├── constants.py
│   ├── numpy_operations.py
│   └── main.py
│
├── dataframe_creation/
│   ├── constants.py
│   ├── employee_dataframe_creator.py
│   ├── employee_analyzer.py
│   └── main.py
│
├── data_cleaning/
│   ├── constants.py
│   ├── employee_dataframe_creator.py
│   ├── data_cleaner.py
│   └── main.py
│
├── data_analysis/
│   ├── constants.py
│   ├── employee_dataframe_creator.py
│   ├── employee_analyzer.py
│   └── main.py
│
├── matplotlib_charts/
│   ├── constants.py
│   ├── chart_creator.py
│   └── main.py
│
├── seaborn_visualizations/
│   ├── constants.py
│   ├── employee_dataframe_creator.py
│   ├── seaborn_chart_creator.py
│   └── main.py
│
├── mini_project/
│   ├── constants.py
│   ├── student_dataframe_creator.py
│   ├── student_performance_analyzer.py
│   ├── visualization_creator.py
│   └── main.py
│
└── README.md
```

---

# Assignment 1: NumPy Basics

## Objectives

* Learn NumPy array creation
* Perform numerical computations
* Work with matrices

## Features Implemented

* Array Creation
* Mean Calculation
* Maximum Value
* Minimum Value
* Sum Calculation
* Array Addition
* Array Multiplication
* 3 × 3 Matrix Creation

## Concepts Covered

* numpy.array()
* mean()
* max()
* min()
* sum()
* Element-wise Operations

---

# Assignment 2: Pandas DataFrame Creation

## Objectives

Learn DataFrame creation and manipulation using Pandas.

## Features Implemented

* Employee DataFrame Creation
* Display First Two Rows
* Summary Statistics Generation
* Department-Based Filtering
* Bonus Calculation

## Concepts Covered

* DataFrame
* head()
* describe()
* Data Filtering
* Column Creation

---

# Assignment 3: Data Cleaning

## Objectives

Handle missing data using Pandas.

## Features Implemented

* Missing Value Detection
* Age Replacement Using Mean
* Salary Replacement Using Default Value

## Concepts Covered

* isnull()
* fillna()
* mean()

---

# Assignment 4: Data Analysis

## Objectives

Perform analytical operations using GroupBy.

## Features Implemented

* Average Salary by Department
* Maximum Salary by Department
* Employee Count by Department

## Concepts Covered

* groupby()
* mean()
* max()
* count()

---

# Assignment 5: Matplotlib Charts

## Objectives

Create visualizations using Matplotlib.

## Charts Implemented

### Bar Chart

Employees by Department

### Line Chart

Department Employee Trend

### Histogram

Salary Distribution

### Scatter Plot

Age vs Salary

## Concepts Covered

* plt.bar()
* plt.plot()
* plt.hist()
* plt.scatter()

---

# Assignment 6: Seaborn Visualizations

## Objectives

Create advanced statistical visualizations.

## Charts Implemented

### Barplot

Department vs Salary

### Boxplot

Salary Distribution Analysis

### Heatmap

Age and Salary Correlation

## Concepts Covered

* sns.barplot()
* sns.boxplot()
* sns.heatmap()
* Correlation Analysis

---

# Assignment 7: Student Performance Mini Project

## Objectives

Build an end-to-end data analysis and visualization project.

## Dataset

Student information containing:

* Name
* Marks
* Hours Studied

## Features Implemented

### Data Processing

* Student DataFrame Creation
* Performance Classification
* Pass/Fail Categorization

### Visualizations

* Hours Studied vs Marks Line Chart
* Study Hours vs Marks Scatter Plot
* Performance vs Marks Barplot

## Performance Logic

```text
Marks > 65  → Pass
Marks ≤ 65  → Fail
```

---

# Learning Outcomes

This assignment provided hands-on experience with:

* NumPy Array Operations
* Pandas DataFrame Manipulation
* Data Cleaning Techniques
* GroupBy-Based Data Analysis
* Matplotlib Visualizations
* Seaborn Statistical Charts
* Correlation Analysis
* End-to-End Data Analysis Workflow
* OOP-Based Python Development
* Modular Software Design
* Python Coding Standards

---

# Author

Rajwansh Bhati


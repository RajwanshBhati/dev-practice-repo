"""
Assignment 7:
Create visualizations using Matplotlib and Seaborn.
"""

import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns


class VisualizationCreator:
    """
    Create charts for student performance analysis.

    This class is responsible only for chart generation.
    """

    def create_line_chart(
        self,
        student_dataframe: pd.DataFrame,
        hours_column_name: str,
        marks_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create line chart for Hours Studied vs Marks.
        """

        plt.figure()

        plt.plot(
            student_dataframe[hours_column_name],
            student_dataframe[marks_column_name],
            marker="o",
        )

        plt.title(chart_title)

        plt.xlabel("Hours Studied")

        plt.ylabel("Marks")

        plt.show()

    def create_scatter_plot(
        self,
        student_dataframe: pd.DataFrame,
        hours_column_name: str,
        marks_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create scatter plot for Hours Studied vs Marks.
        """

        plt.figure()

        plt.scatter(
            student_dataframe[hours_column_name],
            student_dataframe[marks_column_name],
        )

        plt.title(chart_title)

        plt.xlabel("Hours Studied")

        plt.ylabel("Marks")

        plt.show()

    def create_performance_barplot(
        self,
        student_dataframe: pd.DataFrame,
        performance_column_name: str,
        marks_column_name: str,
        chart_title: str,
    ) -> None:
        """
        Create Seaborn barplot for Performance vs Marks.
        """

        plt.figure()

        sns.barplot(
            data=student_dataframe,
            x=performance_column_name,
            y=marks_column_name,
        )

        plt.title(chart_title)

        plt.xlabel("Performance")

        plt.ylabel("Marks")

        plt.show()

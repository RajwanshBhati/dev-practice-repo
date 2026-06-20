"""
Assignment 5:
Create Matplotlib charts.
"""

import matplotlib.pyplot as plt


class ChartCreator:
    """
    Create charts using Matplotlib.

    This class follows Single Responsibility Principle
    because it is responsible only for chart creation.
    """

    def create_bar_chart(
        self,
        departments: list[str],
        employee_counts: list[int],
        chart_title: str,
    ) -> None:
        """
        Create bar chart.

        Bar charts are useful for comparing values
        across different categories.
        """

        plt.figure()

        plt.bar(
            departments,
            employee_counts,
        )

        plt.title(chart_title)

        plt.xlabel("Department")

        plt.ylabel("Employees")

        plt.show()

    def create_line_chart(
        self,
        departments: list[str],
        employee_counts: list[int],
        chart_title: str,
    ) -> None:
        """
        Create line chart.

        Line charts are useful for visualizing trends.
        """

        plt.figure()

        plt.plot(
            departments,
            employee_counts,
            marker="o",
        )

        plt.title(chart_title)

        plt.xlabel("Department")

        plt.ylabel("Employees")

        plt.show()

    def create_histogram(
        self,
        salaries: list[int],
        chart_title: str,
        number_of_bins: int,
    ) -> None:
        """
        Create histogram.

        Histogram is used to understand how data
        values are distributed.
        """

        plt.figure()

        plt.hist(
            salaries,
            bins=number_of_bins,
        )

        plt.title(chart_title)

        plt.xlabel("Salary")

        plt.ylabel("Frequency")

        plt.show()

    def create_scatter_plot(
        self,
        ages: list[int],
        salaries: list[int],
        chart_title: str,
    ) -> None:
        """
        Create scatter plot.

        Scatter plots are useful for understanding
        relationships between two numerical variables.
        """

        plt.figure()

        plt.scatter(
            ages,
            salaries,
        )

        plt.title(chart_title)

        plt.xlabel("Age")

        plt.ylabel("Salary")

        plt.show()

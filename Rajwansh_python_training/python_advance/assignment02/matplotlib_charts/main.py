"""
Assignment 5:
Matplotlib Charts.

Tasks:
1. Create bar chart.
2. Create line chart.
3. Create histogram.
4. Create scatter plot.
"""

from chart_creator import ChartCreator
from constants import (
    AGE_VALUES,
    BAR_CHART_TITLE,
    DEPARTMENTS,
    EMPLOYEE_COUNTS,
    HISTOGRAM_TITLE,
    LINE_CHART_TITLE,
    NUMBER_OF_HISTOGRAM_BINS,
    SALARY_VALUES,
    SCATTER_PLOT_TITLE,
)


def display_bar_chart(
    chart_creator: ChartCreator,
) -> None:
    """
    Display department employee bar chart.

    This function is responsible only
    for bar chart generation.
    """

    chart_creator.create_bar_chart(
        DEPARTMENTS,
        EMPLOYEE_COUNTS,
        BAR_CHART_TITLE,
    )


def display_line_chart(
    chart_creator: ChartCreator,
) -> None:
    """
    Display line chart.

    This function is responsible only
    for line chart generation.
    """

    chart_creator.create_line_chart(
        DEPARTMENTS,
        EMPLOYEE_COUNTS,
        LINE_CHART_TITLE,
    )


def display_histogram(
    chart_creator: ChartCreator,
) -> None:
    """
    Display salary histogram.

    This function is responsible only
    for histogram generation.
    """

    chart_creator.create_histogram(
        SALARY_VALUES,
        HISTOGRAM_TITLE,
        NUMBER_OF_HISTOGRAM_BINS,
    )


def display_scatter_plot(
    chart_creator: ChartCreator,
) -> None:
    """
    Display age vs salary scatter plot.

    This function is responsible only
    for scatter plot generation.
    """

    chart_creator.create_scatter_plot(
        AGE_VALUES,
        SALARY_VALUES,
        SCATTER_PLOT_TITLE,
    )


def main() -> None:
    """
    Execute all chart examples.

    Logic is split into separate functions
    to avoid monolithic code.
    """

    chart_creator = ChartCreator()

    display_bar_chart(chart_creator)

    display_line_chart(chart_creator)

    display_histogram(chart_creator)

    display_scatter_plot(chart_creator)


if __name__ == "__main__":
    main()

"""
Assignment 7:
Student Performance Mini Project.
"""

from constants import (
    BARPLOT_TITLE,
    FAIL_STATUS,
    HOURS_STUDIED_COLUMN_NAME,
    LINE_CHART_TITLE,
    MARKS_COLUMN_NAME,
    PASS_STATUS,
    PASSING_MARKS,
    PERFORMANCE_COLUMN_NAME,
    SCATTER_PLOT_TITLE,
    STUDENT_DATA,
)
from student_dataframe_creator import (
    StudentDataFrameCreator,
)
from student_performance_analyzer import (
    StudentPerformanceAnalyzer,
)
from visualization_creator import (
    VisualizationCreator,
)


def main() -> None:
    """
    Execute student performance project workflow.
    """

    dataframe_creator = (
        StudentDataFrameCreator(
            STUDENT_DATA
        )
    )

    student_dataframe = (
        dataframe_creator.create_dataframe()
    )

    performance_analyzer = (
        StudentPerformanceAnalyzer(
            student_dataframe
        )
    )

    student_dataframe = (
        performance_analyzer.add_performance_column(
            MARKS_COLUMN_NAME,
            PERFORMANCE_COLUMN_NAME,
            PASSING_MARKS,
            PASS_STATUS,
            FAIL_STATUS,
        )
    )

    print(student_dataframe)

    visualization_creator = (
        VisualizationCreator()
    )

    visualization_creator.create_line_chart(
        student_dataframe,
        HOURS_STUDIED_COLUMN_NAME,
        MARKS_COLUMN_NAME,
        LINE_CHART_TITLE,
    )

    visualization_creator.create_scatter_plot(
        student_dataframe,
        HOURS_STUDIED_COLUMN_NAME,
        MARKS_COLUMN_NAME,
        SCATTER_PLOT_TITLE,
    )

    visualization_creator.create_performance_barplot(
        student_dataframe,
        PERFORMANCE_COLUMN_NAME,
        MARKS_COLUMN_NAME,
        BARPLOT_TITLE,
    )


if __name__ == "__main__":
    main()

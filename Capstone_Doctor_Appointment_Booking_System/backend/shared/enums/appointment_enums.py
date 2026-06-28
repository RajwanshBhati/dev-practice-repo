from enum import Enum

class ConsultationType(str, Enum):
    FIRST = "FIRST"
    FOLLOW_UP = "FOLLOW_UP"
    EMERGENCY = "EMERGENCY"
    ROUTINE = "ROUTINE"

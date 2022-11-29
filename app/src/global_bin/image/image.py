from enum import IntEnum
from math import floor
from random import randrange
from typing import List


class ThresholdType(IntEnum):
    OTSU = 0
    KITTLER = 1
    LLOYD = 2
    SUNG = 3
    RIDLER = 4
    HUANG = 5
    RAMESH = 6
    LI1 = 7
    LI2 = 8
    BRINK = 9
    KAPUR = 10
    SAHOO = 11
    SHANBHAG = 12
    YEN = 13
    TSAI = 14

    @staticmethod
    def values():
        return list(ThresholdType)

    @staticmethod
    def count():
        return len(ThresholdType)

    @staticmethod
    def random():
        return ThresholdType.values()[randrange(ThresholdType.count())]


class Image:
    def __init__(self, thresholds: List[float], f_measures: List[float], ideal_threshold: float):
        self.thresholds = thresholds
        self.f_measures = f_measures
        self.ideal_threshold = ideal_threshold

    def threshold(self, threshold_type: ThresholdType) -> float:
        return self.thresholds[threshold_type.value]

    def f_measure(self, threshold: float):
        return self.f_measures[floor(threshold * 255)]

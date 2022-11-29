from abc import ABC
from functools import reduce
from typing import Callable

from ...image import Image


class Operation(ABC):
    DISCRIMINATOR = "UNDEFINED"
    INSTANCE = None

    def __init__(self, func: Callable[[Image], float]):
        self.__func = func

    def execute(self, image: Image) -> float:
        return self.__func(image)

    @staticmethod
    def all():
        return [
            GeometricMeanOperation,
            ArithmeticMeanOperation,
            SquaredMeanOperation,
            HarmonicMeanOperation,
        ]


class GeometricMeanOperation(Operation):
    DISCRIMINATOR = "GEOMETRIC_MEAN"
    INSTANCE = None

    def __init__(self):
        super().__init__(lambda image: geometric_mean(*image.thresholds))

    @staticmethod
    def get_instance():
        if GeometricMeanOperation.INSTANCE is None:
            GeometricMeanOperation.INSTANCE = GeometricMeanOperation()

        return GeometricMeanOperation.INSTANCE


class ArithmeticMeanOperation(Operation):
    DISCRIMINATOR = "ARITHMETIC_MEAN"
    INSTANCE = None

    def __init__(self):
        super().__init__(lambda image: arithmetic_mean(*image.thresholds))

    @staticmethod
    def get_instance():
        if ArithmeticMeanOperation.INSTANCE is None:
            ArithmeticMeanOperation.INSTANCE = ArithmeticMeanOperation()

        return ArithmeticMeanOperation.INSTANCE


class SquaredMeanOperation(Operation):
    DISCRIMINATOR = "SQUARED_MEAN"
    INSTANCE = None

    def __init__(self):
        super().__init__(lambda image: squared_mean(*image.thresholds))

    @staticmethod
    def get_instance():
        if SquaredMeanOperation.INSTANCE is None:
            SquaredMeanOperation.INSTANCE = SquaredMeanOperation()

        return SquaredMeanOperation.INSTANCE


class HarmonicMeanOperation(Operation):
    DISCRIMINATOR = "HARMONIC_MEAN"

    def __init__(self):
        super().__init__(lambda image: harmonic_mean(*image.thresholds))

    @staticmethod
    def get_instance():
        if HarmonicMeanOperation.INSTANCE is None:
            HarmonicMeanOperation.INSTANCE = HarmonicMeanOperation()

        return HarmonicMeanOperation.INSTANCE


def arithmetic_mean(*values):
    return sum(values) / len(values)


def geometric_mean(*values):
    product = reduce(lambda x, y: x * y, values)
    return product ** (1 / len(values))


def squared_mean(*values):
    squares = [x ** 2 for x in values]
    return arithmetic_mean(*squares) ** 0.5


def harmonic_mean(*values):
    inverse = [1 / x for x in values]
    return len(values) / sum(inverse)

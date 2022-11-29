import os
from math import floor
from typing import List

from .image import Image

__DIRECTORY = "../files/images/"

full_set = None
train_set = None
validation_set = None


def find_filenames() -> List[str]:
    global full_set

    if full_set is None:
        full_set = os.listdir(__DIRECTORY)

    return full_set


def find_train_set() -> List[Image]:
    global train_set

    if train_set is None:
        filenames = find_filenames()[:floor(len(find_filenames()) * 0.7)]
        train_set = [load(filename) for filename in filenames]

    return train_set


def find_validation_set() -> List[Image]:
    global validation_set

    if validation_set is None:
        filenames = find_filenames()[floor(len(find_filenames()) * 0.7):floor(len(find_filenames()) * 0.95)]
        validation_set = [load(filename) for filename in filenames]

    return validation_set


def load(filename) -> Image:
    with open(f'{__DIRECTORY}{filename}') as file:
        content = file.read().split("\n")

        thresholds = [float(x) for x in content[0].split(",") if x != ""]
        f_measures = [float(x) for x in content[1].split(",") if x != ""]

        return Image(thresholds[1:], f_measures, thresholds[0])

import csv
import numpy as np

# Usage: [threshold, f_measures, intervals] = csvParser(path)
def csvParser(path):
    with open(path, newline='') as csvfile:
        csvReader = csv.reader(csvfile)
        toReturn = []
        lines = []
        for line in csvReader:
            lines.append(line)
        csvfile.close()
        # Need to pop the last element because the second line is ending in ','
        lines[1].pop()

        # Converting the string elements to float
        for line in lines:
            toReturn.append([float(num_str) for num_str in line])

        # Creating the 256 intervals including [1, 1]
        array = []
        for number in np.arange(0, 256) / 255:
            array.append(round(number, 6))
        toReturn.append(list(zip(array, array[1:len(array)] + [1.0])))
        
        return toReturn
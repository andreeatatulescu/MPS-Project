import time

from global_bin.tree import train

if __name__ == '__main__':
    start = time.time()
    train(1)
    stop = time.time()
    print(stop - start)

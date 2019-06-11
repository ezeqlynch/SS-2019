import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from functools import reduce
import numpy as np
import math


# import PyQt5.QtGui


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--d',
      help="Path to the static data file.",
      default='1.500'
  )
  parser.add_argument(
      '--simus',
      help="Path to the static data file.",
      default=1
  )

  return parser




if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    simusNum = int(parsedArgs.simus)

    for i in range(1,simusNum+1):
        staticFile = open("./data/300-times-v" + parsedArgs.d + "-" + str(i) + ".stats", "r")
        particlesOut = 0
        time = 0
        x = []
        y = []
        # staticFile.readline()
        
        for line in staticFile:
            arr = line.split(' ')
            if(len(arr) == 1):
                if(arr[0] == '\n'):
                    continue
                time += float(arr[0])
                x.append(time)
                particlesOut += 1
                y.append(particlesOut)

        plt.plot(x, y, marker=".",
                markersize=3, linewidth=0.5)
    # plt.title('Partículas salvadas sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Egresos (particulas)')
    plt.xlabel('Tiempo (s)')
    # plt.yscale("log")
    # plt.xscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=0)
    plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    # plt.show()
    plt.savefig("./data/300-times-v" + parsedArgs.d + ".stats" + '.png', bbox_inches='tight')

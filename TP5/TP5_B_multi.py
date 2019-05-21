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
      '--staticFiles',
      help="Path to the static data files.",
      nargs='+',
      default='data/cut.xyz'
  )
  parser.add_argument(
      '--d',
      help="d of static data files.",
      nargs='+',
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    files = parsedArgs.staticFiles
    ds = parsedArgs.d
    
    for index, fileName in enumerate(files, start=0):
        staticFile = open(fileName, "r")
        n = int(staticFile.readline().split(' ')[0])
        deltaTime = 1/60
        totalKE = []
        for line in staticFile:
            arr = line.split(' ')
            if(len(arr) == 1):
                if(arr[0] == '\n'):
                    continue
                totalKE.append(float(arr[0]))
        times = list(map(lambda num: num*deltaTime,
                        list(range(len(totalKE)))))
        plt.plot(times, totalKE, marker=".", markersize=3, linewidth=0.5, label="KE d=" + ds[index])
    plt.title('Energía cinética total sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Energía (J)')
    plt.xlabel('Tiempo (s)')
    plt.yscale("log")
    # plt.xscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=0,ymax=0.1)
    plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    # plt.show()
    plt.savefig('multi.png', bbox_inches='tight')

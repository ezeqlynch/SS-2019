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
      help="d of static data files.",
      nargs='+',
      default='data/cut.xyz'
  )
  parser.add_argument(
      '--vnames',
      help="vMax name of static data files.",
      nargs='+',
      default='data/cut.xyz'
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
    vnames = parsedArgs.vnames
    simusNum = int(parsedArgs.simus)
    print(parsedArgs.d)
    print(parsedArgs.vnames)

    deltaTime = 1/60
    avgEndTimes = []
    avgEndTimesErr = []
    for index, d in enumerate(parsedArgs.d):
        totalEndTimes = []
        for i in range(1, simusNum+1):
            staticFile = open("./data/300-times-v"+ vnames[index] +"-"+ str(i) +".stats", "r")
            # n = int(staticFile.readline().split(' ')[0])
            endTime = 0
            for line in staticFile:
                arr = line.split(' ')
                if(len(arr) == 1):
                    if(arr[0] == '\n'):
                        continue
                    endTime += float(arr[0])
            totalEndTimes.append(endTime)
        avgEndTimes.append(
            np.mean(totalEndTimes))
        avgEndTimesErr.append(
            np.std(totalEndTimes))
    print(len(avgEndTimes))
    print(len(avgEndTimesErr))
    plt.errorbar(parsedArgs.d, avgEndTimes, avgEndTimesErr, marker=".", markersize=3,
                    linewidth=0.5, label="Tiempo promedio de salida")
    # plt.plot(times, avgKE, marker=".", markersize=3,
    #             linewidth=0.5, label="Promedio KE d=0," + d, yerr=avgKEErr)

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Tiempo (s)')
    plt.xlabel('Velocidad Deseada (m/s)')
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

    plt.show()
    # plt.savefig(parsedArgs.staticFile + '.png', bbox_inches='tight')

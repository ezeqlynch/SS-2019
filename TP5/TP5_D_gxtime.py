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
      '--g',
      help="d of static data files.",
      nargs='+',
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    # dnames = parsedArgs.dnames
    
    avgTime = []
    avgTimeErr = []
    totalTimes = []
    for index, g in enumerate(parsedArgs.g):
        times = []
        for i in range(1,4):
            staticFile = open(
                "./data/cerrado/tp5-L1,5-W0,4-D0,0-KN10e5-G"+g+"-"+str(i)+".stats", "r")
            n = int(staticFile.readline().split(' ')[0])
            deltaTime = 1/60
            steps = 0
            for lineIndex, line in enumerate(staticFile):
                arr = line.split(' ')
                if(len(arr) == 1):
                    if(arr[0] == '\n'):
                        continue
                    if(float(arr[0]) < 10e-6 and float(arr[0]) != 0 ):
                        break
                    steps = steps+1
            times.append(steps*deltaTime)
        totalTimes.append(times)
    for i in range(len(totalTimes)):
        avgTime.append(np.mean(totalTimes[i]))
        avgTimeErr.append(np.std(totalTimes[i]))
    plt.errorbar([10,35,70,100,200], avgTime, avgTimeErr, marker="o", markersize=5,
                 linewidth=2, linestyle='None')
        # plt.plot(times, avgKE, marker=".", markersize=3,
        #          linewidth=0.5, label="Promedio KE d=0," + d, yerr=avgKEerr)
    plt.title('Tiempo promedio hasta llegar al equilibrio para distintos valores de g')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Tiempo (s)')
    plt.xlabel('g (kg/s)')
    # plt.xscale("log")
    # plt.legend(loc='best')
    # plt.yscale("log")
    # plt.ylim(ymin=10**-3, ymax=10**-1)
    # plt.ylim(ymin=0.4, ymax=1.2)
    # plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    plt.show()
    # plt.savefig('multiAVG2.png', bbox_inches='tight')

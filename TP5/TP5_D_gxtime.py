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
    averageTimes2 = [[7.24, 7.91], [4.16, 5.5], [2.42, 2.76, 6.1], [3.6, 4.52, 5.08], [4.4, 4.4, 4.74]]
    gstr = ["10.0","35.0","70.0","100.0","200.0","400.0"]
    #tiempos: 10: -,-,-, 35: 7.24, 7.91, - 70: 4,16, 5.5, - 100: 2.42, 2.76, 6.1 200: 3.6, 4.52, 5.08 500: 4.4, 4,4. 4,74
    for index in range(1, len(gstr)):
        times = []
        for i in range(1,4):
            staticFile = open(
                "../tp5-KN10e5-G"+gstr[i]+"-"+str(i)+".stats", "r")
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
    for i in range(len(averageTimes2)):
        avgTime.append(np.mean(averageTimes2[i]))
        avgTimeErr.append(np.std(averageTimes2[i]))
    print (avgTime)
    print (avgTimeErr)
    plt.errorbar([35,70,100,200, 400], avgTime, avgTimeErr, marker="o", markersize=5,
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

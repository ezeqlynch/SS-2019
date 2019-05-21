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
      '--dnames',
      help="d of static data files.",
      nargs='+',
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    dnames = parsedArgs.dnames
    
    for index, d in enumerate(parsedArgs.d):
        avgKE = []
        avgKEerr = []
        totalKEs = []
        for i in range(1,4):
            staticFile = open(
                "./data/tp5-L1,5-W0,4-D0,0-KN10e5-G"+d+"-"+str(i)+".stats", "r")
            n = int(staticFile.readline().split(' ')[0])
            deltaTime = 1/60
            totalKE = []
            for line in staticFile:
                arr = line.split(' ')
                if(len(arr) == 1):
                    if(arr[0] == '\n'):
                        continue
                    totalKE.append(float(arr[0]))
            totalKEs.append(totalKE)
        for i in range(len(totalKEs[0])):
            avgKE.append(np.mean([totalKEs[0][i], totalKEs[1][i], totalKEs[2][i]]))
            avgKEerr.append(np.std([totalKEs[0][i], totalKEs[1][i], totalKEs[2][i]]))
        times = list(map(lambda num: num*deltaTime,
                            list(range(len(avgKE)))))
        plt.errorbar(times, avgKE, avgKEerr, marker=".", markersize=3,
                     linewidth=0.5, label="G=" + d)
        # plt.plot(times, avgKE, marker=".", markersize=3,
        #          linewidth=0.5, label="Promedio KE d=0," + d, yerr=avgKEerr)
    plt.title('Energía cinética total promediada sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Energía (J)')
    plt.xlabel('Tiempo (s)')
    plt.yscale("log")
    # plt.xscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=10**-3, ymax=10**-1)
    plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    plt.show()
    # plt.savefig('multiAVG2.png', bbox_inches='tight')

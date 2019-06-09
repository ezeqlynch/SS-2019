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
    staticFile = open(parsedArgs.staticFile, "r")
    dnames = parsedArgs.dnames

    deltaTime = 1/60
    # for index, d in enumerate(parsedArgs.d):
    avgOut = []
    avgOutErr = []
    totalOuts = []
    for i in range(1, 4):
        x = []
        y = []
        particlesOut = 0
        time = 0
        staticFile = open("./data/tp6-name"+str(i)+".stats", "r")
        # n = int(staticFile.readline().split(' ')[0])
        for line in staticFile:
            arr = line.split(' ')
            if(len(arr) == 1):
                if(arr[0] == '\n'):
                    continue
                outTime = float(arr[0])
                flag = True
                while flag:
                    if(time > outTime):
                        particlesOut += 1
                        flag = False
                    y.append(particlesOut)
                    time += deltaTime
        totalOuts.append(y)
    for i in range(len(totalOuts[0])):
        avgOut.append(
            np.mean([totalOuts[0][i], totalOuts[1][i], totalOuts[2][i]]))
        avgOutErr.append(
            np.std([totalOuts[0][i], totalOuts[1][i], totalOuts[2][i]]))
    times = list(map(lambda num: num*deltaTime,
                        list(range(len(avgOut)))))
    plt.errorbar(times, avgOut, avgOutErr, marker=".", markersize=3,
                    linewidth=0.5, label="Prom. salidas")
    # plt.plot(times, avgKE, marker=".", markersize=3,
    #             linewidth=0.5, label="Promedio KE d=0," + d, yerr=avgKEErr)

    plt.plot(x, y, marker=".",
             markersize=3, linewidth=0.5, label="Escaped Particles")
    plt.title('Particulas salvadas sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Particulas')
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
    plt.savefig(parsedArgs.staticFile + '.png', bbox_inches='tight')

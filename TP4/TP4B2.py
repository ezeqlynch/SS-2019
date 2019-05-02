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
      '--staticFile',
      help="Path to the static data file.",
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    staticFile = open(parsedArgs.staticFile, "r")
    
    n = int(staticFile.readline().split(' ')[0])
    m = 0.1
    deltaTime = 1/30
    times = []

    totalPE = []
    totalKE = []
    totalE = []

    currentPE = []
    currentKE = []

    

    for line in staticFile:
        arr = line.split(' ')
        if(len(arr)==1):
            if(arr[0] == '\n'):
                continue
            ke = reduce((lambda x, y: x + y), currentKE,0)
            pe = reduce((lambda x, y: x + y), currentPE,0)
            currentPE = []
            currentKE = []
            totalE.append(ke+pe)
            totalKE.append(ke)
            totalPE.append(pe)
        else:
            currentKE.append(0.5 * m * (float(arr[3])**2+float(arr[4])**2))
            currentPE.append(float(arr[5]))
    ke = reduce((lambda x, y: x + y), currentKE)
    pe = reduce((lambda x, y: x + y), currentPE)
    totalKE.append(ke)
    totalPE.append(pe)
    totalE.append(ke+pe)
    
    totalPE[0] = totalPE[1]
    totalE[0] = totalPE[0] + totalKE[0]

    times = list(map(lambda num: num*deltaTime,
                     list(range(len(totalE)))))

    # print(totalKE)
    # print(totalPE)
    # print(totalE)

    plt.plot(times, totalKE, color="blue", label="KE")
    plt.plot(times, totalPE, color="red", label="PE")
    plt.plot(times, totalE, color="magenta", label="TE")
    plt.title('Energia sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Energia (J)')
    plt.xlabel('Tiempo (s)')
    # plt.yscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.show()

    
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')

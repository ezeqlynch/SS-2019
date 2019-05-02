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
    boxXSize = 200
    times = []

    left = []
    currentLeft = 0

    

    for line in staticFile:
        arr = line.split(' ')
        if(len(arr)==1):
            if(arr[0] == '\n'):
                continue
            left.append(currentLeft/n)
            currentLeft = 0
        else:
            if(float(arr[1]) < boxXSize):
                currentLeft+=1
    left.append(currentLeft/n)

    times = list(map(lambda num: num*deltaTime,
                     list(range(len(left)))))

    # print(totalKE)
    # print(totalPE)
    # print(totalE)

    plt.plot(times, left, color="blue", label="Fp")
    plt.title('Fracción de particulas en el recinto izquierdo (Fp) sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Fracción de particulas')
    plt.xlabel('Tiempo (s)')
    # plt.yscale("log")
    plt.legend(loc='best')
    plt.ylim(bottom=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.show()

    
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')

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

  parser.add_argument(
      '--bins',
      help="Path to the static data file.",
      default=30
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    staticFile = open(parsedArgs.staticFile, "r")
    
    bins = int(parsedArgs.bins)
    n = int(staticFile.readline().split(' ')[0])
    deltaTime = 1/30
    times = []

    allV = []
    currentV = []

    

    for line in staticFile:
        arr = line.split(' ')
        if(len(arr)==1):
            if(arr[0] == '\n'):
                continue
            allV.append(currentV)
            currentV = []
        else:
            currentV.append(math.sqrt(float(arr[3])**2+float(arr[4])**2))
    
    allV.append(currentV)
    times = list(map(lambda num: num*deltaTime,
                     list(range(len(allV)))))

    plt.figure(1)
    plt.hist(allV[0], edgecolor='black', bins=bins)
    plt.title('Distribucion de V al comienzo')
    plt.ylabel('Cantidad')
    plt.xlabel('Rapidez (m/s)')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    plt.figure(2)
    plt.hist(allV[int(n/3)], edgecolor='black', bins=bins)
    plt.title('Distribucion de V al primer tercio')
    plt.ylabel('Cantidad')
    plt.xlabel('Rapidez (m/s)')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    plt.figure(3)
    plt.hist(allV[int(n*2/3)], edgecolor='black', bins=bins)
    plt.title('Distribucion de V al segundo tercio')
    plt.ylabel('Cantidad')
    plt.xlabel('Rapidez (m/s)')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    plt.figure(4)
    plt.hist(allV[-1], edgecolor='black', bins=bins)
    plt.title('Distribucion de V al final')
    plt.ylabel('Cantidad')
    plt.xlabel('Rapidez (m/s)')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    
    plt.show()

    
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')

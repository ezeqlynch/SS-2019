import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
import numpy as np
import math


# import PyQt5.QtGui


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/DHM_ECM_0.001-0.075.stat'
  )

  parser.add_argument(
      '--method',
      help="Integration Method.",
      default="verlet"
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    method = parsedArgs.method
    staticFile = open(parsedArgs.staticFile, "r")

    deltaTimes = []
    verletECMs = []
    beemanECMs = []
    gpc5ECMs = []
    for line in staticFile:
        timeECMs = line.split(' ')
        deltaTimes.append(float(timeECMs[0]))
        verletECMs.append(float(timeECMs[1]))
        beemanECMs.append(float(timeECMs[2]))
        gpc5ECMs.append(float(timeECMs[3]))

    if(method == "verlet"):
        plt.plot(deltaTimes, verletECMs, color="black", label="verlet")
        plt.title('ECM sobre dt con metodo ' + method)
    elif(method == "beeman"):
        plt.plot(deltaTimes, beemanECMs, color="red", label="beeman")
        plt.title('ECM sobre dt con metodo ' + method)
    elif(method == "gpc5"):
        plt.plot(deltaTimes, gpc5ECMs, color="orange", label="gpc5")
        plt.title('ECM sobre dt con metodo ' + method)
    elif(method == "all"):
        plt.plot(deltaTimes, verletECMs, color="green", label="verlet")
        plt.plot(deltaTimes, beemanECMs, color="red", label="beeman")
        plt.plot(deltaTimes, gpc5ECMs, color="orange", label="gpc5")
        plt.title('ECM sobre dt con todos los metodos')
    else:
        print("Wrong method")
        exit(-1)


    # plt.axis([0, 5, -1, 1])
    plt.ylabel('ECM')
    plt.xlabel('dt (s)')
    plt.yscale("log")
    plt.legend(loc='best')


    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')
    plt.show()

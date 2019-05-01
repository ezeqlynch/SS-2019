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
      default='data/DHM.stat'
  )

  parser.add_argument(
      '--delta',
      help="Path to the static data file.",
      default="0.005"
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    delta = float(parsedArgs.delta)
    staticFile = open(parsedArgs.staticFile, "r")

    times = []
    analytic = []
    verlet = []
    beeman = []
    gpc5 = []

    for line in staticFile:
        time_positions = line.split(' ')
        times.append(float(time_positions[0]))
        analytic.append(float(time_positions[1]))
        verlet.append(float(time_positions[2]))
        beeman.append(float(time_positions[3]))
        gpc5.append(float(time_positions[4]))

    y=verlet

    ecm = 0
    for i in range(len(times)):
        ecm += (y[i]-analytic[i])**2
    ecm /= len(times)
    print(ecm)

    # print(times)
    # print(analytic)
    # plt.plot(times, analytic)
    # plt.plot(times, verlet)
    # plt.plot(times, beeman)
    # plt.plot(times, gpc5)
    plt.errorbar(times,y,yerr=ecm)
    plt.axis([0, 5, -1, 1])

    plt.title('R / tiempo')
    plt.ylabel('R')
    plt.xlabel('Tiempo (s)')

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()
    
    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')
    plt.show()

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
      default='data/DHM_0.005.stat'
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
    
    time_frames = staticFile.readline().split(' ')
    deltaTime = float(time_frames[0])
    times = list(map(lambda num: num*deltaTime,list(range(0, int(time_frames[1])))))
    print(times)

    ECMs = staticFile.readline().split(' ')
    verletECM = float(ECMs[0])
    beemanECM = float(ECMs[1])
    gpc5ECM = float(ECMs[2])
    
    analytic = []
    verlet = []
    beeman = []
    gpc5 = []
    for line in staticFile:
        positions = line.split(' ')
        analytic.append(float(positions[0]))
        verlet.append(float(positions[1]))
        beeman.append(float(positions[2]))
        gpc5.append(float(positions[3]))

    if(method == "verlet"):
        plt.errorbar(times, verlet, yerr=verletECM,
                     color="green", label="verlet")
        plt.title('R sobre tiempo con metodo ' + method)
    elif(method == "beeman"):
        plt.errorbar(times, beeman, yerr=beemanECM,
                     color="red", label="beeman")
        plt.title('R sobre tiempo con metodo ' + method)
    elif(method == "gpc5"):
        plt.errorbar(times, gpc5, yerr=gpc5ECM, color="orange", label="gpc5")
        plt.title('R sobre tiempo con metodo ' + method)
    elif(method == "all"):
        plt.errorbar(times, verlet, yerr=verletECM,
                     color="green", label="verlet")
        plt.errorbar(times, beeman, yerr=beemanECM,
                     color="red", label="beeman")
        plt.errorbar(times, gpc5, yerr=gpc5ECM, color="orange", label="gpc5")
        plt.title('R sobre tiempo con todos los metodos')
    else:
        print("Wrong method")
        exit(-1)

    plt.plot(times, analytic, color="black", label="analytic")

    plt.axis([0, 5, -1, 1])
    plt.ylabel('R')
    plt.xlabel('Tiempo (s)')

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()
    plt.legend(loc="best")
    
    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')
    plt.show()

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
    colors = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#008080',
              '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#000075', '#808080', '#ffffff', '#000000']
    gs = ["10.0", "35.0", "70.0", "100.0", "200.0", "400.0"]
    for j in range(0, 6):
        for i in range(1, 4):
            staticFile = open("../tp5-KN10e5-G"+gs[j]+" "+str(i)+".stats", "r")

            n = int(staticFile.readline().split(' ')[0])
            deltaTime = 1/60
            totalKE = []
            counter = 0
            for line in staticFile:
                arr = line.split(' ')
                if(len(arr) == 1):
                    if(arr[0] == '\n'):
                        continue
                    if counter == 5:
                        counter = 0
                        totalKE.append(float(arr[0]))
                    counter += 1

            times = list(map(lambda num: num*deltaTime*5,
                             list(range(len(totalKE)))))

            if(i == 1):
                plt.plot(times, totalKE, marker=".", markersize=5, linewidth=0.5, label="g="+gs[j], color=colors[j])
            else:
                plt.plot(times, totalKE, marker=".", markersize=5, linewidth=0.5, color=colors[j])

    plt.title('Energía cinética total sobre tiempo')

    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Energía (J)')
    plt.xlabel('Tiempo (s)')
    plt.yscale("log")
    # plt.xscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=0,ymax=0.1)
    plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    plt.show()
    # plt.savefig(parsedArgs.staticFile + '.png', bbox_inches='tight')

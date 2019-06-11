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
def running_mean(x, N):
    cumsum = np.cumsum(np.insert(x, 0, 0))
    return (cumsum[N:] - cumsum[:-N]) / float(N)


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
      '--simus',
      help="Path to the static data file.",
      default=1
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    simusNum = int(parsedArgs.simus)

    deltaTime = 0.2
    for index, d in enumerate(parsedArgs.d):
        avgOut = []
        avgOutErr = []
        totalOuts = []
        for i in range(1, simusNum+1):
            x = []
            y = []
            particlesOut = 0
            staticFile = open("./data/300-times-v" + d +
                              "-" + str(i) + ".stats", "r")
            for line in staticFile:
                arr = line.split(' ')
                if(len(arr) == 1):
                    if(arr[0] == '\n'):
                        continue
                    outTime = float(arr[0])
                    time = 0
                    flag = True
                    while flag:
                        if(time > outTime):
                            particlesOut += 1
                            flag = False
                        y.append(particlesOut)
                        time += deltaTime
            totalOuts.append(y)
        print(len(totalOuts))
        for i in range(len(totalOuts[0])):
            arr = []
            for j in range(simusNum):
                if(i < len(totalOuts[j])):
                    arr.append(totalOuts[j][i])
                else:
                    arr.append(300)
            avgOut.append(np.mean(arr))
            avgOutErr.append(np.std(arr))
        
        times = list(map(lambda num: num*deltaTime,
                         list(range(len(avgOut)))))

        realY = []
        print(len(y))
        halfWindowSize = 50  # Window de 20 segundos
        for i in range(len(y)):
            if(i < halfWindowSize):
                start = 0
                end = i+halfWindowSize
            elif(i >= len(y)-halfWindowSize):
                start = i-halfWindowSize
                end = len(y)-1
            else:
                start = i-halfWindowSize
                end = i+halfWindowSize
            realY.append((y[end]-y[start])/20)
        
        val = running_mean(realY, 20)
        print("cosa")
        print(len(val))
        print(len(times))
        plt.plot(times[:-(len(times)-len(val))], val, marker=".",
                 markersize=3, linewidth=0.5, label="Vmax=" + str(float(d)))
        # plt.errorbar(times, realY, avgOutErr, marker=".", markersize=3,
        #              linewidth=0.5, label="Prom. salidas con Vmax=" + d)
    # plt.plot(times, avgKE, marker=".", markersize=3,
    #             linewidth=0.5, label="Promedio KE d=0," + d, yerr=avgKEErr)
    plt.title('Media móvil del caudal')
    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Caudal (partículas / s)')
    plt.xlabel('Tiempo (s)')
    # plt.yscale("log")
    # plt.xscale("log")
    plt.legend(loc='best')
    plt.ylim(ymin=0.4)
    plt.xlim(xmin=0)

    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    plt.tight_layout()

    plt.show()
    # plt.savefig(parsedArgs.staticFile + '.png', bbox_inches='tight')

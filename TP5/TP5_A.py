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
      '--staticFile',
      help="Path to the static data file.",
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    spliceFrom = [200, 300, 500, 700, 1000]
    files = [15, 175, 2, 225, 25]
    ds = ["0.15", "0.175", "0.2", "0.225", "0.25"]
    dsnum = [0.15, 0.175, 0.2, 0.225, 0.25]
    means = []
    stds = []
    colors = ["blue", "red", "green", "pink", "purple"]
    labels = ["d = 0.150m", "d = 0.175m", "d = 0.200m", "d = 0.225m", "d = 0.250m"]
    for i in range(0, 5):
        staticFile = open("../tp5sims/1/tp5-L1,5-W0,4-D0,"+str(files[i])+"-1-times.stats", "r")
        staticFile2 = open("../tp5sims/1/tp5-L1,5-W0,4-D0,"+str(files[i])+"-2-times.stats", "r")
        staticFile3 = open("../tp5sims/1/tp5-L1,5-W0,4-D0,"+str(files[i])+"-3-times.stats", "r")

        times = []
        times2 = []
        times3 = []
        timesAvg = []
        timesAvg2 = []
        timesAvg3 = []
        fullAvg = []
        fullAvgErr = []
        for line in staticFile:
            times.append(float(line))

        timesAvg = running_mean(times, 1000)
        timesAvg = timesAvg**-1

        for line in staticFile2:
            times2.append(float(line))

        timesAvg2 = running_mean(times2, 1000)
        timesAvg2 = timesAvg2 ** -1

        for line in staticFile3:
            times3.append(float(line))

        timesAvg3 = running_mean(times3, 1000)
        timesAvg3 = timesAvg3 ** -1
        print(min(len(timesAvg), len(timesAvg2), len(timesAvg3)))
        for j in range(0, min(len(timesAvg), len(timesAvg2), len(timesAvg3))):
            fullAvg.append(np.mean([timesAvg[j], timesAvg2[j], timesAvg3[j]]))
            fullAvgErr.append(np.std([timesAvg[j], timesAvg2[j], timesAvg3[j]]))


        #for mean and std single value ->

        aux = np.concatenate((timesAvg[spliceFrom[i]:], timesAvg2[spliceFrom[i]:], timesAvg3[spliceFrom[i]:]), axis=None)
        means.append(np.mean(aux))
        stds.append(np.std(aux))

        #for errors ->
        # plt.errorbar(range(0,min(len(timesAvg), len(timesAvg2), len(timesAvg3))), fullAvg, fullAvgErr, marker=".", markersize=1,
        #              linewidth=0.01, label=labels[i])

        #for all together now ->
        # plt.plot(timesAvg, linewidth=0.5, color=colors[i], label=labels[i])
        # plt.plot(timesAvg2, linewidth=0.5, color=colors[i])
        # plt.plot(timesAvg3, linewidth=0.5, color=colors[i])
        plt.title('Caudal')
    print(means)
    print(stds)
    reg = np.polyfit(dsnum, means, 1)
    x = np.linspace(0.15, 0.25, 100)
    y = reg[0] * x + reg[1]
    plt.errorbar(dsnum, means, stds, marker=".", markersize=5,
                 linewidth=1, linestyle='None', barsabove=True, ecolor="green", color="blue")
    # plt.errorbar(x, y, color="red", label="y="+ format(reg[0], '.2f') +" * x " + format(reg[1], '.2f'))
    plt.xticks(dsnum)
    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Partículas/segundo')
    plt.xlabel('Tamaño de ranura (m)')
    # plt.yscale("log")
    # plt.legend(loc='best')
    plt.ylim(ymin=0)

    plt.grid(b=True, which='major', linestyle='-', axis='y')
    # plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.show()

    # plt.axes().yaxis.set_major_locator(ticker.MultipleLocator(0.25))
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(0.05))
    # plt.axes().xaxis.set_minor_locator(ticker.MultipleLocator(0.5))
    # plt.tight_layout()

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')

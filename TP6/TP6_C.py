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
        times = []
        timesAvgs = []
        # timesAvg2 = []
        # timesAvg3 = []
        fullAvg = []
        fullAvgErr = []
        for j in range(0, 3):
            staticFile = open("../0."+str(files[i])+"-1-times.stats", "r")

            for line in staticFile:
                times.append(float(line.replace(",",".")))
            timesAvg = running_mean(times, 1000)
            timesAvg = timesAvg**-1
            timesAvgs.append(timesAvg)
            
        for j in range(0, min(list(map(lambda timesAvg: len(timesAvg), timesAvgs)))):
            fullAvg.append(np.mean([timesAvgs[0][j], timesAvg[1][j], timesAvg[2][j]]))
            fullAvgErr.append(np.std([timesAvgs[0][j], timesAvgs[1][j], timesAvgs[2][j]]))


        #for mean and std single value ->

        aux = np.concatenate((timesAvgs[0][spliceFrom[i]:], timesAvgs[1][spliceFrom[i]:], timesAvgs[2][spliceFrom[i]:]), axis=None)
        means.append(np.mean(aux))
        stds.append(np.std(aux))
        plt.title("Aproximacion por Beverloo")
        #for errors ->
        # plt.errorbar(range(0,min(len(timesAvg), len(timesAvg2), len(timesAvg3))), fullAvg, fullAvgErr, marker=".", markersize=1,
        #              linewidth=0.01, label=labels[i])

        #for all together now ->
        # plt.plot(timesAvg, linewidth=0.5, color=colors[i], label=labels[i])
        # plt.plot(timesAvg2, linewidth=0.5, color=colors[i])
        # plt.plot(timesAvg3, linewidth=0.5, color=colors[i])
        # plt.title('Media de Medias moviles del caudal')
    print(means)
    print(stds)
    # print()
    reg = np.polyfit(dsnum, means, 1)
    ds = np.arange(0.15, 0.25, 0.0001)
    #
    plt.plot(ds, [5634 * math.pow(math.fabs(x-(0)*0.0125), 1.5) for x in ds], 'r-', label='y = 5634 * (x - 0 * 0.0125)')
    plt.plot(ds, [5634 * math.pow(math.fabs(x-(-0.688)*0.0125), 1.5) for x in ds], 'r-', label='y = 5634 * (x - (-0.688 * 0.0125)', color = "pink")
    plt.errorbar(dsnum, means, stds, marker=".", markersize=5,
                 linewidth=1, linestyle='None', barsabove=True, ecolor="green", color="blue")
    # plt.errorbar(x, y, color="red", label="y="+ format(reg[0], '.2f') +" * x " + format(reg[1], '.2f'))
    plt.xticks(dsnum)
    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Caudal (Particulas/segundo)')
    plt.xlabel('Tamaño de ranura (m)')
    # plt.xlabel('N de particulas que cayeron')
    # plt.yscale("log")
    plt.legend(loc='best')
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

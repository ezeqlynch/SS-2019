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
    spliceFrom = [100, 250, 250]
    spliceTo = [600, 1600, 2000]
    files = [1, 125, 15]
    ds = ["0.1", "0.125", "0.15"]
    dsnum = [0.1, 0.125, 0.15]
    means = []
    stds = []
    colors = ["blue", "red", "green"]
    labels = ["d = 0.1m", "d = 0.125m", "d = 0.15m"]
    for i in range(len(files)):
        staticFile = open("./hourglass-data/0."+str(files[i])+"-1-times.stats", "r")
        staticFile2 = open("./hourglass-data/0."+str(files[i])+"-2-times.stats", "r")
        staticFile3 = open("./hourglass-data/0."+str(files[i])+"-3-times.stats", "r")
        staticFile4 = open("./hourglass-data/0."+str(files[i])+"-4-times.stats", "r")
        staticFile5 = open("./hourglass-data/0."+str(files[i])+"-5-times.stats", "r")

        times = []
        times2 = []
        times3 = []
        times4 = []
        times5 = []

        timesAvg = []
        timesAvg2 = []
        timesAvg3 = []
        timesAvg4 = []
        timesAvg5 = []

        fullAvg = []
        fullAvgErr = []

        for line in staticFile:
            times.append(float(line.replace(",",".")))
        timesAvg = running_mean(times, 100)
        timesAvg = timesAvg**-1

        for line in staticFile2:
            times2.append(float(line.replace(",",".")))
        timesAvg2 = running_mean(times2, 100)
        timesAvg2 = timesAvg2 ** -1

        for line in staticFile3:
            times3.append(float(line.replace(",",".")))
        timesAvg3 = running_mean(times3, 100)
        timesAvg3 = timesAvg3 ** -1
        
        for line in staticFile4:
            times4.append(float(line.replace(",",".")))
        timesAvg4 = running_mean(times4, 100)
        timesAvg4 = timesAvg4 ** -1
        
        for line in staticFile5:
            times5.append(float(line.replace(",",".")))
        timesAvg5 = running_mean(times5, 100)
        timesAvg5 = timesAvg5 ** -1

        for j in range(0, min(len(timesAvg), len(timesAvg2), len(timesAvg3), len(timesAvg4), len(timesAvg5))):
            avgArr = [timesAvg[j], timesAvg2[j],
                      timesAvg3[j], timesAvg4[j], timesAvg5[j]]
            fullAvg.append(np.mean(avgArr))
            fullAvgErr.append(np.std(avgArr))


        #for mean and std single value ->
        aux = np.concatenate((timesAvg[spliceFrom[i]:spliceTo[i]], timesAvg2[spliceFrom[i]:spliceTo[i]],
                              timesAvg3[spliceFrom[i]:spliceTo[i]], timesAvg4[spliceFrom[i]:spliceTo[i]], 
                              timesAvg5[spliceFrom[i]:spliceTo[i]]), axis=None)
        means.append(np.mean(aux))
        stds.append(np.std(aux))
        plt.title("Aproximación por Beverloo")
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
    ds = np.arange(0.1, 0.15, 0.0001)
    #
    plt.plot(ds, [254877 * math.pow(math.fabs(x-(4.208)*0.0125), 2.5)
                  for x in ds], 'r-', label='254877 * (x - 4.208*0.0125)^2.5')
    # plt.plot(ds, [5634 * math.pow(math.fabs(x-(-0.688)*0.0125), 1.5) for x in ds], 'r-', label='y = 5634 * (x - (-0.688 * 0.0125)', color = "pink")
    plt.errorbar(dsnum, means, stds, marker=".", markersize=5,
                 linewidth=1, linestyle='None', barsabove=True, ecolor="green", color="blue")
    # plt.errorbar(x, y, color="red", label="y="+ format(reg[0], '.2f') +" * x " + format(reg[1], '.2f'))
    plt.xticks(dsnum)
    # plt.axis([0, 5, -1, 1])
    plt.ylabel('Caudal (p/s)')
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
    # plt.savefig('fig3.png', bbox_inches='tight')

    # plt.savefig(parsedArgs.staticFile + 'deltaTime' +
    #             parsedArgs.delta + '.png', bbox_inches='tight')

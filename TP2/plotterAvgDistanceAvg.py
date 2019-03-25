import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import os


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/2333'
  )

  return parser


if __name__ == "__main__":

        # get parser
        parsedArgs = argumentParser().parse_args()
        allParticlesPerFrame = []
        allMaxDistances = []
        allAverages = []
        allSD = []


        for file in os.listdir(parsedArgs.staticFile):
            if file.endswith(".stats"):
                # staticFile = open("data/2333/2333-stats-%d.stats" % (i), "r")
                staticFile = open(os.path.join(parsedArgs.staticFile, file), "r")
                particlesPerFrame = []
                maxDistances = []
                averages = []
                sd = []

                time = 0

                for line in staticFile:
                        stepData = [s for s in line.split()]
                        # print(stepData)
                        if (len(stepData) == 1):
                                time = int(stepData[0])
                        else:
                                particlesPerFrame.append(int(stepData[0]))
                                maxDistances.append(float(stepData[1]))
                                averages.append(float(stepData[2]))
                                sd.append(float(stepData[3]))
                allParticlesPerFrame.append(particlesPerFrame)
                allMaxDistances.append(maxDistances)
                allAverages.append(averages)
                allSD.append(sd)
                plt.plot(range(len(averages)), averages, alpha=0.5)
        avgAvgDistances = []
        avgSD = []
        for i in range(0, len(allAverages[0])):
                a = []
                for j in range(0, 25):
                        a.append(allAverages[j][i])
                avgAvgDistances.append(np.mean(a))
                avgSD.append(np.std(a))

        # print(averages)

        # Plot histogram data
        plt.title('Number of particles over time') 
        plt.ylabel('Average distance to center') 
        plt.xlabel('Frame')  
        # plt.errorbar(range(len(averages)),
        #              averages, yerr=sd, fmt='none', ecolor='r')
        plt.plot(range(len(avgAvgDistances)),avgAvgDistances, color="black")
        # plt.tight_layout()
        plt.show()

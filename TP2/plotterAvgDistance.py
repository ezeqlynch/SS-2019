import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
# import PyQt5.QtGui


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/2,3,3,3.xyz'
  )

  return parser


if __name__ == "__main__":
        # get parser
        parsedArgs = argumentParser().parse_args()
        staticFile = open(parsedArgs.staticFile, "r")
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

        # print(averages)

        # Plot histogram data
        plt.title('Number of particles over time') 
        plt.ylabel('Average distance to center') 
        plt.xlabel('Frame')  
        plt.errorbar(range(len(averages)),
                     averages, yerr=sd, fmt='none', ecolor='pink')
        plt.plot(range(len(maxDistances)),maxDistances)
        plt.plot(range(len(averages)), averages, color="black")
        # plt.tight_layout()
        plt.show()

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

        # particleNum = int(staticFile.readline())

        for line in staticFile:
          stepData = [s for s in line.split()]
          if (len(stepData) == 1):
            time = int(stepData[0])
          else:
            particlesPerFrame.append(int(stepData[0]))


        # Plot histogram data
        plt.title('Number of particles over time') 
        plt.ylabel('# Particles') 
        plt.xlabel('Frame')  
        plt.plot(range(len(particlesPerFrame)), particlesPerFrame)
        plt.tight_layout()
        plt.show()

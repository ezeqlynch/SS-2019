import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker


# import PyQt5.QtGui


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/2333_2d/2333-stats-0.stats'
  )

  parser.add_argument(
      '--name',
      help="Path to the static data file.",
      default="Simulación 2233"
  )

  parser.add_argument(
      '--error',
      help="Show error in simulation\n\n",
      action='store_true'
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
        plt.title('Cantidad de particulas a lo largo del tiempo.') 
        plt.ylabel('Cantidad de particulas') 
        plt.xlabel('Iteración') 
        plt.grid(b=True, which='major', linestyle='-')
        plt.grid(b=True, which='minor', color="gray", linestyle='--')
        plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(250))
        plt.plot(range(len(particlesPerFrame)), particlesPerFrame)
        plt.tight_layout()
        plt.show()

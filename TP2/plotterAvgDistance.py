import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
# import PyQt5.QtGui
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from textwrap import wrap




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
      '--showError',
      help="Show error in simulation\n\n",
      action='store_true'
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
                if (len(particlesPerFrame) > 300):
                        break
                if (len(stepData) == 1):
                        time = int(stepData[0])
                else:
                        particlesPerFrame.append(int(stepData[0]))
                        maxDistances.append(float(stepData[1]))
                        averages.append(float(stepData[2]))
                        sd.append(float(stepData[3]))

        # print(averages)

        # Plot histogram data
        plt.title('Distancia promedio y maxima de las celdas vivas al centro.') 
        plt.ylabel('Distancia al centro (medida en celdas)') 
        plt.xlabel('Iteración')  
        plt.errorbar(range(len(averages)),
                     averages, yerr=sd, fmt='none', ecolor='pink')
        plt.plot(range(len(maxDistances)),maxDistances)
        plt.plot(range(len(averages)), averages, color="black")
        plt.grid(b=True, which='major', linestyle='-')
        plt.grid(b=True, which='minor', color="gray", linestyle='--')
        # plt.ylim(ymin=0)
        plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(5))


        black_patch = mpatches.Patch(color='black', label='Promedio')
        pink_patch = mpatches.Patch(color='pink', label='Error')
        blue_patch = mpatches.Patch(color='blue', label='Maximo')
        title = mpatches.Rectangle(
            (0, 0), 1, 1, fc="w", fill=False, edgecolor='none', linewidth=0, label=parsedArgs.name)
        first_legend = plt.legend(
                handles=[title, black_patch, pink_patch, blue_patch], loc=0)
        plt.tight_layout()
        plt.show()

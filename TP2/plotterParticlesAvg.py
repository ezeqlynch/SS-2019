import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import os
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from textwrap import wrap




def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/2333_2d/'
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
                        if (len(particlesPerFrame) > 100):
                                break
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
                if(not parsedArgs.showError):
                        plt.plot(range(len(particlesPerFrame)), particlesPerFrame, alpha=0.5)
        avgAvgPPF = []
        avgSD = []
        for i in range(0, len(allParticlesPerFrame[0])):
                a = []
                for j in range(0, 25):
                        if(i < len(allParticlesPerFrame[j])):
                                a.append(allParticlesPerFrame[j][i])
                        else:
                                a.append(0)
                avgAvgPPF.append(np.mean(a))
                avgSD.append(np.std(a))

        # print(averages)
        # Plot histogram data
        plt.ylabel('Cantidad de celdas vivas') 
        plt.xlabel('Iteración')  
        plt.grid(b=True, which='major', linestyle='-')
        plt.grid(b=True, which='minor', color="gray", linestyle='--')
        plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(1000))


        black_patch = mpatches.Patch(color='black', label='Promedio')
        title = mpatches.Rectangle((0, 0), 1, 1, fc="w", fill=False, edgecolor='none', linewidth=0, label=parsedArgs.name)
        if(parsedArgs.showError):
                plt.title("\n".join(wrap('Promedio de cantidad de celdas vivas a lo largo del tiempo en 25 simulaciones.',60)))
                pink_patch = mpatches.Patch(color='pink', label='Error')
                first_legend = plt.legend(handles=[title, black_patch, pink_patch], loc=0)
        else:
                plt.title("\n".join(wrap('Cantidad de celdas vivas a lo largo del tiempo en 25 simulaciones y su promedio.',60)))
                first_legend = plt.legend(handles=[title, black_patch], loc=0)

        plt.gca().add_artist(first_legend)
        
        # major_ticks = np.arange(0, 1000 + 1, 500)
        # minor_ticks = np.arange(0, 1000 + 1, 50)
        # plt.ylim(ymin=0)
        # plt.xlim(xmin=0)

        if(parsedArgs.showError):
                plt.errorbar(range(len(avgAvgPPF)), avgAvgPPF, yerr=avgSD, fmt='none', ecolor='pink')
        plt.plot(range(len(avgAvgPPF)),avgAvgPPF, color="black")
        plt.tight_layout()
        if(parsedArgs.showError):
                plt.savefig(parsedArgs.staticFile + 'ParticlesAvg'+ parsedArgs.name +'_error.png', bbox_inches='tight')
        else:
                plt.savefig(parsedArgs.staticFile + 'ParticlesAvg'+ parsedArgs.name +'.png', bbox_inches='tight')
        plt.show()

import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from particle import Particle
import numpy as np
import math


# import PyQt5.QtGui


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFiles',
      help="static data files.",
      nargs='+',
      default='data/cut.xyz'
  )
  parser.add_argument(
      '--names',
      help="static data files.",
      nargs='+',
      default='data/cut.xyz'
  )

  parser.add_argument(
      '--name',
      help="Path to the static data file.",
      default="100N"
  )
  parser.add_argument(
      '--delta',
      help="Path to the static data file.",
      default=1.0/100
  )
  parser.add_argument(
      '--density',
      help="Velocity at start\n\n",
      action='store_true'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    deltaTime = float(parsedArgs.delta)
    staticFiles = parsedArgs.staticFiles
    for fileIndex, staticFileName in enumerate(staticFiles, start=0):
        staticFile = open(staticFileName, "r")
        # outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")
        
        particles = dict()


        #PARSE PARTICLES
        particleNum = int(staticFile.readline())
        staticFile.readline()
        for i in range(particleNum):
            rawParticle = staticFile.readline().split(' ')
            particles[int(rawParticle[0])] = Particle(rawParticle[0],
                                                        rawParticle[1], rawParticle[2],
                                                        rawParticle[4], rawParticle[5],
                                                        rawParticle[3])
        staticFile.readline()

        collisionTimes = []
        collisionOnDeltaTime = dict()

        #PARSE COLLISIONS
        collisionNum = int(staticFile.readline())
        currentTime = 0
        for i in range(collisionNum):
            print("collision %i of %i" % (i+1, collisionNum), end="\r")
            nt = staticFile.readline()
            if(nt == ""):
                break
            staticFile.readline()
            nextTime = float(nt)
            timeTaken = nextTime-currentTime
            currentTime = nextTime
            collisionTimes.append(timeTaken)
            histogramDiv = math.floor(timeTaken/deltaTime)
            if histogramDiv not in collisionOnDeltaTime:
                collisionOnDeltaTime[histogramDiv] = 0
            collisionOnDeltaTime[histogramDiv] = collisionOnDeltaTime[histogramDiv] + 1
            staticFile.readline()
            staticFile.readline()
            staticFile.readline()
            staticFile.readline()
        staticFile.close()
        
        if(parsedArgs.density):
            plt.title('Probabilidad de colisión / tiempo')
            plt.ylabel('Probabilidad')
            plt.xlabel('Tiempo (s)')
            weights = np.ones_like(collisionTimes)/float(len(collisionTimes))
            y, x, _ = plt.hist(collisionTimes, bins=30, weights=weights,  edgecolor='black', linewidth=0.5, label="Vi < "+parsedArgs.names[fileIndex] + "m/s")
        else:
            plt.title('Cantidad de Colisiones / tiempo')
            plt.ylabel('Cantidad')
            plt.xlabel('Tiempo (s)')
            plt.hist(collisionTimes, bins=30)
    # plt.xticks(yAxis, xAxis)
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.ylim(ymin=0)
    plt.xlim(xmin=0, xmax=0.05)  # 50->0.8 100->0.3 250->0.05
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(5))

    # black_patch = mpatches.Patch(color='black', label='Promedio')
    # pink_patch = mpatches.Patch(color='pink', label='Error')
    # blue_patch = mpatches.Patch(color='blue', label='Maximo')
    # title = mpatches.Rectangle(
    #     (0, 0), 1, 1, fc="w", fill=False, edgecolor='none', linewidth=0, label=parsedArgs.name)
    # first_legend = plt.legend(
    #     handles=[title, black_patch, pink_patch, blue_patch], loc=0)
    plt.tight_layout()
    plt.legend(loc='best')
    # if(parsedArgs.density):
    #     plt.savefig(parsedArgs.staticFile + 'collitionsTime'+ parsedArgs.name +'.png', bbox_inches='tight')
    # else:
    #     plt.savefig(parsedArgs.staticFile + 'collitionsDensityTime'+ parsedArgs.name +'.png', bbox_inches='tight')
    plt.show()

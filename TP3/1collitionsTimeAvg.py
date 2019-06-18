import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from particle import Particle
import numpy as np
import math
import os



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

    # staticFile = open(parsedArgs.staticFile, "r")
    # outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")
    
    allParticles = []
    allCollisionTimes = []
    allCollisionDeltaTimes = []

    for fileIndex, staticFileName in enumerate(staticFiles, start=0):
        for file in os.listdir(staticFileName):
            if file.endswith(".stat"):
                # staticFile = open("data/2333/2333-stats-%d.stats" % (i), "r")
                staticFile = open(os.path.join(
                    staticFileName, file), "r")
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
                allParticles.append(particles)
                allCollisionTimes.append(collisionTimes)
                allCollisionDeltaTimes.append(collisionOnDeltaTime)

        # print(allCollisionTimes)
        avgCollisionTimes = []
        avgSD = []
        for i in range(0, len(max(allCollisionTimes))):
            a = []
            for j in range(0, 10):
                    if(i < len(allCollisionTimes[j])):
                            a.append(allCollisionTimes[j][i])
                    else:
                            a.append(0)
            avgCollisionTimes.append(np.mean(a))
            avgSD.append(np.std(a))

        # y, binEdges = np.histogram(avgCollisionTimes, bins=30)
        # bincenters = 0.5*(binEdges[1:]+binEdges[:-1])
        # width = 0.05
        # plt.bar(bincenters, y, width=width, color='r', yerr=avgSD)
        

        # Plot histogram data
        if(parsedArgs.density):
            plt.title('Probabilidad de Colisiones / tiempo')
            plt.ylabel('Probabilidad')
            plt.xlabel('Tiempo (s)')
            weights = np.ones_like(avgCollisionTimes)/float(len(avgCollisionTimes))
            y, x, _ = plt.hist(avgCollisionTimes, bins=30,
                               weights=weights, alpha=0.2,  edgecolor='black', linewidth=1, label="V="+parsedArgs.names[fileIndex])
        else:
            plt.title('Promedio de colisiones / tiempo.')
            plt.ylabel('Cantidad promedio')
            plt.xlabel('Tiempo (s)')
            # plt.hist(avgCollisionTimes, bins=30)
            plt.errorbar(range(len(avgCollisionTimes)),
                        avgCollisionTimes, yerr=avgSD, fmt='none', ecolor='pink')
    # plt.xticks(yAxis, xAxis)
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    # plt.ylim(ymin=0)
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

    # plt.savefig(parsedArgs.staticFile + 'collitionsTimeAvg' +
    #             parsedArgs.name + '.png', bbox_inches='tight')
    plt.show()

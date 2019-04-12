import pylab as plt
import argparse
from argparse import RawTextHelpFormatter
import matplotlib
# import matplotlib.pyplot as plt
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
      '--staticFile',
      help="Path to the static data file.",
      default='data/test.xyz'
  )

  parser.add_argument(
      '--name',
      help="Path to the static data file.",
      default="Simulación 2233"
  )
  parser.add_argument(
      '--delta',
      help="Path to the static data file.",
      default=1.0/100
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    deltaTime = float(parsedArgs.delta)
    # staticFile = open(parsedArgs.staticFile, "r")
    # outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")

    allParticles = []
    allCollisionTimes = []
    allCollisionDeltaTimes = []

    for file in os.listdir(parsedArgs.staticFile):
        if file.endswith(".stat"):
            # staticFile = open("data/2333/2333-stats-%d.stats" % (i), "r")
            staticFile = open(os.path.join(
                parsedArgs.staticFile, file), "r")
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
            staticFile.close()
            allParticles.append(particles)
            allCollisionTimes.append(collisionTimes)
            allCollisionDeltaTimes.append(collisionOnDeltaTime)

    # print(allCollisionTimes)
    avgCollisionTimes = []
    avgSD = []
    for i in range(0, len(allCollisionTimes[0])):
        a = []
        for j in range(0, 10):
                if(i < len(allCollisionTimes[j])):
                        a.append(allCollisionTimes[j][i])
                else:
                        a.append(0)
        avgCollisionTimes.append(np.mean(a))
        avgSD.append(np.std(a))
    
    print(len(avgCollisionTimes))
    print(len(avgSD))

    data = avgCollisionTimes
    y, binEdges = np.histogram(data, bins=30)
    bincenters = 0.5*(binEdges[1:]+binEdges[:-1])
    menStd = np.sqrt(y)
    width = 0.001
    plt.bar(bincenters, y, width=width, color='r', yerr=menStd)
    plt.show()

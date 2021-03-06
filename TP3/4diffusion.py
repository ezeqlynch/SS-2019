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
      '--staticFile',
      help="Path to the static data file.",
      default='data/test.xyz'
  )

  parser.add_argument(
      '--name',
      help="Path to the static data file.",
      default=0.16
  )

  parser.add_argument(
      '--chooseMax',
      help="Path to the static data file.",
      action='store_true'
  )
  parser.add_argument(
      '--xmax',
      help="Path to the static data file.",
      default=0.16
  )
  parser.add_argument(
      '--ymax',
      help="Path to the static data file.",
      default=0.1
  )
  parser.add_argument(
      '--start',
      help="Velocity at start\n\n",
      action='store_true'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    staticFile = open(parsedArgs.staticFile, "r")
    # outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")

    particles = dict()

    #PARSE PARTICLES
    particleNum = int(staticFile.readline())
    staticFile.readline()
    for i in range(particleNum):
        staticFile.readline()
        # rawParticle = staticFile.readline().split(' ')
        # particles[int(rawParticle[0])] = Particle(rawParticle[0],
        #                                             rawParticle[1], rawParticle[2],
        #                                             rawParticle[4], rawParticle[5],
        #                                             rawParticle[3])
    staticFile.readline()

    collisionTimes = []
    collisionOnDeltaTime = dict()

    #PARSE COLLISIONS
    collisionNum = int(staticFile.readline())
    currentTime = 0
    xBig = []
    yBig = []
    xSmall = []
    ySmall = []
    for i in range(collisionNum):
        print("collision %i of %i" % (i+1, collisionNum), end="\r")
        nt = staticFile.readline()
        if(nt == ""):
            break
        pos = staticFile.readline().split(' ')
        xBig.append(float(pos[1]))
        yBig.append(float(pos[2]))
        pos = staticFile.readline().split(' ')
        xSmall.append(float(pos[1]))
        ySmall.append(float(pos[2]))
        staticFile.readline()
        staticFile.readline()
        staticFile.readline()
    staticFile.close()




    # Calculate Velocity
    distanceFromCenter = []
    despCuadMed = []
    despCuadMed.append(0)
    for i in range(len(xBig)):
        distanceFromCenter.append(
            (xBig[i] - 0.25) ** 2 +
                (yBig[i] - 0.25) ** 2)

    for i in range(0, len(distanceFromCenter)):
        despCuadMed.append(despCuadMed[i-1] + 1/len(distanceFromCenter) * (distanceFromCenter[i] ** 2))
    print(despCuadMed)
    
    # diff = np.diff(distanceFromCenter)  # this calculates r(t + dt) - r(t)
    # diff_sq = diff**2
    # MSD = np.mean(diff_sq)
    # print(diff_sq)

    # Plot histogram data
    plt.title('Desplazamiento cuadratico medio en base a la colision.')
    plt.ylabel('Desplazamiento cuadratico medio')
    plt.xlabel('Numero de colision')
    # plt.errorbar(range(len(averages)),
    #                 averages, yerr=sd, fmt='none', ecolor='pink')
    # weights = np.ones_like(velocityModules)/float(len(velocityModules))
    # y, x, _ = plt.hist(velocityModules, bins=30, weights=weights)

    plt.plot(range(len(despCuadMed)), despCuadMed)

    # plt.xticks(yAxis, xAxis)
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.xlim(left=0)
    plt.ylim(bottom=0)
    if(parsedArgs.chooseMax):
        plt.xlim(left=0, right=parsedArgs.xmax)
        plt.ylim(bottom=0, top=parsedArgs.ymax)
    # plt.axes().yaxis.set_minor_locator(ticker.MultipleLocator(5))

    # black_patch = mpatches.Patch(color='black', label='Promedio')
    # pink_patch = mpatches.Patch(color='pink', label='Error')
    # blue_patch = mpatches.Patch(color='blue', label='Maximo')
    # title = mpatches.Rectangle(
    #     (0, 0), 1, 1, fc="w", fill=False, edgecolor='none', linewidth=0, label=parsedArgs.name)
    # first_legend = plt.legend(
    #     handles=[title, black_patch, pink_patch, blue_patch], loc=0)
    plt.tight_layout()
    plt.show()

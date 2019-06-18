import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
import numpy as np
from particle import Particle
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
      default='100N'
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
    x = []
    y = []
    deltaTime = 1
    last = 0
    for i in range(collisionNum):
        print("collision %i of %i" % (i+1, collisionNum), end="\r")
        nt = staticFile.readline()
        if(nt == ""):
            break
        pos = staticFile.readline().split(' ')
        if(float(nt) > last):
            y.append(math.sqrt( (0.25 - float(pos[1])) **2 + (0.25 - float(pos[2]))**2))
            x.append(float(nt))
            last += deltaTime
        # y.append(float(pos[2]))
        staticFile.readline() #particula chica
        staticFile.readline()  # choque 1
        staticFile.readline()  # choque 2
        staticFile.readline()  # blank
    staticFile.close()

    # Plot histogram data
    # k = 10
    # x2 = np.interp(np.arange(len(x) * k), np.arange(len(x)) * k, x)
    # y2 = np.interp(np.arange(len(y) * k), np.arange(len(y)) * k, y)
    print(y)
    fig, ax = plt.subplots(1, 1, figsize=(8, 8))
    # Now, we draw our points with a gradient of colors.
    ax.scatter(x, y,marker='.',)
    # ax.plot(x[0], y[0], 'go')
    # ax.plot(x[-1], y[-1], 'ro')
    # ax.axis('equal')
    # ax.title('Camino recorrido por particula grande')
    # ax.set_xlim(xmin=0.0, xmax=0.5)
    # ax.set_ylim(ymin=0.0, ymax=0.5)
    plt.title('Movimiento de la particula grande en el espacio 2D')
    plt.savefig(parsedArgs.staticFile + 'bigPath' +
                parsedArgs.name + '.png', bbox_inches='tight')

    # ax.set_axis_off()
    plt.show()

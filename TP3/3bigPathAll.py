import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
import matplotlib.cm as cm
import numpy as np
from particle import Particle
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
  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    # staticFile = open(parsedArgs.staticFile, "r")
    # outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")

    particles = dict()

    allParticles = []
    allX = []
    allY = []

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
                staticFile.readline()
            staticFile.readline()

            #PARSE COLLISIONS
            collisionNum = int(staticFile.readline())
            currentTime = 0
            x = []
            y = []
            for i in range(collisionNum):
                print("collision %i of %i" % (i+1, collisionNum), end="\r")
                nt = staticFile.readline()
                if(nt == ""):
                    break
                pos = staticFile.readline().split(' ')
                x.append(float(pos[1]))
                y.append(float(pos[2]))
                staticFile.readline()
                staticFile.readline()
                staticFile.readline()
            allX.append(x)
            allY.append(y)
            staticFile.close()


    fig, ax = plt.subplots(1, 1, figsize=(8, 8))
    for i in range(len(allX)):
        # Plot histogram data
        k = 10
        x2 = np.interp(np.arange(len(allX[i]) * k), np.arange(len(allX[i])) * k, allX[i])
        y2 = np.interp(np.arange(len(allY[i]) * k), np.arange(len(allY[i])) * k, allY[i])
        # Now, we draw our points with a gradient of colors.
        color = "b"
        if(i == 0):
            color = "b"
        if(i == 1):
            color = "g"
        if(i == 2):
            color = "r"
        if(i == 3):
            color = "c"
        if(i == 4):
            color = "m"
        if(i == 5):
            color = "y"
        if(i == 6):
            color = "k"
        if(i == 7):
            color = 'tab:pink'
        if(i == 8):
            color = 'tab:gray'
        if(i == 9):
            color = 'tab:orange'

        ax.scatter(x2, y2,  linewidths=0, c=color,
                   marker='o', s=2)
        ax.plot(allX[i][0], allY[i][0], 'ko')
        ax.plot(allX[i][-1], allY[i][-1], 'o', color=color)
    ax.axis('equal')
    # ax.title('Camino recorrido por particula grande')
    ax.set_xlim(left=0.0, right=0.5)
    ax.set_ylim(bottom=0.0, top=0.5)
    plt.title('Movimiento de la particula grande en el espacio 2D')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    # ax.set_axis_off()
    plt.show()

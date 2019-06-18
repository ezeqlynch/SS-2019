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
  parser.add_argument(
      '--name',
      help="Path to the static data file.",
      default='100N'
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
    maxColl = 0
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
            last = 0
            deltaTime = 10
            for i in range(collisionNum):
                # print("collision %i of %i" % (i+1, collisionNum), end="\r")
                nt = staticFile.readline()
                if(nt == ""):
                    break
                pos = staticFile.readline().split(' ')
                if (float(nt) > last):
                    y.append((0.25 - float(pos[1])) ** 2 + (0.25 - float(pos[2])) ** 2)
                    x.append(float(nt))
                    last += deltaTime
                # x.append(float(pos[1]))
                # y.append(float(pos[2]))
                staticFile.readline()  # particula chica
                staticFile.readline()  # choque 1
                staticFile.readline()  # choque 2
                staticFile.readline()  # blank
            if(len(x) > maxColl):
                maxColl = len(x)
            allX.append(x)
            allY.append(y)
            staticFile.close()

    for i in range(len(allX)):
        if(len(allX[i]) < maxColl):
            lastDis = allY[i][-1]
            for j in range(maxColl - len(allX[i])):
                allY[i].append(lastDis)


    avgX = []
    for i in range(len(allY)):
        for j in range(len(allY[i])):
            if(i == 0):
                avgX.append(allY[i][j]/10)
            else:
                avgX[j] += allY[i][j]/10

    sdX = []
    for i in range(len(allY)):
        for j in range(len(allY[i])):
            if(i == 0):
                sdX.append(1/9 * (allY[i][j] - avgX[j])**2)
            else:
                sdX[j] +=  1/9 * (allY[i][j] - avgX[j])**2

    for i in range(len(sdX)):
        sdX[i] = math.sqrt(sdX[i])


    cVal = -1
    cValErr = 999999999
    c = 0
    error = []
    errZero = 0
    while c < 0.001:
        suma = 0
        for i in range(0, 1):
            sub = avgX[i] - c * allX[0][i]
            suma = suma + math.pow(sub, 2)  # sumatoria de ^ al cuadrado para obtener error
        error.append(suma)
        if (c == 0):
            errZero = suma
        if (cValErr > suma):
            cVal = c
            cValErr = suma
        c += 0.00000001

    cVal2 = -1
    cValErr2 = 999999999
    error2 = []
    errZero2 = 0
    c=0
    while c < 0.001:
        suma = 0
        for i in range(len(allX[0])):
            sub = avgX[i] - c * allX[0][i]
            suma = suma + math.pow(sub, 2)  # sumatoria de ^ al cuadrado para obtener error
        error.append(suma)
        if (c == 0):
            errZero2 = suma
        if (cValErr2 > suma):
            cVal2 = c
            cValErr2 = suma
        c += 0.00000001

    print(cVal)
    print(cVal2)
    auxTimes= np.arange(0, 220, 0.5)
    auxTimes2= np.arange(0, 10 * len(allX[0]), 0.5)
    fig, ax = plt.subplots(1, 1, figsize=(8, 8))
    ax.plot(auxTimes2, [cVal2*x for x in auxTimes2], 'r-', label="y={:.5f}x, E(D)={:2f}".format(cVal2, cValErr2))
    ax.plot(auxTimes, [cVal*x for x in auxTimes], 'g-', label="y={:.5f}x, E(D)={:2f}".format(cVal, cValErr))
    # ax.plot(allX[0], avgX, '.')
    ax.errorbar(allX[0], avgX, sdX, marker="o", markersize=5,
                 linewidth=2, linestyle='None')

    plt.ylabel('DCM(m^2)')
    plt.xlabel('Tiempo(s)')
    plt.title('Desplazamiento Cuadrático Medio')
    plt.grid(b=True, which='major', linestyle='-')
    plt.grid(b=True, which='minor', color="gray", linestyle='--')
    plt.legend(loc='best')
    plt.savefig(parsedArgs.staticFile + 'bigPathAll' +
                parsedArgs.name + '.png', bbox_inches='tight')
    # ax.set_axis_off()
    plt.show()

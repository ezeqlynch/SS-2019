import argparse
from argparse import RawTextHelpFormatter
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.ticker as ticker
from particle import Particle


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
      default=1.0/24
  )

  return parser


if __name__ == "__main__":
        # get parser
        parsedArgs = argumentParser().parse_args()
        deltaTime = float(parsedArgs.delta)
        staticFile = open(parsedArgs.staticFile, "r")
        outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")

        particles = dict()
        currentTime = 0
        while True:
            try:
                pnStr = staticFile.readline()
                if not pnStr:
                    break
                particleNum = int(pnStr)
                nextTime = float(staticFile.readline())
                while(currentTime < nextTime):
                    outputFile.write("%i\n"% particleNum)
                    outputFile.write("%f\n"% currentTime)
                    for key in particles:
                        outputFile.write("%f %f %f %f %f\n" % (
                            particles[key].posX, 
                            particles[key].posY, 
                            particles[key].velX, 
                            particles[key].velY, 
                            particles[key].radius))
                        particles[key].posX = particles[key].posX + particles[key].velX * deltaTime
                        particles[key].posY = particles[key].posY + particles[key].avelY * deltaTime
                    currentTime += deltaTime
                for i in range(particleNum):
                    rawParticle = staticFile.readline().split(' ')
                    particles[i] = Particle(i, 
                        rawParticle[0], rawParticle[1], 
                        rawParticle[2], rawParticle[3], 
                        rawParticle[4])
                
            except EOFError:
                print("Reached EOF")
                break
        staticFile.close()
        outputFile.close()

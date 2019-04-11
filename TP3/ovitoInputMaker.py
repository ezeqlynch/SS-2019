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
      default=1.0/100
  )

  return parser


if __name__ == "__main__":
        # get parser
        parsedArgs = argumentParser().parse_args()
        deltaTime = float(parsedArgs.delta)
        staticFile = open(parsedArgs.staticFile, "r")
        outputFile = open("%s_ovito.xyz" % (parsedArgs.staticFile[:-4]), "w")

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

        #PARSE COLLISIONS
        collisionNum = int(staticFile.readline())
        currentTime = 0
        for i in range(collisionNum):
            print("collision %i of %i" % (i+1, collisionNum), end="\r")
            nt = staticFile.readline()
            if(nt == ""):
                break
            nextTime = float(nt)
            while(currentTime + deltaTime < nextTime):
                outputFile.write("%i\n"% (particleNum+2))
                outputFile.write("%f\n"% currentTime)
                for key in particles:
                    outputFile.write("%f %f %f %f %f\n" % (
                        particles[key].posX, 
                        particles[key].posY, 
                        particles[key].velX, 
                        particles[key].velY, 
                        particles[key].radius))
                    particles[key].posX = particles[key].posX + particles[key].velX * deltaTime
                    particles[key].posY = particles[key].posY + particles[key].velY * deltaTime
                outputFile.write("%f %f %f %f %f\n" % (
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.000001))
                outputFile.write("%f %f %f %f %f\n" % (
                    0.5,
                    0.5,
                    0.0,
                    0.0,
                    0.000001))
                currentTime += deltaTime
            # print()
            for i in range(2):
                rawCollision = staticFile.readline().split(' ')
                rawCollision[0] = int(rawCollision[0])
                if(rawCollision[0] < 0):
                    break
                rawCollision[1] = float(rawCollision[1])
                rawCollision[2] = float(rawCollision[2])
                # print(rawCollision)
                # print(particles[rawCollision[0]])
                particles[rawCollision[0]].posX = particles[rawCollision[0]].posX + particles[rawCollision[0]].velX * (nextTime-currentTime)
                particles[rawCollision[0]].posY = particles[rawCollision[0]].posY + particles[rawCollision[0]].velY * (nextTime-currentTime)
                # print(particles[rawCollision[0]].velX)
                particles[rawCollision[0]].velX = rawCollision[1] 
                particles[rawCollision[0]].velY = rawCollision[2]
                # print(particles[rawCollision[0]].velX)
                particles[rawCollision[0]].posX = particles[rawCollision[0]].posX + particles[rawCollision[0]].velX * (currentTime+deltaTime-nextTime)
                particles[rawCollision[0]].posY = particles[rawCollision[0]].posY + particles[rawCollision[0]].velY * (currentTime+deltaTime-nextTime)
            staticFile.readline()
        staticFile.close()
        outputFile.close()

import argparse
from argparse import RawTextHelpFormatter
from particle import Particle
from markerupdater import MarkerUpdater
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

import numpy as np


def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/output.txt'
  )

  parser.add_argument(
      '--neighboursFile',
      help="Path to the neighbours file.",
      default='data/vecins.txt'
  )
  
  parser.add_argument(
      '--interactable',
      help="Test flag to show graphics and check error\n\n",
      action='store_true'
  )

  return parser



def redraw(newId):
  # plt.cla()
  ax.clear()
  idAnalized = newId
  c = list(map(lambda particle: 'black', particles))
  c[idAnalized] = 'g'
  for vecin in particles[idAnalized].vecins:
    c[vecin] = 'r'
  
  for p in particles:
    circle = plt.Circle((p.posX, p.posY),
                        p.radius, color=c[p.id], fill=True)
    ax.add_artist(circle)
  coll = ax.scatter(x, y, s, c,  marker='o', picker=True)
  ax.set_title("Particle neighbours")
  plt.xlabel('x coordinates')
  plt.ylabel('y coordinates')
  # ax.grid(True)
  # Major ticks every 20, minor ticks every 5
  major_ticks = np.arange(0, size + 1, cellSize)
  minor_ticks = np.arange(0, size + 1, cellSize/2)

  ax.set_xticks(major_ticks)
  ax.set_xticks(minor_ticks, minor=True)
  ax.set_yticks(major_ticks)
  ax.set_yticks(minor_ticks, minor=True)
  ax.set_aspect('equal', 'box')
  plt.axis([0, size, 0, size])
  red_patch = mpatches.Patch(color='red', label='Neighbours')
  black_patch = mpatches.Patch(color='black', label='Not neighbours')
  green_patch = mpatches.Patch(color='green', label='Selected Particle')
  circle_patch = mpatches.Circle((0.5, 0.5), 0.1, facecolor="white",
                                 edgecolor="green", linewidth=1, label='Rc + Selected Radius')
  first_legend = plt.legend(handles=[red_patch, black_patch, green_patch, circle_patch],
             bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)

  plt.gca().add_artist(first_legend)

  # green_patch2 = mpatches.Patch(color='green', label=particles[idAnalized])
  # plt.legend(handles=[green_patch2],
  #            bbox_to_anchor=(1.05, 0), loc=3, borderaxespad=0.)


  # Or if you want different settings for the grids:
  # ax.grid(which='minor', alpha=0.2)
  # ax.grid(which='major', alpha=0.5)

  # And a corresponding grid
  ax.grid(which='both')

  # mplcursors.cursor(hover=True)
  fig.tight_layout()
  
  circle = plt.Circle((particles[idAnalized].posX, particles[idAnalized].posY), rc + particles[idAnalized].radius, color='g', fill=False)
  ax.add_artist(circle)
  if edges:
    circle = plt.Circle((particles[idAnalized].posX+size, particles[idAnalized].posY-size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX+size, particles[idAnalized].posY), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX+size, particles[idAnalized].posY+size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX, particles[idAnalized].posY-size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX, particles[idAnalized].posY+size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX-size, particles[idAnalized].posY-size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX-size, particles[idAnalized].posY), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)
    circle = plt.Circle((particles[idAnalized].posX-size, particles[idAnalized].posY+size), rc + particles[idAnalized].radius, color='g', fill=False)
    ax.add_artist(circle)




def on_pick(event):
  print('Selected', particles[event.ind[0]])
  print('Neighbours:', particles[event.ind[0]].vecins)
  print()
  # coll._facecolors[event.ind[0],:] = (0, 1, 0, 1)
  redraw(event.ind[0])
  fig.canvas.draw()


if __name__ == "__main__":
  # get parser
  parsedArgs = argumentParser().parse_args()

  my_updater = MarkerUpdater()

  staticFile = open(parsedArgs.staticFile, "r")
  particles = list()
  particleNum = int(staticFile.readline())
  size = float(staticFile.readline())
  cellSize = int(staticFile.readline())
  rc = float(staticFile.readline())
  edges = staticFile.readline() != 'false\n'
  for x in staticFile:
    rawParticle = x.split(' ')
    particles.append(Particle(rawParticle[0], float(
        rawParticle[2]), float(rawParticle[3]), rawParticle[1]))
  # print(particles)

  outputFile = open(parsedArgs.neighboursFile, "r")
  endTime = int(outputFile.readline())
  for x in outputFile:
    vecinsIds = x.split(' ')
    vecinsIds.pop()
    vecinsIds = list(map(lambda idStr: int(idStr), vecinsIds))
    particleId = vecinsIds.pop(0)
    particles[particleId].vecins = vecinsIds

  idAnalized = 0

  x = list(map(lambda particle: particle.posX, particles))
  y = list(map(lambda particle: particle.posY, particles))
  s = list(map(lambda particle: particle.radius * 100, particles))
  c = list(map(lambda particle: 'black', particles))
  c[idAnalized] = 'g'
  for vecin in particles[idAnalized].vecins:
    c[vecin] = 'r'
  # print(particles[idAnalized].vecins)
  # c = list(map(lambda particle : 'black' if particle.posX>rc or particle.posY>rc else 'red', particles))

  fig, ax = plt.subplots()
  fig.canvas.mpl_connect('pick_event', on_pick)

  redraw(0)
  # my_updater.add_ax(ax, ['size', 'pick_event'])  # scatter plot, only marker size
  plt.show()

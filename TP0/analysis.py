from particle import Particle
import matplotlib.pyplot as plt
import numpy as np
import mplcursors


staticFile = open("data/output.txt", "r")
particles = list()
particleNum = int(staticFile.readline())
size = float(staticFile.readline())
cellSize = int(staticFile.readline())
rc = float(staticFile.readline())
for x in staticFile:
  rawParticle = x.split(' ')
  particles.append(Particle(rawParticle[0], float(rawParticle[2]), float(rawParticle[3]), rawParticle[1]))
# print(particles)

outputFile = open("data/vecins.txt", "r")
for x in outputFile:
  vecinsIds = x.split(' ')
  vecinsIds.pop()
  vecinsIds = list(map(lambda idStr : int(idStr), vecinsIds))
  particleId = vecinsIds.pop(0)
  particles[particleId].vecins = vecinsIds

idAnalized = 33

x = list(map(lambda particle : particle.posX, particles))
y = list(map(lambda particle : particle.posY, particles))
s = list(map(lambda particle : 1, particles))
c = list(map(lambda particle : 'black', particles))
for vecin in particles[idAnalized].vecins:
  c[vecin] = 'red'
# print(particles[idAnalized].vecins)
# c = list(map(lambda particle : 'black' if particle.posX>rc or particle.posY>rc else 'red', particles))

# PLOTTING
fig, ax = plt.subplots()
ax.scatter(x, y, s, c)
ax.set_title("Mouse over a point")
# ax.grid(True)
# Major ticks every 20, minor ticks every 5
major_ticks = np.arange(0, size + 1, 10)
minor_ticks = np.arange(0, size + 1, 5)

ax.set_xticks(major_ticks)
ax.set_xticks(minor_ticks, minor=True)
ax.set_yticks(major_ticks)
ax.set_yticks(minor_ticks, minor=True)
ax.set_aspect('equal', 'box')
plt.axis([0, size, 0, size])

# Or if you want different settings for the grids:
# ax.grid(which='minor', alpha=0.2)
# ax.grid(which='major', alpha=0.5)

# And a corresponding grid
ax.grid(which='both')

# mplcursors.cursor(hover=True)
fig.tight_layout()

# ADD CIRCLE
circle = plt.Circle((particles[idAnalized].posX, particles[idAnalized].posY), rc, color='g', fill=False)
ax.add_artist(circle)

plt.show()


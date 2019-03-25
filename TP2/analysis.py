from random import randint

particleNum = 10
gridSize = 8
grid = []
#Create a 8x8 grid
for row in range(gridSize):
    grid.append([])
    for col in range(gridSize):
        grid[row].append(False)

for coins in range(particleNum):
    c_x = randint(0, len(grid)-1)
    c_y = randint(0, len(grid[0])-1)
    while grid[c_x][c_y]:
        c_x = randint(0, len(grid) - 1)
        c_y = randint(0, len(grid[0]) - 1)
    grid[c_x][c_y] = True

for line in grid:
  print(line)

fo = open("foo.xyz", "w+")
print("Name of the file: ", fo.name)
fo.write("%d\n" % (particleNum))
fo.write("\n")

for i, line in enumerate(grid):
  for j, point in enumerate(line):
    if(point):
      fo.write("'black' %d %d\n" % (i,j))

fo.close()

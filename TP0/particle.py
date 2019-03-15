class Particle:
  def __init__(self, id, posX, posY, radius = 1):
    self.id = int(id)
    self.posX = float(posX)
    self.posY = float(posY)
    self.radius = float(radius)
    self.vecins = []
  
  def __repr__(self):
    return "%d: x=%.2f, y=%.2f, r=%.2f" % (self.id, self.posX, self.posY, self.radius)
class Particle:
  def __init__(self, id, posX, posY):
    self.id = int(id)
    self.posX = int(posX)
    self.posY = int(posY)
  
  def __repr__(self):
    return "x=%d, y=%d" % (self.posX, self.posY)
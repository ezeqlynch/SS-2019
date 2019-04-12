class Particle:
  def __init__(self, id, posX, posY, velX, velY, radius=0.1):
    self.id = int(id)
    self.posX = float(posX)
    self.posY = float(posY)
    self.velX = float(velX)
    self.velY = float(velY)
    self.radius = float(radius)
    self.vecins = []
  
  def __repr__(self):
    return "%d: x=%.2f, y=%.2f, vx=%.2f, vy=%.2f, r=%.2f" % (self.id, self.posX, self.posY, self.velX, self.velY, self.radius)

  def setVelX(self,vel):
    self.velX = float(vel)
  
  def setVelY(self, vel):
    self.velY = float(vel)

  def setPosX(self,pos):
    self.posX = float(pos)
  
  def setPosY(self, pos):
    self.posY = float(pos)

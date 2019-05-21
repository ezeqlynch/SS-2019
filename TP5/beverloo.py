import numpy as np
import math

d = [0.15, 0.175, 0.2, 0.225,0.25]
Fx = [303.494,383.917,499.467,660.963,740.148]

cVal = -1
cValErr = -1
c = -3
while c < 3:
    suma = 0
    for i in range(len(d)):
        sub = Fx[i] - 5150 * math.sqrt(math.pow(math.fabs(d[i]-c*0.0125), 3))
        suma = suma + math.pow(sub,2)
    if(cValErr > suma):
        cVal = c
        cValErr = suma
    c+=0.001
print(cVal)
print(cValErr)

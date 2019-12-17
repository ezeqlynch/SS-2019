import numpy as np
import math
import matplotlib
import matplotlib.pyplot as plt


d = [0.1, 0.125, 0.15]
Fx = [84.25308915054367, 193.85788906580865, 431.2780399176422]
cVal = -1
cValErr = 999999999
c = 0
error = []
cs = []
errZero = 0
while c < 8:
    suma = 0
    for i in range(0,len(d)):
        sub = Fx[i] - (254877 * math.pow(d[i]-c*0.0125, 2.5))
        suma = suma + math.pow(sub,2) #sumatoria de ^ al cuadrado para obtener error
    error.append(suma)
    cs.append(c)
    if(c == 0):
        errZero = suma
    if(cValErr > suma):
        cVal = c
        cValErr = suma
    c+=0.001
suma = 0
for i in range(0,len(d)):
    sub = Fx[i] - (254877 * math.pow(d[i]-0*0.0125, 2.5))
    suma = suma + math.pow(sub,2) #sumatoria de ^ al cuadrado para obtener error
errZero = suma
plt.title("Error de ajuste de c")
plt.xlabel("c")
plt.ylabel("Error de ajuste")
plt.plot(cs, error)
text= "c={:.3f}, error={:.3f}".format(cVal, cValErr)
bbox_props = dict(boxstyle="square,pad=0.3", fc="w", ec="k", lw=0.72)
arrowprops=dict(arrowstyle="->",connectionstyle="angle,angleA=0,angleB=90")
kw = dict(xycoords='data',textcoords="axes fraction",
              arrowprops=arrowprops, bbox=bbox_props, ha="right", va="top")
plt.annotate(text, xy=(cVal, cValErr), xytext=(0.7,0.7), **kw)
plt.grid(b=True, which='major', linestyle='-', axis='y')
plt.show()
print(cVal)
print(cValErr)

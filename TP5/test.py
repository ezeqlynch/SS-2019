import numpy as np
import math
import matplotlib
import matplotlib.pyplot as plt


d = [0.15, 0.175, 0.2, 0.225,0.25]
Fx = [330.3869178239104, 418.0200090394148, 517.6873880957608, 627.2069402731283, 807.7456232678642]

cVal = -1
cValErr = 999999999
c = -3
error = []
cs = []
errZero = 0
while c < 5:
    suma = 0
    for i in range(0,len(d)):
        sub = Fx[i] - (5634 * math.pow(d[i]-c*0.0125, 1.5))
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
    sub = Fx[i] - (5634 * math.pow(d[i]-0*0.0125, 1.5))
    suma = suma + math.pow(sub,2) #sumatoria de ^ al cuadrado para obtener error
errZero = suma
plt.title("Error de ajuste de c")
plt.xlabel("c")
plt.ylabel("Error de ajuste")
plt.plot(cs, error)
text= "x={:.3f}, y={:.3f}".format(cVal, cValErr)
text2= "x={:.3f}, y={:.3f}".format(0, errZero)
bbox_props = dict(boxstyle="square,pad=0.3", fc="w", ec="k", lw=0.72)
arrowprops=dict(arrowstyle="->",connectionstyle="angle,angleA=0,angleB=90")
kw = dict(xycoords='data',textcoords="axes fraction",
              arrowprops=arrowprops, bbox=bbox_props, ha="right", va="top")
plt.annotate(text, xy=(cVal, cValErr), xytext=(0.4,0.4), **kw)
plt.annotate(text2, xy=(0, errZero), xytext=(0.4, 0.5), **kw)
plt.grid(b=True, which='major', linestyle='-', axis='y')
plt.show()
print(cVal)
print(cValErr)
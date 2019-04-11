import numpy as np
import matplotlib.pyplot as plt
# %matplotlib inline

n = 5000
x = np.cumsum(np.random.randn(n))
y = np.cumsum(np.random.randn(n))

# We add 10 intermediary points between two
# successive points. We interpolate x and y.
k = 10
x2 = np.interp(np.arange(n * k), np.arange(n) * k, x)
y2 = np.interp(np.arange(n * k), np.arange(n) * k, y)
fig, ax = plt.subplots(1, 1, figsize=(8, 8))
# Now, we draw our points with a gradient of colors.
ax.scatter(x2, y2, c=range(n * k), linewidths=0,
           marker='o', s=3, cmap=plt.cm.jet,)
ax.axis('equal')
ax.plot(x[0], y[0], 'go')
ax.plot(x[-1], y[-1], 'ro')
# ax.set_axis_off()
plt.show()

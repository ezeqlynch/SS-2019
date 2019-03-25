import matplotlib
import matplotlib.pyplot as plt
# import PyQt5.QtGui

staticFile = open("./output5.xyz", "r")
particlesPerFrame = []

# particleNum = int(staticFile.readline())

for x in staticFile:
    line = [int(s) for s in x.split() if s.isdigit()]
    if (len(line) == 1):
        particlesPerFrame.append(int(line[0]))


#  Compute plot size in inches (DPI determines label size)
dpi = 80
# plot_width = 0.5 * viewport_width / dpi
# plot_height = 0.5 * viewport_height / dpi


# Plot histogram data
plt.bar(range(len(particlesPerFrame)-1), particlesPerFrame)
plt.tight_layout()
plt.show()

# Render figure to an in-memory buffer.
# buf = fig.canvas.print_to_buffer()

# Create a QImage from the memory buffer
# res_x, res_y = buf[1]
# img = PyQt5.QtGui.QImage(buf[0], res_x, res_y, PyQt5.QtGui.QImage.Format_RGBA8888)

# Paint QImage onto rendered viewport 
# painter.drawImage(0,0,img)
import matplotlib
import matplotlib.pyplot as plt
import PyQt5.QtGui
import ovito

# Activate 'agg' backend for off-screen plotting.
matplotlib.use('Agg')

def render(painter, **args):

	# Find the existing HistogramModifier in the pipeline 
	# and get its histogram data.
	node = ovito.dataset.selected_node
	data = node.compute()
	num_particles = (data.number_of_particles if node else 0)
	text2 = "{} particles".format(num_particles)
	painter.drawText(10, painter.window().height() - 10, text2)



	
	# Get size of rendered viewport image in pixels.
	viewport_width = painter.window().width()
	viewport_height = painter.window().height()
	
	#  Compute plot size in inches (DPI determines label size)
	dpi = 80
	plot_width = 0.5 * viewport_width / dpi
	plot_height = 0.5 * viewport_height / dpi
	
	# Create figure
	fig, ax = plt.subplots(figsize=(plot_width,plot_height), dpi=dpi)
	fig.patch.set_alpha(0.5)
	plt.title('Coordination')
	
	# Plot histogram data
	ax.bar(x, y)
	plt.tight_layout()
	
	# Render figure to an in-memory buffer.
	buf = fig.canvas.print_to_buffer()
	
	# Create a QImage from the memory buffer
	res_x, res_y = buf[1]
	img = PyQt5.QtGui.QImage(buf[0], res_x, res_y, PyQt5.QtGui.QImage.Format_RGBA8888)
	
	# Paint QImage onto rendered viewport 
	painter.drawImage(0,0,img)

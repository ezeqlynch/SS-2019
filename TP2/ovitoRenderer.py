import ovito

# This user-defined function is called by OVITO to let it draw arbitrary graphics on top of the viewport.
# It is passed a QPainter (see http://qt-project.org/doc/qt-5/qpainter.html).
def render(painter, **args):
	
	# This demo code prints the current animation frame into the upper left corner of the viewport.
	text1 = "Frame {}".format(ovito.dataset.anim.current_frame)
	painter.drawText(10, 10 + painter.fontMetrics().ascent(), text1)

	# Also print the current number of particles into the lower left corner of the viewport.
	node = ovito.dataset.selected_node
	num_particles = (node.compute().number_of_particles if node else 0)
	text2 = "{} particles".format(num_particles)
	painter.drawText(10, painter.window().height() - 10, text2)

	
	# Print to the log window:
	print(text1)
	print(text2)

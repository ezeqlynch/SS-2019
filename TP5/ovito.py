import ovito
from PyQt5.QtCore import *
from PyQt5.QtGui import *
import numpy as np

def draw_line(painter, x1, x2):
	painter.drawLine(x1[0],x1[1],x2[0],x2[1])
	
def project_point(xyz, painter, args):
	view_tm = args['view_tm'] # 3x4 matrix
	proj_tm = args['proj_tm'] # 4x4 matrix
	world_pos = np.append(xyz, 1) # Convert to 4-vector.
	view_pos = np.dot(view_tm, world_pos) # Transform to view space.	
	# Check if point is behind the viewer. If yes, stop here.
	if args['is_perspective'] and view_pos[2] >= 0.0: return None
	# Project to screen space:
	screen_pos = np.dot(proj_tm, np.append(view_pos, 1)) 
	screen_pos[0:3] /= screen_pos[3]
	win_rect = painter.window()
	x = win_rect.left() + win_rect.width() * (screen_pos[0] + 1) / 2
	y = win_rect.bottom() - win_rect.height() * (screen_pos[1] + 1) / 2 + 1
	return (x,y)	

# This user-defined function is called by OVITO to let it draw arbitrary graphics on top of the viewport.
# It is passed a QPainter (see http://qt-project.org/doc/qt-5/qpainter.html).
def render(painter, **args):

	node = ovito.dataset.selected_node
	positions = node.compute().particle_properties.position.array
	text1 = "Frame {}".format(ovito.dataset.anim.current_frame)
	pen = QPen(Qt.SolidLine)
	pen.setWidth(3)
	pen.setColor(QColor(255,255,255))
	painter.setPen(pen)
	painter.drawText(10, 10 + painter.fontMetrics().ascent(), text1)
	
	rendij = 0.125
	#cuadrado inferior
	x1 = project_point([0, 0, 1.5], painter, args) 
	x2 = project_point([0, 0.4, 1.5], painter, args)
	x3 = project_point([0.4, 0.4, 1.5], painter, args)
	x4 = project_point([0.4, 0, 1.5], painter, args)
	
	x5 = project_point([0, 0, 1], painter, args)
	x6 = project_point([0, 0.4, 1], painter, args)
	x7 = project_point([0.4, 0.4, 1], painter, args)
	x8 = project_point([0.4, 0, 1], painter, args)
	
	x9 = project_point([0.4/2-rendij/2, 0.4/2-rendij/2, 0.75], painter, args)
	x10 = project_point([0.4/2-rendij/2, 0.4/2+rendij/2, 0.75], painter, args)
	x11 = project_point([0.4/2+rendij/2, 0.4/2+rendij/2, 0.75], painter, args)
	x12 = project_point([0.4/2+rendij/2, 0.4/2-rendij/2, 0.75], painter, args)
	
	x13 = project_point([0, 0, 0.5], painter, args)
	x14 = project_point([0, 0.4, 0.5], painter, args)
	x15 = project_point([0.4, 0.4, 0.5], painter, args)
	x16 = project_point([0.4, 0, 0.5], painter, args)
	
	x17 = project_point([0, 0, 0], painter, args)
	x18 = project_point([0, 0.4, 0], painter, args)
	x19 = project_point([0.4, 0.4, 0], painter, args)
	x20 = project_point([0.4, 0, 0], painter, args)
	
	draw_line(painter, x1, x2)
	draw_line(painter, x1, x4)
	draw_line(painter, x1, x5)
	draw_line(painter, x2, x3)
	draw_line(painter, x2, x6)
	draw_line(painter, x3, x4)
	draw_line(painter, x3, x7)
	draw_line(painter, x4, x8)
	
	draw_line(painter, x5, x6)
	draw_line(painter, x5, x8)
	draw_line(painter, x5, x9)
	draw_line(painter, x6, x7)
	draw_line(painter, x6, x10)
	draw_line(painter, x7, x8)
	draw_line(painter, x7, x11)
	draw_line(painter, x8, x12)
	
	draw_line(painter, x9, x10)
	draw_line(painter, x9, x12)
	draw_line(painter, x9, x13)
	draw_line(painter, x10, x11)
	draw_line(painter, x10, x14)
	draw_line(painter, x11, x12)
	draw_line(painter, x11, x15)
	draw_line(painter, x12, x16)
	
	draw_line(painter, x13, x14)
	draw_line(painter, x13, x16)
	draw_line(painter, x13, x17)
	draw_line(painter, x14, x15)
	draw_line(painter, x14, x18)
	draw_line(painter, x15, x16)
	draw_line(painter, x15, x19)
	draw_line(painter, x16, x20)
	
	draw_line(painter, x17, x18)
	draw_line(painter, x17, x20)
	draw_line(painter, x18, x19)
	draw_line(painter, x19, x20)
	
	
	
	
	
	# This demo code prints the current animation frame into the upper left corner of the viewport.
	
	
	
	#painter.drawLine(xy5[0],xy5[1],xy8[0],xy8[1])
	#painter.drawLine(xy6[0],xy6[1],xy7[0],xy7[1])
	
	font = QFont(painter.font())
	font.setPixelSize(50)
	painter.setFont(font)
	time = ovito.dataset.anim.current_frame*0.016
	text1 = "Time {:.2f}s".format(time)
	painter.drawText(10, painter.window().height() - 60, text1)
	
	node = ovito.dataset.selected_node
	count = 0
	for i in node.compute().particle_properties.position.array:
		if(i[2] < 0.75):
			count = count + 1
	
	num_particles = (node.compute().number_of_particles if node else 0)
	text2 = "Out  {} Flow {:.2f}".format(count, count/time)
	painter.drawText(10, painter.window().height() - 10, text2)
	


	# Print to the log window:
	print(text1)
	print(text2)
	#print(xy)


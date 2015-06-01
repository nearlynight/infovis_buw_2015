package infovis.scatterplot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private int xPress;
	private int yPress;

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		xPress = arg0.getX();
		yPress = arg0.getY();
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		view.updateMarkerRect(xPress, yPress, x, y);
		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}

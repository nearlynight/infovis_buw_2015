package infovis.paracoords;

import infovis.scatterplot.Model;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	Shape currentShape = null;
	
	private boolean markerMode   = false;
	private boolean moveAxisMode = false;
	private int activeAxis = 0;
	private int clickedX = 0;
	private int clickedY = 0;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if(view.aboveAxis(e.getX(), e.getY()) != -1){
			// move axes
			moveAxisMode = true;
			activeAxis = view.aboveAxis(e.getX(), e.getY());
		}
		else{
			// span marker rect
			markerMode = true;
		}
		clickedX = e.getX();
		clickedY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		markerMode   = false;
		moveAxisMode = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (moveAxisMode){
			view.moveAxis(activeAxis, e.getX()-clickedX);
			clickedX = e.getX();
		}
		else if (markerMode){
			view.updateMarker(clickedX, clickedY, e.getX(), e.getY());
		}
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}

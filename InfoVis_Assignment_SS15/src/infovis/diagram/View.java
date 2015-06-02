package infovis.diagram;

import infovis.diagram.elements.Element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.awt.geom.Point2D;

import javax.swing.JPanel;



public class View extends JPanel{
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	//private double translateX= 0;
	//private double translateY= 0;
	private double 				translateX, translateY, transOvX, transOvY = 0;
	private Rectangle2D overviewRect = new Rectangle2D.Double(); 
	private Rectangle2D marker = new Rectangle2D.Double(); 
	private double overviewScale = .2;
	
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public void paint(Graphics g) {
		
		Graphics2D g2D = (Graphics2D) g;
		
		// MAIN VIEW
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.translate(translateX, translateY);
		// scale overview with slider
		g2D.scale(scale,scale);
		paintDiagram(g2D);
		
		// OVERVIEW RECTANGLE
		overviewRect = new Rectangle2D.Double(0,0,getWidth(), getHeight()); 
		// downscale overview window when scaling (remains size)
		g2D.scale(1/scale, 1/scale);
		g2D.translate(-translateX, -translateY);
		//g2D.translate(transOvX, transOvY);
		g2D.scale(overviewScale, overviewScale);
		g2D.setClip(null);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.draw(overviewRect);
		
		// MARKER RECTANGLE
		// generate marker such that position adapts to coord when moving and size adapts to scaling of main window
		marker = new Rectangle2D.Double(
				overviewRect.getX() - translateX/scale, 
				overviewRect.getY() - translateY/scale,
				overviewRect.getWidth()  / scale,
				overviewRect.getHeight() / scale);
		g2D.setClip(overviewRect);
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fill(marker);
		g2D.setColor(Color.ORANGE);
		g2D.draw(marker);
		//g2D.setClip(overviewRect);
		paintDiagram(g2D);
			
	}
	private void paintDiagram(Graphics2D g2D){
		for (Element element: model.getElements()){
			element.paint(g2D);
		}
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale(){
		return scale;
	}
	public double getTranslateX() {
		return translateX;
	}
	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}
	public double getTranslateY() {
		return translateY;
	}
	public void setTranslateY(double tansslateY) {
		this.translateY = tansslateY;
	}
	public void updateTranslation(double x, double y){
		setTranslateX(x);
		setTranslateY(y);
	}	
	public void updateMarker(int x, int y){
		marker.setRect(x, y, 16, 10);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}
	public void setMarkerCenter(double x, double y){
		translateX = -(x - transOvX) / overviewScale * scale + getWidth() * .5;
		translateY = -(y - transOvY) / overviewScale * scale + getHeight()* .5;
	}
	public boolean isInMarker(double x, double y){
		if (x > 0 && x < getWidth()*overviewScale && y > 0 && y < getHeight()*overviewScale) {
			return true;
		} else {
			return false;
		}
	}
	public Point2D viewToModel(double x, double y) {
		return new Point2D.Double((x - translateX) / scale, (y - translateY) / scale);
	}
	public Point2D modelToView(double x, double y) {
		return new Point2D.Double(x * scale + translateX, y * scale + translateY);
	}
}
 
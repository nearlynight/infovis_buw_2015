package infovis.paracoords;

import infovis.scatterplot.Model;
import infovis.scatterplot.Range;
import infovis.scatterplot.Data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class View extends JPanel {
	private Model model = null;
	private Rectangle2D marker = new Rectangle2D.Double(0,0,0,0);
	private int axes[];	// horizontal position of axes
	private Map<Integer, Integer> axesMap = new TreeMap<Integer, Integer>(); // maps horizontal position to index of axis
	final static private int leftPadding = 150;
	final static private int upperPadding = 20;
	final static private int defaultDistance = 100;
	final static private int itemDistance = 30;
	final static private int height = 400;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.setColor(Color.orange);
		g2D.fill(marker);
		g2D.setColor(Color.red);
		g2D.draw(marker);
		g2D.setColor(Color.black);
		for (int i=0; i<model.getDim(); i++){
			String l = model.getLabels().get(i);
			g2D.translate(axes[i], (upperPadding + height + 20));
			g2D.rotate(Math.PI*.25);
			g2D.drawString(l, 0, 0);
			g2D.rotate(-Math.PI*.25);
			g2D.translate(-axes[i], -(upperPadding + height + 20));
			g2D.drawLine(axes[i], upperPadding - 5, axes[i], upperPadding + height + 5);
		}
		for (int i=0; i<model.getList().size(); i++){
			String l = model.getList().get(i).getLabel();
			g2D.drawString(l, 10, 10 + upperPadding + i*itemDistance);
		}
		for (int i=0; i<model.getList().size(); i++){
			g2D.setColor(model.getList().get(i).getColor());
			for (Line2D l : getLines(i)){
				g2D.draw(l);
			}
			g2D.setColor(Color.black);
		}
	}
	
	// generate one line between two axes (create line segment)
	private Line2D getLine(int d1, int d2, int i){
		int   x1;
		int   y1;
		if (d1 == -1){
			x1 = 100;
			y1 = upperPadding + i*itemDistance;
		}
		else {
			Range r1 = model.getRanges().get(d1);
			x1 = axes[d1];
			y1 = (int) ((1-((model.getList().get(i).getValue(d1)- r1.getMin())
					/ (r1.getMax() - r1.getMin())))
					* height + upperPadding);
		}
		Range r2 = model.getRanges().get(d2);
		int   x2 = axes[d2];
		int   y2 = (int) ((1-((model.getList().get(i).getValue(d2) - r2.getMin())
				/ (r2.getMax() - r2.getMin())))
				* height + upperPadding);
		return new Line2D.Double(x1, y1, x2, y2);
	}
	
	// ein Aufruf der Funktion kuemmert sich um ein Auto (i)
	// map key = pixel position of axes
	// get line segment for data set entry and combine them to one line
	private List<Line2D> getLines(int i){
		List<Line2D> l = new ArrayList<Line2D>();
		int prev = -1;
		// iterate over map 
		for (Map.Entry<Integer, Integer> e : axesMap.entrySet()){
			
			l.add(getLine(prev, e.getValue(), i));
			prev = e.getValue();
		}
		return l;
	}
	
	// calculate if marker rect intersects line
	private void mark(){
		for (int i=0; i<model.getList().size(); i++){	// iterate through model dataset
			Data item = model.getList().get(i);			// get line number i
			item.setColor(Color.blue);
			for (Line2D l : getLines(i)){	// iterate over all segments (segment is part between two axis) of one line
				if (marker.intersectsLine(l)){
					item.setColor(Color.red);
					break;
				}
			}
		}
	}
	
	public void updateMarker(int x1, int y1, int x2, int y2){
		int xmin = Math.min(x1, x2);
		int xmax = Math.max(x1, x2);
		int ymin = Math.min(y1, y2);
		int ymax = Math.max(y1, y2);
		marker = new Rectangle2D.Double(xmin, ymin, xmax-xmin, ymax-ymin);
		update();
		mark();
	}
	
	// check if courser is in axes range
	public int aboveAxis(int x, int y){
		for (int i=0; i<axes.length; i++){
			
			if (Math.abs(axes[i]-x) < 5
					&& y > upperPadding
					&& y < upperPadding + height){
				// return which axes is selected
				return i;
			}
		}
		return -1;
	}
	
	// move active axis 
	public void moveAxis(int i, int x){
		axes[i] += x;
		marker = new Rectangle2D.Double(0,0,0,0);
		update();
	}

	@Override
	public void update(Graphics g) {
		update();
		paint(g);
	}
	
	// fill axesMap with xValues and number of axes
	public void update(){
		axesMap.clear();	// b/c after moving axes it has new position and index
		for (int i=0; i<model.getDim(); i++){
			axesMap.put(axes[i], i);
		}
	}

	public Model getModel() {
		return model;
	}

	// 
	public void setModel(Model model) {
		this.model = model;
		axes = new int[model.getDim()];
		for (int i=0; i<model.getDim(); i++){
			axes[i] = leftPadding + i*defaultDistance;
		}
		update();
		mark();
	}
	
}

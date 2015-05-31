package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class View extends JPanel {
	     private Model model = null;
	     private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
	     private final int boxSize = 150;
	     private final int circleSize = boxSize/50;
	     private final int smallBoxSize = boxSize-circleSize;
	     private final int outerSpace = 50;     

		 public Rectangle2D getMarkerRectangle() {
			return markerRectangle;
		}
		 
		@Override
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.clearRect(0, 0, getWidth(), getHeight());
			g2D.setColor(Color.LIGHT_GRAY);
			g2D.fill(markerRectangle);
			g2D.setColor(Color.GRAY);
			g2D.draw(markerRectangle);
			g2D.setColor(Color.BLACK);

			for (int i = 0; i<model.getDim(); ++i) {
				for (int j = 0; j<model.getDim(); ++j) {
					g2D.drawRect(boxSize*i+outerSpace, boxSize*j+outerSpace, boxSize, boxSize);
					String l = model.getLabels().get(i);
					g2D.drawString(l, boxSize*i+outerSpace+10, 45);
					g2D.rotate(Math.PI*0.5);
					g2D.drawString(l, boxSize*i+outerSpace+10, -35);
					g2D.rotate(-Math.PI*.5);
					for (int k = 0; k < model.getList().size(); ++k) {
						Point2D myPoint = giveMePoint(i,j,k);			
						g2D.setColor(model.getList().get(k).getColor());
						g2D.drawRect((int)myPoint.getX(), (int)myPoint.getY(), circleSize, circleSize);
					}
					g2D.setColor(Color.BLACK);
				}	
			}		
		}
		
		public void setModel(Model model) {
			this.model = model;
		}
		
		public Point2D giveMePoint (int i, int j, int k) {
			double valueX = model.getList().get(k).getValues()[i];
			double valueY = model.getList().get(k).getValues()[j];
			
			double maxX = model.getRanges().get(i).getMax();
			double maxY = model.getRanges().get(j).getMax();
			double minX = model.getRanges().get(i).getMin();
			double minY = model.getRanges().get(j).getMin();
			
			double posX = 1 - ((maxX-valueX) / (maxX-minX));
			double posY = 1 - ((maxY-valueY) / (maxY-minY));
			int pointX = outerSpace + boxSize*i + (int)(smallBoxSize * posX);
			int pointY = outerSpace + boxSize*j + (int)(smallBoxSize * posY);
			return new Point2D.Double(pointX, pointY);
		}
		
		public void updateMarkerRect(int xPress, int yPress, int xDrag, int yDrag) {
			Tile myTile = new Tile(xPress, yPress); 
			xDrag = Math.max(myTile.getXMin(), xDrag);
			xDrag = Math.min(myTile.getXMax(), xDrag);
			yDrag = Math.max(myTile.getYMin(), yDrag);
			yDrag = Math.min(myTile.getYMax(), yDrag);
			int widthMin = Math.min(xPress, xDrag);
			int heigthMin = Math.min(yPress, yDrag);
			int widthMax = Math.max(xPress, xDrag);
			int heigthMax = Math.max(yPress, yDrag);
			int width = widthMax - widthMin;
			int heigth = heigthMax - heigthMin;
			markerRectangle.setRect(widthMin, heigthMin, width, heigth);
			colorMarkedPoints(myTile.a, myTile.b);

			//int restrictX = restrictRectangleMarkerX(xPress, yPress, width, heigth);
		}
		
		public void colorMarkedPoints(int i, int j) {
			for (int k = 0; k < model.getList().size(); ++k) {
				Point2D point = giveMePoint(i,j,k);
				if (markerRectangle.contains(point)) {
					model.getList().get(k).setColor(Color.RED);
				} else {
					model.getList().get(k).setColor(Color.BLACK);
				}
			}
		}
		
		public class Tile {
			public int a;
			public int b;
			
			public Tile (int xPress, int yPress) {
				this.a = (xPress - outerSpace) / boxSize;
				this.b = (yPress - outerSpace) / boxSize;
			}
			
			public int getXMin() {
				return a*boxSize+outerSpace;
			}
			public int getXMax() {
				return a*boxSize+outerSpace+boxSize;
			}
			public int getYMin() {
				return b*boxSize+outerSpace;
			}
			public int getYMax() {
				return b*boxSize+outerSpace+boxSize;
			}
		}
}




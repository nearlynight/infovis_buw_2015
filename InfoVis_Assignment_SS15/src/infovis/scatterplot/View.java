package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class View extends JPanel {
	     private Model model = null;
	     private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0); 
	     private final int boxSize = 150;
	     private final int smallBoxSize = 147;
	     private final int outerSpace = 50;
	     private final int circleSize = 3;

		 public Rectangle2D getMarkerRectangle() {
			return markerRectangle;
		}
		 
		@Override
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.clearRect(0, 0, getWidth(), getHeight());
			g2D.setColor(Color.BLACK);
			g2D.fill(markerRectangle);
			g2D.draw(markerRectangle);

			for (int i = 0; i<model.getDim(); ++i) {
				for (int j = 0; j<model.getDim(); ++j) {
					g2D.drawRect(boxSize*i+outerSpace, boxSize*j+outerSpace, boxSize, boxSize);
					String l = model.getLabels().get(i);
					g2D.drawString(l, boxSize*i+outerSpace+10, 45);
					g2D.rotate(Math.PI*0.5);
					g2D.drawString(l, boxSize*i+outerSpace+10, -35);
					g2D.rotate(-Math.PI*.5);
					for (int k = 0; k < model.getList().size(); ++k) {
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
						g2D.drawRect(pointX, pointY, circleSize, circleSize);
					}
				}	
			}


			
			
			/*
	        for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
			}
			
			for (Range range : model.getRanges()) {
				Debug.print(range.toString());
				Debug.print(",  ");
				Debug.println("");
			}
			/*
			for (Data d : model.getList()) {
				Debug.print(d.toString());
				Debug.println("");
			}
	        */
			
		}
		public void setModel(Model model) {
			this.model = model;
		}
}

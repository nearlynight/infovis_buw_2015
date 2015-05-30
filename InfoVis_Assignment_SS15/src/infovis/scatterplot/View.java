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
	     private int boxSize = 150;
	     private int outerSpace = 50;

		 public Rectangle2D getMarkerRectangle() {
			return markerRectangle;
		}
		 
		@Override
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2D.clearRect(0, 0, getWidth(), getHeight());
			g2D.setColor(Color.BLACK);
			g2D.fill(markerRectangle);
			g2D.draw(markerRectangle);
			
			//g2D.drawString("test", 100, 100);
			//g2D.drawRect(100, 110, 100, 100);

			for (int i = 0; i<model.getDim(); ++i) {
				for (int j = 0; j<model.getDim(); ++j) {
					g2D.drawRect(boxSize*i+outerSpace, boxSize*j+outerSpace, boxSize, boxSize);
					String l = model.getLabels().get(i);
					g2D.drawString(l, boxSize*i+outerSpace+10, 45);
					g2D.rotate(Math.PI*0.5);
					g2D.drawString(l, boxSize*i+outerSpace+10, -35);
					g2D.rotate(-Math.PI*.5);
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

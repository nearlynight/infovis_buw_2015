package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;

public class Fisheye implements Layout {
	
	private View view;
	
	static private final double d = 3;	// distortion factor
	static private final double s = 1;	// vertex-size scale factor in fisheye view
	
	private int fx = 0;
	private int fy = 0;
	
	public Fisheye(View view){
		this.view = view;
	}

	public void setMouseCoords(int x, int y) {
		fx = x;
		fy = y;
	}

	public Model transform(Model model) {
		Model newModel = new Model();
		
		
		for (Vertex v : model.getVertices()){
			// select visible vertices 
			if (view.getBounds().contains(v.getX(), v.getY()))
				newModel.addVertex(transformVertex(v));
		}
		
		for (Edge e : model.getEdges()){
			Vertex v1 = transformVertex(e.getSource());
			Vertex v2 = transformVertex(e.getTarget());
			newModel.addEdge(new Edge(v1, v2));
		}
		
		return newModel;
	}
	
	// resize vertex according to distance focus (fx, fy)
	private Vertex transformVertex(Vertex v) {
		double sg = sGeom(v);
		double width = v.getWidth() * sg;
		double height = v.getHeight() * sg;
		double x = f1(v.getCenterX(), fx, view.getWidth() ) - width / 2;
		double y = f1(v.getCenterY(), fy, view.getHeight()) - height / 2;
		
		System.out.println(sg);
		
		return new Vertex(x, y, width, height);
	}
	
	// compute position w/ Cartesian Transformation
	private double g(double x){
		return (d + 1) * x / (d * x + 1);
		//return (d + 1) / Math.pow(d * x + 1, 2);
	}
	
	
	private double f1(double v, double f, double bound) {
		double dmax  = v > f ? bound - f : -f;
		double dnorm = v - f;
		
		return f + g(dnorm / dmax) * dmax;
	}
	
	// COMPUTE SIZE
	// geometric size, undistorted
	private double getSNorm(Vertex v) {
		return v.getWidth();
	}
	
	// avoid overlapping of vertices in fisheye view
	// "Because magnification decreases as we move away 
	//  from the focus, taking a point farther away from the
	//  focus rather than closer to the focus is conservative. 
	//  It ensures that vertices that do not overlap in the 
	//  normal view do not overlap in the fisheye view either."
	private Point2D getQNorm(Vertex v) {
		double qs = s * getSNorm(v) / 2;												// distance of the center of the vertex away from the focus
		double qx = fx < v.getCenterX() ? v.getCenterX() + qs : v.getCenterX() - qs;	// Xcoord of fisheye vertex
		double qy = fy < v.getCenterY() ? v.getCenterY() + qs : v.getCenterY() - qs;	// Ycoord of fisheye vertex
		
		return new Point2D.Double(qx, qy);
	}
	
	// geometric size
	private double sGeom(Vertex v) {
		Point2D qNorm = getQNorm(v);
		double px = f1(v.getCenterX(), fx, view.getWidth());
		double py = f1(v.getCenterY(), fy, view.getHeight());
		double qx = f1(qNorm.getX(),   fx, view.getWidth());
		double qy = f1(qNorm.getY(),   fy, view.getHeight());
		
		return 2 * Math.min(Math.abs(qx - px), Math.abs(qy - py)) / getSNorm(v);
	}

	@Override
	public void setMouseCoords(int x, int y, View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Model transform(Model model, View view) {
		// TODO Auto-generated method stub
		return null;
	}
}
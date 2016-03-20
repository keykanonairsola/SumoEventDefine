package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Junction {
	String id;
	double x, y;
	ArrayList<String> incLanes;
	ArrayList<String> shape;
	
	//the edges that connect to this junction
	HashMap<String, Edge> edges;
	
	public Junction(String id,String x, String y, 
			String intLanes1, String intLanes2, String shape){
		this.id = new String(id);
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
		
		incLanes = new ArrayList<String>();
		this.shape = new ArrayList<String>();
		
		this.edges = new HashMap<String, Edge>();
		
		//handle intLanes1
		String templanes1[] = intLanes1.split(" ");
		for(int i = 0; i < templanes1.length; i ++){
			incLanes.add(templanes1[i]);
		}
		
		//handle intLanes2
		String templanes2[] = intLanes2.split(" ");
		for(int i = 0; i < templanes2.length; i ++){
			incLanes.add(templanes2[i]);
		}
		
		//handle shape
		String tempshape[] = shape.split(" ");
		for(int i = 0; i < tempshape.length; i ++){
			this.shape.add(tempshape[i]);
		}
	}

	//get
	public String getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public ArrayList<String> getIncLanes() {
		return incLanes;
	}

	public ArrayList<String> getShape() {
		return shape;
	}
	
	//add an edge 
	public void addEdge(Edge e){
		edges.put(e.getId(), e);
	}
	
	//get edges
	public HashMap<String, Edge> getEdges(){
		return edges;
	}
}

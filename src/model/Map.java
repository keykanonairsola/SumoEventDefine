package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Map {
	HashMap<String,Edge> edges;
	HashMap<String,Junction> junctions;
	int scaling = 1;
	
	public Map(){
		edges = new HashMap<String,Edge>();
		junctions = new HashMap<String,Junction>();
	}
	
	public void computePosition(Junction junction){		
		//flag
		boolean findFlag = false;
		
		//get junction lanes
		ArrayList<String> junctionLane;
		if( (junctionLane = junction.getIncLanes()) != null){
			
			//loop the junction lines
			for(int i = 0; i < junctionLane.size(); i ++){
				//get edge iterator
				Iterator<Entry<String, Edge>> edgeIt = edges.entrySet().iterator();
				
				//loop to travel the all edges
				while(edgeIt.hasNext() && !findFlag){
					//get a edge
					Edge edge = edgeIt.next().getValue();
					
					//get from x y
					if(!edge.getFrom().equals("") && edge.getFrom().equals(junction.getId())){
						edge.setFromX(junction.getX());
						edge.setFromY(junction.getY());
					}
					
					//get to x y
					if(!edge.getTo().equals("") && edge.getTo().equals(junction.getId())){
						edge.setToX(junction.getX());
						edge.setToY(junction.getY());
					}
					
					
					//get lanes
					HashMap<String,Lane> lanes = edge.getLanes();
					Iterator<Entry<String, Lane>> laneIt = lanes.entrySet().iterator();
					
					//loop to travel the all lanes
					while(laneIt.hasNext()){
						//get a lane
						Lane lane = laneIt.next().getValue();
						
						//find the position
						if(lane.getId().equals(junctionLane.get(i))){
							//lane.x = junction.x;
							//lane.y = junction.y;
							
							//edge.setFromX(junction.x);
							//edge.setFromY(junction.y);
							
							junction.addEdge(edge);
							//edge.setToX(junction.x);
							//edge.setToY(junction.y);
							
							edge.addLanes(lane.getId(), lane);
							findFlag = true;
							break;
						}
						
					}
					
					edges.replace(edge.id, edge);
				}
				
				findFlag = false;
				
			}
			
		}
	}
	
	//add a edge
	public void addEdge(String id, Edge edge){
		if(edges.containsKey(id)){
			edges.replace(id, edge);
		}
		else{
			edges.put(id, edge);
		}
	}
	
	//add a junction
	public void addjunction(String id, Junction junction){
		junctions.put(id, junction);
		
		//use a junction to get the lane position
		computePosition(junction);
		
		int n = (int) (junction.getX()/512);
		if(n > scaling){
			scaling = n;
		}
		n =  (int) (junction.getY()/512);
		if(n > scaling){
			scaling = n;
		}
	}
	
	//get a edge
	public Edge getEdge(String id){
		return edges.get(id);
	}
	
	//get a junction
	public Junction getJunction(String id){
		return junctions.get(id);
	
	}
	
	//return edges
	public HashMap<String, Edge> getEdges(){
		return edges;
	}
	
	//return junctions
	public HashMap<String, Junction> getJunction(){
		return junctions;
	}
	
	//get scaling
	public int getScaling(){
		return this.scaling;
	}
	
}

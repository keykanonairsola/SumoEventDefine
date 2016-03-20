package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Edge {
	String id;
	HashMap<String, Lane>lanes;
	String from, to;
	double fromX, fromY;
	double toX, toY;
	ArrayList<String> tolist;

	public Edge(String id){
		this.id = new String(id);
		lanes = new HashMap<String, Lane>();
		from = new String("");
		to = new String("");
		fromX = Double.MIN_VALUE;
		fromY = Double.MIN_VALUE;
		toX = Double.MIN_VALUE;
		toY = Double.MIN_VALUE;
		tolist = new ArrayList<String>();
	}
	
	//add a lane
	public void addLanes(String id, Lane lane){
		if(lanes.containsKey(id)){
			lanes.replace(id, lane);
		}
		else{
			lanes.put(id, lane);
		}
	}
	
	public String getId() {
		return id;
	}

	//get a lane
	public Lane getLane(String id){
		return lanes.get(id);
	}
	
	//get lanes to search
	public HashMap<String, Lane> getLanes(){
		return lanes;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	public double getFromX() {
		return fromX;
	}

	public void setFromX(double fromX) {
		this.fromX = fromX;
	}

	public double getFromY() {
		return fromY;
	}

	public void setFromY(double fromY) {
		this.fromY = fromY;
	}

	public double getToX() {
		return toX;
	}

	public void setToX(double toX) {
		this.toX = toX;
	}

	public double getToY() {
		return toY;
	}

	public void setToY(double toY) {
		this.toY = toY;
	}
	
	public void addToid(String toid){
		tolist.add(toid);
	}
	
	public boolean isConnected(String toid){
		return tolist.contains(toid);
	}
}

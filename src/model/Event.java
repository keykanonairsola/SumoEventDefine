package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Event {
	//the lane event happen
	ArrayList< Edge> edges;
	
	boolean bike_Unimpeded;
	boolean bike_Congestion;
	boolean delivery_Congestion;
	//the vehicle type
	//{"Car", "Motobike" , "Bike" , "Person"}
	//0 = car, 1 = motobike, 2 = bike , 3 = person
	int vehicleType;
	
	//the event type
	//{"Accident", "Congestion", "Unimpeded"}
	//  0 = Unimpeded,1 = Congestion,2 = Accident,
	int eventType;
	
	Accident acc;
	
	//the vehicle number
	int vehicleNum;
	
	int timeStart;
	
	public Event(ArrayList< Edge> edges,int vType, int eType,
			int vehicleNum, int timeStart, boolean bike_Unimpeded, boolean bike_Congestion, boolean delivery_Congestion, 
			Accident acc){
		this.edges = edges;
		this.vehicleType = vType;
		this.eventType = eType;
		this.vehicleNum = vehicleNum;
		this.timeStart = timeStart;
		
		this.bike_Unimpeded = bike_Unimpeded;
		this.bike_Congestion = bike_Congestion;
		this.delivery_Congestion = delivery_Congestion;
		if(acc.getAccidentLane() == null){
			this.acc.setAccidentLane(null);
		}else{
			this.acc = new Accident(acc);
		}
	}
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public boolean isBikeUnimpeded() {
		return this.bike_Unimpeded;
	}

	public boolean isBikeCongestion() {
		return this.bike_Congestion;
	}

	public int getVehicleType() {
		return vehicleType;
	}

	public int getEventType() {
		return eventType;
	}

	public int getTimeStart() {
		return timeStart;
	}

	public String getWriteString(int vNum){
		//initial
		String eventString = new String();
		String vtype = new String();
		String etype = new String();
		String typeStr = new String();
		if(vehicleType == 0){
			if((eventType == 1|| eventType == 2)  && !delivery_Congestion){
				eventString += "<vType id=\"delivery_Congestion\" vClass=\"delivery\" maxSpeed=\"5\"/>\r\n";
			}
			if(eventType == 1 || eventType == 2){
				typeStr = "deliverry_Congestion";
			}
			vtype = "vehicle";
			etype = "route";
		}
		else if(vehicleType == 1){
			if(eventType == 0 && !this.bike_Unimpeded){
				eventString += "<vType id=\"bike_Unimpeded\" vClass=\"bicycle\"/>\r\n";
			}
			else if((eventType == 1 || eventType == 2)&& !this.bike_Congestion){
				eventString += "<vType id=\"bike_Congestion\" vClass=\"bicycle\" maxSpeed=\"1\"/>\r\n";
			}
			if(eventType == 0){
				typeStr = "bike_Unimpeded";
			}
			else if(eventType == 1 || eventType == 2){
				typeStr = "bike_Congestion";
			}
			vtype = "vehicle";
			etype = "route";
		}
		else{
			vtype = "person";
			etype = "walk";
		}
		for(int i = 0; i < this.vehicleNum; i ++){
			eventString += "<" + vtype + " id=\"" + (vNum+i);
			if(vehicleType == 1){
				eventString += "\" type=\"" + typeStr;
			}
			if(eventType == 0){
				eventString += "\" depart=\"" + (timeStart+i*3);
			}
			else {
				eventString += "\" depart=\"" + (timeStart);
			}
			eventString += ".00\">\r\n";
			eventString += "\t<" + etype + " edges=\"";
			for(int j = 0; j < edges.size()-1; j ++){
				eventString += edges.get(j).getId() + " ";
			}
			eventString += edges.get(edges.size()-1).getId() + "\"/>\r\n";
			if(eventType == 2){
				eventString += "\t<stop lane=\"" + acc.getAccidentLane() + "\" endPos=\""
						+ acc.getPos() + "\" until=\"" + acc.getUntil() + "\"/>\r\n";
			}
			eventString += "</" + vtype + ">\r\n";
			
		}
		
		return eventString;
	}
	
	public int getVehicleNum(){
		return this.vehicleNum;
	}
}

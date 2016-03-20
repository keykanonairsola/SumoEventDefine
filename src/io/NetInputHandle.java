package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import model.Edge;
import model.Junction;
import model.Lane;
import model.Map;

public class NetInputHandle {
	Map map;
	File net;
	BufferedReader netstream;
	
	public NetInputHandle(String filename){
		//initial trafficLogs
		map = new Map();
		
		//read the file net and handle
		if(filename == null){
			net = new File("*.net.xml");
		}
		else{
			net = new File(filename);
		}
		//a line of the file
		String line = new String();
		
		/* the data need to store
		 * 
		 */
		
		//edge
		String edgeID = new String();
		String from = new String();
		String to = new String();
		Edge edge = null;
		
		//lane
		String laneID = new String();
		double laneLength = 0;
		String laneShape = new String();
	
		//junction
		String junctionID = new String();
		double junctionX = 0;
		double junctionY = 0;
		String junctionLane1 = new String();
		String junctionLane2 = new String();
		String junctionShape = new String();
		
		try {
			//create sumo.tr stream 
			netstream = new BufferedReader(new InputStreamReader(new FileInputStream(net)));
			
			//get line of the sumo.tr file
			while((line = netstream.readLine()) != null){
				//edge line
				if(line.contains("edge id") ){
					//get the edge
					String subline[] = line.split("\"");
					
					edgeID = subline[1];
					
					edge = new Edge(edgeID);
					//get from to
					if(subline.length > 5){
						from = subline[3];
						to = subline[5];
						edge.setFrom(from);
						edge.setTo(to);
					}
	
				}
				
				else if(line.contains("</edge>")){
					if(edge != null){
						map.addEdge(edgeID, edge);
					}
				}
				//lane id line
				else if(line.contains("lane id")){
					//get laneID
					//System.out.println(line);
					String laneMessage[] = line.split("\"");
					laneID = new String(laneMessage[1]);
					String allow = new String();
					String disallow = new String();
					if(laneMessage.length > 13){
						
						laneLength = Double.parseDouble(laneMessage[11]);
						laneShape = new String(laneMessage[13]);
					}
					else if(laneMessage.length > 11){
						laneLength = Double.parseDouble(laneMessage[9]);
						laneShape = new String(laneMessage[11]);
					}
					else{
						laneLength = Double.parseDouble(laneMessage[7]);
						laneShape = new String(laneMessage[9]);
					}
					
					
					if(laneMessage[4].contains("disallow")){
						disallow = laneMessage[5];
						edge.addLanes(laneID, new Lane(laneID, null, disallow, laneLength, laneShape));
					}
					else if(laneMessage[4].contains("allow")){
						allow = laneMessage[5];
						edge.addLanes(laneID, new Lane(laneID, allow, null, laneLength, laneShape));
					}
					else if(laneMessage[2].contains("disallow")){
						disallow = laneMessage[3];
						edge.addLanes(laneID, new Lane(laneID, null, disallow, laneLength, laneShape));
					}
					else if(laneMessage[2].contains("allow")){
						allow = laneMessage[3];
						edge.addLanes(laneID, new Lane(laneID, allow, null, laneLength, laneShape));
					}
					else{
						System.out.println(line);
					}
					
				}
				
				//junction line
				else if(line.contains("junction id")){
					String subline[] = line.split("\"");
					junctionID = new String(subline[1]);
					
					// x and y
					junctionX = Double.parseDouble(subline[5]);
					junctionY = Double.parseDouble(subline[7]);

					//junction inclane
					junctionLane1 = new String(subline[9]);
					junctionLane2 = new String(subline[11]);
					
					
					
					//System.out.println(junctionID);
					//junction shape
					if(subline.length > 13){
						junctionShape = new String(subline[13]);
					}
					
					//create junction
					Junction junction = new Junction(junctionID, subline[5], subline[7], 
							junctionLane1, junctionLane2, junctionShape);
					
					//add junction
					map.addjunction(junctionID, junction);
				}
				
				else if(line.contains("connection")){
					String subline[] = line.split("\"");
					
					String fromid = subline[1];
					String toid = subline[3];
					
					map.getEdge(fromid).addToid(toid);
				}
				
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public Map getMap() {
		return map;
	}
	
}

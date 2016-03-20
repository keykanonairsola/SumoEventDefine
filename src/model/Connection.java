package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Connection {
	HashMap<String, ArrayList<String>> connArr;
	
	public Connection(){
		connArr = new HashMap<String, ArrayList<String>>();
	}
	
	// add a connection
	public void addConnection(String fromid, String toid){
		ArrayList<String> tolist = connArr.get(fromid);
		if(tolist == null){
			tolist = new ArrayList<String>();
		}
		
		//add to id
		tolist.add(toid);
		
		connArr.put(fromid, tolist);
	}
	
	public ArrayList<String> getToid(String fromid){
		return connArr.get(fromid);
	}
	
	//is the connection exists
	public boolean isConnected(String from, String to){
		ArrayList<String> tolist = connArr.get(from);
		//there is no from id 
		if(tolist == null){
			return false;
		}
		
		return tolist.contains(to);
	}

}

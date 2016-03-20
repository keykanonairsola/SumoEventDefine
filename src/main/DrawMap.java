package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Accident;
import model.Edge;
import model.Lane;
import model.Map;

public class DrawMap extends JPanel{
	//Map
	Map map;
	private ArrayList<Edge> selectedEdge;
	Accident acc = new Accident();
	
	//map center
	int movex = 0;
	int movey = 0;
	int fromx = 0;
	int fromy = 0;
	int tox = 0;
	int toy = 0;
	
	int scaling;
	int vtype;
	int etype;
	public DrawMap(Map map){
		this.map = map;
		this.scaling = map.getScaling();
		
		selectedEdge = new ArrayList<Edge>();
		
		super.setPreferredSize(new Dimension(512, 512));
		
		this.addMouseWheelListener(new MouseWheelListener() {  
            public void mouseWheelMoved(MouseWheelEvent e) {  
            	movex *= scaling;
            	movey *= scaling;
            	scaling = (e.getWheelRotation()< 0)?scaling +1 : scaling -1;  
               
			    if(scaling == 0){
				    scaling = 1;
			    }
			    movex /= scaling;
            	movey /= scaling;
			  
			    repaint();
            }  
        });  
		
		this.addMouseListener(new MouseAdapter(){
			
			public void mousePressed(MouseEvent e){
				fromx = e.getX();
				fromy = e.getY();
			}
			
			public void mouseReleased(MouseEvent e) {
				tox = e.getX();
				toy = e.getY();
				
				movex += tox - fromx;
				movey += toy - fromy;
				
				repaint();
				
				
			}
			
			public void mouseClicked(MouseEvent e){
				int x = e.getX() - movex;
				int y = 512 - (e.getY()) + movey;
				
				HashMap<String, Edge> edges = map.getEdges();
				
				//get edge iterator
				Iterator<Entry<String, Edge>> edgeIt = edges.entrySet().iterator();
				
				//loop to travel the all edges
				while(edgeIt.hasNext() ){
					//get a edge
					Edge edge = edgeIt.next().getValue();
					
					//get the lanes of the edge
					HashMap<String, Lane> lanes = edge.getLanes();
					
					Iterator<Entry<String, Lane>> laneIt = lanes.entrySet().iterator();
					
					while(laneIt.hasNext()){
						//get a lane
						Lane lane = laneIt.next().getValue();
						
						int pos = 0;
						
						String[] shape = lane.getShape().split(" ");
						
						int lane_x = (int)lane.getX() / scaling;
						int lane_y = (int)lane.getY() / scaling;
						
						//draw every part of a lane
						for(int i = 0; i < shape.length; i ++){
							String[] vec = shape[i].split(",");
							int vector_x = (int)(Double.parseDouble(vec[0]) / scaling);
							int vector_y = (int)(Double.parseDouble(vec[1]) / scaling);
							
							pos += Math.sqrt((Math.pow(vector_x - lane_x, 2) + Math.pow(vector_y - lane_y, 2)))* scaling;
							
							//the general expression A,B,C of a line
							double A = vector_y - lane_y;
							double B = lane_x - vector_x;
							double C = vector_x * lane_y - lane_x * vector_y;
							
							if(A != 0 || B != 0 && C != 0){
								//the distance between a point and a line
								double dis = Math.abs(A*x + B*y + C)/ Math.sqrt(A*A + B*B);
								boolean inLine = (x > lane_x && x < vector_x && y > lane_y && y < vector_y) ||
										(x < lane_x && x > vector_x && y > lane_y && y < vector_y) ||
										(x > lane_x && x < vector_x && y < lane_y && y > vector_y) ||
										(x < lane_x && x > vector_x && y < lane_y && y > vector_y);
								if(dis < 2*scaling && inLine){
									//not allow the vehicle
									
									String allow = lane.getAllow();
									String disallow = lane.getDisallow();
									String vtypeStr = new String();
									//set vehicle type string
									switch(vtype){
									case 0: vtypeStr = "delivery"; break;
									case 1:
									case 2: vtypeStr = "bicycle"; break;
									case 3: vtypeStr = "pedestrian"; break;
									default: break;
									}
									
									//judge allow or disallow
									if(allow != null){
										if(! allow.contains(vtypeStr)){
											JOptionPane.showMessageDialog(null, "该路段车辆类型限制",
													"该路段不允许该类型车辆通过", JOptionPane.ERROR_MESSAGE);
											break;
										}
									}
									if(disallow != null){
										if(disallow.contains(vtypeStr)){
											JOptionPane.showMessageDialog(null, "该路段车辆类型限制",
													"该路段不允许该类型车辆通过", JOptionPane.ERROR_MESSAGE);
											break;
										}
									}
									
									
									System.out.println("edge id = " + edge.getId() + " , lane id = " + lane.getId());
									
									//select 
									if(selectedEdge.contains(edge)){
										selectedEdge.remove(edge);								
									}
									else{
										if(! selectedEdge.isEmpty()){
											Edge lastE = selectedEdge.get(selectedEdge.size()-1);
											if(! lastE.isConnected(edge.getId())){
												continue;
											}
										}
										
										selectedEdge.add(edge);
										if(etype == 2){
											acc.setAccidentLane(lane.getId());
											acc.setPos(pos);
											acc.setUntil(900);
											//System.out.println("edge id = " + edge.getId() + " , lane id = " + lane.getId());
										}
										
									}
									repaint();
									return;
								}
								lane_x = vector_x;
								lane_y = vector_y;
							}
						}
						
					}
				}
			}
		});
		
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		int gtype = 0;
		//get edges
		HashMap<String, Edge> edges = map.getEdges();
		
		//get edge iterator
		Iterator<Entry<String, Edge>> edgeIt = edges.entrySet().iterator();
		
		//loop to travel the all edges
		while(edgeIt.hasNext() ){
			//get a edge
			Edge edge = edgeIt.next().getValue();
			
			if(selectedEdge.contains(edge)){
				gtype = 1;
				g2d.setColor(new Color(255, 0 , 0));
				g2d.setStroke(new BasicStroke(3f));
				
			}else{
				g2d.setColor(new Color(0,0,0));
				g2d.setStroke(new BasicStroke(1f));
			}
			//get the lanes of the edge
			HashMap<String, Lane> lanes = edge.getLanes();
			
			Iterator<Entry<String, Lane>> laneIt = lanes.entrySet().iterator();
			
			while(laneIt.hasNext()){
				//get a lane
				Lane lane = laneIt.next().getValue();

				String[] shape = lane.getShape().split(" ");
				
				int x = (int)lane.getX() / scaling;
				int y = (int)lane.getY() / scaling;
				
				//draw every part of a lane
				for(int i = 0; i < shape.length; i ++){
					String[] vec = shape[i].split(",");
					int vector_x = (int)(Double.parseDouble(vec[0]) / scaling);
					int vector_y = (int)(Double.parseDouble(vec[1]) / scaling);
					
					//System.out.println(x + "," +  y + "->" + vector_x + "," + vector_y);
					g2d.drawLine(this.scalingPositionTransferX(x), this.scalingPositionTransferY(512-y),
							this.scalingPositionTransferX(vector_x), this.scalingPositionTransferY(512-vector_y));
					x = vector_x;
					y = vector_y;
				}
				
			}
		}
	}
	
	//when mouse click a lane. we need know the which lane is selected
	public ArrayList<Edge> getSelectedLane(){
		return selectedEdge;
	}
	
	public void setVType(int vtype){
		this.vtype = vtype;
	}
	public void setEType(int etype){
		this.etype = etype;
	}

	public Accident getAccident() {
		return acc;
	}
	public void cleanSelectedEdge(){
		this.selectedEdge.clear();
		repaint();
	}
	
	private int scalingPositionTransferX(int x){
		return (x)  +  movex;
	}
	
	private int scalingPositionTransferY(int y){
		return (y)  +  movey;
	}
}

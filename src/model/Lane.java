package model;

public class Lane {
	//
	String id;
	double length;
	String shape;
	String allow;
	String disallow;
	//position
	double x, y;
	
	public Lane(String id, String allow, String disallow, double length, String shape){
		this.id = new String(id);
		if(allow != null){
			this.allow = new String(allow);
		}else{
			this.allow = null;
		}
		if(disallow != null){
			this.disallow = new String(disallow);
		}
		else{
			this.disallow = null;
		}
		this.length = length;
		this.shape = new String(shape);
		
		//calculate x and y;
		x = Double.parseDouble(shape.split(" ")[0].split(",")[0]);
		y = Double.parseDouble(shape.split(" ")[0].split(",")[1]);
	}

	public String getAllow() {
		return allow;
	}

	public String getDisallow() {
		return disallow;
	}

	//set and  get
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public double getLength() {
		return length;
	}

	public String getShape() {
		return shape;
	}
	
	
	
}

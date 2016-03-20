package model;

public class Accident {
	String accidentLane ;
	int pos = 50;
	int until = 900;
	
	public Accident(){
		accidentLane = new String();
		pos = 50;
		until = 900;
	}
	
	public Accident(Accident acc) {
		// TODO 自动生成的构造函数存根
		this.accidentLane = new String(acc.getAccidentLane());
		this.pos = acc.getPos();
		this.until = acc.getUntil();
	}
	
	public Accident(String laneid, int pos, int until){
		this.accidentLane = new String(laneid);
		this.pos = pos;
		this.until = until;
	}
	
	public String getAccidentLane() {
		return accidentLane;
	}
	public void setAccidentLane(String accidentLane) {
		this.accidentLane = accidentLane;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getUntil() {
		return until;
	}
	public void setUntil(int until) {
		this.until = until;
	}
	
	
}

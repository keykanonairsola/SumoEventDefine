package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Accident;
import model.Edge;
import model.Event;
import model.Lane;
import io.NetInputHandle;
import io.NetInputHandle;

public class EventDefine extends JFrame{
	NetInputHandle net;
	ArrayList<Event> eventBuf;
	boolean bike_Unimpeded;
	boolean bike_Congestion;
	boolean delivery_Congestion;
	String netFileName;
	//NetInputHandle sumotr;
	public EventDefine(String netFileName){
		//get the input file and handle
		this.netFileName = netFileName;
		net = new NetInputHandle(netFileName);
		
		//initial eventBuf
		eventBuf = new ArrayList<Event>();
		
		bike_Unimpeded = false;
		bike_Congestion = false;
		delivery_Congestion = false;
		//Grid layout
		setLayout(new BorderLayout());
		
		init_north();
		init_west();
		init_center();
		add(north_part, BorderLayout.NORTH);
		add(west_part, BorderLayout.WEST);
		add(center_part, BorderLayout.CENTER);
		//set title
		this.setTitle("EventDefine");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1024,768);
		this.setVisible(true);
	}
	
	
	
	/*----------------------------GUI part----------------------------------*/
	/*----------------------North part-------------------------
	 * JLabel to hint , JTextField to input , JButton to send request
	 * Name and password to login, register to jump to Register windows
	*/
	private JPanel north_part = new JPanel();
	//JPanel part
	//private JPanel nSetPanel = new JPanel();
	//private JPanel nVehiclePanel = new JPanel();
	//private JPanel nTimePanel = new JPanel();
	//private JPanel nButtonPanel = new JPanel();
	
	//Text Panel part
	private JLabel nVNumHint = new JLabel("Vehicle number");
	private JLabel nTimeStartHint = new JLabel("Time start");


	//Text field to input word
	private JTextField nVehicleNumber = new JTextField(5); 
	private JTextField nTimeStart = new JTextField(5);
	
	//vehicle type box selection
	private JComboBox nVTypeBox;
	
	//event type box selection
	private JComboBox nETypeBox;

	//button to send request
	private JButton nAddEvent = new JButton("Add Event");
	private JButton nCreateFile = new JButton("Create File");
	
	
	
	
	// inital north part
	private void init_north(){
		north_part.setLayout(new FlowLayout(FlowLayout.LEFT, 20 , 10));
		north_part.setPreferredSize(new Dimension(800, 100));
		
		
		//initial vehicle type box
		String[] vtype = {"Car",  "Bike" , "Person"};
		nVTypeBox = new JComboBox(vtype);
		nVTypeBox.setBorder(BorderFactory.createTitledBorder("Vehicle Type"));
		nVTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO 自动生成的方法存根
				int index = nVTypeBox.getSelectedIndex();
				mapPanel.setVType(index);
				mapPanel.cleanSelectedEdge();
			}
		});
		
		//initial event type box
		String[] etype = {  "Unimpeded","Congestion","Accident"};
		nETypeBox = new JComboBox(etype);
		nETypeBox.setBorder(BorderFactory.createTitledBorder("Event Type"));
		nETypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO 自动生成的方法存根
				int index = nETypeBox.getSelectedIndex();
				mapPanel.setEType(index);
				mapPanel.cleanSelectedEdge();
			}
		});
		
		north_part.add(nVTypeBox);
		north_part.add(nETypeBox);
		
		//add three condition
		//north_part.add(nSetPanel, BorderLayout.CENTER);
		//north_part.add(nButtonPanel, BorderLayout.EAST);
		
		//north_part.add(nVehiclePanel);
		//north_part.add(nTimePanel);
		
		
		//nVNumPanel initial
		//nVehiclePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20 , 10));
		north_part.add(nVNumHint);
		north_part.add(nVehicleNumber);
		
		//nTimePanel
		//nTimePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20 , 10));
		north_part.add(nTimeStartHint);
		north_part.add(nTimeStart);
		
		//nButtonPlane
		//nButtonPanel.setLayout(new GridLayout(2,1));
		north_part.add(nAddEvent);
		north_part.add(nCreateFile);
		
				
		nAddEvent.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				ArrayList<Edge> edges = new ArrayList<Edge>(mapPanel.getSelectedLane());
				Accident acc = new Accident(mapPanel.getAccident());
				
				Event event = new Event(edges, nVTypeBox.getSelectedIndex(),
						nETypeBox.getSelectedIndex(), Integer.parseInt(nVehicleNumber.getText().trim()),
						Integer.parseInt(nTimeStart.getText()),bike_Unimpeded, bike_Congestion, delivery_Congestion, acc);
				eventBuf.add(event);
				
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				for(int index = 0; index < eventBuf.size(); index ++){
						listModel.add(index, index + "," + eventBuf.get(index).getVehicleNum());
						int vtype = eventBuf.get(index).getVehicleType();
						int etype = eventBuf.get(index).getEventType();
						if(vtype == 1){
							if(etype == 0){
								bike_Unimpeded = true;
							}
							else if(etype == 1 || etype == 2){
								bike_Congestion = true;
							}
						}
						else if(vtype == 0 && (etype == 1|| etype == 2)){
							delivery_Congestion = true;
						}
				}
				mList.setModel(listModel);
			}
		});
		
		nCreateFile.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				writeEvents();
			}
		});
		
	}
	
	public void writeEvents(){
		//get address 
		int pos = netFileName.indexOf(".net.xml");
		String address = netFileName.substring(0, pos);
		
		try {
			//configuration write part
			FileWriter out = new FileWriter(address + ".sumo.cfg");
			out.write("<configuration>\r\n");
			out.write("<input>\r\n");
			out.write("\t<net-file value=\"" + netFileName + "\"/>\r\n");
			out.write("\t<route-files value=\"" + address + ".rou.xml" + "\"/>\r\n");
			out.write("\t<additional-files value=\"\"/>\r\n");
			out.write("\t<junction-files alue=\"\"/>\r\n");
			out.write("</input>\r\n");
			out.write("\r\n");
			out.write("<output>\r\n");
			out.write("\t<netstate-dump value=\"" + address + ".sumo.tr\"/>\r\n");
			out.write("\t<tripinfo-output valu=\"output-tripinfos.xml\"/>\r\n");
			out.write("\t<vehroute-output value=\"output-vehroutes.xml\"/>\r\n");
			out.write("</output>\r\n");
			out.write("\r\n");
			out.write("<time>\r\n");
			out.write("\t<begin value=\"0\"/>\r\n");
			out.write("\t<end value=\"1000\"/>\r\n");
			out.write("\t<time-to-teleport value=\"-1\"/>\r\n");
			out.write("\t<seed value=\"23423\"/>\r\n");
			out.write("\t<route-steps value=\"-1\"/>\r\n");
			out.write("</time>\r\n");
			out.write("\r\n");
			out.write("<reports>\r\n");
			out.write("\t<print-options value=\"false\"/>\r\n");
			out.write("</reports>\r\n");
			out.write("\r\n");
			out.write("</configuration>\r\n");
			
			out.close();
			
			//write route file 
			int vNum = 0;
			FileWriter route = new FileWriter(address + ".rou.xml");
			route.write("<routes xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n");
			route.write("xsi:noNamespaceSchemaLocation=\"http://sumo.dlr.de/xsd/routes_file.xsd\">\r\n");
			for(int i = 0; i < eventBuf.size(); i ++){
				Event event = eventBuf.get(i);
				route.write(event.getWriteString(vNum));
				vNum += event.getVehicleNum();
			}
			
			route.write("</routes>");
			route.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/*-------------west part---------------------------------------
	 *list to show query answer
	 */
	//data part
	private String[] queryList = {"null"};
	
	private JPanel  west_part = new JPanel();
	//Scroll pane where event list in
	private JScrollPane mScrPane = new JScrollPane();
	
	private int selectIndex = 0;
	private JButton delEvent = new JButton("delete");
	//list to show event
	private JList<String> mList = new JList<String>(queryList);
	
	//init west part
	private void init_west(){
		//set Title
		west_part.setBorder(new TitledBorder("Event"));
		//set layout
		west_part.setLayout(new BorderLayout());
		west_part.setPreferredSize(new Dimension(200, 600));
		
		//button
		
		
		//init Scrollpane
		mList.setVisibleRowCount(1);
		mScrPane.setViewportView(mList);

		west_part.add(mScrPane, BorderLayout.CENTER);
		west_part.add(delEvent, BorderLayout.NORTH);
		
		delEvent.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				eventBuf.remove(selectIndex);
				bike_Unimpeded = false;
				bike_Congestion = false;
				delivery_Congestion = false;
				//refresh the list
				DefaultListModel<String> listModel = new DefaultListModel<String>();
				for(int index = 0; index < eventBuf.size(); index ++){
						listModel.add(index, index + "," + eventBuf.get(index).getVehicleNum());
						int vtype = eventBuf.get(index).getVehicleType();
						int etype = eventBuf.get(index).getEventType();
						if(vtype == 1){
							if(etype == 0){
								bike_Unimpeded = true;
							}
							else if(etype == 1 || etype == 2){
								bike_Congestion = true;
							}
						}
						else if(vtype == 0 && (etype == 1|| etype == 2)){
							delivery_Congestion = true;
						}
				}
				mList.setModel(listModel);
			}
		});
		
		mList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {//
				JList list = (JList)e.getSource();
				selectIndex = list.getSelectedIndex();
			}
		});
		
	}
	
	/* --------------------------------center part-----------------------------
	 * draw the map
	 * select the edge and junction to add event
	 */
	private JPanel center_part = new JPanel();
	private DrawMap mapPanel ;
	void init_center(){
		mapPanel = new DrawMap(net.getMap());
		center_part.add(mapPanel);
	}

}

package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class NetFileReader extends JFrame{
	//data part
	String netFileName = "D:\\study\\Graduation_Project\\sumo\\test\\map0.net.xml";
	
	/*------------------------------------------------------------*/
	public static void main(String[] args){
		NetFileReader netReader = new NetFileReader();
		
	}
	
	
	public NetFileReader(){
		//init each part
		init_label();
		init_input();
		init_button();
		//Grid layout
		setLayout(new BorderLayout());
		add(label_part, BorderLayout.WEST);
		add(center_part, BorderLayout.CENTER);
		add(button_part, BorderLayout.SOUTH);
		
		//set title
		this.setTitle("NetFileReader");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(450,120);
		this.setVisible(true);
	}
	
	
	
	
	/*--------------------------GUI part------------------------------*/
	
	/*----------------Label part-----------------*/
	//label to hint 
	JPanel label_part = new JPanel();
	JLabel net_hint = new JLabel("   net file address      ");
	//JLabel sumotr_hint = new JLabel("   sumotr file address       ");
	
	//init hint part
	private void init_label(){
		//Gridlayout
		label_part.setLayout(new GridLayout(1,1));
		//add label
		label_part.add(net_hint);
		//label_part.add(sumotr_hint);
	}
	
	/*----------------input part-------------------------*/
	//input name , password and confirmed password
	JPanel center_part = new JPanel();
	JPanel input_part = new JPanel();
	JTextField net_input = new JTextField(netFileName);
	// JTextField sumotr_input = new JTextField(50);

	
	//init password part
	private void init_input(){
		//Flow layout
		center_part.setBorder(new TitledBorder(""));
		//set layout
		center_part.setLayout(new BorderLayout());
		center_part.setPreferredSize(new Dimension(150, 50));
		center_part.add(input_part);
		
		GridLayout input_layout = new GridLayout(1,1);
		input_part.setLayout(input_layout);
		input_part.setSize(130,50);
		
		net_input.setSize(50,10);
		//sumotr_input.setSize(50,10);
		
		//add input field
		input_part.add(net_input);
		//input_part.add(sumotr_input);
			
	}
	
	/*--------button part----------------------------------*/
	//init button part
	JPanel button_part = new JPanel();
	JButton start = new JButton("Start");
	JButton cancel = new JButton("Cancel");
	private void init_button(){
		//Flow layout
		button_part.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
		button_part.add(start);
		button_part.add(cancel);
	
		
		//register mouse listener
		start.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				//get file name
				netFileName = new String(net_input.getText());
				//sumotrFileName = new String(sumotr_input.getText());
				
				
				if(netFileName.equals("")){
					
					EventDefine eventDefine= new EventDefine("D:\\study\\Graduation_Project\\sumo\\test\\map0.net.xml");
				}
				else{
					
					new EventDefine(netFileName);
				}
				//dispose();
			}
		});
		
		cancel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				dispose();
			}
		});
	}
	
}

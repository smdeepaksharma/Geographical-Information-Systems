package com.sdsu.airpollution;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class HotPick extends JDialog {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 2115136160300802290L;
	String mystate = IndiaMap.mystate;
	  String mybird = null;
	  String mybirdpic = null;
	  JPanel jpanel = new JPanel();
	  JPanel jpanel2 = new JPanel();
	  String[][]
	stateBirds={{"Bangalore","html/Bangalore.html"},{"Mumbai","Willow Ptarmigan","willow.jpg"},
	    {"Delhi","Cactus Wren","cactuswren.jpg"},{"Arkansas","Northern Mockingbird","mockingbird.jpg"},
	    {"California","California Quail","califquail.jpg"},{"Colorado","Lark Bunting","larkbunting.jpg"},
	    {"Connecticut","American Robin","robin.jpg"},{"Delaware","Blue Hen Chicken","bluehenchicken.jpg"},
	    {"District of Columbia","Wood Thrush","woodthrush.jpg"},{"Florida","Northern Mockingbird","mockingbird.jpg"},
	    {"Georgia","Brown Thrasher","brownthrasher.jpg"},{"Hawaii","Hawaiian Goose","goose.jpg"},
	    {"Idaho"," Mountain Bluebird","mtnbluebird.jpg"},{"Illinois","Northern Cardinal","cardinal.jpg"},
	    {"Indiana","Northern Cardinal","cardinal.jpg"},{"Iowa","Eastern Goldfinch","goldfinch.jpg"},
	    {"Kansas","Western Meadowlark","meadowlark.jpg"},{"Kentucky","Northern Cardinal","cardinal.jpg"},
	    {"Louisiana","Brown Pelican","pelican.jpg"},{"Maine","Black-capped Chickadee","chickadee.jpg"},
	    {"Maryland","Baltimore Oriole","oriole.jpg"},{"Massachusetts","Black-capped Chickadee","chickadee.jpg"},
	    {"Michigan","American Robin","robin.jpg"},{"Minnesota","Common Loon","loon.jpg"},
	    {"Mississippi","Northern Mockingbird","mockingbird.jpg"},{"Missouri","Eastern Bluebird","bluebird.jpg"},
	    {"Montana","Northern Meadowlark","meadowlark.jpg"},{"Nebraska","Northern Meadowlark","meadowlark.jpg"},
	    {"Nevada","Mountain Bluebird","mtnbluebird.jpg"},{"New Hampshire","Purple Finch","purplefinch.jpg"},
	    {"New Jersey","Eastern Goldfinch","goldfinch.jpg"},{"New Mexico","Roadrunner","roadrunner.jpg"},
	    {"New York","Eastern Bluebird","bluebird.jpg"},{"North Carolina","Northern Cardinal","cardinal.jpg"},
	    {"North Dakota","Western Meadowlark","meadowlark.jpg"},{"Ohio","Northern Cardinal","cardinal.jpg"},
	    {"Oklahoma","Scissor-tailed Flycatcher","flycatcher.jpg"},{"Oregon","Western Meadowlark","meadowlark.jpg"},
	    {"Pennsylvania","Ruffed Grouse","grouse.jpg"},{"Rhode Island","Rhode Island Red Chicken","redchicken.jpg"},
	    {"South Carolina","Northern Mockingbird","mockingbird.jpg"},{"South Dakota","Common Pheasant","pheasant.jpg"},
	    {"Tennessee","Northern Mockingbird","mockingbird.jpg"},{"Texas","NorthernMockingbird","mockingbird.jpg"},
	    {"Utah","California Gull","gull.jpg"},{"Vermont","Hermit Thrush","hermitthrush.jpg"},
	    {"Virginia","Northern Cardin al","cardinal.jpg"},{"Washington","Willow Goldfinch","willowgoldfinch.jpg"},
	    {"West Virginia","Northern Cardinal","cardinal.jpg"},
	    {"Wisconsin","American Robin","robin.jpg"},{"Wyoming","Western Meadowlark","meadowlark.jpg"}};
	  
	
	HotPick() throws IOException {
	    setTitle("This was your pick");
	    setBounds(250,250,350,350);
	    addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	            setVisible(false);
	          }
	    });
	    for (int i = 0;i<51;i++)  {
	          if (stateBirds[i][0].equals(mystate)) {
	        	  mybird = stateBirds[i][1];
	            break;
	          }
	    }
	    JLabel label = new JLabel(mystate+":   ");
	    JLabel label2 = new JLabel("<a href='mybird'></a>");
	   /* ImageIcon birdIcon = new ImageIcon(mybirdpic);
	    JLabel birdLabel = new JLabel(birdIcon);
	    jpanel2.add(birdLabel);*/
	    jpanel.add(label);
	    jpanel.add(label2);
	    getContentPane().add(jpanel2,BorderLayout.CENTER);
	    getContentPane().add(jpanel,BorderLayout.SOUTH);
	  }
	}
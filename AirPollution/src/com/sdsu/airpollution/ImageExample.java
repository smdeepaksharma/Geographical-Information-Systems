package com.sdsu.airpollution;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ImageExample extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3695848228543934346L;
	int i = 1;
	ImageIcon img = new ImageIcon("slider/1.png");
	
	JLabel imgLbl = new JLabel(img);
	JLabel lnkLabel = new JLabel();
	JButton btnNext = new JButton(new ImageIcon("images/n.png"));
	JButton btnPrevious = new JButton(new ImageIcon("images/p.png"));
	JPanel btbnPanel = new JPanel();
	
	JPanel infoPanel = new JPanel();
	
	JLabel infoLabel = new JLabel();
	JPanel linkPanel = new JPanel();
	String info2;
	String info1;
	
	ImageExample() {
		
		setSize(800, 600);
		setLayout(new BorderLayout());
		
		getContentPane().add(btbnPanel,BorderLayout.SOUTH);
		getContentPane().add(infoPanel,BorderLayout.NORTH);
		btbnPanel.add(btnPrevious);
		btbnPanel.add(btnNext);	
		add(imgLbl,BorderLayout.CENTER);
		imgLbl.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		btnNext.setPreferredSize(new Dimension(40,35));
		btnPrevious.setPreferredSize(new Dimension(40,35));
		btnNext.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnNext.setToolTipText("Next");
		btnPrevious.setToolTipText("Previous");
		setVisible(true);
		//imgLbl.setBorder(BorderFactory.createLineBorder(Color.black));
		
		
		info1 = "<html><h3>Bangalore generates 2500 tonnes of solid waste every day,<br> "
						+ "and this waste is often disposed off in a very unscientific manner. And this worsens the<br> "
						+ "situation in the polluted 'garden city'.<br><a href=http://www.w3schools.com>Visit W3Schools.com!</a></h3>";
		
		info2 =	"<html><h3>Once upon a time, walkers in the famous Cubbon Park and Lal Bagh used to enjoy the fresh <br>"
						+ "air during their walks. Today, a majority of them are forced to wear pollution masks during<br> "
						+ "their morning and evening walks. Rapid industrialisation anda surge in the number of vehicles<br> "
						+ "have made this once beautiful city explode into metropolitan nightmare.</h3></html>";
		infoLabel.setText(info1);
		infoPanel.add(infoLabel);
		lnkLabel.setText("<html></html>");
		lnkLabel.add(linkPanel);
		getContentPane().add(linkPanel,BorderLayout.EAST);
		
		
	}

	public void next() {
		if (i >= 4) {
			i = 1;
		} else {
			i++;
		}
		img = new ImageIcon("slider/" + i + ".png");
		if(i==1) { infoLabel.setText(info1);}
		else if(i==2) {infoLabel.setText(info2);}
		else{ infoLabel.setText("info3");}
		imgLbl.setIcon(img);

	}

	public void previous() {
		// System.out.println("i="+i);
		if (i <= 1) {
			i = 4;

		} else {
			i--;
		}
		img = new ImageIcon("slider/" + i + ".png");
		imgLbl.setIcon(img);
		if(i==1) { infoLabel.setText(info1);}
		else if(i==2) {infoLabel.setText(info2);}
		else{ infoLabel.setText("info3");}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnNext) {
			next();
		}
		if (ae.getSource() == btnPrevious) {
			previous();
		}
	}

	public static void main(String args[]) {
		ImageExample i = new ImageExample();
	}
}

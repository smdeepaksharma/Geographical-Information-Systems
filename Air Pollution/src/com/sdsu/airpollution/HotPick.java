package com.sdsu.airpollution;

/**
 * @author Deepak
 */
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HotPick extends JDialog implements ActionListener {
	private static final long serialVersionUID = 3695848228543934346L;
	int i = 0;

	// Slider
	ImageIcon img = IndiaMap.getScaledImage(
			new ImageIcon("slider/Default.png"), 600, 250);
	JLabel imgLbl = new JLabel(img);
	JPanel imageSlider = new JPanel(new BorderLayout());
	JButton btnNext = new JButton(IndiaMap.getScaledImage(new ImageIcon(
			"images/FRD.png"), 30, 30));
	JButton btnPrevious = new JButton(IndiaMap.getScaledImage(new ImageIcon(
			"images/BKRD.png"), 30, 30));

	// Content
	private JEditorPane pane;
	// Hot Links
	JPanel linkPanel = new JPanel(new FlowLayout());
	JLabel lnkLabel = new JLabel("HOT LINKS:");
	JButton video1 = new JButton("Watch Videos");
	JButton link1 = new JButton(
			"Click here to view Real Time Air Quality Index");
	JButton link2 = new JButton("Google Search");

	public String stateName;
	HashMap<String, String> contenturls = new HashMap<String, String>();
	HashMap<String, String> videourls = new HashMap<String, String>();

	public HotPick(URL url, String stateName) {

		setTitle(stateName);
		createUrl();
		videoUrl();
		this.stateName = stateName;

		linkPanel.add(lnkLabel);
		linkPanel.add(link1);
		linkPanel.add(video1);
		linkPanel.add(link2);

		// web video links
		video1.addActionListener(this);
		btnNext.addActionListener(this);
		btnPrevious.addActionListener(this);

		btnNext.setToolTipText("Next");
		btnPrevious.setToolTipText("Previous");

		pane = new JEditorPane();
		pane.setEditable(false); // Read-only

		link1.addActionListener(this);
		link2.addActionListener(this);
		String lngurl = url + ".html";
		if (IndiaMap.langFlag == false) {
			lngurl = url + "Sp.html";
			video1.setText("Ver videos");
			link1.setText("Haga clic aquí para ver el índice de calidad del aire en tiempo real");
			link2.setText("Búsqueda de Google");
		}
		try {
			pane.setPage(lngurl);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(pane, "Error");
		}
		JScrollPane scroll = new JScrollPane(pane);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setSize(700, 600);
		pane.setBounds(60, 0, 600, 250);
		pane.setAutoscrolls(true);
		imageSlider.add(imgLbl, BorderLayout.CENTER);
		imageSlider.add(btnNext, BorderLayout.EAST);
		imageSlider.add(btnPrevious, BorderLayout.WEST);
		getContentPane().add(imageSlider, BorderLayout.NORTH);
		getContentPane().add(scroll, BorderLayout.CENTER);
		getContentPane().add(linkPanel, BorderLayout.SOUTH);
		setVisible(true);
		setDefaultLookAndFeelDecorated(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setVisible(false);
			}
		});
	}

	public void next() {
		if (i >= 4) {
			i = 1;
		} else {
			i++;
		}
		img = IndiaMap.getScaledImage(new ImageIcon("slider/" + stateName + i
				+ ".png"), 600, 250);
		imgLbl.setIcon(img);
	}

	public void previous() {
		if (i <= 1) {
			i = 4;
		} else {
			i--;
		}
		img = IndiaMap.getScaledImage(new ImageIcon("slider/" + stateName + i
				+ ".png"), 600, 250);
		imgLbl.setIcon(img);
	}

	private void createUrl() {
		contenturls.put("Bangalore",
				"http://aqicn.org/city/india/bangalore/bwssb/");
		contenturls.put("Delhi", "http://aqicn.org/city/delhi/shadipur/");
		contenturls.put("Ahmedabad",
				"http://aqicn.org/city/india/ahmedabad/maninagar/");
		contenturls.put("Kanpur",
				"http://aqicn.org/city/india/kanpur/nehru-nagar/");
		contenturls.put("Gwalior",
				"https://www.numbeo.com/pollution/in/Gwalior-India");
	}

	private void videoUrl() {
		videourls.put("Bangalore", "html/BloreVideo.html");
		videourls.put("Delhi", "html/DelhiVideo.html");
		videourls.put("Ahmedabad", "html/AhmedabadVideo.html");
		videourls.put("Kanpur", "html/KanpurVideo.html");
		videourls.put("Gwalior", "html/GwaliorVideo.html");
	}

	public void actionPerformed(ActionEvent ae) {
		String uri;
		if (ae.getSource() == btnNext) {
			next();
		}
		if (ae.getSource() == btnPrevious) {
			previous();
		}
		if (ae.getSource() == link1) {
			uri = contenturls.get(stateName);
			System.out.println("Browse uri" + uri);
			try {
				Desktop.getDesktop().browse(new URI(uri));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(getContentPane(),
						"Unable to launch browser");
			}
		}
		if (ae.getSource() == link2) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.google.com"));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(getContentPane(),
						"Unable to launch browser");
			}
		}
		if (ae.getSource() == video1) {
			try {
				URI vpath = (new java.io.File(videourls.get(stateName)))
						.toURI();
				Desktop.getDesktop().browse(vpath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(getContentPane(),
						"Unable to launch browser");
			}
		}
	}
}

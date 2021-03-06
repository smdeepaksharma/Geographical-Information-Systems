package com.sdsu.airpollution;

/**
 * @author Deepak
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class Help extends JDialog {

	private static final long serialVersionUID = 1L;
	private JEditorPane pane;

	public Help(String url) {

		setTitle("Help");
		pane = new JEditorPane();
		pane.setEditable(false); // Read-only
		getContentPane().add(new JScrollPane(pane), "Center");
		String lngurl = url + ".html";
		if (IndiaMap.langFlag == false) {
			lngurl = url + "Sp.html";
		}
		try {
			// display the page
			pane.setPage(lngurl);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(pane, "Error");
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setVisible(false);
			}
		});
		this.setBounds(800, 10, 500, 500);
	}

}

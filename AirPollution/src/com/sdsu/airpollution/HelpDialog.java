package com.sdsu.airpollution;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class HelpDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8984122966800826577L;
	JTextArea helptextArea;

	public HelpDialog(String inputText) {
		setBounds(70, 70, 450, 250);
		setTitle("Help");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		helptextArea = new JTextArea(inputText, 7, 40);
		Font f = Font.getFont("MONOSPACED");
		helptextArea.setFont(f);
		JScrollPane scrollPane = new JScrollPane(helptextArea);
		helptextArea.setEditable(false);
		getContentPane().add(scrollPane,"Center");
		
	}
}

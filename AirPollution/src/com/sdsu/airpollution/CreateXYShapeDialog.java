package com.sdsu.airpollution;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.esri.mo2.file.shp.ShapefileWriter;
import com.esri.mo2.file.shp.ShapefileFolder;

@SuppressWarnings("deprecation")
class CreateXYShapeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4592897556484423122L;
	String name = "";
	String path = "";
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JTextField nameField = new JTextField(
			"enter layer name here, then hit ENTER", 35);
	JTextField pathField = new JTextField(
			"enter full path name here, then hit ENTER", 35);
	com.esri.mo2.map.dpy.FeatureLayer XYlayer;
	ActionListener lis = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			Object o = ae.getSource();
			if (o == pathField) {
				path = pathField.getText().trim();
				System.out.println("FILE PATH : " + path);
			} else if (o == nameField) {
				name = nameField.getText().trim();
				System.out.println("FULL PATH : " + path + "    " + name);
			} else if (o == cancel)
				setVisible(false);
			else {
				try {
					ShapefileWriter.writeFeatureLayer(XYlayer, path, name, 0);
				} catch (Exception e) {
					System.out.println("write error");
				}
				setVisible(false);
			}
		}
	};

	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JLabel centerlabel = new JLabel();

	// center label;
	CreateXYShapeDialog(com.esri.mo2.map.dpy.FeatureLayer layer5) {
		XYlayer = layer5;
		setBounds(40, 250, 600, 300);
		setTitle("Create new shapefile?");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		nameField.addActionListener(lis);
		pathField.addActionListener(lis);
		ok.addActionListener(lis);
		cancel.addActionListener(lis);
		String s = "<HTML> To make a new shapefile from the new layer, enter<BR>"
				+ "the new name you want for the layer and hit ENTER.<BR>"
				+ "then enter a path to the folder you want to use <BR>"
				+ "and hit ENTER once again <BR>"
				+ "As an example type C:\\mylayers<BR>"
				+ "You can then add it to the map in the usual way.<BR>"
				+ "Click ENTER after replacing the text with your layer name";
		centerlabel.setHorizontalAlignment(JLabel.CENTER);
		centerlabel.setText(s);
		// getContentPane().add(centerlabel,BorderLayout.CENTER);
		panel1.add(centerlabel);
		panel1.add(nameField);
		panel1.add(pathField);
		panel2.add(ok);
		panel2.add(cancel);
		getContentPane().add(panel2, BorderLayout.SOUTH);
		getContentPane().add(panel1, BorderLayout.CENTER);
	}
}

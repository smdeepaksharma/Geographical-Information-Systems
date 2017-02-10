package com.sdsu.airpollution;

/**
 * 
 * @author Deepak
 * AddLayerDialog class contains methods to create a dialog window to enable the 
 * users to add new layers to the map
 *
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import com.esri.mo2.ui.bean.Map;

public class AddLayerDialog extends JDialog {

	private static final long serialVersionUID = -6757856558095236683L;
	Map map;
	ActionListener lis;
	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JPanel panel1 = new JPanel();
	@SuppressWarnings("deprecation")
	com.esri.mo2.ui.bean.CustomDatasetEditor cus = new com.esri.mo2.ui.bean.CustomDatasetEditor();

	AddLayerDialog() throws IOException {
		setBounds(50, 50, 520, 430);
		setTitle("Select a theme/layer");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		lis = new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source == cancel)
					setVisible(false);
				else {
					try {
						setVisible(false);
						map.getLayerset().addLayer(cus.getLayer());
						map.redraw();
					} catch (IOException e) {
					}
				}
			}
		};
		ok.addActionListener(lis);
		cancel.addActionListener(lis);
		getContentPane().add(cus, BorderLayout.CENTER);
		panel1.add(ok);
		panel1.add(cancel);
		getContentPane().add(panel1, BorderLayout.SOUTH);
	}

	public void setMap(com.esri.mo2.ui.bean.Map map1) {
		map = map1;
	}
}
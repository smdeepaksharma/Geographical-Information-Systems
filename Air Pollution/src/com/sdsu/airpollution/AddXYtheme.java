package com.sdsu.airpollution;

/**
 * @author Deepak
 * AddXYtheme class contains methods to add new features to the map using csv files
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.cs.geom.Point;
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.ui.bean.Map;

class AddXYtheme extends JDialog {

	private static final long serialVersionUID = 3177378516699019213L;
	Map map;
	Vector<FieldsData> s2 = new Vector<FieldsData>();
	JFileChooser jfc = new JFileChooser();
	BasePointsArray bpa = new BasePointsArray();
	FeatureLayer XYlayer;

	AddXYtheme() throws IOException {
		setBounds(50, 50, 520, 430);
		jfc.showOpenDialog(this);
		try {
			File file = jfc.getSelectedFile();
			FileReader fred = new FileReader(file);
			BufferedReader in = new BufferedReader(fred);
			String s; // = in.readLine();
			double x, y;
			int n = 0;
			while ((s = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				System.out.println("X :" + x);
				System.out.println("Y :" + y);
				bpa.insertPoint(n++, new Point(x, y));
				FieldsData data = new FieldsData();
				data.setCityName(st.nextToken());
				data.setPh10Val(st.nextToken());
				data.setStateName(st.nextToken());
				s2.addElement(data);
			}
			in.close();
		} catch (IOException e) {
		}

		XYfeatureLayer xyfl = new XYfeatureLayer(bpa, map, s2);
		XYlayer = xyfl;
		xyfl.setVisible(true);
		map = IndiaMap.map;
		map.getLayerset().addLayer(xyfl);
		map.redraw();
		CreateXYShapeDialog xydialog = new CreateXYShapeDialog(XYlayer);
		xydialog.setVisible(true);
	}

	public void setMap(com.esri.mo2.ui.bean.Map map1) {
		map = map1;
	}
}

class FieldsData {
	private String cityName;
	private String ph10Val;
	private String stateName;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPh10Val() {
		return ph10Val;
	}

	public void setPh10Val(String ph10Val) {
		this.ph10Val = ph10Val;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}

package com.sdsu.airpollution;

/**
 * @author Deepak
 */
import java.awt.Color;
import java.util.Vector;
import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.cs.geom.Point;
import com.esri.mo2.data.feat.BaseDataID;
import com.esri.mo2.data.feat.BaseFeature;
import com.esri.mo2.data.feat.BaseFeatureClass;
import com.esri.mo2.data.feat.BaseField;
import com.esri.mo2.data.feat.BaseFields;
import com.esri.mo2.data.feat.Feature;
import com.esri.mo2.data.feat.Field;
import com.esri.mo2.data.feat.MapDataset;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.dpy.LayerCapabilities;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.map.mem.MemoryFeatureClass;
import com.esri.mo2.ui.bean.Map;

class XYfeatureLayer extends BaseFeatureLayer {
	BaseFields fields;
	@SuppressWarnings("rawtypes")
	private java.util.Vector featureVector;

	public XYfeatureLayer(BasePointsArray bpa, Map map, Vector<FieldsData> s2) {
		createFeaturesAndFields(bpa, map, s2);
		BaseFeatureClass bfc = getFeatureClass("MyPoints", bpa);
		setFeatureClass(bfc);
		BaseSimpleRenderer srd = new BaseSimpleRenderer();
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol();
		sms.setType(SimpleMarkerSymbol.CIRCLE_MARKER);
		sms.setSymbolColor(Color.RED);
		sms.setWidth(18);
		sms.setTransparency(0.4);
		;
		sms.setOutline(Color.black);
		srd.setSymbol(sms);
		setRenderer(srd);
		// without setting layer capabilities, the points will not
		// display (but the toc entry will still appear)
		XYLayerCapabilities lc = new XYLayerCapabilities();
		setCapabilities(lc);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createFeaturesAndFields(BasePointsArray bpa, Map map,
			Vector<FieldsData> s2) {
		featureVector = new Vector();
		fields = new BaseFields();
		createDbfFields();
		for (int i = 0; i < bpa.size(); i++) {
			BaseFeature feature = new BaseFeature(); // feature is a row
			feature.setFields(fields);
			Point p = new Point(bpa.getPoint(i));
			feature.setValue(0, p);
			System.out.println(p);
			feature.setValue(1, new Integer(0)); // point data
			FieldsData d = s2.elementAt(i);

			feature.setValue(2, d.getCityName());
			feature.setValue(3, Integer.parseInt(d.getPh10Val()));
			feature.setValue(4, d.getStateName());
			// System.out.println("What is this?"+(String)s2.elementAt(i));
			feature.setDataID(new BaseDataID("MyPoints", i));
			featureVector.addElement(feature);
		}
	}

	private void createDbfFields() {
		fields.addField(new BaseField("#SHAPE#", Field.ESRI_SHAPE, 0, 0));
		fields.addField(new BaseField("ID", java.sql.Types.INTEGER, 9, 0));
		// VARCHAR field width is VERY important--must be large enough
		fields.addField(new BaseField("Name", java.sql.Types.VARCHAR, 35, 0));
		fields.addField(new BaseField("PM05", java.sql.Types.INTEGER, 12, 0));
		fields.addField(new BaseField("State_Name", java.sql.Types.VARCHAR, 35,
				0));
	}

	public BaseFeatureClass getFeatureClass(String name, BasePointsArray bpa) {
		MemoryFeatureClass featClass = null;
		try {
			featClass = new MemoryFeatureClass(MapDataset.POINT, fields);
		} catch (IllegalArgumentException iae) {
		}
		featClass.setName(name);
		for (int i = 0; i < bpa.size(); i++) {
			featClass.addFeature((Feature) featureVector.elementAt(i));
		}
		return featClass;
	}

	private final class XYLayerCapabilities extends LayerCapabilities {
		@SuppressWarnings("static-access")
		XYLayerCapabilities() {
			for (int i = 0; i < this.size(); i++) {
				setAvailable(this.getCapabilityName(i), true);
				setEnablingAllowed(this.getCapabilityName(i), true);
				getCapability(i).setEnabled(true);
			}
		}
	}
}
package com.sdsu.airpollution;

/**
 * @author Deepak
 * 
 */
import javax.swing.*;

import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;

import com.esri.mo2.ui.bean.*; // beans used: Map,Layer,Toc,TocAdapter,Tool
// TocEvent,Legend(a legend is part of a toc),ActateLayer
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;

import javax.swing.border.BevelBorder;

import org.jfree.ui.RefineryUtilities;

import com.esri.mo2.data.feat.*; //ShapefileFolder, ShapefileWriter
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.AoFillStyle;
import com.esri.mo2.map.draw.AoLineStyle;
import com.esri.mo2.map.draw.BaseClassBreaksRenderer;
import com.esri.mo2.map.draw.BaseRange;
import com.esri.mo2.map.draw.BaseSimpleLabelRenderer;
import com.esri.mo2.map.draw.RasterMarkerSymbol;
import com.esri.mo2.map.draw.SimpleLineSymbol;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.SimplePolygonSymbol;
import com.esri.mo2.map.draw.Symbol;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.ui.dlg.AboutBox;

import java.awt.image.BufferedImage;

import com.esri.mo2.cs.geom.*; //using Envelope, Point, BasePointsArray

public class IndiaMap extends JFrame {

	private static final long serialVersionUID = -3832270804069814609L;
	static Map map = new Map();
	static boolean fullMap = true; // Map not zoomed
	static boolean langFlag = true;
	static boolean helpToolOn;
	Legend legend;
	Legend legend2;

	Layer baselayer = new Layer();
	Layer layer3 = null;
	Layer pm10 = new Layer();
	Layer pm05 = new Layer();
	Layer splayer2 = new Layer();
	Layer splayer = new Layer();
	static AcetateLayer acetLayer;
	static com.esri.mo2.map.dpy.Layer layer4;
	com.esri.mo2.map.dpy.Layer activeLayer;

	int activeLayerIndex = -1;
	com.esri.mo2.cs.geom.Point initPoint, endPoint;
	double distance;

	JMenuBar mbar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu theme = new JMenu("Theme");
	JMenu layercontrol = new JMenu("LayerControl");
	JMenu help = new JMenu("Help");
	JMenu charts = new JMenu("Charts");

	JMenuItem pm10_chart = new JMenuItem("PM 10");
	JMenuItem pm05_chart = new JMenuItem("PM 05");

	// Theme menu items
	JMenuItem openAtbItem = new JMenuItem("Open attribute table",
			new ImageIcon("images/tableview.gif"));
	JMenuItem createlayeritem = new JMenuItem("Create layer from selection",
			new ImageIcon("images/Icon0915b.jpg"));
	// Layer control items
	static JMenuItem promoteitem = new JMenuItem("Promote selected layer",
			new ImageIcon("images/promote.png"));
	JMenuItem demoteitem = new JMenuItem("Demote selected layer",
			new ImageIcon("images/demote.png"));
	JMenuItem promoteToTop = new JMenuItem("Promote to top", new ImageIcon(
			"images/top.png"));
	// File menu items
	JMenuItem printitem = new JMenuItem("print", new ImageIcon(
			"images/print.gif"));
	JMenuItem addlyritem = new JMenuItem("add layer", new ImageIcon(
			"images/addtheme.gif"));
	JMenuItem remlyritem = new JMenuItem("remove layer", new ImageIcon(
			"images/delete.gif"));
	JMenuItem propsitem = new JMenuItem("Legend Editor", new ImageIcon(
			"images/properties.gif"));
	// Help menu items
	JMenu about = new JMenu("About");
	JMenuItem aboutmap = new JMenuItem("Air Pollution Map");
	JMenuItem abtMojo = new JMenuItem("About MOJO...");
	JMenuItem contact = new JMenuItem("Contact");

	JMenu helpTopics = new JMenu("Help topics");
	JMenuItem tbContents = new JMenuItem("Table of contents");
	JMenuItem lengedEditor = new JMenuItem("Legend editor");
	JMenuItem layerControlHelp = new JMenuItem("Layer Control");

	Toc toc = new Toc();

	// Path of shape files
	// String s1 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\INDIA.shp";
	// String s3 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\In\\PM10EN.shp";
	// String s4 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\In\\PM05EN.shp";
	// String s5 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\In\\PM10MX.shp";
	// String s6 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\In\\PM05MX.shp";
	String s1 = "shapefiles/INDIA.shp";
	String s3 = "shapefiles/PM10EN.shp";
	String s4 = "shapefiles/PM05EN.shp";
	String s5 = "shapefiles/PM10MX.shp";
	String s6 = "shapefiles/PM05MX.shp";
	String datapathname = "";
	String legendname = "";

	ZoomPanToolBar zptb = new ZoomPanToolBar();
	static SelectionToolBar stb = new SelectionToolBar();
	JToolBar customTB = new JToolBar();

	ComponentListener complistener;

	JLabel statusLabel = new JLabel("status bar    LOC");
	static JLabel milesLabel = new JLabel("   DISTANCE:  0 miles    ");
	static JLabel kmLabel = new JLabel("  0 km    ");
	static JLabel cityName = new JLabel(" ");
	java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");

	JPanel myjp = new JPanel();
	JPanel myjp2 = new JPanel();

	JButton printButton = new JButton(new ImageIcon("icons/print.png"));
	JButton addLayerButton = new JButton(new ImageIcon("icons/addlayer.png"));
	// JButton pointerButton = new JButton(new ImageIcon("icons/cursor.png"));
	JButton pointerButton = new JButton(new ImageIcon("icons/cursor.png"));
	JButton distanceJB = new JButton(new ImageIcon("images/measure_1.gif"));
	JButton XYjb = new JButton("XY");
	JButton hotjb = new JButton(new ImageIcon("icons/hotlink16X16.png"));
	JButton helpToolbutton = new JButton(new ImageIcon("icons/helptool.png"));

	JButton zoomInButton;
	JButton zoomOut;
	JButton zoomFullExtentButton;
	JButton zoomToSelectedLayerButton;
	JButton pan;
	JButton panoneDirection;
	JButton nxtExtent;
	JButton prvExtent;
	JButton identifyTool;

	JButton searchTool;
	JButton findTool;
	JButton queryBuilder;
	JButton clearAllSelection;
	JButton selectFeatures;
	JButton attributes;
	JButton bufferTool;

	String chartTitle = "Unhealthiest Cities In India";
	// Arrow arrow = new Arrow();
	// DistanceTool distanceTool= new DistanceTool();

	/*********** Internationalization *******************/
	JMenu language = new JMenu("Select Language");
	JMenuItem english = new JMenuItem("Inglés - English");
	JMenuItem spanish = new JMenuItem("Español - Spanish");
	public static ResourceBundle names; // (1)
	Locale loc1 = new Locale("es", "MX");
	Locale loc2 = new Locale("en", "US");

	/*****************************************************/

	ActionListener customTBListner;
	ActionListener layerListener;
	ActionListener layerControlListener;
	ActionListener helpListener;
	ActionListener chartListner;
	ActionListener languagelis;
	TocAdapter mytocadapter;

	static Envelope env;
	static HelpTool helpTool = new HelpTool();
	AttributeTable attrtab = null;

	// hot link
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image bolt = tk.getImage("images/htlink.gif");
	java.awt.Cursor boltCursor = tk.createCustomCursor(bolt,
			new java.awt.Point(11, 26), "bolt");
	MyPickAdapter picklis = new MyPickAdapter();
	// the Identify class implements a PickListener
	Identify hotlink = new Identify();
	static String mystate = null;

	/*************************** PICK ADAPTER **************************/
	class MyPickAdapter implements PickListener {
		// implements hot link
		public void beginPick(PickEvent pe) {
		};

		public void endPick(PickEvent pe) {
		}

		@SuppressWarnings("unused")
		public void foundData(PickEvent pe) {
			// fires only when a layer feature is clicked
			FeatureLayer flayer2 = (FeatureLayer) pe.getLayer();
			com.esri.mo2.data.feat.Cursor c = pe.getCursor();
			Feature f = null;
			System.out.println("inside foundData");
			Fields fields = null;
			if (c != null) {
				f = (Feature) c.next();
				System.out.println("Inside if block" + f.getClass().toString());
				fields = f.getFields();
				String sname = fields.getField(2).getName();
				System.out.println("S name: " + sname);
				mystate = (String) f.getValue(2);
				System.out.println("My state" + mystate);
				try {
					System.out.println("Inside pick adapter try block");
					String url = "html/" + mystate;
					URL path = (new java.io.File(url)).toURI().toURL();
					System.out.println(path);
					HotPick hotpick = new HotPick(path, mystate);
					hotpick.setVisible(true);
				} catch (Exception e) {
				}
			}
		}
	};

	/*************************** END OF PICK ADAPTER **************************/
	// Constructor
	@SuppressWarnings("rawtypes")
	public IndiaMap() {

		super("Air Pollution Index of India");
		this.setBounds(10, 10, 900, 700);

		zptb.setMap(map);
		stb.setMap(map);

		setJMenuBar(mbar);
		mbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		// to handle component resize problem
		ActionListener lisZoom = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fullMap = false;
			}
		};
		ActionListener lisFullExt = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fullMap = true;
			}
		};
		// next line gets ahold of a reference to the zoom in button
		nxtExtent = (JButton) zptb.getActionComponent(1);
		prvExtent = (JButton) zptb.getActionComponent(0);
		zoomOut = (JButton) zptb.getActionComponent(5);
		zoomOut.setIcon(new ImageIcon("icons/zoomout.png"));
		pan = (JButton) zptb.getActionComponent(6);
		panoneDirection = (JButton) zptb.getActionComponent(7);
		identifyTool = (JButton) zptb.getActionComponent(8);
		identifyTool.setIcon(new ImageIcon("icons/ident.png"));

		searchTool = (JButton) stb.getActionComponent(0);
		findTool = (JButton) stb.getActionComponent(1);
		queryBuilder = (JButton) stb.getActionComponent(2);
		selectFeatures = (JButton) stb.getActionComponent(3);
		clearAllSelection = (JButton) stb.getActionComponent(4);
		attributes = (JButton) stb.getActionComponent(5);
		bufferTool = (JButton) stb.getActionComponent(6);

		zoomInButton = (JButton) zptb.getActionComponent("ZoomIn");
		zoomInButton.setIcon(new ImageIcon("icons/zoomIn.png"));
		zoomFullExtentButton = (JButton) zptb.getActionComponent(3);
		zoomToSelectedLayerButton = (JButton) zptb
				.getActionComponent("ZoomToSelectedLayer");
		zoomInButton.addActionListener(lisZoom);
		zoomFullExtentButton.addActionListener(lisFullExt);
		zoomToSelectedLayerButton.addActionListener(lisZoom);
		complistener = new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				if (fullMap) {
					map.setExtent(env);
					map.zoom(1.0);
					map.redraw();
				}
			}
		};
		addComponentListener(complistener);

		/******************* HELP TOOL ************************************/
		MouseAdapter xy = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					HelpToolDialog helpdialog = new HelpToolDialog(
							"file:///E:/CommonWorkSpace/GIS/AirPollution/html/xy");
					helpdialog.setVisible(true);
				}
			}
		};
		MouseAdapter mlLisAddLayer = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					HelpToolDialog helpdialog = new HelpToolDialog(
							"file:///E:/CommonWorkSpace/GIS/AirPollution/html/addlayer");
					helpdialog.setVisible(true);
				}
			}
		};
		MouseAdapter bolt = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					HelpToolDialog helpdialog = new HelpToolDialog(
							"file:///E:/CommonWorkSpace/GIS/AirPollution/html/bolt");
					helpdialog.setVisible(true);
				}
			}
		};
		MouseAdapter distance = new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
					HelpToolDialog helpdialog = new HelpToolDialog(
							"file:///E:/CommonWorkSpace/GIS/AirPollution/html/distance");
					helpdialog.setVisible(true);
				}
			}
		};
		/*************************************************************************/
		/***************** CUSTOM TOOL BAR LISTENER ADAPTER **********************/
		customTBListner = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source == printButton || source instanceof JMenuItem) {
					com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
					mapPrint.setMap(map);
					mapPrint.doPrint();// prints the map
				} else if (source == pointerButton) {
					Arrow arrow = new Arrow();
					map.setSelectedTool(arrow);
				} else if (source == distanceJB) {
					DistanceTool distanceTool = new DistanceTool();
					map.setSelectedTool(distanceTool);
				} else if (source == XYjb) {
					try {
						AddXYtheme addXYtheme = new AddXYtheme();
						addXYtheme.setMap(map);
						addXYtheme.setVisible(false);// the file chooser needs a
														// parent
						// but the parent can stay behind the scenes
						map.redraw();
					} catch (IOException e) {
					}
				} else if (source == hotjb) {
					hotlink.setCursor(boltCursor);
					map.setSelectedTool(hotlink);
				} else if (source == helpToolbutton) {
					helpToolOn = true;
					map.setSelectedTool(helpTool);
				} else {
					try {
						AddLayerDialog aldlg = new AddLayerDialog();
						aldlg.setMap(map);
						aldlg.setVisible(true);
					} catch (IOException e) {
					}
				}
			}
		};
		/***************** END OF CUSTOM TOOL BAR LISTENER ADAPTER **********************/

		/********************* LAYER CONTROL LISTENER ***********************************/
		layerControlListener = new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				System.out.println(activeLayerIndex + " active index");
				if (source == promoteitem) {
					map.getLayerset().moveLayer(activeLayerIndex,
							++activeLayerIndex);
				} else if (source == promoteToTop) {
					int layerCount = map.getLayerset().getSize();
					map.getLayerset().moveLayer(activeLayerIndex,
							layerCount - 1);
					activeLayerIndex = -2;
				} else {
					map.getLayerset().moveLayer(activeLayerIndex,
							--activeLayerIndex);
				}
				enableDisableButtons();
				map.redraw();
			}
		};
		/********************* END OF LAYER CONTROL LISTENER *******************************/

		/*********************** HELP MENU LISTENER ***********************************/
		helpListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source instanceof JMenuItem) {

					if (source == abtMojo) {
						AboutBox aboutBox = new AboutBox();
						aboutBox.setProductName("MOJO");
						aboutBox.setProductVersion("2.0");
						aboutBox.setVisible(true);
					} else if (source == tbContents) {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/toc");
						hpdg.setVisible(true);
					} else if (source == layerControlHelp) {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/layerchelp");
						hpdg.setVisible(true);
					} else if (source == lengedEditor) {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/legendhelp");
						hpdg.setVisible(true);
					} else if (source == aboutmap) {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/aboutMap");
						hpdg.setVisible(true);
					} else if (source == contact) {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/contact");
						hpdg.setVisible(true);
					}
				}
			}
		};
		/*********************** END OF HELP MENU LISTENER ***********************************/
		/*
		 * helpListener = new ActionListener() { public void
		 * actionPerformed(ActionEvent ae) { Object source = ae.getSource(); if
		 * (source instanceof JMenuItem) { String arg = ae.getActionCommand();
		 * if (arg == "About MOJO...") { AboutBox aboutBox = new AboutBox();
		 * aboutBox.setProductName("MOJO"); aboutBox.setProductVersion("2.0");
		 * aboutBox.setVisible(true); } } } };
		 */
		/************************* FILE MENU LISTENER **********************************/
		layerListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source instanceof JMenuItem) {

					if (source == addlyritem) {
						try {
							AddLayerDialog aldlg = new AddLayerDialog();
							aldlg.setMap(map);
							aldlg.setVisible(true);
						} catch (IOException e) {
						}
					} else if (source == remlyritem) {
						try {
							com.esri.mo2.map.dpy.Layer dpylayer = legend
									.getLayer();
							map.getLayerset().removeLayer(dpylayer);
							map.redraw();
							remlyritem.setEnabled(false);
							propsitem.setEnabled(false);
							openAtbItem.setEnabled(false);
							promoteitem.setEnabled(false);
							demoteitem.setEnabled(false);
							promoteToTop.setEnabled(false);
							stb.setSelectedLayer(null);
							zptb.setSelectedLayer(null);
						} catch (Exception e) {
						}
					} else if (source == propsitem) {
						LayerProperties lp = new LayerProperties();
						lp.setLegend(legend);
						lp.setSelectedTabIndex(0);
						lp.setVisible(true);
					} else if (source == openAtbItem) {
						try {
							layer4 = legend.getLayer();
							AttributeTable attrtab = new AttributeTable();
							attrtab.setVisible(true);
						} catch (IOException ioe) {
						}
					} else if (source == createlayeritem) {
						BaseSimpleRenderer sbr = new com.esri.mo2.map.draw.BaseSimpleRenderer();
						// For polygons
						SimplePolygonSymbol simplepolysymbol = new com.esri.mo2.map.draw.SimplePolygonSymbol();
						// For circles
						SimpleMarkerSymbol flashcircle = new SimpleMarkerSymbol();
						// For lines
						SimpleLineSymbol flashLine = new SimpleLineSymbol();
						simplepolysymbol.setPaint(AoFillStyle.getPaint(
								com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL,
								new java.awt.Color(255, 255, 0)));
						simplepolysymbol.setBoundary(true);
						flashcircle.setOutline(new Color(255, 255, 255));
						flashcircle.setSymbolColor(Color.RED);
						flashcircle.setType(SimpleMarkerSymbol.CROSS_MARKER);
						flashLine.setLineColor(new Color(255, 0, 0));
						layer4 = legend.getLayer();
						FeatureLayer flayer2 = (FeatureLayer) layer4;
						// next line verifies a selection was made
						System.out.println("has selected"
								+ flayer2.hasSelection());
						// next line creates the 'set' of selections
						if (flayer2.hasSelection()) {
							SelectionSet selectset = flayer2.getSelectionSet();
							FeatureLayer selectedlayer = flayer2
									.createSelectionLayer(selectset);

							FeatureClass selectedclass = selectedlayer
									.getFeatureClass();
							RasterMarkerSymbol rms = new RasterMarkerSymbol();

							int type = selectedclass.getFeatureType();
							System.out.println("Type id:" + type);
							if (type == 0) {
								rms.setSizeX(10);
								rms.setSizeY(10);
								rms.setImageString("images/bulleye.png");
								sbr.setSymbol(rms);
							} else if (type == 1) {
								flashLine.setStroke(AoLineStyle.getStroke(
										AoLineStyle.DASH_LINE, 5));
								flashLine.setLineColor(Color.RED);
								sbr.setSymbol(flashLine);
							} else if (type == 2) {
								sbr.setSymbol(simplepolysymbol);
							}
							sbr.setLayer(selectedlayer);
							selectedlayer.setRenderer(sbr);
							Layerset layerset = map.getLayerset();
							layerset.addLayer(selectedlayer);
							// selectedlayer.setVisible(true);
							try {
								legend2 = toc.findLegend(selectedlayer);
							} catch (Exception e) {
							}

							CreateShapeDialog csd = new CreateShapeDialog(
									selectedlayer);
							csd.setVisible(true);
							Flash flash = new Flash(legend2);
							flash.start();
							map.redraw(); // necessary to see color immediately

						} else {
							JOptionPane.showMessageDialog(getContentPane(),
									"Please select Layer");
						}
					}
				}
			}
		};
		/******************** END OF FILE MENU LISTENER **********************************/

		/************************* CHART MENU LISTENER *****************************/
		chartListner = new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();
				if (source instanceof JMenuItem) {

					PMChart chart;
					if (source == pm10_chart) {
						chart = new PMChart("PM 10 Index", chartTitle,
								"csv/ph10.csv");
					} else {
						chart = new PMChart("PM 05 Index", chartTitle,
								"csv/ph05.csv");
					}
					chart.pack();
					chart.setResizable(true);
					RefineryUtilities.centerFrameOnScreen(chart);
					chart.setVisible(true);
				}
			}
		};

		/************************* END OF CHART MENU LISTENER *****************************/

		/***************** LANGUAGE LISTENER *****************************/
		languagelis = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Object source = ae.getSource();

				if (source instanceof JMenuItem) {

					if (source == english) {
						langFlag = true;
						names = ResourceBundle.getBundle("NamesBundle", loc2);
						java.util.List list = toc.getAllLegends();
						int count = list.size();
						for (int j = 0; j < count; j++) { // remove old layers
							com.esri.mo2.map.dpy.Layer dpylayer1 = (com.esri.mo2.map.dpy.Layer) ((Legend) list
									.get(j)).getLayer();
							map.getLayerset().removeLayer(dpylayer1);
						}
						addShapefileToMap(baselayer, s1);
						addShapefileToMap(pm10, s3);
						addShapefileToMap(pm05, s4);

					} else if (source == spanish) {
						langFlag = false;
						names = ResourceBundle.getBundle("NamesBundle", loc1);

						java.util.List list = toc.getAllLegends();
						int count = list.size();
						for (int j = 0; j < count; j++) { // remove old layers
							com.esri.mo2.map.dpy.Layer dpylayer1 = (com.esri.mo2.map.dpy.Layer) ((Legend) list
									.get(j)).getLayer();
							map.getLayerset().removeLayer(dpylayer1);
						}
						addShapefileToMap(baselayer, s1);
						addShapefileToMap(splayer, s5);
						addShapefileToMap(splayer2, s6);

					}
					translate();

					map.redraw();
					labelRenderer();
					pmvalueColorRenderer(splayer);
					pmvalueColorRenderer(splayer2);

				}
			}
		};

		/****************************** TOC ADAPTER *****************************/
		toc.setMap(map);
		mytocadapter = new TocAdapter() {
			public void click(TocEvent e) {
				System.out.println(activeLayerIndex + "dex");
				legend = e.getLegend();
				activeLayer = legend.getLayer();
				stb.setSelectedLayer(activeLayer);
				zptb.setSelectedLayer(activeLayer);
				// get acive layer index for promote and demote
				activeLayerIndex = map.getLayerset().indexOf(activeLayer);
				// layer indices are in order added, not toc order.
				System.out.println(activeLayerIndex + "active ndex");
				remlyritem.setEnabled(true);
				propsitem.setEnabled(true);
				openAtbItem.setEnabled(true);
				createlayeritem.setEnabled(true);
				enableDisableButtons();
			}
		};
		/************************* END OF TOC ADAPTER *****************************/

		/****************** GEODETIC CO-ORDINATES LISTENER ****************************/
		map.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent me) {
				com.esri.mo2.cs.geom.Point worldPoint = null;
				if (map.getLayerCount() > 0) {
					worldPoint = map.transformPixelToWorld(me.getX(), me.getY());
					String s = " X : " + df.format(worldPoint.getX()) + " "
							+ "Y : " + df.format(worldPoint.getY());
					statusLabel.setText(s);
				} else {
					statusLabel.setText("X:0.000 Y:0.000");
				}
			}
		});
		toc.addTocListener(mytocadapter);
		/*************** END OF GEODETIC CO-ORDINATES LISTENER ************************/

		// assume no layer initially selected
		remlyritem.setEnabled(false);
		propsitem.setEnabled(false);
		openAtbItem.setEnabled(false);
		promoteitem.setEnabled(false);
		demoteitem.setEnabled(false);
		promoteToTop.setEnabled(false);
		createlayeritem.setEnabled(false);

		// Custom tool bar action listener
		printitem.addActionListener(customTBListner);
		printButton.addActionListener(customTBListner);
		addLayerButton.addActionListener(customTBListner);
		pointerButton.addActionListener(customTBListner);
		distanceJB.addActionListener(customTBListner);
		XYjb.addActionListener(customTBListner);
		hotjb.addActionListener(customTBListner);
		helpToolbutton.addActionListener(customTBListner);
		// Layer Listener
		addlyritem.addActionListener(layerListener);
		remlyritem.addActionListener(layerListener);
		propsitem.addActionListener(layerListener);
		openAtbItem.addActionListener(layerListener);
		createlayeritem.addActionListener(layerListener);
		// Layer control listener
		promoteitem.addActionListener(layerControlListener);
		demoteitem.addActionListener(layerControlListener);
		promoteToTop.addActionListener(layerControlListener);
		// Pick Listener for hot link tool
		hotlink.addPickListener(picklis);
		// Help menu item listeners
		abtMojo.addActionListener(helpListener);
		contact.addActionListener(helpListener);
		about.addActionListener(helpListener);
		// Chart menu listeners
		pm05_chart.addActionListener(chartListner);
		pm10_chart.addActionListener(chartListner);
		// Language menu listeners
		english.addActionListener(languagelis);
		spanish.addActionListener(languagelis);

		lengedEditor.addActionListener(helpListener);
		layerControlHelp.addActionListener(helpListener);
		tbContents.addActionListener(helpListener);
		aboutmap.addActionListener(helpListener);

		addLayerButton.addMouseListener(mlLisAddLayer);
		XYjb.addMouseListener(xy);
		hotjb.addMouseListener(bolt);
		distanceJB.addMouseListener(distance);
		// Adding items to File menu
		file.setIcon(getScaledImage(new ImageIcon("images/menu.png"), 15, 15));
		language.setIcon(new ImageIcon("images/language.png"));
		file.add(addlyritem);
		file.add(remlyritem);
		file.addSeparator();
		file.add(printitem);
		file.addSeparator();
		file.add(propsitem);
		// Adding items to theme menu
		theme.setIcon(getScaledImage(new ImageIcon("images/theme.png"), 15, 15));
		theme.add(openAtbItem);
		theme.add(createlayeritem);
		// Adding items to Layer control menu
		layercontrol.setIcon(getScaledImage(new ImageIcon("images/layers.png"),
				15, 15));
		layercontrol.add(promoteitem);
		layercontrol.add(demoteitem);
		layercontrol.add(promoteToTop);

		// Adding items to help menu
		help.setIcon(getScaledImage(new ImageIcon("images/help.png"), 15, 15));

		help.add(helpTopics);
		help.add(about);
		help.add(contact);

		about.add(abtMojo);
		about.add(aboutmap);

		helpTopics.add(tbContents);
		helpTopics.add(layerControlHelp);
		helpTopics.add(lengedEditor);

		helpTool.setToolTipText("Tool Manual");
		// adding chart items
		charts.setIcon(getScaledImage(new ImageIcon("images/chartbar.png"), 16,
				15));
		charts.add(pm05_chart);
		charts.add(pm10_chart);

		// Adding language items
		language.add(english);
		language.add(spanish);

		// Adding menu(s) to Menu bar
		mbar.add(file);
		mbar.add(theme);
		mbar.add(layercontrol);
		mbar.add(help);
		mbar.add(charts);
		mbar.add(language);

		// Adding Tool tip texts for buttons.
		printButton.setToolTipText("Print map");
		addLayerButton.setToolTipText("Add layer");
		XYjb.setToolTipText("Add a layer of points from a file");
		pointerButton.setToolTipText("Pointer");
		distanceJB.setToolTipText("Press-drag-release to measure a distance");
		hotjb.setToolTipText("hotlink tool--click somthing to maybe see a picture");
		hotlink.setPickWidth(7);// sets tolerance for hotlink clicks
		helpToolbutton
				.setToolTipText("<html><b>Tool Manual</b><br>Left click here, then right click on<br> a tool to learn about that tool<br> click arrow tool when done</html>");

		// Adding tools to Custom tool bar
		customTB.add(printButton);
		customTB.add(addLayerButton);
		customTB.add(pointerButton);
		customTB.add(distanceJB);
		customTB.add(XYjb);
		customTB.add(hotjb);
		customTB.add(helpToolbutton);

		myjp.add(customTB);
		myjp.add(zptb);
		myjp.add(stb);
		myjp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// Panel at bottom of map ( coordinates and distance)
		myjp2.setBorder(BorderFactory.createLineBorder(Color.black));
		myjp2.add(statusLabel);
		myjp2.add(createVerticalSeparator());
		myjp2.add(milesLabel);
		myjp2.add(createVerticalSeparator());
		myjp2.add(kmLabel);

		getContentPane().add(toc, BorderLayout.WEST);
		splitPane.setLeftComponent(toc);
		getContentPane().add(map, BorderLayout.CENTER);
		splitPane.setRightComponent(map);
		splitPane.setOneTouchExpandable(true);

		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(myjp, BorderLayout.NORTH);
		getContentPane().add(myjp2, BorderLayout.SOUTH);
		// adding shape files to map
		addShapefileToMap(baselayer, s1);
		addShapefileToMap(pm10, s3);
		addShapefileToMap(pm05, s4);

		// for generating graduated color symbols

		// CityIdentifier.createCityList();

		java.util.List list = toc.getAllLegends();

		/*************** PRESET LABEL RENDERER ********************/

		com.esri.mo2.map.dpy.Layer lay1 = ((Legend) list.get(1)).getLayer();
		FeatureLayer flayer1 = (FeatureLayer) lay1;
		BaseSimpleLabelRenderer bslr1 = new BaseSimpleLabelRenderer();
		FeatureClass fclass1 = flayer1.getFeatureClass();
		String[] colnames = fclass1.getFields().getNames();
		System.out.println(colnames[2]); //
		// state name field
		Fields fields = fclass1.getFields();
		// capture state_name field
		Field field = fields.getField(2);
		System.out.println(field.getName());
		bslr1.setLabelField(field); //
		// make state_name the label field
		flayer1.setLabelRenderer(bslr1);
		/******** END OF PRESET LABEL RENDERER ********************/

		/**** SETTING TRANSPARENCY FOR BASE LAYER **************/
		FeatureLayer flayer = (FeatureLayer) ((Legend) list.get(2)).getLayer();
		BaseSimpleRenderer bsr = (BaseSimpleRenderer) flayer.getRenderer();
		Symbol sym = bsr.getSymbol(); // returns class Symbol
		SimplePolygonSymbol sps = (SimplePolygonSymbol) sym;
		sps.setPaint(AoFillStyle.getPaint(Color.GRAY));
		sps.setFillTransparency(0.5);
		bsr.setSymbol(sps);

		// To classify PM values into Low, Moderate and High concentrations
		pmvalueColorRenderer(pm10);
		pmvalueColorRenderer(pm05);

		// To set background color for TOC
		toc.setBackground(Color.LIGHT_GRAY);
		Legend pm05 = (Legend) list.get(0);
		Legend pm10 = (Legend) list.get(1);
		Legend ind = (Legend) list.get(2);

		ind.setSelectionBackgroundColor(Color.LIGHT_GRAY);
		pm05.toggleSelected();
		pm05.setSelectionBackgroundColor(new Color(250, 250, 250));
		pm10.setSelectionBackgroundColor(new Color(255, 255, 255));

	}

	/* Creates vertical separator between J Labels */
	static JComponent createVerticalSeparator() {
		JSeparator x = new JSeparator(SwingConstants.VERTICAL);
		x.setPreferredSize(new Dimension(1, 20));
		return x;
	}

	/**
	 * This function divides the PM values into quantiles and renders
	 * corresponding color
	 * 
	 * @param layer
	 */
	@SuppressWarnings("rawtypes")
	private void pmvalueColorRenderer(Layer layer) {
		com.esri.mo2.map.dpy.Layer dplayer = map.getLayer(layer.getName());

		FeatureLayer flayer = (BaseFeatureLayer) dplayer;
		Fields fields = flayer.getFeatureClass().getFields();

		int i;
		int breaks10[] = { 70, 156, 243, 330 };
		int breaks05[] = { 38, 82, 126, 170 };
		int breaks[];
		if (layer.getName().equals("PM10EN")
				|| layer.getName().equals("PM10MX")) {
			i = fields.findField("PM10");
			breaks = breaks10;
		} else {
			i = fields.findField("PM05");
			breaks = breaks05;
		}

		Field f = fields.getField(i);
		BaseClassBreaksRenderer cbr = new BaseClassBreaksRenderer();
		cbr.setField(f);
		int numClasses = 3;
		Comparable low, high = null;

		for (int j = 0; j < numClasses; j++) {
			low = (Comparable) f.parse(String.valueOf(breaks[j]));
			high = (Comparable) f.parse(String.valueOf(breaks[j + 1]));
			System.out.println("low" + low);
			System.out.println("high" + high);
			BaseRange b = new BaseRange(low, high);
			SimpleMarkerSymbol sms = new SimpleMarkerSymbol();

			int r = (102 * (numClasses - j - 1));
			sms.setSymbolColor(new Color(255, r, 0));

			if (layer.getName().equals("PM10EN")
					|| layer.getName().equals("PM10MX")) {
				sms.setType(0);
				sms.setWidth(16);
				sms.setTransparency(0.8);
				sms.setOverlap(true);

			} else {
				sms.setType(0);
				sms.setWidth(12);
				sms.setTransparency(0.8);
				sms.setOverlap(true);
			}
			cbr.addBreak(sms, b);

		}
		flayer.setRenderer(cbr);
		map.redraw();
		toc.refresh();

	}

	/**
	 * This method adds shape files to the map
	 * 
	 * @param layer
	 * @param s
	 */
	private void addShapefileToMap(Layer layer, String s) {
		String datapath = s; // "C:\\ESRI\\MOJ20\\Samples\\Data\\USA\\States.shp";
		layer.setDataset("0;" + datapath);
		map.add(layer);
	}

	public static void main(String[] args) {
		IndiaMap qstart = new IndiaMap();
		qstart.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Thanks, Quick Start exits");
				System.exit(0);
			}
		});
		qstart.setVisible(true);
		env = map.getExtent();
	}

	/**
	 * This method is used to enable and disable the layer control menu items
	 */
	@SuppressWarnings("deprecation")
	private void enableDisableButtons() {
		int layerCount = map.getLayerset().getSize();
		if ((layerCount < 2) || activeLayerIndex == -1) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(false);
			promoteToTop.setEnabled(false);
		} else if (activeLayerIndex == 0) {
			demoteitem.setEnabled(false);
			promoteitem.setEnabled(true);
			promoteToTop.setEnabled(true);
		} else if (activeLayerIndex == layerCount - 1) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(true);
			promoteToTop.setEnabled(false);
		} else if (activeLayerIndex == -2) {
			promoteitem.setEnabled(false);
			promoteToTop.setEnabled(false);
			demoteitem.setEnabled(true);
			activeLayerIndex = layerCount - 1;
		} else {
			promoteitem.setEnabled(true);
			demoteitem.setEnabled(true);
			promoteToTop.setEnabled(true);
		}
	}

	/**
	 * This method is used to scale the image icons
	 * 
	 * @param srcImg
	 * @param w
	 * @param h
	 * @return
	 */
	public static ImageIcon getScaledImage(ImageIcon srcImg, int w, int h) {
		Image image = srcImg.getImage();
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, w, h, null);
		g2.dispose();

		return new ImageIcon(resizedImg);
	}

	private void labelRenderer() {
		/*************** PRESET LABEL RENDERER ********************/
		@SuppressWarnings("rawtypes")
		java.util.List list = toc.getAllLegends();
		com.esri.mo2.map.dpy.Layer lay1 = ((Legend) list.get(1)).getLayer();
		FeatureLayer flayer1 = (FeatureLayer) lay1;
		BaseSimpleLabelRenderer bslr1 = new BaseSimpleLabelRenderer();
		FeatureClass fclass1 = flayer1.getFeatureClass();
		String[] colnames = fclass1.getFields().getNames();
		System.out.println(colnames[2]); //
		// state name field
		Fields fields = fclass1.getFields();
		// capture state_name field
		Field field = fields.getField(2);
		System.out.println(field.getName());
		bslr1.setLabelField(field); //
		// make state_name the label field
		flayer1.setLabelRenderer(bslr1);
		/******** END OF PRESET LABEL RENDERER ********************/
	}

	private void translate() { // (3)

		System.out.println("INSIDE TRANSLATE");
		file.setText(names.getString("File"));
		addlyritem.setText(names.getString("AddLayer"));
		remlyritem.setText(names.getString("RemoveLayer"));
		printitem.setText(names.getString("Print"));
		propsitem.setText(names.getString("LegendEditor"));

		lengedEditor.setText(names.getString("LegendEditor"));
		layerControlHelp.setText(names.getString("LayerControl"));
		tbContents.setText(names.getString("TableOfContents"));

		theme.setText(names.getString("Theme"));
		openAtbItem.setText(names.getString("OpenAttributeTable"));
		createlayeritem.setText(names.getString("CreateLayerfromSelection"));

		layercontrol.setText(names.getString("LayerControl"));
		promoteitem.setText(names.getString("PromoteLayer"));
		demoteitem.setText(names.getString("DemoteLayer"));
		promoteToTop.setText(names.getString("PromoteToTop"));

		help.setText(names.getString("Help"));
		about.setText(names.getString("About"));
		contact.setText(names.getString("Contact"));
		aboutmap.setText(names.getString("AirPollution"));
		abtMojo.setText(names.getString("AboutMojo"));
		helpTopics.setText(names.getString("HelpTopics"));

		charts.setText(names.getString("Charts"));

		printButton.setToolTipText(names.getString("Print"));
		addLayerButton.setToolTipText(names.getString("AddLayer"));
		pointerButton.setToolTipText(names.getString("Pointer"));
		distanceJB.setToolTipText(names.getString("DistanceText"));
		XYjb.setToolTipText(names.getString("AddXY"));
		milesLabel.setText(names.getString("DISTANCE"));
		kmLabel.setText(names.getString("km"));
		hotjb.setToolTipText(names.getString("HotLinkTool"));

		prvExtent.setToolTipText(names.getString("PreviousExtent"));
		nxtExtent.setToolTipText(names.getString("NextExtent"));
		zoomToSelectedLayerButton.setToolTipText(names
				.getString("ZoomToFullExtent"));
		zoomFullExtentButton
				.setToolTipText(names.getString("ZoomToFullExtent"));
		zoomInButton.setToolTipText(names.getString("ZoomIn"));
		zoomOut.setToolTipText(names.getString("Zoomout"));
		pan.setToolTipText(names.getString("PreviousExtent"));
		panoneDirection.setToolTipText(names.getString("PanOneDirection"));
		identifyTool.setToolTipText(names.getString("Identify"));

		searchTool.setToolTipText(names.getString("Search"));
		findTool.setToolTipText(names.getString("Find"));
		queryBuilder.setToolTipText(names.getString("QueryBuilder"));
		clearAllSelection.setToolTipText(names.getString("ClearAllSelection"));
		selectFeatures.setToolTipText(names.getString("SelectFeatures"));
		attributes.setToolTipText(names.getString("Attributes"));
		bufferTool.setToolTipText(names.getString("Buffer"));

		english.setText(names.getString("English"));
		spanish.setText(names.getString("Spanish"));
		language.setText(names.getString("SelectLanguage"));
		helpToolbutton.setToolTipText(names.getString("ToolManual"));

		chartTitle = names.getString("ChartTitle");
		this.setTitle(names.getString("AirPollutionIndexofIndia"));

	}

}

@SuppressWarnings("serial")
class HelpTool extends Tool {
}
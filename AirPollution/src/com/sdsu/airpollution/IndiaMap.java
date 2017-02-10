package com.sdsu.airpollution;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import com.esri.mo2.ui.bean.*; // beans used: Map,Layer,Toc,TocAdapter,Tool
// TocEvent,Legend(a legend is part of a toc),ActateLayer
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;
import javax.swing.border.BevelBorder;
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

	Legend legend;
	Legend legend2;

	Layer baselayer = new Layer();
	Layer layer3 = null;
	Layer pm10 = new Layer();
	Layer pm05 = new Layer();
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
	JMenuItem startHelp = new JMenuItem("Getting Started");

	JMenu helpTopics = new JMenu("Help topics");
	JMenuItem tbContents = new JMenuItem("Table of contents");
	JMenuItem lengedEditor = new JMenuItem("Legend editor");
	JMenuItem layerControlHelp = new JMenuItem("Layer Control");

	Toc toc = new Toc();

	// Path of shape files
	String s1 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\INDIA.shp";
	String s3 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\pm10.shp";
	String s4 = "C:\\ESRI\\MOJ20\\Samples\\Data\\India\\pm05.shp";
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

	JButton printButton = new JButton(new ImageIcon("images/print.gif"));
	JButton addLayerButton = new JButton(new ImageIcon("images/addtheme.gif"));
	JButton pointerButton = new JButton(new ImageIcon("images/pointer.gif"));
	JButton distanceJB = new JButton(new ImageIcon("images/measure_1.gif"));
	JButton XYjb = new JButton("XY");
	JButton hotjb = new JButton(new ImageIcon("images/hotlink.gif"));
	// Arrow arrow = new Arrow();
	// DistanceTool distanceTool= new DistanceTool();

	ActionListener customTBListner;
	ActionListener layerListener;
	ActionListener layerControlListener;
	ActionListener helpListener;
	TocAdapter mytocadapter;

	static Envelope env;

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
					String url = "file:///E:/CommonWorkSpace/GIS/AirPollution/html/"
							+ mystate + ".html";
					System.out.println(url);
					// Desktop.getDesktop().browse(new URI(url));
					HotP hotpick = new HotP(url, mystate);
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
		this.setBounds(100, 100, 800, 600);

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
				map.setExtent(env);
				map.zoom(1.0);
				map.redraw();
			}
		};
		// next line gets ahold of a reference to the zoom in button
		JButton zoomInButton = (JButton) zptb.getActionComponent("ZoomIn");
		JButton zoomFullExtentButton = (JButton) zptb
				.getActionComponent("ZoomToFullExtent");
		JButton zoomToSelectedLayerButton = (JButton) zptb
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
				String source = ae.getActionCommand();
				System.out.println(activeLayerIndex + " active index");
				if (source == "Promote selected layer") {
					map.getLayerset().moveLayer(activeLayerIndex,
							++activeLayerIndex);
				} else if (source == "Promote to top") {
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
					String arg = ae.getActionCommand();
					if (arg == "About MOJO...") {
						AboutBox aboutBox = new AboutBox();
						aboutBox.setProductName("MOJO");
						aboutBox.setProductVersion("2.0");
						aboutBox.setVisible(true);
					} else if (arg == "Table of contents") {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/toc.html");
						hpdg.setVisible(true);
					} else if (arg == "Layer Control") {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/layerchelp.html");
						hpdg.setVisible(true);
					} else if (arg == "Legend editor") {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/legendhelp.html");
						hpdg.setVisible(true);
					} else if (arg == "Air Pollution Map") {
						Help hpdg = new Help(
								"file:///E:/CommonWorkSpace/GIS/AirPollution/html/aboutMap.html");
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
					String arg = ae.getActionCommand();
					if (arg == "add layer") {
						try {
							AddLayerDialog aldlg = new AddLayerDialog();
							aldlg.setMap(map);
							aldlg.setVisible(true);
						} catch (IOException e) {
						}
					} else if (arg == "remove layer") {
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
					} else if (arg == "Legend Editor") {
						LayerProperties lp = new LayerProperties();
						lp.setLegend(legend);
						lp.setSelectedTabIndex(0);
						lp.setVisible(true);
					} else if (arg == "Open attribute table") {
						try {
							layer4 = legend.getLayer();
							AttributeTable attrtab = new AttributeTable();
							attrtab.setVisible(true);
						} catch (IOException ioe) {
						}
					} else if (arg == "Create layer from selection") {
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
								rms.setImageString("E:/CommonWorkSpace/GIS/GISLab2/images/bulleye.png");
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
		startHelp.addActionListener(helpListener);
		lengedEditor.addActionListener(helpListener);
		layerControlHelp.addActionListener(helpListener);
		tbContents.addActionListener(helpListener);
		aboutmap.addActionListener(helpListener);
		// Adding items to File menu
		file.setIcon(getScaledImage(new ImageIcon("images/menu.png"), 15, 15));
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
		help.add(startHelp);
		help.add(helpTopics);
		help.add(about);
		help.add(contact);

		about.add(abtMojo);
		about.add(aboutmap);

		helpTopics.add(tbContents);
		helpTopics.add(layerControlHelp);
		helpTopics.add(lengedEditor);
		// Adding menu(s) to Menu bar
		mbar.add(file);
		mbar.add(theme);
		mbar.add(layercontrol);
		mbar.add(help);

		// Adding Tool tip texts for buttons
		printButton.setToolTipText("Print map");
		addLayerButton.setToolTipText("Add layer");
		XYjb.setToolTipText("Add a layer of points from a file");
		printButton.setToolTipText("Pointer");
		distanceJB.setToolTipText("Press-drag-release to measure a distance");
		hotjb.setToolTipText("hotlink tool--click somthing to maybe see a picture");
		hotlink.setPickWidth(7);// sets tolerance for hotlink clicks

		// Adding tools to Custom tool bar
		customTB.add(printButton);
		customTB.add(addLayerButton);
		customTB.add(pointerButton);
		customTB.add(distanceJB);
		customTB.add(XYjb);
		customTB.add(hotjb);

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
		sps.setPaint(AoFillStyle.getPaint(Color.LIGHT_GRAY));
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
	 * This function divides the PM values using Quantile method and renders
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
		if (layer.getName().equals("pm10")) {
			i = fields.findField("PM_10");
			breaks = breaks10;
		} else {
			i = fields.findField("PM_05");
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
			if (layer.getName().equals("pm10")) {
				sms.setType(0);
				sms.setWidth(14);
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
}
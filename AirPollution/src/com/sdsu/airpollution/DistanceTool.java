package com.sdsu.airpollution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import com.esri.mo2.ui.bean.AcetateLayer;
import com.esri.mo2.ui.bean.DragTool;

class DistanceTool extends DragTool  {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 8770659955924151576L;
	int startx,starty,endx,endy,currx,curry;
	  com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
	  double distance;
	  public void mousePressed(MouseEvent me) {
	        startx = me.getX(); starty = me.getY();
	        initPoint = IndiaMap.map.transformPixelToWorld(me.getX(),me.getY());
	  }
	  public void mouseReleased(MouseEvent me) {
	          // now we create an acetatelayer instance and draw a line on it
	        endx = me.getX(); endy = me.getY();
	        endPoint = IndiaMap.map.transformPixelToWorld(me.getX(),me.getY());
	    distance = (69.44 / (2*Math.PI)) * 360 * Math.acos(
	                                 Math.sin(initPoint.y * 2 * Math.PI / 360)
	                           * Math.sin(endPoint.y * 2 * Math.PI / 360)
	                           + Math.cos(initPoint.y * 2 * Math.PI / 360)
	                           * Math.cos(endPoint.y * 2 * Math.PI / 360)
	                           * (Math.abs(initPoint.x - endPoint.x) < 180 ?
	                    Math.cos((initPoint.x - endPoint.x)*2*Math.PI/360):
	                    Math.cos((360 - Math.abs(initPoint.x -
	endPoint.x))*2*Math.PI/360)));
	    System.out.println( distance  );
	    IndiaMap.milesLabel.setText("DISTANCE : " + new
	Float((float)distance).toString() + " miles  ");
	    IndiaMap.kmLabel.setText(new Float((float)(distance*1.6093)).toString() + " km ");
	    if (IndiaMap.acetLayer != null)
	      IndiaMap.map.remove(IndiaMap.acetLayer);
	    IndiaMap.acetLayer = new AcetateLayer() {
	      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		public void paintComponent(java.awt.Graphics g) {
	                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
	                Line2D.Double line = new Line2D.Double(startx,starty,endx,endy);
	                g2d.setColor(new Color(0,0,250));
	                g2d.draw(line);
	      }
	    };
	    @SuppressWarnings("unused")
		Graphics g = super.getGraphics();
	    IndiaMap.map.add(IndiaMap.acetLayer);
	    IndiaMap.map.redraw();
	  }
	  public void cancel() {};
	}
package com.sdsu.airpollution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.esri.mo2.cs.geom.Point;

public class CityIdentifier {

	private static HapMap<String,Double> chartInput = new HashMap<String,Double>();

	public static void createCityList() {
		try {
			File file = new File("E:/MS_CS/GIS/PROJECT/ph10.csv");
			FileReader fred = new FileReader(file);
			BufferedReader in = new BufferedReader(fred);
			String s; // = in.readLine();
			double x, y;
			String city;
			cityList = new ArrayList<City>();
			while ((s = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				city = st.nextToken();
				Point location = new Point(x, y);
				cityList.add(new City(city, location));
			}
			Iterator<City> itr = cityList.iterator();
			while(itr.hasNext())
			{
				City v = itr.next();
				System.out.println("Name : "+v.getCityName());
				System.out.println("Lat lng:"+v.getLatlng());
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Error :" + e);
		}
	}

	public static String getCityName(Point latlng) {
		String city = "";
		Iterator<City> itr = cityList.iterator();
		Double x,y,lat,lng;
		while (itr.hasNext()) {
			City c = itr.next();
			x= c.getLatlng().getX();
			y = c.getLatlng().getY();
			lat = latlng.getX();
			lng = latlng.getY();
			System.out.println("X n lat"+x.intValue()+" "+ lat.intValue());
			if (x.intValue()==lat.intValue() && y.intValue()==lng.intValue()) {
				city = c.getCityName();
			} else {
				city = "";
			}
		}
		return city;
	}
}

class City {
	private String cityName;
	private Point latlng;

	public City(String cityName, Point location) {
		this.cityName = cityName;
		this.latlng = location;

	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Point getLatlng() {
		return latlng;
	}

	public void setLatlng(Point ltlng) {
		this.latlng = ltlng;
	}

}
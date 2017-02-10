package com.sdsu.airpollution;

import java.util.StringTokenizer;

public class Demo {

	public static void main(String[] args) {
		String data = "72.2541, 20.3465, Arizona";
		StringTokenizer st = new StringTokenizer(data, ",");
		double x = Double.parseDouble(st.nextToken());
		double y = Double.parseDouble(st.nextToken());
		String name = st.nextToken().toString();
		System.out.println("X :" + x);
		System.out.println("Y :" + y);
		System.out.println("Name:"+name);
	}
}
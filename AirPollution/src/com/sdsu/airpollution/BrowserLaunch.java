package com.sdsu.airpollution;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserLaunch {

	public static void launchBrowser(String url)
	{
	Desktop dp = Desktop.getDesktop();
	try {
		dp.browse(new URI(url));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
}

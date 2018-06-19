package com.paysafe.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
/**
 * Contains a bunch of helper methods
 * @author eanuama
 *
 */
public class ServiceHelper {
	private final static Logger log = Logger.getLogger(ServiceHelper.class.getName());

	private static ServiceHelper instance = null;

	private ServiceHelper() {

	}

	public static ServiceHelper getInstance() {
		if (instance == null) {
			instance = new ServiceHelper();
		}
		return instance;
	}

	/**
	 * Checks the valid format of the URL
	 * @param inputURL
	 * @return
	 */
	public boolean isValidURL(String inputURL) {
		try {
			URL url = new URL(inputURL);
		} catch (MalformedURLException e) {
			log.warning("Invalid URL format " + inputURL);
			return false;
		}
		return true;
	}
}

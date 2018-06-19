package com.paysafe.beans;

/**
 * StartRequest class is used when we start monitoring a server.
 * 
 * @author eanuama
 *
 */
public class StartRequest {
	// interval is time duration between requests to the server.
	// It is in milliseconds.
	private long interval;
	// url is the address of the server that needs to be monitored
	private String url;

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

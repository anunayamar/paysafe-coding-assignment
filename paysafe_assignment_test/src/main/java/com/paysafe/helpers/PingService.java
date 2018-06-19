package com.paysafe.helpers;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PingService implements Runnable {
	private long interval;
	private String url;
	private Map<String, Date> serverLastUpMap = new HashMap<String, Date>();
	private Map<String, Date> serverLastDownMap = new HashMap<String, Date>();

	public PingService(long interval, String url) {
		this.interval = interval;
		this.url = url;
	}

	public PingService(long interval, String url, Map<String, Date> serverLastUpMap,
			Map<String, Date> serverLastDownMap) {
		this.interval = interval;
		this.url = url;
		this.serverLastUpMap = serverLastUpMap;
		this.serverLastDownMap = serverLastDownMap;

	}

	public void run() {
		RestTemplate restTemplate = new RestTemplate();
		HttpStatus statusCode = null;
		Date serverResponseTime = null;

		ResponseEntity<String> response = null;
		/**Monitoring threads are run indefinitely. They are only stopped when stop request is received for the
		 * server url 
		 */
		while (true) {
			try {
				response = restTemplate.getForEntity(url, String.class);

				HttpHeaders headers = response.getHeaders();
				serverResponseTime = new Date(headers.getDate());
				statusCode = response.getStatusCode();

			} catch (Exception e) {
				if(e.getCause() != null && e.getCause() instanceof UnknownHostException) {
					//Unknown host exception says that server is not reachable i.e. server is down
					System.out.println("Unknown host exception");
					serverResponseTime = new Date();
					statusCode = null;
				}
			}
			// Non null status code signifies that server responds
			//Adding the server and last up time
			if (statusCode !=null) {
				serverLastUpMap.put(url, serverResponseTime);				
			} else {
				//Adding the server and last down time
				serverLastDownMap.put(url, serverResponseTime);				
			}

			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				System.out.println("Monitoring stopped for server " + url);
				break;
			}
		}

	}

}

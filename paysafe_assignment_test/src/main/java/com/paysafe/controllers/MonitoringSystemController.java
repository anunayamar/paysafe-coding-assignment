package com.paysafe.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paysafe.beans.ServerReport;
import com.paysafe.beans.StartRequest;
import com.paysafe.errorhandlers.ApiError;
import com.paysafe.helpers.PingService;
import com.paysafe.helpers.ServiceHelper;

@RestController
public class MonitoringSystemController {
	private final static Logger log = Logger.getLogger(MonitoringSystemController.class.getName());
	private ServiceHelper helper = ServiceHelper.getInstance();

	// Stores the threads responsible for every server
	private Map<String, Thread> serviceMap = new HashMap<>();
	// Stores the last time the server was up for every server
	private Map<String, Date> serverLastUpMap = new HashMap<>();
	// Stores the last time the server was down for every server
	private Map<String, Date> serverLastDownMap = new HashMap<>();

	/**
	 * This startService should be called to start the monitoring of the given
	 * server. Two parameters can be passed with the help of startRequest. 1)
	 * Interval in milliseconds2) Server URL
	 * 
	 * @param startRequest
	 * @return
	 */
	@RequestMapping(value = "/start-service", method = RequestMethod.POST, consumes = "application/JSON", produces = "application/json")
	public ResponseEntity<?> startService(@RequestBody StartRequest startRequest) {
		String url = startRequest.getUrl();
		long interval = startRequest.getInterval();
		log.info("In start-service for server:" + url + " and interval:" + interval);

		// Checking whether the URL format is valid and interval is greater than zero
		if ((helper.isValidURL(url) && interval > 0)) {
			Thread pingService = serviceMap.get(url);

			if (pingService == null || !pingService.isAlive()) {
				pingService = new Thread(new PingService(interval, url, serverLastUpMap, serverLastDownMap));
				pingService.start();
				serviceMap.put(url, pingService);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Server is already under monitoring");
			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Malformed server url or negative interval");
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * To stop the monitoring of specific server call stopService endpoint
	 * 
	 * @param url
	 *            : The url represents the server whose monitoring should be stopped
	 * @return
	 */
	@RequestMapping(value = "/stop-service", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> stopService(@RequestParam(value = "url") String url) {
		log.info("In stop-service for server:" + url);
		if (helper.isValidURL(url)) {
			Thread pingService = serviceMap.get(url);
			if (pingService != null && pingService.isAlive()) {
				pingService.interrupt();
				return new ResponseEntity<>(HttpStatus.OK);
			}
			log.warning("Monitoring service already stopped for the server or the server"
					+ " monitoring was never started");

			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					"Server was never monitored or server monitoring was already stopped");
			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Malformed server url");
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/**
	 * getServerReport returns you the server-report like when the server was last
	 * up and when it was last down. To use this service pass URL of the server
	 * whose server report you would like to have.
	 * 
	 * @param url:
	 *            The url represents the server whose monitoring should be stopped
	 * @return
	 */
	@RequestMapping(value = "/server-report", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getServerReport(@RequestParam(value = "url") String url) {
		log.info("In server-report for server:" + url);
		if (helper.isValidURL(url)) {
			Thread pingService = serviceMap.get(url);
			if (pingService != null) {
				ServerReport serverReport = new ServerReport();
				serverReport.setLastUp(serverLastUpMap.get(url));
				serverReport.setLastDown(serverLastDownMap.get(url));
				return new ResponseEntity<>(serverReport, HttpStatus.OK);
			}
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Server was never monitored");
			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		}
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Malformed server url");
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}
}

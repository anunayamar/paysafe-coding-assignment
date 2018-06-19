package com.paysafe.beans;

import java.util.Date;

public class ServerReport {
	private Date lastUp;
	private Date lastDown;

	public Date getLastUp() {
		return lastUp;
	}

	public void setLastUp(Date lastUp) {
		this.lastUp = lastUp;
	}

	public Date getLastDown() {
		return lastDown;
	}

	public void setLastDown(Date lastDown) {
		this.lastDown = lastDown;
	}

}

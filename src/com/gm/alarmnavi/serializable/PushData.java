package com.gm.alarmnavi.serializable;

import java.io.*;
import java.util.*;

public class PushData implements Serializable {

	private static final long serialVersionUID = -2948510249995214657L;

	private boolean signal;
	private List<String> locationInRoute;
	private double[] locations;

	public PushData(boolean signal) {
		this.signal = signal;
	}

	public void setGroupAppID(List<String> locationInRoute) {
		this.locationInRoute = locationInRoute;
	}

	public void setLocation(double[] locations) {
		this.locations = locations;
	}

	public boolean getSignal() {
		return signal;
	}

	public List<String> getAppIds() {
		return locationInRoute;
	}

	public double[] getLocation() {
		return locations;
	}
}

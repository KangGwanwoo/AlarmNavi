package com.gm.alarmnavi.serializable;

import java.io.Serializable;
import java.util.List;

public class FireData implements Serializable {

	private static final long serialVersionUID = -6387800686287499495L;

	private String distacne, duration;
	private List<double[]> routePointList;

	public FireData(List<double[]> routePointList, String distance,
			String duration) {
		this.distacne = distance;
		this.duration = duration;
		this.routePointList = routePointList;
	}

	public String getDistacne() {
		return distacne;
	}

	public String getDuration() {
		return duration;
	}

	public List<double[]> getRoutePointList() {
		return routePointList;
	}

	public double[] getFirePoint() {
		return routePointList.get(routePointList.size() - 1);

	}
}

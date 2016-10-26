package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

public class ScreenText implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860317159053884981L;

	private String screen;

	private List<KeyValue> labels;

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public List<KeyValue> getLabels() {
		return labels;
	}

	public void setLabels(List<KeyValue> labels) {
		this.labels = labels;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

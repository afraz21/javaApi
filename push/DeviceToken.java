package org.iqvis.nvolv3.push;

import java.io.Serializable;
import java.util.List;

public class DeviceToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> $in;

	public List<String> get$in() {
		return $in;
	}

	public void set$in(List<String> $in) {
		this.$in = $in;
	}

}

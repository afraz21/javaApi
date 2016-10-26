package org.iqvis.nvolv3.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class ActivityTimeGroup {
	
	@NotEmpty
	private Integer time_group;

	public Integer getTime_group() {
		return time_group;
	}

	public void setTime_group(Integer time_group) {
		this.time_group = time_group;
	}

}

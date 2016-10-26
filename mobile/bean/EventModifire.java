package org.iqvis.nvolv3.mobile.bean;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.iqvis.nvolv3.domain.EventConfiguration;

public abstract class EventModifire {

	@JsonIgnore(false)
	abstract EventConfiguration getEventConfiguration();
}

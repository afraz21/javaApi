package org.iqvis.nvolv3.bean;

import java.io.Serializable;

import org.iqvis.nvolv3.domain.Track;

public class EventTrackResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Track track;

	private EventTrack eventTrack;

	public EventTrack getEventTrack() {
		return eventTrack;
	}

	public void setEventTrack(EventTrack eventTrack) {
		this.eventTrack = eventTrack;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

}

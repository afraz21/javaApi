package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.MultiLingualAddresses;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.utils.Utils;

public class EventVenue implements Serializable {

	/**
	 * 
	 */
	protected static Logger logger = Logger.getLogger("EventVenue");

	private static final long serialVersionUID = -7549396691789791705L;

	private String id;

	private String name;

	// private List<Location> location=null;
	private Double latitude;

	private Double longitude;

	private String city;

	private String state;

	private String countryCode;

	private String stateCode;

	public EventVenue() {

	}

	public EventVenue(Venue ven, String code, String eventDefault) {

		if (ven != null) {

			logger.debug(ven.getLocations());

			this.id = ven.getId();

			if (ven.getLongitude() == null && ven.getLatitude() == null && ven.getLocations() != null) {

				for (Location loc : ven.getLocations()) {

					if (loc.getLatitude() != null && loc.getLongitude() != null) {

						this.latitude = (Double) loc.getLatitude();

						this.longitude = (Double) loc.getLongitude();

						if (loc.getLatitude() > 0 && loc.getLongitude() > 0)

							break;
					}
				}
			}

			else {
				this.latitude = ven.getLatitude();
				this.longitude = ven.getLongitude();
			}

			if (ven.getMultiLingual() != null) {
				try {

					List<MultiLingualAddresses> list = Utils.getMultiLingualAddressesByLangCode(ven.getMultiLingual(), code, eventDefault);

					if (list != null) {

						this.name = ven.getName();

						if (list.size() > 0) {

							this.city = list.get(0).getCity();

							this.state = list.get(0).getState();

							this.countryCode = list.get(0).getCountryCode();

							this.stateCode = list.get(0).getCountryCode();
						}
					}

				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * public List<Location> getLocation() { return location; }
	 * 
	 * public void setLocation(List<Location> location) { this.location =
	 * location; }
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((EventVenue) obj).getId());
	}

}

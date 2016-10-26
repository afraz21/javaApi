package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.domain.Personnel;

/**
 * A simple POJO representing a EventTrack
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
public class MobilePersonnels implements Serializable {

	private static final long serialVersionUID = -1527566248002296042L;

	@NotEmpty(message = "Personnels cannot be empty.")
	private List<Personnel> personnels;

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

}

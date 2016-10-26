package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.domain.Personnel;

/**
 * A simple POJO representing a EventTrack
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
public class ActivityPersonnels extends Audit implements Serializable {

	private static final long serialVersionUID = -1527566248002296042L;

	@NotEmpty(message = "Type id cannot be empty.")
	private Data type;

	@NotEmpty(message = "Personnels cannot be empty.")
	private List<Personnel> personnels;

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public Data getType() {
		return type;
	}

	public void setType(Data type) {
		this.type = type;
	}

}

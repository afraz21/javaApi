package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;

/**
 * A simple POJO representing a EventTrack
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
public class ActivityPersonnels extends Audit implements Serializable {

	private static final long serialVersionUID = -1527566248002296042L;

	@NotEmpty(message = "Type id cannot be empty.")
	private String typeId;

	@NotEmpty(message = "Personnels cannot be empty.")
	private List<String> personnels;
	
	private String personnelType;

	public String getPersonnelType() {
		return personnelType;
	}

	public void setPersonnelType(String personnelType) {
		this.personnelType = personnelType;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<String> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<String> personnels) {
		this.personnels = personnels;
	}

}

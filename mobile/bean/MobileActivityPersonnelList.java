package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.domain.Activity;

public class MobileActivityPersonnelList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String typeId;

	private List<String> personnels = new ArrayList<String>();

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<String> getPersonnels() {
		return personnels;
	}

	public void addPersonnelId(String id) {
		personnels.add(id);
	}

	public void setPersonnels(List<String> personnels) {
		this.personnels = personnels;
	}

	public static List<MobileActivityPersonnelList> getPersonnels(Activity activity) {
		List<MobileActivityPersonnelList> tempList = new ArrayList<MobileActivityPersonnelList>();
		if (activity.getPersonnels() != null) {
			for (ActivityPersonnels pers : activity.getPersonnels()) {

				MobileActivityPersonnelList temp = new MobileActivityPersonnelList();

				temp.setTypeId(pers.getTypeId());
				temp.setPersonnels(pers.getPersonnels());
				tempList.add(temp);
			}
		}
		return tempList;
	}

}

package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.domain.Personnel;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */
public class EventSpeakers implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	private String name;

	private String id;

	List<Personnel> personnels;

	List<Personnel> keyNotePersonnels;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public List<Personnel> getKeyNotePersonnels() {
		return keyNotePersonnels;
	}

	public void setKeyNotePersonnels(List<Personnel> keyNotePersonnels) {
		this.keyNotePersonnels = keyNotePersonnels;
	}

}

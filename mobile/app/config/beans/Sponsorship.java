package org.iqvis.nvolv3.mobile.app.config.beans;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.Data;

/**
 * A simple POJO representing a ReferenceData
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class Sponsorship implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	private Boolean showSignUpScreen = false;

	private Boolean signUpManadatory = false;

	private List<Data> labels = null;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getShowSignUpScreen() {
		return showSignUpScreen;
	}

	public void setShowSignUpScreen(Boolean showSignUpScreen) {
		this.showSignUpScreen = showSignUpScreen;
	}

	public Boolean getSignUpManadatory() {
		return signUpManadatory;
	}

	public void setSignUpManadatory(Boolean signUpManadatory) {
		this.signUpManadatory = signUpManadatory;
	}

	public List<Data> getLabels() {
		return labels;
	}

	public void setLabels(List<Data> labels) {
		this.labels = labels;
	}

}

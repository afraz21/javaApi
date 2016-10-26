package org.iqvis.nvolv3.mobile.app.config.beans;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.mobile.bean.DataCMS;

/**
 * A simple POJO representing a ReferenceData
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class SignUp implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	private Boolean showSignUpScreen = false;

	private Boolean signUpManadatory = false;

	private List<DataCMS> labels = null;

	private List<DataCMS> texts = null;

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

	public List<DataCMS> getLabels() {
		return labels;
	}

	public void setLabels(List<DataCMS> labels) {
		this.labels = labels;
	}

	public List<DataCMS> getTexts() {
		return texts;
	}

	public void setTexts(List<DataCMS> texts) {
		this.texts = texts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

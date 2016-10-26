package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class SelectAnswerCodeStat implements Serializable {
	public SelectAnswerCodeStat() {
		super();
	}

	public SelectAnswerCodeStat(String answerCode, Integer count) {
		super();
		this.answerCode = answerCode;
		this.count = count;
	}

	public SelectAnswerCodeStat(String answerCode, float count) {
		super();
		this.answerCode = answerCode;
		this.count = count;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String answerCode;

	private float count;

	public String getAnswerCode() {
		return answerCode;
	}

	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}

}

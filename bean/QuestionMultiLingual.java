package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

public class QuestionMultiLingual implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String languageCode;

	private String title;

	private List<QuestionOption> answers;

	public List<QuestionOption> getAnswers() {
		return answers;
	}

	public void setAnswers(List<QuestionOption> answers) {
		this.answers = answers;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

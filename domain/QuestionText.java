package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.QuestionMultiLingual;

public class QuestionText implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<QuestionMultiLingual> multilingual;

	public List<QuestionMultiLingual> getMultilingual() {
		return multilingual;
	}

	public void setMultilingual(List<QuestionMultiLingual> multilingual) {
		this.multilingual = multilingual;
	}

}

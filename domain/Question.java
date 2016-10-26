package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.bean.QuestionMultiLingual;
import org.iqvis.nvolv3.bean.QuestionUI;
import org.springframework.data.annotation.Id;

/**
 * @author iqvis
 *
 */
public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String questionKey;

	private QuestionUI ui;

	@NotEmpty(message = "Type should not be empty")
	private String Type;

	private List<QuestionMultiLingual> multilingual;

	public List<QuestionMultiLingual> getMultilingual() {
		return multilingual;
	}

	public void setMultilingual(List<QuestionMultiLingual> multilingual) {
		this.multilingual = multilingual;
	}

	public QuestionUI getUi() {
		return ui;
	}

	public void setUi(QuestionUI ui) {
		this.ui = ui;
	}

	public String getQuestionKey() {
		return questionKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

}

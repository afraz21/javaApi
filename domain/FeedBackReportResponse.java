package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

public class FeedBackReportResponse implements Serializable {

	public FeedBackReportResponse(String questionId, String questionType, String questionKey, List<SelectAnswerCodeStat> selections) {
		super();
		this.questionId = questionId;
		this.questionType = questionType;
		this.questionKey = questionKey;
		this.selections = selections;
	}

	public FeedBackReportResponse(String questionId, String questionType, List<SelectAnswerCodeStat> selections) {
		super();
		this.questionId = questionId;
		this.questionType = questionType;
		this.selections = selections;
	}

	public FeedBackReportResponse() {
		super();
	}

	public FeedBackReportResponse(String questionId, List<SelectAnswerCodeStat> selections) {
		super();
		this.questionId = questionId;
		this.selections = selections;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String questionId;

	private String questionType;

	private String questionKey;

	public String getQuestionKey() {
		return questionKey;
	}

	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	private List<SelectAnswerCodeStat> selections;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public List<SelectAnswerCodeStat> getSelections() {
		return selections;
	}

	public void setSelections(List<SelectAnswerCodeStat> selections) {
		this.selections = selections;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (this.questionId + "").equals(((FeedBackReportResponse) obj).getQuestionId());
	}

}

package org.iqvis.nvolv3.domain;

import java.util.List;

public class CheckIn {

	private Boolean exists;

	private Boolean skipable;

	private List<String> options;

	private String validation;

	private Integer preTime;

	private Integer postTime;

	private List<String> code;
	
	public Boolean getExists() {
		return exists;
	}

	public void setExists(Boolean exists) {
		this.exists = exists;
	}

	public Boolean getSkipable() {
		return skipable;
	}

	public void setSkipable(Boolean skipable) {
		this.skipable = skipable;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public Integer getPreTime() {
		return preTime;
	}

	public void setPreTime(Integer preTime) {
		this.preTime = preTime;
	}

	public Integer getPostTime() {
		return postTime;
	}

	public void setPostTime(Integer postTime) {
		this.postTime = postTime;
	}

	public List<String> getCode() {
		return code;
	}

	public void setCode(List<String> code) {
		this.code = code;
	}
	
	

}

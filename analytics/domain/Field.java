package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class Field implements Serializable {
	public Field() {
		super();
	}

	public Field(String field, Object value, String operator) {
		super();
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Field name cannot be empty in where clause.")
	String field;

	@NotEmpty(message = "Field value  cannot be empty in where clause.")
	Object value;

	@NotEmpty(message = "Operator cannot be empty in where clause.")
	String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}

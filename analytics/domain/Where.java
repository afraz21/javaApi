package org.iqvis.nvolv3.analytics.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class Where implements Serializable {
	public Where() {
		super();
	}

	public Where(String field, String value, String operator) {
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
	String value;

	@NotEmpty(message = "Operator cannot be empty in where clause.")
	String operator;

	private List<Field> or = new ArrayList<Field>();

	private List<Field> and = new ArrayList<Field>();

	public List<Field> getOr() {
		return or;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setOr(List<Field> or) {
		this.or = or;
	}

	public List<Field> getAnd() {
		return and;
	}

	public void setAnd(List<Field> and) {
		this.and = and;
	}

}

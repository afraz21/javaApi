package org.iqvis.nvolv3.search;

import java.util.List;

public class Where {

	List<Field> or;

	List<Field> and;

	String fieldName;

	String fieldValue;

	String operator;

	String fieldType;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<Field> getOr() {
		return or;
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

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}

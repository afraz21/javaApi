package org.iqvis.nvolv3.search;

import org.hibernate.validator.constraints.NotEmpty;

public class Field {

	@NotEmpty(message = "Field name cannot be empty in where clause.")
	String fieldName;

	@NotEmpty(message = "Field value  cannot be empty in where clause.")
	String fieldValue;

	@NotEmpty(message = "Operator cannot be empty in where clause.")
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

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}

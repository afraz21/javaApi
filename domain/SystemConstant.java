package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "systemConstents")
public class SystemConstant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String type;

	private String name;

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @author Qasim
	 * @param void
	 * @return Integer
	 * @throws
	 * @see Integer
	 * */
	public Integer toInteger() {

		return this.value!=null?((Double)this.value).intValue():null;
	}

	/**
	 * @author Qasim
	 * @param void no parameter
	 * @return String
	 * @throws
	 * @see String
	 * */
	public String toString() {

		return (String) this.value;
	}

	/**
	 * @author Qasim
	 * @param void
	 * @return Boolean
	 * @throws
	 * @see Boolean
	 */
	public Boolean toBoolean() {

		return (Boolean) this.value;
	}
}

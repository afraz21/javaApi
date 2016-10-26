package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Connector implements Serializable {

	public Connector(String name, String url) {
		super();
		this.name = name;
		
		this.url = url;
	}

	public Connector(String name, String url, String accesstoken, String secretKey) {
		super();
		
		this.name = name;
		
		this.url = url;
		
		this.accessToken = accesstoken;
		
		this.secretToken = secretKey;

	}

	public Connector() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;

	private String url;

	private String accessToken;

	private String secretToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub

		if (obj == null) {
			return false;
		}

		return ((Connector) obj).getName().equals(this.name);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecretToken() {
		return secretToken;
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

}

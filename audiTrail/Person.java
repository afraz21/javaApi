package org.iqvis.nvolv3.audiTrail;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private String login;
	private String name;
	private List<String> ss = new ArrayList<String>();

	public Person(String login, String name) {
		this.login = login;
		this.name = name;
		ss.add(name);
	}

	public Person() {

	}

	public List<String> getSs() {
		return ss;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}
}

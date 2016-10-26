package org.iqvis.nvolv3.bean;

public class MultiLingualPersonnelInformation extends MultiLingual {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bio;

	private String organization;

	private String qualification;

	private String designation;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

}

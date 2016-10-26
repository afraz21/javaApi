package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.controller.Contact;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a User
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class User extends Audit implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	private String firstName;

	private String lastName;

	private String accountId;

	private String address1;

	private String address2;

	private String country;

	private String state;

	private String zip;

	private String city;

	private String partnerId;

	private Boolean organizerApproved;

	private Boolean sponsorApproved;
	
	private String countryCode;
	
	private String stateCode;
	
	private Contact contact;

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	@JsonIgnore
	private List<String> userTypes; // Common User/Organizer/Admin/Sponsor

	private Boolean isDeleted = false;

	private String userStatus; // Pending Verification / Active / blocked

	public Boolean getOrganizerApproved() {
		return organizerApproved;
	}

	public void setOrganizerApproved(Boolean organizerApproved) {
		this.organizerApproved = organizerApproved;
	}

	public Boolean getSponsorApproved() {
		return sponsorApproved;
	}

	public void setSponsorApproved(Boolean sponsorApproved) {
		this.sponsorApproved = sponsorApproved;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Email
	@NotNull(message = "Email cannot be null.")
	@NotEmpty(message = "Email cannot be empty.")
	private String email;

	@NotNull
	@NotEmpty(message = "Password cannot be empty.")
	private String password;

	@JsonIgnore
	private List<Track> tracks;

	@JsonIgnore
	private List<Venue> venues;

	@JsonIgnore
	private List<Sponsor> sponsors;

	@JsonIgnore
	private List<Personnel> personnels;

	@JsonIgnore
	private List<News> news;

	private List<String> languages;

	private String newPassword;

	private String contactNumber;

	@DBRef(lazy = true)
	private Media organizerCompanyLogo;

	@JsonIgnore
	public Media getOrganizerCompanyLogoO() {
		return organizerCompanyLogo;
	}

	public Object getOrganizerCompanyLogo() {
		if (organizerCompanyLogo == null)
			return null;

		return new Picture(organizerCompanyLogo);
	}

	public void setOrganizerCompanyLogo(Media companyLogo) {
		this.organizerCompanyLogo = companyLogo;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getLastName() {

		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty
	public List<String> getUserType() {

		return this.userTypes;
	}

	@JsonIgnore
	public List<String> getUserTypes() {
		return userTypes;
	}

	@JsonIgnore
	public String getUserTypeCurrent() {

		return this.userTypes==null || this.userTypes.size()==0?null:this.userTypes.get(0);
	}
	
	@JsonProperty
	public void setUserTypes(List<String> userTypes) {
		this.userTypes = userTypes;
	}

	@JsonProperty
	public void setUserType(String userType) {

		if (this.userTypes == null)
			this.userTypes = new ArrayList<String>();

		if (userType != null && userType != "") {

			this.userTypes.add(userType);
		}

	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Venue> getVenues() {
		return venues;
	}

	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}

	public List<Sponsor> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<Sponsor> sponsers) {
		this.sponsors = sponsers;
	}

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> speakers) {
		this.personnels = speakers;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String toString() {

		return firstName + " " + lastName;
	}
}

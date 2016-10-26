package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.MultiLingualPersonnelInformation;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.Personnel;
import org.springframework.data.mongodb.core.mapping.Document;

import ch.lambdaj.Lambda;

/**
 * A simple POJO representing a Personnel
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class PersonnelResponse implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	private String id;

	private String name;

	private Media picture;

	private String email;

	private String phone;

	private String organizerId;

	private String type;

	private String questionnaireType;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(String questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	private String questionnaireId;

	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	private boolean keynote = false;

	public boolean isKeynote() {
		return keynote;
	}

	public void setKeynote(boolean keynote) {
		this.keynote = keynote;
	}

	private List<MultiLingualPersonnelInformation> multiLingual;

	public PersonnelResponse(Personnel p, List<EventPersonnel> eventPersonnel) {

		if (p != null) {
			this.id = p.getId();

			this.name = p.getName();

			this.picture = p.getPictureO();

			this.phone = p.getPhone();

			this.email = p.getEmail();

			this.type = p.getType();

			this.multiLingual = p.getMultiLingual();

			this.organizerId = p.getOrganizerId();

			try {
				EventPersonnel temp = Lambda.select(eventPersonnel, Lambda.having(Lambda.on(EventPersonnel.class).getPersonnelId(), Matchers.equalTo(p.getId()))).get(0);

				if (temp != null) {
					this.keynote = temp.isFeatured();

					this.questionnaireId = temp.getQuestionnaireId();

					this.questionnaireType = temp.getQuestionnaireType();

					this.sortOrder = temp.getSortOrder();
				}
			}
			catch (Exception e) {

			}

		}
	}

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

	public Picture getPicture() {
		return new Picture(picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MultiLingualPersonnelInformation> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingualPersonnelInformation> multiLingual) {
		this.multiLingual = multiLingual;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((PersonnelResponse) obj).getId());
	}

}

package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.EventResource;

public class MobileEventResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String type;

	private Picture logo;

	private String attachmentURL;

	private List<MultiLingual> multiLingual;

	public MobileEventResource(EventResource r) {
		this.id = r.getId();
		this.type = r.getType();
		this.logo = new Picture(r.getPicture());
		this.attachmentURL = r.getAttachmentURL();
		this.multiLingual = r.getMultiLingual();
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

	public Picture getLogo() {
		return logo;
	}

	public void setLogo(Picture logo) {
		this.logo = logo;
	}

	public String getAttachmentURL() {
		return attachmentURL;
	}

	public void setAttachmentURL(String attachmentURL) {
		this.attachmentURL = attachmentURL;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

}

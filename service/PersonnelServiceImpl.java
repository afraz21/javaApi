package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingualPersonnelInformation;
import org.iqvis.nvolv3.dao.PersonnelDao;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_PERSONNEL)
@Transactional
public class PersonnelServiceImpl implements PersonnelService {

	@Autowired
	private PersonnelDao personnelDao;

	public Personnel get(String id, String organizerId) throws NotFoundException {

		return personnelDao.get(id, organizerId);
	}

	public Personnel add(Personnel personnel) throws Exception {

		personnel.setCreatedDate(new DateTime());

		return personnelDao.add(personnel);
	}

	public Boolean delete(String id) {

		return personnelDao.delete(id);
	}

	public Personnel edit(Personnel personnel, String personnelId) throws Exception, NotFoundException {

		Personnel existingPersonnel = null;

		if (null != personnelId && !StringUtils.isEmpty(personnelId)) {

			existingPersonnel = personnelDao.get(personnelId, personnel.getOrganizerId());
		}

		if (null == existingPersonnel) {

			throw new NotFoundException(personnelId, "Personnel");
		}

		if (null != personnel.getName() && !StringUtils.isEmpty(personnel.getName())) {
			existingPersonnel.setName(personnel.getName());
		}

		if (null != personnel.getTwitterHandle() && !StringUtils.isEmpty(personnel.getTwitterHandle())) {

			existingPersonnel.setTwitterHandle(personnel.getTwitterHandle());
		}

		if (null != personnel.getEmail()) {
			existingPersonnel.setEmail(personnel.getEmail());
		}

		if (null != personnel.getPhone()) {
			existingPersonnel.setPhone(personnel.getPhone());
		}

		if (null != personnel.getPicture() && !StringUtils.isEmpty(personnel.getPicture())) {
			existingPersonnel.setPicture(personnel.getPictureO());
		}

		if (personnel.getOrganizerId() != null && !StringUtils.isEmpty(personnel.getOrganizerId())) {

			existingPersonnel.setOrganizerId(personnel.getOrganizerId());
		}

		if (null != personnel.getLastModifiedBy() && !StringUtils.isEmpty(personnel.getLastModifiedBy())) {
			existingPersonnel.setLastModifiedBy(personnel.getLastModifiedBy());
		}

		if (null != personnel.getMultiLingual() && personnel.getMultiLingual().size() > 0) {

			List<MultiLingualPersonnelInformation> finalLanguages = new ArrayList<MultiLingualPersonnelInformation>();

			if (null != existingPersonnel.getMultiLingual()) {
				finalLanguages = Utils.updateMultiLingualPersonnel(existingPersonnel.getMultiLingual(), personnel.getMultiLingual());

			}
			else {

				finalLanguages = personnel.getMultiLingual();
			}

			existingPersonnel.setMultiLingual(finalLanguages);

		}

		if (null != personnel.getType() && !StringUtils.isEmpty(personnel.getType())) {
			existingPersonnel.setType(personnel.getType());
		}

		if (null != personnel.getIsDeleted() && !StringUtils.isEmpty(personnel.getIsDeleted())) {
			existingPersonnel.setIsDeleted(personnel.getIsDeleted());
		}

		if (null != personnel.getIsActive() && !StringUtils.isEmpty(personnel.getIsActive())) {
			existingPersonnel.setIsActive(personnel.getIsActive());
		}

		existingPersonnel.setLastModifiedBy(personnel.getCreatedBy());

		existingPersonnel.setLastModifiedDate(new DateTime());

		if (null != personnel.getActivities()) {

			existingPersonnel.setActivities(personnel.getActivities());
		}

		if (existingPersonnel.getVersion() != null) {

			existingPersonnel.setVersion(existingPersonnel.getVersion() + 1);
		}

		return personnelDao.edit(existingPersonnel);
	}

	public String getPersonnelDetailUrl(Personnel personnel, HttpServletRequest request) {

		return personnelDao.getPersonnelDetailUrl(personnel, request);
	}

	public Page<Personnel> getAll(Criteria search, Pageable pageAble, String organizerId) {

		return personnelDao.getAll(Utils.parseQuery(search, "personnels."), search, pageAble, organizerId);
	}

	public List<Personnel> getPersonnelsByIds(List<String> ids, String organizerId) throws NotFoundException {

		return personnelDao.getPersonnelsByIds(ids, organizerId);
	}

	public List<Personnel> getOrganizerPersonnelsList(String organizerId) throws NotFoundException {

		return personnelDao.getOrganizerPersonnelsList(organizerId);
	}

}

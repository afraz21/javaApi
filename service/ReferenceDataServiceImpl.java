package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.ReferenceDataDao;
import org.iqvis.nvolv3.domain.ReferenceData;
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

@Service(Constants.SERVICE_REFERENCE_DATA)
@Transactional
public class ReferenceDataServiceImpl implements ReferenceDataService {

	@Autowired
	private ReferenceDataDao reference_Data_Dao;

	// @Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	// private DataChangeLogService dataChangeLogService;

	public Page<ReferenceData> getAll(Criteria referenceDataSearch, HttpServletRequest request, Pageable pageAble) {

		return reference_Data_Dao.getAll(Utils.parseCriteria(referenceDataSearch, ""), pageAble);
	}

	public ReferenceData get(String type, String organizerId) {

		return reference_Data_Dao.get(type, organizerId);
	}

	public ReferenceData get(String type) {

		return reference_Data_Dao.getEventType(type);
	}

	public ReferenceData add(ReferenceData referenceData) throws Exception {

		return reference_Data_Dao.add(referenceData);
	}

	public Boolean delete(String id) {

		return reference_Data_Dao.delete(id);
	}

	@SuppressWarnings("unused")
	public ReferenceData edit(ReferenceData referenceData, String referenceDataType) throws Exception, NotFoundException {

		// List<String> updatedReferenceIds=new ArrayList<String>();
		//
		// List<String> deleteReferenceIds=new ArrayList<String>();

		ReferenceData existingReferenceData = null;

		if (null != referenceDataType && !StringUtils.isEmpty(referenceDataType)) {

			existingReferenceData = get(referenceDataType, referenceData.getOrganizerId());
		}

		if (null == existingReferenceData) {

			throw new NotFoundException(referenceDataType, "ReferenceData");
		}
		if (null != referenceData.getType()) {
			existingReferenceData.setType(referenceData.getType());
		}

		if (null != referenceData.getOrganizerId()) {

			existingReferenceData.setOrganizerId(referenceData.getOrganizerId());
		}

		if (referenceData.getIsDeleted() != null) {

			existingReferenceData.setIsDeleted(referenceData.getIsDeleted());
		}

		if (null != referenceData.getData() && referenceData.getData().size() > 0) {

			List<Data> existingReferenceList = new ArrayList<Data>(existingReferenceData.getData());

			if (existingReferenceList != null) {

				for (Data refData : referenceData.getData()) {

					boolean flag = false;

					if (refData.getId() != null) {

						for (Data preData : existingReferenceList) {

							if (preData.getId().equals(refData.getId())) {

								if (refData.getIsActive() != null) {

									preData.setIsActive(refData.getIsActive());

									flag = true;
								}

								if (refData.getIsDeleted() != null) {

									preData.setIsDeleted(refData.getIsDeleted());

									flag = true;
								}

								if (refData.getName() != null) {

									preData.setName(refData.getName());

									flag = true;
								}

								if (refData.getPicture() != null) {

									preData.setPicture(refData.getPicture());

									flag = true;
								}

								if (refData.getType() != null) {

									preData.setType(refData.getType());

									flag = true;
								}

								if (refData.getSortOrder() != null) {

									preData.setSortOrder(refData.getSortOrder());

									flag = true;
								}

								if (null != refData.getMultiLingual()) {

									List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

									if (null != preData.getMultiLingual()) {
										finalLanguages = Utils.updateMultiLingual(preData.getMultiLingual(), refData.getMultiLingual());
									}
									else {

										finalLanguages = refData.getMultiLingual();
									}

									preData.setMultiLingual(finalLanguages);

									flag = true;

								}

								// if(flag){
								// updatedReferenceIds.add(refData.getId());
								// }

								preData.setLastModifiedDate(new DateTime());

								preData.setLastModifiedBy(referenceData.getCreatedBy());

								break;

							}
						}
					}
					else {

						refData.setId(UUID.randomUUID().toString());

						refData.setCreatedBy(referenceData.getCreatedBy());

						refData.setCreatedDate(new DateTime());

						existingReferenceList.add(refData);
					}
				}
			}

			existingReferenceData.setData(existingReferenceList);
		}

		ReferenceData editedReferenceData = reference_Data_Dao.edit(existingReferenceData);

		// List<String> l=new ArrayList<String>();
		// l.add(eventid);
		//
		//
		//
		//
		// dataChangeLogService.add(l, "EVENT", editedReferenceData.getType(),
		// editedReferenceData.getId(), Constants.LOG_ACTION_UPDATE,
		// EventPersonnel.class.toString());

		return editedReferenceData;

	}

	public String getReferenceDataDetailUrl(ReferenceData referenceData, HttpServletRequest request) {

		return reference_Data_Dao.getReferenceDataDetailUrl(referenceData, request);
	}

	public ReferenceData get(String id, String selector, boolean f) {

		return reference_Data_Dao.get(id, selector, false);
	}

}

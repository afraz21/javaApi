package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.SponsorDao;
import org.iqvis.nvolv3.dao.VendorDao;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
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

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_VENDOR)
@Transactional
public class VendorServiceImpl implements VendorService {

	@Autowired
	VendorDao vendorDao;

	@Autowired
	SponsorDao sponsorDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Vendor get(String id, String organizerId) throws NotFoundException {

		return vendorDao.get(id, organizerId);
	}

	public Vendor add(Vendor vendor) throws Exception {

		vendor.setCreatedDate(new DateTime());

		vendor = vendorDao.add(vendor);

		List<String> l = new ArrayList<String>();

		l.add(vendor.getEventId());

		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", Constants.VENDOR_LOG_KEY, vendor.getId(), Constants.LOG_ACTION_ADD, Sponsor.class.toString());

		}

		return vendor;
	}

	public Vendor edit(Vendor vendor) throws Exception {

		Vendor existingVendor = get(vendor.getId(), vendor.getOrganizerId());

		if (null == existingVendor) {

			throw new NotFoundException(vendor.getId(), "Vendor");

		}
		else {

			if (!existingVendor.getOrganizerId().equals(vendor.getOrganizerId())) {

				throw new NotFoundException(vendor.getId(), "Vendor");

			}

			existingVendor.setLastModifiedBy(vendor.getCreatedBy());

			existingVendor.setLastModifiedDate(new DateTime());

			if (vendor.getIsDeleted() != null && !StringUtils.isEmpty(vendor.getIsDeleted().toString())) {

				existingVendor.setIsDeleted(vendor.getIsDeleted());
			}

			if (vendor.getIsActive() != null && !StringUtils.isEmpty(vendor.getIsActive())) {

				existingVendor.setIsActive(vendor.getIsActive());
			}

			if (vendor.getName() != null && !StringUtils.isEmpty(vendor.getName())) {

				existingVendor.setName(vendor.getName());
			}

			// if (vendor.getPicture() != null) {
			//
			// existingVendor.setPicture(vendor.getPicture());
			// }

			if (vendor.getPictureO() != null) {

				existingVendor.setPicture(vendor.getPictureO());
			}

			if (null != vendor.getMultiLingual() && vendor.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingVendor.getMultiLingual()) {

					finalLanguages = Utils.updateMultiLingual(existingVendor.getMultiLingual(), vendor.getMultiLingual());

				}
				else {

					finalLanguages = vendor.getMultiLingual();
				}

				existingVendor.setMultiLingual(finalLanguages);

			}

			if (vendor.getBoothNumber() != null) {

				existingVendor.setBoothNumber(vendor.getBoothNumber());
			}

			if (vendor.getVendorCategoryId() != null) {

				existingVendor.setVendorCategoryId(vendor.getVendorCategoryId());
			}

			if (vendor.getOrganizerId() != null) {

				existingVendor.setOrganizerId(vendor.getOrganizerId());
			}

			if (vendor.getLongitude() != null && !StringUtils.isEmpty(vendor.getLongitude())) {

				existingVendor.setLongitude(existingVendor.getLongitude());
			}

			if (vendor.getLatitude() != null && !StringUtils.isEmpty(vendor.getLatitude())) {

				existingVendor.setLatitude(existingVendor.getLatitude());
			}

			if (vendor.getSponsorId() != null && !StringUtils.isEmpty(vendor.getSponsorId())) {

				existingVendor.setSponsorId(existingVendor.getSponsorId());
			}

			if (vendor.getEventId() != null && !StringUtils.isEmpty(vendor.getEventId())) {

				existingVendor.setEventId(existingVendor.getEventId());
			}

			existingVendor.setSortOrder(vendor.getSortOrder());

		}

		existingVendor = vendorDao.edit(existingVendor);

		List<String> l = new ArrayList<String>();

		l.add(existingVendor.getEventId());

		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", Constants.VENDOR_LOG_KEY, existingVendor.getId(), existingVendor.getIsDeleted() ? Constants.LOG_ACTION_DELETE : Constants.LOG_ACTION_UPDATE, Vendor.class.toString());

		}

		return existingVendor;
	}

	public Boolean delete(String id) {

		Vendor vendor = vendorDao.get(id);

		if (vendorDao.delete(id)) {

			List<String> l = new ArrayList<String>();

			l.add(vendor.getEventId());

			if (l.size() > 0) {

				dataChangeLogService.add(l, "EVENT", Constants.VENDOR_LOG_KEY, id, Constants.LOG_ACTION_DELETE, Vendor.class.toString());

			}

			return true;
		}

		return false;
	}

	public String getVendorDetailUrl(Vendor vendor, HttpServletRequest request) {

		return vendorDao.getVendorDetailUrl(vendor, request);
	}

	public Page<Vendor> getAll(Criteria vendorSearch, HttpServletRequest request, Pageable pageAble, String organizerId) {

		return vendorDao.getAll(Utils.parseCriteria(vendorSearch, ""), pageAble, organizerId);
	}

	public long count(String eventId, String organizerId) {
		// TODO Auto-generated method stub
		return vendorDao.count(eventId, organizerId);
	}

}

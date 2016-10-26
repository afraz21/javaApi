package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.SponsorDao;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_SPONSOR)
@Transactional
public class SponsorServiceImpl implements SponsorService {

	@Autowired
	SponsorDao sponserDao;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Sponsor get(String id) {

		return sponserDao.get(id);
	}

	public Sponsor add(Sponsor sponser) throws Exception {

		Sponsor addedSponsor = sponserDao.add(sponser);

		List<String> l = new ArrayList<String>();

		l.addAll(eventService.getEventIdsBySelector(addedSponsor.getId(), "sponsors.sponsorId"));

		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, addedSponsor.getId(), Constants.LOG_ACTION_ADD, Sponsor.class.toString());

		}

		return addedSponsor;
	}

	public Sponsor edit(Sponsor sponsor) throws Exception {

		Sponsor existingSponsor = get(sponsor.getId());

		if (null == existingSponsor) {

			throw new NotFoundException(sponsor.getId(), "Sponsor");

		}
		else {

			if (!existingSponsor.getOrganizerId().equals(sponsor.getOrganizerId())) {

				throw new NotFoundException(sponsor.getId(), "Sponsor");

			}

			existingSponsor.setLastModifiedBy(sponsor.getCreatedBy());

			if (sponsor.getIsDeleted() != null && !StringUtils.isEmpty(sponsor.getIsDeleted().toString())) {

				existingSponsor.setIsDeleted(sponsor.getIsDeleted());
			}

			if (sponsor.getIsActive() != null && !StringUtils.isEmpty(sponsor.getIsActive())) {

				existingSponsor.setIsActive(sponsor.getIsActive());
			}

			if (sponsor.getName() != null && !StringUtils.isEmpty(sponsor.getName())) {

				existingSponsor.setName(sponsor.getName());
			}

			// if (sponsor.getPicture() != null) {
			//
			// existingSponsor.setPicture(sponsor.getPicture());
			// }

			if (sponsor.getPictureO() != null) {

				existingSponsor.setPicture(sponsor.getPictureO());
			}

			if (null != sponsor.getMultiLingual() && sponsor.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingSponsor.getMultiLingual()) {
					finalLanguages = Utils.updateMultiLingual(existingSponsor.getMultiLingual(), sponsor.getMultiLingual());

				}
				else {

					finalLanguages = sponsor.getMultiLingual();
				}

				existingSponsor.setMultiLingual(finalLanguages);

			}

			if (sponsor.getBusinessCategory() != null) {

				existingSponsor.setBusinessCategory(sponsor.getBusinessCategory());
			}

			if (sponsor.getUser() != null) {

				existingSponsor.setUser(sponsor.getUser());
			}

			if (sponsor.getFirstName() != null && !StringUtils.isEmpty(sponsor.getFirstName())) {

				existingSponsor.setFirstName(sponsor.getFirstName());
			}

			if (sponsor.getLastName() != null && !StringUtils.isEmpty(sponsor.getLastName())) {

				existingSponsor.setLastName(sponsor.getLastName());
			}

			if (sponsor.getInvite() != null && !StringUtils.isEmpty(sponsor.getInvite())) {

				existingSponsor.setInvite(sponsor.getInvite());
			}

			if (sponsor.getEmail() != null) {

				existingSponsor.setEmail(sponsor.getEmail());
			}

			if (sponsor.getSponsorCategoryId() != null) {

				existingSponsor.setSponsorCategoryId(sponsor.getSponsorCategoryId());
			}

			// if (sponsor.getSponsor_type() != null) {
			//
			// existingSponsor.setSponsor_type(sponsor.getSponsor_type());
			// }
		}

		Sponsor spon = sponserDao.edit(existingSponsor);

		List<String> l = new ArrayList<String>();

		if (!spon.getIsDeleted()) {
			l.addAll(eventService.getEventIdsBySelector(spon.getId(), "sponsors.sponsorId"));

			if (l.size() > 0) {

				// int i =0;
				// for (String id : l) {
				// System.out.println("i :"+ i);
				//
				// Event event=eventService.get(id, sponsor.getOrganizerId());
				//
				// event.setEventLevelChange(true);
				//
				// eventService.edit(event, id, sponsor.getOrganizerId());
				//
				// }

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, spon.getId(), Constants.LOG_ACTION_UPDATE, Sponsor.class.toString());

			}
		}
		else {
			l.addAll(eventService.getEventIdsBySelector(spon.getId(), "sponsors.sponsorId"));

			if (l.size() > 0) {
				dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, spon.getId(), Constants.LOG_ACTION_DELETE, Sponsor.class.toString());

			}
		}

		return spon;
	}

	public Boolean delete(String id) {

		return sponserDao.delete(id);
	}

	public String getSponserDetailUrl(Sponsor sponser, HttpServletRequest request) {

		return sponserDao.getSponserDetailUrl(sponser, request);
	}

	public Page<Sponsor> getAll(Criteria sponsorSearch, HttpServletRequest request, Pageable pageAble, String organizerId) {

		return sponserDao.getAll(Utils.parseCriteria(sponsorSearch, ""), pageAble, organizerId);
	}

	public List<Sponsor> getAll(Query query, String organizerId) {
		// TODO Auto-generated method stub
		return sponserDao.getAll(query, organizerId);
	}

}

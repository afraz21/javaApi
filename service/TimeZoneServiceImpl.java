package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.TimeZoneDao;
import org.iqvis.nvolv3.domain.TimeZone;
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

@Service(Constants.SERVICE_TIME_ZONE)
@Transactional
public class TimeZoneServiceImpl implements TimeZoneService {

	@Autowired
	TimeZoneDao timeZoneDao;

	public TimeZone get(String id) throws NotFoundException {

		return timeZoneDao.get(id);
	}

	public TimeZone add(TimeZone timeZone) throws Exception {

		timeZone.setCreatedDate(new DateTime());

		return timeZoneDao.add(timeZone);
	}

	public TimeZone edit(TimeZone timeZone) throws Exception {

		TimeZone existingTimeZone = get(timeZone.getId());

		if (null == existingTimeZone) {

			throw new NotFoundException(timeZone.getId(), "TimeZone");

		}
		else {

			existingTimeZone.setLastModifiedBy(timeZone.getCreatedBy());

			existingTimeZone.setLastModifiedDate(new DateTime());

			if (timeZone.getIsDeleted() != null && !StringUtils.isEmpty(timeZone.getIsDeleted().toString())) {

				existingTimeZone.setIsDeleted(timeZone.getIsDeleted());
			}

			if (timeZone.getIsActive() != null && !StringUtils.isEmpty(timeZone.getIsActive())) {

				existingTimeZone.setIsActive(timeZone.getIsActive());
			}

			if (timeZone.getName() != null && !StringUtils.isEmpty(timeZone.getName())) {

				existingTimeZone.setName(timeZone.getName());
			}

			if (timeZone.getDescription() != null && !StringUtils.isEmpty(timeZone.getDescription())) {

				existingTimeZone.setDescription(timeZone.getDescription());
			}

		}

		return timeZoneDao.edit(existingTimeZone);
	}

	public Boolean delete(String id) {

		return timeZoneDao.delete(id);
	}

	public String getTimeZoneDetailUrl(TimeZone timeZone, HttpServletRequest request) {

		return timeZoneDao.getTimeZoneDetailUrl(timeZone, request);
	}

	public Page<TimeZone> getAll(Criteria timeZoneSearch, HttpServletRequest request, Pageable pageAble) {

		return timeZoneDao.getAll(Utils.parseCriteria(timeZoneSearch, ""), pageAble);
	}

}

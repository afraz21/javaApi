package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.NewsDao;
import org.iqvis.nvolv3.domain.News;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_NEWS)
@Transactional
public class NewsServiceImpl implements NewsService {

	@Autowired
	NewsDao newsDao;

	public News get(String id, String organizerId) throws NotFoundException {

		return newsDao.get(id, organizerId);
	}

	public News add(News news) throws Exception {

		return newsDao.add(news);
	}

	public News edit(News news) throws Exception {

		News existingNews = get(news.getId(), news.getOrganizerId());

		if (null == existingNews) {

			throw new NotFoundException(news.getId(), "News");

		}
		else {

			if (news.getIsDeleted() != null && !StringUtils.isEmpty(news.getIsDeleted().toString())) {
				existingNews.setIsDeleted(news.getIsDeleted());
			}

			if (news.getIsActive() != null && !StringUtils.isEmpty(news.getIsActive())) {
				existingNews.setIsActive(news.getIsActive());
			}

			if (news.getPictureO() != null && !StringUtils.isEmpty(news.getPictureO())) {

				existingNews.setPicture(news.getPictureO());
			}

			if (news.getMultiLingual() != null && news.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingNews.getMultiLingual()) {

					finalLanguages = Utils.updateMultiLingual(existingNews.getMultiLingual(), news.getMultiLingual());

				}
				else {

					finalLanguages = news.getMultiLingual();
				}

				existingNews.setMultiLingual(finalLanguages);

			}

		}

		return newsDao.edit(existingNews);
	}

	public String getNewsDetailUrl(News news, HttpServletRequest request) {

		return newsDao.getNewsDetailUrl(news, request);
	}

	public Page<News> getAll(Criteria userCriteria, String code, Pageable pageAble, String organizerId) {

		return newsDao.getAll(Utils.parseQuery(userCriteria, "news."), code, pageAble, organizerId);
	}

}

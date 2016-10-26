package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.News;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import ch.lambdaj.Lambda;

import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

@SuppressWarnings("restriction")
@Repository
public class NewsDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single News
	 * 
	 * @param String
	 *            id
	 * @return News
	 * @throws NotFoundException
	 */
	public News get(String id, String organizerId) throws NotFoundException {

		logger.debug("Retrieving an existing news");

		Query query = new Query(Criteria.where("id").is(organizerId).and("news.isDeleted").is(false));

		query.fields().include("news");

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		if (user == null) {
			throw new NotFoundException(organizerId, "Organizer News");
		}

		List<News> news = Lambda.select(user.getNews(), Lambda.having(Lambda.on(News.class).getId(), Matchers.equalTo(id)));

		if (news != null && news.size() > 0) {

			return news.get(0);

		}
		else {

			throw new NotFoundException(id, "News");
		}
	}

	/**
	 * Adds a new News
	 * 
	 * @param News
	 *            news
	 * @return News
	 */
	public News add(News news) throws Exception {
		logger.debug("Adding a new venue");

		try {

			User e = mongoTemplate.findOne(new Query(Criteria.where("id").is(news.getOrganizerId())), User.class);

			if (e != null) {

				String newsId = UUID.randomUUID().toString();

				news.setId(newsId);

				if (e.getNews() != null) {

					e.getNews().add(news);

				}
				else {

					// adding news
					List<News> newsList = new ArrayList<News>();

					newsList.add(news);

					e.setNews(newsList);

				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(news.getOrganizerId(), "Organizer");
			}

			return news;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add new venue", e);
			throw e;
		}
	}

	/**
	 * Edits an existing News
	 * 
	 * @param News
	 *            news
	 * @return News
	 */
	public News edit(News news) throws Exception {
		logger.debug("Editing existing News");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newNewsRec = (DBObject) converter.convertToMongoType(news);

			Query query = Query.query(Criteria.where("news._id").is(news.getId()).and("id").is(news.getOrganizerId()));

			Update update = new Update().set("news.$", newNewsRec);

			mongoTemplate.updateFirst(query, update, User.class);

			return news;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing news", e);

			throw e;
		}

	}

	/*
	 * 
	 * Get all the news on the basis of organizer
	 */
	public Page<News> getAll(Criteria userCriteria, String code, Pageable pageAble, String organizerId) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		logger.debug("Retrieving all news");

		String collection = "news";

		Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("id").is(organizerId)), Aggregation.skip(pageAble.getOffset()), Aggregation.limit(pageAble.getPageSize()));

		AggregationResults<News> result = mongoTemplate.aggregate(aggregation, User.class, News.class);

		DBObject object = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<News> newsList = new ArrayList<News>();

		Gson gson = new Gson();

		for (DBObject dbObject : dbObjects) {

			DBObject dbNews = (DBObject) dbObject.get(collection);

			String id = dbNews.get("_id").toString();

			News obj = gson.fromJson(dbObject.get(collection).toString(), News.class);

			obj.setId(id);

			if (null != dbNews.get("picture")) {

				DBRef picture = (DBRef) dbNews.get("picture");

				String mediaID = picture.getId().toString();

				Media media = mediaDao.get(mediaID);

				obj.setPicture(media);
			}

			newsList.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind("news"), Aggregation.match(userCriteria));

		AggregationResults<News> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, User.class, News.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		for (News news : newsList) {

			try {
				news.setMultiLingual(Utils.getMultiLingualByLangCode(news.getMultiLingual(), code));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		Page<News> newsPage = new PageImpl<News>(newsList, pageAble, total.size());

		return newsPage;

	}

	public String getNewsDetailUrl(News news, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.News_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, news.getOrganizerId()) + Urls.GET_NEWS.replace(Urls.GET_NEWS, news.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}

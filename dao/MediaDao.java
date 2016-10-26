package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class MediaDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves a single Media
	 * 
	 * @param String
	 *            id
	 * @return Media
	 */
	public Media get(String id) {
		logger.debug("Retrieving an existing Media");

		Query query = new Query(Criteria.where("id").is(id));

		Media media = mongoTemplate.findOne(query, Media.class, MongoDBCollections.MEDIA.toString());

		return media;
	}

	/**
	 * Adds a new Media
	 * 
	 * @param Media
	 *            media
	 * @return Media
	 */
	public Media add(Media media) throws Exception {
		logger.debug("Adding a new Media");

		try {

			mongoTemplate.insert(media, MongoDBCollections.MEDIA.toString());

			return media;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new media", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Media
	 * 
	 * @param Media
	 *            media
	 * @param String
	 *            mediaId
	 * @return Media
	 */
	public Media edit(Media media) throws Exception, NotFoundException {

		logger.debug("Editing existing Media");

		try {

			mongoTemplate.save(media);

			return media;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing media", e);

			throw e;
		}

	}

	public String getMediaDetailUrl(Media media, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.MEDIA_BASE_URL;

		replaceUrlToken += Urls.GET_MEDIA.replace("{id}", media.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}

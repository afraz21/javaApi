package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.News;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.NewsService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.News_BASE_URL)
public class NewsController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_NEWS)
	private NewsService newsService;

	@RequestMapping(value = Urls.ADD_NEWS, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid News news, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add news");

		ResponseMessage response = new ResponseMessage();

		try {

			news.setOrganizerId(organizerId);

			News addedNews = newsService.add(news);

			addedNews = newsService.get(addedNews.getId(), organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "News"));

			response.setRecordId(addedNews.getId().toString());

			response.setRecord(addedNews);

			response.setDetails_url(newsService.getNewsDetailUrl(addedNews, request));

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "News"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding news", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(news.getOrganizerId(), "Organizer");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_NEWS, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody @Valid News news, @PathVariable(value = "organizerId") String organizerId, @PathVariable("id") String newsId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a news");

		ResponseMessage response = new ResponseMessage();

		news.setId(newsId);

		try {

			news.setOrganizerId(organizerId);

			News editedNews = newsService.edit(news);

			editedNews = newsService.get(editedNews.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "news"));

			response.setRecordId(editedNews.getId().toString());

			response.setRecord(editedNews);

			response.setDetails_url(newsService.getNewsDetailUrl(editedNews, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(newsId, "News");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_NEWS, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String newsId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a News");

		ResponseMessage response = new ResponseMessage();

		try {

			News news = newsService.get(newsId, organizerId);

			if (news != null) {

				news.setIsDeleted(true);

				newsService.edit(news);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "News"));

				response.setRecordId(newsId);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(newsId, "News");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(newsId, "News");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_NEWS, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public News get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a News");

		News existingNews = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingNews = newsService.get(Id, organizerId);

		}

		if (existingNews == null) {

			throw new NotFoundException(Id, "News");
		}

		return existingNews;
	}

	@RequestMapping(value = Urls.GET_NEWS_LIST, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<News> getAll(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, @RequestParam(value = "langCode", required = false) String code, HttpServletRequest request) {

		logger.debug("Received request to show all news");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<News> newsList = newsService.getAll(search, code, pageAble, organizerId);

		return newsList;
	}

}

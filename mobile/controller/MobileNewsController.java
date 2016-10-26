package org.iqvis.nvolv3.mobile.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.News;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.NewsService;
import org.iqvis.nvolv3.utils.Constants;
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
@RequestMapping(Urls.MOBILE_NEWS_BASE_URL)
public class MobileNewsController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_NEWS)
	private NewsService newsService;

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

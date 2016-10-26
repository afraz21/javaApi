package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.FileTypeNotAllowedException;
import org.iqvis.nvolv3.exceptionHandler.MediaTypeNotAllowedException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.MediaService;
import org.iqvis.nvolv3.upload.factory.MediaFactory;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.ImageSize;
import org.iqvis.nvolv3.utils.ImageUtils;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.MEDIA_BASE_URL)
public class MediaController {

	@Resource(name = Constants.SERVICE_MEDIA)
	private MediaService mediaService;

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	MediaFactory mediaFactory;

	@RequestMapping(value = Urls.ADD_MEDIA, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage addMedia(@RequestParam(required = false, value = "mediaFile") MultipartFile file, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "bitcasa", required = false) boolean bitcasa, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to upload media");

		ResponseMessage responseMessage = new ResponseMessage();

		if (!ImageUtils.validateImageFile(file)) {
			throw new FileTypeNotAllowedException();
		}
		else {

			if (MongoDBCollections.valueOf(MongoDBCollections.class, type) != null) {

				try {

					String url;
					String urlSmall;
					String urlLarge;
					String urlMedium;
					String urlThumb;

					url = mediaFactory.upload(Utils.multipartToFile(file, ""), type);

					urlSmall = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.SMALL.toString()), type);

					urlLarge = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.LARGE.toString()), type);

					urlMedium = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.MEDIUM.toString()), type);

					urlThumb = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.THUMBNAIL.toString()), type);

					Media media = new Media();

					// media.setId(UUID.randomUUID().toString());

					media.setType(type);

					media.setUrl(url);

					media.setUrlSmall(urlSmall);

					media.setUrlMedium(urlMedium);

					media.setUrlLarge(urlLarge);

					media.setUrlThumb(urlThumb);

					media.setMediaContainer(mediaFactory.getMyClass());

					media = mediaService.add(media);

					responseMessage.setRecordId(media.getId());

					responseMessage.setMessage("Media file has been uploaded sucessfully.");
					
					responseMessage.setRecord(media);
					
					responseMessage.setDetails_url(mediaService.getMediaDetailUrl(media, request));

				}
				catch (Exception e) {

					e.printStackTrace();
					throw e;
				}

			}
			else {

				throw new MediaTypeNotAllowedException(type);
			}
		}
		return responseMessage;
	}

	@RequestMapping(value = Urls.ADD_RESOURCE, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage addResources(@RequestParam(required = false, value = "mediaFile") MultipartFile file, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to upload media");

		String type = MongoDBCollections.EVENT_RESOURCES.toString();

		ResponseMessage responseMessage = new ResponseMessage();

		try {

			String url = mediaFactory.upload(Utils.multipartToFile(file, ""), type);

			Media media = new Media();

			// media.setId(UUID.randomUUID().toString());

			media.setType(type);

			media.setUrl(url);

			media.setUrlSmall("");

			media.setUrlMedium("");

			media.setUrlLarge("");

			media.setUrlThumb("");

			media = mediaService.add(media);

			responseMessage.setRecordId(media.getId());

			responseMessage.setMessage("Media file has been uploaded sucessfully.");

			responseMessage.setRecord(media);

			responseMessage.setDetails_url(mediaService.getMediaDetailUrl(media, request));

		}
		catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

		return responseMessage;
	}

	@RequestMapping(value = Urls.ADD_CUSTUM_MEDIA, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage addCustomMedia(@RequestParam(required = false, value = "mediaFile") MultipartFile file, @RequestParam(value = "type", required = true) String type, @RequestParam(value = "width", required = true) int width, @RequestParam(value = "height", required = true) int height, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to upload media");

		ResponseMessage responseMessage = new ResponseMessage();

		if (!ImageUtils.validateImageFile(file)) {
			throw new FileTypeNotAllowedException();
		}
		else {

			if (MongoDBCollections.valueOf(MongoDBCollections.class, type) != null) {

				try {
					String url = mediaFactory.upload(Utils.multipartToFile(file, ""), type);

					String urlCustom = mediaFactory.upload(ImageUtils.createCustomImage(file, ImageSize.CUSTOM.toString(), width, height), type);

					Media media = new Media();

					media.setType(type);

					media.setUrl(url);

					media.setUrlCustom(urlCustom);

					media = mediaService.add(media);

					responseMessage.setMessage("Media file has been uploaded sucessfully.");

					responseMessage.setRecord(media);

					responseMessage.setDetails_url(mediaService.getMediaDetailUrl(media, request));

				}
				catch (Exception e) {

					throw e;
				}

			}
			else {

				throw new MediaTypeNotAllowedException(type);
			}
		}
		return responseMessage;
	}

	@RequestMapping(value = "/files", method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage addCustomMediaPsd(@RequestParam(required = false, value = "mediaFile") MultipartFile file, @RequestParam(value = "type", required = true) String type, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to upload media");

		ResponseMessage responseMessage = new ResponseMessage();

		try {
			String url = mediaFactory.upload(Utils.multipartToFile(file, ""), type);

			Media media = new Media();

			media.setType(type);

			media.setUrl(url);

			media = mediaService.add(media);

			responseMessage.setMessage("Media file has been uploaded sucessfully.");

			responseMessage.setRecord(media);

			responseMessage.setDetails_url(mediaService.getMediaDetailUrl(media, request));

		}
		catch (Exception e) {

			throw e;
		}

		return responseMessage;
	}

	/*
	 * Get Media By Id
	 */

	@RequestMapping(value = Urls.GET_MEDIA, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Media getEvent(@PathVariable("id") String mediaId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an event");

		Media existingMedia = null;

		if (null != mediaId && !mediaId.equalsIgnoreCase("")) {

			existingMedia = mediaService.get(mediaId);
		}

		if (existingMedia == null) {
			throw new NotFoundException(mediaId, "Media");
		}

		return existingMedia;
	}

}

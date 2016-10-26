package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventLink;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(Urls.EVENT_BASE_URL)
public class BranchIOLinkUpdate {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@RequestMapping(value = Urls.UPDATE_EVENT + "/branchio/{appId}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editBranchIo(@RequestBody EventLink link, @PathVariable("id") String eventid, @PathVariable("appId") String appId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		ResponseMessage response = new ResponseMessage();

		Event event = eventService.get(eventid, organizerId);

		response.setMessageCode(Constants.ERROR_CODE);

		response.setHttpCode(Constants.ERROR_CODE);

		if (event != null && event.getLinkedApp() != null && event.getLinkedApp().size() > 0) {

			boolean flag = true;

			for (EventLink eventLink : event.getLinkedApp()) {

				if (eventLink.getAppId().equals(appId)) {

					eventLink.setBranchIOurl(link.getBranchIOurl());

					flag = false;

				}

			}
			if (!flag) {
			
				List<String> l = new ArrayList<String>();

				l.add(event.getId());

				dataChangeLogService.add(l, "EVENT", "", "", Constants.LOG_ACTION_UPDATE, event.getClass().toString());

				Event editedEvent = eventService.edit(event, eventid, organizerId);

				response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Event"));

				response.setRecordId(editedEvent.getId().toString());

				editedEvent = eventService.get(editedEvent.getId(), organizerId);

				response.setRecord(editedEvent.getLinkedApp());

				response.setMessageCode(Constants.SUCCESS_CODE);

				response.setHttpCode(Constants.SUCCESS_CODE);

				response.setDetails_url(eventService.getEventDetailUrl(editedEvent, request));
			}
		}

		return response;
	}

}

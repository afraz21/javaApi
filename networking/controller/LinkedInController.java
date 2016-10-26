package org.iqvis.nvolv3.networking.controller;

import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_ACCESS_TOKEN;
import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_REQUEST_TOKEN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

import javax.annotation.Resource;

import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.SocialNetworkKeys;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.AttendeeDetail;
import org.iqvis.nvolv3.mobile.bean.LinkedinInformation;
import org.iqvis.nvolv3.oauth.service.OAuthServiceConfig;
import org.iqvis.nvolv3.oauth.service.OAuthServiceProvider;
import org.iqvis.nvolv3.utils.Constants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.google.gson.Gson;

@Controller
public class LinkedInController {

	@Autowired
	@Qualifier("linkedInServiceProvider")
	private OAuthServiceProvider linkedInServiceProvider;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@RequestMapping(value = { "/login-linkedin" }, method = RequestMethod.GET)
	public String login(@RequestParam(value="appId",required=false) String appId, @RequestParam(value="organizerId",required=false) String organizerId,WebRequest request) {

		if (appId != null) {

			AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

			SocialNetworkKeys keys = appConfiguration.getSocialMediaKeysMyName(Constants.LINKED_IN);

			linkedInServiceProvider = new OAuthServiceProvider(new OAuthServiceConfig(keys.getApikey(), keys.getApisecret(), keys.getCallback(), org.scribe.builder.api.LinkedInApi.class));

		}

		// getting request and access token from session
		Token requestToken = null;// (Token)request.getAttribute(ATTR_OAUTH_REQUEST_TOKEN,SCOPE_SESSION);

		Token accessToken = null; // (Token)
									// request.getAttribute(ATTR_OAUTH_ACCESS_TOKEN,
									// SCOPE_SESSION);

		if (requestToken == null || accessToken == null) {
			// generate new request token
			OAuthService service = linkedInServiceProvider.getService();
			requestToken = service.getRequestToken();
			request.setAttribute(ATTR_OAUTH_REQUEST_TOKEN, requestToken, SCOPE_SESSION);

			// redirect to linkedin auth page
			return "redirect:" + service.getAuthorizationUrl(requestToken);
		}
		return "welcomePage";
	}

	@RequestMapping(value = { "/linkedin-callback" }, method = RequestMethod.GET)
	@ResponseBody
	public String callback(@RequestParam(value="appId",required=false) String appId, @RequestParam(value="organizerId",required=false) String organizerId, @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier, WebRequest request) {

		ResponseMessage response = new ResponseMessage();

		if (appId != null) {

			AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

			SocialNetworkKeys keys = appConfiguration.getSocialMediaKeysMyName(Constants.LINKED_IN);

			linkedInServiceProvider = new OAuthServiceProvider(new OAuthServiceConfig(keys.getApikey(), keys.getApisecret(), keys.getCallback(), org.scribe.builder.api.LinkedInApi.class));

		}

		Gson gson = new Gson();

		try {
			// String hidden = null;

			if (oauthVerifier == null) {

				response.setMessage("Fail");

				response.setHttpCode(Constants.ERROR_CODE);

				// modelAndView.addObject("response_json_string", );

				return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
			}

			// getting request tocken
			OAuthService service = linkedInServiceProvider.getService();
			Token requestToken = (Token) request.getAttribute(ATTR_OAUTH_REQUEST_TOKEN, SCOPE_SESSION);

			// getting access token
			Verifier verifier = new Verifier(oauthVerifier);
			Token accessToken = service.getAccessToken(requestToken, verifier);

			// store access token as a session attribute
			request.setAttribute(ATTR_OAUTH_ACCESS_TOKEN, accessToken, SCOPE_SESSION);
			// https://developer.linkedin.com/docs/signin-with-linkedin
			// https://developer.linkedin.com/docs/fields/basic-profile
			// getting user profile
			OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,headline,summary,picture-url,public-profile-url,email-address)?format=json");

			service.signRequest(accessToken, oauthRequest);

			Response oauthResponse = oauthRequest.send();

			LinkedinInformation profileInfo = gson.fromJson(oauthResponse.getBody(), LinkedinInformation.class);

			AttendeeDetail detail = null;

			if (profileInfo != null) {

				detail = new AttendeeDetail(profileInfo);

				detail.setAccessToken(accessToken.getToken());

				detail.setAccessToken(accessToken.getSecret());
			}

			response.setRecord(detail);

			response.setMessage("Success");

			response.setHttpCode(Constants.SUCCESS_CODE);

			// modelAndView.addObject("response_json_string",
			// gson.toJson(response));

		}
		catch (Exception e) {
			response.setMessage("Fail");

			response.setHttpCode(Constants.ERROR_CODE);

			// modelAndView.addObject("response_json_string",
			// gson.toJson(response));

			return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
		}

		return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
	}
}
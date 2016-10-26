package org.iqvis.nvolv3.networking.controller;

import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_ACCESS_TOKEN;
import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_REQUEST_TOKEN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.mobile.bean.AttendeeDetail;
import org.iqvis.nvolv3.mobile.bean.FacebookInformation;
import org.iqvis.nvolv3.oauth.service.OAuthServiceProviderCarib;
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
public class FacebookControllerCarib {

	@Autowired
	@Qualifier("facebookServiceProviderCarib")
	private OAuthServiceProviderCarib facebookServiceProviderCarib;

	private static final Token EMPTY_TOKEN = null;

	@RequestMapping(value = { "/login-facebook-caribtix" }, method = RequestMethod.GET)
	public String login(WebRequest request) {

		// getting request and access token from session
		Token accessToken = null;// (Token)
									// request.getAttribute(ATTR_OAUTH_ACCESS_TOKEN,
									// SCOPE_SESSION);

		if (accessToken == null) {

			// generate new request token
			// / this is only for gettting profile informatino
			// OAuthService service = facebookServiceProvider.getService();
			OAuthService service = facebookServiceProviderCarib.getFacbookPostService();

			request.setAttribute(ATTR_OAUTH_REQUEST_TOKEN, EMPTY_TOKEN, SCOPE_SESSION);

			// redirect to facebook auth page
			System.out.print("" + service.getAuthorizationUrl(EMPTY_TOKEN));

			return "redirect:" + service.getAuthorizationUrl(EMPTY_TOKEN);
		}

		return "redirect:/facebook-callback";
	}

	@RequestMapping(value = { "/facebook-callback-caribtix" }, method = RequestMethod.GET)
	@ResponseBody
	public String callback(@RequestParam(value = "code", required = false) String oauthVerifier, WebRequest request) {

		ResponseMessage response = new ResponseMessage();

		Gson gson = new Gson();

		if (oauthVerifier == null) {

			response.setMessage("Fail");

			response.setHttpCode(Constants.ERROR_CODE);

			return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
		}

		try {

			// getting request token
			OAuthService service = facebookServiceProviderCarib.getService();

			Token requestToken = (Token) request.getAttribute(ATTR_OAUTH_REQUEST_TOKEN, SCOPE_SESSION);

			// getting access token
			Verifier verifier = new Verifier(oauthVerifier);

			Token accessToken = service.getAccessToken(requestToken, verifier);

			// store access token as a session attribute
			request.setAttribute(ATTR_OAUTH_ACCESS_TOKEN, accessToken, SCOPE_SESSION);

			// getting user profile
			OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me?fields=id,name,picture,link,email");

			service.signRequest(accessToken, oauthRequest);

			Response oauthResponse = oauthRequest.send();

			System.out.println(oauthResponse.getBody());

			FacebookInformation profileInfo = gson.fromJson(oauthResponse.getBody(), FacebookInformation.class);

			AttendeeDetail detail = null;

			if (profileInfo != null) {

				detail = new AttendeeDetail(profileInfo);

				detail.setAccessToken(accessToken.getToken());

				detail.setAccessToken(accessToken.getSecret());

			}

			response.setRecord(detail);

			response.setMessage("Success");

			response.setHttpCode(Constants.SUCCESS_CODE);
			

		}
		catch (Exception e) {

			response.setMessage("Fail");

			response.setHttpCode(Constants.ERROR_CODE);

			return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
		}

		return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
	}

	// http://codeoftheday.blogspot.com/2013/09/share-post-on-facebook-page-using.html
	// http://codeoftheday.blogspot.com/2013/09/share-post-on-facebook-wall-or-timeline.html
	// http://api-portal.anypoint.mulesoft.com/facebook/api/facebook-graph-api/docs/code
	public static void postOnFacebookWall(String accesstoken, String message, String image_URL) {

		try {

			OAuthRequest request = new OAuthRequest(Verb.POST, "https://graph.facebook.com/me/feed");

			request.addBodyParameter("message", message);

			request.addBodyParameter("link", image_URL);

			request.addBodyParameter("access_token", accesstoken);

			Response response = request.send();

			System.out.println(response.getCode());

			String responseBody = response.getBody();

			System.out.println(responseBody);

		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}
}
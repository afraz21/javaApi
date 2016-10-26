package org.iqvis.nvolv3.networking.controller;

import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_ACCESS_TOKEN;
import static org.iqvis.nvolv3.oauth.service.SessionAttributes.ATTR_OAUTH_REQUEST_TOKEN;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.SocialNetworkKeys;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.AttendeeDetail;
import org.iqvis.nvolv3.mobile.bean.TwitterInformation;
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

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import com.google.gson.Gson;

@Controller
public class TwitterController {

	@Autowired
	@Qualifier("twitterServiceProvider")
	private OAuthServiceProvider twitterServiceProvider;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@RequestMapping(value = { "/login-twitter" }, method = RequestMethod.GET)
	public String login(@RequestParam(value = "appId", required = false) String appId, @RequestParam(value = "organizerId", required = false) String organizerId, WebRequest request) {

		if (appId != null) {

			AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

			SocialNetworkKeys keys = appConfiguration.getSocialMediaKeysMyName(Constants.TWITTER);

			twitterServiceProvider = new OAuthServiceProvider(new OAuthServiceConfig(keys.getApikey(), keys.getApisecret(), keys.getCallback(), org.scribe.builder.api.TwitterApi.class));

		}

		// getting request and access token from session
		Token requestToken = null;// Token)
									// request.getAttribute(ATTR_OAUTH_REQUEST_TOKEN,
									// SCOPE_SESSION);
		// Token accessToken = null;// (Token)
		// request.getAttribute(ATTR_OAUTH_ACCESS_TOKEN,
		// SCOPE_SESSION);
		// if (requestToken == null || accessToken == null) {
		// generate new request token

		OAuthService service = twitterServiceProvider.getTwitterService();
		requestToken = service.getRequestToken();
		request.setAttribute(ATTR_OAUTH_REQUEST_TOKEN, requestToken, SCOPE_SESSION);

		// redirect to twitter auth page
		return "redirect:" + service.getAuthorizationUrl(requestToken);

		// }

	}

	@ResponseBody
	@RequestMapping(value = { "/twitter-callback" }, method = RequestMethod.GET)
	public String callback(@RequestParam(value = "appId", required = false) String appId, @RequestParam(value = "organizerId", required = false) String organizerId, @RequestParam(value = "oauth_token", required = false) String oauthToken, @RequestParam(value = "oauth_verifier", required = false) String oauthVerifier, WebRequest request) {

		ResponseMessage response = new ResponseMessage();

		if (appId != null) {

			AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

			SocialNetworkKeys keys = appConfiguration.getSocialMediaKeysMyName(Constants.TWITTER);

			twitterServiceProvider = new OAuthServiceProvider(new OAuthServiceConfig(keys.getApikey(), keys.getApisecret(), keys.getCallback(), org.scribe.builder.api.TwitterApi.class));

		}

		Gson gson = new Gson();

		if (oauthVerifier == null) {

			response.setMessage("Fail");

			response.setHttpCode(Constants.ERROR_CODE);

			return "<input type='hidden' value='" + ("" + gson.toJson(response)) + "' id='response_json' />";
		}

		try {

			// getting request token
			OAuthService service = twitterServiceProvider.getTwitterService();

			Token requestToken = (Token) request.getAttribute(ATTR_OAUTH_REQUEST_TOKEN, SCOPE_SESSION);

			// getting access token
			Verifier verifier = new Verifier(oauthVerifier);

			Token accessToken = service.getAccessToken(requestToken, verifier);

			// store access token as a session attribute
			request.setAttribute(ATTR_OAUTH_ACCESS_TOKEN, accessToken, SCOPE_SESSION);

			// getting user profile

			OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");

			service.signRequest(accessToken, oauthRequest); // the access token
															// from
															// step 4
			Response oauthResponse = oauthRequest.send();

			System.out.println(oauthResponse.getBody());

			TwitterInformation profileInfo = gson.fromJson(oauthResponse.getBody(), TwitterInformation.class);

			AttendeeDetail detail = null;

			if (profileInfo != null) {

				detail = new AttendeeDetail(profileInfo);

				detail.setAccessToken(accessToken.getToken());

				detail.setSecretToken(accessToken.getSecret());

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

	/*
	 * public void uploadImageAndText(Token accessToken, String text, String
	 * imageFileUrl) {
	 * 
	 * String tweet; try { tweet = URLEncoder.encode(text, "UTF-8");
	 * 
	 * imageFileUrl = URLEncoder.encode(imageFileUrl, "UTF-8");
	 * 
	 * tweet = tweet + imageFileUrl;
	 * 
	 * OAuthService service = twitterServiceProvider.getTwitterService();
	 * 
	 * String urlTweet =
	 * "https://api.twitter.com/1.1/statuses/update.json?status=" + tweet;
	 * 
	 * System.out.println("request: " + urlTweet);
	 * 
	 * OAuthRequest request2 = new OAuthRequest(Verb.POST, urlTweet);
	 * 
	 * service.signRequest(accessToken, request2);
	 * 
	 * System.out.println("REQUEST: " + request2.getUrl());
	 * 
	 * Response response2 = request2.send();
	 * 
	 * System.out.println(response2.getBody());
	 * 
	 * } catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
	 * block
	 * 
	 * }
	 * 
	 * }
	 */

	public void uploadImageAndText(Token accessToken, String text, String imageFileUrl, File file) {

		String tweet;
		try {
			tweet = URLEncoder.encode(text, "UTF-8");

			imageFileUrl = URLEncoder.encode(imageFileUrl, "UTF-8");

			tweet = tweet + imageFileUrl;

			OAuthService service = twitterServiceProvider.getTwitterService();

			String urlTweet = "https://api.twitter.com/1.1/statuses/update_with_media.json";

			System.out.println("request: " + urlTweet);

			OAuthRequest request2 = new OAuthRequest(Verb.POST, urlTweet);

			System.out.println("REQUEST: " + request2.getUrl());

			byte[] bytesEncoded;
			try {

				bytesEncoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));

				request2.addBodyParameter("media[]", bytesEncoded.toString());

				request2.addBodyParameter("status", text);

			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			service.signRequest(accessToken, request2);

			Response response2 = request2.send();

			System.out.println(response2.getBody());

		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block

		}

	}

	/*
	 * public void uploadImageAndText(Token accessToken, String text,
	 * MultipartFile file) {
	 * 
	 * OAuthRequest request = new OAuthRequest(Verb.POST,
	 * "https://api.twitter.com/1.1/statuses/update_with_media.json");
	 * 
	 * OAuthService service = twitterServiceProvider.getService();
	 * 
	 * MultipartEntity entity = new MultipartEntity();
	 * 
	 * try {
	 * 
	 * entity.addPart("status", new StringBody(text));
	 * 
	 * if (file != null) {
	 * 
	 * 
	 * File convFile = new File( file.getOriginalFilename());
	 * 
	 * file.transferTo(convFile);
	 * 
	 * entity.addPart("media", new FileBody(convFile));
	 * 
	 * 
	 * }
	 * 
	 * ByteArrayOutputStream out = new ByteArrayOutputStream();
	 * 
	 * entity.writeTo(out);
	 * 
	 * request.addPayload(out.toByteArray());
	 * 
	 * request.addHeader(entity.getContentType().getName(),
	 * entity.getContentType().getValue());
	 * 
	 * service.signRequest(accessToken, request);
	 * 
	 * Response response13 = request.send();
	 * 
	 * if (response13.isSuccessful()) { // you're all good
	 * 
	 * System.out.println(response13.getBody());
	 * 
	 * } else {
	 * 
	 * System.out.println(response13.getBody()); }
	 * 
	 * } catch (UnsupportedEncodingException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace(); } }
	 */

	// String tweet;

	public void uploadImageAndText(Token accesstoken, String text, String imageFileUrl, String appId, String organizerId) {
		System.out.println("-------Inside Upload To Twitter-----");
		System.out.println("-------App Id-----"+appId);
		System.out.println("-------Org Id-----"+organizerId);		
				
		AppConfiguration appConfiguration = appConfiguration_Service.getAppObject(appId);

		SocialNetworkKeys keys = appConfiguration.getSocialMediaKeysMyName(Constants.TWITTER);
		
		System.out.println("-------API Key-----"+keys.getApikey());
		System.out.println("-------API Secret-----"+keys.getApisecret());
		System.out.println("-------API Callback-----"+keys.getCallback());
		
		twitterServiceProvider = new OAuthServiceProvider(new OAuthServiceConfig(keys.getApikey(), keys.getApisecret(), keys.getCallback(), org.scribe.builder.api.TwitterApi.class));
		
		// Your Twitter App's Consumer Key
		String consumerKey = twitterServiceProvider.getConfig().getApiKey();

		// Your Twitter App's Consumer Secret
		String consumerSecret = twitterServiceProvider.getConfig().getApiSecret();
		
		System.out.println("-------Consumer Key-----"+consumerKey);
		System.out.println("-------Consumer Secret-----"+consumerSecret);

		// Your Twitter Access Token
		String accessToken = accesstoken.getToken();

		// Your Twitter Access Token Secret
		String accessTokenSecret = accesstoken.getSecret();
		
		System.out.println("-------AccessToken-----"+accessToken);
		System.out.println("-------AccessToken Secret-----"+accessTokenSecret);

		try {
			// Instantiate a re-usable and thread-safe factory
			TwitterFactory twitterFactory = new TwitterFactory();

			// Instantiate a new Twitter instance
			Twitter twitter = twitterFactory.getInstance();

			// setup OAuth Consumer Credentials
			twitter.setOAuthConsumer(consumerKey, consumerSecret);

			// setup OAuth Access Token
			twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

			// Instantiate and initialize a new twitter status update
			StatusUpdate statusUpdate = new StatusUpdate(
			// your tweet or status message
					text);

			// attach any media, if you want to
			try {

				if (!imageFileUrl.equals("")) {
					statusUpdate.setMedia(
					// title of media
							"NVOLV", new URL(imageFileUrl.replace(" ", "+") + "?imgmax=800").openStream());

				}
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// tweet or update status

			Status status = twitter.updateStatus(statusUpdate);
		}
		catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
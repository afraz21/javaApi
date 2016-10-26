package org.iqvis.nvolv3.oauth.service;

import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.service.ConstantFactoryImpl;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class OAuthServiceProvider {

	private OAuthServiceConfig config;

	public OAuthServiceProvider() {
	}

	public OAuthServiceProvider(OAuthServiceConfig config) {
		this.config = config;
	}

	public OAuthService getService() {
		System.out.println(config);
		return new ServiceBuilder().provider(config.getApiClass()).apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).callback(config.getCallback()).build();
	}

	public OAuthService getFacbookPostService() {
		System.out.println(config);
		return new ServiceBuilder().provider(config.getApiClass()).apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).callback(config.getCallback()).scope("publish_actions").scope("public_profile").scope("email").build();
	}

	public OAuthService getTwitterService() {
		System.out.println(config);
		return new ServiceBuilder().provider(TwitterApi.SSL.class).apiKey(config.getApiKey()).apiSecret(config.getApiSecret()).callback(config.getCallback()).build();
	}

	public OAuthServiceConfig getConfig() {

		return config;
	}

	public List<Status> searchTweets(Token accesstoken, String hashTags,Feed feed) {

		List<Status> tweets = new ArrayList<Status>();

		// Your Twitter App's Consumer Key
		String consumerKey = this.getConfig().getApiKey();

		// Your Twitter App's Consumer Secret
		String consumerSecret = this.getConfig().getApiSecret();
		
		// Your Twitter Access Token
		String accessToken = accesstoken.getToken();

		// Your Twitter Access Token Secret
		String accessTokenSecret = accesstoken.getSecret();

		try {
			// Instantiate a re-usable and thread-safe factory
			TwitterFactory twitterFactory = new TwitterFactory();

			// Instantiate a new Twitter instance
			Twitter twitter = twitterFactory.getInstance();

			// setup OAuth Consumer Credentials
			twitter.setOAuthConsumer(consumerKey, consumerSecret);

			// setup OAuth Access Token
			twitter.setOAuthAccessToken(new AccessToken("3220167163-GlBahYmf79Nw8AgdG28MP8Kjk3sjM9ZswgFo438", "kDorbbFk0bOeksdfZrfkTv4zB4wf8EIUzQwwXEsJb49Zz"));

			Query query = new Query(hashTags);
			
			if(feed!=null){
				
				query.sinceId(feed.getSocialMediaId());
			}
			
			query.count(ConstantFactoryImpl.TWITTER_FEED_FETCH_COUNT);
			
			QueryResult result = twitter.search(query);

			tweets.addAll(result.getTweets());

		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return tweets;
	}
}

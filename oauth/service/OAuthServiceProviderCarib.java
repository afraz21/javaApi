package org.iqvis.nvolv3.oauth.service;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.oauth.OAuthService;

public class OAuthServiceProviderCarib {
	
	private OAuthServiceConfigCarib config;
	
	public OAuthServiceProviderCarib() {
	}
	
	public OAuthServiceProviderCarib(OAuthServiceConfigCarib config) {
		this.config = config;
	}

	public OAuthService getService() {
		System.out.println(config);
		return new ServiceBuilder().provider(config.getApiClass())
							.apiKey(config.getApiKey())
						    .apiSecret(config.getApiSecret())
						    .callback(config.getCallback())
						    .build();
	}
	
	public OAuthService getFacbookPostService() {
		System.out.println(config);
		return new ServiceBuilder().provider(config.getApiClass())
							.apiKey(config.getApiKey())
						    .apiSecret(config.getApiSecret())
						    .callback(config.getCallback())
						    .scope("publish_actions")
						    .scope("public_profile")
						    .scope("email")
						    .build();
	}
	
	public OAuthService getTwitterService() {
		System.out.println(config);
		return new ServiceBuilder().provider(TwitterApi.SSL.class)
							.apiKey(config.getApiKey())
						    .apiSecret(config.getApiSecret())
						    .callback(config.getCallback())
						    .build();
	}
	
	public OAuthServiceConfigCarib getConfig(){
		
		return config;
	}
}

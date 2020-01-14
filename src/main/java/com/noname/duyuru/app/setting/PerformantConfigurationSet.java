package com.noname.duyuru.app.setting;

import com.noname.duyuru.app.jpa.models.Configuration;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class PerformantConfigurationSet implements ConfigurationSet {
    private static final Logger LOGGER = LogManager.getLogger(PerformantConfigurationSet.class);
    private final ConfigurationRepository configurationRepository;

    private boolean announcementCheckEnabled = true;
    private String hookToken;
    private String certificate;
    private boolean webhookEnabled;

    private final String botToken;
    private final long masterChatId;
    private final User master;
	private final String defaultLanguage;
	private String webHookUrl;
	private boolean cleaningEnabled;

	private final int threadCount;

	public PerformantConfigurationSet(final ConfigurationRepository configurationRepository){
		this.configurationRepository=configurationRepository;
		threadCount=Runtime.getRuntime().availableProcessors();

		final Optional<Configuration> defaultLanguageOption=configurationRepository.findById("defaultLanguage");
		final Optional<Configuration> masterChatIdOption=configurationRepository.findById("masterChatId");
		final Optional<Configuration> botTokenOption=configurationRepository.findById("botToken");

		if(masterChatIdOption.isPresent()){
			this.masterChatId=Long.valueOf(masterChatIdOption.get().getValue());
			master=new User();
			master.setId(this.masterChatId);
		}else{
			this.masterChatId=0;
			master=new User();
			LOGGER.error("'masterChatId' configuration does not exist.");
		}
		if(botTokenOption.isPresent()){
			this.botToken=botTokenOption.get().getValue();
		}else{
			throw new IllegalArgumentException("botToken is not specified.");
		}
		if(defaultLanguageOption.isPresent()){
			this.defaultLanguage=defaultLanguageOption.get().getValue();
		}else{
			this.defaultLanguage="en";
			LOGGER.info("No default language is set by 'defaultLanguage'. Using 'en' as defaultLanguage");
		}
		reload();
	}

	@Override
	public final boolean isAnnouncementCheckEnabled(){
		return announcementCheckEnabled;
	}

	@Override
	public final String getBotToken(){
		return botToken;
	}

	@Override
	public final long getMasterChatId(){
		return masterChatId;
	}

	@Override
	public final User getMaster(){
		return master;
	}

	@Override
	public final String getDefaultLanguage(){
		return defaultLanguage;
	}

	@Override
	public final void enableAnnouncementCheck(){
		announcementCheckEnabled=true;
	}

	@Override
	public final void disableAnnouncementCheck(){
		announcementCheckEnabled=false;
	}

	@Override
	public final String getWebhookUrl(){
		return webHookUrl;
	}

	@Override
	public final void setWebhookToken(final String token){
		hookToken=token;
	}

	@Override
	public final String getWebhookToken(){
		return hookToken;
	}

	@Override
	public final void setCertificate(final String certificate){
		this.certificate=certificate;
	}

	@Override
	public final String getCertificate(){
		return certificate;
	}

	@Override
	public final void reload(){
		final Optional<Configuration> webHookUrlOption=configurationRepository.findById("webhookUrl");
		final Optional<Configuration> certificateOption=configurationRepository.findById("certificate");
		final Optional<Configuration> cleanOption=configurationRepository.findById("cleanAnnouncements");

		if(webHookUrlOption.isPresent()){
			setWebhookUrl(webHookUrlOption.get().getValue());
		}else{
			setWebhookUrl("");
			setWebhookToken("");
			LOGGER.info("'webHookUrl is not set. Not using webhook at start");
		}
		if(certificateOption.isPresent()){
			setCertificate(certificateOption.get().getValue());
		}else{
			setCertificate("");
			LOGGER.info("no certificate exists in database");
		}
		if(cleanOption.isPresent()){
			this.cleaningEnabled=Boolean.parseBoolean(cleanOption.get().getValue());
		}else{
			this.cleaningEnabled=false;
			LOGGER.info("cleanAnnouncements is not set. Not cleaning by default");
		}
	}

	@Override
	public final String getType(){
		return "performant";
	}

	@Override
	public final void setWebhookUrl(final String webhookUrl){
		this.webHookUrl=webhookUrl;
	}

	@Override
	public final int getThreads(){
		return threadCount;
	}

	@Override
	public void setWebhookEnabled(boolean enabled){
		webhookEnabled=enabled;
	}

	@Override
	public boolean isWebHookEnabled(){
		return webhookEnabled;
	}

	@Override
	public void setCleaningEnabled(boolean cleaningEnabled){
		this.cleaningEnabled=cleaningEnabled;
	}

	@Override
	public boolean isCleaningEnabled(){
		return cleaningEnabled;
	}
}

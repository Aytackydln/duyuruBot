package com.noname.duyuru.app.setting;

import com.noname.duyuru.app.jpa.models.Configuration;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DynamicConfigurationSet implements ConfigurationSet {
	private final static Logger LOGGER = LogManager.getLogger(DynamicConfigurationSet.class);

	private boolean announcementCheckEnabled=true;
	private String hookToken;
	private String certificate;
	private boolean webhookEnabled;

	private String botToken;
	private long masterChatId;
	private User master;
	private String defaultLanguage;
	private String webHookUrl;
	private boolean cleaningEnabled;

	private final ConfigurationRepository configurationRepository;
	private final int threads;

	public DynamicConfigurationSet(ConfigurationRepository configurationRepository){
		this.configurationRepository=configurationRepository;
		threads=Runtime.getRuntime().availableProcessors();
		reload();
	}

	@Override
	public void reload(){
		final Optional<Configuration> defaultLanguageOption=configurationRepository.findById("defaultLanguage");
		final Optional<Configuration> masterChatIdOption=configurationRepository.findById("masterChatId");
		final Optional<Configuration> botTokenOption=configurationRepository.findById("botToken");
		final Optional<Configuration> webHookUrlOption=configurationRepository.findById("webhookUrl");
		final Optional<Configuration> certificateOption=configurationRepository.findById("certificate");
		final Optional<Configuration> cleanOption=configurationRepository.findById("cleanAnnouncements");

		if(masterChatIdOption.isPresent()){
			this.masterChatId=Long.valueOf(masterChatIdOption.get().getValue());
			master=new User();
			master.setId(this.masterChatId);
		}else{
			this.masterChatId=0;
			master=new User();
			LOGGER.error("'masterChatId' configuration does not exist.");
		}
		if (botTokenOption.isPresent()) {
			this.botToken = botTokenOption.get().getValue();
		} else {
			throw new IllegalArgumentException("botToken is not specified.");
		}
		if (defaultLanguageOption.isPresent()) {
			this.defaultLanguage = defaultLanguageOption.get().getValue();
		} else {
			this.defaultLanguage = "en";
			LOGGER.info("No default language is set by 'defaultLanguage'. Using 'en' as defaultLanguage");
		}
		if (webHookUrlOption.isPresent()) {
			this.webHookUrl = webHookUrlOption.get().getValue();
		} else {
			this.webHookUrl="";
			this.hookToken="";
			LOGGER.info("'webHookUrl is not set. Not using webhook at start");
		}
		if(certificateOption.isPresent()){
			this.certificate=certificateOption.get().getValue();
		}else{
			this.certificate="";
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
	public String getType() {
		return "dynamic";
	}

	@Override
	public void setWebhookUrl(String webhookUrl) {
		this.webHookUrl = webhookUrl;
	}

	@Override
	public int getThreads() {
		return threads;
	}

	@Override
	public boolean isAnnouncementCheckEnabled() {
		return announcementCheckEnabled;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public long getMasterChatId() {
		return masterChatId;
	}

	@Override
	public User getMaster() {
		return master;
	}

	@Override
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	@Override
	public void enableAnnouncementCheck() {
		announcementCheckEnabled = true;
	}

	@Override
	public void disableAnnouncementCheck() {
		announcementCheckEnabled = false;
	}

	@Override
	public String getWebhookUrl() {
		return webHookUrl;
	}

	@Override
	public void setWebhookToken(String token) {
		hookToken = token;
	}

	@Override
	public String getWebhookToken() {
		return hookToken;
	}

	@Override
	public void setCertificate(final String certificate) {
		this.certificate = certificate;
	}

	@Override
	public String getCertificate() {
		return certificate;
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

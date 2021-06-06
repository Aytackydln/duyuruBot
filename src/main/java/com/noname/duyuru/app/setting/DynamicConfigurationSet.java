package com.noname.duyuru.app.setting;

import com.noname.duyuru.app.jpa.models.Configuration;
import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.ConfigurationRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
@Getter
@Setter
public class DynamicConfigurationSet implements ConfigurationSet {
    private boolean announcementCheckEnabled = true;
    private String webhookToken;
    private String certificate;
    private boolean webhookEnabled;

    private long botId;
    private String botPassword;
    private long masterChatId;
    private User master;
    private String defaultLanguage;
    private String webhookUrl;
    private boolean cleaningEnabled;

    private final ConfigurationRepository configurationRepository;
    private final int threads;

    public DynamicConfigurationSet(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
        threads = Runtime.getRuntime().availableProcessors();
        reload();
    }

    @Override
    public void reload() {
        final Optional<Configuration> defaultLanguageOption = configurationRepository.findById("defaultLanguage");
        final Optional<Configuration> masterChatIdOption = configurationRepository.findById("masterChatId");
        final Optional<Configuration> botIdOption = configurationRepository.findById("botId");
        final Optional<Configuration> botPasswordOption = configurationRepository.findById("botPassword");
        final Optional<Configuration> webHookUrlOption = configurationRepository.findById("webhookUrl");
        final Optional<Configuration> certificateOption = configurationRepository.findById("certificate");
        final Optional<Configuration> cleanOption = configurationRepository.findById("cleanAnnouncements");

        if (masterChatIdOption.isPresent()) {
            this.masterChatId = Long.parseLong(masterChatIdOption.get().getValue());
            master = new User();
            master.setId(this.masterChatId);
        } else {
            this.masterChatId = 0;
            master = new User();
            LOGGER.error("'masterChatId' configuration does not exist.");
        }
        if (botIdOption.isPresent()) {
            this.botId = Long.parseLong(botIdOption.get().getValue());
        } else {
            throw new IllegalArgumentException("botId is not specified.");
        }
        if (botPasswordOption.isPresent()) {
            this.botPassword = botPasswordOption.get().getValue();
        } else {
            throw new IllegalArgumentException("botPassword is not specified.");
        }
        if (defaultLanguageOption.isPresent()) {
            this.defaultLanguage = defaultLanguageOption.get().getValue();
        } else {
            this.defaultLanguage = "en";
            LOGGER.info("No default language is set by 'defaultLanguage'. Using 'en' as defaultLanguage");
        }
        if (webHookUrlOption.isPresent()) {
            this.webhookUrl = webHookUrlOption.get().getValue();
        } else {
            this.webhookUrl = "";
            this.webhookToken = "";
            LOGGER.info("'webHookUrl is not set. Not using webhook at start");
        }
        if (certificateOption.isPresent()) {
            this.certificate = certificateOption.get().getValue();
        } else {
            this.certificate = "";
            LOGGER.info("no certificate exists in database");
        }
        if (cleanOption.isPresent()) {
            this.cleaningEnabled = Boolean.parseBoolean(cleanOption.get().getValue());
        } else {
            this.cleaningEnabled = false;
            LOGGER.info("cleanAnnouncements is not set. Not cleaning by default");
        }
    }

    @Override
    public String getBotToken() {
        return getBotId() + ':' + getBotPassword();
    }

    @Override
    public String getType() {
        return "dynamic";
    }

    @Override
    public void enableAnnouncementCheck() {
        announcementCheckEnabled = true;
    }

    @Override
    public void disableAnnouncementCheck() {
        announcementCheckEnabled = false;
    }
}

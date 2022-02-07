package aytackydln.duyuru.setting;

import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.models.ConfigurationEntity;
import aytackydln.duyuru.jpa.repository.ConfigurationRepository;
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
    private UserEntity master;
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
        final Optional<ConfigurationEntity> defaultLanguageOption = configurationRepository.findById("defaultLanguage");
        final Optional<ConfigurationEntity> masterChatIdOption = configurationRepository.findById("masterChatId");
        final Optional<ConfigurationEntity> botIdOption = configurationRepository.findById("botId");
        final Optional<ConfigurationEntity> botPasswordOption = configurationRepository.findById("botPassword");
        final Optional<ConfigurationEntity> webHookUrlOption = configurationRepository.findById("webhookUrl");
        final Optional<ConfigurationEntity> certificateOption = configurationRepository.findById("certificate");
        final Optional<ConfigurationEntity> cleanOption = configurationRepository.findById("cleanAnnouncements");

        if (masterChatIdOption.isPresent()) {
            this.masterChatId = Long.parseLong(masterChatIdOption.get().getValue());
            master = new UserEntity();
            master.setId(this.masterChatId);
        } else {
            this.masterChatId = 0;
            master = new UserEntity();
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

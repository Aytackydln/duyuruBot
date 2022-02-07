package aytackydln.duyuru.setting;

import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.jpa.models.ConfigurationEntity;
import aytackydln.duyuru.jpa.repository.ConfigurationRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Getter
@Setter
@Log4j2
public class PerformantConfigurationSet implements ConfigurationSet {
    private final ConfigurationRepository configurationRepository;

    private boolean announcementCheckEnabled = true;
    private String webhookToken;
    private String certificate;
    private boolean webhookEnabled;

    private final long botId;
    private final String botPassword;
    private final long masterChatId;
    private final UserEntity master;
    private final String defaultLanguage;
    private String webhookUrl;
    private boolean cleaningEnabled;

    private final int threads;

    public PerformantConfigurationSet(final ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
        threads = Runtime.getRuntime().availableProcessors();

        final Optional<ConfigurationEntity> defaultLanguageOption = configurationRepository.findById("defaultLanguage");
        final Optional<ConfigurationEntity> masterChatIdOption = configurationRepository.findById("masterChatId");
        final Optional<ConfigurationEntity> botIdOption = configurationRepository.findById("botId");
        final Optional<ConfigurationEntity> botPasswordOption = configurationRepository.findById("botPassword");

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
        reload();
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
	public final void reload() {
        final Optional<ConfigurationEntity> webHookUrlOption = configurationRepository.findById("webhookUrl");
        final Optional<ConfigurationEntity> certificateOption = configurationRepository.findById("certificate");
        final Optional<ConfigurationEntity> cleanOption = configurationRepository.findById("cleanAnnouncements");

        if (webHookUrlOption.isPresent()) {
            setWebhookUrl(webHookUrlOption.get().getValue());
        } else {
            setWebhookUrl("");
            setWebhookToken("");
            LOGGER.info("'webHookUrl is not set. Not using webhook at start");
        }
        if (certificateOption.isPresent()) {
            setCertificate(certificateOption.get().getValue());
        } else {
            setCertificate("");
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
        return botId + ":" + botPassword;
    }

    @Override
    public final String getType() {
        return "performant";
    }
}

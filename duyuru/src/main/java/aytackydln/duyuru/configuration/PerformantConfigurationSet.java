package aytackydln.duyuru.configuration;

import aytackydln.duyuru.configuration.port.ConfigurationPort;
import aytackydln.duyuru.subscriber.Subscriber;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Getter
@Setter
@Slf4j
public class PerformantConfigurationSet implements ConfigurationSet {
    private final ConfigurationPort configurationPort;

    private boolean announcementCheckEnabled = true;
    private String webhookToken;
    private String certificate;
    private boolean webhookEnabled;

    private final long botId;
    private final String botPassword;
    private final long masterChatId;
    private final Subscriber master;
    private final String defaultLanguage;
    private String webhookUrl;
    private boolean cleaningEnabled;

    private final int threads;

    public PerformantConfigurationSet(ConfigurationPort configurationPort) {
        this.configurationPort = configurationPort;
        threads = Runtime.getRuntime().availableProcessors();

        Optional<String> defaultLanguageOption = configurationPort.find("defaultLanguage");
        Optional<String> masterChatIdOption = configurationPort.find("masterChatId");
        Optional<String> botIdOption = configurationPort.find("botId");
        Optional<String> botPasswordOption = configurationPort.find("botPassword");

        if (masterChatIdOption.isPresent()) {
            this.masterChatId = Long.parseLong(masterChatIdOption.get());
            master = new Subscriber();
            master.setId(this.masterChatId);
        } else {
            this.masterChatId = 0;
            master = new Subscriber();
            LOGGER.error("'masterChatId' configuration does not exist.");
        }
        if (botIdOption.isPresent()) {
            this.botId = Long.parseLong(botIdOption.get());
        } else {
            throw new IllegalArgumentException("botId is not specified.");
        }
        if (botPasswordOption.isPresent()) {
            this.botPassword = botPasswordOption.get();
        } else {
            throw new IllegalArgumentException("botPassword is not specified.");
        }
        if (defaultLanguageOption.isPresent()) {
            this.defaultLanguage = defaultLanguageOption.get();
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
        Optional<String> webHookUrlOption = configurationPort.find("webhookUrl");
        Optional<String> certificateOption = configurationPort.find("certificate");
        Optional<String> cleanOption = configurationPort.find("cleanAnnouncements");

        if (webHookUrlOption.isPresent()) {
            setWebhookUrl(webHookUrlOption.get());
        } else {
            setWebhookUrl("");
            setWebhookToken("");
            LOGGER.info("'webHookUrl is not set. Not using webhook at start");
        }
        if (certificateOption.isPresent()) {
            setCertificate(certificateOption.get());
        } else {
            setCertificate("");
            LOGGER.info("no certificate exists in database");
        }
        if (cleanOption.isPresent()) {
            this.cleaningEnabled = Boolean.parseBoolean(cleanOption.get());
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

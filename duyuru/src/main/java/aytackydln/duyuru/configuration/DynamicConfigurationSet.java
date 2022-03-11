package aytackydln.duyuru.configuration;

import aytackydln.duyuru.configuration.port.ConfigurationPort;
import aytackydln.duyuru.subscriber.Subscriber;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
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
    private Subscriber master;
    private String defaultLanguage;
    private String webhookUrl;
    private boolean cleaningEnabled;

    private final ConfigurationPort configurationPort;
    private final int threads;

    public DynamicConfigurationSet(ConfigurationPort configurationPort) {
        this.configurationPort = configurationPort;
        threads = Runtime.getRuntime().availableProcessors();
        reload();
    }

    @Override
    public void reload() {
        Optional<String> defaultLanguageOption = configurationPort.find("defaultLanguage");
        Optional<String> masterChatIdOption = configurationPort.find("masterChatId");
        Optional<String> botIdOption = configurationPort.find("botId");
        Optional<String> botPasswordOption = configurationPort.find("botPassword");
        Optional<String> webHookUrlOption = configurationPort.find("webhookUrl");
        Optional<String> certificateOption = configurationPort.find("certificate");
        Optional<String> cleanOption = configurationPort.find("cleanAnnouncements");

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
        if (webHookUrlOption.isPresent()) {
            this.webhookUrl = webHookUrlOption.get();
        } else {
            this.webhookUrl = "";
            this.webhookToken = "";
            LOGGER.info("'webHookUrl is not set. Not using webhook at start");
        }
        if (certificateOption.isPresent()) {
            this.certificate = certificateOption.get();
        } else {
            this.certificate = "";
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

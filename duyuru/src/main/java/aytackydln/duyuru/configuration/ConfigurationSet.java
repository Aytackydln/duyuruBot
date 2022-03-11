package aytackydln.duyuru.configuration;

import aytackydln.duyuru.subscriber.Subscriber;

public interface ConfigurationSet {
	void reload();

	int getThreads();

	boolean isAnnouncementCheckEnabled();

	long getBotId();

	String getBotPassword();

	String getBotToken();

	long getMasterChatId();

	Subscriber getMaster();

	String getDefaultLanguage();

	void enableAnnouncementCheck();

	void disableAnnouncementCheck();

	String getType();

	void setWebhookUrl(String webhookUrl);

	String getWebhookUrl();

	void setWebhookToken(String token);

	String getWebhookToken();

	void setCertificate(String certificate);

	String getCertificate();

	void setWebhookEnabled(boolean enabled);

	boolean isWebhookEnabled();

	void setCleaningEnabled(boolean enabled);

	boolean isCleaningEnabled();
}

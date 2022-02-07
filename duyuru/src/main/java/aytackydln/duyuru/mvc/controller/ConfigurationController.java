package aytackydln.duyuru.mvc.controller;

import aytackydln.duyuru.component.VersionKeeper;
import aytackydln.duyuru.service.ConfigurationService;
import aytackydln.duyuru.service.telegram.PollingService;
import aytackydln.duyuru.setting.ConfigurationSet;
import aytackydln.duyuru.mvc.message.MessageBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
@RequiredArgsConstructor
public class ConfigurationController {
	public static final String HOME_URL = "/";
	public static final String CONFIGURATION_URL = "/configuration";

	private final ConfigurationSet configurationSet;
	private final ConfigurationService configurationService;
	private final PollingService pollingService;
	private final MessageBox messageBox;

	@GetMapping(HOME_URL)
	public ModelAndView index(VersionKeeper versionKeeper) {
		var mv = new ModelAndView("index");
		mv.addObject("configuration", configurationSet);
		mv.addObject("versionKeeper", versionKeeper);
		return mv;
	}

	@GetMapping(CONFIGURATION_URL)
	public ModelAndView configuration() {
		var mv = new ModelAndView("configuration");
		mv.addObject("configuration", configurationSet);
		return mv;
	}

	@PostMapping(CONFIGURATION_URL)
	public String configure(final String botToken, final String masterChatId,
							final String defaultLanguage, final String webhookUrl,
							final String configurationType, final String certificate,
							final String webhookToken, final String announcementCleaning) {
		List<String> updated = new ArrayList<>();
		List<String> needsRestart = new ArrayList<>();

		final boolean performant = configurationSet.getType().equals("performant");
		if (!botToken.equals("")) {
			configurationService.set("botToken", botToken);
			needsRestart.add("botToken");
		}
		if(!masterChatId.equals("")){
			configurationService.set("masterChatId", masterChatId);
			if (performant)
				needsRestart.add("masterChatId");
			else
				updated.add("masterChatId");
		}
		if (!defaultLanguage.equals("")) {
			configurationService.set("defaultLanguage", defaultLanguage);
			if (performant)
				needsRestart.add("defaultLanguage");
			else
				updated.add("defaultLanguage");
		}
		if (!configurationType.equals("")) {
			configurationService.set("configurationType", configurationType);
			if (performant)
				needsRestart.add("configurationType");
			else
				updated.add("configurationType");
		}
		if (!webhookUrl.equals(configurationSet.getWebhookUrl())) {
			configurationService.set("webhookUrl", webhookUrl);
			updated.add("webhookUrl");
		}
		if (!certificate.equals("")) {
			configurationService.set("certificate", certificate);
			updated.add("certificate");
		}
		if (!webhookToken.equals("")) {
			configurationSet.setWebhookToken(webhookToken);
			updated.add("webhookToken");
		}
		if(!announcementCleaning.equals("")) {
			if ("true".equals(announcementCleaning)) {
				configurationSet.setCleaningEnabled(true);
			} else if ("false".equals(announcementCleaning)) {
				configurationSet.setCleaningEnabled(false);
			}
			updated.add("announcementCleaning");
		}

		if (!updated.isEmpty()) {
			configurationSet.reload();
			messageBox.addSuccess(String.join(", ", updated) + " have been reloaded");
		}
		if (!needsRestart.isEmpty()) {
			messageBox.addInfo(String.join(", ", needsRestart) + " will be updated on restart because using performant configuration");
		}

		return REDIRECT_URL_PREFIX + CONFIGURATION_URL;
	}

	@GetMapping("/disableAnnouncements")
	public String disableAnnouncements() {
		configurationSet.disableAnnouncementCheck();
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("/enableAnnouncements")
	public String enableAnnouncements() {
		configurationSet.enableAnnouncementCheck();
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("deleteWebhook")
	public final String deleteWebhook() throws InterruptedException {
		pollingService.deleteWebhook();
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("setWebhook")
	public final String setWebhook() {
		messageBox.add(pollingService.createWebhook());
		return REDIRECT_URL_PREFIX + HOME_URL;
	}
}

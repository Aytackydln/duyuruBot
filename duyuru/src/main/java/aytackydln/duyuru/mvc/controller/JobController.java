package aytackydln.duyuru.mvc.controller;

import aytackydln.duyuru.service.AnnouncementService;
import aytackydln.duyuru.service.SubscriptionService;
import aytackydln.duyuru.service.TopicUpdater;
import aytackydln.duyuru.service.dictionary.DictionaryKeeper;
import aytackydln.duyuru.mvc.message.MessageBox;
import aytackydln.duyuru.mvc.message.ViewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

import static aytackydln.duyuru.mvc.controller.ConfigurationController.HOME_URL;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

@Controller
@RequiredArgsConstructor
public class JobController {
	private final TopicUpdater topicUpdater;
	private final MessageBox messageBox;
	private final DictionaryKeeper dictionaryKeeper;
	private final AnnouncementService announcementService;
	private final SubscriptionService subscriptionService;

	@GetMapping("/updateTopics")
	public String updateTopics() {
		Collection<ViewMessage> result = topicUpdater.updateTopics();
		messageBox.addInfo("Update completed");
		messageBox.addAll(result);

		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("/updateDictionary")
	public String updateTranslations() {
		dictionaryKeeper.updateTranslations();
		messageBox.addSuccess("Updated translations");
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("/clearAnnouncements")
	public String clearAnnouncements() {
		messageBox.add(announcementService.clearAnnouncements());
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("triggerCheck")
	public String triggerCheck() {
		subscriptionService.checkNewAnnouncements();
		messageBox.addSuccess("Announcement check triggered");
		return REDIRECT_URL_PREFIX + HOME_URL;
	}
}

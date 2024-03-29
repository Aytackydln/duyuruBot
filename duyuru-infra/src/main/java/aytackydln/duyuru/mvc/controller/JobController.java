package aytackydln.duyuru.mvc.controller;

import aytackydln.duyuru.announcement.AnnouncementFacade;
import aytackydln.duyuru.mvc.message.InfoMessage;
import aytackydln.duyuru.mvc.message.MessageBox;
import aytackydln.duyuru.mvc.message.ViewMessage;
import aytackydln.duyuru.topic.TopicUpdater;
import aytackydln.duyuru.dictionary.DictionaryKeeper;
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
	private final AnnouncementFacade announcementService;

	@GetMapping("/updateTopics")
	public String updateTopics() {
		Collection<ViewMessage> result = topicUpdater.updateTopics().stream().map(
				(s -> (ViewMessage)new InfoMessage(s))
		).toList();
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
		Collection<? extends ViewMessage> announcementResults = announcementService.clearAnnouncements()
				.stream()
				.map(InfoMessage::new)
				.toList();
		messageBox.addAll(announcementResults);
		return REDIRECT_URL_PREFIX + HOME_URL;
	}

	@GetMapping("/triggerCheck")
	public String triggerCheck() {
		announcementService.checkNewAnnouncements();
		messageBox.addSuccess("Announcement check triggered");
		return REDIRECT_URL_PREFIX + HOME_URL;
	}
}

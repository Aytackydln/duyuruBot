package com.noname.duyuru.app.mvc.controller;

import com.noname.duyuru.app.mvc.message.IViewMessage;
import com.noname.duyuru.app.mvc.message.MessageBox;
import com.noname.duyuru.app.service.AnnouncementService;
import com.noname.duyuru.app.service.SubscriptionService;
import com.noname.duyuru.app.service.TopicUpdater;
import com.noname.duyuru.app.service.dictionary.DictionaryKeeper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

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
		Collection<IViewMessage> result=topicUpdater.updateTopics();
		messageBox.addInfo("Update completed");
		messageBox.addAll(result);

		return "redirect:/";
	}

	@GetMapping("/updateDictionary")
	public String updateTranslations() {
		dictionaryKeeper.updateTranslations();
		messageBox.addSuccess("Updated translations");
		return "redirect:/";
	}

	@GetMapping("/clearAnnouncements")
	public String clearAnnouncements(){
			messageBox.add(announcementService.clearAnnouncements());
		return "redirect:/";
	}

	@GetMapping("triggerCheck")
	public String triggerCheck() {
		subscriptionService.checkNewAnnouncements();
		messageBox.addSuccess("Announcement check triggered");
		return "redirect:/";
	}
}

package com.noname.duyuru.app.mvc.controller;

import com.noname.duyuru.app.component.VersionKeeper;
import com.noname.duyuru.app.mvc.message.MessageBox;
import com.noname.duyuru.app.service.ConfigurationService;
import com.noname.duyuru.app.service.PollingService;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConfigurationController {
	private final ConfigurationSet configurationSet;
	private final ConfigurationService configurationService;
	private final PollingService pollingService;
	private final MessageBox messageBox;

	public ConfigurationController(ConfigurationSet configurationSet, ConfigurationService configurationService,
			PollingService pollingService, MessageBox messageBox) {
		this.configurationSet = configurationSet;
		this.configurationService = configurationService;
		this.pollingService = pollingService;
		this.messageBox = messageBox;
	}

	@GetMapping("/")
	public ModelAndView index(VersionKeeper versionKeeper) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("configuration", configurationSet);
		mv.addObject("versionKeeper", versionKeeper);
		return mv;
	}

	@GetMapping("/configuration")
	public ModelAndView configuration() {
		ModelAndView mv = new ModelAndView("configuration");
		mv.addObject("configuration", configurationSet);
		return mv;
	}

	@PostMapping("/configuration")
	public String configure(final String botToken, final String masterChatId,
							final String defaultLanguage, final String webhookUrl,
							final String configurationType, final String certificate,
							final String webhookToken, final String announcementCleaning){
		List<String> updated=new ArrayList<>();
		List<String> needsRestart=new ArrayList<>();

		final boolean performant=configurationSet.getType().equals("performant");
		if(!botToken.equals("")){
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
		if (!webhookUrl.equals("")) {
			configurationService.set("webhookUrl", webhookUrl);
			updated.add("webhookUrl");
		}
		if(!certificate.equals("")){
			configurationService.set("certificate", certificate);
			updated.add("certificate");
		}
		if(!webhookToken.equals("")){
			configurationSet.setWebhookToken(webhookToken);
			updated.add("webhookToken");
		}
		if(!announcementCleaning.equals("")){
			switch(announcementCleaning){
				case "true":
					configurationSet.setCleaningEnabled(true);
					break;
				case "false":
					configurationSet.setCleaningEnabled(false);
					break;
			}
			updated.add("announcementCleaning");
		}

		if(!updated.isEmpty()){
			configurationSet.reload();
			messageBox.addSuccess(String.join(", ", updated)+" have been reloaded");
		}
		if(!needsRestart.isEmpty()){
			messageBox.addInfo(String.join(", ", needsRestart)+" will be updated on restart because using performant configuration");
		}

		return "redirect:/configuration";
	}

	@GetMapping("/disableAnnouncements")
	public String disableAnnouncements() {
		configurationSet.disableAnnouncementCheck();
		return "redirect:/";
	}

	@GetMapping("/enableAnnouncements")
	public String enableAnnouncements() {
		configurationSet.enableAnnouncementCheck();
		return "redirect:/";
	}

	@GetMapping("deleteWebhook")
	public final String deleteWebhook() throws InterruptedException {
		pollingService.deleteWebhook();
		return "redirect:/";
	}

	@GetMapping("setWebhook")
	public final String setWebhook() {
		messageBox.add(pollingService.createWebhook());
		return "redirect:/";
	}
}

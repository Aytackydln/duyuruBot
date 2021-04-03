package com.noname.duyuru.app.mvc.controller;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.jpa.repositories.MessageRepository;
import com.noname.duyuru.app.service.AnnouncementService;
import com.noname.duyuru.app.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class StatisticsController {
	private final MessageRepository messageRepository;
	private final AnnouncementService announcementService;
	private final SubscriptionService subscriptionService;

	@GetMapping("/messages")
	public ModelAndView messages(final @PageableDefault(sort = "time", direction = Sort.Direction.DESC, size = 13) Pageable pageable) {
		final ModelAndView mv = new ModelAndView("messages");
		mv.addObject("messagePage", messageRepository.findAll(pageable));
		return mv;
	}

	@GetMapping("/subscriptions")
	public ModelAndView subscriptions(final @PageableDefault(size = 9) Pageable pageable) {
		final ModelAndView mv = new ModelAndView("subscriptions");
		Page<User> list = subscriptionService.getUsersWithSubscriptions(pageable);
		mv.addObject("usersPage", list);
		return mv;
	}

	@GetMapping("/statistics")
	public ModelAndView statistics(){
		return new ModelAndView("statistics");
	}

	@GetMapping("/announcements")
	public ModelAndView announcements(final @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable page) {
		final ModelAndView mv = new ModelAndView("announcements");
		mv.addObject("announcementList", announcementService.getAnnouncements(page));
		return mv;
	}
}

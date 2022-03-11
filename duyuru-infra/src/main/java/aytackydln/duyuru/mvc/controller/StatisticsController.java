package aytackydln.duyuru.mvc.controller;

import aytackydln.duyuru.jpa.repository.AnnouncementRepository;
import aytackydln.duyuru.jpa.repository.MessageRepository;
import aytackydln.duyuru.jpa.repository.UserRepository;
import aytackydln.duyuru.jpa.entity.UserEntity;
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
	//CRUD
	private final MessageRepository messageRepository;
	private final AnnouncementRepository announcementRepository;
	private final UserRepository userRepository;

	@GetMapping("/messages")
	public ModelAndView messages(final @PageableDefault(sort = "time", direction = Sort.Direction.DESC, size = 13) Pageable pageable) {
		var mv = new ModelAndView("messages");
		mv.addObject("messagePage", messageRepository.findAll(pageable));
		return mv;
	}

	@GetMapping("/subscriptions")
	public ModelAndView subscriptions(final @PageableDefault(size = 9) Pageable pageable) {
		var mv = new ModelAndView("subscriptions");
		Page<UserEntity> list = userRepository.getUserWithSubscriptions(pageable);
		mv.addObject("usersPage", list);
		return mv;
	}

	@GetMapping("/statistics")
	public ModelAndView statistics(){
		return new ModelAndView("statistics");
	}

	@GetMapping("/announcements")
	public ModelAndView announcements(final @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable page) {
		var mv = new ModelAndView("announcements");
		mv.addObject("announcementList", announcementRepository.findAll(page));
		return mv;
	}
}

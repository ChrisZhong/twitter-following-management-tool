package io.github.chriszhong.twitter;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private final Twitter twitter;
	private final ConnectionRepository connectionRepository;

	@Inject
	HomeController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String start(Model model, HttpSession httpSession) {
		String id = httpSession.getId();
		logger.debug("Session Id: {}", id);
		logger.debug("Connection Repository: {}", connectionRepository.toString());
		Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
		if (connection == null) {
			logger.debug("No connection established, redirecting ...");
			return "redirect:/connect/twitter";
		}
		logger.debug("Connection's display mame: {}", connection.getDisplayName());
		logger.debug("Connection's image Url: {}", connection.getImageUrl());
		logger.debug("Connection's key: {}", connection.getKey());
		logger.debug("Connection's profile Url: {}", connection.getProfileUrl());
		model.addAttribute(twitter.userOperations().getUserProfile());
		CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
		model.addAttribute("friends", friends);
		model.addAttribute("previous", friends.getPreviousCursor());
		model.addAttribute("next", friends.getNextCursor());
		return "home";
	}

}

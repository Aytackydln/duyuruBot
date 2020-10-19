package com.noname.duyuru.app.rest.controller;

import com.noname.duyuru.app.json.models.Update;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.service.CommandProcessor;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramController {
	private static final Logger LOGGER = LogManager.getLogger(TelegramController.class);

	private final CommandProcessor commandProcessor;
	private final ConfigurationSet configurationSet;

	public TelegramController(final CommandProcessor commandProcessor, final ConfigurationSet configurationSet) {
		this.commandProcessor = commandProcessor;
		this.configurationSet = configurationSet;
	}

    @PostMapping("webhook/{token}")
    public final ResponseEntity<TelegramResponse> processWebhook(@RequestBody final Update update,
                                                                 @PathVariable("token") final String token) {
        try {
            if (token.equals(configurationSet.getWebhookToken())) {
                final TelegramResponse result = commandProcessor.processUpdate(update);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.info("received invalid webhook connection on: {}", token);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            LOGGER.error(e);
			return null;
		}
	}
}
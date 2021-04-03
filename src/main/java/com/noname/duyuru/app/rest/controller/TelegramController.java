package com.noname.duyuru.app.rest.controller;

import com.noname.duyuru.app.json.models.Update;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.service.telegram.CommandProcessor;
import com.noname.duyuru.app.setting.ConfigurationSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class TelegramController {
	private final CommandProcessor commandProcessor;
	private final ConfigurationSet configurationSet;

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
            LOGGER.error("Error processing webhook", e);
			return null;
		}
	}
}
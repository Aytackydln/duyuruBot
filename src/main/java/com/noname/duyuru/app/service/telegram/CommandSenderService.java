package com.noname.duyuru.app.service.telegram;

import com.noname.duyuru.app.configuration.TelegramClientConfig;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommandSenderService {

    private final RestTemplate telegramClient;
    private final CommandObserverService commandObserverService;

    @Async(TelegramClientConfig.LIMITED_COMMAND_SENDER)
    void sendLimitedResponse(TelegramResponse responseToSend) {
        LOGGER.debug("sending limited telegram command");
        submitResponse(responseToSend);
    }

    @Async
    void submitResponseAsync(TelegramResponse responseToSend) {
        submitResponse(responseToSend);
    }

    private void submitResponse(TelegramResponse responseToSend) {
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        responseToSend.preSend();

        final HttpEntity<TelegramResponse> request = new HttpEntity<>(responseToSend, headers);
        try {
            telegramClient.postForEntity("/" + responseToSend.getMethod(), request, String.class);
        } catch (Exception e) {
            TelegramResponse errorResponse = responseToSend.onError(e);
            if (errorResponse != null)
                commandObserverService.addCommand(errorResponse);
        }
    }
}

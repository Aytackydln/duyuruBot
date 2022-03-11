package aytackydln.duyuru.adapter.telegram;

import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import aytackydln.chattools.telegram.exception.TelegramException;
import aytackydln.duyuru.configuration.TelegramClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
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
            telegramClient.postForLocation("/" + responseToSend.getMethod(), request);
        } catch (HttpClientErrorException e) {
            TelegramException exception = new TelegramException();
            TelegramResponse errorResponse = responseToSend.onError(exception);
            if (errorResponse != null)
                commandObserverService.addCommand(errorResponse);
        } catch (ResourceAccessException e) {
            try {
                Thread.sleep(10000);    //TODO handle infinite loop?
            } catch (InterruptedException ignored) {
            }
            commandObserverService.addCommand(responseToSend);
        }
    }
}

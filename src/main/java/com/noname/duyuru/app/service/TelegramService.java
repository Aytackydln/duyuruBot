package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.json.telegram.response.DeleteMessage;
import com.noname.duyuru.app.json.telegram.response.SendMessage;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

// @DependsOn telegramClientConfig so this bean is shut down before message executor.
@Service
@DependsOn("telegramClientConfig")
public class TelegramService implements DisposableBean {
    private static final Logger LOGGER = LogManager.getLogger(TelegramService.class);

    private final TelegramCommandSenderService telegramCommandSenderService;

    private final ConfigurationSet configurationSet;

    public TelegramService(TelegramCommandSenderService telegramCommandSenderService, ConfigurationSet configurationSet) {
        this.telegramCommandSenderService = telegramCommandSenderService;
        this.configurationSet = configurationSet;

        sendMessageToMaster("I have woken up, master");
    }

    void sendCommand(TelegramResponse response) {
        try {
            if (response.isLimited()) {
                try {
                    telegramCommandSenderService.sendLimitedResponse(response);
                } catch (InterruptedException ignored) {
                }
            } else {
                telegramCommandSenderService.submitResponseAsync(response);
            }
        } catch (final HttpClientErrorException e) {
            //TODO 403'te subları sil
            TelegramResponse failResponse = response.onError(e);
            if (failResponse != null)
                sendCommand(failResponse);
        } catch (final ResourceAccessException e) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
            sendCommand(response);    //TODO exeqtor queue full iken ne olur?
        } catch (Exception e) {
            LOGGER.error("Unhandled exception", e);
            sendMessageToMaster("unknown error while sending response: " + response.toString());
        }
    }

    public void destroy() {
        sendMessageToMaster("Goodbye, master...");
    }

    void sendMessage(final User user, final String message) {
        final SendMessage sendMessage = new SendMessage(user.getId(), message);
        sendCommand(sendMessage);
    }

    void sendMessageToMaster(final String message) {
        sendMessage(configurationSet.getMaster(), message);
    }

    void deleteMessage(final User user, final long messageId) {
        telegramCommandSenderService.submitResponseAsync(new DeleteMessage(user.getId(), messageId));
    }
}

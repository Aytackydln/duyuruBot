package com.noname.duyuru.app.service.telegram;

import com.noname.duyuru.app.jpa.models.User;
import com.noname.duyuru.app.json.telegram.response.DeleteMessage;
import com.noname.duyuru.app.json.telegram.response.SendMessage;
import com.noname.duyuru.app.json.telegram.response.TelegramResponse;
import com.noname.duyuru.app.setting.ConfigurationSet;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("telegramClientConfig") //so this bean is shut down before message executor.
public class TelegramService implements DisposableBean {

    private final CommandSenderService commandSenderService;
    private final ConfigurationSet configurationSet;

    public TelegramService(CommandSenderService commandSenderService, ConfigurationSet configurationSet, CommandObserverService commandObserverService) {
        this.commandSenderService = commandSenderService;
        this.configurationSet = configurationSet;

        commandObserverService.onCommandAdded(this::sendCommand);

        sendMessageToMaster("I have woken up, master");
    }

    void sendCommand(TelegramResponse response) {
        if (response.isLimited()) {
            commandSenderService.sendLimitedResponse(response);
        } else {
            commandSenderService.submitResponseAsync(response);
        }
    }

    public void destroy() {
        sendMessageToMaster("Goodbye, master...");
    }

    public void sendMessage(final User user, final String message) {
        final var sendMessage = new SendMessage(user.getId(), message);
        sendCommand(sendMessage);
    }

    public void sendMessageToMaster(final String message) {
        sendMessage(configurationSet.getMaster(), message);
    }

    void deleteMessage(final User user, final long messageId) {
        commandSenderService.submitResponseAsync(new DeleteMessage(user.getId(), messageId));
    }
}

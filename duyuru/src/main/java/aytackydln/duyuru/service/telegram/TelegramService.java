package aytackydln.duyuru.service.telegram;

import aytackydln.duyuru.jpa.models.UserEntity;
import aytackydln.duyuru.setting.ConfigurationSet;
import aytackydln.chattools.telegram.dto.response.DeleteMessage;
import aytackydln.chattools.telegram.dto.response.SendMessage;
import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
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

    void sendCommand(@Nullable TelegramResponse response) {
        if (response != null) {
            if (response.isLimited()) {
                commandSenderService.sendLimitedResponse(response);
            } else {
                commandSenderService.submitResponseAsync(response);
            }
        }
    }

    public void destroy() {
        sendMessageToMaster("Goodbye, master...");
    }

    public void sendMessage(final UserEntity userEntity, final String message) {
        final var sendMessage = new SendMessage(userEntity.getId(), message);
        sendCommand(sendMessage);
    }

    public void sendMessageToMaster(final String message) {
        sendMessage(configurationSet.getMaster(), message);
    }

    void deleteMessage(final UserEntity userEntity, final long messageId) {
        commandSenderService.submitResponseAsync(new DeleteMessage(userEntity.getId(), messageId));
    }
}

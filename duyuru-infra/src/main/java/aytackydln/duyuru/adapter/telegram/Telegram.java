package aytackydln.duyuru.adapter.telegram;

import aytackydln.chattools.telegram.dto.response.DeleteMessage;
import aytackydln.chattools.telegram.dto.response.SendMessage;
import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import aytackydln.duyuru.chatplatform.port.TelegramPort;
import aytackydln.duyuru.subscriber.Subscriber;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@DependsOn("telegramClientConfig") //so this bean is shut down before message executor.
public class Telegram implements TelegramPort {

    private final CommandSenderService commandSenderService;

    public Telegram(CommandSenderService commandSenderService, CommandObserverService commandObserverService) {
        this.commandSenderService = commandSenderService;

        commandObserverService.onCommandAdded(this::sendCommand);
    }

    @Override
    public void sendCommand(@Nullable TelegramResponse response) {
        if (response != null) {
            if (response.isLimited()) {
                commandSenderService.sendLimitedResponse(response);
            } else {
                commandSenderService.submitResponseAsync(response);
            }
        }
    }

    @Override
    public void sendMessage(final Subscriber subscriber, final String message) {
        final var sendMessage = new SendMessage(subscriber.getId(), message);
        sendCommand(sendMessage);
    }

    @Override
    public void deleteMessage(final Subscriber subscriber, final long messageId) {
        commandSenderService.submitResponseAsync(new DeleteMessage(subscriber.getId(), messageId));
    }
}

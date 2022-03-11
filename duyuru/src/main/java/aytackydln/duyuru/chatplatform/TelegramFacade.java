package aytackydln.duyuru.chatplatform;

import aytackydln.duyuru.chatplatform.port.TelegramPort;
import aytackydln.duyuru.common.semantic.DomainComponent;
import aytackydln.duyuru.configuration.port.ConfigurationPort;
import aytackydln.duyuru.subscriber.Subscriber;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainComponent
public class TelegramFacade {
    private final ConfigurationPort configurationPort;
    private final TelegramPort telegramPort;

    public void sendMessageToMaster(final String message) {
        String masterId = configurationPort.get("master");

        var masterUser = new Subscriber();
        masterUser.setId(Long.parseLong(masterId));

        telegramPort.sendMessage(masterUser, message);
    }

    public void onStart(){  //TODO interface
        sendMessageToMaster("I have woken up, master");
    }

    public void onShutdown(){  //TODO interface
        sendMessageToMaster("Goodbye, master...");
    }
}

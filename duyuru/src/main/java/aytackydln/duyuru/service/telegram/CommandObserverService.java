package aytackydln.duyuru.service.telegram;

import aytackydln.chattools.telegram.dto.response.TelegramResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class CommandObserverService {
    private final List<Consumer<TelegramResponse>> functions = new ArrayList<>();

    void onCommandAdded(Consumer<TelegramResponse> f) {
        functions.add(f);
    }

    void addCommand(TelegramResponse telegramResponse) {
        for (Consumer<TelegramResponse> f : functions) {
            f.accept(telegramResponse);
        }
    }
}

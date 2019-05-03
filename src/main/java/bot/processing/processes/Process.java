package bot.processing.processes;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Process {

    void handleRequest(Update update);

    void sendResponse(Update update);

    String getCurState();
}

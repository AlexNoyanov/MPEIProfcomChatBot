package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SendingDocumentProcess implements Process {
    private UserSession userSession;
    private String curState;

    public SendingDocumentProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "finished";
    }

    @Override
    public void handleRequest(Update update) {
        //don't have request
        //do nothing
    }

    @Override
    public void sendResponse(Update update) {
        ChatBot.sendChatAction(userSession.getChatId(), ActionType.UPLOADDOCUMENT);
        //send document here TO DO
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

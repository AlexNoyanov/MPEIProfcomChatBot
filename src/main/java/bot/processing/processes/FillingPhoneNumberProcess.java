package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import dbService.entity.Student;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

public class FillingPhoneNumberProcess implements Process {
    private UserSession userSession;
    private String curState;

    public FillingPhoneNumberProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "sendQuestion";
    }

    @Override
    public void handleRequest(Update update) {
        if(!update.hasMessage())
            return;
        curState = "finished";

        String phoneNumber = update.getMessage().getText();
        Student personalData = userSession.getPersonalData();
        personalData.setPhoneNumber(phoneNumber);

        Document document = userSession.getDocument();
        document.setField("phoneNumber", phoneNumber);
    }

    @Override
    public void sendResponse(Update update) {
        ChatBot.sendMessage(userSession.getChatId(),
                "Введите номер телефона:");
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

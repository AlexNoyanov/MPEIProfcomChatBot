package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import dbService.entity.Student;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

public class FillingLearningGroupProcess implements Process {
    private UserSession userSession;
    private String curState;

    public FillingLearningGroupProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "sendQuestion";
    }

    @Override
    public void handleRequest(Update update) {
        if(!update.hasMessage())
            return;
        curState = "finished";

        String learningGroup = update.getMessage().getText();
        Student personalData = userSession.getPersonalData();
        personalData.setLearningGroup(learningGroup);

        Document document = userSession.getDocument();
        document.setField("learningGroup", learningGroup);
    }

    @Override
    public void sendResponse(Update update) {
        ChatBot.sendMessage(userSession.getChatId(),
                "Введите вашу учебную группу:");
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

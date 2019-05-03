package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import dbService.entity.Student;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

public class FillingFIOProcess implements Process {
    private UserSession userSession;
    private String curState;

    public FillingFIOProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "sendQuestion";
    }

    @Override
    public void handleRequest(Update update) {
        if(!update.hasMessage())
            return;
        curState = "finished";

        String[] fio = update.getMessage().getText().split(" ");
        Student personalData = userSession.getPersonalData();
        personalData.setSurName(fio[0]);
        personalData.setFirstName(fio[1]);
        personalData.setMiddleName(fio[2]);

        Document document = userSession.getDocument();
        document.setField("surName", fio[0]);
        document.setField("firstName", fio[1]);
        document.setField("middleName", fio[2]);
    }

    @Override
    public void sendResponse(Update update) {
        ChatBot.sendMessage(userSession.getChatId(),
                "Введите ФИО:");
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

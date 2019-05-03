package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import dbService.entity.Student;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

public class FillingStudentCertificateProcess implements Process {
    private UserSession userSession;
    private String curState;

    public FillingStudentCertificateProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "sendQuestion";
    }

    @Override
    public void handleRequest(Update update) {
        if(!update.hasMessage())
            return;
        curState = "finished";

        String studentCertificate = update.getMessage().getText();
        Student personalData = userSession.getPersonalData();
        personalData.setStudentCertificate(studentCertificate);

        Document document = userSession.getDocument();
        document.setField("studentCertificate", studentCertificate);
    }

    @Override
    public void sendResponse(Update update) {
        ChatBot.sendMessage(userSession.getChatId(),
                "Введите номер студенческого билета:");
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

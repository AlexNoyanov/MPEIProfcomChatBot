package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import dbService.dao.DAOContext;
import dbService.dao.StudentsDAO;
import dbService.entity.Student;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

import java.io.File;

public class SendingDocumentProcess implements Process {
    private UserSession userSession;
    private String curState;

    public SendingDocumentProcess(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void handleRequest(Update update) {
        //don't have request
        //do nothing
    }

    @Override
    public void sendResponse(Update update) {
        curState = "finished";

    }

    @Override
    public String getCurState() {
        return curState;
    }
}

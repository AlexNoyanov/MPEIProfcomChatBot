package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import bot.keyboards.InlineKeyboardProvider;
import bot.processing.providers.BaseProcessProvider;
import dbService.dao.DAOContext;
import dbService.dao.StudentsDAO;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import pdf.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendingPreviewProcess implements Process {
    private UserSession userSession;
    private String curState;

    public SendingPreviewProcess(UserSession userSession) {
        this.userSession = userSession;
        curState = "sendPreview";
    }

    @Override
    public void handleRequest(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        if(callback == null)
            return;
        String callbackData = callback.getData();
        if(!callbackData.startsWith("sendingPreview"))
            return;
        callbackData = callbackData.split(" ")[1];

        BaseProcessProvider processProvider = new BaseProcessProvider(userSession);
        curState = "finished";
        switch (callbackData) {
            case "sendDocument": {
                //add sendingDocumentProcess TO DO
                break;
            }
            case "edit": {
                processProvider.addEditingProcess(userSession.getCurDocumentName() +
                                                    "Fields.txt");
                break;
            }
        }
    }

    @Override
    public void sendResponse(Update update) {
        switch (curState) {
            case "sendPreview": {
                ChatBot.sendChatAction(userSession.getChatId(), ActionType.UPLOADDOCUMENT);
                InlineKeyboardProvider keyboardProvider = new InlineKeyboardProvider() {
                    @Override
                    public InlineKeyboardMarkup getKeyboard() {
                        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                        keyboard.add(createRow(createButton("Редактировать",
                                                    "sendingPreview edit")));
                        keyboardMarkup.setKeyboard(keyboard);
                        return keyboardMarkup;
                    }
                };
                StudentsDAO studentsDAO = DAOContext.getStudentsDAO();
                studentsDAO.insert(userSession.getPersonalData());

                Document document = userSession.getDocument();
                File documentFile = document.saveToDirectory();
                ChatBot.sendDocument(userSession.getChatId(), "Готово!",
                        documentFile, keyboardProvider.getKeyboard());

            }
        }
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

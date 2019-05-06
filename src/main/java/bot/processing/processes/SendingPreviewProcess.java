package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import bot.keyboards.InlineKeyboardProvider;
import bot.processing.providers.BaseProcessProvider;
import dbService.dao.DAOContext;
import dbService.dao.StudentsDAO;
import dbService.entity.Student;
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
        switch (callbackData) {
            case "download": {
                curState = "sendDocument";
                break;
            }
            case "edit": {
                curState = "finished";
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
                ChatBot.sendChatAction(userSession.getChatId(), ActionType.UPLOADPHOTO);
                InlineKeyboardProvider keyboardProvider = new InlineKeyboardProvider() {
                    @Override
                    public InlineKeyboardMarkup getKeyboard() {
                        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                        keyboard.add(createRow(createButton("Скачать документ",
                                                        "sendingPreview download"),
                                               createButton("Редактировать",
                                                        "sendingPreview edit")));
                        keyboardMarkup.setKeyboard(keyboard);
                        return keyboardMarkup;
                    }
                };
                Document document = userSession.getDocument();
                File imageFile = document.saveDocumentAsImage(userSession.getCurDocumentName() + ".jpg");
                ChatBot.sendPhoto(userSession.getChatId(), "Посмотрите, что у нас получилось!",
                                                imageFile, keyboardProvider.getKeyboard());
                imageFile.delete();
                break;
            }
            case "sendDocument": {
                ChatBot.sendChatAction(userSession.getChatId(), ActionType.UPLOADDOCUMENT);

                StudentsDAO studentsDAO = DAOContext.getStudentsDAO();
                Student dataInDataBase = studentsDAO.getByChatId(userSession.getChatId());
                if(dataInDataBase == null)
                    studentsDAO.insert(userSession.getPersonalData());
                else
                    studentsDAO.update(userSession.getPersonalData());

                Document document = userSession.getDocument();
                File documentFile = document.saveToDirectory(userSession.getCurDocumentName() + ".pdf");
                ChatBot.sendDocument(userSession.getChatId(), "Готово!", documentFile);
                documentFile.delete();
                break;
            }
        }
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

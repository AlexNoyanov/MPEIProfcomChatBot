package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import bot.keyboards.ListKeyboardProvider;
import bot.processing.providers.BaseProcessProvider;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import util.ListsUtil;

import java.util.List;

public class EditingProcess implements Process {
    private final String FIELDS_FILENAME;

    private UserSession userSession;
    private ListKeyboardProvider keyboardProvider;
    private List<String> fields;
    private String curState;

    public EditingProcess(UserSession userSession, String fieldsFileName) {
        this.userSession = userSession;
        FIELDS_FILENAME = fieldsFileName;
        fields = ListsUtil.getList(FIELDS_FILENAME);
        keyboardProvider = new ListKeyboardProvider(fields, FIELDS_FILENAME);
        curState = "sendQuestion";
    }

    private void handleEdit(String fieldIndex) {
        int index = Integer.parseInt(fieldIndex);
        BaseProcessProvider processProvider = new BaseProcessProvider(userSession);

        switch (fields.get(index)) {
            case "\uFEFFФИО": {
                processProvider.addFillingFIOProcess();
                break;
            }
            case "Учебная группа": {
                processProvider.addFillingLearningGroupProcess();
                break;
            }
            case "Номер студенческого билета": {
                processProvider.addFillingStudentCertificateProcess();
                break;
            }
            case "Номер телефона": {
                processProvider.addFillingPhoneNumberProcess();
                break;
            }
            case "Причина": {
                processProvider.addFillingMaterialAidReasonProcess();
                break;
            }
            //more fields TO DO
        }
        processProvider.addSendingPreviewProcess();
    }

    @Override
    public void handleRequest(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        if(callback == null)
            return;
        String callbackData = callback.getData();
        if(!callbackData.startsWith(FIELDS_FILENAME))
            return;
        callbackData = callbackData.split(" ")[1];

        switch (callbackData) {
            case "prev": {
                curState = "sendPrevKeyboard";
                break;
            }
            case "next": {
                curState = "sendNextKeyboard";
                break;
            }
            default: {
                curState = "finished";
                handleEdit(callbackData);
            }
        }
    }

    @Override
    public void sendResponse(Update update) {
        switch (curState) {
            case "sendQuestion": {
                ChatBot.sendMessage(userSession.getChatId(),
                        "Выберите поле для изменения:",
                        keyboardProvider.getKeyboard());
                break;
            }
            case "sendPrevKeyboard": {
                ChatBot.editMessageReplyMarkup(userSession.getChatId(),
                        update.getCallbackQuery().getMessage().getMessageId(),
                        update.getCallbackQuery().getInlineMessageId(),
                        keyboardProvider.getPrevKeyboard());
                break;
            }
            case "sendNextKeyboard": {
                ChatBot.editMessageReplyMarkup(userSession.getChatId(),
                        update.getCallbackQuery().getMessage().getMessageId(),
                        update.getCallbackQuery().getInlineMessageId(),
                        keyboardProvider.getNextKeyboard());
                break;
            }
            case "finished": {
                //do nothing
            }
        }
    }

    @Override
    public String getCurState() {
        return curState;
    }
}

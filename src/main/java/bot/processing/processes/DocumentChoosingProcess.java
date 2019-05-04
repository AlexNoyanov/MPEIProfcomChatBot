package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import bot.keyboards.ListKeyboardProvider;
import bot.processing.providers.MaterialAidProcessProvider;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import util.ListsUtil;

import java.util.List;

public class DocumentChoosingProcess implements Process {
    private static final String DOCUMENTS = "documents.txt";

    private UserSession userSession;
    private ListKeyboardProvider keyboardProvider;
    private List<String> documents;
    private String curState;

    public DocumentChoosingProcess(UserSession userSession) {
        this.userSession = userSession;
        documents = ListsUtil.getList(DOCUMENTS);
        keyboardProvider = new ListKeyboardProvider(documents, DOCUMENTS);
        curState = "sendQuestion";
    }

    private void handleDocumentChoosing(String documentIndex) {
        int index = Integer.parseInt(documentIndex);
        switch (index) {
            case 0: {
                MaterialAidProcessProvider processProvider = new MaterialAidProcessProvider(userSession);
                processProvider.addMaterialAidProcesses();
                break;
            }
            //more document variants TO DO
        }
    }

    @Override
    public void handleRequest(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        if(callback == null)
            return;
        String callbackData = callback.getData();
        if(!callbackData.startsWith(DOCUMENTS))
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
                handleDocumentChoosing(callbackData);
            }
        }
    }

    @Override
    public void sendResponse(Update update) {
        switch (curState) {
            case "sendQuestion": {
                ChatBot.sendMessage(userSession.getChatId(),
                        "Выберите документ для заполнения:",
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

package bot.processing.processes;

import bot.ChatBot;
import bot.UserSession;
import bot.keyboards.ListKeyboardProvider;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;
import util.ListsUtil;

import java.util.List;

public class FillingMaterialAidReasonProcess implements Process {
    private static final String MATERIAL_AID_REASONS = "materialAidReasons.txt";

    private UserSession userSession;
    private String curState;
    private ListKeyboardProvider keyboardProvider;
    private List<String> reasons;

    public FillingMaterialAidReasonProcess(UserSession userSession) {
        this.userSession = userSession;
        reasons = ListsUtil.getList(MATERIAL_AID_REASONS);
        keyboardProvider = new ListKeyboardProvider(reasons, MATERIAL_AID_REASONS);
        curState = "sendQuestion";
    }

    @Override
    public void handleRequest(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        if(callback == null)
            return;
        String callbackData = callback.getData();
        if(!callbackData.startsWith(MATERIAL_AID_REASONS))
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
                int index = Integer.parseInt(callbackData);
                Document document = userSession.getDocument();
                document.setField("reason", reasons.get(index));
            }
        }
    }

    @Override
    public void sendResponse(Update update) {
        switch (curState) {
            case "sendQuestion": {
                ChatBot.sendMessage(userSession.getChatId(),
                        "Выберите причину:",
                        keyboardProvider.getKeyboard());
                break;
            }
            case "sendPrevKeyboard": {
                ChatBot.editMessageReplyMarkup(userSession.getChatId(),
                        update.getMessage().getMessageId(),
                        update.getCallbackQuery().getInlineMessageId(),
                        keyboardProvider.getPrevKeyboard());
                break;
            }
            case "sendNextKeyboard": {
                ChatBot.editMessageReplyMarkup(userSession.getChatId(),
                        update.getMessage().getMessageId(),
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
        return null;
    }
}

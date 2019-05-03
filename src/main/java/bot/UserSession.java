package bot;

import bot.processing.Process;
import bot.processing.providers.BaseProcessProvider;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdf.Document;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

public class UserSession {
    private String chatId;
    private Document document;
    private Queue<Process> processQueue;

    public UserSession(String chatId) {
        this.chatId = chatId;
        processQueue = new ArrayDeque<>();
    }

    public void executeCurrentProcess(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();
            if(message.getText().equals("//создать")) {
                processQueue.clear();
                BaseProcessProvider processProvider = new BaseProcessProvider(this);
                processProvider.addDocumentChoosingProcess();
            }
            else if(message.getText().equals("//помощь")) {
                //send help information TO DO
            }
        }

        if(processQueue.isEmpty())
            return;

        Process curProcess = processQueue.peek();
        curProcess.handleRequest(update);
        if(curProcess.getCurState().equals("finished")) {
            processQueue.poll();
            if(processQueue.isEmpty())
                return;
            curProcess = processQueue.peek();
        }
        curProcess.sendResponse(update);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Queue<Process> getProcessQueue() {
        return processQueue;
    }

    public void setProcessQueue(Queue<Process> processQueue) {
        this.processQueue = processQueue;
    }
}

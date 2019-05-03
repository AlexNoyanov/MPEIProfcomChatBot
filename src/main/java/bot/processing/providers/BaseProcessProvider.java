package bot.processing.providers;

import bot.UserSession;
import bot.processing.processes.DocumentChoosingProcess;

public class BaseProcessProvider {
    protected UserSession userSession;

    public BaseProcessProvider(UserSession userSession) {
        this.userSession = userSession;
    }

    public void addDocumentChoosingProcess() {
        userSession.getProcessQueue()
                .add(new DocumentChoosingProcess(userSession));
    }

    //more addProcess TO DO
}

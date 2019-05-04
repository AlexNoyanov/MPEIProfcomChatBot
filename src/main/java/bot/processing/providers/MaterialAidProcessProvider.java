package bot.processing.providers;

import bot.UserSession;
import dbService.entity.Student;
import pdf.Document;

import java.io.File;
import java.time.LocalDate;

public class MaterialAidProcessProvider extends BaseProcessProvider {
    private static final String TEMPLATE_PATH = "src" +
            File.separator + "main" + File.separator + "resources" +
            File.separator + "templates" + File.separator + "materialAid.pdf";

    public MaterialAidProcessProvider(UserSession userSession) {
        super(userSession);
    }

    public void addMaterialAidProcesses() {
        Student personalData = userSession.getPersonalData();
        Document document = new Document(TEMPLATE_PATH);
        userSession.setDocument(document);
        userSession.setCurDocumentName("materialAid");

        if(personalData.getFirstName() == null || personalData.getSurName() == null
                || personalData.getMiddleName() == null)
            addFillingFIOProcess();
        else {
            document.setField("firstName", personalData.getFirstName());
            document.setField("surName", personalData.getSurName());
            document.setField("middleName", personalData.getMiddleName());
        }


        if(personalData.getLearningGroup() == null)
            addFillingLearningGroupProcess();
        else
            document.setField("learningGroup", personalData.getLearningGroup());


        if(personalData.getStudentCertificate() == null)
            addFillingStudentCertificateProcess();
        else
            document.setField("studentCertificate", personalData.getStudentCertificate());


        if(personalData.getPhoneNumber() == null)
            addFillingPhoneNumberProcess();
        else
            document.setField("phoneNumber", personalData.getPhoneNumber());

        addFillingMaterialAidReasonProcess();
        addSendingPreviewProcess();
        document.setField("date", dateFormat.format(LocalDate.now()));
    }
}

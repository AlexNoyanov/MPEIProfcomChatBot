package pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Document {
    private static final String TEMP_DOCUMENTS_DIRECTORY = "src" +
            File.separator + "main" + File.separator + "resources" +
            File.separator + "tempDocuments" + File.separator;

    private static final String FONT_FILE = "src" +
            File.separator + "main" + File.separator + "resources" +
            File.separator + "fonts" + File.separator + "FreeSans.ttf";

    private static final PdfFont FONT;
//    private static RandomFileNameGenerator fileNameGenerator;
//    static {
//        fileNameGenerator = new RandomFileNameGenerator();
//    }

    static {
        try {
            FONT = PdfFontFactory.createFont(FONT_FILE, PdfEncodings.IDENTITY_H, true);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private byte[] serializedDoc;
    private PdfDocument pdfDocument;
    private ByteArrayOutputStream outputStream;
    private PdfAcroForm acroForm;
    private Map<String, PdfFormField> fields;

    public Document(String fileName) {
        outputStream = new ByteArrayOutputStream();
        try {
            pdfDocument = new PdfDocument(new PdfReader(fileName),
                                            new PdfWriter(outputStream));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        acroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
        fields = acroForm.getFormFields();
    }

    public Document(byte[] serializedDoc) {
        initialize(serializedDoc);
    }

    private void initialize(byte[] serializedDoc) {
        this.serializedDoc = serializedDoc;
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedDoc);
        try {
            pdfDocument = new PdfDocument(new PdfReader(inputStream),
                                            new PdfWriter(outputStream));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        acroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
        fields = acroForm.getFormFields();
    }

    public void setField(String fieldName, String value) {
        fields.get(fieldName).setFont(FONT)
                .setValue(value)
                .setReadOnly(true);
    }

    public String getFieldValue(String field) {
        if(fields.containsKey(field))
            return fields.get(field).getValueAsString();
        else
            return null;
    }

    public byte[] getSerializedDocument() {
        acroForm.setNeedAppearances(true);
        pdfDocument.close();
        serializedDoc = outputStream.toByteArray();
        initialize(serializedDoc);
        return serializedDoc;
    }

    public File saveToDirectory(String fileName) {
        getSerializedDocument();
        File result = new File(TEMP_DOCUMENTS_DIRECTORY + fileName);
        try {
            PdfWriter writer = new PdfWriter(result);
            writer.write(serializedDoc);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public File saveDocumentAsImage(String fileName) {
        getSerializedDocument();
        File imageFile = new File(TEMP_DOCUMENTS_DIRECTORY + fileName);
        try {
            PDDocument tempDocument = PDDocument.load(serializedDoc);
            PDFRenderer pdfRenderer = new PDFRenderer(tempDocument);
            BufferedImage pageImage = pdfRenderer.renderImageWithDPI(0, 300);
            ImageIO.write(pageImage, "JPEG", imageFile);
            tempDocument.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}

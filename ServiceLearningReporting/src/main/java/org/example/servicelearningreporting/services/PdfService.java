package org.example.servicelearningreporting.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.example.servicelearningreporting.models.ActivityForm;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(ActivityForm activity, List<String> fields) throws Exception {

        Context context = new Context();
        context.setVariable("activity", activity);
        context.setVariable("fields", fields);

        String html = templateEngine.process("pdf/activity-pdf", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
}

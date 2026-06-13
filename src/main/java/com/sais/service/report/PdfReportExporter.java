package com.sais.service.report;

import com.sais.exception.ReportGenerationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;


@Component
@Slf4j
public class PdfReportExporter implements ReportExporter {
    
    private static final String MIME_TYPE = "application/pdf";
    private static final String ERROR_MESSAGE = "PDF rapor export edilirken hata oluştu";
    
    @Override
    public byte[] export(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            
            SimplePdfExporterConfiguration configuration = createConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            
            log.debug("PDF rapor başarıyla export edildi");
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e);
            throw new ReportGenerationException(ERROR_MESSAGE, e);
        }
    }
    
    @Override
    public String getMimeType() {
        return MIME_TYPE;
    }
    
    
    private SimplePdfExporterConfiguration createConfiguration() {
        return new SimplePdfExporterConfiguration();
    }
}


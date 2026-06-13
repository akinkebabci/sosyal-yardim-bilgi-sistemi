package com.sais.service.report;

import com.sais.exception.ReportGenerationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;


@Component
@Slf4j
public class ExcelReportExporter implements ReportExporter {
    
    private static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String ERROR_MESSAGE = "Excel rapor export edilirken hata oluştu";
    
    @Override
    public byte[] export(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            
            SimpleXlsxReportConfiguration configuration = createConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            
            log.debug("Excel rapor başarıyla export edildi");
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
    
   
    private SimpleXlsxReportConfiguration createConfiguration() {
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        return configuration;
    }
}


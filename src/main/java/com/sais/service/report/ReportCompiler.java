package com.sais.service.report;

import com.sais.exception.ReportGenerationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;


@Component
@Slf4j
public class ReportCompiler {
    
    private static final String ERROR_MESSAGE = "Rapor derlenirken hata oluştu: ";
    
   
    public JasperReport compile(String reportPath) {
        try {
            ClassPathResource resource = new ClassPathResource(reportPath);
            
            try (InputStream inputStream = resource.getInputStream()) {
                JasperReport report = JasperCompileManager.compileReport(inputStream);
                log.debug("Rapor başarıyla derlendi: {}", reportPath);
                return report;
            }
            
        } catch (Exception e) {
            String errorMessage = ERROR_MESSAGE + reportPath;
            log.error(errorMessage, e);
            throw new ReportGenerationException(errorMessage, e);
        }
    }
}


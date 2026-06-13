package com.sais.service;

import com.sais.exception.ReportGenerationException;
import com.sais.service.report.ExcelReportExporter;
import com.sais.service.report.PdfReportExporter;
import com.sais.service.report.ReportCompiler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final DataSource dataSource;
    private final ReportCompiler reportCompiler;
    private final PdfReportExporter pdfExporter;
    private final ExcelReportExporter excelExporter;

   
    public byte[] generatePdfFromDatabase(String reportPath, Map<String, Object> parameters) {
        log.info("PDF rapor üretiliyor (veritabanı): {}", reportPath);
        
        try (Connection connection = dataSource.getConnection()) {
            JasperPrint jasperPrint = fillReportFromDatabase(reportPath, parameters, connection);
            return pdfExporter.export(jasperPrint);
            
        } catch (SQLException e) {
            throw new ReportGenerationException("Veritabanı bağlantısı sağlanamadı", e);
        }
    }

   
    public byte[] generatePdfFromCollection(String reportPath, 
                                           Map<String, Object> parameters,
                                           Collection<?> dataCollection) {
        log.info("PDF rapor üretiliyor (collection): {}", reportPath);
        
        JasperPrint jasperPrint = fillReportFromCollection(reportPath, parameters, dataCollection);
        return pdfExporter.export(jasperPrint);
    }

   
    public byte[] generateExcelFromDatabase(String reportPath, Map<String, Object> parameters) {
        log.info("Excel rapor üretiliyor (veritabanı): {}", reportPath);
        
        try (Connection connection = dataSource.getConnection()) {
            JasperPrint jasperPrint = fillReportFromDatabase(reportPath, parameters, connection);
            return excelExporter.export(jasperPrint);
            
        } catch (SQLException e) {
            throw new ReportGenerationException("Veritabanı bağlantısı sağlanamadı", e);
        }
    }

  
    public byte[] generateExcelFromCollection(String reportPath,
                                             Map<String, Object> parameters,
                                             Collection<?> dataCollection) {
        log.info("Excel rapor üretiliyor (collection): {}", reportPath);
        
        JasperPrint jasperPrint = fillReportFromCollection(reportPath, parameters, dataCollection);
        return excelExporter.export(jasperPrint);
    }

   
    private JasperPrint fillReportFromDatabase(String reportPath, 
                                               Map<String, Object> parameters,
                                               Connection connection) {
        try {
            JasperReport jasperReport = reportCompiler.compile(reportPath);
            return JasperFillManager.fillReport(jasperReport, parameters, connection);
            
        } catch (Exception e) {
            throw new ReportGenerationException("Rapor doldurulurken hata oluştu: " + reportPath, e);
        }
    }

    
    private JasperPrint fillReportFromCollection(String reportPath,
                                                 Map<String, Object> parameters,
                                                 Collection<?> dataCollection) {
        try {
            JasperReport jasperReport = reportCompiler.compile(reportPath);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataCollection);
            return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            
        } catch (Exception e) {
            throw new ReportGenerationException("Rapor doldurulurken hata oluştu: " + reportPath, e);
        }
    }
}

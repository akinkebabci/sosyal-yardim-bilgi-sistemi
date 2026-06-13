package com.sais.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;


@Configuration
@Slf4j
public class JasperReportsConfig {

    @PostConstruct
    public void init() {
        log.info("JasperReports yapılandırması başlatıldı");
        
       
        System.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        System.setProperty("net.sf.jasperreports.default.font.name", "DejaVu Sans");
        
       
        System.setProperty("net.sf.jasperreports.export.pdf.force.linebreak.policy", "true");
        System.setProperty("net.sf.jasperreports.export.pdf.encoding", "UTF-8");
        
       
        System.setProperty("net.sf.jasperreports.export.xls.detect.cell.type", "true");
        System.setProperty("net.sf.jasperreports.export.xls.one.page.per.sheet", "false");
        
        log.info("JasperReports yapılandırması tamamlandı");
    }
}


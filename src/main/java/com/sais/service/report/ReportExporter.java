package com.sais.service.report;

import net.sf.jasperreports.engine.JasperPrint;


public interface ReportExporter {
    
 
    byte[] export(JasperPrint jasperPrint);
    
   
    String getMimeType();
}


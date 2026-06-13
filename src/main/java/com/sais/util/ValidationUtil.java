package com.sais.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;


public class ValidationUtil {

    private static final Pattern TC_KIMLIK_PATTERN = Pattern.compile("^[1-9][0-9]{10}$");
    private static final Pattern IBAN_PATTERN = Pattern.compile("^TR\\d{2}\\d{4}\\d{1}\\d{16}$");
    private static final Pattern TELEFON_PATTERN = Pattern.compile("^(\\+90|0)?5\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    
    public static boolean isValidTcKimlikNo(String tcKimlikNo) {
        if (StringUtils.isBlank(tcKimlikNo) || !TC_KIMLIK_PATTERN.matcher(tcKimlikNo).matches()) {
            return false;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(tcKimlikNo.charAt(i));
        }

        int sum1 = (digits[0] + digits[2] + digits[4] + digits[6] + digits[8]) * 7;
        int sum2 = digits[1] + digits[3] + digits[5] + digits[7];
        int digit10 = (sum1 - sum2) % 10;

        int sum3 = 0;
        for (int i = 0; i < 10; i++) {
            sum3 += digits[i];
        }
        int digit11 = sum3 % 10;

        return digit10 == digits[9] && digit11 == digits[10];
    }

    
    public static boolean isValidIBAN(String iban) {
        if (StringUtils.isBlank(iban)) {
            return false;
        }
        String cleanIban = iban.replaceAll("\\s+", "").toUpperCase();
        return IBAN_PATTERN.matcher(cleanIban).matches();
    }

   
    public static boolean isValidTelefon(String telefon) {
        if (StringUtils.isBlank(telefon)) {
            return false;
        }
        String cleanTelefon = telefon.replaceAll("[\\s()-]", "");
        return TELEFON_PATTERN.matcher(cleanTelefon).matches();
    }

    
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

   
    public static boolean isValidEngelOrani(Integer oran) {
        return oran != null && oran >= 1 && oran <= 99;
    }

  
    public static boolean isValidYardimDonemi(Integer ay) {
        return ay != null && ay >= 1 && ay <= 12;
    }

    
    public static boolean isGebzeAddress(String il, String ilce) {
        if (StringUtils.isBlank(il) || StringUtils.isBlank(ilce)) {
            return false;
        }
        return il.equalsIgnoreCase("Kocaeli") && ilce.equalsIgnoreCase("Gebze");
    }
}



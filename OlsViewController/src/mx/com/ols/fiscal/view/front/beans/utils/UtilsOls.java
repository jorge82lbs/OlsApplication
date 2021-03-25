/**
* Project: OLS - Administración de Clientes
*
* File: UtilsOls.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** Esta clase ofrece metodos de utileria para ayuda en el desarrollo<br/>
 *
 * @author Jorge Luis Bautista - Omw
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class UtilsOls {
    /**
     * Desencripta una cadena en base a una psKey
     * @autor Jorge Luis Bautista Santiago
     * @param tsText
     * @param tsKey
     * @return String
     */
    public String decryptObject(String tsText,
                                 String tsKey) throws Exception {
        try{
            Cipher loCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] laKeyBytes = new byte[16];
            byte[] laByteEncode = tsKey.getBytes("UTF-8");
            int    liLength = laByteEncode.length;
            if (liLength > laKeyBytes.length)
                liLength = laKeyBytes.length;
            System.arraycopy(laByteEncode, 0, laKeyBytes, 0, liLength);
            SecretKeySpec   loKeySpec = new SecretKeySpec(laKeyBytes, "AES");
            IvParameterSpec loIvSpec = new IvParameterSpec(laKeyBytes);
            loCipher.init(Cipher.DECRYPT_MODE, loKeySpec, loIvSpec);
            BASE64Decoder   loDecoder = new BASE64Decoder();
            byte[]          laResults = 
                loCipher.doFinal(loDecoder.decodeBuffer(tsText));             
            return new String(laResults, "UTF-8");            
        }catch(Exception loEx){
            System.out.println("ERROR: "+loEx.getMessage());
            return null;
        }
    }

    /**
     * Encripta una cadena en base a una psKey
     * @autor Jorge Luis Bautista Santiago
     * @param tsText
     * @param tsKey
     * @return String
     */
    public String encryptObject(String tsText,
                                String tsKey) throws Exception {
        try{
            Cipher loCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] laKeyBytes = new byte[16];
            byte[] laByteEncode = tsKey.getBytes("UTF-8");
            int    liLength = laByteEncode.length;
            if (liLength > laKeyBytes.length)
                liLength = laKeyBytes.length;
            System.arraycopy(laByteEncode, 0, laKeyBytes, 0, liLength);
            SecretKeySpec   loKeySpec = new SecretKeySpec(laKeyBytes, "AES");
            IvParameterSpec loIvSpec = new IvParameterSpec(laKeyBytes);
            loCipher.init(Cipher.ENCRYPT_MODE, loKeySpec, loIvSpec);
            byte[]          laResponse = loCipher.doFinal(tsText.getBytes("UTF-8"));
            BASE64Encoder   loEncoder = new BASE64Encoder();
            return loEncoder.encode(laResponse);
        }catch(Exception loEx){
            return null;
        }
    }
    
    public String getKeyCoder(){
        String lsReturn = "LFXqSn21ptd+rNihAuZeMg==";
        return lsReturn;
    }
    
    public Integer getMaxIdTable(String tsTable) {
        Integer    liReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsTable = "";
        String     lsField = "";
        if(tsTable.equalsIgnoreCase("Parameters")){
            lsTable = "OLS.OLS_CAT_CONFIG_PARAM_TAB";
            lsField = "ID_PARAMETER";
        }
        if(tsTable.equalsIgnoreCase("Users")){
            lsTable = "OLS.OLS_CAT_USERS_TAB";
            lsField = "ID_USER";
        }
        if(tsTable.equalsIgnoreCase("Customs")){
            lsTable = "OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB";
            lsField = "ID_CLIENT_SAT";
        }
        if(tsTable.equalsIgnoreCase("CustomsRequest")){
            lsTable = "OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB";
            lsField = "ID_REQUEST";
        }
        if(tsTable.equalsIgnoreCase("Emails")){
            lsTable = "OLS.OLS_CAT_SAT_MAILS_CTES_TAB";
            lsField = "ID_MAIL";
        }
        String lsQuery = getQueryMax(lsTable,lsField);        
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                liReturn = loRs.getInt(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("Error al ejecutar:"+loExSql.getMessage());
            loExSql.printStackTrace();
            
        }
        finally{
            try {
                loCnn.close();
                loRs.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }
        return liReturn;
    }
    
    public String getQueryMax(String tsTable, String tsField) {
        String lsQuery = 
        "SELECT NVL(MAX(" + tsField + "),0) \n" + 
        "  FROM " + tsTable;
        return lsQuery;
    }
    
    public Integer getNumMonth(String tsMes){
        Integer liNumMonth = 0;
        if(tsMes.equalsIgnoreCase("ENERO")){
            liNumMonth = 1;
        }
        if(tsMes.equalsIgnoreCase("FEBRERO")){
            liNumMonth = 2;
        }
        if(tsMes.equalsIgnoreCase("MARZO")){
            liNumMonth = 3;
        }
        if(tsMes.equalsIgnoreCase("ABRIL")){
            liNumMonth = 4;
        }
        if(tsMes.equalsIgnoreCase("MAYO")){
            liNumMonth = 5;
        }
        if(tsMes.equalsIgnoreCase("JUNIO")){
            liNumMonth = 6;
        }
        if(tsMes.equalsIgnoreCase("JULIO")){
            liNumMonth = 7;
        }
        if(tsMes.equalsIgnoreCase("AGOSTO")){
            liNumMonth = 8;
        }
        if(tsMes.equalsIgnoreCase("SEPTIEMBRE")){
            liNumMonth = 9;
        }
        if(tsMes.equalsIgnoreCase("OCTUBRE")){
            liNumMonth = 10;
        }
        if(tsMes.equalsIgnoreCase("NOVIEMBRE")){
            liNumMonth = 11;
        }
        if(tsMes.equalsIgnoreCase("DICIEMBRE")){
            liNumMonth = 12;
        }
        return liNumMonth;
    }
    
    public String getPeriodDesc(String tsMonth, String tsYear){
        String lsReturn = "";
        if(tsMonth.length() > 1){
            lsReturn += tsMonth.substring(0, 3);
        }
        lsReturn += "-" + String.valueOf(tsYear);
        return lsReturn;
    }
    
    public String getDescMonth(Integer tiNumMes){
        String lsNumMonth = "SEPTIEMBRE";
        if(tiNumMes == 1){
            lsNumMonth = "ENERO";
        }
        if(tiNumMes == 2){
            lsNumMonth = "FEBRERO";
        }
        if(tiNumMes == 3){
            lsNumMonth = "MARZO";
        }
        if(tiNumMes == 4){
            lsNumMonth = "ABRIL";
        }
        if(tiNumMes == 5){
            lsNumMonth = "MAYO";
        }
        if(tiNumMes == 6){
            lsNumMonth = "JUNIO";
        }
        if(tiNumMes == 7){
            lsNumMonth = "JULIO";
        }        
        if(tiNumMes == 8){
            lsNumMonth = "AGOSTO";
        }
        if(tiNumMes == 9){
            lsNumMonth = "SEPTIEMBRE";
        }
        if(tiNumMes == 10){
            lsNumMonth = "OCTUBRE";
        }
        if(tiNumMes == 11){
            lsNumMonth = "NOVIEMBRE";
        }
        if(tiNumMes == 12){
            lsNumMonth = "DICIEMBRE";
        }
        return lsNumMonth;
    }
    
    public String getStringFromDateYyyyMmDd(Date ttFecha){
        if(ttFecha != null){
            SimpleDateFormat lodfCurrent = new SimpleDateFormat("yyyy-MM-dd");
            String lsCurrDate = lodfCurrent.format(ttFecha);
            return lsCurrDate;    
        }else{
            return null;
        }        
    }
    
    /** Valida expresiones regulares de forma dinamica
      * @autor Jorge Luis Bautista 
      * @param tsClientString
      * @param tsRegularExpression
      * @return boolean
    */
    public boolean validateRegularExpression(String tsClientString, String tsRegularExpression){
        boolean lbReturn = false;
        String  lsToValidate = 
            tsClientString == null ? "" : tsClientString;
        if(!lsToValidate.trim().equalsIgnoreCase("")){
            Matcher loMat = null;
            Pattern loPat = null;
            String  lsExpReg = tsRegularExpression;
            loPat = Pattern.compile(lsExpReg);
            loMat = loPat.matcher(lsToValidate);
            if (!loMat.find()){
                lbReturn = false; 
            }else{
                lbReturn = true;
            }
        }        
        return lbReturn;
    }
    
    /** Valida si la cadena tiene formato de precio paradigm
      * @autor Jorge Luis Bautista 
      * @param tsCadena
      * @return boolean
    */
    public boolean validateExtensionFile(String tsCadena){
        boolean lbRes = 
            validateRegularExpression(tsCadena, "([^\\s]+(\\.(?i)(xls|xlsx))$)");
        return lbRes;
    }
    
    /** Valida si la cadena tiene formato de precio paradigm
      * @autor Jorge Luis Bautista 
      * @param tsCadena
      * @return boolean
    */
    public boolean validateExtensionFileCSV(String tsCadena){
        boolean lbRes = 
            validateRegularExpression(tsCadena, "([^\\s]+(\\.(?i)(csv|CSV))$)");
        return lbRes;
    }
    
    /** Valida si la cadena tiene formato de precio paradigm
      * @autor Jorge Luis Bautista 
      * @param tsCadena
      * @return boolean
    */
    public boolean validateEmailFormat(String tsCadena){
        boolean lbRes = 
            validateRegularExpression(tsCadena, 
                                      "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,3})$");
        return lbRes;
    }
}

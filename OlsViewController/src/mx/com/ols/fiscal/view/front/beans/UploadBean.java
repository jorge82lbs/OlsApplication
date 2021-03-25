/**
* Project: OLS - Administración de Clientes
*
* File: UploadBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import mx.com.ols.fiscal.model.types.ResponseUpdDao;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxClientsSatDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxProvidersSatDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxSatAllegedDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxSatDefinitiveDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.ReportBlacklistDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.ViewObjectDao;
import mx.com.ols.fiscal.view.front.beans.types.ClienteBean;
import mx.com.ols.fiscal.view.front.beans.types.OlsClientUploadErrBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxClientsSatBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxProvidersBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatAllegedBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatDefinitiveBean;
import mx.com.ols.fiscal.view.front.beans.utils.ADFUtils;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;
import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import oracle.jbo.Row;

/** Esta clase gestiona la pantalla de Carga de Archivo<br/>
 *
 * @author Jorge Luis Bautista
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class UploadBean {
    private RichInputNumberSpinbox poYearInc;
    private RichSelectOneChoice poSelMonthInc;
    private RichInputFile poFileSat;
    private RichInputNumberSpinbox poYearDef;
    private RichSelectOneChoice poSelMonthDef;
    private RichInputFile poFileSatDfn;
    private RichInputNumberSpinbox poYearPre;
    private RichSelectOneChoice poSelMonthPre;
    private RichInputFile poFileSatPre;
    private RichInputNumberSpinbox poYearOls;
    private RichSelectOneChoice poSelMonthOls;
    private RichInputFile poFileOls;
    Map goSessionScope = ADFContext.getCurrent().getSessionScope();
    String gsUserName = (String)goSessionScope.get("loggedOlsUser");
    String gsIdUser = (String)goSessionScope.get("loggedOlsIdUser");
    private RichInputNumberSpinbox poYearProv;
    private RichSelectOneChoice poSelMonthProv;
    private RichInputFile poFileProv;
    private RichSelectBooleanCheckbox poNoLocDelete;
    private RichPopup poPopupDelete;
    private RichPanelLabelAndMessage poDeleteMsgLbl;
    private RichOutputText poDeleteIdBinding;

    public UploadBean() {
    }

    public void setPoYearInc(RichInputNumberSpinbox poYearInc) {
        this.poYearInc = poYearInc;
    }

    public RichInputNumberSpinbox getPoYearInc() {
        return poYearInc;
    }

    public void setPoSelMonthInc(RichSelectOneChoice poSelMonthInc) {
        this.poSelMonthInc = poSelMonthInc;
    }

    public RichSelectOneChoice getPoSelMonthInc() {
        return poSelMonthInc;
    }

    public void setPoFileSat(RichInputFile poFileSat) {
        this.poFileSat = poFileSat;
    }

    public RichInputFile getPoFileSat() {
        return poFileSat;
    }

    public void setPoYearDef(RichInputNumberSpinbox poYearDef) {
        this.poYearDef = poYearDef;
    }

    public RichInputNumberSpinbox getPoYearDef() {
        return poYearDef;
    }

    public void setPoSelMonthDef(RichSelectOneChoice poSelMonthDef) {
        this.poSelMonthDef = poSelMonthDef;
    }

    public RichSelectOneChoice getPoSelMonthDef() {
        return poSelMonthDef;
    }

    public void setPoFileSatDfn(RichInputFile poFileSatDfn) {
        this.poFileSatDfn = poFileSatDfn;
    }

    public RichInputFile getPoFileSatDfn() {
        return poFileSatDfn;
    }

    public String uploadSatDfnAction() {
        UtilsOls loUtilsOls = new UtilsOls();
        System.out.println("upload file Definitivos - DEFINITIVE");              
        String lsFieldErrorRq = "";
        boolean lbProcessReq = true;
        boolean lbProcess = true;
        Integer liCountError = 0;
        Integer liIdRequest = 0;
        
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));
        
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
       
        UploadedFile            loFileDef = 
            poFileSatDfn.getValue() == null ? null : 
            (UploadedFile)poFileSatDfn.getValue();
        
        String        lsFileName = "";
        if(loFileDef == null){
            lsFieldErrorRq += "Archivo,";
            lbProcessReq = false;   
        }
        if(!lbProcessReq){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            lsFileName = loFileDef.getFilename();
            //Validar ARCHIVO - (csv,CSV) con expresiones regulares            
            if(!loUtilsOls.validateExtensionFileCSV(lsFileName)){
                //Validar ARCHIVO - Que hoja de excel sea formato valido - No es necesario
                lbProcess = false;
                FacesMessage loMsg;
                loMsg = new FacesMessage("No es Posible Leer el Archivo");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
            
        }     
        
        
        //Validar nombre del archivo DEFINITIVOS
        System.out.println("Validar nombre del archivo DEFINITIVOS");
        //System.out.println("TMP - Por el momento no validar nombre del archivo");
        //if(!validateFileName(loFileDef.getFilename(), "SAT-DEFINITIVOS")){
        if(!validateFileName(loFileDef.getFilename(), "Definitivos")){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("El nombre del Archivo es incorrecto, formato Definitivos no " +
                "encontrado");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        
        if(lbProcess){
            ResponseUpdDao loRe = deleteDeprecateInfo("Definitivos");
            System.out.println("Eliminar informacion deprecada para definitivos: "+loRe.getLsResponse());
            Integer lisMonth = loUtilsOls.getNumMonth(lsMonth); 
            Integer liIdUser = Integer.parseInt(gsIdUser);
            ViewObjectDao loViewObjectDao = new ViewObjectDao();
            liIdRequest = loViewObjectDao.getIdRequestSq();
            try {
                System.out.println("Insertar los siguientes datos (DEFINITIVOS)");
                InputStreamReader loIsr = new InputStreamReader(loFileDef.getInputStream());
                BufferedReader loBfRead = new BufferedReader(loIsr);
                PhmTrxSatDefinitiveDao loPhmTrxSatDefinitiveDao = new PhmTrxSatDefinitiveDao();
                String lsLine = loBfRead.readLine();
                int liI = 0;
                while (lsLine != null) {
                    String[] lsFields = lsLine.split(","); 
                    //System.out.println(Arrays.toString(lsFields));
                    if(liI >= 3){
                        //System.out.println("["+lsFields[0]+"] >> "+lsFields[1]+" - " + lsFields[2].replace("\"", ""));
                        String lsRfcGlobal = "NA";
                        try{
                            PhmTrxSatDefinitiveBean loPhmTrxSatDefinitiveBean = new PhmTrxSatDefinitiveBean();                            
                            String lsRfc =
                                lsFields[1] == null ? "" :
                                lsFields[1];
                            lsRfcGlobal = lsRfc;
                            String lsDescription = 
                                lsFields[2] == null ? "" : 
                                lsFields[2].replace("'", "''");
                            
                            loPhmTrxSatDefinitiveBean.setLiIdRequest(liIdRequest);
                            loPhmTrxSatDefinitiveBean.setLiIndYear(Integer.parseInt(lsYear));
                            loPhmTrxSatDefinitiveBean.setLsIndMonth(lsMonth);
                            loPhmTrxSatDefinitiveBean.setLiNumMonth(lisMonth);
                            loPhmTrxSatDefinitiveBean.setLsIndRfcDfn(lsRfc.trim());
                            loPhmTrxSatDefinitiveBean.setLsIndCompany(lsDescription);
                            loPhmTrxSatDefinitiveBean.setLsIndEstatus("A");
                            
                            loPhmTrxSatDefinitiveBean.setLiNumCreatedBy(liIdUser);
                            loPhmTrxSatDefinitiveBean.setLiNumLastUpdatedBy(liIdUser);
                            ResponseBean loResponseBean = 
                                loPhmTrxSatDefinitiveDao.insertPhmTrxSatDefinitive(loPhmTrxSatDefinitiveBean);
                            if(loResponseBean.getLsResponse().equalsIgnoreCase("ERROR")){
                                OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                loOlsClientUploadErrBean.setLiIdRowExcel(liI);
                                loOlsClientUploadErrBean.setLsIndDesError(loResponseBean.getLsMessageResponse());
                                loOlsClientUploadErrBean.setLsIndTypeClient("DEFINITIVO");
                                loOlsClientUploadErrBean.setLsIndEstatus("A");
                                loOlsClientUploadErrBean.setLsIndFileName(lsFileName);   
                                loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                liCountError++;
                            }
                        }catch(Exception loEx){
                            OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                            loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                            loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                            loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                            loOlsClientUploadErrBean.setLiIdRowExcel(liI);
                            loOlsClientUploadErrBean.setLsIndDesError(loEx.getMessage());
                            loOlsClientUploadErrBean.setLsIndTypeClient("DEFINITIVO");
                            loOlsClientUploadErrBean.setLsIndEstatus("A");
                            loOlsClientUploadErrBean.setLsIndFileName(lsFileName);                            
                            loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);                            
                            loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                            liCountError++;
                        }
                    }
                    liI++;
                    lsLine = loBfRead.readLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("TMP - FileNotFoundException "+e.getMessage()); 
            } catch (IOException e) {
                System.out.println("TMP - IOException "+e.getMessage()); 
            }
        }
        if(liCountError > 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Existen Registros que No Pudieron completar la Transacci\u00f3sn, \n" +
                "favor de revisar pantalla de Incidencias con solicitud ["+liIdRequest+"] ");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Finalizado Satisfactoriamente! ");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        System.out.println("FIN DE CLASE ("+new Date()+")");
        return null;
    }

    
    public String uploadSatPreAction() {
        System.out.println("upload file Presuntos - Alleged");
        UtilsOls loUtilsOls = new UtilsOls();
        String lsFieldErrorRq = "";
        boolean lbProcessReq = true;
        boolean lbProcess = true;
        Integer liCountError = 0;
        Integer liIdRequest = 0;
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
        
        UploadedFile            loFilePre = 
            poFileSatPre.getValue() == null ? null : 
            (UploadedFile)poFileSatPre.getValue();
        String        lsFileName = "";
        
        if(loFilePre == null){
            lsFieldErrorRq += "\nArchivo,";
            lbProcessReq = false;   
        }
        if(!lbProcessReq){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            lsFileName = loFilePre.getFilename();
            if(!loUtilsOls.validateExtensionFileCSV(lsFileName)){
                //Validar ARCHIVO - Que hoja de excel sea formato valido   
                lbProcess = false;
                FacesMessage loMsg;
                loMsg = new FacesMessage("No es Posible Leer el Archivo");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
        }  
        
        //Validar nombre del archivo SAT-PRESUNTOS        
        if(!validateFileName(loFilePre.getFilename(), "Presuntos")){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("El nombre del Archivo es incorrecto, formato Presuntos no " +
                "encontrado");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        
        if(lbProcess){
            ResponseUpdDao loRe = deleteDeprecateInfo("Presuntos");
            System.out.println("Resulta de elimiar registros para Presuntos: "+loRe.getLsResponse());
            Integer lisMonth = loUtilsOls.getNumMonth(lsMonth); 
            Integer liIdUser = Integer.parseInt(gsIdUser);
            ViewObjectDao loViewObjectDao = new ViewObjectDao();
            liIdRequest = loViewObjectDao.getIdRequestSq();
            try {
                System.out.println("Insertar los siguientes datos (PRESUNTOS)");
                InputStreamReader loIsr = new InputStreamReader(loFilePre.getInputStream());
                BufferedReader loBfRead = new BufferedReader(loIsr);
                PhmTrxSatAllegedDao loPhmTrxSatAllegedDao = new PhmTrxSatAllegedDao();
                String lsLine = loBfRead.readLine();
                int liI = 0;
                while (lsLine != null) {
                    String[] lsFields = lsLine.split(","); 
                    System.out.print("["+lsFields.length+"]>>>>>> ");
                    if(liI >= 3 && lsFields.length >3){
                        String lsRfcGlobal = "NA";
                        try{
                            PhmTrxSatAllegedBean loPhmTrxSatAllegedBean = new PhmTrxSatAllegedBean();
                            String lsNumber =
                                lsFields[0] == null ? "" :
                                lsFields[0];
                            System.out.print("["+lsNumber+"]");
                            String lsRfc =
                                lsFields[1] == null ? "" :
                                lsFields[1];
                            System.out.print("["+lsRfc+"]");
                            lsRfcGlobal = lsRfc;
                            if(lsRfc.length() > 0){
                                String lsDescription = "No Aplica";
                                int intIndex = lsRfc.indexOf("XXX");
                                if(intIndex == - 1){
                                    lsDescription = 
                                        lsFields[2] == null ? "" : 
                                        lsFields[2].replace("'", "''");    
                                }
                                System.out.print("["+lsDescription+"]");
                                
                                loPhmTrxSatAllegedBean.setLiIdRequest(liIdRequest);
                                loPhmTrxSatAllegedBean.setLiIndYear(Integer.parseInt(lsYear));
                                loPhmTrxSatAllegedBean.setLsIndMonth(lsMonth);
                                loPhmTrxSatAllegedBean.setLiNumMonth(lisMonth);
                                loPhmTrxSatAllegedBean.setLiNumNumber(Integer.parseInt(lsNumber));
                                loPhmTrxSatAllegedBean.setLsIndRfcAlg(lsRfc.trim());
                                loPhmTrxSatAllegedBean.setLsIndCompany(lsDescription);
                                loPhmTrxSatAllegedBean.setLsIndEstatus("A");
                                loPhmTrxSatAllegedBean.setLiNumCreatedBy(liIdUser);
                                loPhmTrxSatAllegedBean.setLiNumLastUpdatedBy(liIdUser);
                                ResponseBean loResponseBean = 
                                    loPhmTrxSatAllegedDao.insertPhmTrxSatAlleged(loPhmTrxSatAllegedBean);
                                System.out.println("");
                                if(loResponseBean.getLsResponse().equalsIgnoreCase("ERROR")){
                                    System.out.println("Error al insertar(Presuntos) en error: "+loResponseBean.getLsMessageResponse());
                                    OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                    loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                    loOlsClientUploadErrBean.setLiIdRowExcel(liI);
                                    loOlsClientUploadErrBean.setLsIndDesError(loResponseBean.getLsMessageResponse());
                                    loOlsClientUploadErrBean.setLsIndTypeClient("PRESUNTOS");
                                    loOlsClientUploadErrBean.setLsIndEstatus("A");
                                    loOlsClientUploadErrBean.setLsIndFileName(lsFileName);   
                                    loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                    loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                    liCountError++;
                                }
                            }
                        }catch(Exception loEx){
                            System.out.println("Error al insertar(Presuntos): "+loEx.getMessage());
                            OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                            loOlsClientUploadErrBean.setLiIdNumCreatedBy(888);
                            loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(888);
                            loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                            loOlsClientUploadErrBean.setLiIdRowExcel(liI);
                            loOlsClientUploadErrBean.setLsIndDesError(loEx.getMessage());
                            loOlsClientUploadErrBean.setLsIndTypeClient("PRESUNTOS");
                            loOlsClientUploadErrBean.setLsIndEstatus("A");
                            loOlsClientUploadErrBean.setLsIndFileName(lsFileName);
                            loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                            loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                            liCountError++;
                        }
                    }
                    liI++;
                    lsLine = loBfRead.readLine();
                }
            }catch(Exception loEx){
                System.out.println("Error general A");
            }
        }
        if(liCountError > 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Existen Registros que No Pudieron completar la Transacci\u00f3n, \n" +
                "favor de revisar pantalla de Incidencias con solicitud ["+liIdRequest+"] ");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Finalizado Satisfactoriamente!");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        System.out.println("FIN DE CLASE ["+new Date()+"]");
        return null;
    }

    public void setPoYearOls(RichInputNumberSpinbox poYearOls) {
        this.poYearOls = poYearOls;
    }

    public RichInputNumberSpinbox getPoYearOls() {
        return poYearOls;
    }

    public void setPoSelMonthOls(RichSelectOneChoice poSelMonthOls) {
        this.poSelMonthOls = poSelMonthOls;
    }

    public RichSelectOneChoice getPoSelMonthOls() {
        return poSelMonthOls;
    }

    public void setPoFileOls(RichInputFile poFileOls) {
        this.poFileOls = poFileOls;
    }

    public RichInputFile getPoFileOls() {
        return poFileOls;
    }

    public String uploadSatOlsAction() {
        //Map loSessionScope = ADFContext.getCurrent().getSessionScope();
        //String lsUserName = (String)loSessionScope.get("loggedOlsUser");
        //String lsIdUser = (String)loSessionScope.get("loggedOlsIdUser");       
        UtilsOls loUtilsOls = new UtilsOls();
        String lsFieldErrorRq = "";
        boolean lbProcessReq = true;
        boolean lbProcess = true;
        Integer liCountError = 0;
        Integer liIdRequest = 0;
      
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
        
        UploadedFile loFileSAT = 
            poFileOls.getValue() == null ? null : 
            (UploadedFile)poFileOls.getValue();
        
        String        lsFileName = "";
       
        if(loFileSAT == null){
            lsFieldErrorRq += "\nArchivo,";
            lbProcessReq = false;   
        }
        if(!lbProcessReq){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            lsFileName = loFileSAT.getFilename();
            //Validar ARCHIVO - Tipo excel (xls,xlsx) con expresiones regulares
            if(!loUtilsOls.validateExtensionFile(lsFileName)){
                //Validar ARCHIVO - Que hoja de excel sea formato valido                  
                lbProcess = false;
                FacesMessage loMsg;
                loMsg = new FacesMessage("No es Posible Leer el Archivo");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
            
        }
        
        //Validar nombre del archivo OLS-CLIENTES
        if(!validateFileName(loFileSAT.getFilename(), "OLS-CLIENTES")){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("El nombre del Archivo es incorrecto, formato OLS-CLIENTES_YYYYMMDD.xlsx no " +
                "encontrado");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        
        if(lbProcess){    
            Integer lisMonth = loUtilsOls.getNumMonth(lsMonth);  
            Integer liIdUser = Integer.parseInt(gsIdUser);
            String lsRfcGlobal = "";
            try {
                XSSFWorkbook loWorkBook = new XSSFWorkbook(loFileSAT.getInputStream());
                //for (int i = 0; i < numSheets; i++) {
                try{
                    PhmTrxClientsSatDao loPhmTrxClientsSatDao = new PhmTrxClientsSatDao();
                    ViewObjectDao loViewObjectDao = new ViewObjectDao();
                    Sheet loSheet = loWorkBook.getSheetAt(0);
                    liIdRequest = loViewObjectDao.getIdRequestSq();
                    boolean lbBrk = true;
                    int li = 2;
                    //for(int li = 1; li < 15; li++){
                    while(lbBrk && li < 500000){                    
                        Cell loCelda = loSheet.getRow(li).getCell(0);
                        if(loCelda.getCellType() != Cell.CELL_TYPE_BLANK){
                            try{
                                PhmTrxClientsSatBean loPhmTrxClientsSatBean = new PhmTrxClientsSatBean();
                                Integer liId = loViewObjectDao.getMaxIdTable("ClientsSat") + 1;
                                String lsRfc =
                                    loSheet.getRow(li).getCell(0) == null ? "" :
                                    loSheet.getRow(li).getCell(0).getStringCellValue();
                                lsRfcGlobal = lsRfc;
                                String lsDescription = 
                                    loSheet.getRow(li).getCell(1) == null ? "" : 
                                    loSheet.getRow(li).getCell(1).getStringCellValue().replace("'", "''");
                                loPhmTrxClientsSatBean.setLiIdClientSat(liId);
                                loPhmTrxClientsSatBean.setLiRequest(liIdRequest);
                                loPhmTrxClientsSatBean.setLiIndYear(Integer.parseInt(lsYear));
                                loPhmTrxClientsSatBean.setLsIndMonth(String.valueOf(lisMonth));
                                loPhmTrxClientsSatBean.setLiIdLawFirm(1);
                                loPhmTrxClientsSatBean.setLsIndCompany(lsDescription);
                                loPhmTrxClientsSatBean.setLsIndRfc(lsRfc);
                                loPhmTrxClientsSatBean.setLsIndEstatus("A");
                                loPhmTrxClientsSatBean.setLiCreatedBy(liIdUser);
                                loPhmTrxClientsSatBean.setLiUpdateBy(liIdUser);
                                ResponseBean loResponseBean = loPhmTrxClientsSatDao.insertPhmTrxClientsSat(loPhmTrxClientsSatBean);
                                if(loResponseBean.getLsResponse().equalsIgnoreCase("ERROR")){
                                    OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                    loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                    loOlsClientUploadErrBean.setLiIdRowExcel(li);
                                    loOlsClientUploadErrBean.setLsIndDesError(loResponseBean.getLsMessageResponse());
                                    loOlsClientUploadErrBean.setLsIndTypeClient("OLS");
                                    loOlsClientUploadErrBean.setLsIndEstatus("A");
                                    loOlsClientUploadErrBean.setLsIndFileName(lsFileName);   
                                    loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                    loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                    liCountError++;
                                }
                            }catch(Exception loEx){
                                OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                loOlsClientUploadErrBean.setLiIdRowExcel(li);
                                loOlsClientUploadErrBean.setLsIndDesError(loEx.getMessage());
                                loOlsClientUploadErrBean.setLsIndTypeClient("OLS");
                                loOlsClientUploadErrBean.setLsIndEstatus("A");
                                loOlsClientUploadErrBean.setLsIndFileName(lsFileName);
                                loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                liCountError++;
                            }
                        }else{
                            System.out.println("exit to dos");
                            lbBrk = false;
                        }
                        li++;
                    };
                }catch(Exception loExp){
                    System.out.println("Error en la hoja "+loExp.getMessage());
                }finally{
                    System.gc();
                }
                System.out.println("FIN");
                //}
            } catch (IOException loEx) {
                System.gc();
                System.out.println("Error al convertir a XSSFWorkbook "+loEx.getMessage());
            }
        }
        if(liCountError > 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Existen Registros que No Pudieron completar la Transacci\u00f3n, \n" +
                "favor de revisar pantalla de Incidencias con solicitud ["+liIdRequest+"] ");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Finalizado Satisfactoriamente!");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        System.out.println("FIN DE CLASE");
        return null;
    }
    
    public String uploadUnfulfilledAction() {
        UtilsOls loUtilsOls = new UtilsOls();
        System.out.println("upload file No Localizados - UNFULFILLED: "+new Date());
        String lsFieldErrorRq = "";
        boolean lbProcessReq = true;
        boolean lbProcess = true;
        Integer liCountError = 0;
        Integer liIdRequest = 0;
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));          
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
        
        String lsDeleteInfo = "true";
            //getPoNoLocDelete().getValue() == null ? "false" : 
            //getPoNoLocDelete().getValue().toString();
             
        UploadedFile            loFileInc = 
            poFileSat.getValue() == null ? null : 
            (UploadedFile)poFileSat.getValue();
        String        lsFileName = "";
        
        if(loFileInc == null){
            lsFieldErrorRq += "\nArchivo,";
            lbProcessReq = false;   
        }
        
        //Validar nombre del archivo SAT-NO-LOCALIZADOS        
        if(!validateFileName(loFileInc.getFilename(), "No localizados")){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("El nombre del Archivo es incorrecto, formato " +
                "No localizados no encontrado");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        if(!lbProcessReq){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            lsFileName = loFileInc.getFilename();
         
            if(!loUtilsOls.validateExtensionFileCSV(lsFileName)){
                lbProcess = false;
                FacesMessage loMsg;
                loMsg = new FacesMessage("No es Posible Leer el Archivo");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
            
        }        
        
        if(lbProcess){
            //Eliminar registros deprecados de Incumplidos
            ResponseUpdDao loRe = new ResponseUpdDao();
            if(lsDeleteInfo.equalsIgnoreCase("true") ){
                loRe = deleteDeprecateInfo("Incumplidos");    
            }else{
                loRe.setLsResponse("OK");
                loRe.setLsMessage("OK");
                loRe.setLiAffected(0);
                System.out.println("El usuario ha decidido no eliminar registros");
            }
            
            Integer lisMonth = loUtilsOls.getNumMonth(lsMonth); 
            Integer liIdUser = Integer.parseInt(gsIdUser);
            ViewObjectDao loViewObjectDao = new ViewObjectDao();
            liIdRequest = loViewObjectDao.getIdRequestSq();
            try {
                System.out.println("Insertar los siguientes datos (NO LOCALIZADOS)");
                InputStreamReader loIsr = new InputStreamReader(loFileInc.getInputStream());
                BufferedReader loBfRead = new BufferedReader(loIsr);
                
                String              lsEntityView = "OlsCatSatUnfulfilledTabView1";
                String              lsAppModuleDataControl = "OlsAppModuleDataControl";
                ApplicationModule   loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
                ViewObject          loVo = loAm.findViewObject(lsEntityView);
                oracle.jbo.Row                 loRow = null;                        
                System.out.print("llega aqui (no Localizados)? "+new Date());
                String lsLine = loBfRead.readLine();
                int liI = 0;
                while (lsLine != null) {
                    String[] lsFields = lsLine.split(","); 
                    //System.out.println(Arrays.toString(lsFields));
                    if(liI >= 1){
                        //System.out.println("["+lsFields[0]+"] >> "+lsFields[1]+" - " + lsFields[2].replace("\"", ""));
                        String lsRfcGlobal = "NA";
                        try{
                            Integer liIdNoloc = loViewObjectDao.getIdNoLocSq();
                            String lsRfc =
                                lsFields[0] == null ? "" :
                                lsFields[0];
                            lsRfcGlobal = lsRfc;
                            String lsDescription = lsFields[1] == null ? "" : lsFields[1];
                            //System.out.println("RFC("+lsRfc+")");
                            loRow = (oracle.jbo.server.ViewRowImpl)loVo.createRow();
                            loRow.setAttribute("IdClientUnf", liIdNoloc);
                            loRow.setAttribute("IdRequest", liIdRequest);                            
                            loRow.setAttribute("IndYear", Integer.parseInt(lsYear));
                            loRow.setAttribute("IndRfcUnf", lsRfc);
                            loRow.setAttribute("IndCompany", lsDescription);                            
                            loRow.setAttribute("IndEstatus", "A");
                            loRow.setAttribute("FecCreationDate", new Date());
                            loRow.setAttribute("NumCreatedBy", liIdUser);
                            loRow.setAttribute("FecLastUpdateDate", new Date());
                            loRow.setAttribute("NumLastUpdatedBy", liIdUser);
                            loVo.insertRow(loRow);
                            loVo.executeQuery();
                            loAm.getTransaction().commit();
                        }catch(Exception loEx){
                            OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                            loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                            loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                            loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                            loOlsClientUploadErrBean.setLiIdRowExcel(liI);
                            loOlsClientUploadErrBean.setLsIndDesError(loEx.getMessage());
                            loOlsClientUploadErrBean.setLsIndTypeClient("INCUMPLIDO");
                            loOlsClientUploadErrBean.setLsIndEstatus("A");
                            loOlsClientUploadErrBean.setLsIndFileName(lsFileName);
                            loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                            loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                            liCountError++;
                        }
                    }
                    liI++;
                    lsLine = loBfRead.readLine();
                }
            } catch (FileNotFoundException e) {
                System.out.println("TMP - FileNotFoundException "+e.getMessage()); 
            } catch (IOException e) {
                System.out.println("TMP - IOException "+e.getMessage()); 
            }             
        }
        if(liCountError > 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Existen Registros que No Pudieron completar la Transacci\u00f3n, \n" +
                "favor de revisar pantalla de Incidencias con solicitud ["+liIdRequest+"] ");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Finalizado Satisfactoriamente!");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        System.out.println("FIN DE CLASE ["+new Date()+"]");
        return null;
    }
    
    public ResponseUpdDao deleteDeprecateInfo(String tsType){
        ResponseUpdDao loResponseUpdDao = new ResponseUpdDao();
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        loResponseUpdDao = loViewObjectDao.deleteDepreacteTable(tsType);
        return loResponseUpdDao;
    }
    
    public int getCurrentCalendar(String tsParameter){
        int      liRes = 0;
        Calendar ltDate = new GregorianCalendar();
        int liYear = ltDate.get(Calendar.YEAR);
        int liMonth = ltDate.get(Calendar.MONTH);
        int liDay = ltDate.get(Calendar.DAY_OF_MONTH);
        int liHour = ltDate.get(Calendar.HOUR_OF_DAY);
        int liMinute = ltDate.get(Calendar.MINUTE);
        int liSecond = ltDate.get(Calendar.SECOND);
        
        if(tsParameter.equalsIgnoreCase("YEAR")){liRes = liYear;}
        if(tsParameter.equalsIgnoreCase("MONTH")){liRes = liMonth;}
        if(tsParameter.equalsIgnoreCase("DAY")){liRes = liDay;}
        if(tsParameter.equalsIgnoreCase("HOUR")){liRes = liHour;}
        if(tsParameter.equalsIgnoreCase("MINUTE")){liRes = liMinute;}
        if(tsParameter.equalsIgnoreCase("SECOND")){liRes = liSecond;}
        
        
        return liRes;
    }
    
    public String getCurrentMonth(int liMes){
        String lsMonth = "ENERO";
        switch(liMes){
           case 1 :
              lsMonth = "ENERO";
              break;
           case 2 :
              lsMonth = "FEBRERO";
              break;
            case 3 :
               lsMonth = "MARZO";
               break;
            case 4 :
               lsMonth = "ABRIL";
               break;
            case 5 :
               lsMonth = "MAYO";
               break;
            case 6 :
               lsMonth = "JUNIO";
               break;
            case 7 :
               lsMonth = "JULIO";
               break;
            case 8 :
               lsMonth = "AGOSTO";
               break;
            case 9 :
               lsMonth = "SEPTIEMBRE";
               break;
            case 10 :
               lsMonth = "OCTUBRE";
               break;
            case 11 :
               lsMonth = "NOVIEMBRE";
               break;
            case 12 :
               lsMonth = "DICIEMBRE";
               break;
           default : 
              lsMonth = "ENERO";
        }
        return lsMonth;
    }

    public void setPoYearProv(RichInputNumberSpinbox poYearProv) {
        this.poYearProv = poYearProv;
    }

    public RichInputNumberSpinbox getPoYearProv() {
        return poYearProv;
    }

    public void setPoSelMonthProv(RichSelectOneChoice poSelMonthProv) {
        this.poSelMonthProv = poSelMonthProv;
    }

    public RichSelectOneChoice getPoSelMonthProv() {
        return poSelMonthProv;
    }

    public void setPoFileProv(RichInputFile poFileProv) {
        this.poFileProv = poFileProv;
    }

    public RichInputFile getPoFileProv() {
        return poFileProv;
    }

    public String uploadSatProvAction() {
        //System.out.println("Dentro de uploadSatProvAction");
        UtilsOls loUtilsOls = new UtilsOls();
        String lsFieldErrorRq = "";
        boolean lbProcessReq = true;
        boolean lbProcess = true;
        Integer liCountError = 0;
        Integer liIdRequest = 0;
        
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));
        System.out.println("Current year: ["+lsYear+"]");
            //getPoYearProv().getValue() == null ? "" : 
            //getPoYearProv().getValue().toString();
        //System.out.println("lsYear["+lsYear+"]");
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
        System.out.println("Current month: ["+lsMonth+"]");
            //getPoSelMonthProv().getValue() == null ? "" : 
            //getPoSelMonthProv().getValue().toString();
        //System.out.println("lsMonth["+lsMonth+"]");
        UploadedFile loFileSAT = 
            poFileProv.getValue() == null ? null : 
            (UploadedFile)poFileProv.getValue();
        
        String        lsFileName = "";
       
        if(loFileSAT == null){
            lsFieldErrorRq += "Archivo,";
            lbProcessReq = false;   
        }
        if(!lbProcessReq){
            //System.out.println("Campos requeridos");
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            //System.out.println("Procesando");
            lsFileName = loFileSAT.getFilename();
            //System.out.println("lsFileName: "+lsFileName);
            //Validar ARCHIVO - Tipo excel (xls,xlsx) con expresiones regulares
            if(!loUtilsOls.validateExtensionFile(lsFileName)){
                //Validar ARCHIVO - Que hoja de excel sea formato valido                  
                lbProcess = false;
                FacesMessage loMsg;
                loMsg = new FacesMessage("No es Posible Leer el Archivo");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
            
        }   
        //System.out.println("bandera de procesar["+lbProcess+"]");
        
        //Validar nombre del archivo OLS-PROVEEDORES
        if(!validateFileName(loFileSAT.getFilename(), "OLS-PROVEEDORES")){
            lbProcess = false;
            FacesMessage loMsg;
            loMsg = new FacesMessage("El nombre del Archivo es incorrecto, formato OLS-PROVEEDORES_YYYYMMDD.xlsx no " +
                "encontrado");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        
        if(lbProcess){    
            Integer lisMonth = loUtilsOls.getNumMonth(lsMonth);  
            //System.out.println("lisMonth["+lisMonth+"]");
            Integer liIdUser = Integer.parseInt(gsIdUser);
            //System.out.println("liIdUser["+liIdUser+"]");
            String lsRfcGlobal = "";
            try {
                //System.out.println("Convirtiendo a POI");
                XSSFWorkbook loWorkBook = new XSSFWorkbook(loFileSAT.getInputStream());
                //for (int i = 0; i < numSheets; i++) {
                try{
                    //System.out.println("obtiene hojas");
                    PhmTrxProvidersSatDao loPhmTrxProvidersDao = new PhmTrxProvidersSatDao();
                    ViewObjectDao loViewObjectDao = new ViewObjectDao();
                    Sheet loSheet = loWorkBook.getSheetAt(0);
                    liIdRequest = loViewObjectDao.getIdRequestSq();
                    boolean lbBrk = true;
                    int li = 2;
                    //for(int li = 1; li < 15; li++){
                    while(lbBrk && li < 500000){                    
                        Cell loCelda = loSheet.getRow(li).getCell(0);
                        if(loCelda.getCellType() != Cell.CELL_TYPE_BLANK){
                            try{
                                PhmTrxProvidersBean loPhmTrxProvidersBean = new PhmTrxProvidersBean();
                                Integer liId = loViewObjectDao.getMaxIdTable("ProviderSat") + 1;
                                String lsRfcClient =
                                    loSheet.getRow(li).getCell(0) == null ? "" :
                                    loSheet.getRow(li).getCell(0).getStringCellValue().trim().replace("-", "");
                                String lsRfc =
                                    loSheet.getRow(li).getCell(1) == null ? "" :
                                    loSheet.getRow(li).getCell(1).getStringCellValue().trim().replace("-", "");
                                lsRfcGlobal = lsRfc;
                                String lsDescription = 
                                    loSheet.getRow(li).getCell(2) == null ? "" : 
                                    loSheet.getRow(li).getCell(2).getStringCellValue().replace("'", "''");
                                /*String lsMail = 
                                    loSheet.getRow(li).getCell(3) == null ? "" : 
                                    loSheet.getRow(li).getCell(3).getStringCellValue().replace("'", "''");
                                */
                                Integer liIdClient = loPhmTrxProvidersDao.getIdClientByRfc(lsRfcClient);
                                                                
                                //System.out.println("["+li+"]["+lbBrk+"]["+liId+"]["+lsRfc+"] - " + lsDescription);
                                loPhmTrxProvidersBean.setLiIdProvider(liId);
                                loPhmTrxProvidersBean.setLiIdClientSat(liIdClient);
                                loPhmTrxProvidersBean.setLiRequest(liIdRequest);
                                loPhmTrxProvidersBean.setLiIndYear(Integer.parseInt(lsYear));
                                loPhmTrxProvidersBean.setLsIndMonth(String.valueOf(lisMonth));
                                loPhmTrxProvidersBean.setLiIdLawFirm(1);
                                loPhmTrxProvidersBean.setLsIndCompany(lsDescription);
                                loPhmTrxProvidersBean.setLsIndRfcClient(lsRfcClient);
                                loPhmTrxProvidersBean.setLsIndRfc(lsRfc);
                                //loPhmTrxProvidersBean.setLsIndMail(lsMail);
                                loPhmTrxProvidersBean.setLsIndEstatus("A");
                                loPhmTrxProvidersBean.setLiCreatedBy(liIdUser);
                                loPhmTrxProvidersBean.setLiUpdateBy(liIdUser);
                                ResponseBean loResponseBean = loPhmTrxProvidersDao.insertPhmTrxClientsSat(loPhmTrxProvidersBean);
                                if(loResponseBean.getLsResponse().equalsIgnoreCase("ERROR")){
                                    OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                    loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                    loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                    loOlsClientUploadErrBean.setLiIdRowExcel(li);
                                    loOlsClientUploadErrBean.setLsIndDesError(loResponseBean.getLsMessageResponse());
                                    loOlsClientUploadErrBean.setLsIndTypeClient("PRV");
                                    loOlsClientUploadErrBean.setLsIndEstatus("A");
                                    loOlsClientUploadErrBean.setLsIndFileName(lsFileName);   
                                    loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                    loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                    liCountError++;
                                }
                            }catch(Exception loEx){
                                OlsClientUploadErrBean loOlsClientUploadErrBean = new OlsClientUploadErrBean();
                                loOlsClientUploadErrBean.setLiIdNumCreatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdNumLastUpdatedBy(liIdUser);
                                loOlsClientUploadErrBean.setLiIdRequest(liIdRequest);
                                loOlsClientUploadErrBean.setLiIdRowExcel(li);
                                loOlsClientUploadErrBean.setLsIndDesError(loEx.getMessage());
                                loOlsClientUploadErrBean.setLsIndTypeClient("PRV");
                                loOlsClientUploadErrBean.setLsIndEstatus("A");
                                loOlsClientUploadErrBean.setLsIndFileName(lsFileName);
                                loOlsClientUploadErrBean.setLsIndRfc(lsRfcGlobal);
                                loViewObjectDao.insertUploadErrClients(loOlsClientUploadErrBean);
                                liCountError++;
                            }
                        }else{
                            lbBrk = false;
                        }
                        li++;
                    };
                }catch(Exception loExp){
                    System.out.println("Error en la hoja "+loExp.getMessage());
                }finally{
                    System.gc();
                }
                System.out.println("FIN");
                //}
            } catch (IOException loEx) {
                System.gc();
                System.out.println("Error al convertir a XSSFWorkbook "+loEx.getMessage());
            }
        }
        if(liCountError > 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Existen Registros que No Pudieron completar la Transacción, \n" +
                "favor de revisar pantalla de Incidencias con solicitud ["+liIdRequest+"] ");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Finalizado Satisfactoriamente! ");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        System.out.println("FIN DE CLASE - Proveedores " + new Date());
        return null;
    }
    
    public void setPoYearPre(RichInputNumberSpinbox poYearPre) {
        this.poYearPre = poYearPre;
    }

    public RichInputNumberSpinbox getPoYearPre() {
        return poYearPre;
    }

    public void setPoSelMonthPre(RichSelectOneChoice poSelMonthPre) {
        this.poSelMonthPre = poSelMonthPre;
    }

    public RichSelectOneChoice getPoSelMonthPre() {
        return poSelMonthPre;
    }

    public void setPoFileSatPre(RichInputFile poFileSatPre) {
        this.poFileSatPre = poFileSatPre;
    }

    public RichInputFile getPoFileSatPre() {
        return poFileSatPre;
    }

    public void setPoNoLocDelete(RichSelectBooleanCheckbox poNoLocDelete) {
        this.poNoLocDelete = poNoLocDelete;
    }

    public RichSelectBooleanCheckbox getPoNoLocDelete() {
        return poNoLocDelete;
    }

    public void showLastInfoPopup(ActionEvent actionEvent) {
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        String lsLastDate = loViewObjectDao.getLastModifyNoLocalizados();
        
        FacesMessage loMsg;
        loMsg = new FacesMessage("Fecha de \u00daltima Actualizaci\u00f3n ("+lsLastDate+") ");
        loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, loMsg);
        
    }
    
    public boolean validateFileName(String tsFileName, String tsFlePrefix){
        boolean lbRes = true;
        if(!tsFileName.contains(tsFlePrefix)){
            lbRes = false;
        }
        /*String laFileArr[] = tsFileName.split("\\_");
        if(laFileArr.length > 0){
            if(!laFileArr[0].equalsIgnoreCase(tsFlePrefix)){
                lbRes = false;
            }
        }else{
            lbRes = false;
        }*/
        return lbRes;
    }
    
    public boolean validateFileNameBak(String tsFileName, String tsFlePrefix){
        boolean lbRes = true;
        String laFileArr[] = tsFileName.split("\\_");
        if(laFileArr.length > 0){
            if(!laFileArr[0].equalsIgnoreCase(tsFlePrefix)){
                lbRes = false;
            }
        }else{
            lbRes = false;
        }
        return lbRes;
    }

    public String viewXlsNoLocalizados() {
        redirectionAction("NO-LOCALIZADOS");
        return null;
    }
    
    
    public void redirectionAction(String tsType){
        FacesContext        loContext = FacesContext.getCurrentInstance();
        ExternalContext     loEctx = loContext.getExternalContext();        
        String              lsUrl = 
            loEctx.getRequestContextPath() + "/faces/homePage";
        if(tsType.equalsIgnoreCase("NO-LOCALIZADOS")){
            lsUrl = "https://drive.google.com/file/d/1QDV5JpUVSc_aUtOgEE1DUPyM-75xxHZp/view?usp=sharing";
        }
                
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException e) {
            System.out.println("Error al ingresar a "+lsUrl);
        }
    }

    public void showDeleteProvPopup(ActionEvent actionEvent) {
        getPoDeleteIdBinding().setValue("PROVEEDORES");        
        getPoDeleteMsgLbl().setLabel("Eliminar los registros de PROVEEDORES");        
        new UtilFaces().showPopup(getPoPopupDelete());
    }
    
    public void showDeleteCliPopup(ActionEvent actionEvent) {
        getPoDeleteIdBinding().setValue("CLIENTES");        
        getPoDeleteMsgLbl().setLabel("Eliminar los registros de CLIENTES");        
        new UtilFaces().showPopup(getPoPopupDelete());
    }


    public void setPoPopupDelete(RichPopup poPopupDelete) {
        this.poPopupDelete = poPopupDelete;
    }

    public RichPopup getPoPopupDelete() {
        return poPopupDelete;
    }

    public void setPoDeleteMsgLbl(RichPanelLabelAndMessage poDeleteMsgLbl) {
        this.poDeleteMsgLbl = poDeleteMsgLbl;
    }

    public RichPanelLabelAndMessage getPoDeleteMsgLbl() {
        return poDeleteMsgLbl;
    }

    public void setPoDeleteIdBinding(RichOutputText poDeleteIdBinding) {
        this.poDeleteIdBinding = poDeleteIdBinding;
    }

    public RichOutputText getPoDeleteIdBinding() {
        return poDeleteIdBinding;
    }

    public String deleteAction() {
        String                   lsType = 
        getPoDeleteIdBinding().getValue() == null ? "" : 
        getPoDeleteIdBinding().getValue().toString();
        String lsTabla = "OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB";
        if(lsType.equalsIgnoreCase("CLIENTES")){
            lsTabla = "OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB";
        }
        ViewObjectDao loDao = new ViewObjectDao();
        try{
            loDao.deleteAllFromTable(lsTabla);    
        }catch(Exception loEx){
            System.out.println("Error al eliminar "+loEx.getMessage());
        }
        
        FacesMessage loMsg;
        loMsg = new FacesMessage("["+lsType+"] Se han eliminado correctamente ");
        loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, loMsg);
        
        new UtilFaces().hidePopup(getPoPopupDelete());
        
        return null;
    }

    public String cancelDeleteAction() {
        new UtilFaces().hidePopup(getPoPopupDelete());
        return null;
    }
    
    public boolean contieneValor(String tsDato, Integer tilongitud){
        boolean lbRes = true;
        if(tsDato == null){
            lbRes = false;
        }else{
            if(tsDato.length() <= tilongitud){
                lbRes = false;
            }
        }
        return lbRes;
    }

    public String createAllReportsAction() {
        try {
            System.out.println("Iniciar generar reportes");
            reportsXlsx();
            System.out.println("FIN de generar reportes");
        } catch (Exception e) {
            System.out.println("Excepcion al generar reoprtes: "+e.getMessage());
        }
        return null;
    }
    
    public void writeExcel() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Hoja excel");

        String[] headers = new String[]{
                "Producto",
                "Precio",
                "Enlace"
        };

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
        }

        for (int i = 0; i < 10; ++i) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            dataRow.createCell(0).setCellValue("Hola["+i+"]");
            dataRow.createCell(1).setCellValue("bambi["+i+"]");
            dataRow.createCell(2).setCellValue("venado["+i+"]");
        }

        FileOutputStream file = new FileOutputStream("D:\\jorge82lbs\\data.xls");
        workbook.write(file);
        file.close();
    }
    
    public void reportsXlsx() {
        // Creamos el archivo donde almacenaremos la hoja
        // de calculo, recuerde usar la extension correcta,
        // en este caso .xlsx
        File archivo = new File("D:\\jorge82lbs\\dataXLSX.xlsx");
        
        // Creamos el libro de trabajo de Excel formato OOXML
        Workbook workbook = new XSSFWorkbook();

        // La hoja donde pondremos los datos
        Sheet pagina = workbook.createSheet("Proveedores");
        // Creamos el estilo paga las celdas del encabezado
        CellStyle style = workbook.createCellStyle();
        // Indicamos que tendra un fondo azul aqua
        // con patron solido del color indicado
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] titulos = {"Identificador", "Consumos",
            "Precio Venta", "Precio Compra"};
        Double[] datos = {1.0, 10.0, 45.5, 25.50};

        // Creamos una fila en la hoja en la posicion 0
        Row fila = pagina.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        // Creamos el encabezado
        for (int i = 0; i < titulos.length; i++) {
            // Creamos una celda en esa fila, en la posicion 
            // indicada por el contador del ciclo
            Cell celda = fila.createCell(i);

            // Indicamos el estilo que deseamos 
            // usar en la celda, en este caso el unico 
            // que hemos creado
            celda.setCellStyle(headerStyle);
            celda.setCellValue(titulos[i]);
        }

        // Ahora creamos una fila en la posicion 1
        fila = pagina.createRow(1);

        // Y colocamos los datos en esa fila
        for (int i = 0; i < datos.length; i++) {
            // Creamos una celda en esa fila, en la
            // posicion indicada por el contador del ciclo
            Cell celda = fila.createCell(i);
            celda.setCellValue(datos[i]);
        }

        // Ahora guardaremos el archivo
        try {
            // Creamos el flujo de salida de datos,
            // apuntando al archivo donde queremos 
            // almacenar el libro de Excel
            FileOutputStream salida = new FileOutputStream(archivo);

            // Almacenamos el libro de 
            // Excel via ese 
            // flujo de datos
            workbook.write(salida);

            // Cerramos el libro para concluir operaciones
            workbook.close();

            System.out.println("Archivo creado existosamente en: "+archivo.getAbsolutePath());

        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            System.out.println("Error de entrada/salida");
        }
    }
    
    public void tempo(){
        List<ClienteBean> loLista = new ArrayList<ClienteBean>();
        ReportBlacklistDao loReportBlacklistDao = new ReportBlacklistDao();
        loLista = loReportBlacklistDao.getAllClients();
        System.out.println("Desde la bd");
        for(ClienteBean loCliente: loLista){
            System.out.println(loCliente);
        }
        System.out.println("");
        System.out.println("Ordenado");
        
        
    }

}

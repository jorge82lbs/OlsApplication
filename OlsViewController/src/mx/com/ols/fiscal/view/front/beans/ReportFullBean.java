/**
* Project: OLS - Administración de Clientes
*
* File: ReportFullBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans;


import oracle.adf.view.rich.component.rich.RichPanelWindow;
import oracle.adf.view.rich.component.rich.input.RichInputDate;

import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.poi.util.IOUtils;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import java.sql.Connection;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;

import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.sql.DataSource;

import mx.com.ols.fiscal.view.front.beans.cron.ReportBlacklistAllCron;
import mx.com.ols.fiscal.view.front.beans.mail.OlsMailBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatConfigParamTab;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxSatAllegedDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxSatDefinitiveDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxSatUnfulfilledDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.ViewObjectDao;
import mx.com.ols.fiscal.view.front.beans.types.ExecuteServiceResponseBean;
import mx.com.ols.fiscal.view.front.beans.types.ParReportBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatAllegedBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatDefinitiveBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatUnfulfilledBean;
import mx.com.ols.fiscal.view.front.beans.types.ProcessServiceBean;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import net.sf.jasperreports.engine.JasperExportManager;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

import net.sf.jasperreports.engine.util.JRLoader;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;

import oracle.jbo.client.Configuration;
import oracle.jbo.format.DefaultDateFormatter;

import oracle.jbo.format.FormatErrorException;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

/** Esta clase gestiona la pantalla de Reporte Completo<br/>
 *
 * @author Jorge Luis Bautista
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class ReportFullBean {
    private RichInputText poFilterRfc;
    private RichInputText poFilterCliente;
    private RichSelectBooleanCheckbox poFilterChkClean;
    private RichSelectBooleanCheckbox poFilterChkPre;
    private RichSelectBooleanCheckbox poFilterChkDef;
    private RichSelectBooleanCheckbox poFilterChkInc;
    private RichTable poTblMain;
    String gsEntityIterator = "OlsSatClientsFullVwView1Iterator";
    private RichPopup poPopupSendMail;
    private RichInputText poSendIdClient;
    private RichInputText poSendRfc;
    private RichInputText poSendCompany;
    private RichInputText poSendPresunto;
    private RichInputText poSendDefinitivo;
    private RichInputText poSendIncumplido;
    private RichTable poTblProv;
    private RichPopup poPopupProv;
    private RichInputText poRfcProv;
    private RichInputText poCteProv;
    private RichInputDate poDownFecRev;
    private RichInputDate poDownFecRec;
    private RichInputDate poDownFecDof;
    private RichInputDate poDownFecSat;
    private RichInputText poDownCompany;
    private RichInputText poDownRfc;
    private RichInputText poDownIdClient;
    private RichPopup poPopupDownFile;
    private RichPanelWindow poDownPnlWin;
    private RichInputText poDownType;
    private RichInputText poDownNumProvs;
    private RichInputText poRfcCom;
    private RichInputText poCteCom;
    private RichPopup poPopupProvInfo;
    private RichInputText poRfcByTbl;
    private UIXIterator poPresuntosIterator;
    private RichPopup poPopupDefInfo;
    private RichInputText poRfcDef;
    private RichInputText poCteDef;
    private UIXIterator poDefIterator;
    private RichPopup poPopupIncInfo;
    private RichInputText poRfcInc;
    private RichInputText poCteInc;
    private UIXIterator poIncIterator;

    public void setPoTblMain(RichTable poTblMain) {
        this.poTblMain = poTblMain;
    }

    public RichTable getPoTblMain() {
        return poTblMain;
    }
    
    public ReportFullBean() {
    }

    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterChkClean().setValue(false);
        getPoFilterChkDef().setValue(false);
        getPoFilterChkInc().setValue(false);
        getPoFilterChkPre().setValue(false);
        getPoFilterCliente().setValue("");
        getPoFilterRfc().setValue("");
    }

    public String refreshMainTable() {
        new UtilFaces().refreshTableWhereIterator("1 = 1 ", gsEntityIterator,getPoTblMain());        
        return null;
    }

    public void setPoFilterRfc(RichInputText poFilterRfc) {
        this.poFilterRfc = poFilterRfc;
    }

    public RichInputText getPoFilterRfc() {
        return poFilterRfc;
    }

    public void setPoFilterCliente(RichInputText poFilterCliente) {
        this.poFilterCliente = poFilterCliente;
    }

    public RichInputText getPoFilterCliente() {
        return poFilterCliente;
    }

    public void setPoFilterChkClean(RichSelectBooleanCheckbox poFilterChkClean) {
        this.poFilterChkClean = poFilterChkClean;
    }

    public RichSelectBooleanCheckbox getPoFilterChkClean() {
        return poFilterChkClean;
    }

    public void selectCleanAction(ValueChangeEvent toValueChangeEvent) {
        String lsSel = toValueChangeEvent.getNewValue() == null ? null:
                       toValueChangeEvent.getNewValue().toString();
        if(lsSel.equalsIgnoreCase("true")){
            getPoFilterChkDef().setValue(false);
            getPoFilterChkInc().setValue(false);
            getPoFilterChkPre().setValue(false);
        }
    }

    public void setCleanChecBox3Action(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkPre(RichSelectBooleanCheckbox poFilterChkPre) {
        this.poFilterChkPre = poFilterChkPre;
    }

    public RichSelectBooleanCheckbox getPoFilterChkPre() {
        return poFilterChkPre;
    }

    public void setCleanChecBox2Action(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkDef(RichSelectBooleanCheckbox poFilterChkDef) {
        this.poFilterChkDef = poFilterChkDef;
    }

    public RichSelectBooleanCheckbox getPoFilterChkDef() {
        return poFilterChkDef;
    }

    public void setCleanChecBoxAction(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkInc(RichSelectBooleanCheckbox poFilterChkInc) {
        this.poFilterChkInc = poFilterChkInc;
    }

    public RichSelectBooleanCheckbox getPoFilterChkInc() {
        return poFilterChkInc;
    }

    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        String lsFilterRfc = 
            getPoFilterRfc().getValue() == null ? "": 
            getPoFilterRfc().getValue().toString();        
        if(!lsFilterRfc.equalsIgnoreCase("")){
            lsQuery += " AND IND_RFC = '" + lsFilterRfc + "'";
        }
                
        String lsFilterCliente = 
            getPoFilterCliente().getValue() == null ? "": 
            getPoFilterCliente().getValue().toString();        
        if(!lsFilterCliente.equalsIgnoreCase("")){
            lsQuery += " AND IND_COMPANY = '" + lsFilterCliente + "'";
        }
        
        String lsFilterClean = 
                getPoFilterChkClean().getValue() == null ? "": 
                getPoFilterChkClean().getValue().toString();   
        String lsValClean = "0";
        if(!lsFilterClean.equalsIgnoreCase("")){
            if(lsFilterClean.equalsIgnoreCase("true")){
                lsValClean = "1";
                lsQuery += " AND ALLEGED = 0 AND DEFINITIVE = 0 AND UNFULFILLED = 0 ";
            }            
        }
        if(lsValClean.equalsIgnoreCase("0")){
            String lsFilterInc = 
                getPoFilterChkInc().getValue() == null ? "": 
                getPoFilterChkInc().getValue().toString();        
            if(!lsFilterInc.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterInc.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND UNFULFILLED = " + lsVal;
                }
                
            }
            String lsFilterDfn = 
                getPoFilterChkDef().getValue() == null ? "": 
                getPoFilterChkDef().getValue().toString();        
            if(!lsFilterDfn.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterDfn.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND DEFINITIVE = " + lsVal;
                }
                
            }
            String lsFilterPre = 
                    getPoFilterChkPre().getValue() == null ? "": 
                    getPoFilterChkPre().getValue().toString();        
            if(!lsFilterPre.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterPre.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND ALLEGED = "+lsVal;
                }
            }
        }
        new UtilFaces().refreshTableWhereIterator(lsQuery, gsEntityIterator, getPoTblMain());
        
        return null;
    }
    
    public void setValueCleanCheckbox(ValueChangeEvent toValueChangeEvent){
        String lsSel = toValueChangeEvent.getNewValue() == null ? null:
                       toValueChangeEvent.getNewValue().toString();
        if(lsSel.equalsIgnoreCase("true")){
            getPoFilterChkClean().setValue(false);
        }
    }

    public void showSendMailPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        
        String                   tsId = 
            loNode.getAttribute("IdClientSat") == null ? "" : 
            loNode.getAttribute("IdClientSat").toString();   
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();   
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString();          
        String                   tsPresunto = 
            loNode.getAttribute("Alleged") == null ? null : 
            loNode.getAttribute("Alleged").toString(); 
        String                   tsDefinitivo = 
            loNode.getAttribute("Definitive") == null ? null : 
            loNode.getAttribute("Definitive").toString(); 
        String                   tsIncumplido = 
            loNode.getAttribute("Unfulfilled") == null ? null : 
            loNode.getAttribute("Unfulfilled").toString(); 
        String tsTypes = "";
        if(tsPresunto != null){
            tsTypes += "|PRESUNTO|";
        }
        if(tsDefinitivo != null){
            tsTypes += "|DEFININTIVO|";
        }
        if(tsIncumplido != null){
            tsTypes += "|INCUMPLIDO|";
        }
        
        //Validar si existe configuracion para poder enviar correo
        if(!validateMailConfiguration(tsRfc)){
            FacesMessage loMsg;
            loMsg = new FacesMessage("Configuracion Insuficiente Para EnvIo de Correo");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }else{
            //show popup
            getPoSendIdClient().setValue(tsId);
            getPoSendCompany().setValue(tsClient);
            getPoSendRfc().setValue(tsRfc);
            getPoSendDefinitivo().setValue(tsDefinitivo);
            getPoSendIncumplido().setValue(tsIncumplido);
            getPoSendPresunto().setValue(tsPresunto);
            
            new UtilFaces().showPopup(getPoPopupSendMail());
            
        }        
    }

    public void setPoPopupSendMail(RichPopup poPopupSendMail) {
        this.poPopupSendMail = poPopupSendMail;
    }

    public RichPopup getPoPopupSendMail() {
        return poPopupSendMail;
    }

    public void setPoSendIdClient(RichInputText poSendIdClient) {
        this.poSendIdClient = poSendIdClient;
    }

    public RichInputText getPoSendIdClient() {
        return poSendIdClient;
    }

    public void setPoSendRfc(RichInputText poSendRfc) {
        this.poSendRfc = poSendRfc;
    }

    public RichInputText getPoSendRfc() {
        return poSendRfc;
    }

    public void setPoSendCompany(RichInputText poSendCompany) {
        this.poSendCompany = poSendCompany;
    }

    public RichInputText getPoSendCompany() {
        return poSendCompany;
    }

    public void setPoSendPresunto(RichInputText poSendPresunto) {
        this.poSendPresunto = poSendPresunto;
    }

    public RichInputText getPoSendPresunto() {
        return poSendPresunto;
    }

    public void setPoSendDefinitivo(RichInputText poSendDefinitivo) {
        this.poSendDefinitivo = poSendDefinitivo;
    }

    public RichInputText getPoSendDefinitivo() {
        return poSendDefinitivo;
    }

    public void setPoSendIncumplido(RichInputText poSendIncumplido) {
        this.poSendIncumplido = poSendIncumplido;
    }

    public RichInputText getPoSendIncumplido() {
        return poSendIncumplido;
    }

    @SuppressWarnings("unchecked")
    public String sendMailAction() {
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
               
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();   
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString();   
        
        //Valida si existe al menos un correo configurado
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        Integer liNumMails = loViewObjectDao.getNumMailsConfigurated(tsRfc);
        
        if(liNumMails <= 0){
            FacesMessage loMsg;
            loMsg = new FacesMessage("El Cliente ["+tsClient+"] no tiene direcciones de correo configurados");
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        else{
      
        String tsYear = "2018";
        String lsDefinitivo = "";          
        String lsIncumplido = "";           
        String lsPresunto = "";
        String tsTypes = lsDefinitivo + lsIncumplido + lsPresunto;
        tsTypes += "<br>";
        if(getListDefinitiveInfo().size() > 0){
            tsTypes += "<b>&nbsp;&nbsp;&nbsp;&nbsp; - DEFINITIVOS</b> <br>";
        }
        if(getListAllegedInfo().size() > 0){
            tsTypes += "<b>&nbsp;&nbsp;&nbsp;&nbsp; - PRESUNTOS</b> <br>";
        }
        if(getListIncumplidosInfo().size() > 0){
            tsTypes += "<b>&nbsp;&nbsp;&nbsp;&nbsp; - INCUMPLIDOS</b> <br>";
        }
        Date ltFecRec = 
            getPoDownFecRec().getValue() == null ? null : 
            (Date)getPoDownFecRec().getValue();
        Date ltFecRev = 
            getPoDownFecRev().getValue() == null ? null : 
            (Date)getPoDownFecRev().getValue();
        Date ltFecSat = 
            getPoDownFecSat().getValue() == null ? null : 
            (Date)getPoDownFecSat().getValue();
        Date ltFecDof = 
            getPoDownFecDof().getValue() == null ? null : 
            (Date)getPoDownFecDof().getValue();
        
        Integer liNumProvs = loViewObjectDao.getNumProvsByClient(tsRfc);
        
        ParReportBean loPar = new ParReportBean();
        loPar.setLsClient("<b>"+tsClient+"</b>");
        loPar.setLsRfc(tsRfc);
        loPar.setLsYear(tsYear);
        loPar.setLiNumProvs(liNumProvs);
        loPar.setLsTypes(tsTypes);
        loPar.setLsEmail("jorge82lbs@gmail.com");
        loPar.setLtFecRec(ltFecRec);
        loPar.setLtFecRev(ltFecRev);
        loPar.setLtFecSat(ltFecSat);
        loPar.setLtFecDof(ltFecDof);
        String lsLugaryFecha = getLugaryFecha();       
        loPar.setLsLugar(lsLugaryFecha);
        new UtilFaces().hidePopup(getPoPopupSendMail());
        
        System.out.println("Convirtiedo a PDF");
        Map<String, Object> loMap = new HashMap();        
        DefaultDateFormatter ddf = new DefaultDateFormatter();        
        String lsFecRec = "NA";
        String lsFecRev = "NA";
        String lsFecSat = "NA";
        String lsFecDof = "NA";
        try {
            lsFecRec = 
                ltFecRec == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRec);
            lsFecRev = 
                ltFecRev == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRev); 
            lsFecSat = 
                ltFecSat == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecSat); 
            lsFecDof = 
                ltFecDof == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecDof); 
            
        } catch (FormatErrorException e) {
            ;
        }
        loMap.put("pstRfc", tsRfc);
        loMap.put("pstRfcDescription", tsClient);
        loMap.put("pstNumProv", liNumProvs.toString());
        loMap.put("pstFecRec", lsFecRec);
        loMap.put("pstFecRev", lsFecRev);
        loMap.put("pstFecSat", lsFecSat);
        loMap.put("pstFecDof", lsFecDof);
        loMap.put("pstLugaryFecha", lsLugaryFecha);
        
        String lsOrdid = "ReporteDetallado";
        String lsResponse = "";
        try {
            lsResponse = 
                runReportLocal("olsDatails.jasper", loMap, lsOrdid);
        } catch (Exception loEx) {                
            loEx.printStackTrace();
            System.out.println("ERROR al ejecutar reporte");
            System.out.println("ERROR: "+loEx.getMessage());
        }
        loPar.setLsRutaPdf(lsResponse);
        System.out.println("Convirtiedo a PDF...FIN");
        
        try {
            OlsMailBean loOlsMailBean = new OlsMailBean();
            loOlsMailBean.sendMailSimple(loPar);
            FacesMessage loMsg;
            loMsg = new FacesMessage("Correo Enviado Satisfactoriamente");
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        } catch (NoSuchProviderException e) {
            FacesMessage loMsg;
            loMsg = new FacesMessage("Error1 al enviar correo "+e.getMessage());
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
        } catch (AddressException e) {
            FacesMessage loMsg;
            loMsg = new FacesMessage("Error2 al enviar correo "+e.getMessage());
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
        } catch (MessagingException e) {
            FacesMessage loMsg;
            loMsg = new FacesMessage("Error3 al enviar correo "+e.getMessage());
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
        }
        
    }
        
        return null;
    }

    public String cancelSendMailAction() {
        new UtilFaces().hidePopup(getPoPopupSendMail());
        return null;
    }
    
    public  boolean validateMailConfiguration(String tsRfc){
        boolean lbReturn = true;
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        Integer liHdr = loViewObjectDao.validateHdrEmailConfig();
        if(liHdr != 4){
            lbReturn = false;
        }
        //Validar destinatarios
        Integer liMails = loViewObjectDao.validateEmailDestiny(tsRfc);
        if(liMails == 0){
            lbReturn = false;
        }
        return lbReturn;
    }

    public void showProvidersFullPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcProv().setValue(tsRfc);
        getPoCteProv().setValue(tsClient);
        String lsEntityIterator = "OlsSatProvidersFullVwView1Iterator";
        String lsWhere = "IND_RFC_CLIENT = '"+tsRfc+"'";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIterator,getPoTblProv());        
        new UtilFaces().showPopup(getPoPopupProv());
    }
    
    public void showProvidersPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcProv().setValue(tsRfc);
        getPoCteProv().setValue(tsClient);
        String lsEntityIterator = "OlsSatProvidersFullVwView1Iterator";
        String lsWhere = "IND_RFC_CLIENT = '"+tsRfc+"'";
        lsWhere += " AND (ALLEGED + DEFINITIVE + UNFULFILLED) > 0";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIterator,getPoTblProv());        
        new UtilFaces().showPopup(getPoPopupProv());
    }

    public void setPoTblProv(RichTable poTblProv) {
        this.poTblProv = poTblProv;
    }

    public RichTable getPoTblProv() {
        return poTblProv;
    }

    public void setPoPopupProv(RichPopup poPopupProv) {
        this.poPopupProv = poPopupProv;
    }

    public RichPopup getPoPopupProv() {
        return poPopupProv;
    }

    public void setPoRfcProv(RichInputText poRfcProv) {
        this.poRfcProv = poRfcProv;
    }

    public RichInputText getPoRfcProv() {
        return poRfcProv;
    }

    public void setPoCteProv(RichInputText poCteProv) {
        this.poCteProv = poCteProv;
    }

    public RichInputText getPoCteProv() {
        return poCteProv;
    }
        
    
    /**
    * Ejecuta la invocacion con jasperreports para generar el reporte
    * @autor 
    * @param tsRepPath
    * @param tsMapParams
    * @param tsFileName
    * @return void
    */
    
   public void runReport(String tsRepPath, 
                         java.util.Map<String, Object> tsMapParams,
                         String tsFileName) throws Exception {
       Connection loCnn = null;
       try {
           HttpServletResponse   loResponse = getResponse();
           ServletOutputStream   loOutput = loResponse.getOutputStream();
           loResponse.setHeader("Cache-Control", "max-age=0");
           ServletContext        loContext = getContext();
           InputStream           loInputStream =
               loContext.getResourceAsStream("/reports/" + tsRepPath);
           JasperReport          loTemplate = 
               (JasperReport)JRLoader.loadObject(loInputStream);
           
           loTemplate.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
           loCnn = getConnection();
           JasperPrint           loPrint =
               JasperFillManager.fillReport(loTemplate, tsMapParams, loCnn);
           ByteArrayOutputStream loBaos = new ByteArrayOutputStream();
           JasperExportManager.exportReportToPdfStream(loPrint, loBaos);
           loResponse.setHeader("Cache-Control", "max-age=0");
           loResponse.setContentType("application/force-download");
           loResponse.setHeader("Content-Disposition",
                              "attachment; " + "filename=" + tsFileName +
                              ".pdf");
           loOutput.write(loBaos.toByteArray());
           loOutput.flush();
           loOutput.close();           
           System.out.println("fin");
           FacesContext.getCurrentInstance().responseComplete();
       } catch (Exception loExJsr) {
           System.out.println("ERROR EN el JASPER 01: "+loExJsr.getMessage());
           loExJsr.printStackTrace();
       } finally {
           close(loCnn);
       }
   }
    
    /**
    * Ejecuta la invocacion con jasperreports para generar el reporte
    * @autor 
    * @param tsJasperPath
    * @param tsMapParams
    * @param tsFileName
    * @return void
    */
    /*
    public void runReportFile(String tsJasperPath,
                             java.util.Map tsMapParams,
                             String tsFileName) throws Exception {
       Connection loCnn = null;
       try {
           
           String ruta = "C:\\Users\\JorgeOWM\\Desktop\\"+tsFileName+".pdf";
           ServletContext        loContext = getContext();
           InputStream           loInputStream =
               loContext.getResourceAsStream("/reports/" + tsJasperPath);
           JasperReport          loJasper = 
               (JasperReport)JRLoader.loadObject(loInputStream);
           loJasper.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
           loCnn = getConnection();
           JasperPrint           loPrint =
               JasperFillManager.fillReport(loJasper, tsMapParams, loCnn);           
           JasperExportManager.exportReportToPdfFile(loPrint, ruta);
           System.out.println("fin");
           FacesContext.getCurrentInstance().responseComplete();
       } catch (Exception loExJsr) {
           System.out.println("ERROR EN el JASPER: "+loExJsr.getMessage());
           loExJsr.printStackTrace();
       } finally {
           close(loCnn);
       }
       
    }
*/
    public void close(Connection toCnn) {
       if (toCnn != null) {
           try {
               toCnn.close();
           } catch (Exception loEx) {
               ;
           }
       }
   }
    public HttpServletResponse getResponse() {
        return (HttpServletResponse)getFacesContext().getExternalContext().getResponse();
    }
    public static FacesContext getFacesContext() {
       return FacesContext.getCurrentInstance();
    }
    public ServletContext getContext() {
        return (ServletContext)getFacesContext().getExternalContext().getContext();
    }
    private Connection getConnection() throws Exception {
        try {
            InitialContext  loInitialContext = new InitialContext();            
            DataSource      loDtSrc = 
                (DataSource)loInitialContext.lookup("java:comp/env/jdbc/legalSolutionsDS");
            Connection      loCnn = loDtSrc.getConnection();
            return loCnn;
        } catch (JboException loEx) {
            loEx.printStackTrace();
            return null;
        }
    }   
    @SuppressWarnings("unchecked")
    public String reportActionBAK() {
        Map loMap = new HashMap();
        loMap.put("pstRfc", "TSO970120MQ0");
        loMap.put("pstRfcDescription", "VWF DE MEXICO, S.A. DE C.V.");
        loMap.put("pstNumProv", "5");
        loMap.put("pstFecRec", "2018-05-09");
        loMap.put("pstFecRev", "2018-05-10");
        loMap.put("pstFecSat", "2018-05-11");
        loMap.put("pstFecDof", "2018-05-12");
        String lsOrdid = "ReporteDetallado";
        try {
            System.out.println("Generando reporte");
            //runReportFile("ListasSATDetallado.jasper", loMap, lsOrdid);
        } catch (Exception loEx) {                
            loEx.printStackTrace();
            System.out.println("ERROR al ejecutar reporte");
            System.out.println("ERROR: "+loEx.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void reportDetailAction(FacesContext facesContext, OutputStream outputStream) {
        boolean lbProcess = true;
        String lsFieldErrorRq = "";
        String lsTypeReport = 
            getPoDownType().getValue() == null ? "" : 
            getPoDownType().getValue().toString();
        if(lsTypeReport.equalsIgnoreCase("D")){
            String lsRfc =           
                getPoDownRfc().getValue() == null ? "" : 
                getPoDownRfc().getValue().toString();
            String lsRfcDescription =
                getPoDownCompany().getValue() == null ? "" : 
                getPoDownCompany().getValue().toString();
            String lsNumProv = 
                getPoDownNumProvs().getValue() == null ? "" : 
                getPoDownNumProvs().getValue().toString();
            String lsFecRec = 
                getPoDownFecRec().getValue() == null ? "" : 
                getPoDownFecRec().getValue().toString();
            String lsFecRev = 
                getPoDownFecRev().getValue() == null ? "" : 
                getPoDownFecRev().getValue().toString();
            String lsFecSat = 
                getPoDownFecSat().getValue() == null ? "" : 
                getPoDownFecSat().getValue().toString();
            String lsFecDof = 
                getPoDownFecDof().getValue() == null ? "" : 
                getPoDownFecDof().getValue().toString();
            String lsLugaryFecha = getLugaryFecha();        
    
            if(lsFecRec.length() < 1){
                lsFieldErrorRq += "Fecha RecepciÃ³n,";
                lbProcess = false;   
            }
            if(lsFecRev.length() < 1){
                lsFieldErrorRq += "Fecha Revisi\u00f3n,";
                lbProcess = false;   
            }
            if(lsFecSat.length() < 1){
                lsFieldErrorRq += "Fecha Publicaci\u00f3n SAT,";
                lbProcess = false;   
            }
            if(lsFecDof.length() < 1){
                lsFieldErrorRq += "Fecha Publicaci\u00f3n DOF,";
                lbProcess = false;   
            }
            
            if(lbProcess){        
                Map loMap = new HashMap();
                loMap.put("pstRfc", lsRfc);
                loMap.put("pstRfcDescription", lsRfcDescription);
                loMap.put("pstNumProv", lsNumProv);
                loMap.put("pstFecRec", lsFecRec);
                loMap.put("pstFecRev", lsFecRev);
                loMap.put("pstFecSat", lsFecSat);
                loMap.put("pstFecDof", lsFecDof);
                loMap.put("pstLugaryFecha", lsLugaryFecha);
                            
                String lsOrdid = "ReporteDetallado";
                try {
                    runReport("olsDatails.jasper", loMap, lsOrdid);
                } catch (Exception loEx) {                
                    loEx.printStackTrace();
                    System.out.println("ERROR al ejecutar reporte");
                    System.out.println("ERROR: "+loEx.getMessage());
                }
            }else{
                FacesMessage loMsg;
                loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                         lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
        }

    }

    public String readWordAction() {
        /*
        try {
            String lsPathWord = "C:\\Users\\JorgeOWM\\Desktop\\OLS\\REPORTE.docx";
            String lsPathPdf = "C:\\Users\\JorgeOWM\\Desktop\\OLS\\REPORTEPDF.pdf";
                       
            // 1) Load DOCX into WordprocessingMLPackage
            System.out.println("Load DOCX into WordprocessingMLPackage");
            InputStream is = new FileInputStream(new File(lsPathWord));
            System.out.println("Load DOCX into WordprocessingMLPackage...OK");
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(is);
            System.out.println("Prepare Pdf settings");
            // 2) Prepare Pdf settings
            PdfSettings pdfSettings = new PdfSettings();
            System.out.println("Prepare Pdf settings.......ok");
            // 3) Convert WordprocessingMLPackage to Pdf
            System.out.println("Convert WordprocessingMLPackage to Pdf");
            OutputStream out = new FileOutputStream(new File(lsPathPdf));
            System.out.println("Convert WordprocessingMLPackage to Pdf..........OK");
            PdfConversion converter = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(wordMLPackage);
            System.out.println("converter.............");
            converter.output(out, pdfSettings);        
            System.out.println("converter.............");
        } catch (Throwable e) {
            e.printStackTrace();
        }*/
        return null;
    }

    public void reportAction(FacesContext facesContext, OutputStream outputStream) {
        //Guardar la ruta y recuerden que se debe poner doble barra \\
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        List<OlsCatConfigParamTab> laOlsCatConfigParamTab = loViewObjectDao.getGeneralParametersByNom("PathContextWL");
        
        //String lsRuta = "C:\\Users\\JorgeOWM\\.jdeveloper\\system12.1.3.0.41.140521.1008\\DefaultDomain\\filesOls\\REPORTE.docx";
        String lsRuta = 
            laOlsCatConfigParamTab.get(0).getLsIndValueParameter() + 
            "filesOls\\REPORTE.docx";
        
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        System.out.println("tsClient["+tsClient+"]");
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "0" : 
            loNode.getAttribute("IndRfc").toString();
        System.out.println("tsRfc["+tsRfc+"]");
        
        DefaultDateFormatter ddf = new DefaultDateFormatter();
        Date ltFecRec = 
            getPoDownFecRec().getValue() == null ? null : 
            (Date)getPoDownFecRec().getValue();
        Date ltFecRev = 
            getPoDownFecRev().getValue() == null ? null : 
            (Date)getPoDownFecRev().getValue();
        Date ltFecSat = 
            getPoDownFecSat().getValue() == null ? null : 
            (Date)getPoDownFecSat().getValue();
        Date ltFecDof = 
            getPoDownFecDof().getValue() == null ? null : 
            (Date)getPoDownFecDof().getValue();

        String lsFecRec;
        try {
            lsFecRec = ltFecRec == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRec);
            System.out.println("lsFecRec["+lsFecRec+"]");
            
            String lsFecRev = 
                ltFecRev == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRev);            
            System.out.println("lsFecRev["+lsFecRev+"]");
            
            String lsFecSat = 
                ltFecSat == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecSat);            
            System.out.println("lsFecSat["+lsFecSat+"]");
            
            String lsFecDof = 
                ltFecDof == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecDof); 
            
            System.out.println("lsFecDof["+lsFecDof+"]");
            String lsLugaryFecha = getLugaryFecha();       
            Integer liNumProvs = loViewObjectDao.getNumProvsByClient(tsRfc);
            
        
            String lsReturn = "";
            //Se crea el objeto File con la ruta del archivo
            System.out.println("Se crea el objeto File con la ruta del archivo");
            File archivodoc = new File(lsRuta);
            FileInputStream fis;
            try {
                fis = new FileInputStream(archivodoc);
                InputStream entradaArch = fis; 
                System.out.println("InputStream ok");
                System.out.println("abriendo archivo para XWPFDocument");
                XWPFDocument doc = new XWPFDocument(entradaArch);
                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            if (text != null && text.contains("pstlugar")) {
                                text = text.replace("pstlugar", lsLugaryFecha);
                                r.setText(text, 0);
                            }
                        }
                    }
                }
                
                for (XWPFTable tbl : doc.getTables()) {
                   for (XWPFTableRow row : tbl.getRows()) {
                      for (XWPFTableCell cell : row.getTableCells()) {
                         for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                              String text = r.getText(0);
                              if (text != null && text.contains("pstCont")) {
                                text = text.replace("pstCont", tsClient);
                                r.setText(text,0);
                              }
                                if (text != null && text.contains("pstMail")) {
                                  text = text.replace("pstMail", tsRfc);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstMon")) {
                                  text = text.replace("pstMon", liNumProvs.toString());
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstRec")) {
                                  text = text.replace("pstRec", lsFecRec);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstRev")) {
                                  text = text.replace("pstRev", lsFecRev);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstSAT")) {
                                  text = text.replace("pstSAT", lsFecSat);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstDOF")) {
                                  text = text.replace("pstDOF", lsFecDof);
                                  r.setText(text,0);
                                }
                            }
                         }
                      }
                   }
                }
                //VOLVEMOS A IMPRIMIR EL DOCUMENTO POR CONSOLA Y COMPROBAMOS QUE SE HA MODIFICADO
                System.out.println("---------------------------");
                try{                                                                
                    lsReturn = "filesOls\\OlsEjecutivo.docx";
                    FileOutputStream fos = new FileOutputStream(lsReturn);
                    doc.write(fos);                
                    fos.close();                
                    //doc.close();
                }catch(IOException ex) {
                    System.out.println(ex.getMessage());                
                }
                System.out.println("descargar archivo....");
                String  tsFileName = "OlsEjecutivo.docx";
                try{
                    HttpServletResponse   loResponse = getResponse();
                    System.out.println("Obtiene ServletOutputStream");
                    //ServletOutputStream   loOutput = loResponse.getOutputStream();                
                    //loResponse.setHeader("Cache-Control", "max-age=0");
                    System.out.println("sett Response 2");
                    //loResponse.setContentType("application/force-download");
                    System.out.println("sett Response 3");
                    //loResponse.setHeader("Content-Disposition",
                    //                   "attachment; " + "filename=" + tsFileName +
                    //                   ".docx");
                    loResponse.setHeader("Content-Disposition",
                                         "attachment; filename=\"" + tsFileName+ "\"");
                    System.out.println("sett Response 4");
                    //ByteArrayOutputStream loBaos = new ByteArrayOutputStream();
                    doc.write(outputStream);
                    
                    //outputStream.write(loBaos.toByteArray());                
                    //IOUtils.copy(loBaos.toByteArray(), outputStream);
                    
                    System.out.println("sett Response 5");
                    outputStream.flush();
                    System.out.println("sett Response 6");
                    outputStream.close();           
                    System.out.println("fin");
                    //FacesContext.getCurrentInstance().responseComplete();                
                    
                }catch(Exception loEx){
                    System.out.println("Exception>>>>>>>>>>>: "+loEx.getMessage());
                }
                
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException>>>>>>>>>>>: "+e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException>>>>>>>>>>>>: "+e.getMessage());
            } 
        } catch (FormatErrorException e) {
            System.out.println("Error en format fecha " + e.getMessage());
        }  
        
        facesContext.responseComplete();
    }

    public void setPoDownFecRev(RichInputDate poDownFecRev) {
        this.poDownFecRev = poDownFecRev;
    }

    public RichInputDate getPoDownFecRev() {
        return poDownFecRev;
    }

    public void setPoDownFecRec(RichInputDate poDownFecRec) {
        this.poDownFecRec = poDownFecRec;
    }

    public RichInputDate getPoDownFecRec() {
        return poDownFecRec;
    }

    public void setPoDownFecDof(RichInputDate poDownFecDof) {
        this.poDownFecDof = poDownFecDof;
    }

    public RichInputDate getPoDownFecDof() {
        return poDownFecDof;
    }

    public void setPoDownFecSat(RichInputDate poDownFecSat) {
        this.poDownFecSat = poDownFecSat;
    }

    public RichInputDate getPoDownFecSat() {
        return poDownFecSat;
    }

    public void setPoDownCompany(RichInputText poDownCompany) {
        this.poDownCompany = poDownCompany;
    }

    public RichInputText getPoDownCompany() {
        return poDownCompany;
    }

    public void setPoDownRfc(RichInputText poDownRfc) {
        this.poDownRfc = poDownRfc;
    }

    public RichInputText getPoDownRfc() {
        return poDownRfc;
    }

    public void setPoDownIdClient(RichInputText poDownIdClient) {
        this.poDownIdClient = poDownIdClient;
    }

    public RichInputText getPoDownIdClient() {
        return poDownIdClient;
    }

    public String downFileAction() {
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoDownRfc().setValue(tsRfc);
        getPoDownCompany().setValue(tsClient);
        
        ViewObjectDao loViewObjectDao = new ViewObjectDao();
        Integer liNumProvs = loViewObjectDao.getNumProvsByClient(tsRfc);
        getPoDownNumProvs().setValue(liNumProvs);
        getPoDownPnlWin().setTitle("Descargar Archivo - Reporte Detallado");
        getPoDownType().setValue("D");
        new UtilFaces().showPopup(getPoPopupDownFile());
        return null;
    }

    public String cancelDownFileAction() {
        new UtilFaces().hidePopup(getPoPopupDownFile());
        return null;
    }

    public void setPoPopupDownFile(RichPopup poPopupDownFile) {
        this.poPopupDownFile = poPopupDownFile;
    }

    public RichPopup getPoPopupDownFile() {
        return poPopupDownFile;
    }

    public void setPoDownPnlWin(RichPanelWindow poDownPnlWin) {
        this.poDownPnlWin = poDownPnlWin;
    }

    public RichPanelWindow getPoDownPnlWin() {
        return poDownPnlWin;
    }

    public void setPoDownType(RichInputText poDownType) {
        this.poDownType = poDownType;
    }

    public RichInputText getPoDownType() {
        return poDownType;
    }

    public void setPoDownNumProvs(RichInputText poDownNumProvs) {
        this.poDownNumProvs = poDownNumProvs;
    }

    public RichInputText getPoDownNumProvs() {
        return poDownNumProvs;
    }
    
    public String getLugaryFecha(){
        String lsReturn = "";
        Calendar loCal = Calendar.getInstance();
        int lsYear = loCal.get(Calendar.YEAR);
        int mes = loCal.get(Calendar.MONTH) + 1;
        int dia = loCal.get(Calendar.DAY_OF_MONTH);
        
        String lsMonth = "";
        
        if(mes == 1){lsMonth = "Enero";}
        if(mes == 2){lsMonth = "Febrero";}
        if(mes == 3){lsMonth = "Marzo";}
        if(mes == 4){lsMonth = "Abril";}
        if(mes == 5){lsMonth = "Mayo";}
        if(mes == 6){lsMonth = "Junio";}
        if(mes == 7){lsMonth = "Julio";}
        if(mes == 8){lsMonth = "Agosto";}
        if(mes == 9){lsMonth = "Septiembre";}
        if(mes == 10){lsMonth = "Octubre";}
        if(mes == 11){lsMonth = "Noviembre";}
        if(mes == 12){lsMonth = "Diciembre";}
        
        lsReturn = "Estado de MÃ©xico a " + dia + " de "+lsMonth+" de " + lsYear;
        return lsReturn;
    }

    @SuppressWarnings("unchecked")
    public void reportDetailAction2(FacesContext facesContext, OutputStream outputStream) {
        Map loMap = new HashMap();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
               
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        System.out.println("tsClient["+tsClient+"]");
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "0" : 
            loNode.getAttribute("IndRfc").toString();
        System.out.println("tsRfc["+tsRfc+"]");
        
        DefaultDateFormatter ddf = new DefaultDateFormatter();
        Date ltFecRec = 
            getPoDownFecRec().getValue() == null ? null : 
            (Date)getPoDownFecRec().getValue();
        Date ltFecRev = 
            getPoDownFecRev().getValue() == null ? null : 
            (Date)getPoDownFecRev().getValue();
        Date ltFecSat = 
            getPoDownFecSat().getValue() == null ? null : 
            (Date)getPoDownFecSat().getValue();
        Date ltFecDof = 
            getPoDownFecDof().getValue() == null ? null : 
            (Date)getPoDownFecDof().getValue();
                        
        
        String lsFecRec;
        try {
            lsFecRec = ltFecRec == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRec);
            System.out.println("lsFecRec["+lsFecRec+"]");
            
            String lsFecRev = 
                ltFecRev == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecRev);            
            System.out.println("lsFecRev["+lsFecRev+"]");
            
            String lsFecSat = 
                ltFecSat == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecSat);            
            System.out.println("lsFecSat["+lsFecSat+"]");
            
            String lsFecDof = 
                ltFecDof == null ? "Sin Especificar" : 
                ddf.format("yyyy-MM-dd", ltFecDof); 
            
            System.out.println("lsFecDof["+lsFecDof+"]");
            String lsLugaryFecha = getLugaryFecha();       
            
            ViewObjectDao loViewObjectDao = new ViewObjectDao();
            Integer liNumProvs = loViewObjectDao.getNumProvsByClient(tsRfc);
            
            loMap.put("pstRfc", tsRfc);
            loMap.put("pstRfcDescription", tsClient);
            loMap.put("pstNumProv", liNumProvs);
            loMap.put("pstFecRec", lsFecRec);
            loMap.put("pstFecRev", lsFecRev);
            loMap.put("pstFecSat", lsFecSat);
            loMap.put("pstFecDof", lsFecDof);
            loMap.put("pstLugaryFecha", lsLugaryFecha);
                        
            String lsOrdid = "ReporteDetallado";
            try {
                runReport("olsDatails.jasper", loMap, lsOrdid);
            } catch (Exception loEx) {                
                loEx.printStackTrace();
                System.out.println("ERROR al ejecutar reporte");
                System.out.println("ERROR: "+loEx.getMessage());
            } 
            
        } catch (FormatErrorException e) {
            System.out.println("Error en format fecha " + e.getMessage());
        }            

    }
    
    public String runReportLocal(String tsRepPath, 
                          java.util.Map tsMapParams,
                          String tsFileName) throws Exception {
        Connection loCnn = null;
        String lsReturn = "filesOls\\OlsDetallado.pdf";        
        try {
            
            ServletContext        loContext = getContext();
            InputStream           loInputStream =
                loContext.getResourceAsStream("/reports/" + tsRepPath);
            JasperReport          loTemplate = 
                (JasperReport)JRLoader.loadObject(loInputStream);
            loTemplate.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            loCnn = getConnection();
            JasperPrint           loPrint =
                JasperFillManager.fillReport(loTemplate, tsMapParams, loCnn);
            ByteArrayOutputStream loBaos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(loPrint, loBaos);
            
            try{
                System.out.println("Guardando archivo....");
                
                FileOutputStream fos = new FileOutputStream(lsReturn);
                JasperExportManager.exportReportToPdfStream(loPrint, fos);
                fos.close();                
                System.out.println("Guardando archivo....OK");
            }catch(IOException ex) {
                System.out.println(ex.getMessage());                
            }
        } catch (Exception loExJsr) {
            System.out.println("ERROR EN el JASPER 08: "+loExJsr.getMessage());
            loExJsr.printStackTrace();
        } finally {
            close(loCnn);
        }
        return lsReturn;
    }

    public void setPoRfcCom(RichInputText poRfcCom) {
        this.poRfcCom = poRfcCom;
    }

    public RichInputText getPoRfcCom() {
        return poRfcCom;
    }

    public void setPoCteCom(RichInputText poCteCom) {
        this.poCteCom = poCteCom;
    }

    public RichInputText getPoCteCom() {
        return poCteCom;
    }

    public void showAllegedInfoAction(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcCom().setValue(tsRfc);
        getPoCteCom().setValue(tsClient);
        new UtilFaces().showPopup(getPoPopupProvInfo());
    }
    
    public void showDefinitiveInfoAction(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcDef().setValue(tsRfc);
        getPoCteDef().setValue(tsClient);
        new UtilFaces().showPopup(getPoPopupDefInfo());
    }
    
    public void showIncumplidosInfoAction(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();       
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcInc().setValue(tsRfc);
        getPoCteInc().setValue(tsClient);
        new UtilFaces().showPopup(getPoPopupIncInfo());
    }
    
    public List<PhmTrxSatAllegedBean> getListAllegedInfo(){
        String lsRfc = 
            getPoRfcByTbl().getValue() == null ? "0" : 
            getPoRfcByTbl().getValue().toString();   
        
        PhmTrxSatAllegedDao laDao = new PhmTrxSatAllegedDao();
        
        return laDao.getAllPresuntosByRfc(lsRfc);
    }
    
    public List<PhmTrxSatDefinitiveBean> getListDefinitiveInfo(){
        String lsRfc = 
            getPoRfcByTbl().getValue() == null ? "0" : 
            getPoRfcByTbl().getValue().toString();   
        
        PhmTrxSatDefinitiveDao laDao = new PhmTrxSatDefinitiveDao();
        
        return laDao.getAllDefinitivosByRfc(lsRfc);
    }
    
    public List<PhmTrxSatUnfulfilledBean> getListIncumplidosInfo(){
        String lsRfc = 
            getPoRfcByTbl().getValue() == null ? "0" : 
            getPoRfcByTbl().getValue().toString();   
        
        PhmTrxSatUnfulfilledDao laDao = new PhmTrxSatUnfulfilledDao();
        
        return laDao.getAllIncumplidosByRfc(lsRfc);
    }

    public void setPoPopupProvInfo(RichPopup poPopupProvInfo) {
        this.poPopupProvInfo = poPopupProvInfo;
    }

    public RichPopup getPoPopupProvInfo() {
        return poPopupProvInfo;
    }
    
    public void selectRowMainTable(SelectionEvent toSelectionEvent) {
        toSelectionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        String                   lsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString();         
        getPoRfcByTbl().setValue(lsRfc);
        
        
    }

    public void setPoRfcByTbl(RichInputText poRfcByTbl) {
        this.poRfcByTbl = poRfcByTbl;
    }

    public RichInputText getPoRfcByTbl() {
        return poRfcByTbl;
    }

    public void setPoPresuntosIterator(UIXIterator poPresuntosIterator) {
        this.poPresuntosIterator = poPresuntosIterator;
    }

    public UIXIterator getPoPresuntosIterator() {
        return poPresuntosIterator;
    }

    public void setPoPopupDefInfo(RichPopup poPopupDefInfo) {
        this.poPopupDefInfo = poPopupDefInfo;
    }

    public RichPopup getPoPopupDefInfo() {
        return poPopupDefInfo;
    }

    public void setPoRfcDef(RichInputText poRfcDef) {
        this.poRfcDef = poRfcDef;
    }

    public RichInputText getPoRfcDef() {
        return poRfcDef;
    }

    public void setPoCteDef(RichInputText poCteDef) {
        this.poCteDef = poCteDef;
    }

    public RichInputText getPoCteDef() {
        return poCteDef;
    }

    public void setPoDefIterator(UIXIterator poDefIterator) {
        this.poDefIterator = poDefIterator;
    }

    public UIXIterator getPoDefIterator() {
        return poDefIterator;
    }

    public void setPoPopupIncInfo(RichPopup poPopupIncInfo) {
        this.poPopupIncInfo = poPopupIncInfo;
    }

    public RichPopup getPoPopupIncInfo() {
        return poPopupIncInfo;
    }

    public void setPoRfcInc(RichInputText poRfcInc) {
        this.poRfcInc = poRfcInc;
    }

    public RichInputText getPoRfcInc() {
        return poRfcInc;
    }

    public void setPoCteInc(RichInputText poCteInc) {
        this.poCteInc = poCteInc;
    }

    public RichInputText getPoCteInc() {
        return poCteInc;
    }

    public void setPoIncIterator(UIXIterator poIncIterator) {
        this.poIncIterator = poIncIterator;
    }

    public UIXIterator getPoIncIterator() {
        return poIncIterator;
    }

    public String cancelFullReportAction() {
        new UtilFaces().hidePopup(getPoPopupProv());
        ExternalContext     loEctx = 
            FacesContext.getCurrentInstance().getExternalContext();
        String              lsUrl = loEctx.getRequestContextPath() + "/faces/reportFullPage";
        HttpServletResponse loResponse = (HttpServletResponse) loEctx.getResponse();
        try {
            loResponse.sendRedirect(lsUrl);
            FacesContext.getCurrentInstance().responseComplete();
        }
        catch (Exception loEx) {
            System.out.println("Error al salir "+loEx.getMessage());
            loEx.printStackTrace();
        }
        return null;
    }

    public String createBlacklistAction() {
        try{
            System.out.println("Iniciar generar reportes");
            executeProcessAction("EXECUTE");
            System.out.println("FIN de generar reportes");
        }catch(Exception loEx){
            System.out.println("Error principal: "+loEx.getMessage());
        }
        
        return null;
    }
    
    /**
     * Ejecuta inicio del cron del servicio seleccionado
     * @autor Jorge Luis Bautista Santiago
     * @param lsAction
     * @param toPopup
     * @return void
     */
    public void executeProcessAction(String lsAction
                                     ){               
       
        String              lsFinalMessage = "Acción Satisfactoria";
        String              lsColorMessage = "blue";
        String              lsGeneralAction = lsAction;
        Integer             liIdUser = null;
        String              lsUserName = null;        
        try {
            liIdUser = 1;
            //System.out.println(">>>> liIdUser: "+liIdUser);
            lsUserName = "System";
            //System.out.println(">>>> lsUserName: "+lsUserName);
            String lsIdService = "1";
                //loIdService.getValue() == null ? null : 
                //loIdService.getValue().toString();    
            //System.out.println(">>>> lsIdService: "+lsIdService);
            String lsTypeService = "ReportBlacklistAll";
                //loTypeService.getValue() == null ? null : 
                //loTypeService.getValue().toString();
            //System.out.println(">>>> lsTypeService: "+lsTypeService);
            
            String lsNameService = "ReportBlacklistAll";
                //loNameService.getValue() == null ? null : 
                //loNameService.getValue().toString();
            
            
            String lsServiceAction = lsAction;   
            String lsIdTrigger = lsIdService + "-" + lsTypeService;
            System.out.println("********** lsIdTrigger["+lsIdTrigger+"] ==> ["+lsServiceAction+"]*********");
            lsGeneralAction = lsServiceAction;
            System.out.println(">>>> lsGeneralAction: "+lsGeneralAction);
            ProcessServiceBean loProcessBean = new ProcessServiceBean();
            loProcessBean.setLiIdUser(liIdUser);
            loProcessBean.setLsUserName(lsUserName);
            loProcessBean.setLsIdTrigger(lsIdTrigger);
            loProcessBean.setLsIdService(lsIdService);
            loProcessBean.setLsServiceToInvoke(lsTypeService);
            loProcessBean.setLsServiceAction(lsServiceAction);
            
            loProcessBean.setLsTypeProcess(lsTypeService);
            loProcessBean.setLsServiceName(lsNameService);
            
            System.out.println(">>>> Ejecutar processServiceExecution para ReportBlacklistAllCron: ");
            ExecuteServiceResponseBean loRes =
            processServiceExecution(loProcessBean, 
                                    ReportBlacklistAllCron.class
                                    );
            lsColorMessage = loRes.getLsColor();
            lsFinalMessage = loRes.getLsMessage();
                                             
        } catch (Exception loEx) {
            
            lsFinalMessage = loEx.getMessage();
            lsColorMessage = "red";
            
        } finally {
            //refreshMainTable();
            StringBuilder loMessage = new StringBuilder("<html><body>");
            loMessage.append("<p style='color:" + lsColorMessage + "'><b>" + lsFinalMessage + "</i></b></p>");
            loMessage.append("</body></html>");        
            FacesMessage loMsg = 
                new FacesMessage(loMessage.toString());     
            loMsg.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext loFctx = FacesContext.getCurrentInstance();
            loFctx.addMessage(null, loMsg);
            
        }
       
    }
    
    
    /**
     * Ejecuta la logica de ejecucion de servicio para Programas
     * @autor Jorge Luis Bautista Santiago
     * @param toPrcBean
     * @param poService
     * @return ExecuteServiceResponseBean
     */
    public ExecuteServiceResponseBean processServiceExecution(ProcessServiceBean toPrcBean,
                                                              Class<? extends Job> loClassCron
                                                              ){
        ExecuteServiceResponseBean         loRes = new ExecuteServiceResponseBean();
        //EntityMappedDao                    loEntityMappedDao = new EntityMappedDao();
        String                             lsFinalMessage = "";
        String                             lsColorMessage = "red";
        
        if(toPrcBean.getLsServiceAction().equalsIgnoreCase("EXECUTE")){
            lsFinalMessage = 
                "El Servicio de " + toPrcBean.getLsServiceToInvoke() + 
                " ha sido ejecutado en segundo plano";
            lsColorMessage = "black";           
            //Integer liIndProcess = new UtilFaces().getIdConfigParameterByName("ExecuteCron"); //
            Integer liNumPgmProcessID = 0;
            Integer liNumEvtbProcessId = 0;
            /*TdlLmkIntServiceBitacoraRowBean loBean = new TdlLmkIntServiceBitacoraRowBean();
            loBean.setLsIdLogServices("0");
            loBean.setLsIdService(toPrcBean.getLsIdService());
            loBean.setLsIndProcess(String.valueOf(liIndProcess));
            loBean.setLsIndEvento("Ejecucion Manual de Servicio "+toPrcBean.getLsServiceName());
            loBean.setLsNumPgmProcessId(String.valueOf(liNumEvtbProcessId));
            loBean.setLsNumPgmProcessId(String.valueOf(liNumPgmProcessID));
            loBean.setLsIdBitacora("0");
            loBean.setLsIdUser(String.valueOf(toPrcBean.getLiIdUser()));
            loBean.setLsUserName(toPrcBean.getLsUserName());
            //new UtilFaces().insertBitacoraServiceService(loBean);
            loEntityMappedDao.insertBitacoraWs(loBean);*/
            
            Scheduler loScheduler;
            try {
                
                loScheduler = new StdSchedulerFactory().getScheduler();
                JobDetail loJob = 
                    JobBuilder.newJob(loClassCron).build();
                Trigger   loTrigger = 
                    TriggerBuilder.newTrigger().withIdentity(toPrcBean.getLsIdTrigger()).build();
                JobDataMap loJobDataMap=  loJob.getJobDataMap();
                loJobDataMap.put("lsIdService", toPrcBean.getLsIdService()); 
                loJobDataMap.put("liIdUser", String.valueOf(toPrcBean.getLiIdUser())); 
                loJobDataMap.put("lsUserName", toPrcBean.getLsUserName()); 
                //loJobDataMap.put("lsIdRequest", lsIdRequest); 
                loJobDataMap.put("lsTypeRequest", "normal");
                loJobDataMap.put("lsTypeProcess", toPrcBean.getLsTypeProcess());
                loJobDataMap.put("lsServiceName", toPrcBean.getLsServiceName());
                loJobDataMap.put("lsPathFiles", "NoAplica");                    
                //loJobDataMap.put("lsPathFiles", "");
                loScheduler.scheduleJob(loJob, loTrigger);
                loScheduler.start();
                
            } catch (Exception loEx) {
                System.out.println("Error al iniciar Cron ["+loEx.getMessage()+"]");
                lsFinalMessage = loEx.getMessage();
                lsColorMessage = "red";
            } 
            
        }
                
        loRes.setLsColor(lsColorMessage);
        loRes.setLsMessage(lsFinalMessage);
       return loRes;
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
    
    
}

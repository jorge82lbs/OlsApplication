/**
* Project: OLS - Administracion de Clientes
*
* File: CustomsBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/

package mx.com.ols.fiscal.view.front.beans;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxProvidersSatDao;
import mx.com.ols.fiscal.view.front.beans.utils.ADFUtils;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;
import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;

/** Esta clase es el backend de la pantalla de Clientes<br/>
 *
 * @author Jorge Luis Bautista - Omw
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */

public class CustomsBean {
    private RichInputNumberSpinbox poFilterYear;
    private RichSelectOneChoice poFilterMonth;
    private RichInputText poFilterRfc;
    private RichInputText poFilterCompany;
    private RichInputText poFilterAddress;
    private RichInputText poFilterPhoneNumber;
    private RichSelectOneChoice poFilterStatusSel;
    private RichPopup poPopupSave;
    private RichInputNumberSpinbox poSaveYear;
    private RichSelectOneChoice poSaveMonth;
    private RichInputText poSaveRfc;
    private RichInputText poSaveCompany;
    private RichInputText poSaveAddress;
    private RichInputText poSavePhoneNumber;
    private RichSelectBooleanCheckbox poSaveStatus;
    private RichOutputText poDeleteIdBinding;
    private RichPanelLabelAndMessage poDeleteMsgLbl;
    private RichPopup poPopupDelete;
    private RichTable poTblMain;
    private RichPopup poPopupUpdate;
    private RichInputText poUpdateIdClient;
    private RichInputNumberSpinbox poUpdateYear;
    private RichSelectOneChoice poUpdateMonth;
    private RichInputText poUpdateRfc;
    private RichInputText poUpdateCompany;
    private RichInputText poUpdateAddress;
    private RichInputText poUpdatePhoneNumber;
    private RichSelectBooleanCheckbox poUpdateStatus;
    String              gsAmDef = "mx.com.ols.model.OlsAppModuleImpl";
    String              gsConfig = "OlsAppModuleLocal";
    String              lsEntityView = "OlsCatSatClientsOlsTabView1";
    String              lsEntityIterator = "OlsCatSatClientsOlsTabView1Iterator";
    String              lsAppModuleDataControl = "OlsAppModuleDataControl";
    Map goSessionScope = ADFContext.getCurrent().getSessionScope();
    String gsUserName = (String)goSessionScope.get("loggedOlsUser");
    String gsIdUser = (String)goSessionScope.get("loggedOlsIdUser");
    Integer liIdUser = Integer.parseInt(gsIdUser);
    private RichPopup poPopupMails;
    private RichInputText poRfcCte;
    private RichInputText poCteDesc;
    private RichTable poTblEmails;
    private RichInputText poCrudEmail;
    private RichInputText poCrudTitular;
    private RichInputText poIdCteDesc;
    private RichPopup poPopupProv;
    private RichInputText poRfcProv;
    private RichInputText poCteProv;
    private RichTable poTblProviders;

    public CustomsBean() {
    }
    
    public String saveEmailAction() {
        UtilsOls loUtilsOls = new UtilsOls();
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        Integer                    liId = loUtilsOls.getMaxIdTable("Emails") + 1;
        String tsIdClientSat = 
            getPoIdCteDesc().getValue() == null ? "" : 
            getPoIdCteDesc().getValue().toString();
        
        String lsRfc = 
            getPoRfcCte().getValue() == null ? "": 
            getPoRfcCte().getValue().toString();
        
        String lsEmail = 
            getPoCrudEmail().getValue() == null ? "": 
            getPoCrudEmail().getValue().toString();
        
        String lsTitular = 
            getPoCrudTitular().getValue() == null ? "": 
            getPoCrudTitular().getValue().toString();                
       
        String lsStatusTab = "1";
        
        if(lsEmail.length() < 1){
            lsFieldErrorRq += "Email,";
            lbProcess = false;   
        }
        if(lsTitular.length() < 1){
            lsFieldErrorRq += "Titular,";
            lbProcess = false;   
        }
        
        if(lbProcess){
            //Validar con expr regular
            boolean lbEmail = loUtilsOls.validateEmailFormat(lsEmail);
            if(lbEmail){                
                ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
                String lsEntityViewTbl = "OlsCatSatMailsCtesTabView1";
                ViewObject        loVo = loAm.findViewObject(lsEntityViewTbl);
                Row               loRow = null;
                try {
                    loRow = (oracle.jbo.server.ViewRowImpl)loVo.createRow();
                    loRow.setAttribute("IdMail", liId);
                    loRow.setAttribute("IdClientSat", tsIdClientSat);
                    loRow.setAttribute("IndRfc", lsRfc);
                    loRow.setAttribute("IndAddress", lsTitular);
                    loRow.setAttribute("IndEmail", lsEmail);
                    loRow.setAttribute("IndRfcType", "C");
                    loRow.setAttribute("IndEstatus", lsStatusTab);
                    loRow.setAttribute("FecCreationDate", new Date());
                    loRow.setAttribute("NumCreatedBy", liIdUser);
                    loRow.setAttribute("FecLastUpdateDate", new Date());
                    loRow.setAttribute("NumLastUpdatedBy", liIdUser);
                    loVo.insertRow(loRow);
                    loVo.executeQuery();
                    loAm.getTransaction().commit();
                }catch(Exception loEx){
                    System.out.println("error al insertar: "+ loEx.getMessage());    
                }
            }else{
                FacesMessage loMsg;
                loMsg = new FacesMessage("Correo Electr\u00f3nico no Permitido");
                loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        String lsEntityIteratorTbl = "OlsCatSatMailsCtesTabView1Iterator";
        String lsWhere = "ID_CLIENT_SAT = "+tsIdClientSat+"";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIteratorTbl, getPoTblEmails());
        return null;
    }   

    public String refreshEmailAction() {
        getPoCrudEmail().setValue(null);
        getPoCrudTitular().setValue(null);
        return null;
    }

    public void deleteEmailActionListener(ActionEvent actionEvent) {
        actionEvent.getPhaseId();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblEmails().getSelectedRowData();  
        String tsIdClientSat = 
            getPoIdCteDesc().getValue() == null ? "" : 
            getPoIdCteDesc().getValue().toString();
        String                   tsIdMail = 
            loNode.getAttribute("IdMail") == null ? "" : 
            loNode.getAttribute("IdMail").toString(); 
        String lsEntityViewTbl = "OlsCatSatMailsCtesTabView1";
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityViewTbl);
        String lsWhere = "ID_MAIL = " + tsIdMail;
        loVo.setWhereClause(lsWhere);
        loVo.executeQuery();
        try {
            Row[] loArrObj = loVo.getAllRowsInRange(); 
            if(loArrObj.length > 0) {                
                 Row loRow = loVo.getAllRowsInRange()[0];                 
                 loRow.remove();
            }
            loAm.getTransaction().commit();
        } catch (Exception loEx) {
            System.out.println("Delete ERROR!!"+loEx.getMessage());
        }
        String lsWhereTbl = "ID_CLIENT_SAT = " + tsIdClientSat;
        String lsEntityIteratorTbl = "OlsCatSatMailsCtesTabView1Iterator";
        new UtilFaces().refreshTableWhereIterator(lsWhereTbl, lsEntityIteratorTbl, getPoTblEmails());
    }

    public String deleteEmailAction() {
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblEmails().getSelectedRowData();  
        String tsIdClientSat = 
            getPoIdCteDesc().getValue() == null ? "" : 
            getPoIdCteDesc().getValue().toString();
        String                   tsIdMail = 
            loNode.getAttribute("IdMail") == null ? "" : 
            loNode.getAttribute("IdMail").toString(); 
        String lsEntityViewTbl = "OlsCatSatMailsCtesTabView1";
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityViewTbl);
        String lsWhere = "ID_MAIL = " + tsIdMail;
        loVo.setWhereClause(lsWhere);
        loVo.executeQuery();
        try {
            Row[] loArrObj = loVo.getAllRowsInRange(); 
            if(loArrObj.length > 0) {                
                 Row loRow = loVo.getAllRowsInRange()[0];                 
                 loRow.remove();
            }
            loAm.getTransaction().commit();
        } catch (Exception loEx) {
            System.out.println("Delete ERROR!!"+loEx.getMessage());
        }
        String lsWhereTbl = "ID_CLIENT_SAT = " + tsIdClientSat;
        String lsEntityIteratorTbl = "OlsCatSatMailsCtesTabView1Iterator";
        new UtilFaces().refreshTableWhereIterator(lsWhereTbl, lsEntityIteratorTbl, getPoTblEmails());
        return null;
    }

    public String deleteProvidersAction() {
        boolean lbResult = true;
        String lsMessage = "";
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblProviders().getSelectedRowData();  
        String                   tsRfcClient = 
            loNode.getAttribute("IndRfcClient") == null ? "" : 
            loNode.getAttribute("IndRfcClient").toString(); 
        String                   tsRfcProvedor = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        
        //Eliminar con Dao
        try{
            PhmTrxProvidersSatDao loPhmTrxProvidersSatDao = new PhmTrxProvidersSatDao();
            ResponseBean loRes = 
                loPhmTrxProvidersSatDao.deletePhmTrxProvidersSat(tsRfcClient, tsRfcProvedor);
            if(loRes.getLsResponse().equalsIgnoreCase("OK")){
                lbResult = true;
            }else{
                lsMessage = loRes.getLsMessageResponse();
                lbResult = false;
            }
        }catch(Exception loEx){
            System.out.println("Error al eliminar provedor");
            lsMessage = "Error al Eliminar proveedor "+loEx.getMessage();
        }
        if(!lbResult){
            FacesMessage loMsg;
            loMsg = new FacesMessage(lsMessage);
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        //Actualizar Tabla de Proveedores
        String lsEntityIteratorTbl = "OlsCatSatProvidersOlsTabView1Iterator";
        String lsWhere = "IND_RFC_CLIENT = '"+tsRfcClient+"'";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIteratorTbl,getPoTblProviders());
        //new UtilFaces().hidePopup(getPoPopupProv());
        return null;
    }
    
    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterAddress().setValue(null);
        getPoFilterCompany().setValue(null);
        getPoFilterMonth().setValue(null);
        getPoFilterPhoneNumber().setValue(null);
        getPoFilterRfc().setValue(null);
        getPoFilterYear().setValue(null);
        
        getPoFilterStatusSel().setValue(null);

    }

    public String refreshMainTable() {
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
    }
    
    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        UtilsOls loUtilsOls = new UtilsOls();
        String lsAddress = 
            getPoFilterAddress().getValue() == null ? "": 
            getPoFilterAddress().getValue().toString();        
        if(!lsAddress.equalsIgnoreCase("")){
            lsQuery += " AND IND_ADDRESS LIKE '" + lsAddress + "%'";
        }
        
        String lsCompany = 
            getPoFilterCompany().getValue() == null ? "": 
            getPoFilterCompany().getValue().toString();        
        if(!lsCompany.equalsIgnoreCase("")){
            lsQuery += " AND IND_COMPANY LIKE '" + lsCompany + "%'";
        }
        
        String lsMonth = 
            getPoFilterMonth().getValue() == null ? "": 
            getPoFilterMonth().getValue().toString();        
        if(!lsMonth.equalsIgnoreCase("")){
            lsQuery += " AND IND_MONTH = " + loUtilsOls.getNumMonth(lsMonth) + "";
        }
        
        String lsPhoneNumber = 
            getPoFilterPhoneNumber().getValue() == null ? "": 
            getPoFilterPhoneNumber().getValue().toString();        
        if(!lsPhoneNumber.equalsIgnoreCase("")){
            lsQuery += " AND IND_PHONE_NUMBER LIKE '" + lsPhoneNumber + "%'";
        }
        
        String lsRfc = 
            getPoFilterRfc().getValue() == null ? "": 
            getPoFilterRfc().getValue().toString();        
        if(!lsRfc.equalsIgnoreCase("")){
            lsQuery += " AND IND_RFC LIKE '" + lsRfc + "%'";
        }
        
        String lsYear = 
            getPoFilterYear().getValue() == null ? "": 
            getPoFilterYear().getValue().toString();        
        if(!lsYear.equalsIgnoreCase("")){
            lsQuery += " AND IND_YEAR = " + lsYear + "";
        }
                
        String lsIndEstatus = 
            getPoFilterStatusSel().getValue() == null ? "": 
            getPoFilterStatusSel().getValue().toString();        
        
        if(!lsIndEstatus.equalsIgnoreCase("")){
            lsQuery += " AND IND_ESTATUS = '" + lsIndEstatus + "'";
        }
        new UtilFaces().refreshTableWhereIterator(lsQuery, lsEntityIterator, getPoTblMain());        
        
        return null;
    }

    public String saveAction() {
        UtilsOls loUtilsOls = new UtilsOls();
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        Integer                    liId = loUtilsOls.getMaxIdTable("Customs") + 1;
        Integer                    liIdRequest = loUtilsOls.getMaxIdTable("CustomsRequest") + 1;
        /*
        String lsYear = 
            getPoSaveYear().getValue() == null ? "2018": 
            getPoSaveYear().getValue().toString();      
        String lsMonth = 
            getPoSaveMonth().getValue() == null ? "ENERO": 
            getPoSaveMonth().getValue().toString();  
        */
        String lsYear = String.valueOf(getCurrentCalendar("YEAR"));
          
        String lsMonth = getCurrentMonth(getCurrentCalendar("MONTH"));
        
        
        Integer liMontNum = loUtilsOls.getNumMonth(lsMonth);
        String lsPeriod = loUtilsOls.getPeriodDesc(lsMonth, lsYear);        
        String lsRfc = 
            getPoSaveRfc().getValue() == null ? "": 
            getPoSaveRfc().getValue().toString();
        String lsCompany = 
            getPoSaveCompany().getValue() == null ? "": 
            getPoSaveCompany().getValue().toString();
        String lsAddress = 
            getPoSaveAddress().getValue() == null ? "": 
            getPoSaveAddress().getValue().toString();                
        String lsPhoneNumber = 
            getPoSavePhoneNumber().getValue() == null ? "": 
            getPoSavePhoneNumber().getValue().toString();
        String lsIndEstatus = 
            getPoSaveStatus().getValue() == null ? "":
            getPoSaveStatus().getValue().toString();
        String                     lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }
        if(lsRfc.length() < 1){
            lsFieldErrorRq += "RFC,";
            lbProcess = false;   
        }
       
        if(lbProcess){
            ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
            ViewObject        loVo = loAm.findViewObject(lsEntityView);
            Row               loRow = null;
            try {
                loRow = (oracle.jbo.server.ViewRowImpl)loVo.createRow();
                loRow.setAttribute("IdClientSat", liId);
                loRow.setAttribute("IdRequest", liIdRequest);
                loRow.setAttribute("IndYear", lsYear);
                loRow.setAttribute("IndMonth", liMontNum);
                loRow.setAttribute("IndPeriod", lsPeriod);
                loRow.setAttribute("IdLawFirm", "1");
                loRow.setAttribute("IndRfc", lsRfc);
                loRow.setAttribute("IndCompany", lsCompany);
                loRow.setAttribute("IndAddress", lsAddress);
                loRow.setAttribute("IndPhoneNumber", lsPhoneNumber);
                loRow.setAttribute("IndEstatus", lsStatusTab);
                loRow.setAttribute("FecCreationDate", new Date());
                loRow.setAttribute("NumCreatedBy", liIdUser);
                loRow.setAttribute("FecLastUpdateDate", new Date());
                loRow.setAttribute("NumLastUpdatedBy", liIdUser);
                loVo.insertRow(loRow);
                loVo.executeQuery();
                loAm.getTransaction().commit();
            }catch(Exception loEx){
                System.out.println("error al insertar: "+ loEx.getMessage());    
            }
        }else{
            FacesMessage loMsg;
            loMsg = new FacesMessage("Los Siguiente Campos son Requeridos " + 
                                     lsFieldErrorRq.substring(0, lsFieldErrorRq.length()-1));
            loMsg.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        new UtilFaces().hidePopup(getPoPopupSave());
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
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

    public String cancelSaveAction() {
        new UtilFaces().hidePopup(getPoPopupSave());
        FacesContext       loContext = FacesContext.getCurrentInstance();
        ExternalContext    loEctx = loContext.getExternalContext();
        String             lsUrl = 
            loEctx.getRequestContextPath() + "/faces/customsPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
    }

    public String cancelDeleteAction() {
        new UtilFaces().hidePopup(getPoPopupDelete());
        return null;
    }

    public String deleteAction() {
        String                   lsId = 
        getPoDeleteIdBinding().getValue() == null ? "" : 
        getPoDeleteIdBinding().getValue().toString();
        
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityView);
        String lsWhere = "ID_CLIENT_SAT = " + lsId;
        loVo.setWhereClause(lsWhere);
        loVo.executeQuery();
        try {
            Row[] loArrObj = loVo.getAllRowsInRange(); 
            if(loArrObj.length > 0) {                
                 Row loRow = loVo.getAllRowsInRange()[0];                 
                 loRow.remove();
            }
            loAm.getTransaction().commit();
        } catch (Exception loEx) {
            System.out.println("Delete ERROR!!"+loEx.getMessage());
        }
        new UtilFaces().hidePopup(getPoPopupDelete());
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
    }
    
    public void showDeletePopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        
        String                   lsId = 
            loNode.getAttribute("IdClientSat") == null ? "" : 
            loNode.getAttribute("IdClientSat").toString();                 
        String                   lsDescripction = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();   
        
        getPoDeleteIdBinding().setValue(lsId);
        
        getPoDeleteMsgLbl().setLabel("Eliminar a " + lsDescripction + " : ");
        
        new UtilFaces().showPopup(getPoPopupDelete());
    }

    public void showEditPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        UtilsOls loUtilsOls = new UtilsOls();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        String                   lsIdClientSat = 
            loNode.getAttribute("IdClientSat") == null ? "" : 
            loNode.getAttribute("IdClientSat").toString();                 
        String                   lsYear = 
            loNode.getAttribute("IndYear") == null ? "" : 
            loNode.getAttribute("IndYear").toString();   
        String                   lsMonth = 
            loNode.getAttribute("IndMonth") == null ? "" : 
            loNode.getAttribute("IndMonth").toString();   
        String                   lsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString();   
        String                   lsCompany = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();
        String                   lsAddress = 
            loNode.getAttribute("IndAddress") == null ? "" : 
            loNode.getAttribute("IndAddress").toString();
        String                   lsPhoneNumber = 
            loNode.getAttribute("IndPhoneNumber") == null ? "" : 
            loNode.getAttribute("IndPhoneNumber").toString();
        String                   lsIndEstatus = 
            loNode.getAttribute("IndEstatus") == null ? "" : 
            loNode.getAttribute("IndEstatus").toString();
        
        String lsMesDesc = loUtilsOls.getDescMonth(Integer.parseInt(lsMonth));
        // Settear valores al popup
        getPoUpdateIdClient().setValue(lsIdClientSat);                
        getPoUpdateAddress().setValue(lsAddress);                
        getPoUpdateCompany().setValue(lsCompany);                
        getPoUpdateMonth().setValue(lsMesDesc);                
        getPoUpdatePhoneNumber().setValue(lsPhoneNumber); 
        getPoUpdateRfc().setValue(lsRfc);  
        getPoUpdateYear().setValue(lsYear);
        
        if(lsIndEstatus.equalsIgnoreCase("1")){
            getPoUpdateStatus().setSelected(true);
        }else{
            getPoUpdateStatus().setSelected(false);
        }
        new UtilFaces().showPopup(getPoPopupUpdate());
    }

    public void showSavePopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        getPoSaveAddress().setValue("");
        getPoSaveCompany().setValue("");
        getPoSaveMonth().setValue("");
        getPoSavePhoneNumber().setValue("");
        getPoSaveRfc().setValue("");
        getPoSaveYear().setValue("");
        getPoSaveStatus().setValue("");
        new UtilFaces().showPopup(getPoPopupSave());
    }

    public String updateAction() {      
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        String lsId = 
            getPoUpdateIdClient().getValue() == null ? "": 
            getPoUpdateIdClient().getValue().toString();        
        
        String lsRfc = 
            getPoUpdateRfc().getValue() == null ? "": 
            getPoUpdateRfc().getValue().toString();                
        String lsCompany = 
            getPoUpdateCompany().getValue() == null ? "": 
            getPoUpdateCompany().getValue().toString();
        String lsAddress = 
            getPoUpdateAddress().getValue() == null ? "": 
            getPoUpdateAddress().getValue().toString();                
        String lsPhoneNumber = 
            getPoUpdatePhoneNumber().getValue() == null ? "": 
            getPoUpdatePhoneNumber().getValue().toString();
        
        if(lsRfc.length() < 1){
            lsFieldErrorRq += "RFC,";
            lbProcess = false;   
        }
        
        String lsIndEstatus = 
            getPoUpdateStatus().getValue() == null ? "": 
            getPoUpdateStatus().getValue().toString();
        String lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }        
        if(lbProcess){            
            ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
            ViewObject           loVo = loAm.findViewObject(lsEntityView);
            String lsWhere = "ID_CLIENT_SAT = " + lsId;
            loVo.setWhereClause(lsWhere);               
            loVo.executeQuery();
            try {
                Row[] loArrObj = loVo.getAllRowsInRange(); 
                if(loArrObj.length > 0) {     
                    Row loRow = loVo.getAllRowsInRange()[0];    
                    //loRow.setAttribute("IndYear", lsYear);
                    //loRow.setAttribute("IndMonth", lsMonth);
                    //loRow.setAttribute("IndPeriod", lsPeriod);
                    loRow.setAttribute("IndRfc", lsRfc);
                    loRow.setAttribute("IndCompany", lsCompany);
                    loRow.setAttribute("IndAddress", lsAddress);
                    loRow.setAttribute("IndPhoneNumber", lsPhoneNumber);
                    loRow.setAttribute("IndEstatus", lsStatusTab);
                    loRow.setAttribute("FecLastUpdateDate", new Date());
                    loRow.setAttribute("NumLastUpdatedBy", liIdUser);
                    loVo.setCurrentRow(loRow);
                }
                loAm.getTransaction().commit();
            } catch (Exception loEx) {
                System.out.println("Update ERROR!!"+loEx.getMessage());
            }
        }
        new UtilFaces().hidePopup(getPoPopupUpdate());
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
    }

    public String cancelUpdateAction() {
        new UtilFaces().hidePopup(getPoPopupUpdate());
        FacesContext       loContext = FacesContext.getCurrentInstance();
        ExternalContext    loEctx = loContext.getExternalContext();
        String             lsUrl = 
            loEctx.getRequestContextPath() + "/faces/customsPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
    }
    
    public void showEmailsPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();  
        String                   tsIdClientSat = 
            loNode.getAttribute("IdClientSat") == null ? "" : 
            loNode.getAttribute("IdClientSat").toString(); 
        String                   tsRfc = 
            loNode.getAttribute("IndRfc") == null ? "" : 
            loNode.getAttribute("IndRfc").toString(); 
        String                   tsClient = 
            loNode.getAttribute("IndCompany") == null ? "" : 
            loNode.getAttribute("IndCompany").toString();  
        getPoRfcCte().setValue(tsRfc);
        getPoCteDesc().setValue(tsClient);
        getPoIdCteDesc().setValue(tsIdClientSat);
        
        String lsEntityIteratorTbl = "OlsCatSatMailsCtesTabView1Iterator";
        String lsWhere = "ID_CLIENT_SAT = "+tsIdClientSat+"";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIteratorTbl,getPoTblEmails());
        new UtilFaces().showPopup(getPoPopupMails());
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

        
        String lsEntityIteratorTbl = "OlsCatSatProvidersOlsTabView1Iterator";
        String lsWhere = "IND_RFC_CLIENT = '"+tsRfc+"'";
        new UtilFaces().refreshTableWhereIterator(lsWhere, lsEntityIteratorTbl,getPoTblProviders());
        new UtilFaces().showPopup(getPoPopupProv());
    }

    public void showSaveMailPopup(ActionEvent actionEvent) {
        // Add event code here...
    }

    public void showEditMailPopup(ActionEvent actionEvent) {
        // Add event code here...
    }

    public void deleteMailPopup(ActionEvent actionEvent) {
        // Add event code here...
    }
    
    /*************** SETTERS AND GETTERS ************************/

    public void setPoFilterYear(RichInputNumberSpinbox poFilterYear) {
        this.poFilterYear = poFilterYear;
    }

    public RichInputNumberSpinbox getPoFilterYear() {
        return poFilterYear;
    }

    public void setPoFilterMonth(RichSelectOneChoice poFilterMonth) {
        this.poFilterMonth = poFilterMonth;
    }

    public RichSelectOneChoice getPoFilterMonth() {
        return poFilterMonth;
    }

    public void setPoFilterRfc(RichInputText poFilterRfc) {
        this.poFilterRfc = poFilterRfc;
    }

    public RichInputText getPoFilterRfc() {
        return poFilterRfc;
    }

    public void setPoFilterCompany(RichInputText poFilterCompany) {
        this.poFilterCompany = poFilterCompany;
    }

    public RichInputText getPoFilterCompany() {
        return poFilterCompany;
    }

    public void setPoFilterAddress(RichInputText poFilterAddress) {
        this.poFilterAddress = poFilterAddress;
    }

    public RichInputText getPoFilterAddress() {
        return poFilterAddress;
    }

    public void setPoFilterPhoneNumber(RichInputText poFilterPhoneNumber) {
        this.poFilterPhoneNumber = poFilterPhoneNumber;
    }

    public RichInputText getPoFilterPhoneNumber() {
        return poFilterPhoneNumber;
    }

    public void setPoFilterStatusSel(RichSelectOneChoice poFilterStatusSel) {
        this.poFilterStatusSel = poFilterStatusSel;
    }

    public RichSelectOneChoice getPoFilterStatusSel() {
        return poFilterStatusSel;
    }

   
    public void setPoPopupSave(RichPopup poPopupSave) {
        this.poPopupSave = poPopupSave;
    }

    public RichPopup getPoPopupSave() {
        return poPopupSave;
    }

    public void setPoSaveYear(RichInputNumberSpinbox poSaveYear) {
        this.poSaveYear = poSaveYear;
    }

    public RichInputNumberSpinbox getPoSaveYear() {
        return poSaveYear;
    }

    public void setPoSaveMonth(RichSelectOneChoice poSaveMonth) {
        this.poSaveMonth = poSaveMonth;
    }

    public RichSelectOneChoice getPoSaveMonth() {
        return poSaveMonth;
    }

    public void setPoSaveRfc(RichInputText poSaveRfc) {
        this.poSaveRfc = poSaveRfc;
    }

    public RichInputText getPoSaveRfc() {
        return poSaveRfc;
    }

    public void setPoSaveCompany(RichInputText poSaveCompany) {
        this.poSaveCompany = poSaveCompany;
    }

    public RichInputText getPoSaveCompany() {
        return poSaveCompany;
    }

    public void setPoSaveAddress(RichInputText poSaveAddress) {
        this.poSaveAddress = poSaveAddress;
    }

    public RichInputText getPoSaveAddress() {
        return poSaveAddress;
    }

    public void setPoSavePhoneNumber(RichInputText poSavePhoneNumber) {
        this.poSavePhoneNumber = poSavePhoneNumber;
    }

    public RichInputText getPoSavePhoneNumber() {
        return poSavePhoneNumber;
    }

    public void setPoSaveStatus(RichSelectBooleanCheckbox poSaveStatus) {
        this.poSaveStatus = poSaveStatus;
    }

    public RichSelectBooleanCheckbox getPoSaveStatus() {
        return poSaveStatus;
    }

    public void setPoDeleteIdBinding(RichOutputText poDeleteIdBinding) {
        this.poDeleteIdBinding = poDeleteIdBinding;
    }

    public RichOutputText getPoDeleteIdBinding() {
        return poDeleteIdBinding;
    }

    public void setPoDeleteMsgLbl(RichPanelLabelAndMessage poDeleteMsgLbl) {
        this.poDeleteMsgLbl = poDeleteMsgLbl;
    }

    public RichPanelLabelAndMessage getPoDeleteMsgLbl() {
        return poDeleteMsgLbl;
    }

    public void setPoPopupDelete(RichPopup poPopupDelete) {
        this.poPopupDelete = poPopupDelete;
    }

    public RichPopup getPoPopupDelete() {
        return poPopupDelete;
    }
   
    public void setPoTblMain(RichTable poTblMain) {
        this.poTblMain = poTblMain;
    }

    public RichTable getPoTblMain() {
        return poTblMain;
    }

    public void setPoPopupUpdate(RichPopup poPopupUpdate) {
        this.poPopupUpdate = poPopupUpdate;
    }

    public RichPopup getPoPopupUpdate() {
        return poPopupUpdate;
    }

    public void setPoUpdateIdClient(RichInputText poUpdateIdClient) {
        this.poUpdateIdClient = poUpdateIdClient;
    }

    public RichInputText getPoUpdateIdClient() {
        return poUpdateIdClient;
    }

    public void setPoUpdateYear(RichInputNumberSpinbox poUpdateYear) {
        this.poUpdateYear = poUpdateYear;
    }

    public RichInputNumberSpinbox getPoUpdateYear() {
        return poUpdateYear;
    }

    public void setPoUpdateMonth(RichSelectOneChoice poUpdateMonth) {
        this.poUpdateMonth = poUpdateMonth;
    }

    public RichSelectOneChoice getPoUpdateMonth() {
        return poUpdateMonth;
    }

    public void setPoUpdateRfc(RichInputText poUpdateRfc) {
        this.poUpdateRfc = poUpdateRfc;
    }

    public RichInputText getPoUpdateRfc() {
        return poUpdateRfc;
    }

    public void setPoUpdateCompany(RichInputText poUpdateCompany) {
        this.poUpdateCompany = poUpdateCompany;
    }

    public RichInputText getPoUpdateCompany() {
        return poUpdateCompany;
    }

    public void setPoUpdateAddress(RichInputText poUpdateAddress) {
        this.poUpdateAddress = poUpdateAddress;
    }

    public RichInputText getPoUpdateAddress() {
        return poUpdateAddress;
    }

    public void setPoUpdatePhoneNumber(RichInputText poUpdatePhoneNumber) {
        this.poUpdatePhoneNumber = poUpdatePhoneNumber;
    }

    public RichInputText getPoUpdatePhoneNumber() {
        return poUpdatePhoneNumber;
    }

    public void setPoUpdateStatus(RichSelectBooleanCheckbox poUpdateStatus) {
        this.poUpdateStatus = poUpdateStatus;
    }

    public RichSelectBooleanCheckbox getPoUpdateStatus() {
        return poUpdateStatus;
    }
   
    public void setPoPopupMails(RichPopup poPopupMails) {
        this.poPopupMails = poPopupMails;
    }

    public RichPopup getPoPopupMails() {
        return poPopupMails;
    }

    public void setPoRfcCte(RichInputText poRfcCte) {
        this.poRfcCte = poRfcCte;
    }

    public RichInputText getPoRfcCte() {
        return poRfcCte;
    }

    public void setPoCteDesc(RichInputText poCteDesc) {
        this.poCteDesc = poCteDesc;
    }

    public RichInputText getPoCteDesc() {
        return poCteDesc;
    }

    public void setPoTblEmails(RichTable poTblEmails) {
        this.poTblEmails = poTblEmails;
    }

    public RichTable getPoTblEmails() {
        return poTblEmails;
    }

    public void setPoCrudEmail(RichInputText poCrudEmail) {
        this.poCrudEmail = poCrudEmail;
    }

    public RichInputText getPoCrudEmail() {
        return poCrudEmail;
    }

    public void setPoCrudTitular(RichInputText poCrudTitular) {
        this.poCrudTitular = poCrudTitular;
    }

    public RichInputText getPoCrudTitular() {
        return poCrudTitular;
    }
    
    public void setPoIdCteDesc(RichInputText poIdCteDesc) {
        this.poIdCteDesc = poIdCteDesc;
    }

    public RichInputText getPoIdCteDesc() {
        return poIdCteDesc;
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

    public void setPoTblProviders(RichTable poTblProviders) {
        this.poTblProviders = poTblProviders;
    }

    public RichTable getPoTblProviders() {
        return poTblProviders;
    }



}

/**
* Project: OLS - Administración de Clientes
*
* File: UsersBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans;

import java.io.IOException;

import java.util.Date;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import mx.com.ols.fiscal.view.front.beans.utils.ADFUtils;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;

/** Esta clase gestiona la pantalla de Administraci�n de Usuarios<br/>
 *
 * @author Jorge Luis Bautista
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class UsersBean {
    private RichInputText poFilterUserName;
    private RichInputText poFilterName;
    private RichInputText poFilterLastName;
    private RichInputText poFilterLastSecName;
    private RichInputText poFilterDescription;
    private RichInputText poEmailColTxt;
    private RichSelectOneChoice poFilterStatusSel;
    private RichInputText poFilterEmail;
    private RichInputText poSavePassword;
    private RichSelectBooleanCheckbox poSaveStatus;
    private RichInputText poSaveEmail;
    private RichInputText poSaveDescription;
    private RichInputText poSaveLastSecName;
    private RichInputText poSaveLastName;
    private RichInputText poSaveName;
    private RichInputText poSaveUserName;
    private RichPopup poPopupSave;
    private RichPopup poPopupDelete;
    private RichPanelLabelAndMessage poDeleteMsgLbl;
    private RichOutputText poDeleteIdBinding;
    private RichPopup poPopupUpdate;
    private RichInputText poUpdateIdUser;
    private RichInputText poUpdateUserName;
    private RichInputText poUpdateName;
    private RichInputText poUpdateLastName;
    private RichInputText poUpdateLastSecName;
    private RichInputText poUpdateDescription;
    private RichInputText poUpdateEmail;
    private RichInputText poUpdatePassword;
    private RichSelectBooleanCheckbox poUpdateStatus;
    private RichTable poTblMain;
    String              gsAmDef = "mx.com.ols.model.OlsAppModuleImpl";
    String              gsConfig = "OlsAppModuleLocal";
    String              lsEntityView = "OlsCatUsersTabView1";
    String              lsEntityIterator = "OlsCatUsersTabView1Iterator";
    String              lsAppModuleDataControl = "OlsAppModuleDataControl";
    Map goSessionScope = ADFContext.getCurrent().getSessionScope();
    String gsUserName = (String)goSessionScope.get("loggedOlsUser");
    String gsIdUser = (String)goSessionScope.get("loggedOlsIdUser");
    Integer liIdUser = Integer.parseInt(gsIdUser);
    

    public UsersBean() {
    }

    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterDescription().setValue(null);
        getPoFilterEmail().setValue(null);
        getPoFilterLastName().setValue(null);
        getPoFilterLastSecName().setValue(null);
        getPoFilterName().setValue(null);
        getPoFilterStatusSel().setValue(null);
        getPoFilterUserName().setValue(null);
    }

    public String refreshMainTable() {
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
    }

    public void setPoFilterUserName(RichInputText poFilterUserName) {
        this.poFilterUserName = poFilterUserName;
    }

    public RichInputText getPoFilterUserName() {
        return poFilterUserName;
    }

    public void setPoFilterName(RichInputText poFilterName) {
        this.poFilterName = poFilterName;
    }

    public RichInputText getPoFilterName() {
        return poFilterName;
    }

    public void setPoFilterLastName(RichInputText poFilterLastName) {
        this.poFilterLastName = poFilterLastName;
    }

    public RichInputText getPoFilterLastName() {
        return poFilterLastName;
    }

    public void setPoFilterLastSecName(RichInputText poFilterLastSecName) {
        this.poFilterLastSecName = poFilterLastSecName;
    }

    public RichInputText getPoFilterLastSecName() {
        return poFilterLastSecName;
    }

    public void setPoFilterDescription(RichInputText poFilterDescription) {
        this.poFilterDescription = poFilterDescription;
    }

    public RichInputText getPoFilterDescription() {
        return poFilterDescription;
    }

    public void setPoEmailColTxt(RichInputText poEmailColTxt) {
        this.poEmailColTxt = poEmailColTxt;
    }

    public RichInputText getPoEmailColTxt() {
        return poEmailColTxt;
    }

    public void setPoFilterStatusSel(RichSelectOneChoice poFilterStatusSel) {
        this.poFilterStatusSel = poFilterStatusSel;
    }

    public RichSelectOneChoice getPoFilterStatusSel() {
        return poFilterStatusSel;
    }

    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        String lsNomUserName = 
            getPoFilterUserName().getValue() == null ? "": 
            getPoFilterUserName().getValue().toString();        
        if(!lsNomUserName.equalsIgnoreCase("")){
            lsQuery += " AND ID_USER_NAME LIKE '" + lsNomUserName + "%'";
        }
        
        String lsName = 
            getPoFilterName().getValue() == null ? "": 
            getPoFilterName().getValue().toString();        
        if(!lsName.equalsIgnoreCase("")){
            lsQuery += " AND NOM_USER LIKE '" + lsName + "%'";
        }
        
        String lsLastName = 
            getPoFilterLastName().getValue() == null ? "": 
            getPoFilterLastName().getValue().toString();        
        if(!lsLastName.equalsIgnoreCase("")){
            lsQuery += " AND IND_LAST_NAME LIKE '" + lsLastName + "%'";
        }
        
        String lsSecLastName = 
            getPoFilterLastSecName().getValue() == null ? "": 
            getPoFilterLastSecName().getValue().toString();        
        if(!lsSecLastName.equalsIgnoreCase("")){
            lsQuery += " AND IND_SECOND_NAME LIKE '" + lsSecLastName + "%'";
        }
        
        String lsDescription = 
            getPoFilterDescription().getValue() == null ? "": 
            getPoFilterDescription().getValue().toString();        
        if(!lsDescription.equalsIgnoreCase("")){
            lsQuery += " AND DES_USER LIKE '" + lsDescription + "%'";
        }
        
        String lsEmail = 
            getPoFilterEmail().getValue() == null ? "": 
            getPoFilterEmail().getValue().toString();        
        if(!lsEmail.equalsIgnoreCase("")){
            lsQuery += " AND IND_MAIL_USER LIKE '" + lsEmail + "%'";
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

    public void setPoFilterEmail(RichInputText poFilterEmail) {
        this.poFilterEmail = poFilterEmail;
    }

    public RichInputText getPoFilterEmail() {
        return poFilterEmail;
    }

    public void setPoSavePassword(RichInputText poSavePassword) {
        this.poSavePassword = poSavePassword;
    }

    public RichInputText getPoSavePassword() {
        return poSavePassword;
    }

    public void setPoSaveStatus(RichSelectBooleanCheckbox poSaveStatus) {
        this.poSaveStatus = poSaveStatus;
    }

    public RichSelectBooleanCheckbox getPoSaveStatus() {
        return poSaveStatus;
    }

    public void setPoSaveEmail(RichInputText poSaveEmail) {
        this.poSaveEmail = poSaveEmail;
    }

    public RichInputText getPoSaveEmail() {
        return poSaveEmail;
    }

    public void setPoSaveDescription(RichInputText poSaveDescription) {
        this.poSaveDescription = poSaveDescription;
    }

    public RichInputText getPoSaveDescription() {
        return poSaveDescription;
    }

    public void setPoSaveLastSecName(RichInputText poSaveLastSecName) {
        this.poSaveLastSecName = poSaveLastSecName;
    }

    public RichInputText getPoSaveLastSecName() {
        return poSaveLastSecName;
    }

    public void setPoSaveLastName(RichInputText poSaveLastName) {
        this.poSaveLastName = poSaveLastName;
    }

    public RichInputText getPoSaveLastName() {
        return poSaveLastName;
    }

    public void setPoSaveName(RichInputText poSaveName) {
        this.poSaveName = poSaveName;
    }

    public RichInputText getPoSaveName() {
        return poSaveName;
    }

    public void setPoSaveUserName(RichInputText poSaveUserName) {
        this.poSaveUserName = poSaveUserName;
    }

    public RichInputText getPoSaveUserName() {
        return poSaveUserName;
    }

    public void setPoPopupSave(RichPopup poPopupSave) {
        this.poPopupSave = poPopupSave;
    }

    public RichPopup getPoPopupSave() {
        return poPopupSave;
    }

    public String saveAction() {
        
        UtilsOls loUtilsOls = new UtilsOls();
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        Integer                    liId = loUtilsOls.getMaxIdTable("Users") + 1;
        
        String lsUserName = 
            getPoSaveUserName().getValue() == null ? "": 
            getPoSaveUserName().getValue().toString();                        
        String lsName = 
            getPoSaveName().getValue() == null ? "": 
            getPoSaveName().getValue().toString();        
        String lsLastName = 
            getPoSaveLastName().getValue() == null ? "": 
            getPoSaveLastName().getValue().toString();                
        String lsSecLastName = 
            getPoSaveLastSecName().getValue() == null ? "": 
            getPoSaveLastSecName().getValue().toString();                
        String lsDescription = 
            getPoSaveDescription().getValue() == null ? "": 
            getPoSaveDescription().getValue().toString();
        String lsEmail = 
            getPoSaveEmail().getValue() == null ? "": 
            getPoSaveEmail().getValue().toString();        
        
        String lsPassword = 
            getPoSavePassword().getValue() == null ? "": 
            getPoSavePassword().getValue().toString();        
        
        String lsIndEstatus = 
            getPoSaveStatus().getValue() == null ? "":
            getPoSaveStatus().getValue().toString();
        String                     lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }
        
        if(lsUserName.length() < 1){
            lsFieldErrorRq += "UserName,";
            lbProcess = false;   
        }
        if(lsEmail.length() < 1){
            lsFieldErrorRq += "Correo Electrónico,";
            lbProcess = false;   
        }
        if(lsPassword.length() < 1){
            lsFieldErrorRq += "Password,";
            lbProcess = false;   
        }        
        
        // Cifrar pssword ---------------
        String lsPwdEncrypted = null;
        try {
            lsPwdEncrypted = loUtilsOls.encryptObject(lsPassword, loUtilsOls.getKeyCoder());
        } catch (Exception e) {
            ;
        }
        //-------------------------------
        if(lbProcess){
            
            ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
            ViewObject        loVo = loAm.findViewObject(lsEntityView);
            Row               loRow = null;
            try {
                loRow = (oracle.jbo.server.ViewRowImpl)loVo.createRow();
                loRow.setAttribute("IdUser", liId);
                loRow.setAttribute("IdUserName", lsUserName);
                loRow.setAttribute("NomUser", lsName);
                loRow.setAttribute("IndLastName", lsLastName);
                loRow.setAttribute("IndSecondName", lsSecLastName);
                loRow.setAttribute("DesUser", lsDescription);
                loRow.setAttribute("IndMailUser", lsEmail);
                loRow.setAttribute("IndPassword", lsPwdEncrypted);
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

    public String cancelSaveAction() {        
        new UtilFaces().hidePopup(getPoPopupSave());
        FacesContext       loContext = FacesContext.getCurrentInstance();
        ExternalContext    loEctx = loContext.getExternalContext();
        String             lsUrl = 
            loEctx.getRequestContextPath() + "/faces/usersPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
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
        String                   lsId = 
        getPoDeleteIdBinding().getValue() == null ? "" : 
        getPoDeleteIdBinding().getValue().toString();
        
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityView);
        String lsWhere = "ID_USER = " + lsId;
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

    public String cancelDeleteAction() {
        new UtilFaces().hidePopup(getPoPopupDelete());
        return null;
    }

    public void setPoPopupUpdate(RichPopup poPopupUpdate) {
        this.poPopupUpdate = poPopupUpdate;
    }

    public RichPopup getPoPopupUpdate() {
        return poPopupUpdate;
    }

    public void setPoUpdateIdUser(RichInputText poUpdateIdUser) {
        this.poUpdateIdUser = poUpdateIdUser;
    }

    public RichInputText getPoUpdateIdUser() {
        return poUpdateIdUser;
    }

    public void setPoUpdateUserName(RichInputText poUpdateUserName) {
        this.poUpdateUserName = poUpdateUserName;
    }

    public RichInputText getPoUpdateUserName() {
        return poUpdateUserName;
    }

    public void setPoUpdateName(RichInputText poUpdateName) {
        this.poUpdateName = poUpdateName;
    }

    public RichInputText getPoUpdateName() {
        return poUpdateName;
    }

    public void setPoUpdateLastName(RichInputText poUpdateLastName) {
        this.poUpdateLastName = poUpdateLastName;
    }

    public RichInputText getPoUpdateLastName() {
        return poUpdateLastName;
    }

    public void setPoUpdateLastSecName(RichInputText poUpdateLastSecName) {
        this.poUpdateLastSecName = poUpdateLastSecName;
    }

    public RichInputText getPoUpdateLastSecName() {
        return poUpdateLastSecName;
    }

    public void setPoUpdateDescription(RichInputText poUpdateDescription) {
        this.poUpdateDescription = poUpdateDescription;
    }

    public RichInputText getPoUpdateDescription() {
        return poUpdateDescription;
    }

    public void setPoUpdateEmail(RichInputText poUpdateEmail) {
        this.poUpdateEmail = poUpdateEmail;
    }

    public RichInputText getPoUpdateEmail() {
        return poUpdateEmail;
    }

    public void setPoUpdatePassword(RichInputText poUpdatePassword) {
        this.poUpdatePassword = poUpdatePassword;
    }

    public RichInputText getPoUpdatePassword() {
        return poUpdatePassword;
    }

    public void setPoUpdateStatus(RichSelectBooleanCheckbox poUpdateStatus) {
        this.poUpdateStatus = poUpdateStatus;
    }

    public RichSelectBooleanCheckbox getPoUpdateStatus() {
        return poUpdateStatus;
    }

    public void setPoTblMain(RichTable poTblMain) {
        this.poTblMain = poTblMain;
    }

    public RichTable getPoTblMain() {
        return poTblMain;
    }

    public void showSavePopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        getPoSaveDescription().setValue("");
        getPoSaveEmail().setValue("");
        getPoSaveLastName().setValue("");
        getPoSaveLastSecName().setValue("");
        getPoSaveName().setValue("");
        getPoSavePassword().setValue("");
        getPoSaveStatus().setValue("");
        getPoSaveUserName().setValue("");
        new UtilFaces().showPopup(getPoPopupSave());
    }

    public void showEditPopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        UtilsOls loUtilsOls = new UtilsOls();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        String                   lsIdUser = 
            loNode.getAttribute("IdUser") == null ? "" : 
            loNode.getAttribute("IdUser").toString();                 
        String                   lsUserName = 
            loNode.getAttribute("IdUserName") == null ? "" : 
            loNode.getAttribute("IdUserName").toString();   
        String                   lsName = 
            loNode.getAttribute("NomUser") == null ? "" : 
            loNode.getAttribute("NomUser").toString();   
        String                   lsLastName = 
            loNode.getAttribute("IndLastName") == null ? "" : 
            loNode.getAttribute("IndLastName").toString();   
        String                   lsSecondName = 
            loNode.getAttribute("IndSecondName") == null ? "" : 
            loNode.getAttribute("IndSecondName").toString();
        String                   lsDescription = 
            loNode.getAttribute("DesUser") == null ? "" : 
            loNode.getAttribute("DesUser").toString();
        String                   lsEmail = 
            loNode.getAttribute("IndMailUser") == null ? "" : 
            loNode.getAttribute("IndMailUser").toString();
        String                   lsPassword = 
            loNode.getAttribute("IndPassword") == null ? "" : 
            loNode.getAttribute("IndPassword").toString();
        String                   lsIndEstatus = 
            loNode.getAttribute("IndEstatus") == null ? "" : 
            loNode.getAttribute("IndEstatus").toString();
        // Settear valores al popup
        getPoUpdateIdUser().setValue(lsIdUser);                
        getPoUpdateUserName().setValue(lsUserName);                
        getPoUpdateName().setValue(lsName);                
        getPoUpdateLastName().setValue(lsLastName);                
        getPoUpdateLastSecName().setValue(lsSecondName); 
        getPoUpdateDescription().setValue(lsDescription);  
        getPoUpdateEmail().setValue(lsEmail);
        try {
            String lsPwdEcrypted = loUtilsOls.decryptObject(lsPassword, loUtilsOls.getKeyCoder());
            getPoUpdatePassword().setValue(lsPwdEcrypted); 
        } catch (Exception e) {
            ;
        }
        
        if(lsIndEstatus.equalsIgnoreCase("1")){
            getPoUpdateStatus().setSelected(true);
        }else{
            getPoUpdateStatus().setSelected(false);
        }
        new UtilFaces().showPopup(getPoPopupUpdate());
    }

    public void showDeletePopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblMain().getSelectedRowData();
        
        String                   lsIdUser = 
            loNode.getAttribute("IdUser") == null ? "" : 
            loNode.getAttribute("IdUser").toString();                 
        String                   lsUserName = 
            loNode.getAttribute("IdUserName") == null ? "" : 
            loNode.getAttribute("IdUserName").toString();   
        getPoDeleteIdBinding().setValue(lsIdUser);
        getPoDeleteMsgLbl().setLabel("Eliminar a " + lsUserName + " : ");
        new UtilFaces().showPopup(getPoPopupDelete());
    }

    public String updateAction() {
        UtilsOls loUtilsOls = new UtilsOls();        
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        String lsId = 
            getPoUpdateIdUser().getValue() == null ? "": 
            getPoUpdateIdUser().getValue().toString();        
        String lsUserName = 
            getPoUpdateUserName().getValue() == null ? "": 
            getPoUpdateUserName().getValue().toString(); 
        String lsName = 
            getPoUpdateName().getValue() == null ? "": 
            getPoUpdateName().getValue().toString();     
        String lsLastName = 
            getPoUpdateLastName().getValue() == null ? "": 
            getPoUpdateLastName().getValue().toString(); 
        String lsSecLastName = 
            getPoUpdateLastSecName().getValue() == null ? "": 
            getPoUpdateLastSecName().getValue().toString();                
        String lsDescription = 
            getPoUpdateDescription().getValue() == null ? "": 
            getPoUpdateDescription().getValue().toString();
        String lsEmail = 
            getPoUpdateEmail().getValue() == null ? "": 
            getPoUpdateEmail().getValue().toString();    
        String lsPassword = 
            getPoUpdatePassword().getValue() == null ? "": 
            getPoUpdatePassword().getValue().toString(); 
        System.out.println("flag 03 "+lsPassword);
        if(lsUserName.length() < 1){
            lsFieldErrorRq += "UserName,";
            lbProcess = false;   
        }
        if(lsEmail.length() < 1){
            lsFieldErrorRq += "Correo Electr\u00f3nico,";
            lbProcess = false;   
        }
        if(lsPassword.length() < 1){
            lsFieldErrorRq += "Password,";
            lbProcess = false;   
        }     
        // Cifrar pssword ---------------
        String lsPwdEncrypted = null;
        try {
            lsPwdEncrypted = loUtilsOls.encryptObject(lsPassword, loUtilsOls.getKeyCoder());
        } catch (Exception e) {
            System.out.println("Error al enctriptar: "+e.getMessage());
            lbProcess = false;   
        }
        //-------------------------------
        
        String lsIndEstatus = 
            getPoUpdateStatus().getValue() == null ? "": 
            getPoUpdateStatus().getValue().toString();
        String lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }     
        System.out.println("procesar? ["+lbProcess+"]");
        if(lbProcess){   
            System.out.println("procesar........");
            ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
            ViewObject           loVo = loAm.findViewObject(lsEntityView);
            String lsWhere = "ID_USER = " + lsId;
            System.out.println("WHERE: "+lsWhere);
            loVo.setWhereClause(lsWhere);               
            loVo.executeQuery();
            try {
                Row[] loArrObj = loVo.getAllRowsInRange(); 
                if(loArrObj.length > 0) {    
                    System.out.println("usuario entcontrado");
                    Row loRow = loVo.getAllRowsInRange()[0];    
                    loRow.setAttribute("NomUser", lsName);
                    loRow.setAttribute("IndLastName", lsLastName);
                    loRow.setAttribute("IndSecondName", lsSecLastName);
                    loRow.setAttribute("DesUser", lsDescription);
                    loRow.setAttribute("IndMailUser", lsEmail);
                    loRow.setAttribute("IndPassword", lsPwdEncrypted);
                    loRow.setAttribute("IndEstatus", lsStatusTab);
                    loRow.setAttribute("FecLastUpdateDate", new Date());
                    loRow.setAttribute("NumLastUpdatedBy", liIdUser);
                    loVo.setCurrentRow(loRow);
                    System.out.println("sobreescribiendo");
                }
                loAm.getTransaction().commit();
                System.out.println("sobreescribiendo......OK");
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
            loEctx.getRequestContextPath() + "/faces/usersPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
    }
}

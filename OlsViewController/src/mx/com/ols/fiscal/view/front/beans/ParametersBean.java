/**
* Project: OLS - Administración de Clientes
*
* File: ParametersBean.java
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

import mx.com.ols.fiscal.view.front.beans.model.daos.ViewObjectDao;
import mx.com.ols.fiscal.view.front.beans.utils.ADFUtils;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierNodeBinding;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;

/** Esta clase gestiona la pantalla de parametros generales<br/>
 *
 * @author Jorge Luis Bautista
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class ParametersBean {
    private RichInputText poFilterParameter;
    private RichInputText poFilterDescParameter;
    private RichInputText poFilterUsedBy;
    private RichInputText poFilterParameterValue;
    private RichSelectOneChoice poFilterStatusSel;
    private RichInputText poSaveParameter;
    private RichInputText poSaveDescription;
    private RichInputText poSaveUsedBy;
    private RichInputText poSaveValue;
    private RichSelectBooleanCheckbox poSaveStatus;
    private RichPopup poPopupDelete;
    private RichPopup poPopupSave;
    private RichPanelLabelAndMessage poDeleteMsgLbl;
    private RichOutputText poDeleteIdBinding;
    private RichPopup poPopupUpdate;
    private RichInputText poUpdateIdParameter;
    private RichInputText poUpdateParameter;
    private RichInputText poUpdateDescription;
    private RichInputText poUpdateUsedBy;
    private RichInputText poUpdateValue;
    private RichSelectBooleanCheckbox poUpdateStatus;
    private RichTable poTblParameters;
    String              gsAmDef = "mx.com.ols.model.OlsAppModuleImpl";
    String              gsConfig = "OlsAppModuleLocal";
    String              lsEntityView = "OlsCatConfigParamTabView1";
    String              lsEntityIterator = "OlsCatConfigParamTabView1Iterator";
    String              lsAppModuleDataControl = "OlsAppModuleDataControl";
    Map goSessionScope = ADFContext.getCurrent().getSessionScope();
    String gsUserName = (String)goSessionScope.get("loggedOlsUser");
    String gsIdUser = (String)goSessionScope.get("loggedOlsIdUser");
    Integer liIdUser = Integer.parseInt(gsIdUser);
    
    
    public ParametersBean() {
    }

    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterDescParameter().setValue(null);
        getPoFilterParameter().setValue(null);
        getPoFilterStatusSel().setValue(null);
        getPoFilterParameterValue().setValue(null);
        getPoFilterUsedBy().setValue(null);
    }

    public String refreshMainTable() {
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblParameters());
        return null;
    }

    public void setPoFilterParameter(RichInputText poFilterParameter) {
        this.poFilterParameter = poFilterParameter;
    }

    public RichInputText getPoFilterParameter() {
        return poFilterParameter;
    }

    public void setPoFilterDescParameter(RichInputText poFilterDescParameter) {
        this.poFilterDescParameter = poFilterDescParameter;
    }

    public RichInputText getPoFilterDescParameter() {
        return poFilterDescParameter;
    }

    public void setPoFilterUsedBy(RichInputText poFilterUsedBy) {
        this.poFilterUsedBy = poFilterUsedBy;
    }

    public RichInputText getPoFilterUsedBy() {
        return poFilterUsedBy;
    }

    public void setPoFilterParameterValue(RichInputText poFilterParameterValue) {
        this.poFilterParameterValue = poFilterParameterValue;
    }

    public RichInputText getPoFilterParameterValue() {
        return poFilterParameterValue;
    }

    public void setPoFilterStatusSel(RichSelectOneChoice poFilterStatusSel) {
        this.poFilterStatusSel = poFilterStatusSel;
    }

    public RichSelectOneChoice getPoFilterStatusSel() {
        return poFilterStatusSel;
    }

    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        
        String lsNomParameter = 
            getPoFilterParameter().getValue() == null ? "": 
            getPoFilterParameter().getValue().toString();        
        if(!lsNomParameter.equalsIgnoreCase("")){
            lsQuery += " AND NOM_PARAMETER LIKE '" + lsNomParameter + "%'";
        }
        
        String lsDescParameter = 
            getPoFilterDescParameter().getValue() == null ? "": 
            getPoFilterDescParameter().getValue().toString();        
        if(!lsNomParameter.equalsIgnoreCase("")){
            lsQuery += " AND IND_DESC_PARAMETER LIKE '" + lsDescParameter + "%'";
        }
        
        String lsUsedBy = 
            getPoFilterUsedBy().getValue() == null ? "": 
            getPoFilterUsedBy().getValue().toString();        
        if(!lsNomParameter.equalsIgnoreCase("")){
            lsQuery += " AND IND_USED_BY LIKE '" + lsUsedBy + "%'";
        }
        
        String lsValParameter = 
            getPoFilterParameterValue().getValue() == null ? "": 
            getPoFilterParameterValue().getValue().toString();        
        if(!lsValParameter.equalsIgnoreCase("")){
            lsQuery += " AND IND_VAL_PARAMETER LIKE '" + lsValParameter + "%'";
        }
        String lsIndEstatus = 
            getPoFilterStatusSel().getValue() == null ? "": 
            getPoFilterStatusSel().getValue().toString();        
        if(!lsIndEstatus.equalsIgnoreCase("")){
            lsQuery += " AND IND_ESTATUS = '" + lsIndEstatus + "'";
        }
        new UtilFaces().refreshTableWhereIterator(lsQuery, lsEntityIterator, getPoTblParameters());        
        
        return null;
    }


    public void setPoSaveParameter(RichInputText poSaveParameter) {
        this.poSaveParameter = poSaveParameter;
    }

    public RichInputText getPoSaveParameter() {
        return poSaveParameter;
    }

    public void setPoSaveDescription(RichInputText poSaveDescription) {
        this.poSaveDescription = poSaveDescription;
    }

    public RichInputText getPoSaveDescription() {
        return poSaveDescription;
    }

    public void setPoSaveUsedBy(RichInputText poSaveUsedBy) {
        this.poSaveUsedBy = poSaveUsedBy;
    }

    public RichInputText getPoSaveUsedBy() {
        return poSaveUsedBy;
    }

    public void setPoSaveValue(RichInputText poSaveValue) {
        this.poSaveValue = poSaveValue;
    }

    public RichInputText getPoSaveValue() {
        return poSaveValue;
    }

    public void setPoSaveStatus(RichSelectBooleanCheckbox poSaveStatus) {
        this.poSaveStatus = poSaveStatus;
    }

    public RichSelectBooleanCheckbox getPoSaveStatus() {
        return poSaveStatus;
    }

    public void setPoPopupDelete(RichPopup poPopupDelete) {
        this.poPopupDelete = poPopupDelete;
    }

    public RichPopup getPoPopupDelete() {
        return poPopupDelete;
    }

    public void setPoPopupSave(RichPopup poPopupSave) {
        this.poPopupSave = poPopupSave;
    }

    public RichPopup getPoPopupSave() {
        return poPopupSave;
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

    public String saveAction() {
        UtilsOls loUtilsOls = new UtilsOls();
        boolean                    lbProcess = true;
        String                     lsFieldErrorRq = "";
        Integer                    liIdParameter = 
            loUtilsOls.getMaxIdTable("Parameters") + 1;
        String                     lsNomParameter = 
            getPoSaveParameter().getValue() == null ? "" : 
            getPoSaveParameter().getValue().toString();
        String                    lsDescription = 
            getPoSaveDescription().getValue() == null ? "" : 
            getPoSaveDescription().getValue().toString();
        String                     lsUsedBy = 
            getPoSaveUsedBy().getValue() == null ? "" : 
            getPoSaveUsedBy().getValue().toString();
        String                     lsValueParameter = 
            getPoSaveValue().getValue() == null ? "" :
            getPoSaveValue().getValue().toString();
        String                     lsIndEstatus = 
            getPoSaveStatus().getValue() == null ? "":
            getPoSaveStatus().getValue().toString();
        String                     lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }
        
        if(lsNomParameter.length() < 1){
            lsFieldErrorRq += "Nombre del Par\u00e1metro,";
            lbProcess = false;   
        }
        if(lsValueParameter.length() < 1){
            lsFieldErrorRq += "Valor del Par\u00e1metro,";
            lbProcess = false;   
        }
        
        if(lbProcess){
            
            ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
            ViewObject        loVo = loAm.findViewObject(lsEntityView);
            Row               loRow = null;
            try {
                loRow = (oracle.jbo.server.ViewRowImpl)loVo.createRow();
                loRow.setAttribute("IdParameter", liIdParameter);
                loRow.setAttribute("NomParameter", lsNomParameter);
                loRow.setAttribute("IndDescParameter", lsDescription);
                loRow.setAttribute("IndUsedBy", lsUsedBy);
                loRow.setAttribute("IndValueParameter", lsValueParameter);
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
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblParameters());
        return null;
    }

    public String cancelSaveAction() {
        new UtilFaces().hidePopup(getPoPopupSave());
        FacesContext       loContext = FacesContext.getCurrentInstance();
        ExternalContext    loEctx = loContext.getExternalContext();
        String             lsUrl = 
            loEctx.getRequestContextPath() + "/faces/parametersPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
    }

    public String deleteAction() {
        String                   lsIdParameter = 
        getPoDeleteIdBinding().getValue() == null ? "" : 
        getPoDeleteIdBinding().getValue().toString();
       
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityView);
        String lsWhere = "ID_PARAMETER = "+lsIdParameter;
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
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblParameters());
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

    public void setPoUpdateIdParameter(RichInputText poUpdateIdParameter) {
        this.poUpdateIdParameter = poUpdateIdParameter;
    }

    public RichInputText getPoUpdateIdParameter() {
        return poUpdateIdParameter;
    }

    public void setPoUpdateParameter(RichInputText poUpdateParameter) {
        this.poUpdateParameter = poUpdateParameter;
    }

    public RichInputText getPoUpdateParameter() {
        return poUpdateParameter;
    }

    public void setPoUpdateDescription(RichInputText poUpdateDescription) {
        this.poUpdateDescription = poUpdateDescription;
    }

    public RichInputText getPoUpdateDescription() {
        return poUpdateDescription;
    }

    public void setPoUpdateUsedBy(RichInputText poUpdateUsedBy) {
        this.poUpdateUsedBy = poUpdateUsedBy;
    }

    public RichInputText getPoUpdateUsedBy() {
        return poUpdateUsedBy;
    }

    public void setPoUpdateValue(RichInputText poUpdateValue) {
        this.poUpdateValue = poUpdateValue;
    }

    public RichInputText getPoUpdateValue() {
        return poUpdateValue;
    }

    public void setPoUpdateStatus(RichSelectBooleanCheckbox poUpdateStatus) {
        this.poUpdateStatus = poUpdateStatus;
    }

    public RichSelectBooleanCheckbox getPoUpdateStatus() {
        return poUpdateStatus;
    }

    public String updateAction() {
        String lsIdParameter = 
            getPoUpdateIdParameter().getValue() == null ? "": 
            getPoUpdateIdParameter().getValue().toString();        
        String lsNomParameter = 
            getPoUpdateParameter().getValue() == null ? "": 
            getPoUpdateParameter().getValue().toString();
        String                    lsDescription = 
            getPoUpdateDescription().getValue() == null ? "" : 
            getPoUpdateDescription().getValue().toString();
        String                     lsUsedBy = 
            getPoUpdateUsedBy().getValue() == null ? "" : 
            getPoUpdateUsedBy().getValue().toString();
        String lsValueParameter = 
            getPoUpdateValue().getValue() == null ? "": 
            getPoUpdateValue().getValue().toString();
        String lsIndEstatus = 
            getPoUpdateStatus().getValue() == null ? "": 
            getPoUpdateStatus().getValue().toString();
        String lsStatusTab = "0";
        if(lsIndEstatus.equalsIgnoreCase("true")){
            lsStatusTab = "1";
        }        
        
        ApplicationModule    loAm = ADFUtils.getApplicationModuleForDataControl(lsAppModuleDataControl);
        ViewObject           loVo = loAm.findViewObject(lsEntityView);
        String lsWhere = "ID_PARAMETER = " + lsIdParameter;
        loVo.setWhereClause(lsWhere);               
        loVo.executeQuery();
        try {
            Row[] loArrObj = loVo.getAllRowsInRange(); 
            if(loArrObj.length > 0) {     
                Row loRow = loVo.getAllRowsInRange()[0];    
                
                loRow.setAttribute("NomParameter", lsNomParameter);
                loRow.setAttribute("IndDescParameter", lsDescription);
                loRow.setAttribute("IndUsedBy", lsUsedBy);
                loRow.setAttribute("IndValueParameter", lsValueParameter);
                loRow.setAttribute("IndEstatus", lsStatusTab);
                loRow.setAttribute("FecLastUpdateDate", new Date());
                loRow.setAttribute("NumLastUpdatedBy", liIdUser);         
                loVo.setCurrentRow(loRow);
            }
            loAm.getTransaction().commit();
        } catch (Exception loEx) {
            System.out.println("Update ERROR!!"+loEx.getMessage());
        }
        new UtilFaces().hidePopup(getPoPopupUpdate());
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblParameters());
        return null;
    }

    public String cancelUpdateAction() {
        new UtilFaces().hidePopup(getPoPopupUpdate());
        FacesContext       loContext = FacesContext.getCurrentInstance();
        ExternalContext    loEctx = loContext.getExternalContext();
        String             lsUrl = 
            loEctx.getRequestContextPath() + "/faces/parametersPage";        
        try {
            loEctx.redirect(lsUrl);
        } catch (IOException loEx) {
            ;
        }
        return null;
    }

    public void setPoTblParameters(RichTable poTblParameters) {
        this.poTblParameters = poTblParameters;
    }

    public RichTable getPoTblParameters() {
        return poTblParameters;
    }

    public void showSavePopup(ActionEvent toActionEvent) {
        toActionEvent.getSource();
        getPoSaveDescription().setValue("");
        getPoSaveParameter().setValue("");
        getPoSaveStatus().setValue("");
        getPoSaveUsedBy().setValue("");
        getPoSaveValue().setValue("");
        getPoSaveStatus().setValue("");
        new UtilFaces().showPopup(getPoPopupSave());
    }

    public void showEditPopup(ActionEvent poActionEvent) {
        poActionEvent.getSource();
        FacesCtrlHierNodeBinding loNode = 
            (FacesCtrlHierNodeBinding) getPoTblParameters().getSelectedRowData();
        String                   lsIdParameter = 
            loNode.getAttribute("IdParameter") == null ? "" : 
            loNode.getAttribute("IdParameter").toString();                 
        String                   lsNomParameter = 
            loNode.getAttribute("NomParameter") == null ? "" : 
            loNode.getAttribute("NomParameter").toString();   
        String                   lsDescParameter = 
            loNode.getAttribute("IndDescParameter") == null ? "" : 
            loNode.getAttribute("IndDescParameter").toString();   
        String                   lsUsedBy = 
            loNode.getAttribute("IndUsedBy") == null ? "" : 
            loNode.getAttribute("IndUsedBy").toString();   
        String                   lsParameterValue = 
            loNode.getAttribute("IndValueParameter") == null ? "" : 
            loNode.getAttribute("IndValueParameter").toString();
        String                   lsIndEstatus = 
            loNode.getAttribute("IndEstatus") == null ? "" : 
            loNode.getAttribute("IndEstatus").toString();
        // Settear valores al popup
        getPoUpdateIdParameter().setValue(lsIdParameter);                
        getPoUpdateParameter().setValue(lsNomParameter);                  
        getPoUpdateDescription().setValue(lsDescParameter);                  
        getPoUpdateUsedBy().setValue(lsUsedBy);                  
        getPoUpdateValue().setValue(lsParameterValue);
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
            (FacesCtrlHierNodeBinding) getPoTblParameters().getSelectedRowData();
        
        String                   lsIdParameter = 
            loNode.getAttribute("IdParameter") == null ? "" : 
            loNode.getAttribute("IdParameter").toString();                 
        String                   lsNomParameter = 
            loNode.getAttribute("NomParameter") == null ? "" : 
            loNode.getAttribute("NomParameter").toString();   
        getPoDeleteIdBinding().setValue(lsIdParameter);
        
        getPoDeleteMsgLbl().setLabel("Eliminar a " + lsNomParameter + " : ");
        new UtilFaces().showPopup(getPoPopupDelete());
    }
    
}

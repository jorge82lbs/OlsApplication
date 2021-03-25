/**
* Project: OLS - Administración de Clientes
*
* File: UtilFaces.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans.utils;

import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import mx.com.ols.fiscal.view.front.beans.types.MapBean;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

/** Esta clase ofrece metodos de utileria para ayuda en el desarrollo<br/>
 *
 * @author Jorge Luis Bautista - Omw
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class UtilFaces {
    String gsAmDef = "com.televisa.integration.model.AppModuleIntergrationImpl";
    String gsConfig = "AppModuleIntergrationLocal";
    
    /**
     * Muestra popup genérico en pantalla
     * @autor Jorge Luis Bautista Santiago  
     * @param toPopup
     * @return void
     */
    public void showPopup(RichPopup toPopup) {
        FacesContext             loFacesContext = 
            FacesContext.getCurrentInstance();
        ExtendedRenderKitService loService =
            org.apache.myfaces.trinidad.util.Service.getRenderKitService(loFacesContext,
                                                                         ExtendedRenderKitService.class);
        loService.addScript(loFacesContext,
                          "AdfPage.PAGE.findComponent('" + toPopup.getClientId(loFacesContext) +
                          "').show();");
    }

    /**
     * Oculta popup generico de la pantalla pantalla
     * @autor Jorge Luis Bautista Santiago  
     * @param toPopup
     * @return void
     */
    public void hidePopup(RichPopup toPopup) {
        FacesContext             loFacesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService loService =
            org.apache.myfaces.trinidad.util.Service.getRenderKitService(loFacesContext,
                                                                         ExtendedRenderKitService.class);
        loService.addScript(loFacesContext,
                          "AdfPage.PAGE.findComponent('" + 
                          toPopup.getClientId(loFacesContext) +
                          "').hide();");
    }
    
    /**
     * Resuelve expresion para obtener objeto
     * @autor Jorge Luis Bautista Santiago  
     * @param tsExpression
     * @return Object
     */
    public Object resolveExpression(String tsExpression) {
        FacesContext      loFacesContext = FacesContext.getCurrentInstance();
        Application       loApp = loFacesContext.getApplication();
        ExpressionFactory loElFactory = loApp.getExpressionFactory();
        ELContext         loElContext = loFacesContext.getELContext();
        ValueExpression   loValueExp =
            loElFactory.createValueExpression(loElContext, tsExpression,
                                            Object.class);
        return loValueExp.getValue(loElContext);
    }
    
    /**
     * Actualiza iterador de la tabla principal con condiciones de busqueda
     * @autor Jorge Luis Bautista Santiago
     * @param tsWhere
     * @param tsIteraror
     * @param toRichTable
     * @return void
     */
    public void refreshTableWhereIterator(String tsWhere, 
                                          String tsIteraror, 
                                          RichTable toRichTable
                                          ) {
        try{
            DCBindingContainer loDCBinding = 
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();         
            DCIteratorBinding  loDCIterator = 
                loDCBinding.findIteratorBinding(tsIteraror);        
            ViewObject         loVO = loDCIterator.getViewObject();
            loVO.setWhereClause(tsWhere);
            //System.out.println(loVO.getQuery());
            loVO.executeQuery();
            AdfFacesContext.getCurrentInstance().addPartialTarget(toRichTable);
        }catch(Exception loIntExp){
         System.out.println(loIntExp.getMessage());   
        }
    }
    
    /**
     * Actualiza iterador de la tabla principal con condiciones de busqueda
     * Tabla parametrizada
     * @autor Jorge Luis Bautista Santiago
     * @param tsWhere
     * @param tsIteraror
     * @param toRichTable
     * @return void
     */
    public void refreshTableWherePrmIterator(String tsWhere, 
                                             String tsIteraror, 
                                             RichTable toRichTable,
                                             List<MapBean> laParams
                                          ) {
        try{
            DCBindingContainer loDCBinding =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding  loDCIterator =
                loDCBinding.findIteratorBinding(tsIteraror);
            ViewObject         loVO = loDCIterator.getViewObject();
            for(MapBean loMapBean : laParams){
                loVO.setNamedWhereClauseParam(loMapBean.getLsId(), loMapBean.getLsValue());   
            } 
            loVO.setWhereClause(tsWhere);
            //System.out.println(loVO.getQuery());
            loVO.executeQuery();
            AdfFacesContext.getCurrentInstance().addPartialTarget(toRichTable);
        }catch(Exception loIntExp){
         System.out.println(loIntExp.getMessage());   
        }
    }

}

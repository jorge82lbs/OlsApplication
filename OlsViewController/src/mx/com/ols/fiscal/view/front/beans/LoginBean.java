/**
* Project: OLS - Administración de Clientes
*
* File: LoginBean.java 
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatUsersTab;
import mx.com.ols.fiscal.view.front.beans.users.UserInfoBean;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;

/** Esta clase gestiona el login template<br/>
 *
 * @author Jorge Luis Bautista - Omw
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class LoginBean {
    private RichInputText poUserName;
    private RichInputText poPassword;
    private RichPopup     poPopupExitConfirm;

    public void setPoPopupExitConfirm(RichPopup poPopupExitConfirm) {
        this.poPopupExitConfirm = poPopupExitConfirm;
    }

    public RichPopup getPoPopupExitConfirm() {
        return poPopupExitConfirm;
    }

    public LoginBean() {
    }

    public String clearAction() {
        getPoUserName().setValue(null);
        getPoPassword().setValue(null);
        return null;
    }

    public void setPoUserName(RichInputText poUserName) {
        this.poUserName = poUserName;
    }

    public RichInputText getPoUserName() {
        return poUserName;
    }

    public void setPoPassword(RichInputText poPassword) {
        this.poPassword = poPassword;
    }

    public RichInputText getPoPassword() {
        return poPassword;
    }

    public String loginAction() {
        FacesContext        loContext = FacesContext.getCurrentInstance();
        ExternalContext     loEctx = loContext.getExternalContext();        
        HttpServletRequest  loRequest = (HttpServletRequest)loEctx.getRequest();
        HttpSession         loSession = loRequest.getSession(true);
        
        String lsUserName = 
            getPoUserName().getValue() == null ? "" : 
            getPoUserName().getValue().toString();
        String lsPassword = 
            getPoPassword().getValue() == null ? "" : 
            getPoPassword().getValue().toString();
        if(!lsUserName.equalsIgnoreCase("") || !lsPassword.equalsIgnoreCase("")){
            //Validar en base de Datos
            OlsCatUsersDao loOlsCatUsersDao = new OlsCatUsersDao();
            //Integer liFlagExist = loOlsCatUsersDao.getUserExistValidate(lsUserName, lsPassword);
            Integer liFlagExist = 1;
            if(liFlagExist > 0){
            //loSession.setAttribute("session.logged", "true");
                loSession.setAttribute("session.ols", "true");
                
                OlsCatUsersTab loOlsCatUsersTab = loOlsCatUsersDao.getOlsCatUsersTabByUserName(lsUserName);
                UserInfoBean   loUserInfo = (UserInfoBean) new UtilFaces().resolveExpression("#{UserInfoBean}");    
                
                DateFormat          ldDateFormat = 
                    new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date                ldDate = new Date();                    
                loUserInfo.setPsUserFullName(loOlsCatUsersTab.getLsNomUser() + " " +
                                             loOlsCatUsersTab.getLsIndLastName() +  " " +
                                             loOlsCatUsersTab.getLsIndSecondName());
                loUserInfo.setPsEmail(loOlsCatUsersTab.getLsIndMailUser());
                loUserInfo.setPsDateTimeLogin(ldDateFormat.format(ldDate));
                loUserInfo.setPsIdUser(String.valueOf(loOlsCatUsersTab.getLiIdUser()));
                loUserInfo.setPsUserName(loOlsCatUsersTab.getLsIdUserName());
                loSession.setAttribute("loggedOlsUser", loUserInfo.getPsUserName());                             
                loSession.setAttribute("loggedOlsIdUser", loUserInfo.getPsIdUser()); 
                String              lsUrl = 
                    loEctx.getRequestContextPath() + "/faces/homePage";
                try {
                    loEctx.redirect(lsUrl);
                } catch (IOException e) {
                    System.out.println("Error al ingresar");
                }
            }else{
                FacesMessage loMsg = 
                    new FacesMessage("Firma No Permitida para el Usuario " + lsUserName);
                loMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, loMsg);
            }
        }else{
            FacesMessage loMsg = 
                new FacesMessage("Los Campos son Requeridos");
            loMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, loMsg);
        }
        return null;
    }


    public String exitApplication() {
        ExternalContext     loEctx = 
            FacesContext.getCurrentInstance().getExternalContext();
        String              lsUrl = loEctx.getRequestContextPath() + "/faces/indexPage";
        HttpSession         loSession = (HttpSession) loEctx.getSession(false);
        HttpServletResponse loResponse = (HttpServletResponse) loEctx.getResponse();
        loSession.invalidate();
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

    public String cancelExitAppication() {
        new UtilFaces().hidePopup(getPoPopupExitConfirm());
        return null;
    }
}

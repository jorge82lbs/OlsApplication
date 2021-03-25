/**
* Project: Ols - Administración de Clientes
*
* File: UserInfoBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans.users;

/** Esta clase es un bean para almacenar los datos informativos del usuario en 
  * sesion <br/><br/>  
  * 
  * @author Jorge Luis Bautista
  * 
  * @version 01.00.01
  * 
  * @date Septiembre 14, 2018, 12:00 pm
  */
public class UserInfoBean {
    private String psIdUser;
    private String psUserName;
    private String psUserFirstName;
    private String psLastName;
    private String psLastSecondName;
    private String psUserFullName;
    private String psIsGroup;
    private String psIsInternal;
    private String psUserVisible;
    private String psDateTimeLogin;
    private String psLogged;
    private String psMenu;
    private String psComponents;
    private String psRol;
    private String psFlagRootPortal;
    private String psEmail;

    public void setPsIdUser(String psIdUser) {
        this.psIdUser = psIdUser;
    }

    public String getPsIdUser() {
        return psIdUser;
    }

    public void setPsUserName(String psUserName) {
        this.psUserName = psUserName;
    }

    public String getPsUserName() {
        return psUserName;
    }

    public void setPsUserFirstName(String psUserFirstName) {
        this.psUserFirstName = psUserFirstName;
    }

    public String getPsUserFirstName() {
        return psUserFirstName;
    }

    public void setPsLastName(String psLastName) {
        this.psLastName = psLastName;
    }

    public String getPsLastName() {
        return psLastName;
    }

    public void setPsLastSecondName(String psLastSecondName) {
        this.psLastSecondName = psLastSecondName;
    }

    public String getPsLastSecondName() {
        return psLastSecondName;
    }

    public void setPsUserFullName(String psUserFullName) {
        this.psUserFullName = psUserFullName;
    }

    public String getPsUserFullName() {
        return psUserFullName;
    }

    public void setPsIsGroup(String psIsGroup) {
        this.psIsGroup = psIsGroup;
    }

    public String getPsIsGroup() {
        return psIsGroup;
    }

    public void setPsIsInternal(String psIsInternal) {
        this.psIsInternal = psIsInternal;
    }

    public String getPsIsInternal() {
        return psIsInternal;
    }

    public void setPsUserVisible(String psUserVisible) {
        this.psUserVisible = psUserVisible;
    }

    public String getPsUserVisible() {
        return psUserVisible;
    }

    public void setPsDateTimeLogin(String psDateTimeLogin) {
        this.psDateTimeLogin = psDateTimeLogin;
    }

    public String getPsDateTimeLogin() {
        return psDateTimeLogin;
    }

    public void setPsLogged(String psLogged) {
        this.psLogged = psLogged;
    }

    public String getPsLogged() {
        return psLogged;
    }

    public void setPsMenu(String psMenu) {
        this.psMenu = psMenu;
    }

    public String getPsMenu() {
        return psMenu;
    }

    public void setPsComponents(String psComponents) {
        this.psComponents = psComponents;
    }

    public String getPsComponents() {
        return psComponents;
    }

    public void setPsRol(String psRol) {
        this.psRol = psRol;
    }

    public String getPsRol() {
        return psRol;
    }

    public void setPsFlagRootPortal(String psFlagRootPortal) {
        this.psFlagRootPortal = psFlagRootPortal;
    }

    public String getPsFlagRootPortal() {
        return psFlagRootPortal;
    }

    public void setPsEmail(String psEmail) {
        this.psEmail = psEmail;
    }

    public String getPsEmail() {
        return psEmail;
    }

    public void setPsToken(String psToken) {
        this.psToken = psToken;
    }

    public String getPsToken() {
        return psToken;
    }
    private String psToken;
}

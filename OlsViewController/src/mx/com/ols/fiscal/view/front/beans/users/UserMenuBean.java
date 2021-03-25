/**
* Project: Ols - Administración de Clientes
*
* File: UserInfoBean.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLSB - 2018
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
public class UserMenuBean {
    public void setLsRolAdministrador(String lsRolAdministrador) {
        this.lsRolAdministrador = lsRolAdministrador;
    }

    public String getLsRolAdministrador() {
        return lsRolAdministrador;
    }

    public void setLsRolUsuario(String lsRolUsuario) {
        this.lsRolUsuario = lsRolUsuario;
    }

    public String getLsRolUsuario() {
        return lsRolUsuario;
    }
    private String lsRolAdministrador;
    private String lsRolUsuario;
}

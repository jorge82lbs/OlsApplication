/**
* Project: Ols - Administración de Clientes
*
* File: ConnectionDataBase.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans.model.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;

import javax.sql.DataSource;


/** Crea conexion a una base de datos<br/><br/>
 *
 * @author Jorge Luis Bautista
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class ConnectionDataBase {
    private String gsDataSource = "java:comp/env/jdbc/legalSolutionsDS";
    public Connection getConnection(){
        Connection                          loCnn = null;
        InitialContext                      loInitialContext;
        DataSource                          loDs;     
        try{
            loInitialContext = new InitialContext();
            loDs =
                (DataSource)loInitialContext.lookup(gsDataSource);
            loCnn = loDs.getConnection();
        }catch(SQLException loExSql){
            loExSql.printStackTrace();
            loCnn = null;
        }catch(Exception loEx){
            loEx.printStackTrace();
            loCnn = null;
        }    
        return loCnn;
    }
}

/**
* Project: OLS - Administración de Clientes
*
* File: OlsCatUsersDao.java
*
* Created on:  Septiembre 6, 2018 at 11:00
*
* Copyright (c) - JLBS - 2018
*/
package mx.com.ols.fiscal.view.front.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatUsersTab;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;
/** Esta clase ofrece metodos de conexion a bd para gestion de Usuarios<br/>
 *
 * @author Jorge Luis Bautista - Omw
 *
 * @version 01.00.01
 *
 * @date Septiembre 14, 2018, 12:00 pm
 */
public class OlsCatUsersDao {
    public Integer getUserExistValidate(String psUserName, String psPassword){
        Integer    liReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;        
        UtilsOls loUtilsOls = new UtilsOls();
        String lsPassword;
        try {
            lsPassword = loUtilsOls.encryptObject(psPassword, loUtilsOls.getKeyCoder());
            String     lsQuery = getQueryUserExist(psUserName, lsPassword);
            try {
                Statement loStmt = loCnn.createStatement();
                loRs = loStmt.executeQuery(lsQuery);  
                while(loRs.next()){
                    liReturn = loRs.getInt(1);
                }
            } catch (SQLException loExSql) {
                loExSql.printStackTrace();
            }        
            finally{
                try {
                    loCnn.close();
                    loRs.close();
                } catch (SQLException loEx) {
                    loEx.printStackTrace();
                }
            }
        } catch (Exception e) {
            liReturn = 0;
        }
        
        return liReturn;
    }
    
    String getQueryUserExist(String psUserName, String psPassword){
        String lsQuery = 
            "SELECT COUNT(1)\n" + 
            "  FROM OLS.OLS_CAT_USERS_TAB\n" + 
            " WHERE ID_USER_NAME = '" + psUserName + "' \n" + 
            "   AND IND_PASSWORD = '" + psPassword + "'";
        
        return lsQuery;
    }
    
    public OlsCatUsersTab getOlsCatUsersTabByUserName(String tsUserName){
        OlsCatUsersTab                loOlsCatUsersTab = new OlsCatUsersTab();        
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;
        String lsQueryParadigm = getQueryOlsCatUsersTabByUserName(tsUserName);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                loOlsCatUsersTab.setLiIdUser(loRs.getInt("ID_USER"));                    
                loOlsCatUsersTab.setLsNomUser(loRs.getString("NOM_USER"));
                loOlsCatUsersTab.setLsIndLastName(loRs.getString("IND_LAST_NAME"));
                loOlsCatUsersTab.setLsIndSecondName(loRs.getString("IND_SECOND_NAME"));
                loOlsCatUsersTab.setLsIdUserName(loRs.getString("ID_USER_NAME"));
                loOlsCatUsersTab.setLsIndPassword(loRs.getString("IND_PASSWORD"));
                loOlsCatUsersTab.setLsDesUser(loRs.getString("DES_USER"));
                loOlsCatUsersTab.setLsIndMailUser(loRs.getString("IND_MAIL_USER"));
                loOlsCatUsersTab.setLsIndPhoneNumber(loRs.getString("IND_PHONE_NUMBER"));
                loOlsCatUsersTab.setLsIndReqSecret(loRs.getString("IND_REQ_SECRET"));
                loOlsCatUsersTab.setLsIndResSecret(loRs.getString("IND_RES_SECRET"));
                loOlsCatUsersTab.setLsIndEstatus(loRs.getString("IND_ESTATUS"));
            }
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQueryParadigm);
            loExSql.printStackTrace();
        }
        finally{
            try {
                loCnn.close();
                loRs.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }       
        return loOlsCatUsersTab;
    }
    String getQueryOlsCatUsersTabByUserName(String tsUserName){
        String lsQuery = 
            " SELECT ID_USER,\n" + 
            "        NOM_USER,\n" + 
            "        IND_LAST_NAME,\n" + 
            "        IND_SECOND_NAME,\n" + 
            "        ID_USER_NAME,\n" + 
            "        IND_PASSWORD,\n" + 
            "        DES_USER,\n" + 
            "        IND_MAIL_USER,\n" + 
            "        IND_PHONE_NUMBER,\n" + 
            "        IND_REQ_SECRET,\n" + 
            "        IND_RES_SECRET,\n" + 
            "        IND_ESTATUS,\n" + 
            "        ATTRIBUTE_CATEGORY,\n" + 
            "        ATTRIBUTE1,\n" + 
            "        ATTRIBUTE2,\n" + 
            "        ATTRIBUTE3,\n" + 
            "        ATTRIBUTE4,\n" + 
            "        ATTRIBUTE5,\n" + 
            "        ATTRIBUTE6,\n" + 
            "        ATTRIBUTE7,\n" + 
            "        ATTRIBUTE8,\n" + 
            "        ATTRIBUTE9,\n" + 
            "        ATTRIBUTE10,\n" + 
            "        ATTRIBUTE11,\n" + 
            "        ATTRIBUTE12,\n" + 
            "        ATTRIBUTE13,\n" + 
            "        ATTRIBUTE14,\n" + 
            "        ATTRIBUTE15,\n" + 
            "        FEC_CREATION_DATE,\n" + 
            "        NUM_CREATED_BY,\n" + 
            "        FEC_LAST_UPDATE_DATE,\n" + 
            "        NUM_LAST_UPDATED_BY,\n" + 
            "        NUM_LAST_UPDATE_LOGIN \n" + 
            "   FROM OLS.OLS_CAT_USERS_TAB\n" + 
            "  WHERE ID_USER_NAME = '" + tsUserName + "'";
        
        return lsQuery;
    }
    
    
}

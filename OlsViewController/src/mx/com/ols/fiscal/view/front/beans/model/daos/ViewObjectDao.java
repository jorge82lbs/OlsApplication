package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import mx.com.ols.fiscal.model.types.ResponseUpdDao;
import mx.com.ols.fiscal.view.front.beans.mail.MailHeaderBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatConfigParamTab;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.OlsCatSatMailsCtesBean;
import mx.com.ols.fiscal.view.front.beans.types.OlsClientUploadErrBean;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatDefinitiveBean;

public class ViewObjectDao {
    public Integer getMaxIdTable(String tsTable) {
        Integer    liReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsTable = "";
        String     lsField = "";
        if(tsTable.equalsIgnoreCase("Parameters")){
            lsTable = "OLS.OLS_CAT_CONFIG_PARAM_TAB";
            lsField = "ID_PARAMETER";
        }       
        if(tsTable.equalsIgnoreCase("ClientsSat")){
            lsTable = "OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB";
            lsField = "ID_CLIENT_SAT";
        }  
        if(tsTable.equalsIgnoreCase("ProviderSat")){
            lsTable = "OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB";
            lsField = "ID_PROVIDER";
        } 
        String lsQuery = getQueryMax(lsTable,lsField);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                liReturn = loRs.getInt(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("error al ejecutar:"+loExSql.getMessage());
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
        return liReturn;
    }
    
    public String getQueryMax(String tsTable, String tsField) {
        String lsQuery = 
        "SELECT NVL(MAX(" + tsField + "),0) \n" + 
        "  FROM " + tsTable;
        return lsQuery;
    }
    
    public Integer getIdRequestSq() {
        Integer    liReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String lsQuery = "SELECT OLS.OLS_REQUEST_ID_SQ.NEXTVAL FROM DUAL";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                liReturn = loRs.getInt(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("error al ejecutar:"+loExSql.getMessage());
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
        return liReturn;
    }
    
    public Integer getIdNoLocSq() {
        Integer    liReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String lsQuery = "SELECT OLS.OLS_UNFULFILLED_ID_SQ.NEXTVAL FROM DUAL";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                liReturn = loRs.getInt(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("error al ejecutar:"+loExSql.getMessage());
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
        return liReturn;
    }
    
    public String getQueryInsertUploadErrClients(OlsClientUploadErrBean toOlsClientUploadErrBean){
        String lsQuery = 
            "INSERT INTO OLS.OLS_TRX_SAT_UPLOAD_ERR_TAB (ID_UPLOAD_ERR,\n" + 
            "                                            ID_REQUEST,\n" + 
            "                                            ID_ROW_EXCEL,\n" + 
            "                                            IND_RFC,\n" + 
            "                                            IND_TYPE_CLIENT,\n" + 
            "                                            IND_DES_ERROR,\n" + 
            "                                            IND_FILE_NAME,\n" + 
            "                                            IND_ESTATUS, \n" + 
            "                                            FEC_CREATION_DATE, \n" + 
            "                                            NUM_CREATED_BY,  \n" + 
            "                                            FEC_LAST_UPDATE_DATE,  \n" + 
            "                                            NUM_LAST_UPDATED_BY \n" + 
            "                                           )\n" + 
            "                                    VALUES (OLS.OLS_UPLOAD_ERR_ID_SQ.NEXTVAL,\n" + 
            "                                            " + toOlsClientUploadErrBean.getLiIdRequest() + ",\n" + 
            "                                            " + toOlsClientUploadErrBean.getLiIdRowExcel() + ",\n" + 
            "                                            '" + toOlsClientUploadErrBean.getLsIndRfc() + "',\n" + 
            "                                            '" + toOlsClientUploadErrBean.getLsIndTypeClient() + "',\n" + 
            "                                            '" + toOlsClientUploadErrBean.getLsIndDesError() + "',\n" + 
            "                                            '" + toOlsClientUploadErrBean.getLsIndFileName() + "',\n" + 
            "                                            '" + toOlsClientUploadErrBean.getLsIndEstatus() + "',\n" + 
            "                                            SYSDATE,\n" + 
            "                                            " + toOlsClientUploadErrBean.getLiIdNumCreatedBy() + ",\n" + 
            "                                            SYSDATE,\n" + 
            "                                            " + toOlsClientUploadErrBean.getLiIdNumLastUpdatedBy() + "                                           \n" + 
            "                                           )";
        
        return lsQuery;
    }
    
    public ResponseBean insertUploadErrClients(OlsClientUploadErrBean toOlsClientUploadErrBean) {
        ResponseBean    loResponse = new ResponseBean();
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsQueryParadigm = getQueryInsertUploadErrClients(toOlsClientUploadErrBean);
        try {
            Statement loStmt = loCnn.createStatement();
            int liAffected = loStmt.executeUpdate(lsQueryParadigm);
            loResponse.setLsResponse("OK");
            loResponse.setLsType("10");
            loResponse.setLsMessageResponse("Rows Affected ["+liAffected+"]");
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQueryParadigm);
            loResponse.setLsResponse("ERROR");
            loResponse.setLsType("100");
            loResponse.setLsMessageResponse(loExSql.getMessage());
            loExSql.printStackTrace();
        }
        finally{
            try {
                loCnn.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }        
        return loResponse;
    }
    
    public List<OlsCatConfigParamTab> getGeneralParametersByUsedBy(String tsUsedBy){
        List<OlsCatConfigParamTab> listParamters = new ArrayList<OlsCatConfigParamTab>();
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;   
        String                        lsQueryParadigm = getQueryGeneralParametersByUsedBy(tsUsedBy);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                OlsCatConfigParamTab loOlsCatConfigParamTab = new OlsCatConfigParamTab();             
                loOlsCatConfigParamTab.setLiIdParameter(loRs.getInt("ID_PARAMETER"));
                loOlsCatConfigParamTab.setLsIndDescParameter(loRs.getString("IND_DESC_PARAMETER") == null ? null : 
                                          loRs.getString("IND_DESC_PARAMETER").trim());                
                loOlsCatConfigParamTab.setLsIndEstatus(loRs.getString("IND_ESTATUS") == null ? null : 
                                          loRs.getString("IND_ESTATUS").trim()); 
                loOlsCatConfigParamTab.setLsIndUsedBy(loRs.getString("IND_USED_BY") == null ? null : 
                                          loRs.getString("IND_USED_BY").trim()); 
                loOlsCatConfigParamTab.setLsIndValueParameter(loRs.getString("IND_VALUE_PARAMETER") == null ? null : 
                                          loRs.getString("IND_VALUE_PARAMETER").trim()); 
                loOlsCatConfigParamTab.setLsNomParameter(loRs.getString("NOM_PARAMETER") == null ? null : 
                                          loRs.getString("NOM_PARAMETER").trim()); 
                listParamters.add(loOlsCatConfigParamTab);
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
        return listParamters;
    }
    
    public String getQueryGeneralParametersByUsedBy(String tsUsedBy){
        String lsQuery = "SELECT ID_PARAMETER,\n" + 
        "       NOM_PARAMETER,\n" + 
        "       IND_DESC_PARAMETER,\n" + 
        "       IND_USED_BY,\n" + 
        "       IND_VALUE_PARAMETER,\n" + 
        "       IND_ESTATUS\n" + 
        "  FROM OLS.OLS_CAT_CONFIG_PARAM_TAB\n" + 
        " WHERE IND_USED_BY = '" + tsUsedBy  + "'\n";
        
        return lsQuery;
    }
    
    public List<OlsCatConfigParamTab> getGeneralParametersByNom(String tsNomParameter){
        List<OlsCatConfigParamTab> listParamters = new ArrayList<OlsCatConfigParamTab>();
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;
        String                        lsQueryParadigm = getQueryGeneralParametersByNom(tsNomParameter);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                OlsCatConfigParamTab loOlsCatConfigParamTab = new OlsCatConfigParamTab();             
                loOlsCatConfigParamTab.setLiIdParameter(loRs.getInt("ID_PARAMETER"));
                loOlsCatConfigParamTab.setLsIndDescParameter(loRs.getString("IND_DESC_PARAMETER") == null ? null : 
                                          loRs.getString("IND_DESC_PARAMETER").trim());                
                loOlsCatConfigParamTab.setLsIndEstatus(loRs.getString("IND_ESTATUS") == null ? null : 
                                          loRs.getString("IND_ESTATUS").trim()); 
                loOlsCatConfigParamTab.setLsIndUsedBy(loRs.getString("IND_USED_BY") == null ? null : 
                                          loRs.getString("IND_USED_BY").trim()); 
                loOlsCatConfigParamTab.setLsIndValueParameter(loRs.getString("IND_VALUE_PARAMETER") == null ? null : 
                                          loRs.getString("IND_VALUE_PARAMETER").trim()); 
                loOlsCatConfigParamTab.setLsNomParameter(loRs.getString("NOM_PARAMETER") == null ? null : 
                                          loRs.getString("NOM_PARAMETER").trim()); 
                listParamters.add(loOlsCatConfigParamTab);
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
        return listParamters;
    }
    
    public String getQueryGeneralParametersByNom(String tsNomParameter){
        String lsQuery = "SELECT ID_PARAMETER,\n" + 
        "       NOM_PARAMETER,\n" + 
        "       IND_DESC_PARAMETER,\n" + 
        "       IND_USED_BY,\n" + 
        "       IND_VALUE_PARAMETER,\n" + 
        "       IND_ESTATUS\n" + 
        "  FROM OLS.OLS_CAT_CONFIG_PARAM_TAB\n" + 
        " WHERE NOM_PARAMETER = '" + tsNomParameter  + "'\n";
        
        return lsQuery;
    }
    
    public Integer validateHdrEmailConfig() { //Deben ser 4
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        Integer                 liResponse = 0;
        String                  lsQueryParadigm = 
            "SELECT COUNT(1)\n" + 
        "  FROM OLS.OLS_CAT_CONFIG_PARAM_TAB\n" + 
        " WHERE IND_USED_BY = 'SIMPLE_MAIL_HDR'";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                liResponse = loRs.getInt(1); 
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
        return liResponse;
    }
    
    public Integer validateEmailDestiny(String lsRFC) { //Deben ser mininmo 1
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        Integer                 liResponse = 0;
        String                  lsQuery = 
            "SELECT COUNT(1)\n" + 
            "  FROM OLS.OLS_CAT_SAT_MAILS_CTES_TAB \n" + 
            " WHERE IND_RFC = '"+lsRFC+"'";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                liResponse = loRs.getInt(1); 
            }           
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQuery);
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
        return liResponse;
    }
    
    public List<OlsCatSatMailsCtesBean> getEmailDestinyByRfc(String tsRfc){
        List<OlsCatSatMailsCtesBean> loReturn = new ArrayList<OlsCatSatMailsCtesBean>();
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;   
        String                        lsQueryParadigm = getQueryEmailDestiny(tsRfc);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                OlsCatSatMailsCtesBean loOlsCatMailsCtesTab = new OlsCatSatMailsCtesBean();             
                loOlsCatMailsCtesTab.setIdMail(loRs.getInt("ID_MAIL"));
                loOlsCatMailsCtesTab.setIdClientSat(loRs.getInt("ID_CLIENT_SAT"));                
                loOlsCatMailsCtesTab.setIndAddress(loRs.getString("IND_ADDRESS") == null ? null : 
                                          loRs.getString("IND_ADDRESS").trim()); 
                loOlsCatMailsCtesTab.setIndEmail(loRs.getString("IND_EMAIL") == null ? null : 
                                          loRs.getString("IND_EMAIL").trim()); 
                loOlsCatMailsCtesTab.setIndEstatus(loRs.getString("IND_ESTATUS") == null ? null : 
                                          loRs.getString("IND_ESTATUS").trim()); 
                loOlsCatMailsCtesTab.setIndRfc(loRs.getString("IND_RFC") == null ? null : 
                                          loRs.getString("IND_RFC").trim()); 
                loOlsCatMailsCtesTab.setIndRfcType(loRs.getString("IND_RFC_TYPE") == null ? null : 
                                          loRs.getString("IND_RFC_TYPE").trim()); 
                loReturn.add(loOlsCatMailsCtesTab);
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
        return loReturn;
    }
    
    public String getQueryEmailDestiny(String tsRfc){
        String lsQuery = "SELECT ID_MAIL,\n" + 
        "          ID_CLIENT_SAT,\n" + 
        "          IND_RFC,\n" + 
        "          IND_ADDRESS,\n" + 
        "          IND_EMAIL,\n" + 
        "          IND_RFC_TYPE,\n" + 
        "          IND_ESTATUS \n" + 
        "     FROM OLS.OLS_CAT_SAT_MAILS_CTES_TAB \n" + 
        "    WHERE IND_RFC = '"+tsRfc+"'";
        
        return lsQuery;
    }
    
    public Integer getNumProvsByClient(String tsClient) {
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        Integer                 liResponse = 0;
        String                  lsQueryParadigm = 
            "SELECT count(1)\n" + 
            "  FROM OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB \n" + 
            " WHERE IND_RFC_CLIENT = '"+tsClient+"'";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                liResponse = loRs.getInt(1); 
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
        return liResponse;
    }
    
    public ResponseUpdDao deleteDepreacteTable(String tsType) {
        ResponseUpdDao    loReturn = new ResponseUpdDao(); 
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsTable = "";
        if(tsType.equalsIgnoreCase("Incumplidos")){
            lsTable = "OLS.OLS_CAT_SAT_UNFULFILLED_TAB";
        }  
        if(tsType.equalsIgnoreCase("Presuntos")){
            lsTable = "OLS.OLS_CAT_SAT_ALLEGED_TAB";
        }  
        if(tsType.equalsIgnoreCase("Definitivos")){
            lsTable = "OLS.OLS_CAT_SAT_DEFINITIVE_TAB";
        }  
        String lsQuery = getQueryDelete(lsTable);
        try {
            Statement loStmt = loCnn.createStatement();
            int liAffected = loStmt.executeUpdate(lsQuery); 
            loReturn.setLiAffected(liAffected);
            loReturn.setLsResponse("OK");
            loReturn.setLsMessage("OK");
            System.out.println("Registros eliminados satisfactoriamente de la tabla: "+lsTable);
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");            
            loReturn.setLiAffected(0);
            loReturn.setLsResponse("ERROR");
            loReturn.setLsMessage(loExSql.getMessage());
            loExSql.printStackTrace();
        }
        finally{
            try {
                loCnn.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }        
        return loReturn;
    }
    
    public String getQueryDelete(String tsTable) {
        String lsQuery = 
            "DELETE FROM "+tsTable;
        return lsQuery;
    }
    
    public String getLastModifyNoLocalizados() {
        String     lsReturn = ""; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsQuery = 
            "SELECT MAX(FEC_CREATION_DATE) LAST_MODIFY\n" + 
            "  FROM OLS.OLS_CAT_SAT_UNFULFILLED_TAB\n" + 
            " WHERE ID_REQUEST = (SELECT MAX(ID_REQUEST)\n" + 
            "                       FROM OLS.OLS_CAT_SAT_UNFULFILLED_TAB\n" + 
            "                    )";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                lsReturn = loRs.getString(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("error al ejecutar:"+loExSql.getMessage());
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
        return lsReturn;
    }
    
    public Integer getNumMailsConfigurated(String lsRfc) {
        Integer     lsReturn = 0; 
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsQuery = 
            "SELECT COUNT(1) CNT_MAILS\n" + 
            "  FROM OLS.OLS_CAT_SAT_MAILS_CTES_TAB\n" + 
            " WHERE IND_RFC = '" + lsRfc + "'";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                lsReturn = loRs.getInt(1);
            }
        } catch (SQLException loExSql) {
            System.out.println("error al ejecutar:"+loExSql.getMessage());
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
        return lsReturn;
    }
    
    public ResponseBean deleteAllFromTable(String tsTable) {
        ResponseBean    loResponse = new ResponseBean();
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsQueryParadigm = 
            "DELETE\n" + 
            "  FROM "+tsTable;
        try {
            Statement loStmt = loCnn.createStatement();
            int liAffected = loStmt.executeUpdate(lsQueryParadigm);
            loResponse.setLsResponse("OK");
            loResponse.setLsType("10");
            loResponse.setLsMessageResponse("Rows Affected ["+liAffected+"]");
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQueryParadigm);
            loResponse.setLsResponse("ERROR");
            loResponse.setLsType("100");
            loResponse.setLsMessageResponse(loExSql.getMessage());
            loExSql.printStackTrace();
        }
        finally{
            try {
                loCnn.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }        
        return loResponse;
    }
    
}

package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxProvidersBean;

public class PhmTrxProvidersSatDao {
    public String getQueryInsert(PhmTrxProvidersBean toPhmTrxProvidersBean){
        String lsQuery = "INSERT INTO OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB (ID_PROVIDER,  \n" + 
        "                                         ID_CLIENT_SAT,\n" + 
        "                                         ID_REQUEST,\n" + 
        "                                         IND_YEAR,\n" + 
        "                                         IND_MONTH,\n" + 
        "                                         ID_LAW_FIRM,\n" + 
        "                                         IND_RFC_CLIENT,\n" + 
        "                                         IND_RFC,\n" + 
        "                                         IND_COMPANY,\n" + 
        "                                         IND_EMAIL,\n" + 
        "                                         IND_ESTATUS,\n" + 
        "                                         FEC_CREATION_DATE,\n" + 
        "                                         NUM_CREATED_BY,\n" + 
        "                                         FEC_LAST_UPDATE_DATE,\n" + 
        "                                         NUM_LAST_UPDATED_BY\n" + 
        "                                        )\n" + 
        "                                 VALUES ("+toPhmTrxProvidersBean.getLiIdProvider()+",\n" + 
        "                                         "+toPhmTrxProvidersBean.getLiIdClientSat()+",\n" + 
        "                                         "+toPhmTrxProvidersBean.getLiRequest()+",\n" + 
        "                                         "+toPhmTrxProvidersBean.getLiIndYear()+",\n" + 
        "                                         "+toPhmTrxProvidersBean.getLsIndMonth()+",\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLiIdLawFirm()+"',\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLsIndRfcClient()+"',\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLsIndRfc()+"',\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLsIndCompany()+"',\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLsIndMail()+"',\n" + 
        "                                         '"+toPhmTrxProvidersBean.getLsIndEstatus()+"',\n" + 
        "                                         SYSDATE,\n" + 
        "                                         "+toPhmTrxProvidersBean.getLiCreatedBy()+",\n" + 
        "                                         SYSDATE,\n" + 
        "                                         "+toPhmTrxProvidersBean.getLiUpdateBy()+"\n" + 
        "                                        )";
        
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxClientsSat(PhmTrxProvidersBean toPhmTrxProvidersSatBean) {
        ResponseBean    loResponse = new ResponseBean();
        Integer liFlag = 
            validateIfExist(toPhmTrxProvidersSatBean.getLsIndRfc(), 
                            toPhmTrxProvidersSatBean.getLsIndRfcClient());
        if(liFlag == 0){
            Connection loCnn = new ConnectionDataBase().getConnection();
            String     lsQueryParadigm = getQueryInsert(toPhmTrxProvidersSatBean);
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
        }else{
            //System.out.println("El proveedor ya existe RFC_CLIENT["+toPhmTrxProvidersSatBean.getLsIndRfcClient()+
            //                   "] - RFC_PROVEEDOR["+toPhmTrxProvidersSatBean.getLsIndRfc()+"]");
            loResponse.setLsResponse("ERROR");
            loResponse.setLsType("101");
            loResponse.setLsMessageResponse("RFC ya Existe");
        }
        return loResponse;
    }
    
    public Integer validateIfExist(String tsRfc, String tsRfcClient) {
        Integer    loFlag = 0;
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsQueryParadigm = getQueryValidateIfExist(tsRfc, tsRfcClient);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                loFlag = loRs.getInt(1);
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
        return loFlag;
    }
    
    public String getQueryValidateIfExist(String tsRfc, String tsRfcClient){
        String lsQuery = 
        "SELECT COUNT(1) \n" + 
        "  FROM OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB\n" + 
        " WHERE IND_RFC = '" + tsRfc + "' \n"+
        "   AND IND_RFC_CLIENT = '"+tsRfcClient+"'";
        return lsQuery;
    }
    
    public Integer getIdClientByRfc(String tsRfc) {
        Integer    loReturn = 0;
        Integer liFlag = 
            validateIfExistClient(tsRfc);
        if(liFlag > 0){
            Connection loCnn = new ConnectionDataBase().getConnection();
            ResultSet  loRs = null;
            String     lsQueryParadigm = getQueryIdClientByRfc(tsRfc);
            try {
                Statement loStmt = loCnn.createStatement();
                loRs = loStmt.executeQuery(lsQueryParadigm);  
                while(loRs.next()){
                    loReturn = loRs.getInt(1);
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
        }
        return loReturn;
    }
    
    public Integer validateIfExistClient(String tsRfc) {
        Integer    loFlag = 0;
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsQueryParadigm = getQueryValidateIfExistClient(tsRfc);
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                loFlag = loRs.getInt(1);
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
        return loFlag;
    }
    
    public String getQueryValidateIfExistClient(String tsRfc){
        String lsQuery = 
        "SELECT COUNT(1) \n" + 
        "  FROM OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB\n" + 
        " WHERE IND_RFC = '" + tsRfc + "'";
        return lsQuery;
    }
    
    public String getQueryIdClientByRfc(String tsRfc){
        String lsQuery = 
        "SELECT ID_CLIENT_SAT \n" + 
        "  FROM OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB\n" + 
        " WHERE IND_RFC = '" + tsRfc + "'";
        
        return lsQuery;
    }
    
    public ResponseBean deletePhmTrxProvidersSat(String tsRfcClient, String lsRfcProvider) {
        ResponseBean    loResponse = new ResponseBean();
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsQuery = 
            "DELETE\n" + 
            "  FROM OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB \n" + 
            " WHERE IND_RFC_CLIENT = '"+tsRfcClient+"'\n" + 
            "   AND IND_RFC        = '"+lsRfcProvider+"'";
        try {
            Statement loStmt = loCnn.createStatement();
            int liAffected = loStmt.executeUpdate(lsQuery);
            loResponse.setLsResponse("OK");
            loResponse.setLsType("10");
            loResponse.setLsMessageResponse("Rows Affected ["+liAffected+"]");
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQuery);
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

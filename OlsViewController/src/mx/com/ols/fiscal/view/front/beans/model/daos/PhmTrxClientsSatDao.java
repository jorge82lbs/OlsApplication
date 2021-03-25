package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mx.com.ols.fiscal.view.front.beans.model.beans.OlsTrxSatClientsReportBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxClientsSatBean;

public class PhmTrxClientsSatDao {
    public String getQueryInsert(PhmTrxClientsSatBean toPhmTrxClientsSatBean){
        String lsQuery = "INSERT INTO OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB (ID_CLIENT_SAT,  \n" + 
        "                                         ID_REQUEST,\n" + 
        "                                         IND_YEAR,\n" + 
        "                                         IND_MONTH,\n" + 
        "                                         ID_LAW_FIRM,\n" + 
        "                                         IND_RFC,\n" + 
        "                                         IND_COMPANY,\n" + 
        "                                         IND_ESTATUS,\n" + 
        "                                         FEC_CREATION_DATE,\n" + 
        "                                         NUM_CREATED_BY,\n" + 
        "                                         FEC_LAST_UPDATE_DATE,\n" + 
        "                                         NUM_LAST_UPDATED_BY\n" + 
        "                                        )\n" + 
        "                                 VALUES ("+toPhmTrxClientsSatBean.getLiIdClientSat()+",\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLiRequest()+",\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLiIndYear()+",\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLsIndMonth()+",\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLiIdLawFirm()+"',\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndRfc()+"',\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndCompany()+"',\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndEstatus()+"',\n" + 
        "                                         SYSDATE,\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLiCreatedBy()+",\n" + 
        "                                         SYSDATE,\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLiUpdateBy()+"\n" + 
        "                                        )";
        
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxClientsSat(PhmTrxClientsSatBean toPhmTrxClientsSatBean) {
        ResponseBean    loResponse = new ResponseBean();
        Integer liFlag = validateIfExist(toPhmTrxClientsSatBean.getLsIndRfc());
        if(liFlag == 0){
            Connection loCnn = new ConnectionDataBase().getConnection();
            String     lsQueryParadigm = getQueryInsert(toPhmTrxClientsSatBean);
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
            System.out.println("El cliente ya existe");
            loResponse.setLsResponse("ERROR");
            loResponse.setLsType("101");
            loResponse.setLsMessageResponse("RFC ya Existe");
        }
        return loResponse;
    }
    
    public Integer validateIfExist(String tsRfc) {
        Integer    loFlag = 0;
        Connection loCnn = new ConnectionDataBase().getConnection();
        ResultSet  loRs = null;
        String     lsQueryParadigm = getQueryValidateIfExist(tsRfc);
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
    
    public String getQueryValidateIfExist(String tsRfc){
        String lsQuery = 
        "SELECT COUNT(1) \n" + 
        "  FROM OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB\n" + 
        " WHERE IND_RFC = '" + tsRfc + "'";
        return lsQuery;
    }
    
    
    
    public String getQueryTrxClientsReportBlacklist(OlsTrxSatClientsReportBean toPhmTrxClientsSatBean){
        String lsQuery = "INSERT INTO OLS.OLS_TRX_SAT_CLIENTS_REPORT_TAB (ID_CLIENT_SAT,  \n" + 
        "                                         IND_RFC,\n" + 
        "                                         IND_COMPANY,\n" + 
                         
        "                                         IND_PROCESS,\n" + 
        "                                         IND_COMMENT,\n" + 
        "                                         NUM_SUPPLIERS,\n" + 
                         
        "                                         IND_ESTATUS,\n" + 
        "                                         FEC_CREATION_DATE,\n" + 
        "                                         NUM_CREATED_BY,\n" + 
        "                                         FEC_LAST_UPDATE_DATE,\n" + 
        "                                         NUM_LAST_UPDATED_BY\n" + 
        "                                        )\n" + 
        "                                 VALUES ("+toPhmTrxClientsSatBean.getLsIdClientSat()+",\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndRfc()+"',\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndCompany()+"',\n" + 
                         
        "                                         '"+toPhmTrxClientsSatBean.getLsIndProcess()+"',\n" + 
        "                                         '"+toPhmTrxClientsSatBean.getLsIndComment()+"',\n" + 
        "                                         "+toPhmTrxClientsSatBean.getLsNumSuppliers()+",\n" + 
                         
        "                                         '"+toPhmTrxClientsSatBean.getLsIndEstatus()+"',\n" + 
        "                                         SYSDATE,\n" + 
        "                                         1,\n" + 
        "                                         SYSDATE,\n" + 
        "                                         1\n" + 
        "                                        )";
        
        return lsQuery;
    }
    
    public ResponseBean insertTrxClientsReportBlacklist(OlsTrxSatClientsReportBean loBean) {
        ResponseBean    loResponse = new ResponseBean();
        
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsQueryParadigm = 
            getQueryTrxClientsReportBlacklist(loBean);
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
    
    public ResponseBean deleteClientsReportBlacklist() {
        ResponseBean    loResponse = new ResponseBean();
        
        Connection loCnn = new ConnectionDataBase().getConnection();
        String     lsQueryParadigm = 
            "DELETE " +
            "  FROM OLS.OLS_TRX_SAT_CLIENTS_REPORT_TAB ";
            
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

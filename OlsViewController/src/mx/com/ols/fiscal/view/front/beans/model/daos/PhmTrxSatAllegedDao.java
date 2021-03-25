package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatAllegedBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;

public class PhmTrxSatAllegedDao {
    
    public String getQueryInsert(PhmTrxSatAllegedBean toPhmTrxSatAllegedBean){
        String lsQuery = "INSERT INTO OLS.OLS_CAT_SAT_ALLEGED_TAB (ID_CLIENT_ALG,  \n" + 
        "                                         ID_REQUEST,\n" + 
        "                                         IND_YEAR,\n" + 
        "                                         IND_MONTH,\n" + 
        "                                         NUM_MONTH,\n";
        if(toPhmTrxSatAllegedBean.getLtFecPeriod() != null){
            lsQuery += "                          FEC_PERIOD,\n";       
        }        
        lsQuery += "                              NUM_NUMBER,\n" + 
        "                                         IND_RFC_ALG,\n" + 
        "                                         IND_COMPANY,\n" + 
        "                                         IND_SITUATION,\n" + 
        "                                         IND_TRADE_NUMBER,\n";
        if(toPhmTrxSatAllegedBean.getLsFecPublicationSat() != null){
            lsQuery += "                          FEC_PUBLICATION_SAT,\n";       
        }        
        lsQuery += 
        "                                         IND_TRADE_NUMBER2,\n";
        if(toPhmTrxSatAllegedBean.getLsFecPublicationDof() != null){
            lsQuery += "                          FEC_PUBLICATION_DOF,\n";       
        }        
        lsQuery += 
        "                                         IND_ESTATUS,\n" + 
        "                                         FEC_CREATION_DATE,\n" + 
        "                                         NUM_CREATED_BY, \n" + 
        "                                         FEC_LAST_UPDATE_DATE, \n" + 
        "                                         NUM_LAST_UPDATED_BY\n" + 
        "                                       )\n" + 
        "                               VALUES  (OLS.OLS_ALLEGED_ID_SQ.NEXTVAL,  \n" + 
        "                                        " + toPhmTrxSatAllegedBean.getLiIdRequest() + ",\n" + 
        "                                        " + toPhmTrxSatAllegedBean.getLiIndYear() + ",\n" + 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndMonth() + "',\n" + 
        "                                        " + toPhmTrxSatAllegedBean.getLiNumMonth() + ",\n";
        if(toPhmTrxSatAllegedBean.getLsFecPeriod() != null){
            lsQuery += "                         to_date('" + toPhmTrxSatAllegedBean.getLsFecPeriod() + "','YYYY-MM-DD'),\n";
        }
        lsQuery += "                             " + toPhmTrxSatAllegedBean.getLiNumNumber() + ",\n" + 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndRfcAlg() + "',\n" + 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndCompany() + "',\n" + 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndSituation() + "',\n" + 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndTradeNumber() + "',\n";
        if(toPhmTrxSatAllegedBean.getLsFecPublicationSat() != null){
            lsQuery += "                         to_date('" + toPhmTrxSatAllegedBean.getLsFecPublicationSat() + "','YYYY-MM-DD'),\n";
        }
        lsQuery += 
        "                                        '" + toPhmTrxSatAllegedBean.getLsIndTradeNumber2() + "',\n";
        if(toPhmTrxSatAllegedBean.getLsFecPublicationDof() != null){
            lsQuery += "                         to_date('" + toPhmTrxSatAllegedBean.getLsFecPublicationDof() + "','YYYY-MM-DD'),\n";
        }
        lsQuery += 
        "                                        'A',\n" + 
        "                                        SYSDATE,\n" + 
        "                                        " + toPhmTrxSatAllegedBean.getLiNumCreatedBy() + ", \n" + 
        "                                        SYSDATE, \n" + 
        "                                        " + toPhmTrxSatAllegedBean.getLiNumLastUpdatedBy() + "\n" + 
        "                                       )";
        
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxSatAlleged(PhmTrxSatAllegedBean toPhmTrxSatAllegedBean) {
        ResponseBean    loResponse = new ResponseBean();
        //Integer liFlag = validateIfExist(toPhmTrxSatAllegedBean.getLsIndRfcAlg());
        //if(liFlag == 0){
        if(true){
            Connection loCnn = new ConnectionDataBase().getConnection();
            String     lsQueryParadigm = getQueryInsert(toPhmTrxSatAllegedBean);
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
        "  FROM OLS.OLS_CAT_SAT_ALLEGED_TAB\n" + 
        " WHERE IND_RFC_ALG = '" + tsRfc + "'";
        return lsQuery;
    }
    
    public List<PhmTrxSatAllegedBean> getAllPresuntosByRfc(String tsRfc){
        List<PhmTrxSatAllegedBean> laRes = new ArrayList<PhmTrxSatAllegedBean>();
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        String                  lsQueryParadigm = getQueryAllPresuntosByRfc(tsRfc);        
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                PhmTrxSatAllegedBean loItem = new PhmTrxSatAllegedBean();
                loItem.setLiIdClientAlg(loRs.getInt("ID_CLIENT_ALG"));
                loItem.setLiIdRequest(loRs.getInt("ID_REQUEST"));
                loItem.setLiIndYear(loRs.getInt("IND_YEAR"));
                loItem.setLsIndMonth(loRs.getString("IND_MONTH"));
                loItem.setLiNumNumber(loRs.getInt("NUM_NUMBER"));
                loItem.setLiNumMonth(loRs.getInt("NUM_MONTH"));
                loItem.setLsIndRfcAlg(loRs.getString("IND_RFC_ALG"));
                loItem.setLsIndCompany(loRs.getString("IND_COMPANY"));
                loItem.setLsIndSituation(loRs.getString("IND_SITUATION"));
                loItem.setLsIndTradeNumber(loRs.getString("IND_TRADE_NUMBER"));
                loItem.setLsFecPublicationSat(loRs.getString("FEC_PUBLICATION_SAT"));
                loItem.setLsIndTradeNumber2(loRs.getString("IND_TRADE_NUMBER2"));
                loItem.setLsIndEstatus(loRs.getString("IND_ESTATUS"));
                laRes.add(loItem);
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
        return laRes;
    }
    
    public String getQueryAllPresuntosByRfc(String tsRfc){
        String lsQuery = 
            " SELECT ID_CLIENT_ALG,\n" + 
            "        ID_REQUEST,\n" + 
            "        IND_YEAR,\n" + 
            "        IND_MONTH,\n" + 
            "        NUM_NUMBER,\n" + 
            "        NUM_MONTH,\n" + 
            "        FEC_PERIOD,\n" + 
            "        IND_PERIOD,\n" + 
            "        IND_RFC_ALG,\n" + 
            "        IND_COMPANY,\n" + 
            "        IND_SITUATION,\n" + 
            "        IND_TRADE_NUMBER,\n" + 
            "        FEC_PUBLICATION_SAT,\n" + 
            "        IND_TRADE_NUMBER2,\n" + 
            "        FEC_PUBLICATION_DOF,\n" + 
            "        IND_ESTATUS\n" + 
            "   FROM OLS.OLS_CAT_SAT_ALLEGED_TAB\n" + 
            "  WHERE IND_RFC_ALG = '"+tsRfc+"'";
        return lsQuery;
    }
    
}

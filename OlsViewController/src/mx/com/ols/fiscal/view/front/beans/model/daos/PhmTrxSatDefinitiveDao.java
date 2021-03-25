package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatDefinitiveBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatAllegedBean;

public class PhmTrxSatDefinitiveDao {
    public String getQueryInsert(PhmTrxSatDefinitiveBean toPhmTrxSatDefinitiveBean){
        String lsQuery = 
            "INSERT INTO OLS.OLS_CAT_SAT_DEFINITIVE_TAB (ID_CLIENT_DFN,  \n" + 
            "                                            ID_REQUEST,\n" + 
            "                                            IND_YEAR,\n" + 
            "                                            IND_MONTH,\n" + 
            "                                            NUM_MONTH,\n";
            if(toPhmTrxSatDefinitiveBean.getLtFecPeriod() != null){
                lsQuery += "                                            FEC_PERIOD,\n";
            }
            lsQuery +=            
            "                                            NUM_NUMBER,\n" + 
            "                                            IND_RFC_DFN,\n" + 
            "                                            IND_COMPANY,\n" + 
            "                                            IND_SITUATION,\n" + 
            "                                            IND_TRADE_NUMBER,\n";
            if(toPhmTrxSatDefinitiveBean.getLsFecPublication() != null){
                lsQuery += "                                            FEC_PUBLICATION,\n";
            }
            lsQuery +=              
            "                                            IND_TRADE_NUMBER2,\n";
            if(toPhmTrxSatDefinitiveBean.getLsFecPublicationDof() != null){
                lsQuery += "                                            FEC_PUBLICATION_DOF,\n";
            }
            lsQuery +=             
            "                                            IND_TRADE_DFN,\n";
            if(toPhmTrxSatDefinitiveBean.getLsFecPublicationSat() != null){
                lsQuery +=             "                                            FEC_PUBLICATION_SAT,\n";
            }
            if(toPhmTrxSatDefinitiveBean.getLsFecPubDofDfn() != null){
                lsQuery += "                                            FEC_PUB_DOF_DFN,\n";
            }
            lsQuery +=            
            "                                            IND_ESTATUS,\n" + 
            "                                            FEC_CREATION_DATE,\n" + 
            "                                            NUM_CREATED_BY, \n" + 
            "                                            FEC_LAST_UPDATE_DATE, \n" + 
            "                                            NUM_LAST_UPDATED_BY\n" + 
            "                                            )\n" + 
            "                                    VALUES (OLS.OLS_DEFINITIVE_ID_SQ.NEXTVAL,  \n" + 
            "                                            " + toPhmTrxSatDefinitiveBean.getLiIdRequest() + ",\n" + 
            "                                            " + toPhmTrxSatDefinitiveBean.getLiIndYear() + ",\n" + 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndMonth() + "',\n" + 
            "                                            " + toPhmTrxSatDefinitiveBean.getLiNumMonth() + ",\n";
            if(toPhmTrxSatDefinitiveBean.getLtFecPeriod() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatDefinitiveBean.getLsFecPeriod() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=
            "                                            " + toPhmTrxSatDefinitiveBean.getLiNumNumber() + ",\n" + 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndRfcDfn() + "',\n" + 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndCompany() + "',\n" + 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndSituation() + "',\n" + 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndTradeNumber() + "',\n";
            if(toPhmTrxSatDefinitiveBean.getLsFecPublication() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatDefinitiveBean.getLsFecPublication() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=  
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndTradeNumber() + "',\n";
            if(toPhmTrxSatDefinitiveBean.getLsFecPublicationDof() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatDefinitiveBean.getLsFecPublicationDof() + "','YYYY-MM-DD'),\n";
            }
            lsQuery += 
            "                                            '" + toPhmTrxSatDefinitiveBean.getLsIndTradeDfn() + "',\n" ;
            if(toPhmTrxSatDefinitiveBean.getLsFecPublicationSat() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatDefinitiveBean.getLsFecPublicationSat() + "','YYYY-MM-DD'),\n";
            }
            if(toPhmTrxSatDefinitiveBean.getLsFecPubDofDfn() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatDefinitiveBean.getLsFecPubDofDfn() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=
            "                                            'A',\n" + 
            "                                            SYSDATE,\n" + 
            "                                        " + toPhmTrxSatDefinitiveBean.getLiNumCreatedBy() + ", \n" + 
            "                                        SYSDATE, \n" + 
            "                                        " + toPhmTrxSatDefinitiveBean.getLiNumLastUpdatedBy() + "\n" + 
            "                                            )";
        
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxSatDefinitive(PhmTrxSatDefinitiveBean toPhmTrxSatDefinitiveBean) {
        ResponseBean    loResponse = new ResponseBean();
        //System.out.println(""+toPhmTrxSatDefinitiveBean.getLsIndRfcDfn()+" >> Validando si ya existe...");
        Integer liFlag = 0;
        //Integer liFlag = validateIfExist(toPhmTrxSatDefinitiveBean.getLsIndRfcDfn());
        //System.out.println("liFlag["+liFlag+"]");
        if(liFlag == 0){
            Connection loCnn = new ConnectionDataBase().getConnection();
            String     lsQueryParadigm = getQueryInsert(toPhmTrxSatDefinitiveBean);
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
        "  FROM OLS.OLS_CAT_SAT_DEFINITIVE_TAB\n" + 
        " WHERE IND_RFC_DFN = '" + tsRfc + "'";
        return lsQuery;
    }
    
    public List<PhmTrxSatDefinitiveBean> getAllDefinitivosByRfc(String tsRfc){
        List<PhmTrxSatDefinitiveBean> laRes = new ArrayList<PhmTrxSatDefinitiveBean>();
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        String                  lsQueryParadigm = getQueryAllDefinitivosByRfc(tsRfc);        
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                PhmTrxSatDefinitiveBean loItem = new PhmTrxSatDefinitiveBean();
                loItem.setLiIdClientDfn(loRs.getInt("ID_CLIENT_DFN"));
                loItem.setLiIdRequest(loRs.getInt("ID_REQUEST"));
                loItem.setLiIndYear(loRs.getInt("IND_YEAR"));                
                loItem.setLsIndMonth(loRs.getString("IND_MONTH"));
                loItem.setLiNumNumber(loRs.getInt("NUM_NUMBER"));
                loItem.setLiNumMonth(loRs.getInt("NUM_MONTH"));
                loItem.setLsIndRfcDfn(loRs.getString("IND_RFC_DFN"));
                loItem.setLsIndCompany(loRs.getString("IND_COMPANY"));
                loItem.setLsIndSituation(loRs.getString("IND_SITUATION"));
                loItem.setLsIndTradeNumber(loRs.getString("IND_TRADE_NUMBER"));
                loItem.setLsFecPublication(loRs.getString("FEC_PUBLICATION"));
                loItem.setLsIndTradeNumber2(loRs.getString("IND_TRADE_NUMBER2"));
                loItem.setLsFecPublicationDof(loRs.getString("FEC_PUBLICATION_DOF"));
                loItem.setLsIndTradeDfn(loRs.getString("IND_TRADE_DFN"));
                loItem.setLsFecPublicationSat(loRs.getString("FEC_PUBLICATION_SAT"));
                loItem.setLsFecPubDofDfn(loRs.getString("FEC_PUB_DOF_DFN"));
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
    
    public String getQueryAllDefinitivosByRfc(String tsRfc){
        String lsQuery = 
            " SELECT ID_CLIENT_DFN,\n" + 
            "        ID_REQUEST,\n" + 
            "        IND_YEAR,\n" + 
            "        IND_MONTH,\n" + 
            "        NUM_NUMBER,\n" + 
            "        NUM_MONTH,\n" + 
            "        FEC_PERIOD,\n" + 
            "        IND_PERIOD,\n" + 
            "        IND_RFC_DFN,\n" + 
            "        IND_COMPANY,\n" + 
            "        IND_SITUATION,\n" + 
            "        IND_TRADE_NUMBER,\n" + 
        "        FEC_PUBLICATION,\n" + 
            "        FEC_PUBLICATION_SAT,\n" + 
            "        IND_TRADE_NUMBER2,\n" + 
            "        FEC_PUBLICATION_DOF,\n" + 
            "        IND_TRADE_DFN,\n" + 
            "        FEC_PUB_DOF_DFN,\n" + 
            "        IND_ESTATUS\n" + 
            "   FROM OLS.OLS_CAT_SAT_DEFINITIVE_TAB\n" + 
            "  WHERE IND_RFC_DFN = '"+tsRfc+"'";
        //System.out.println(lsQuery);
        return lsQuery;
    }
}

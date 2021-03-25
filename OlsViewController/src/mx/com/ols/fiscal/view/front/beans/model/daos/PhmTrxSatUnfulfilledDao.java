package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatUnfulfilledBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.ResponseBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.PhmTrxSatDefinitiveBean;

public class PhmTrxSatUnfulfilledDao {
    public String getQueryInsert(PhmTrxSatUnfulfilledBean toPhmTrxSatUnfulfilledBean){
        String lsQuery = 
            "INSERT INTO OLS.OLS_CAT_SAT_UNFULFILLED_TAB(ID_CLIENT_UNF,  \n" + 
            "                                            ID_REQUEST,\n" + 
            "                                            IND_YEAR,\n" + 
            "                                            IND_MONTH,\n" + 
            "                                            NUM_MONTH,\n";
            if(toPhmTrxSatUnfulfilledBean.getLtFecPeriod() != null){
                lsQuery += "                                            FEC_PERIOD,\n";
            }
            lsQuery +=            
            "                                            NUM_NUMBER,\n" + 
            "                                            IND_RFC_UNF,\n" + 
            "                                            IND_COMPANY,\n" + 
            "                                            IND_TYPE_PERSON,\n" + 
            "                                            IND_SUPPOSED,\n";
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublication() != null){
                lsQuery += "                                            FEC_PUBLICATION,\n";
            }
            lsQuery +=
            "                                            NUM_AMOUNT,\n";
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() != null){
                lsQuery += "                                            FEC_PUBLICATION_LAW,\n";
            }
            lsQuery +=              
            "                                            IND_ESTATUS,\n" + 
            "                                            FEC_CREATION_DATE,\n" + 
            "                                            NUM_CREATED_BY, \n" + 
            "                                            FEC_LAST_UPDATE_DATE, \n" + 
            "                                            NUM_LAST_UPDATED_BY\n" + 
            "                                            )\n" + 
            //"                                    VALUES (" + toPhmTrxSatUnfulfilledBean.getLiIdClientUnf() + ",  \n" + 
            "                                    VALUES (OLS.OLS_UNFULFILLED_ID_SQ.NEXTVAL,  \n" + 
            "                                            " + toPhmTrxSatUnfulfilledBean.getLiIdRequest() + ",\n" + 
            "                                            " + toPhmTrxSatUnfulfilledBean.getLiIndYear() + ",\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndMonth() + "',\n" + 
            "                                            " + toPhmTrxSatUnfulfilledBean.getLiNumMonth() + ",\n";
            if(toPhmTrxSatUnfulfilledBean.getLtFecPeriod() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatUnfulfilledBean.getLsFecPeriod() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=
            "                                            " + toPhmTrxSatUnfulfilledBean.getLiNumNumber() + ",\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndRfcUnf() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndCompany() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndTypePerson() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndSupposed() + "',\n" ;
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublication() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatUnfulfilledBean.getLsFecPublication() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=  
            "                                            " + toPhmTrxSatUnfulfilledBean.getLdNumAmount() + ",\n";
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() != null){
                lsQuery += "                         to_date('" + toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() + "','YYYY-MM-DD'),\n";
            }
            lsQuery +=  
            "                                            'A',\n" + 
            "                                            SYSDATE,\n" + 
            "                                        " + toPhmTrxSatUnfulfilledBean.getLiNumCreatedBy() + ", \n" + 
            "                                        SYSDATE, \n" + 
            "                                        " + toPhmTrxSatUnfulfilledBean.getLiNumLastUpdatedBy() + "\n" + 
            "                                            )";
        
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxSaTUnfulfilled(PhmTrxSatUnfulfilledBean toPhmTrxSatUnfulfilled) {
        ResponseBean    loResponse = new ResponseBean();
        Integer liFlag = validateIfExist(toPhmTrxSatUnfulfilled.getLsIndRfcUnf());
        if(liFlag == 0){
            Connection loCnn = new ConnectionDataBase().getConnection();                            
            String     lsQueryParadigm = getQueryInsert(toPhmTrxSatUnfulfilled);
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
                    System.gc();
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
        "  FROM OLS.OLS_CAT_SAT_UNFULFILLED_TAB\n" + 
        " WHERE IND_RFC_UNF = '" + tsRfc + "'";
        return lsQuery;
    }
    
    public List<PhmTrxSatUnfulfilledBean> getAllIncumplidosByRfc(String tsRfc){
        List<PhmTrxSatUnfulfilledBean> laRes = new ArrayList<PhmTrxSatUnfulfilledBean>();
        Connection              loCnn = new ConnectionDataBase().getConnection();
        ResultSet               loRs = null;
        String                  lsQueryParadigm = getQueryAllIncumplidosByRfc(tsRfc);        
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                PhmTrxSatUnfulfilledBean loItem = new PhmTrxSatUnfulfilledBean();
                loItem.setLiIdClientUnf(loRs.getInt("ID_CLIENT_UNF"));
                loItem.setLiIdRequest(loRs.getInt("ID_REQUEST"));
                loItem.setLiNumNumber(loRs.getInt("NUM_NUMBER"));
                loItem.setLiNumMonth(loRs.getInt("NUM_MONTH"));
                loItem.setLiIndYear(loRs.getInt("IND_YEAR"));
                loItem.setLsIndMonth(loRs.getString("IND_MONTH"));
                loItem.setLsIndRfcUnf(loRs.getString("IND_RFC_UNF"));
                loItem.setLsIndCompany(loRs.getString("IND_COMPANY"));
                loItem.setLsIndTypePerson(loRs.getString("IND_TYPE_PERSON"));
                loItem.setLsIndSupposed(loRs.getString("IND_SUPPOSED"));
                loItem.setLsFecPublication(loRs.getString("FEC_PUBLICATION"));
                loItem.setLdNumAmount(loRs.getDouble("NUM_AMOUNT"));
                loItem.setLsFecPublicationLaw(loRs.getString("FEC_PUBLICATION_LAW"));
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
    
    public String getQueryAllIncumplidosByRfc(String tsRfc){
        String lsQuery = 
            " SELECT ID_CLIENT_UNF,\n" + 
            "        ID_REQUEST,\n" + 
            "        NUM_NUMBER,\n" + 
            "        FEC_PERIOD,\n" + 
            "        NUM_MONTH,\n" + 
            "        IND_YEAR,\n" + 
            "        IND_MONTH,\n" + 
            "        IND_PERIOD,\n" + 
            "        IND_RFC_UNF,\n" + 
            "        IND_COMPANY,\n" + 
            "        IND_TYPE_PERSON,\n" + 
            "        IND_SUPPOSED,\n" + 
            "        FEC_PUBLICATION,\n" + 
            "        NUM_AMOUNT,\n" + 
            "        FEC_PUBLICATION_LAW,\n" + 
            "        IND_ESTATUS\n" + 
            "   FROM OLS.OLS_CAT_SAT_UNFULFILLED_TAB\n" + 
            "  WHERE IND_RFC_UNF = '"+tsRfc+"'";
        return lsQuery;
    }
    
    public ResponseBean insertPhmTrxSaTNoloc(PhmTrxSatUnfulfilledBean toPhmTrxSatUnfulfilled) {
        ResponseBean    loResponse = new ResponseBean();
        Connection loCnn = new ConnectionDataBase().getConnection();                            
        String     lsQueryParadigm = getQueryInsertNoloc(toPhmTrxSatUnfulfilled);
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
                System.gc();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }
       
        return loResponse;
    }
    
    public String getQueryInsertNoloc(PhmTrxSatUnfulfilledBean toPhmTrxSatUnfulfilledBean){
        String lsQuery = 
            "INSERT INTO OLS.OLS_CAT_SAT_NOLOC_TAB(IND_NUM,  \n" + 
            "                                            IND_RFC,\n" + 
            "                                            IND_COMPANY,\n" + 
            "                                            IND_TYPE,\n" + 
            "                                            IND_SUPUESTO,\n"+
        "                                            FEC_PUBLICATION,\n";
            if(toPhmTrxSatUnfulfilledBean.getLdNumAmount() != null){
                lsQuery += "                                            IND_MONTO,\n";
            }           
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() != null){
                lsQuery += "                                            FEC_PUBL_MONTO,\n";
            }            
            lsQuery +=              
            "                                            IND_ESTATUS\n" + 
            "                                            )\n" + 
            //"                                    VALUES (" + toPhmTrxSatUnfulfilledBean.getLiIdClientUnf() + ",  \n" + 
            "                                    VALUES (" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLiNumNumber() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndRfcUnf() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndCompany() + "',\n" + 
            "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndTypePerson() + "',\n"+
        "                                            '" + toPhmTrxSatUnfulfilledBean.getLsIndSupposed() + "',\n"+
        "                                            '" + toPhmTrxSatUnfulfilledBean.getLsFecPublication() + "',\n";
            if(toPhmTrxSatUnfulfilledBean.getLdNumAmount() != null){
                lsQuery += "                         '" + toPhmTrxSatUnfulfilledBean.getLdNumAmount() + "',\n";
            }
            if(toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() != null){
                lsQuery += "                         '" + toPhmTrxSatUnfulfilledBean.getLsFecPublicationLaw() + "',\n";
            }
            lsQuery +=  
            "                                            'A'\n" + 
            "                                            )";
        
        return lsQuery;
    }
    
}

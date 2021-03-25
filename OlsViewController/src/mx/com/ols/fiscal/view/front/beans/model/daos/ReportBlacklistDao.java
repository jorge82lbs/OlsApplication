package mx.com.ols.fiscal.view.front.beans.model.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatConfigParamTab;
import mx.com.ols.fiscal.view.front.beans.model.beans.OlsTmpBlacklistBean;
import mx.com.ols.fiscal.view.front.beans.model.connection.ConnectionDataBase;
import mx.com.ols.fiscal.view.front.beans.types.ClienteBean;

import oracle.jdbc.OracleTypes;

public class ReportBlacklistDao {
    public ReportBlacklistDao() {
        super();
    }
    
    public List<ClienteBean> getAllClients(){
        List<ClienteBean> listParamters = new ArrayList<ClienteBean>();
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;   
        String                        lsQueryParadigm = 
            "SELECT ID_CLIENT_SAT, " +
            "       TP.IND_RFC_CLIENT,\n" + 
            "       (SELECT TC.IND_COMPANY\n" + 
            "          FROM OLS.OLS_CAT_SAT_CLIENTS_OLS_TAB TC\n" + 
            "         WHERE TC.IND_RFC = TP.IND_RFC_CLIENT\n" + 
            "       ) CLIENTE, COUNT(1) NUM_PROVEEDORES\n" + 
            "  FROM OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB TP\n " +
            " GROUP BY ID_CLIENT_SAT, TP.IND_RFC_CLIENT\n";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                ClienteBean loOlsCatConfigParamTab = new ClienteBean();             
                
                loOlsCatConfigParamTab.setLsRfc(loRs.getString("IND_RFC_CLIENT") == null ? null : 
                                          loRs.getString("IND_RFC_CLIENT").trim()); 
                loOlsCatConfigParamTab.setLsIdClienteSat(loRs.getString("ID_CLIENT_SAT") == null ? null : 
                                          loRs.getString("ID_CLIENT_SAT").trim()); 
                loOlsCatConfigParamTab.setLsRazonSocial(loRs.getString("CLIENTE") == null ? null : 
                                          loRs.getString("CLIENTE").trim()); 
                loOlsCatConfigParamTab.setLsExtraData(loRs.getString("NUM_PROVEEDORES") == null ? null : 
                                          loRs.getString("NUM_PROVEEDORES").trim()); 
                
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
    
    public List<ClienteBean> getDataReport(String tsRfcClient){
        List<ClienteBean> listParamters = new ArrayList<ClienteBean>();
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;   
        String                        lsQueryParadigm = 
            "SELECT  OLS.ID_PROVIDER,\n" + 
            "        OLS.ID_CLIENT_SAT,\n" + 
            "        OLS.IND_RFC_CLIENT,\n" + 
            "        OLS.IND_RFC,\n" + 
            "        OLS.IND_COMPANY,\n" + 
            "        (SELECT   COUNT (1)\n" + 
            "           FROM   OLS.OLS_CAT_SAT_ALLEGED_TAB SUP\n" + 
            "          WHERE   UPPER (TRIM (SUP.IND_RFC_ALG)) LIKE\n" + 
            "                     '%' || UPPER (OLS.IND_RFC) || '%')\n" + 
            "           ALLEGED,\n" + 
            "        (SELECT   COUNT (1)\n" + 
            "           FROM   OLS.OLS_CAT_SAT_DEFINITIVE_TAB DFN\n" + 
            "          WHERE   UPPER (TRIM (DFN.IND_RFC_DFN)) LIKE\n" + 
            "                     '%' || UPPER (OLS.IND_RFC) || '%')\n" + 
            "           DEFINITIVE,\n" + 
            "        (SELECT   COUNT (1)\n" + 
            "           FROM   OLS.OLS_CAT_SAT_UNFULFILLED_TAB UNF\n" + 
            "          WHERE   UPPER (TRIM (UNF.IND_RFC_UNF)) LIKE\n" + 
            "                     '%' || UPPER (OLS.IND_RFC) || '%')\n" + 
            "           UNFULFILLED\n" + 
            "  FROM  OLS.OLS_CAT_SAT_PROVIDERS_OLS_TAB OLS\n" + 
            " WHERE  OLS.IND_RFC_CLIENT = '"+tsRfcClient+"'";
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQueryParadigm);  
            while(loRs.next()){
                ClienteBean loOlsCatConfigParamTab = new ClienteBean();             
                
                loOlsCatConfigParamTab.setLsRfc(loRs.getString("IND_RFC_CLIENT") == null ? null : 
                                          loRs.getString("IND_RFC_CLIENT").trim()); 
                loOlsCatConfigParamTab.setLsRazonSocial(loRs.getString("CLIENTE") == null ? null : 
                                          loRs.getString("CLIENTE").trim()); 
                
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
    
    
    /**
     * Obtiene información de Landmark relativa a GetVersionMaterialSegments
     * @autor Jorge Luis Bautista Santiago
     * @param InputGetScheduledProgEventDetails
     * @return OutputGetScheduledProgEventDetails
     */
    public String callPopulateReportBlacklist(String tsRfcClient) 
    throws SQLException {
        String loOutputResponse = "";
        Connection        loCnn = new ConnectionDataBase().getConnection();
        CallableStatement loCallStmt = null;
        System.out.println("(callPopulateReportBlacklist): "+tsRfcClient);
        
        String            lsQueryCall = "{call OLS.OLS_POPULATE_REPORT_PR (?,?)}";
        try {
            loCallStmt = loCnn.prepareCall(lsQueryCall);
            loCallStmt.setString(1, tsRfcClient);
            loCallStmt.registerOutParameter(2, OracleTypes.VARCHAR);            
            loCallStmt.execute();
            //System.out.println("");
            loOutputResponse = (String) loCallStmt.getObject(2);
            System.out.println("Resultado: "+loOutputResponse);
            
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            loOutputResponse = "ERROR";
            System.out.println(loExSql.getMessage());
            throw loExSql;
        }
        finally{
            try {
                loCnn.close();
            } catch (SQLException loEx) {
                loEx.printStackTrace();
            }
        }
        return loOutputResponse;
    }
    
    /**
     * Obtiene direcciones de correo como destinatarios
     * @autor Jorge Luis Bautista Santiago
     * @return List
     */
    public List<OlsTmpBlacklistBean> getReportBlacklist(String tsRFC){
        List<OlsTmpBlacklistBean> laServices = new ArrayList<OlsTmpBlacklistBean>();
        //System.out.println("getReportBlacklist....conectando a BD");
        Connection                    loCnn = new ConnectionDataBase().getConnection();
        ResultSet                     loRs = null;
        String                        lsQuery = getQueryReportBlacklist(tsRFC);
        //System.out.println("getReportBlacklist....query obtenido OK");
        try {
            Statement loStmt = loCnn.createStatement();
            loRs = loStmt.executeQuery(lsQuery);  
            while(loRs.next()){
                OlsTmpBlacklistBean loServiceBean = new OlsTmpBlacklistBean();             
                loServiceBean.setLiIdProvider(loRs.getInt("ID_PROVIDER"));
                loServiceBean.setLiIdClientSat(loRs.getInt("ID_CLIENT_SAT"));
                loServiceBean.setLsIndRfcClient(loRs.getString("IND_RFC_CLIENT"));
                loServiceBean.setLsIndRfc(loRs.getString("IND_RFC"));
                loServiceBean.setLsIndCompany(loRs.getString("IND_COMPANY") == null ? "" : loRs.getString("IND_COMPANY"));
                loServiceBean.setLiAlleged(loRs.getInt("ALLEGED"));
                loServiceBean.setLiDefinitive(loRs.getInt("DEFINITIVE"));
                loServiceBean.setLiUnfulfilled(loRs.getInt("UNFULFILLED"));
                loServiceBean.setLiBlacklist(loRs.getInt("BLACKLIST"));
                
                laServices.add(loServiceBean);
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
        return laServices;
    }
    
    public String getQueryReportBlacklist(String tsRFC){
        String lsQuery = 
            "SELECT ID_PROVIDER,\n" + 
            "       ID_CLIENT_SAT,\n" + 
            "       IND_RFC_CLIENT,\n" + 
            "       IND_RFC,\n" + 
            "       IND_COMPANY,\n" + 
            "       ALLEGED,\n" + 
            "       DEFINITIVE,\n" + 
            "       UNFULFILLED,\n" + 
            "       BLACKLIST  \n" + 
            "  FROM OLS.OLS_TMP_BLACKLIST_TAB\n" + 
            " WHERE IND_RFC_CLIENT = '"+tsRFC+"'\n " +        
            " ORDER BY BLACKLIST DESC, IND_COMPANY";
        return lsQuery;
    }
    
}

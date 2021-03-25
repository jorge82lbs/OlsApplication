package mx.com.ols.fiscal.view.front.beans.model.test;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import mx.com.ols.fiscal.model.types.ResponseUpdDao;

import oracle.jdbc.OracleTypes;

public class ResResultSetGallo {
    public ResResultSetGallo() {
        super();
    }

    public static void main(String[] args) {
        ResResultSetGallo resResultSetGallo = new ResResultSetGallo();
        try {
            ResponseUpdDao loResponseUpdDao = resResultSetGallo.callProcedureResultSet();
            System.out.println("Resultado: "+loResponseUpdDao.getLsResponse());
        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        }
        //resResultSetGallo.pasearFecha();
        
    }
    
    public ResponseUpdDao callProcedureResultSet() throws SQLException {
        ResponseUpdDao    loResponseUpdDao = new ResponseUpdDao();
        Connection        loCnn = getConnection();
        CallableStatement loCallStmt = null;
        System.out.println("(GetScheduledProgEventDetails).........");
        Integer parameter1 = 1818;
        String parameter2 = "PLAN";
        java.util.Date     ltDate1 = getDateYYYYMMDD_LMK("03-12-2019 1:00:00");
        java.util.Date     ltDate2 = getDateYYYYMMDD_LMK("03-12-2019 12:59:59");
        String parameter5 = "N";
        String parameter6 = "S";
        System.out.println("ltDate1: ["+ltDate1+"]");
        System.out.println("ltDate2: ["+ltDate2+"]");
        
        java.sql.Date     ltDateSQL1 = new java.sql.Date(ltDate1.getTime());
        java.sql.Date     ltDateSQL2 = new java.sql.Date(ltDate2.getTime());
        
        String            lsQueryParadigm = "{call INTEGRATION_SCHEDULES.GetScheduledProgEventDetails (?,?,?,?,?,?,?,?)}";
        try {
            System.out.println("Dentro de try");
            loCallStmt = loCnn.prepareCall(lsQueryParadigm);
            System.out.println("prepareCall");
            //loCallStmt.setBigDecimal(1, new BigDecimal(parameter1));
            loCallStmt.setInt(1, parameter1);
            loCallStmt.setString(2, parameter2);
            loCallStmt.setDate(3, ltDateSQL1);
            loCallStmt.setDate(4, ltDateSQL2);
            loCallStmt.setString(5, parameter5);
            loCallStmt.setString(6, parameter6);
                        
            loCallStmt.registerOutParameter(7, OracleTypes.CURSOR);
            loCallStmt.registerOutParameter(8, OracleTypes.CURSOR);
            System.out.println("RegisterOutput");
            loCallStmt.execute();
            System.out.println("execute");
            
            ResultSet resultSet = (ResultSet) loCallStmt.getObject(8);
            while (resultSet.next()) {
                String userName = resultSet.getString(1);
                String name = resultSet.getString(2);
                String mail = resultSet.getString(3);
                
                System.out.println("Resultado: ["+userName+"]["+name+"]["+mail+"]");
            }
            
            loResponseUpdDao.setLsResponse("OK");
            loResponseUpdDao.setLiAffected(0);
            loResponseUpdDao.setLsMessage("Success");
        } catch (SQLException loExSql) {
            System.out.println("ERROR AL EJECUTAR: ");
            System.out.println(lsQueryParadigm);
            loResponseUpdDao.setLsResponse("ERROR");
            loResponseUpdDao.setLiAffected(0);
            loResponseUpdDao.setLsMessage(loExSql.getMessage());
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
        return loResponseUpdDao;
    }
    
    public Connection getConnection(){
        Connection                          loCnn = null;
        try{
            loCnn = 
                DriverManager.getConnection("jdbc:oracle:thin:@10.173.0.66:1622:TVLRSDBT", "JSERRANOD", "XkaFCY9Qq6Px05QcYrtz");
                         
        }catch(SQLException loExSql){
            loExSql.printStackTrace();
            loCnn = null;
        }catch(Exception loEx){
            loEx.printStackTrace();
            loCnn = null;
        }    
        return loCnn;
    }
    
    /**
     * Convierte una cadena en formato fecha a una fecha real sql con ese mismo formato
     * @autor Jorge Luis Bautista Santiago
     * @param lsDateStr
     * @return java.sql.Date
     */
    private java.sql.Date getDateYYYYMMDD(String lsDateStr){
        SimpleDateFormat loFormatText = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String           lsStrDate = lsDateStr;
        java.util.Date             ltDatePivot = null;
        try {
            ltDatePivot = loFormatText.parse(lsStrDate);
            System.out.println("ltDatePivot["+ltDatePivot+"]");
        } catch (ParseException loEx) {
            System.out.println("ERROR al PARSEAR (getDateYYYYMMDD)");
            loEx.printStackTrace();
        }
        java.sql.Date ltDateResponse = new java.sql.Date(ltDatePivot.getTime());
        return ltDateResponse;
    }
    
    public void pasearFecha(){
        java.util.Date     ltDate1 = getDateYYYYMMDD_LMK("03-12-2019 1:00:00");
        java.util.Date     ltDate2 = getDateYYYYMMDD_LMK("03-12-2019 12:59:59");
        //java.sql.Date     ltDate2 = getDateYYYYMMDD("03-12-2019");
        System.out.println("ltDate1: ["+ltDate1+"]");
        System.out.println("ltDate2: ["+ltDate2+"]");
    }
    
    private java.util.Date getDateYYYYMMDD_LMK(String lsDateStr){
        SimpleDateFormat loFormatText = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String           lsStrDate = lsDateStr;
        java.util.Date             ltDatePivot = null;
        try {
            ltDatePivot = loFormatText.parse(lsStrDate);
            //System.out.println("ltDatePivot["+ltDatePivot+"]");
        } catch (ParseException loEx) {
            System.out.println("ERROR al PARSEAR (getDateYYYYMMDD)");
            loEx.printStackTrace();
        }
        return ltDatePivot;
    }
    
    
}

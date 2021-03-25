package mx.com.ols.fiscal.view.front.beans.model.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.ols.fiscal.model.types.ResponseUpdDao;

import oracle.jdbc.OracleTypes;

public class ResResultSet {
    public ResResultSet() {
        super();
    }
    
    public ResponseUpdDao callProcedureResultSet() throws SQLException {
        ResponseUpdDao    loResponseUpdDao = new ResponseUpdDao();
        Connection        loCnn = getConnection();
        CallableStatement loCallStmt = null;
        System.out.println("(get_All_Ols_Users).........");
        
        //System.out.println("ltDate: ["+ltDate+"]");
        String            lsQueryParadigm = "{call OLS.get_All_Ols_Users(?)}";
        try {
            System.out.println("Dentro de try");
            loCallStmt = loCnn.prepareCall(lsQueryParadigm);
            System.out.println("prepareCall");
            loCallStmt.registerOutParameter(1, OracleTypes.CURSOR);
            System.out.println("RegisterOutput");
            loCallStmt.execute();
            System.out.println("execute");
            
            ResultSet resultSet = (ResultSet) loCallStmt.getObject(1);
            while (resultSet.next()) {
                String userName = resultSet.getString("ID_USER_NAME");
                String name = resultSet.getString("NOM_USER");
                String mail = resultSet.getString("IND_MAIL_USER");
                
                System.out.println("Usuario: ["+userName+"]["+name+"]["+mail+"]");
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
    
    
    public static void main(String a[]){
        ResResultSet loResResultSet = new ResResultSet();
        try {
            ResponseUpdDao    loResponseUpdDao = loResResultSet.callProcedureResultSet();
            System.out.println("Resultado: "+loResponseUpdDao.getLsResponse());
        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    
    public Connection getConnection(){
        Connection                          loCnn = null;
        try{
            loCnn = 
                DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "rondaboliche");
                         
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

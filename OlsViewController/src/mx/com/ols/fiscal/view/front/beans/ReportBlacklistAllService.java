package mx.com.ols.fiscal.view.front.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.List;

import mx.com.ols.fiscal.view.front.beans.model.beans.OlsTmpBlacklistBean;
import mx.com.ols.fiscal.view.front.beans.model.beans.OlsTrxSatClientsReportBean;
import mx.com.ols.fiscal.view.front.beans.model.daos.PhmTrxClientsSatDao;
import mx.com.ols.fiscal.view.front.beans.model.daos.ReportBlacklistDao;
import mx.com.ols.fiscal.view.front.beans.types.BasicInputParameters;
import mx.com.ols.fiscal.view.front.beans.types.ClienteBean;
import mx.com.ols.fiscal.view.front.beans.types.ResponseService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportBlacklistAllService {
    public ReportBlacklistAllService() {
        super();
    }
    
    public ResponseService executeServiceAction(BasicInputParameters loInput){
        ResponseService loResponseService = new ResponseService();
        //EntityMappedDao        loEntityMappedDao = new EntityMappedDao();
        String lsReturn = "Son mis pitbulls y el bolo";
        loResponseService.setLiIdRequest(loInput.getLiIdRequest());
        loResponseService.setLiIdService(loInput.getLiIdService());
        loResponseService.setLiIdUser(loInput.getLiIdUser());
        loResponseService.setLsMessageResponse(lsReturn);
        loResponseService.setLsServiceType(loInput.getLsServiceType());
        loResponseService.setLsMessageResponse(lsReturn);
        loResponseService.setLsUserName(loInput.getLsUserName());
        //Obtener idLog service de la tabla 
        //Integer                   liIdLogService = loInput.getLiIdRequest();//new ViewObjectDao().getMaxIdParadigm("Log") + 1;
        
        /*TdlLmkIntServicesLogRowBean loSlb = new TdlLmkIntServicesLogRowBean();
        loSlb.setLiIdLogServices(liIdLogService);
        loSlb.setLiIdService(loInput.getLiIdService());
        loSlb.setLiIndProcess(0);
        loSlb.setLiNumPgmProcessId(loInput.getLiIdRequest());
        loSlb.setLiNumProcessId(loInput.getLiIdRequest());
        loSlb.setLiNumUser(loInput.getLiIdUser());
        loSlb.setLsIndEstatus("1");
        loSlb.setLsIndResponse("N");
        loSlb.setLsIndServiceType(loInput.getLsServiceType());
        loSlb.setLsMessage("Execute DummyProccess");
        loSlb.setLsUserName(loInput.getLsUserName());
        loSlb.setLiIdUser(loInput.getLiIdUser());
*/
        //new UtilFaces().insertLogServiceService(loSlb);
        
        /*loEntityMappedDao.insertSimpleServicesLog(loSlb, 
                                                  loInput.getLiIdUser(), 
                                                  loInput.getLsUserName()
                                                  );*/
        
        System.out.println("Ejecutando ReportBlacklistAllService a las "+new Date()+" Input["+loInput.getLsMessage()+"]");

        ReportBlacklistDao loReportBlacklistDao = new ReportBlacklistDao();
        PhmTrxClientsSatDao loPhmTrxClientsSatDao = new PhmTrxClientsSatDao();
        List<ClienteBean> laListClientes = loReportBlacklistDao.getAllClients();
        if(laListClientes.size()>0){
            loPhmTrxClientsSatDao.deleteClientsReportBlacklist();
            //invocar procedimiento por cada RFC del cliente
            for(ClienteBean loClientBean : laListClientes){
                String lsMensaje = "Procesado";
                if(Integer.parseInt(loClientBean.getLsExtraData()) > 0){
                    try {
                        //System.out.println("Service: Call Procedure ["+new Date()+"]");
                        String lsResultado = loReportBlacklistDao.callPopulateReportBlacklist(loClientBean.getLsRfc());
                        //System.out.println("Service: resultado["+lsResultado+"] ["+new Date()+"]");
                        if(lsResultado.equalsIgnoreCase("OK")){
                            //realizar un select a los registros
                            List<OlsTmpBlacklistBean> laListSuppliers = 
                                loReportBlacklistDao.getReportBlacklist(loClientBean.getLsRfc());
                            System.out.println("Service: numero de Proveedores["+laListSuppliers.size()+"]");
                            //crear el excel con los datos obtenidos
                            createExcelFile(loClientBean.getLsRfc(),
                                            laListSuppliers);
                            //insertar en bd de control el resultado del procesamiento
                            //System.out.println("Insertar en bd de control el resultado del procesamiento");
                            
                        }else{
                            OlsTrxSatClientsReportBean loBean = new OlsTrxSatClientsReportBean();
                            loBean.setLsIdClientSat(loClientBean.getLsIdClienteSat());
                            loBean.setLsIndRfc(loClientBean.getLsRfc());
                            loBean.setLsIndCompany(loClientBean.getLsRazonSocial());
                            loBean.setLsIndComment("Error al generar Reporte");
                            loBean.setLsNumSuppliers("0");
                            loBean.setLsIndEstatus("A");
                            loPhmTrxClientsSatDao.insertTrxClientsReportBlacklist(loBean);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error al ejecutar el SP: "+e.getMessage());
                    }
                }else{
                    lsMensaje = "Sin Proveedores para Procesar";
                }
                OlsTrxSatClientsReportBean loBean = new OlsTrxSatClientsReportBean();
                loBean.setLsIdClientSat(loClientBean.getLsIdClienteSat());
                loBean.setLsIndRfc(loClientBean.getLsRfc());
                loBean.setLsIndCompany(loClientBean.getLsRazonSocial());
                loBean.setLsIndComment(lsMensaje);
                loBean.setLsNumSuppliers(loClientBean.getLsExtraData());
                loBean.setLsIndEstatus("A");
                loPhmTrxClientsSatDao.insertTrxClientsReportBlacklist(loBean);
                
                
            }            
        }else{
            OlsTrxSatClientsReportBean loBean = new OlsTrxSatClientsReportBean();
            loBean.setLsIdClientSat("0");
            loBean.setLsIndRfc("0");
            loBean.setLsIndCompany("0");
            loBean.setLsIndComment("No existen clientes para procesar");
            loBean.setLsNumSuppliers("0");
            loBean.setLsIndEstatus("A");
            loPhmTrxClientsSatDao.insertTrxClientsReportBlacklist(loBean);
        }


        return loResponseService;
    }
    
    public boolean createExcelFile(String lsRfcClient,
                                   List<OlsTmpBlacklistBean> laListSuppliers){
        boolean lbRes = true;
        try{
            String lsNomArch = lsRfcClient + "_"+getIdByDate()+".xlsx";
            System.out.println("Nombre del archivo: "+lsNomArch);
            File loFile = new File("D:\\jorge82lbs\\OLS\\Reports\\"+lsNomArch);
            Workbook loWorkbook = new XSSFWorkbook();
            Sheet loSheet = loWorkbook.createSheet("SUPP");            
            CellStyle loCellStyle = loWorkbook.createCellStyle();
            loCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            String[] lsHeader = {"NUM","RFC SUPPLIER", "SUPPLIER NAME",
                "PRESUNTOS", "DEFINITIVOS","NO LOCALIZADOS", "EN BLACKLIST"};
            Row loRowHdr = loSheet.createRow(0);
            CellStyle loHeaderStyle = loWorkbook.createCellStyle();
            Font loFont = loWorkbook.createFont();
            loFont.setBold(true);
            loHeaderStyle.setFont(loFont);
            //System.out.println("Encabezado......");
            // Encabezado
            for (int liI = 0; liI < lsHeader.length; liI++) {
                Cell loCell = loRowHdr.createCell(liI);
                loCell.setCellStyle(loHeaderStyle);
                loCell.setCellValue(lsHeader[liI]);
            }
            //System.out.println("Cuerpo......");
            // Poblar registros con informacion de la lista
            int liI=1;
            for(OlsTmpBlacklistBean loBeanSupp : laListSuppliers){
                Row loRow = loSheet.createRow(liI);                
                //System.out.print("Row("+liI+")");
                
                Cell celda0 = loRow.createCell(0);
                celda0.setCellValue(liI);
                Cell celda1 = loRow.createCell(1);
                celda1.setCellValue(loBeanSupp.getLsIndRfc());
                //System.out.print("-("+loBeanSupp.getLsIndRfc()+")");
                Cell celda2 = loRow.createCell(2);
                celda2.setCellValue(loBeanSupp.getLsIndCompany());
                //System.out.print("-("+loBeanSupp.getLsIndCompany()+")");
                Cell celda3 = loRow.createCell(3);
                celda3.setCellValue(loBeanSupp.getLiAlleged());
                //System.out.print("-("+loBeanSupp.getLiAlleged()+")");
                Cell celda4 = loRow.createCell(4);
                celda4.setCellValue(loBeanSupp.getLiDefinitive());
                //System.out.print("-("+loBeanSupp.getLiDefinitive()+")");
                Cell celda5 = loRow.createCell(5);
                celda5.setCellValue(loBeanSupp.getLiUnfulfilled());
                //System.out.print("-("+loBeanSupp.getLiUnfulfilled()+")");
                Cell celda6 = loRow.createCell(6);
                celda6.setCellValue(loBeanSupp.getLiBlacklist());
                //System.out.print("-("+loBeanSupp.getLiBlacklist()+")");
                //System.out.println("");
                liI++;
            }
            System.out.println("fin de body......");
            // Ahora guardaremos el archivo
            try {
                //System.out.println("FileoutpStream");
                FileOutputStream loOut = new FileOutputStream(loFile);
                loWorkbook.write(loOut);
                loWorkbook.close();
                System.out.println("Archivo creado existosamente en: "+loFile.getAbsolutePath());

            } catch (FileNotFoundException ex) {
                System.out.println("Archivo no localizable en sistema de archivos");
            } catch (IOException ex) {
                System.out.println("Error de entrada/salida");
            }
        }catch(Exception loEx){
            lbRes = false;
            System.out.println("Error al crear excel: "+loEx.getMessage());
        }
        return lbRes;
    }
    
    /**
     * Obtiene, en base a la fecha, el id_paradigm a manejar en intergracion
     * @autor Jorge Luis Bautista Santiago     
     * @return String
     */
    private String getIdByDate(){
        String     lsResponse = null;
        DateFormat loDf = new SimpleDateFormat("yyyyMMddHHmmss");
        lsResponse = loDf.format(new java.util.Date(System.currentTimeMillis()));
        return lsResponse;
    }
    
}

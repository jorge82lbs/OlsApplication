package mx.com.ols.fiscal.view.front.beans.mail;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;

import javax.activation.FileDataSource;

import javax.faces.context.FacesContext;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

import javax.naming.InitialContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import mx.com.ols.fiscal.view.front.beans.model.beans.OlsCatConfigParamTab;
import mx.com.ols.fiscal.view.front.beans.model.daos.ViewObjectDao;
import mx.com.ols.fiscal.view.front.beans.types.OlsCatSatMailsCtesBean;
import mx.com.ols.fiscal.view.front.beans.types.ParReportBean;
import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

import oracle.jbo.JboException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class OlsMailBean {
    @SuppressWarnings("unchecked")
    public void sendMailSimple(ParReportBean toParBean
                              ) throws AddressException, MessagingException, NoSuchProviderException {
        Properties props = new Properties();    
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); //Linea agregada que soluciono muchos problemas
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");        
        Session loSession = Session.getDefaultInstance(props);
        MailHeaderBean loMailHeaderBean = getMailHeaderBean(toParBean.getLsClient(),
                                                            toParBean.getLsRfc(),
                                                            toParBean.getLsYear(),
                                                            toParBean.getLsMonth()
                                                            );
        MimeMessage loMessage = new MimeMessage(loSession);
        try {
            loMessage.setFrom(new InternetAddress(loMailHeaderBean.getLsSenderMail()));  
            loMessage.setSubject(loMailHeaderBean.getLsSubject());
            
            for(String lsRecTo : loMailHeaderBean.getLsReceiverMail()){
                System.out.println("Enviando correo a ["+lsRecTo+"]");
                loMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(lsRecTo));
            }                   
            String tsContribuyente = toParBean.getLsClient(); 
            String tsRfc = toParBean.getLsRfc();
            String tsLugarFecha = toParBean.getLsLugar();
            String tsEmail = toParBean.getLsEmail();
            String tsMonitoreados = 
                toParBean.getLiNumProvs() == null ? "0": 
                toParBean.getLiNumProvs().toString();
            String tsFecRecepcion = 
                toParBean.getLtFecRec() == null ? "Sin especificar": 
                toParBean.getLtFecRec().toString();
            String tsFecRevision = 
                toParBean.getLtFecRev() == null ? "Sin especificar": 
                toParBean.getLtFecRev().toString();
            String tsFecSat = 
                toParBean.getLtFecSat() == null ? "Sin especificar": 
                toParBean.getLtFecSat().toString();
            String tsFecDof = 
                toParBean.getLtFecDof() == null ? "Sin especificar": 
                toParBean.getLtFecDof().toString();
            String lsFilePath = readWordOls(tsRfc, 
                                            tsContribuyente, 
                                            tsLugarFecha, 
                                            tsEmail, 
                                            tsMonitoreados, 
                                            tsFecRecepcion, 
                                            tsFecRevision, 
                                            tsFecSat, 
                                            tsFecDof
                                            );
            
            String lsBody = "<br>"+getMailBody(toParBean.getLsClient(), 
                                                   toParBean.getLsRfc(), 
                                                   toParBean.getLsYear(), 
                                                   toParBean.getLsMonth(), 
                                                   toParBean.getLsTypes()
                                                   )+"<b>OLS</b><br>";
            
            lsBody += "<b>Lic. Julieta Jaramillo<b>";
            
            //Adjuntar Archivo,  si funciona
            BodyPart texto = new MimeBodyPart();
            texto.setContent(lsBody, "text/html");
            
            
            BodyPart loAdjunto = new MimeBodyPart();
            //adjunto.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\JorgeOWM\\Desktop\\casca\\DamaraR_01.jpg")));
            loAdjunto.setDataHandler(new DataHandler(new FileDataSource(lsFilePath)));
            //adjunto.setDataHandler(new DataHandler(new FileDataSource(tsFilePath)));            
            loAdjunto.setFileName(tsRfc+".docx");
            
            BodyPart loAdjuntoPdf = new MimeBodyPart();
            loAdjuntoPdf.setDataHandler(new DataHandler(new FileDataSource(toParBean.getLsRutaPdf())));
            loAdjuntoPdf.setFileName(tsRfc+"-Detail.pdf");
            
            MimeMultipart miltiParte = new MimeMultipart();
            miltiParte.addBodyPart(texto);
            miltiParte.addBodyPart(loAdjunto);
            miltiParte.addBodyPart(loAdjuntoPdf);
            
            loMessage.setContent(miltiParte);
            //loMessage.setText(lsBody, "ISO-8859-1", "html");
            
            Transport loTransport = loSession.getTransport("smtp");
            loTransport.connect(loMailHeaderBean.getLsSenderMail(), loMailHeaderBean.getLsSenderPassword());
            loTransport.sendMessage(loMessage, loMessage.getRecipients(Message.RecipientType.TO));
            loTransport.close();
            System.out.println("sent"); 
        } catch (MessagingException e) {
            System.out.println("ERROR AL ENVIAR MAIL: "+e.getMessage());
            throw e;
        }
    }
    
    public Properties getProperties(){
        Properties loPrps = new Properties();
        loPrps.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");        
        loPrps.setProperty("mail.transport.protocol", "smtp");
        loPrps.setProperty("mail.smtp.host", "smtp.gmail.com");
        loPrps.setProperty("mail.smtp.starttls.enable", "true");
        loPrps.setProperty("mail.smtp.port", "587");
        loPrps.setProperty("mail.smtp.auth", "true");
        return loPrps;
    }
    
    public MailHeaderBean getMailHeaderBean(String tsClient,
                                            String tsRfc,
                                            String tsYear,
                                            String tsMonth
                                           ){
        boolean                    lbProcess = true;
        MailHeaderBean             loMailHeaderBean = new MailHeaderBean();
        UtilsOls                   loUtilsOls = new UtilsOls();
        ViewObjectDao              loViewObjectDao = new ViewObjectDao();
        List<OlsCatConfigParamTab> laListPrms = 
            loViewObjectDao.getGeneralParametersByUsedBy("SIMPLE_MAIL_HDR");
        if(laListPrms.size() > 0){
            for(OlsCatConfigParamTab loOlsCatConfigParamTab : laListPrms){
                if(loOlsCatConfigParamTab.getLsNomParameter().equalsIgnoreCase("MailRemitente")){                    
                    if(loOlsCatConfigParamTab.getLsIndValueParameter() != null){
                        loMailHeaderBean.setLsSenderMail(loOlsCatConfigParamTab.getLsIndValueParameter());
                    }else{
                        lbProcess =  false;
                    }
                }
                if(loOlsCatConfigParamTab.getLsNomParameter().equalsIgnoreCase("MailPassword")){
                    //Desecnriptar password                    
                    if(loOlsCatConfigParamTab.getLsIndValueParameter() != null){
                        String lsPwdDecrypted = null;
                        try {
                            lsPwdDecrypted =
                                loUtilsOls.decryptObject(loOlsCatConfigParamTab.getLsIndValueParameter(),
                                                         loUtilsOls.getKeyCoder());
                        } catch (Exception e) {
                            ;
                        }                        
                        loMailHeaderBean.setLsSenderPassword(lsPwdDecrypted);
                    }else{
                        lbProcess =  false;
                    }
                    
                }
                if(loOlsCatConfigParamTab.getLsNomParameter().equalsIgnoreCase("MailSubject")){
                    if(loOlsCatConfigParamTab.getLsIndValueParameter() != null){
                        loMailHeaderBean.setLsSubject(loOlsCatConfigParamTab.getLsIndValueParameter());
                    }else{
                        lbProcess =  false;
                    }
                    
                }
            }
        }else{
            lbProcess = false;
        }
        List<OlsCatSatMailsCtesBean> laListDest = 
            loViewObjectDao.getEmailDestinyByRfc(tsRfc);
        
        if(laListDest.size() > 0){
            List<String> laStr = new ArrayList<String>();
            for(OlsCatSatMailsCtesBean loOlsCatMailsCtes : laListDest){                
                laStr.add(loOlsCatMailsCtes.getIndEmail());
            }
            loMailHeaderBean.setLsReceiverMail(laStr);
        }else{
            lbProcess = false;
        }
        
        return loMailHeaderBean;
    }
    
    
    public String getMailBody(String tsClient,
                              String tsRfc,
                              String tsYear,
                              String tsMonth,
                              String tsTypes
                            ){
        String lsReturn = "";
        ViewObjectDao              loViewObjectDao = new ViewObjectDao();
        List<OlsCatConfigParamTab> laListPrms = 
            loViewObjectDao.getGeneralParametersByUsedBy("SIMPLE_MAIL_HDR");
        if(laListPrms.size() > 0){
            for(OlsCatConfigParamTab loOlsCatConfigParamTab : laListPrms){
                if(loOlsCatConfigParamTab.getLsNomParameter().equalsIgnoreCase("MailMessage")){
                    if(loOlsCatConfigParamTab.getLsIndValueParameter() != null){
                        lsReturn = loOlsCatConfigParamTab.getLsIndValueParameter().replace(":p_client", tsClient);
                        lsReturn = lsReturn.replace(":p_rfc", tsRfc);
                        lsReturn = lsReturn.replace(":lsTypes", tsTypes);
                    }
                }
            }
        }
        return lsReturn;
    }
   
    public HttpServletResponse getResponse() {
        return (HttpServletResponse)getFacesContext().getExternalContext().getResponse();
    }
    public static FacesContext getFacesContext() {
       return FacesContext.getCurrentInstance();
    }
    public ServletContext getContext() {
        return (ServletContext)getFacesContext().getExternalContext().getContext();
    }
    private Connection getConnection() throws Exception {
        try {
            InitialContext  loInitialContext = new InitialContext();            
            DataSource      loDtSrc = 
                (DataSource)loInitialContext.lookup("java:comp/env/jdbc/legalSolutionsDS");
            Connection      loCnn = loDtSrc.getConnection();
            return loCnn;
        } catch (JboException loEx) {
            loEx.printStackTrace();
            return null;
        }
    }   
    
    public void runReport(String tsRepPath, 
                          java.util.Map tsMapParams,
                          String tsFileName) throws Exception {
        Connection loCnn = null;
        try {
            ServletContext        loContext = getContext();
            InputStream           loInputStream =
                loContext.getResourceAsStream("/reports/" + tsRepPath);
            JasperReport          loTemplate = 
                (JasperReport)JRLoader.loadObject(loInputStream);
            
            loTemplate.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            loCnn = getConnection();
            JasperPrint           loPrint =
                JasperFillManager.fillReport(loTemplate, tsMapParams, loCnn);
            ByteArrayOutputStream loBaos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(loPrint, loBaos);
            
            try{
                String lsReturn = "filesOls\\OlsDetallado.docx";
                FileOutputStream fos = new FileOutputStream(lsReturn);
                JasperExportManager.exportReportToPdfStream(loPrint, fos);        
                fos.close();                
            }catch(IOException ex) {
                System.out.println(ex.getMessage());                
            }
        } catch (Exception loExJsr) {
            System.out.println("ERROR EN el JASPER: "+loExJsr.getMessage());
            loExJsr.printStackTrace();
        } finally {
            close(loCnn);
        }
    }
    
    public void close(Connection toCnn) {
       if (toCnn != null) {
           try {
               toCnn.close();
           } catch (Exception loEx) {
               ;
           }
       }
    }
    
    public String readWordOls(String tsRfc, 
                              String tsClient,
                              String lsLugaryFecha,
                              String tsEmail,
                              String liNumProvs,
                              String lsFecRec,
                              String lsFecRev,
                              String lsFecSat,
                              String lsFecDof
                            ){
            //Guardar la ruta y recuerden que se debe poner doble barra \\
            ViewObjectDao loViewObjectDao = new ViewObjectDao();
            List<OlsCatConfigParamTab> laOlsCatConfigParamTab = loViewObjectDao.getGeneralParametersByNom("PathContextWL");
            
            //String lsRuta = "C:\\Users\\JorgeOWM\\.jdeveloper\\system12.1.3.0.41.140521.1008\\DefaultDomain\\filesOls\\REPORTE.docx";
            String lsRuta = 
                laOlsCatConfigParamTab.get(0).getLsIndValueParameter() + 
                "filesOls\\REPORTE.docx";
            String lsReturn = "";
            //Se crea el objeto File con la ruta del archivo
            File archivodoc = new File(lsRuta);
            FileInputStream fis;
            try {
                fis = new FileInputStream(archivodoc);
                InputStream entradaArch = fis; 
                XWPFDocument doc = new XWPFDocument(entradaArch);
                for (XWPFParagraph p : doc.getParagraphs()) {
                    List<XWPFRun> runs = p.getRuns();
                    if (runs != null) {
                        for (XWPFRun r : runs) {
                            String text = r.getText(0);
                            
                            if (text != null && text.contains("pstlugar")) {
                                text = text.replace("pstlugar", lsLugaryFecha);
                                r.setText(text, 0);
                            }
                        }
                    }
                }
                
                for (XWPFTable tbl : doc.getTables()) { 
                   for (XWPFTableRow row : tbl.getRows()) {
                      for (XWPFTableCell cell : row.getTableCells()) {
                         for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                              String text = r.getText(0);
                              if (text != null && text.contains("pstCont")) {
                                text = text.replace("pstCont", tsClient);
                                r.setText(text,0);
                              }
                                if (text != null && text.contains("pstMail")) {
                                  text = text.replace("pstMail", tsRfc);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstMon")) {
                                  text = text.replace("pstMon", liNumProvs);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstRec")) {
                                  text = text.replace("pstRec", lsFecRec);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstRev")) {
                                  text = text.replace("pstRev", lsFecRev);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstSAT")) {
                                  text = text.replace("pstSAT", lsFecSat);
                                  r.setText(text,0);
                                }
                                if (text != null && text.contains("pstDOF")) {
                                  text = text.replace("pstDOF", lsFecDof);
                                  r.setText(text,0);
                                }
                            }
                         }
                      }
                   }
                }
                try{
                    lsReturn = "filesOls\\OlsEjecutivo.docx";
                    FileOutputStream fos = new FileOutputStream(lsReturn);
                    doc.write(fos);                
                    fos.close();                
                    //doc.close();
                }catch(IOException ex) {
                    System.out.println(ex.getMessage());                
                }             
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFoundException>>>>>>>>>>>: "+e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException>>>>>>>>>>>>: "+e.getMessage());
            }
            return lsReturn;
        }
    
    public String getDatePlace(){
        String lsReturn = "Estado de México a ";
        String lsMes = "";
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016 
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014 
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 6 
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17 
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR); //169 
        int month = cal.get(Calendar.MONTH); // 5 
        int year = cal.get(Calendar.YEAR); // 2016
        if(month == 1){lsMes = "Enero";}
        if(month == 2){lsMes = "Febrero";}
        if(month == 3){lsMes = "Marzo";}
        if(month == 3){lsMes = "Marzo";}
        if(month == 3){lsMes = "Marzo";}
        if(month == 3){lsMes = "Marzo";}
        
        return lsReturn;
    }
       
}

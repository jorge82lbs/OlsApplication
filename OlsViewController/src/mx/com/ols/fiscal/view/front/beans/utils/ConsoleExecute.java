package mx.com.ols.fiscal.view.front.beans.utils;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ConsoleExecute {
    public ConsoleExecute() {
        super();
    }
    
    public static void main(String[] a){
        ConsoleExecute loConsoleExecute = new ConsoleExecute();
        loConsoleExecute.sendMailSimple();
    }
    
    public void showMessage(){
        System.out.println("bambi es un venado");
    }
    
    public void sendMailSimple() {
        try {
            Properties props = new Properties();
            
            props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.auth", "true");
            System.out.println("creando session con propiedades setteadas");
            Session session = Session.getDefaultInstance(props);
            System.out.println("creando session con propiedades setteadas.....ok");
            String correoRemitente = "jorge82lbs@gmail.com";
            String passwordRemitente = "arisbeth31.69";
            String correoReceptor = "jorge82lbs@hotmail.com";
            String asunto = "Mi primero correo en Java";
            String mensaje = "Hola<br>Este es el contenido de mi primer correo desde <b>java</b><br>";

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoRemitente));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correoReceptor));
            message.setSubject(asunto);
            message.setText(mensaje, "ISO-8859-1", "html");
            System.out.println("Instancia Transport");
            Transport t = session.getTransport("smtp");
            System.out.println("Instancia Transport...connect");
            t.connect(correoRemitente, passwordRemitente);
            System.out.println("Instancia Transport...send");
            t.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            t.close();
            System.out.println("sent");

        } catch (AddressException ex) {
            System.out.println("ERROR 01 "+ex.getMessage());
        } catch (MessagingException ex) {
            System.out.println("ERROR 02 "+ex.getMessage());
        }
    }
    
}

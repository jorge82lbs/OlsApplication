package mx.com.ols.fiscal.view.front.beans.mail;

import java.util.List;

public class MailHeaderBean {
    private String lsSenderMail;
    private String lsSenderPassword;
    private String lsSubject;
    private List<String> lsReceiverMail;

    public void setLsReceiverMail(List<String> lsReceiverMail) {
        this.lsReceiverMail = lsReceiverMail;
    }

    public List<String> getLsReceiverMail() {
        return lsReceiverMail;
    }

    public void setLsSenderMail(String lsSenderMail) {
        this.lsSenderMail = lsSenderMail;
    }

    public String getLsSenderMail() {
        return lsSenderMail;
    }

    public void setLsSenderPassword(String lsSenderPassword) {
        this.lsSenderPassword = lsSenderPassword;
    }

    public String getLsSenderPassword() {
        return lsSenderPassword;
    }
   
    public void setLsSubject(String lsSubject) {
        this.lsSubject = lsSubject;
    }

    public String getLsSubject() {
        return lsSubject;
    }
    
}

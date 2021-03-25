package mx.com.ols.fiscal.view.front.beans.types;

import java.util.Date;

public class ParReportBean {
    private String lsClient;
    private String lsRfc; 
    private String lsYear; 
    private String lsMonth; 
    private String lsTypes;
    private Integer liNumProvs;
    private Date ltFecRec;
    private Date ltFecRev;
    private Date ltFecSat;
    private Date ltFecDof;
    private String lsLugar;
    private String lsEmail;
    private String lsRutaPdf;

    public void setLsRutaPdf(String lsRutaPdf) {
        this.lsRutaPdf = lsRutaPdf;
    }

    public String getLsRutaPdf() {
        return lsRutaPdf;
    }


    public void setLsLugar(String lsLugar) {
        this.lsLugar = lsLugar;
    }

    public String getLsLugar() {
        return lsLugar;
    }

    public void setLsEmail(String lsEmail) {
        this.lsEmail = lsEmail;
    }

    public String getLsEmail() {
        return lsEmail;
    }

    public void setLsClient(String lsClient) {
        this.lsClient = lsClient;
    }

    public String getLsClient() {
        return lsClient;
    }

    public void setLsRfc(String lsRfc) {
        this.lsRfc = lsRfc;
    }

    public String getLsRfc() {
        return lsRfc;
    }

    public void setLsYear(String lsYear) {
        this.lsYear = lsYear;
    }

    public String getLsYear() {
        return lsYear;
    }

    public void setLsMonth(String lsMonth) {
        this.lsMonth = lsMonth;
    }

    public String getLsMonth() {
        return lsMonth;
    }

    public void setLsTypes(String lsTypes) {
        this.lsTypes = lsTypes;
    }

    public String getLsTypes() {
        return lsTypes;
    }

    public void setLiNumProvs(Integer liNumProvs) {
        this.liNumProvs = liNumProvs;
    }

    public Integer getLiNumProvs() {
        return liNumProvs;
    }

    public void setLtFecRec(Date ltFecRec) {
        this.ltFecRec = ltFecRec;
    }

    public Date getLtFecRec() {
        return ltFecRec;
    }

    public void setLtFecRev(Date ltFecRev) {
        this.ltFecRev = ltFecRev;
    }

    public Date getLtFecRev() {
        return ltFecRev;
    }

    public void setLtFecSat(Date ltFecSat) {
        this.ltFecSat = ltFecSat;
    }

    public Date getLtFecSat() {
        return ltFecSat;
    }

    public void setLtFecDof(Date ltFecDof) {
        this.ltFecDof = ltFecDof;
    }

    public Date getLtFecDof() {
        return ltFecDof;
    }

}

package mx.com.ols.fiscal.view.front.beans.types;

public class OlsClientUploadErrBean {
    private Integer liIdUploadErr;  
    private Integer liIdRequest;
    private Integer liIdRowExcel;  
    private String lsIndRfc;
    private String lsIndTypeClient;
    private String lsIndFileName;

    public void setLsIndFileName(String lsIndFileName) {
        this.lsIndFileName = lsIndFileName;
    }

    public String getLsIndFileName() {
        return lsIndFileName;
    }

    public void setLiIdUploadErr(Integer liIdUploadErr) {
        this.liIdUploadErr = liIdUploadErr;
    }

    public Integer getLiIdUploadErr() {
        return liIdUploadErr;
    }

    public void setLiIdRequest(Integer liIdRequest) {
        this.liIdRequest = liIdRequest;
    }

    public Integer getLiIdRequest() {
        return liIdRequest;
    }

    public void setLiIdRowExcel(Integer liIdRowExcel) {
        this.liIdRowExcel = liIdRowExcel;
    }

    public Integer getLiIdRowExcel() {
        return liIdRowExcel;
    }

    public void setLsIndRfc(String lsIndRfc) {
        this.lsIndRfc = lsIndRfc;
    }

    public String getLsIndRfc() {
        return lsIndRfc;
    }

    public void setLsIndTypeClient(String lsIndTypeClient) {
        this.lsIndTypeClient = lsIndTypeClient;
    }

    public String getLsIndTypeClient() {
        return lsIndTypeClient;
    }

    public void setLsIndDesError(String lsIndDesError) {
        this.lsIndDesError = lsIndDesError;
    }

    public String getLsIndDesError() {
        return lsIndDesError;
    }

    public void setLsIndEstatus(String lsIndEstatus) {
        this.lsIndEstatus = lsIndEstatus;
    }

    public String getLsIndEstatus() {
        return lsIndEstatus;
    }

    public void setLiIdNumCreatedBy(Integer liIdNumCreatedBy) {
        this.liIdNumCreatedBy = liIdNumCreatedBy;
    }

    public Integer getLiIdNumCreatedBy() {
        return liIdNumCreatedBy;
    }

    public void setLiIdNumLastUpdatedBy(Integer liIdNumLastUpdatedBy) {
        this.liIdNumLastUpdatedBy = liIdNumLastUpdatedBy;
    }

    public Integer getLiIdNumLastUpdatedBy() {
        return liIdNumLastUpdatedBy;
    }
    private String lsIndDesError;
    private String lsIndEstatus;
    private Integer liIdNumCreatedBy;
    private Integer liIdNumLastUpdatedBy;
}

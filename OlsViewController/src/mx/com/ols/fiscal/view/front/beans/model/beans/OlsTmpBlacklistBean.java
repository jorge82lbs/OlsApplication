package mx.com.ols.fiscal.view.front.beans.model.beans;

public class OlsTmpBlacklistBean {
    public OlsTmpBlacklistBean() {
        super();
    }
    
    private Integer liIdProvider;
    private Integer liIdClientSat;
    private String lsIndRfcClient;
    private String lsIndRfc;
    private String lsIndCompany;
    private Integer liAlleged;
    private Integer liDefinitive;
    private Integer liUnfulfilled;
    private Integer liBlacklist;


    public void setLiIdProvider(Integer liIdProvider) {
        this.liIdProvider = liIdProvider;
    }

    public Integer getLiIdProvider() {
        return liIdProvider;
    }

    public void setLiIdClientSat(Integer liIdClientSat) {
        this.liIdClientSat = liIdClientSat;
    }

    public Integer getLiIdClientSat() {
        return liIdClientSat;
    }

    public void setLsIndRfcClient(String lsIndRfcClient) {
        this.lsIndRfcClient = lsIndRfcClient;
    }

    public String getLsIndRfcClient() {
        return lsIndRfcClient;
    }

    public void setLsIndRfc(String lsIndRfc) {
        this.lsIndRfc = lsIndRfc;
    }

    public String getLsIndRfc() {
        return lsIndRfc;
    }

    public void setLsIndCompany(String lsIndCompany) {
        this.lsIndCompany = lsIndCompany;
    }

    public String getLsIndCompany() {
        return lsIndCompany;
    }

    public void setLiAlleged(Integer liAlleged) {
        this.liAlleged = liAlleged;
    }

    public Integer getLiAlleged() {
        return liAlleged;
    }

    public void setLiDefinitive(Integer liDefinitive) {
        this.liDefinitive = liDefinitive;
    }

    public Integer getLiDefinitive() {
        return liDefinitive;
    }

    public void setLiUnfulfilled(Integer liUnfulfilled) {
        this.liUnfulfilled = liUnfulfilled;
    }

    public Integer getLiUnfulfilled() {
        return liUnfulfilled;
    }

    public void setLiBlacklist(Integer liBlacklist) {
        this.liBlacklist = liBlacklist;
    }

    public Integer getLiBlacklist() {
        return liBlacklist;
    }
}

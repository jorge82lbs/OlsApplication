package mx.com.ols.fiscal.view.front.beans.types;

public class ClienteBean {
    public ClienteBean() {
        super();
    }
    private String lsIdClienteSat;
    private String lsRfc;
    private String lsRazonSocial;
    private String lsExtraData;

    public void setLsRfc(String lsRfc) {
        this.lsRfc = lsRfc;
    }

    public String getLsRfc() {
        return lsRfc;
    }

    public void setLsRazonSocial(String lsRazonSocial) {
        this.lsRazonSocial = lsRazonSocial;
    }

    public String getLsRazonSocial() {
        return lsRazonSocial;
    }

    public void setLsExtraData(String lsExtraData) {
        this.lsExtraData = lsExtraData;
    }

    public String getLsExtraData() {
        return lsExtraData;
    }


    public void setLsIdClienteSat(String lsIdClienteSat) {
        this.lsIdClienteSat = lsIdClienteSat;
    }

    public String getLsIdClienteSat() {
        return lsIdClienteSat;
    }

}

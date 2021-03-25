package mx.com.ols.fiscal.view.front.beans.types;

public class ReportRowBean {
    public ReportRowBean() {
        super();
    }
    private String lsRfcClient;
    private String lsRfcProveedor;
    private String lsRazonSocialProveedor;
    private String lsPresunto;
    private String lsDefinitivo;
    private String lsNoLocalizado;


    public void setLsRfcClient(String lsRfcClient) {
        this.lsRfcClient = lsRfcClient;
    }

    public String getLsRfcClient() {
        return lsRfcClient;
    }

    public void setLsRfcProveedor(String lsRfcProveedor) {
        this.lsRfcProveedor = lsRfcProveedor;
    }

    public String getLsRfcProveedor() {
        return lsRfcProveedor;
    }

    public void setLsRazonSocialProveedor(String lsRazonSocialProveedor) {
        this.lsRazonSocialProveedor = lsRazonSocialProveedor;
    }

    public String getLsRazonSocialProveedor() {
        return lsRazonSocialProveedor;
    }

    public void setLsPresunto(String lsPresunto) {
        this.lsPresunto = lsPresunto;
    }

    public String getLsPresunto() {
        return lsPresunto;
    }

    public void setLsDefinitivo(String lsDefinitivo) {
        this.lsDefinitivo = lsDefinitivo;
    }

    public String getLsDefinitivo() {
        return lsDefinitivo;
    }

    public void setLsNoLocalizado(String lsNoLocalizado) {
        this.lsNoLocalizado = lsNoLocalizado;
    }

    public String getLsNoLocalizado() {
        return lsNoLocalizado;
    }


}

package mx.com.ols.fiscal.view.front.beans;

import java.net.InetAddress;
import java.net.UnknownHostException;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

public class HomeBean {
    private RichOutputText lsOrlsUrl;

    public HomeBean() {
    }

    public String getOlsUrlAction() {
        String lsIp = "";
        try {
            InetAddress loIp = InetAddress.getLocalHost();
            //System.out.println("HostAddress = "+loIp.getHostAddress());
            //System.out.println("Address = "+loIp.getAddress());
            //System.out.println("CanonicalHostName = "+loIp.getCanonicalHostName());
           lsIp = "http://" + loIp.getHostAddress() + ":7101/OlsApplication/faces/indexPage";
       } catch (UnknownHostException e) {
           e.printStackTrace();
       }
        getLsOrlsUrl().setValue(lsIp);
        getLsOrlsUrl().setVisible(true);
        return null;
    }

    public void setLsOrlsUrl(RichOutputText lsOrlsUrl) {
        this.lsOrlsUrl = lsOrlsUrl;
    }

    public RichOutputText getLsOrlsUrl() {
        return lsOrlsUrl;
    }

}

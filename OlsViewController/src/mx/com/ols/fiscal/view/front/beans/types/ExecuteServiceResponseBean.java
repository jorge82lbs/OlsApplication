package mx.com.ols.fiscal.view.front.beans.types;

public class ExecuteServiceResponseBean {
    public ExecuteServiceResponseBean() {
        super();
    }
    
    private String lsColor;
    private String lsMessage;


    public void setLsColor(String lsColor) {
        this.lsColor = lsColor;
    }

    public String getLsColor() {
        return lsColor;
    }

    public void setLsMessage(String lsMessage) {
        this.lsMessage = lsMessage;
    }

    public String getLsMessage() {
        return lsMessage;
    }

}

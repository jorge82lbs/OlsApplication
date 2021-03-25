package mx.com.ols.fiscal.view.front.beans.model.beans;

public class ResponseBean {
    private String lsResponse;
    private String lsType;
    private String lsMessageResponse;

    public void setLsResponse(String lsResponse) {
        this.lsResponse = lsResponse;
    }

    public String getLsResponse() {
        return lsResponse;
    }

    public void setLsType(String lsType) {
        this.lsType = lsType;
    }

    public String getLsType() {
        return lsType;
    }

    public void setLsMessageResponse(String lsMessageResponse) {
        this.lsMessageResponse = lsMessageResponse;
    }

    public String getLsMessageResponse() {
        return lsMessageResponse;
    }
}

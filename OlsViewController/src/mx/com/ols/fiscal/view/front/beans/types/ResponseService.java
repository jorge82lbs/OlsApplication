package mx.com.ols.fiscal.view.front.beans.types;

public class ResponseService {
    public ResponseService() {
        super();
    }
    private Integer liIdRequest;
    private Integer liIdService;
    private Integer liIdUser;
    private String lsUserName;
    private String lsMessageResponse;
    private String lsServiceType;


    public void setLiIdRequest(Integer liIdRequest) {
        this.liIdRequest = liIdRequest;
    }

    public Integer getLiIdRequest() {
        return liIdRequest;
    }

    public void setLiIdService(Integer liIdService) {
        this.liIdService = liIdService;
    }

    public Integer getLiIdService() {
        return liIdService;
    }

    public void setLiIdUser(Integer liIdUser) {
        this.liIdUser = liIdUser;
    }

    public Integer getLiIdUser() {
        return liIdUser;
    }

    public void setLsUserName(String lsUserName) {
        this.lsUserName = lsUserName;
    }

    public String getLsUserName() {
        return lsUserName;
    }

    public void setLsMessageResponse(String lsMessageResponse) {
        this.lsMessageResponse = lsMessageResponse;
    }

    public String getLsMessageResponse() {
        return lsMessageResponse;
    }

    public void setLsServiceType(String lsServiceType) {
        this.lsServiceType = lsServiceType;
    }

    public String getLsServiceType() {
        return lsServiceType;
    }

}

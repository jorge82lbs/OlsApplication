package mx.com.ols.fiscal.view.front.beans.types;

public class ProcessServiceBean {
    public ProcessServiceBean() {
        super();
    }
    
    private Integer liIdUser;
    private String lsUserName;
    private String lsIdService;
    private String lsServiceToInvoke;
    private String lsServiceAction;
    private String lsIdTrigger;
    private String lsServiceName;
    private String lsTypeProcess;


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

    public void setLsIdService(String lsIdService) {
        this.lsIdService = lsIdService;
    }

    public String getLsIdService() {
        return lsIdService;
    }

    public void setLsServiceToInvoke(String lsServiceToInvoke) {
        this.lsServiceToInvoke = lsServiceToInvoke;
    }

    public String getLsServiceToInvoke() {
        return lsServiceToInvoke;
    }

    public void setLsServiceAction(String lsServiceAction) {
        this.lsServiceAction = lsServiceAction;
    }

    public String getLsServiceAction() {
        return lsServiceAction;
    }

    public void setLsIdTrigger(String lsIdTrigger) {
        this.lsIdTrigger = lsIdTrigger;
    }

    public String getLsIdTrigger() {
        return lsIdTrigger;
    }

    public void setLsServiceName(String lsServiceName) {
        this.lsServiceName = lsServiceName;
    }

    public String getLsServiceName() {
        return lsServiceName;
    }

    public void setLsTypeProcess(String lsTypeProcess) {
        this.lsTypeProcess = lsTypeProcess;
    }

    public String getLsTypeProcess() {
        return lsTypeProcess;
    }

}

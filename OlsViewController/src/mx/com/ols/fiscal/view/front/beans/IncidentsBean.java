package mx.com.ols.fiscal.view.front.beans;

import java.util.Map;

import javax.faces.event.ActionEvent;

import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

public class IncidentsBean {
    private RichInputNumberSpinbox poFilterYear;
    private RichSelectOneChoice poFilterMonth;
    private RichInputText poFilterRfc;
    private RichTable poTblMain;
    private RichInputText poFilterRequest;
    String              gsAmDef = "mx.com.ols.model.OlsAppModuleImpl";
    String              gsConfig = "OlsAppModuleLocal";
    String              lsEntityView = "OlsTrxSatUploadErrTabView1";
    String              lsEntityIterator = "OlsTrxSatUploadErrTabView1Iterator";

    String              lsAppModuleDataControl = "OlsAppModuleDataControl";
    Map goSessionScope = ADFContext.getCurrent().getSessionScope();
    String gsUserName = (String)goSessionScope.get("loggedOlsUser");
    String gsIdUser = (String)goSessionScope.get("loggedOlsIdUser");
    Integer liIdUser = Integer.parseInt(gsIdUser);
    private RichInputText poFilterType;

    public IncidentsBean() {
    }

    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterMonth().setValue(null);
        getPoFilterRequest().setValue(null);
        getPoFilterRfc().setValue(null);
        getPoFilterType().setValue(null);
        getPoFilterYear().setValue(null);
    }

    public String refreshMainTable() {
        new UtilFaces().refreshTableWhereIterator("1 = 1", lsEntityIterator, getPoTblMain());
        return null;
    }

    public void setPoFilterYear(RichInputNumberSpinbox poFilterYear) {
        this.poFilterYear = poFilterYear;
    }

    public RichInputNumberSpinbox getPoFilterYear() {
        return poFilterYear;
    }

    public void setPoFilterMonth(RichSelectOneChoice poFilterMonth) {
        this.poFilterMonth = poFilterMonth;
    }

    public RichSelectOneChoice getPoFilterMonth() {
        return poFilterMonth;
    }

    public void setPoFilterRfc(RichInputText poFilterRfc) {
        this.poFilterRfc = poFilterRfc;
    }

    public RichInputText getPoFilterRfc() {
        return poFilterRfc;
    }

    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        String lsRequest = 
            getPoFilterRequest().getValue() == null ? "": 
            getPoFilterRequest().getValue().toString();        
        if(!lsRequest.equalsIgnoreCase("")){
            lsQuery += " AND ID_REQUEST = " + lsRequest + "";
        }
        
        String lsRfc = 
            getPoFilterRfc().getValue() == null ? "": 
            getPoFilterRfc().getValue().toString();        
        if(!lsRfc.equalsIgnoreCase("")){
            lsQuery += " AND IND_RFC LIKE '" + lsRfc + "%'";
        }
        
        String lsType = 
            getPoFilterType().getValue() == null ? "": 
            getPoFilterType().getValue().toString();        
        if(!lsType.equalsIgnoreCase("")){
            lsQuery += " AND IND_TYPE_CLIENT LIKE '" + lsType + "%'";
        }
        
        new UtilFaces().refreshTableWhereIterator(lsQuery, lsEntityIterator, getPoTblMain());      
        return null;
    }

    public void setPoTblMain(RichTable poTblMain) {
        this.poTblMain = poTblMain;
    }

    public RichTable getPoTblMain() {
        return poTblMain;
    }

    public void setPoFilterRequest(RichInputText poFilterRequest) {
        this.poFilterRequest = poFilterRequest;
    }

    public RichInputText getPoFilterRequest() {
        return poFilterRequest;
    }

    public void setPoFilterType(RichInputText poFilterType) {
        this.poFilterType = poFilterType;
    }

    public RichInputText getPoFilterType() {
        return poFilterType;
    }
}

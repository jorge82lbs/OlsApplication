package mx.com.ols.fiscal.view.front.beans;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import mx.com.ols.fiscal.view.front.beans.types.MapBean;
import mx.com.ols.fiscal.view.front.beans.utils.UtilFaces;

import mx.com.ols.fiscal.view.front.beans.utils.UtilsOls;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

public class ReportPeriodBean {
    private RichInputNumberSpinbox poFilterYear;
    private RichSelectOneChoice poFilterMonth;
    private RichInputText poFilterRfc;
    private RichInputText poFilterCliente;
    private RichSelectBooleanCheckbox poFilterChkClean;
    private RichSelectBooleanCheckbox poFilterChkPre;
    private RichSelectBooleanCheckbox poFilterChkDef;
    private RichSelectBooleanCheckbox poFilterChkInc;
    private String poValueYear;
    
    private String poValueMonth;
    String gsEntityIterator = "OlsSatClientsByPeriodVw1Iterator";
    private RichTable poTblMain;

    public ReportPeriodBean() {
    }

    public void resetFilterValues(ActionEvent toActionEvent) {
        toActionEvent.getPhaseId();
        getPoFilterChkClean().setValue(false);
        getPoFilterChkDef().setValue(false);
        getPoFilterChkInc().setValue(false);
        getPoFilterChkPre().setValue(false);
        getPoFilterCliente().setValue("");
        getPoFilterRfc().setValue("");
        getPoFilterMonth().setValue("");
        getPoFilterYear().setValue("");
    }

    public String refreshMainTable() {
        String lsQuery = "1 = 1";
        Calendar fecha = Calendar.getInstance();
        int liAnio = fecha.get(Calendar.YEAR);
        int liMes = fecha.get(Calendar.MONTH) + 1;
        String lsYear = String.valueOf(liAnio);
        String lsMonth = getNomMes(liMes);                
        
        List<MapBean> laParams = new ArrayList<MapBean>();
        MapBean loMapBeanYear = new MapBean();
        loMapBeanYear.setLsId("lstAnio");
        loMapBeanYear.setLsValue(lsYear);
        MapBean loMapBeanMonth = new MapBean();
        loMapBeanMonth.setLsId("lstMes");
        loMapBeanMonth.setLsValue(lsMonth);
        laParams.add(loMapBeanYear);
        laParams.add(loMapBeanMonth);
        new UtilFaces().refreshTableWherePrmIterator(lsQuery, 
                                                  gsEntityIterator, 
                                                  getPoTblMain(),
                                                  laParams
                                                  );
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

    public void setPoFilterCliente(RichInputText poFilterCliente) {
        this.poFilterCliente = poFilterCliente;
    }

    public RichInputText getPoFilterCliente() {
        return poFilterCliente;
    }

    public void setPoFilterChkClean(RichSelectBooleanCheckbox poFilterChkClean) {
        this.poFilterChkClean = poFilterChkClean;
    }

    public RichSelectBooleanCheckbox getPoFilterChkClean() {
        return poFilterChkClean;
    }

    public void selectCleanAction(ValueChangeEvent toValueChangeEvent) {
        String lsSel = toValueChangeEvent.getNewValue() == null ? null:
                       toValueChangeEvent.getNewValue().toString();
        if(lsSel.equalsIgnoreCase("true")){
            getPoFilterChkDef().setValue(false);
            getPoFilterChkInc().setValue(false);
            getPoFilterChkPre().setValue(false);
        }
    }
    
    public void setValueCleanCheckbox(ValueChangeEvent toValueChangeEvent){
        String lsSel = toValueChangeEvent.getNewValue() == null ? null:
                       toValueChangeEvent.getNewValue().toString();
        if(lsSel.equalsIgnoreCase("true")){
            getPoFilterChkClean().setValue(false);
        }
    }

    public void setCleanChecBox3Action(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkPre(RichSelectBooleanCheckbox poFilterChkPre) {
        this.poFilterChkPre = poFilterChkPre;
    }

    public RichSelectBooleanCheckbox getPoFilterChkPre() {
        return poFilterChkPre;
    }

    public void setCleanChecBox2Action(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkDef(RichSelectBooleanCheckbox poFilterChkDef) {
        this.poFilterChkDef = poFilterChkDef;
    }

    public RichSelectBooleanCheckbox getPoFilterChkDef() {
        return poFilterChkDef;
    }

    public void setCleanChecBoxAction(ValueChangeEvent valueChangeEvent) {
        setValueCleanCheckbox(valueChangeEvent);
    }

    public void setPoFilterChkInc(RichSelectBooleanCheckbox poFilterChkInc) {
        this.poFilterChkInc = poFilterChkInc;
    }

    public RichSelectBooleanCheckbox getPoFilterChkInc() {
        return poFilterChkInc;
    }

    public String searchFilterAction() {
        String lsQuery = " 1 = 1 ";
        
        String lsYear = 
            getPoFilterYear().getValue() == null ? "": 
            getPoFilterYear().getValue().toString();    
        //System.out.println("lsYear: "+lsYear);
        /*
        if(!lsYear.equalsIgnoreCase("")){
            lsQuery += " AND IND_YEAR_PRM = " + lsYear + "";
        }
        */
        String lsMonth = 
            getPoFilterMonth().getValue() == null ? "": 
            getPoFilterMonth().getValue().toString(); 
        //System.out.println("lsMonth: "+lsMonth);
        /*
        if(!lsMonth.equalsIgnoreCase("")){
            lsQuery += " AND IND_MONTH_PRM = '" + loUtilsOls.getNumMonth(lsMonth) + "'";
        }
        */
        String lsFilterRfc = 
            getPoFilterRfc().getValue() == null ? "": 
            getPoFilterRfc().getValue().toString();        
        if(!lsFilterRfc.equalsIgnoreCase("")){
            lsQuery += " AND IND_RFC = '" + lsFilterRfc + "'";
        }
                
        String lsFilterCliente = 
            getPoFilterCliente().getValue() == null ? "": 
            getPoFilterCliente().getValue().toString();        
        if(!lsFilterCliente.equalsIgnoreCase("")){
            lsQuery += " AND IND_COMPANY = '" + lsFilterCliente + "'";
        }
        
        String lsFilterClean = 
                getPoFilterChkClean().getValue() == null ? "": 
                getPoFilterChkClean().getValue().toString();   
        String lsValClean = "0";
        if(!lsFilterClean.equalsIgnoreCase("")){
            if(lsFilterClean.equalsIgnoreCase("true")){
                lsValClean = "1";
                lsQuery += " AND ALLEGED = 0 AND DEFINITIVE = 0 AND UNFULFILLED = 0 ";
            }            
        }
        if(lsValClean.equalsIgnoreCase("0")){
            String lsFilterInc = 
                getPoFilterChkInc().getValue() == null ? "": 
                getPoFilterChkInc().getValue().toString();        
            if(!lsFilterInc.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterInc.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND UNFULFILLED = " + lsVal;
                }
                
            }
            String lsFilterDfn = 
                getPoFilterChkDef().getValue() == null ? "": 
                getPoFilterChkDef().getValue().toString();        
            if(!lsFilterDfn.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterDfn.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND DEFINITIVE = " + lsVal;
                }
                
            }
            
            String lsFilterPre = 
                    getPoFilterChkPre().getValue() == null ? "": 
                    getPoFilterChkPre().getValue().toString();        
            if(!lsFilterPre.equalsIgnoreCase("")){
                String lsVal = "0";
                if(lsFilterPre.equalsIgnoreCase("true")){
                    lsVal = "1";
                    lsQuery += " AND ALLEGED = "+lsVal;
                }
                
            }
        }
        //System.out.println("lsQuery: "+lsQuery);
        List<MapBean> laParams = new ArrayList<MapBean>();
        MapBean loMapBeanYear = new MapBean();
        loMapBeanYear.setLsId("lstAnio");
        loMapBeanYear.setLsValue(lsYear);
        MapBean loMapBeanMonth = new MapBean();
        loMapBeanMonth.setLsId("lstMes");
        loMapBeanMonth.setLsValue(lsMonth);
        laParams.add(loMapBeanYear);
        laParams.add(loMapBeanMonth);
        
        new UtilFaces().refreshTableWherePrmIterator(lsQuery, 
                                                  gsEntityIterator, 
                                                  getPoTblMain(),
                                                  laParams
                                                  );
        return null;
    }

    public void setPoTblMain(RichTable poTblMain) {
        this.poTblMain = poTblMain;
    }
    
    public void setPoValueYear(String poValueYear) {
        this.poValueYear = poValueYear;
    }

    public String getPoValueYear() {
        Calendar fecha = Calendar.getInstance();
        int liAnio = fecha.get(Calendar.YEAR);
        String lsYear = String.valueOf(liAnio);        
        return lsYear;//poValueYear;
    }

    public void setPoValueMonth(String poValueMonth) {
        this.poValueMonth = poValueMonth;
    }

    public String getPoValueMonth() {
        Calendar fecha = Calendar.getInstance();
        int liMes = fecha.get(Calendar.MONTH) + 1;
        String lsMonth = getNomMes(liMes);     
        return lsMonth;//poValueMonth;
    }

    public RichTable getPoTblMain() {
        return poTblMain;
    }
    
    String getNomMes(int liNumMes){
        String lsReturn = "";
        if(liNumMes == 1){
            lsReturn = "ENERO";
        }
        if(liNumMes == 2){
            lsReturn = "FEBRERO";
        }
        if(liNumMes == 3){
            lsReturn = "MARZO";
        }
        if(liNumMes == 4){
            lsReturn = "ABRIL";
        }
        if(liNumMes == 5){
            lsReturn = "MAYO";
        }
        if(liNumMes == 6){
            lsReturn = "JUNIO";
        }
        if(liNumMes == 7){
            lsReturn = "JULIO";
        }
        if(liNumMes == 8){
            lsReturn = "AGOSTO";
        }
        if(liNumMes == 9){
            lsReturn = "SEPTIEMBRE";
        }
        if(liNumMes == 10){
            lsReturn = "OCTUBRE";
        }
        if(liNumMes == 11){
            lsReturn = "NOVIEMBRE";
        }
        if(liNumMes == 12){
            lsReturn = "DICIEMBRE";
        }
        return lsReturn;
    }
}

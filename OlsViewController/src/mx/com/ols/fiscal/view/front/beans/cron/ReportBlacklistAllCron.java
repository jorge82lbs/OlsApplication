package mx.com.ols.fiscal.view.front.beans.cron;

import java.util.Date;

import mx.com.ols.fiscal.view.front.beans.ReportBlacklistAllService;
import mx.com.ols.fiscal.view.front.beans.model.daos.ViewObjectDao;

import mx.com.ols.fiscal.view.front.beans.types.BasicInputParameters;

import mx.com.ols.fiscal.view.front.beans.types.ResponseService;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReportBlacklistAllCron implements Job{
    public ReportBlacklistAllCron() {
        super();
    }

    @Override
    public void execute(JobExecutionContext toJobExecutionContext) throws JobExecutionException {
        System.out.println("Ejecucion de Cron >>  ReportBlacklistAllCron ["+new Date()+"]");
        JobDataMap                loDataMap = toJobExecutionContext.getJobDetail().getJobDataMap();
        String                    lsIdService = loDataMap.getString("lsIdService");  
        String                    lsUserName = loDataMap.getString("lsUserName");
        String                    lsIdUser = loDataMap.getString("liIdUser");
        String                    lsTypeProcess = loDataMap.getString("lsTypeProcess");
        Integer                            liIdRequest = 
            new ViewObjectDao().getIdRequestSq();
        
        ReportBlacklistAllService loServiceAction = new ReportBlacklistAllService();
        
        BasicInputParameters loInput = new BasicInputParameters();
        loInput.setLiIdRequest(liIdRequest);
        loInput.setLiIdService(Integer.parseInt(lsIdService));
        loInput.setLiIdUser(Integer.parseInt(lsIdUser));
        loInput.setLsMessage("RONDA-BOLO-RAYUELAS-TIGUE");
        loInput.setLsUserName(lsUserName);
        loInput.setLsServiceType(lsTypeProcess);
        System.out.println("Ejecutando desde de Cron ");
        ResponseService loPits = loServiceAction.executeServiceAction(loInput);
        
        System.out.println("ID["+liIdRequest+"]Finish de Cron >> ["+loPits.getLsMessageResponse()+"]");
    }
}

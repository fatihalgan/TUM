/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;



/**
 *
 * @author db2admin
 */
public class DailySalesInfoEmailsTask extends QuartzJobBean {

    private IDealerService dealerService;
    
    public DailySalesInfoEmailsTask() {
        super();
    }
    
    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        dealerService.sendDailySalesInfoEmails();
    }

    
}

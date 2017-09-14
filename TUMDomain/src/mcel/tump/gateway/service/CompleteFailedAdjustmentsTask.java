/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service;

import mcel.tump.account.service.IAccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author db2admin
 */
public class CompleteFailedAdjustmentsTask extends QuartzJobBean {

    private IAccountService accountService;
    private static final Log logger = LogFactory.getLog(CompleteFailedAdjustmentsTask.class);

    public CompleteFailedAdjustmentsTask() {
        super();
    }

    public IAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        logger.debug("Complete failed adjustments task has started...");
        accountService.processFailedAdjustments();
        logger.debug("Complete failed adjustments task has finished...");
    }
}

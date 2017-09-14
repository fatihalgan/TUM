package mcel.tump.account.service;

import java.util.ArrayList;
import java.util.List;

import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.notification.service.INotificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TotalHourlySalesTask extends QuartzJobBean {

	private static final Log logger = LogFactory.getLog(TotalHourlySalesTask.class);
	
	private IAccountService accountService;
	private INotificationService notificationService;
	private List<String> notificationList = new ArrayList<String>();
	
	public TotalHourlySalesTask() {
		super();
		notificationList.add("falgan@mcel.co.mz");
	}
	
	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}
	
	public INotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(INotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public List<String> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<String> notificationList) {
		this.notificationList = notificationList;
	}
	
	@Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		logger.debug("Querying the total hourly sales by voucher type...");
		TotalHourlySalesByVoucherType sales = getAccountService().getHourlySalesByVoucherType();
		logger.debug("Total hourly sales by voucher type resultset received: " + sales.getSales().size() + " records found...");
		try {
			getNotificationService().sendHourlySalesReportMail(sales, getNotificationList());
		} catch(Exception e) {
			logger.error("Cannot send the report to the recipients..." + e.getMessage());
		}
	}
}

package mcel.tump.account.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.notification.service.INotificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MonthlySalesTask extends QuartzJobBean {

	private static final Log logger = LogFactory.getLog(MonthlySalesTask.class);
	
	private IAccountService accountService;
	private INotificationService notificationService;
	private List<String> notificationList = new ArrayList<String>();
	private ByteArrayOutputStream reportContent;
	private WritableWorkbook workbook;
	
	public MonthlySalesTask() {
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
	
	public byte[] getReportFile() {
		try {
			workbook.write();
			workbook.close();
			return reportContent.toByteArray();
		} catch(Exception e) {
			logger.error("Could not write the excel workbook contents to stream...");
		}
		return null;
	}
	
	private void prepareExcelWorkbook() {
		try {
			reportContent = new ByteArrayOutputStream(8192);
			workbook = Workbook.createWorkbook(reportContent);
		} catch(Exception e) {
			logger.error("Could not create the excel workbook for Total Daily Sales..." + e.getMessage());
		}
	}
	
	@Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		logger.debug("Querying the total monthly sales by dealer...");
		TotalMonthlySalesByDealer sales = getAccountService().getMonthlySalesByDealer(cal.getTime());
		sales.setEndDate(cal.getTime());
		cal.add(Calendar.MONTH, -1);
		sales.setDate(cal.getTime());
		logger.debug("Total monthly sales by dealer resultset received: " + sales.getSales().size() + " records found...");
		prepareExcelWorkbook();
		TotalMonthlySalesByDealerSheetProducer totalMonthlySalesByDealer = new TotalMonthlySalesByDealerSheetProducer();
		totalMonthlySalesByDealer.setData(sales);
		totalMonthlySalesByDealer.produceSheet(workbook);
		
		byte[] reportContent = getReportFile();
		try {
			getNotificationService().sendMonthlySalesReportMail(reportContent, sales.getDate(), sales.getEndDate(), getNotificationList());
		} catch(Exception e) {
			logger.error("Cannot send the report to the recipients..." + e.getMessage());
		}
	}
}

package mcel.tump.account.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.notification.service.INotificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TotalDailySalesTask extends QuartzJobBean {

	private static final Log logger = LogFactory.getLog(TotalDailySalesTask.class);
	
	private IAccountService accountService;
	private INotificationService notificationService;
	private List<String> notificationList = new ArrayList<String>();
	private ByteArrayOutputStream reportContent;
	private WritableWorkbook workbook;
	
	public TotalDailySalesTask() {
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

	@Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.DATE, -1);
		logger.debug("Querying the total daily sales by voucher type...");
		TotalDailySalesByVoucherType sales = getAccountService().getDailySalesByVoucherType(cal.getTime());
		logger.debug("Total daily sales by voucher type resultset received: " + sales.getSales().size() + " records found...");
		prepareExcelWorkbook();
		TotalDailySalesByVoucherTypeSheetProducer totalDailySalesByVoucherType = new TotalDailySalesByVoucherTypeSheetProducer();
		totalDailySalesByVoucherType.setData(sales);
		totalDailySalesByVoucherType.produceSheet(workbook);
		
		logger.debug("Querying the daily payments...");
		DailyPayments payments = getAccountService().getDailyPayments(cal.getTime());
		logger.debug("Daily payments resultset received: " + payments.getPayments().size() + " records found...");
		DailyPaymentsSheetProducer dailyPayments = new DailyPaymentsSheetProducer();
		dailyPayments.setData(payments);
		dailyPayments.produceSheet(workbook);
		
		byte[] reportContent = getReportFile();
		try {
			getNotificationService().sendDailySalesReportMail(reportContent, cal.getTime(), getNotificationList());
		} catch(Exception e) {
			logger.error("Cannot send the report to the recipients..." + e.getMessage());
		}
    }
	
	private void prepareExcelWorkbook() {
		try {
			reportContent = new ByteArrayOutputStream(8192);
			workbook = Workbook.createWorkbook(reportContent);
		} catch(Exception e) {
			logger.error("Could not create the excel workbook for Total Daily Sales..." + e.getMessage());
		}
	}
	
		
	
	
	
}

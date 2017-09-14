package mcel.tump.account.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

import mcel.tump.account.domain.AccpacPayment;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.SaleByVoucherType;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TotalDailySalesExcelProducerTestCase extends TestCase {

	private List<SaleByVoucherType> salesData = new ArrayList<SaleByVoucherType>();
	private List<AccpacPayment> paymentData = new ArrayList<AccpacPayment>();
	private TotalDailySalesByVoucherTypeSheetProducer producer = new TotalDailySalesByVoucherTypeSheetProducer();
	private DailyPaymentsSheetProducer paymentProducer = new DailyPaymentsSheetProducer();
	private ByteArrayOutputStream reportContent;
	private WritableWorkbook workbook;
	
	private void prepareExcelWorkbook() {
		try {
			reportContent = new ByteArrayOutputStream(8192 * 2);
			workbook = Workbook.createWorkbook(reportContent);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    public byte[] getReportFile() {
		try {
			workbook.write();
			workbook.close();
			return reportContent.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    @Before
    public void setUp() {
    	prepareExcelWorkbook();
    	salesData.add(new SaleByVoucherType(10, 12456, "CLI00000"));
    	salesData.add(new SaleByVoucherType(20, 5367, "CLI00000"));
    	salesData.add(new SaleByVoucherType(50, 400, "CLI00000"));
    	TotalDailySalesByVoucherType sales = new TotalDailySalesByVoucherType();
    	sales.setDate(new Date());
    	sales.setSales(salesData);
    	producer.setData(sales);
    	
    	paymentData.add(new AccpacPayment("CLI00000", "CLI00260", "IN900016435", "12465", 1000000l, new Date()));
    	paymentData.add(new AccpacPayment("CLI00000", "CLI00290", "IN900016437", "12469", 2000000l, new Date()));
    	paymentData.add(new AccpacPayment("CLI00000", "CLI00260", "IN900016438", "12472", 500000l, new Date()));
    	DailyPayments payments = new DailyPayments();
    	payments.setDate(new Date());
    	payments.setPayments(paymentData);
    	paymentProducer.setData(payments);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProduceExcelReport() {
    	producer.produceSheet(workbook);
    	paymentProducer.produceSheet(workbook);
    	byte[] result = getReportFile();
    	try {
    		OutputStream out = new FileOutputStream("E:\\exceltest.xls");
    		out.write(result);
    	} catch(Exception e) {
    		fail(e.getMessage());
    	}
    }

}

package mcel.tump.account.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mcel.tump.account.domain.SaleByDealer;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;

import junit.framework.TestCase;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class TotalMonthlySalesExcelProducerTestCase extends TestCase {

	private List<SaleByDealer> salesData = new ArrayList<SaleByDealer>();
	private TotalMonthlySalesByDealerSheetProducer producer = new TotalMonthlySalesByDealerSheetProducer();
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
    	salesData.add(new SaleByDealer(12456, "CLI00000"));
    	salesData.add(new SaleByDealer(20, "NMS12"));
    	salesData.add(new SaleByDealer(12, "MTS01"));
    	TotalMonthlySalesByDealer sales = new TotalMonthlySalesByDealer();
    	sales.setDate(new Date());
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(sales.getDate());
    	cal.roll(Calendar.MONTH, true);
    	sales.setEndDate(cal.getTime());
    	sales.setSales(salesData);
    	producer.setData(sales);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProduceExcelReport() {
    	producer.produceSheet(workbook);
    	byte[] result = getReportFile();
    	try {
    		OutputStream out = new FileOutputStream("E:\\exceltest.xls");
    		out.write(result);
    	} catch(Exception e) {
    		fail(e.getMessage());
    	}
    }

}

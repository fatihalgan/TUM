package mcel.tump.account.service;

import java.text.DateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jxl.DateCell;
import jxl.Range;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import mcel.tump.account.domain.AccpacPayment;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.SaleByVoucherType;
import mcel.tump.util.ExcelSheetProducer;

public class DailyPaymentsSheetProducer implements ExcelSheetProducer<DailyPayments> {

	private static final Log logger = LogFactory.getLog(DailyPaymentsSheetProducer.class);
	
	private DailyPayments data = new DailyPayments();
	
	public DailyPayments getData() {
		return data;
	}
	
	public void setData(DailyPayments data) {
		this.data = data;
	}

	public void produceSheet(WritableWorkbook workbook) {
		try {
			WritableSheet sheet = workbook.createSheet("Daily Payments Received", 1);
			
			WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
			WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
			
			Range range = sheet.mergeCells(0, 0, 6, 0);
			DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
			String strDate = format.format(data.getDate());
			
			Label label = new Label(0, 0, "TUM Daily Payments: " + strDate, headerCellFormat);
			sheet.addCell(label);
			
			label = new Label(1, 1, "Shop Code", headerCellFormat);
			sheet.addCell(label);
			label = new Label(2, 1, "Sub Dealer", headerCellFormat);
			sheet.addCell(label);
			label = new Label(3, 1, "Invoice Number", headerCellFormat);
			sheet.addCell(label);
			label = new Label(4, 1, "Shipment Number", headerCellFormat);
			sheet.addCell(label);
			label = new Label(5, 1, "Amount", headerCellFormat);
			sheet.addCell(label);
						
			for(int i = 0; i < data.getPayments().size(); i++) {
				AccpacPayment payment = data.getPayments().get(i);
				label  = new Label(1, i+2, payment.getShopName());
				sheet.addCell(label);
				label = new Label(2, i+2, payment.getSubDealer());
				sheet.addCell(label);
				label = new Label(3, i+2, payment.getInvoiceNumber());
				sheet.addCell(label);
				label = new Label(4, i+2, payment.getShipmentNumber());
				sheet.addCell(label);
				Number number = new Number(5, i+2, payment.getAmount());
				sheet.addCell(number);
			}
			
			label = new Label(0, data.getPayments().size() + 2, "Total", headerCellFormat);
			sheet.addCell(label);
			Number number = new Number(1, data.getPayments().size() + 2, data.getTotalNumberPaid(), headerCellFormat);
			sheet.addCell(number);
			
			number = new Number(5, data.getPayments().size() + 2, data.getTotalAmountPaid(), headerCellFormat);
			sheet.addCell(number);
		} catch(Exception e) {
			logger.error(e);
		}
	}
}

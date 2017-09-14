package mcel.tump.account.service;

import java.text.DateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jxl.Range;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import mcel.tump.account.domain.SaleByVoucherType;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.util.ExcelSheetProducer;

public class TotalDailySalesByVoucherTypeSheetProducer implements ExcelSheetProducer<TotalDailySalesByVoucherType> {

	private static final Log logger = LogFactory.getLog(TotalDailySalesByVoucherTypeSheetProducer.class);
	
	private TotalDailySalesByVoucherType data = new TotalDailySalesByVoucherType();
		
	public TotalDailySalesByVoucherType getData() {
		return data;
	}

	public void setData(TotalDailySalesByVoucherType data) {
		this.data = data;
	}

	public void produceSheet(WritableWorkbook workbook) {
		try {
			WritableSheet sheet = workbook.createSheet("Daily Sales By Voucher Type", 0);
			
			WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
			WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
			
			Range range = sheet.mergeCells(0, 0, 4, 0);
			DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
			String strDate = format.format(data.getDate());			
			
			Label label = new Label(0, 0, "TUM Daily Sales By Vocuher Type: " + strDate, headerCellFormat);
			sheet.addCell(label);
			
			label = new Label(1, 1, "Amount", headerCellFormat);
			sheet.addCell(label);
			label = new Label(2, 1, "Units Sold", headerCellFormat);
			sheet.addCell(label);
			label = new Label(3, 1, "Dealer Code", headerCellFormat);
			sheet.addCell(label);
		
			for(int i = 0; i < data.getSales().size(); i++) {
				SaleByVoucherType sale = data.getSales().get(i);
				Number number = new Number(1, i+2, sale.getTransAmount());
				sheet.addCell(number);
				number = new Number(2, i+2, sale.getNumberSold());
				sheet.addCell(number);
				label = new Label(3, i+2, sale.getDealerCode());
				sheet.addCell(label);
			}
			
			label = new Label(0, data.getSales().size() + 2, "Total", headerCellFormat);
			sheet.addCell(label);
			Number number = new Number(1, data.getSales().size() + 2, data.getTotalAmountSold(), headerCellFormat);
			sheet.addCell(number);
			
			number = new Number(2, data.getSales().size() + 2, data.getTotalNumberSold(), headerCellFormat);
			sheet.addCell(number);
			
		} catch(Exception e) {
			logger.error(e);
		}
		
	}
}

package mcel.tump.account.service;

import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jxl.Range;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import mcel.tump.account.domain.SaleByDealer;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.util.ExcelSheetProducer;

public class TotalMonthlySalesByDealerSheetProducer implements ExcelSheetProducer<TotalMonthlySalesByDealer> {

	private static final Log logger = LogFactory.getLog(TotalMonthlySalesByDealerSheetProducer.class);
	private TotalMonthlySalesByDealer data = new TotalMonthlySalesByDealer();
	
	@Override
	public TotalMonthlySalesByDealer getData() {
		return data;
	}

	@Override
	public void produceSheet(WritableWorkbook workbook) {
		try {
			WritableSheet sheet = workbook.createSheet("Monthly Sales By Dealer", 0);
			
			WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
			WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
			
			Range range = sheet.mergeCells(0, 0, 4, 0);
			DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
			String strDate = format.format(data.getDate());
			String strEndDate = format.format(data.getEndDate());
			
			Label label = new Label(0, 0, "TUM Monthly Sales By Dealer: " + strDate + " - " + strEndDate, headerCellFormat);
			sheet.addCell(label);
			
			label = new Label(1, 1, "Amount", headerCellFormat);
			sheet.addCell(label);
			label = new Label(2, 1, "Dealer Code", headerCellFormat);
			sheet.addCell(label);
		
			for(int i = 0; i < data.getSales().size(); i++) {
				SaleByDealer sale = data.getSales().get(i);
				Number number = new Number(1, i+2, sale.getTransAmount());
				sheet.addCell(number);
				label = new Label(2, i+2, sale.getDealerCode());
				sheet.addCell(label);
			}
			
			label = new Label(0, data.getSales().size() + 2, "Total", headerCellFormat);
			sheet.addCell(label);
			Number number = new Number(1, data.getSales().size() + 2, data.getTotalAmountSold(), headerCellFormat);
			sheet.addCell(number);
						
		} catch(Exception e) {
			logger.error(e);
		}		
	}

	@Override
	public void setData(TotalMonthlySalesByDealer data) {
		this.data = data;		
	}
	
	
}

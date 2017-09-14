package mcel.tump.util;

import jxl.write.WritableWorkbook;

public interface ExcelSheetProducer<E> {
	public void produceSheet(WritableWorkbook workbook);
	public void setData(E data);
	public E getData();
}

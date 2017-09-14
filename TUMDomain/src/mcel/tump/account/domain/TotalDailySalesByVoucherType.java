package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TotalDailySalesByVoucherType implements Serializable {

	private static final long serialVersionUID = -4510874849800519771L;
	private List<SaleByVoucherType> sales = new ArrayList<SaleByVoucherType>();
	private Date date;
	private Date endDate;
	
	public List<SaleByVoucherType> getSales() {
		return sales;
	}
	public void setSales(List<SaleByVoucherType> sales) {
		this.sales = sales;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Long getTotalNumberSold() {
		long total = 0;
		for(SaleByVoucherType sale : sales) {
			total = total + sale.getNumberSold();
		}
		return total;
	}
	
	public Long getTotalAmountSold() {
		long total = 0;
		for(SaleByVoucherType sale : sales) {
			total = total + (sale.getNumberSold() * sale.getTransAmount());
		}
		return total;
	}
}

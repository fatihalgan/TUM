package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TotalHourlySalesByVoucherType implements Serializable {

	private static final long serialVersionUID = 3142419289088326225L;
	private List<SaleByVoucherType> sales = new ArrayList<SaleByVoucherType>();
	
	public TotalHourlySalesByVoucherType() {
		super();
	}
	
	public List<SaleByVoucherType> getSales() {
		return sales;
	}
	
	public void setSales(List<SaleByVoucherType> sales) {
		this.sales = sales;
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
			total = total + (sale.getTransAmount() * sale.getNumberSold());
		}
		return total;
	}
	
	public String toString() {
		return "Total Number Sold: " + getTotalNumberSold().toString() + " -  Total Amount Sold: " + getTotalAmountSold().toString();
	}
	
}

package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TotalMonthlySalesByDealer implements Serializable {

	private static final long serialVersionUID = -6500677033401244056L;
	private List<SaleByDealer> sales = new ArrayList<SaleByDealer>();
	private Date date;
	private Date endDate;
	
	public List<SaleByDealer> getSales() {
		return sales;
	}
	public void setSales(List<SaleByDealer> sales) {
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
	
	public Long getTotalAmountSold() {
		long total = 0;
		for(SaleByDealer sale : sales) {
			total = total + (sale.getTransAmount());
		}
		return total;
	}
}

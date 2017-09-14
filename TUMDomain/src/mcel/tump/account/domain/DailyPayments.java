package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyPayments implements Serializable {

	private static final long serialVersionUID = 4986267659528350057L;
	private List<AccpacPayment> payments = new ArrayList<AccpacPayment>();
	private Date date;
	
	public List<AccpacPayment> getPayments() {
		return payments;
	}
	public void setPayments(List<AccpacPayment> payments) {
		this.payments = payments;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getTotalNumberPaid() {
		long total = 0l;
		for(AccpacPayment payment : payments) {
			total = total + 1;
		}
		return total;
	}
	
	public Long getTotalAmountPaid() {
		long total = 0l;
		for(AccpacPayment payment : payments) {
			total = total + payment.getAmount();
		}
		return total;
	}
	
	
}

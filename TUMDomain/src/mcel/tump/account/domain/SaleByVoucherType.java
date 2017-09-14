package mcel.tump.account.domain;

import java.io.Serializable;

public class SaleByVoucherType implements Serializable {

	private static final long serialVersionUID = -1029271928005310063L;
	private int transAmount;
	private int numberSold;
	private String dealerCode;
	
	public SaleByVoucherType() {
		super();
	}
	
	public SaleByVoucherType(int transAmount, int numberSold, String dealerCode) {
		this.transAmount = transAmount;
		this.numberSold = numberSold;
		this.dealerCode = dealerCode;
	}
	
	public int getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}
	public int getNumberSold() {
		return numberSold;
	}
	public void setNumberSold(int numberSold) {
		this.numberSold = numberSold;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
}

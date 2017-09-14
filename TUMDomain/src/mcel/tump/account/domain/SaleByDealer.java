package mcel.tump.account.domain;

import java.io.Serializable;

public class SaleByDealer implements Serializable {

	private static final long serialVersionUID = 2803007118945798448L;
	private int transAmount;
	private String dealerCode;
	
	public SaleByDealer() {
		super();
	}
	
	public SaleByDealer(int transAmount, String dealerCode) {
		this.transAmount = transAmount;
		this.dealerCode = dealerCode;
	}

	public int getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
	
	
}

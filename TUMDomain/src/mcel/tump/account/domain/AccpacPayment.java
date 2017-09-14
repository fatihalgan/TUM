package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.Date;

public class AccpacPayment implements Serializable {

	private static final long serialVersionUID = 9038130010833760510L;
	private String shopName;
	private String subDealer;
	private String invoiceNumber;
	private String shipmentNumber;
	private Long amount;
	private Date paymentTime;
	
	public AccpacPayment() {
		super();
	}
	
	public AccpacPayment(String shopName, String subDealer, String invoiceNumber, String shipmentNumber, Long amount, Date paymentTime) {
		this.shopName = shopName;
		this.subDealer = subDealer;
		this.invoiceNumber = invoiceNumber;
		this.shipmentNumber = shipmentNumber;
		this.amount = amount;
		this.paymentTime = paymentTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getSubDealer() {
		return subDealer;
	}

	public void setSubDealer(String subDealer) {
		this.subDealer = subDealer;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getShipmentNumber() {
		return shipmentNumber;
	}

	public void setShipmentNumber(String shipmentNumber) {
		this.shipmentNumber = shipmentNumber;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	
	
}

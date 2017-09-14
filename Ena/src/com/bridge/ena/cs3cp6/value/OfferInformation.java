package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferInformation implements Serializable {

	private static final long serialVersionUID = 5468618629083755233L;
	
	private int offerId;
	private Date startDate;
	private Date expiryDate;
	private Date startDateTime;
	private Date expiryDateTime;
	
	private List<DedicatedAccountInformation> dedicatedAccountInformation = new ArrayList<DedicatedAccountInformation>();
	
	public OfferInformation() {
		super();
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	public List<DedicatedAccountInformation> getDedicatedAccountInformation() {
		return dedicatedAccountInformation;
	}

	public void setDedicatedAccountInformation(
			List<DedicatedAccountInformation> dedicatedAccountInformation) {
		this.dedicatedAccountInformation = dedicatedAccountInformation;
	}
	
	

}

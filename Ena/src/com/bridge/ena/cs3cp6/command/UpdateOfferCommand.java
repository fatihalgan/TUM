package com.bridge.ena.cs3cp6.command;

import java.util.Date;

public class UpdateOfferCommand extends AbstractCS3CP6Command {

	private String originTransactionId;
	private Date originTimestamp;
	private String subscriberNumber;
	private Integer offerId;
	private Date startDate;
	private Integer startDateRelative;
	private Date expiryDate;
	private Integer expiryDateRelative;
	private Date startDateTime;
	private Integer startDateTimeRelative;
	private Date expiryDateTime;
	private Integer expiryDateTimeRelative;
	private Integer offerType;
	
	public UpdateOfferCommand(String originTransactionId, Date originTimestamp,
		String subscriberNumber, Integer offerId, Integer offerType,
		String subscriberNoPrefix, Integer subscriberNumberNAI) {
		super(subscriberNoPrefix, subscriberNumberNAI);
		this.originTransactionId = originTransactionId;
		this.originTimestamp = originTimestamp;
		this.subscriberNumber = subscriberNumber;
		this.offerId = offerId;
		this.offerType = offerType;
	}
	
	@Override
	public Float getDecimalDenominator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsGetSubscriberInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prepareRequest() {
		// TODO Auto-generated method stub
		
	}
}

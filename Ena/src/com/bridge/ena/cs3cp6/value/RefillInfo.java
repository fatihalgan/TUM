package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;

public class RefillInfo implements Serializable {

	private RefillValue refillValueTotal;
	private RefillValue refillValuePromotion;
	
	public RefillInfo() {
		super();
	}

	public RefillValue getRefillValueTotal() {
		return refillValueTotal;
	}

	public void setRefillValueTotal(RefillValue refillValueTotal) {
		this.refillValueTotal = refillValueTotal;
	}

	public RefillValue getRefillValuePromotion() {
		return refillValuePromotion;
	}

	public void setRefillValuePromotion(RefillValue refillValuePromotion) {
		this.refillValuePromotion = refillValuePromotion;
	}
	
	public RefillValue getRefillValueWithoutPromotion() {
		return refillValueTotal.subtract(refillValuePromotion);
	}
	
}

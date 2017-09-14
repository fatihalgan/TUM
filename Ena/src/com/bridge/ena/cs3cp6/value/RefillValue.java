package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;

public class RefillValue implements Serializable {

	private Float refillAmount1;
	private Integer serviceFeeDaysExtended;
	private Integer supervisionDaysExtended;
	
	public RefillValue() {
		super();
	}

	public Float getRefillAmount1() {
		return refillAmount1;
	}

	public void setRefillAmount1(Float refillAmount1) {
		this.refillAmount1 = refillAmount1;
	}

	public Integer getServiceFeeDaysExtended() {
		return serviceFeeDaysExtended;
	}

	public void setServiceFeeDaysExtended(Integer serviceFeeDaysExtended) {
		this.serviceFeeDaysExtended = serviceFeeDaysExtended;
	}

	public Integer getSupervisionDaysExtended() {
		return supervisionDaysExtended;
	}

	public void setSupervisionDaysExtended(Integer supervisionDaysExtended) {
		this.supervisionDaysExtended = supervisionDaysExtended;
	}
	
	public RefillValue subtract(RefillValue other) {
		if(other == null) other = new RefillValue();
		RefillValue returnVal = new RefillValue();
		if(other.refillAmount1 != null) returnVal.setRefillAmount1(this.refillAmount1 - other.refillAmount1);
		else returnVal.setRefillAmount1(this.refillAmount1);
		if(other.serviceFeeDaysExtended != null) returnVal.setServiceFeeDaysExtended(this.serviceFeeDaysExtended - other.serviceFeeDaysExtended);
		else returnVal.setServiceFeeDaysExtended(this.serviceFeeDaysExtended);
		if(other.supervisionDaysExtended != null) returnVal.setSupervisionDaysExtended(this.supervisionDaysExtended - other.supervisionDaysExtended);
		else returnVal.setSupervisionDaysExtended(this.supervisionDaysExtended);
		return returnVal;
	}
}

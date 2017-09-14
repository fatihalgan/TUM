package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;

public class AccountFlags implements Serializable {

	private static final long serialVersionUID = 1002902438842276982L;
	private Boolean activationStatusFlag;
	private Boolean negativeBarringStatusFlag;
	private Boolean serviceFeePeriodExpiryFlag;
	private Boolean serviceFeePeriodWarningActiveFlag;
	private Boolean supervisionPeriodExpiryFlag;
	private Boolean supervisionPeriodWarningActiveFlag;
	
	public AccountFlags() {
		super();
	}

	public Boolean getActivationStatusFlag() {
		return activationStatusFlag;
	}

	public void setActivationStatusFlag(Boolean activationStatusFlag) {
		this.activationStatusFlag = activationStatusFlag;
	}

	public Boolean getNegativeBarringStatusFlag() {
		return negativeBarringStatusFlag;
	}

	public void setNegativeBarringStatusFlag(Boolean negativeBarringStatusFlag) {
		this.negativeBarringStatusFlag = negativeBarringStatusFlag;
	}

	public Boolean getServiceFeePeriodExpiryFlag() {
		return serviceFeePeriodExpiryFlag;
	}

	public void setServiceFeePeriodExpiryFlag(Boolean serviceFeePeriodExpiryFlag) {
		this.serviceFeePeriodExpiryFlag = serviceFeePeriodExpiryFlag;
	}

	public Boolean getServiceFeePeriodWarningActiveFlag() {
		return serviceFeePeriodWarningActiveFlag;
	}

	public void setServiceFeePeriodWarningActiveFlag(
			Boolean serviceFeePeriodWarningActiveFlag) {
		this.serviceFeePeriodWarningActiveFlag = serviceFeePeriodWarningActiveFlag;
	}

	public Boolean getSupervisionPeriodExpiryFlag() {
		return supervisionPeriodExpiryFlag;
	}

	public void setSupervisionPeriodExpiryFlag(Boolean supervisionPeriodExpiryFlag) {
		this.supervisionPeriodExpiryFlag = supervisionPeriodExpiryFlag;
	}

	public Boolean getSupervisionPeriodWarningActiveFlag() {
		return supervisionPeriodWarningActiveFlag;
	}

	public void setSupervisionPeriodWarningActiveFlag(
			Boolean supervisionPeriodWarningActiveFlag) {
		this.supervisionPeriodWarningActiveFlag = supervisionPeriodWarningActiveFlag;
	}
	
	
}

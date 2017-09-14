package mcel.tump.reservations.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class VoucherReservation implements Serializable {

	public static final String STATUS_RESERVE_REQUESTED = "REQ_RESERVE";
	public static final String STATUS_COMMIT_REQUESTED = "REQ_COMMIT";
	public static final String STATUS_CANCEL_REQUESTED = "REQ_CANCEL";
	public static final String STATUS_RESERVED = "RESERVED";
	public static final String STATUS_CANCELLED = "CANCELLED";
	public static final String STATUS_COMMITTED = "COMMITTED";
	public static final String STATUS_RESERVE_ERROR = "RES_ERROR";
	public static final String STATUS_COMMIT_ERROR = "COM_ERROR";
	public static final String STATUS_CANCEL_ERROR = "CAN_ERROR";
	
	private static final long serialVersionUID = 9202140603499691028L;
	
	private String requestTransactionId;
	private String requestDealerId;
	private String requestSubDealerId;
	private String subscriberMSISDN;
	private String senderMSISDN;
	private String voucherValue;
	private String voucherActivationCode;
	private String voucherSerialNumber;
	private Date requestTimestamp;
	private Date tumTimestamp;
	private String status;
	
	public VoucherReservation() {
		super();
	}

	public String getRequestTransactionId() {
		return requestTransactionId;
	}

	public void setRequestTransactionId(String requestTransactionId) {
		this.requestTransactionId = requestTransactionId;
	}

	public String getRequestDealerId() {
		return requestDealerId;
	}

	public void setRequestDealerId(String requestDealerId) {
		this.requestDealerId = requestDealerId;
	}

	public String getRequestSubDealerId() {
		return requestSubDealerId;
	}

	public void setRequestSubDealerId(String requestSubDealerId) {
		this.requestSubDealerId = requestSubDealerId;
	}

	public String getSubscriberMSISDN() {
		return subscriberMSISDN;
	}

	public void setSubscriberMSISDN(String subscriberMSISDN) {
		this.subscriberMSISDN = subscriberMSISDN;
	}

	public String getSenderMSISDN() {
		return senderMSISDN;
	}

	public void setSenderMSISDN(String senderMSISDN) {
		this.senderMSISDN = senderMSISDN;
	}

	public String getVoucherValue() {
		return voucherValue;
	}

	public void setVoucherValue(String voucherValue) {
		this.voucherValue = voucherValue;
	}

	public String getVoucherActivationCode() {
		return voucherActivationCode;
	}

	public void setVoucherActivationCode(String voucherActivationCode) {
		this.voucherActivationCode = voucherActivationCode;
	}

	public String getVoucherSerialNumber() {
		return voucherSerialNumber;
	}

	public void setVoucherSerialNumber(String voucherSerialNumber) {
		this.voucherSerialNumber = voucherSerialNumber;
	}

	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public Date getTumTimestamp() {
		return tumTimestamp;
	}

	public void setTumTimestamp(Date tumTimestamp) {
		this.tumTimestamp = tumTimestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	 public boolean equals(final Object other) {
		 if (this == other) {
			 return true;
	     }
	     if (!(other instanceof VoucherReservation)) {
	    	 return false;
	     }
	     VoucherReservation castOther = (VoucherReservation) other;
	     return new EqualsBuilder().append(requestDealerId, castOther.requestDealerId).append(requestTransactionId, requestTransactionId)
	    	.isEquals();
	    }

	    @Override
	    public int hashCode() {
	        return new HashCodeBuilder(736273725, 1705233981).append(requestDealerId).append(requestTransactionId).toHashCode();
	    }
	
	
}

package mcel.tump.reservations.domain;

import java.io.Serializable;
import java.util.Date;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Reservation implements Serializable {

	public static final String STATUS_RESERVED = "RESERVED";
	public static final String STATUS_COMMITTING = "COMMITTING";
	public static final String STATUS_CANCELLING = "CANCELLING";
	public static final String STATUS_COMMITTED = "COMMITTED";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_CANCELLED = "CANCELLED";

	private static final long serialVersionUID = -2190827917680899334L;

	private Long requestTransactionId;
	private String username;
	private String password;
	private String requestDealerId;
	private String requestSubDealerId;
	private String subscriberMSISDN;
	private String senderMSISDN;
	private Long transAmount;
	private Date requestTimestamp;
	private Date tumTimestamp;
	private String status;
	private int version;

	public Reservation() {
		super();
	}

	public Long getRequestTransactionId() {
		return requestTransactionId;
	}

	public void setRequestTransactionId(Long requestTransactionId) {
		this.requestTransactionId = requestTransactionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Long getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(Long transAmount) {
		this.transAmount = transAmount;
	}

	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTumTimestamp() {
		return tumTimestamp;
	}

	public void setTumTimestamp(Date tumTimestamp) {
		this.tumTimestamp = tumTimestamp;
	}

	public TUMPResponse cancel(TUMPResponse response) {
		if (status.equals(STATUS_CANCELLING)) {
			status = STATUS_CANCELLED;
			response.generateCancelReservationResponse(
					requestTransactionId.toString(), requestDealerId,
					requestSubDealerId, subscriberMSISDN,
					transAmount.intValue(), new Date(), 0);
			return response;
		}
		if (status.equals(STATUS_CANCELLED)) {
			response.generateFaultResponse(
					TUMResponseCodes.RESERVATION_ALREADY_CANCELLED
							.getResponseCode(), requestTransactionId.toString());
			return response;
		}
		if (status.equals(STATUS_COMMITTED)) {
			response.generateFaultResponse(
					TUMResponseCodes.RESERVATION_ALREADY_COMMITTED
							.getResponseCode(), requestTransactionId.toString());
			return response;
		}
		response.generateFaultResponse(
				TUMResponseCodes.RESERVATION_LOCKED
						.getResponseCode(), requestTransactionId.toString());
		return response;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Reservation)) {
			return false;
		}
		Reservation castOther = (Reservation) other;
		return new EqualsBuilder()
				.append(requestDealerId, castOther.requestDealerId)
				.append(requestTransactionId, requestTransactionId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(736273715, 1705237981)
				.append(requestDealerId).append(requestTransactionId)
				.toHashCode();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}

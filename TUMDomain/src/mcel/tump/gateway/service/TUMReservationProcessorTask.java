package mcel.tump.gateway.service;

import java.util.List;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.reservations.domain.Reservation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TUMReservationProcessorTask extends QuartzJobBean {

	private static final Log logger = LogFactory.getLog(TUMReservationProcessorTask.class);
	
	private ITUMPGatewayService tumGatewayService;
	
	public TUMReservationProcessorTask() {
		super();
	}
	
	public ITUMPGatewayService getTumGatewayService() {
		return tumGatewayService;
	}

	public void setTumGatewayService(ITUMPGatewayService tumGatewayService) {
		this.tumGatewayService = tumGatewayService;
	}

	@Override
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		getTumGatewayService().checkoutReservations();
		List<Reservation> reservations = getTumGatewayService().findReservationsByStatus(Reservation.STATUS_RESERVED);
		for(Reservation reservation : reservations) {
			try {
				//Process the reservation
				TUMRechargeRequest request = generateTUMRequestFromReservation(reservation);
				//Update the status of the reservation according to the result
				TUMPResponse response = new TUMPResponse();
				getTumGatewayService().rechargeSubscriber(request, response);
				if(response.isFault()) {
					getTumGatewayService().updateReservation(reservation, Reservation.STATUS_ERROR);
				} else {
					getTumGatewayService().updateReservation(reservation, Reservation.STATUS_COMMITTED);
				}
			} catch(Exception e) {
				logger.error("Failed to commit reservation: " + e.getMessage());
				getTumGatewayService().updateReservation(reservation, Reservation.STATUS_ERROR);
			}
		}
	}
	
	private TUMRechargeRequest generateTUMRequestFromReservation(Reservation reservation) {
		TUMPRequest request = new TUMPRequest();
		request.generateRechargeSubscriberRequest(reservation.getUsername(), reservation.getPassword(), reservation.getRequestTransactionId().toString(), reservation.getRequestDealerId(), reservation.getRequestSubDealerId(), reservation.getSubscriberMSISDN(), reservation.getTransAmount().intValue(), reservation.getRequestTimestamp());
		return request;
	}
}

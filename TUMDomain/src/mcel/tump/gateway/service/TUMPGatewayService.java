package mcel.tump.gateway.service;

import java.util.List;
import mcel.tump.gateway.dao.ITUMPGatewayDao;
import mcel.tump.gateway.exception.TUMApplicationException;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.reservations.domain.Reservation;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.account.dao.IAccountDao;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.gateway.service.chain.RechargeRequestProcessor;
import mcel.tump.gateway.service.chain.TUMRechargeFaultResponseException;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMPRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

    
public class TUMPGatewayService implements ITUMPGatewayService {

    private static final Log logger = LogFactory.getLog(TUMPGatewayService.class);
    private ITUMPGatewayDao tumpGatewayDao;
    private IAccountDao accountDao;
    private ISecurityService securityService;
    private IDealerService dealerService;
    private RechargeRequestProcessor rechargeRequestProcessor;
       
    public TUMPGatewayService() {
    }

    public ITUMPGatewayDao getTumpGatewayDao() {
        return tumpGatewayDao;
    }

    public void setTumpGatewayDao(ITUMPGatewayDao tumpGatewayDao) {
        this.tumpGatewayDao = tumpGatewayDao;
    }
    
    public ISecurityService getSecurityService() {
        return securityService;
    }
    
    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }
    
    public RechargeRequestProcessor getRechargeRequestProcessor() {
        return rechargeRequestProcessor;
    }

    public void setRechargeRequestProcessor(RechargeRequestProcessor rechargeRequestProcessor) {
        this.rechargeRequestProcessor = rechargeRequestProcessor;
    }
    
    @Override
    public void balanceCheck(TUMPRequest request, TUMPResponse response) {
        logger.debug("Will perform a balance check operation for transaction: " + request.getRequestTransactionID());
		request.setRequestTransactionID(getDealerService().fetchNextRequestTransactionID().toString());
        getTumpGatewayDao().balanceCheck(request, response);
    }

    public void rechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform a pinless recharge operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
    }
    
    public void reserveRechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform a pinless recharge reservation operation for transaction: " + request.getRequestTransactionID());
    	authenticateReservationRequest(request, response);
    	if(response.isFault()) return;
    	getRechargeRequestProcessor().process(request, response);
        //Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
    }
    
    public Reservation lockRechargeReservation(String requestTransactionId, String newStatus) {
    	return getTumpGatewayDao().lockRechargeReservation(requestTransactionId, newStatus);
    }
    
    public void cancelReserveRechargeSubscriber(TUMPRequest request, TUMPResponse response) {
    	logger.debug("Will perform cancellation of recharge reservation with ID: " + request.getRequestTransactionID());
    	authenticateReservationRequest(request, response);
    	if(response.isFault()) return;
    	Reservation reservation = null;
    	try {
    		logger.debug("Locking the reservation with id: " + request.getRequestTransactionID());
    		reservation = lockRechargeReservation(request.getRequestTransactionID(), Reservation.STATUS_CANCELLING);
    	} catch(TUMApplicationException te) {
    		logger.error("Got Exception during cancelReserveRechargeSubscriber: " + te.getMessage());
    		response.generateFaultResponse(te.getResponseCode(), request.getRequestTransactionID());
    		return;
    	} catch(HibernateException he) {
    		logger.error("Got Exception during cancelReserveRechargeSubscriber: " + he.getMessage());
    		response.generateFaultResponse(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), request.getRequestTransactionID());
    		return;
    	}
    	logger.debug("Reservation " + request.getRequestTransactionID() + " successfully locked.. Will perform a cancel operation..");
    	getTumpGatewayDao().cancelReserveRechargeSubscriber(reservation, request, response);
    	//Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
    }
    
    public void commitReserveRechargeSubscriber(TUMPRequest request, TUMPResponse response) {
    	logger.debug("Will commit recharge reservation with ID: " + request.getRequestTransactionID());
    	authenticateReservationRequest(request, response);
    	if(response.isFault()) return;
    	Reservation reservation = null;
    	try {
    		logger.debug("Locking the reservation with id: " + request.getRequestTransactionID());
    		reservation = lockRechargeReservation(request.getRequestTransactionID(), Reservation.STATUS_COMMITTING);
    	} catch(TUMApplicationException te) {
    		response.generateFaultResponse(te.getResponseCode(), request.getRequestTransactionID());
    		return;
    	} catch(HibernateException he) {
    		response.generateFaultResponse(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), request.getRequestTransactionID());
    		return;
    	}
    	TUMPRequest newReq = new TUMPRequest();
    	newReq.generateRechargeSubscriberRequest(reservation.getUsername(), reservation.getPassword(), reservation.getRequestTransactionId().toString(), reservation.getRequestDealerId(), reservation.getRequestSubDealerId(), reservation.getSubscriberMSISDN(), reservation.getTransAmount().intValue(), reservation.getRequestTimestamp());
		rechargeSubscriber(newReq, response);
		getTumpGatewayDao().commitReserveRechargeSubscriber(reservation, request, response);
    }

    
    private void authenticateReservationRequest(TUMRechargeRequest request, TUMRechargeResponse response) {
    	User user = securityService.getUserForDealer(request.getRequestDealerID());
    	if(user == null) {
    		response.generateFaultResponse(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
    		return;
    	}
    	if(!user.getUsername().equals(request.getUsername())) {
    		response.generateFaultResponse(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
    		return;
    	}
    	if(!user.getPassword().equals(request.getPassword())) {
    		response.generateFaultResponse(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
    		return;
    	}
    }
    
    @Override
	public void rechargeSubscriberPin(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform a pinned recharge operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
		//Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
	}
    
    @Override
    public void rechargeSubscriberSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform an SMS recharge operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
    	//Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
    }
    
    public void adjustSubscriber(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform an adjustment operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
    	//Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
    }
    
    public void checkSubscriberValid(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("Will perform an account details query for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
    	//Throw this exception only to make spring roll back the transaction..
        if(response.isFault()) throw new TUMRechargeFaultResponseException();
    }
    
    @Override
	public void checkTransactionStatus(TUMPRequest request, TUMPResponse response) {
		logger.debug("Will perform a check transaction status query for transaction: " + request.getRequestTransactionID());
		getTumpGatewayDao().checkTransactionStatus(request, response);
	}
    
    public void changePassword(TUMPRequest request, TUMPResponse response) {
    	logger.debug("Will perform a change password operation for transaction: " + request.getRequestTransactionID());
    	User user = getSecurityService().loadUserByUsername(request.getUsername());
        boolean result = user.updatePassword(request.getNewPassword(), request.getConfirmPassword());
        if(!result) {
            response.generateFaultResponse(TUMResponseCodes.NEW_PASSWORD_CONFIRM_ERROR.getResponseCode(), request.getRequestTransactionID());
        } else {
            getSecurityService().saveUser(user);
            getTumpGatewayDao().changePassword(request, response);
        }
    }
    
    public void checkoutReservations() {
    	try {
    		logger.debug("Will checkout reservations for processing..");
    		getTumpGatewayDao().checkoutReservations();
    	} catch(Exception e) {
    		logger.error(e.getMessage());
    	}
    }
    
    public List<Reservation> findReservationsByStatus(String status) {
    	return getTumpGatewayDao().findReservationsByStatus(status);
    }
    
    
    public void updateReservation(Reservation reservation, String newStatus) {
    	getTumpGatewayDao().attachForUpdate(reservation);
    	reservation.setStatus(newStatus);
    	getTumpGatewayDao().update(reservation);
    }
    
    
    public void rechargeLogs(TUMPRequest request, TUMPResponse response) {
        request.setRequestTransactionID(getDealerService().fetchNextRequestTransactionID().toString());
        getTumpGatewayDao().getRechargeLogs(request, response);
    }
    
    public void dailySalesReport(TUMPRequest request, TUMPResponse response) {
        request.setRequestTransactionID(getDealerService().fetchNextRequestTransactionID().toString());
        getTumpGatewayDao().getTotalDailySalesReport(request, response);
    }

    public IAccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }
}

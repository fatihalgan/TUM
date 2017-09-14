package mcel.tump.gateway.dao;

import java.util.Date;
import java.util.List;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.reservations.domain.Reservation;
import mcel.tump.util.persistence.IBaseDao;

public interface ITUMPGatewayDao extends IBaseDao {

    public void balanceCheck(TUMPRequest request, TUMPResponse response);

    public void rechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response);

    public void rechargeSubscriberPinned(TUMRechargeRequest request, TUMRechargeResponse response);
    
    public void reserveRechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response);
    
    public Reservation lockRechargeReservation(String requestTransactionId, String newStatus);
    
    public void checkTransactionStatus(TUMPRequest  request, TUMPResponse response);
    
    public void cancelReserveRechargeSubscriber(Reservation reservation, TUMRechargeRequest request, TUMRechargeResponse response);
    
    public void commitReserveRechargeSubscriber(Reservation reservation, TUMRechargeRequest request, TUMRechargeResponse response);
        
    public void writeFinalLog(Long tumTransactionId, Integer csResult, Date csTimestamp, String balanceBefore, String balanceAfter,
        Integer serviceClassOld, Float transactionAmount, Float refillPortion, Float adjustmentPortion, String voucherProfileID,
        String voucherProfileName, Date newServiceClassExpiryDate, Integer serviceClassNew, String subscriberMSISDN,
        Integer updateServiceClassFlag, Integer refillFlag, Integer adjustmentFlag, Integer balanceEnquiryFlag);

    public void changePassword(TUMPRequest request, TUMPResponse response);

    public void getRechargeLogs(TUMPRequest request, TUMPResponse response);

    public void getTotalDailySalesReport(TUMPRequest request, TUMPResponse response);
    
    public void checkoutReservations();
    
    public void checkoutReservationForCancel(TUMRechargeRequest request, TUMRechargeResponse response);
    
    public List<Reservation> findReservationsByStatus(String status);
    
    public Reservation checkoutReservationById(String requestTransactionId);
    
    public void reserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response);
    
    public void commitReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response);
    
    public void cancelReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response); 
    
    public void writeVoucherFinalLog(TUMRechargeRequest request, TUMRechargeResponse response);
    
    
}

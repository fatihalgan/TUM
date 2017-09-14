package mcel.tump.gateway.service;

import java.util.List;

import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.reservations.domain.Reservation;

public interface ITUMPGatewayService {

    public void balanceCheck(TUMPRequest request, TUMPResponse response);
    public void rechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response);
    public void reserveRechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response);
    public void rechargeSubscriberPin(TUMRechargeRequest request, TUMRechargeResponse response);
    public void rechargeSubscriberSMS(TUMRechargeRequest request, TUMRechargeResponse response);
    public void checkTransactionStatus(TUMPRequest request, TUMPResponse response);
    public void adjustSubscriber(TUMRechargeRequest request, TUMRechargeResponse response);
    public void checkSubscriberValid(TUMRechargeRequest request, TUMRechargeResponse response);
    public void changePassword(TUMPRequest request, TUMPResponse response);
    public void rechargeLogs(TUMPRequest request, TUMPResponse response);
    public void dailySalesReport(TUMPRequest request, TUMPResponse response);
    public void checkoutReservations();
    public List<Reservation> findReservationsByStatus(String status);
    public void updateReservation(Reservation reservation, String newStatus);
    public void cancelReserveRechargeSubscriber(TUMPRequest request, TUMPResponse response);
    public void commitReserveRechargeSubscriber(TUMPRequest request, TUMPResponse response);
    public Reservation lockRechargeReservation(String requestTransactionId, String newStatus);
}    
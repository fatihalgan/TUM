/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

/**
 *
 * @author db2admin
 */
public enum TUMGWTokens {

	AccountID("AccountID"),
    AdjustSubscriberAccountRequest("AdjustSubscriberAccount"),
	Amount("Amount"),
    AlertCode("AlertCode"),
    Balance("Balance"),
    BalanceCheckRequest("BalanceCheck"),
    CancelReservationRequest("CancelReservation"),
    CancelVoucherRequest("CancelPin"),
    ChangePasswordRequest("ChangePassword"),
    CheckSubscriberValidRequest("CheckSubscriberValid"),
    CheckTransactionStatusRequest("CheckTransactionStatus"),
    CommitReservationRequest("CommitReservation"),
    CommitVoucherRequest("CommitPin"),
    ConfirmPassword("ConfirmPassword"),
    DealerBalanceAfter("DealerBalanceAfter"),
    DealerBalanceBefore("DealerBalanceBefore"),
    DedicatedAccountValue("DedicatedAccountValue"),
    ERPAmount("ERPAmount"),
    ERPDealerID("ERPDealerID"),
    ERPOrderID("ERPOrderID"),
    ERPReference("ERPReference"),
    ERPTimeStamp("ERPTimeStamp"),
    NewPassword("NewPassword"),
    Password("Password"),
    PaymentArriveRequest("PaymentArrive"),
    PendingPayments("PendingPayments"),
    RechargeLogsRequest("RechargeLogs"),
    RechargeSubscriberRequest("RechargeSubscriber"),
    RechargeSubscriberSMSRequest("RechargeSubscriberSMS"),
    RechargeSubscriberPinRequest("RechargeSubscriberPin"),
    RegisterPaymentRequest("RegisterPayment"),
    RequestDealerID("RequestDealerID"),
    RequestSubDealerID("RequestSubDealerID"),
    RequestTimeStamp("RequestTimeStamp"),
    RequestTransactionID("RequestTransactionID"),
    ReserveRechargeSubscriberRequest("ReserveRechargeSubscriber"),
    ReserveVoucherRequest("ReservePin"),
    Signature("Signature"),
    SubscriberBalanceAfter("SubscriberBalanceAfter"),
    SubscriberBalanceBefore("SubscriberBalanceBefore"),
    SubscriberMSISDN("SubscriberMSISDN"),
    SenderMSISDN("SenderMSISDN"),
    SubscriberTemporaryBlocked("SubscriberTemporaryBlocked"),
    SubscriberRefillUnbarDateTime("SubscriberRefillUnbarDateTime"),
    TotalDeailySalesReportRequest("TotalDailySalesReport"),
    TotalPendingBalance("TotalPendingBalance"),
    TransferAmount("TransferAmount"),
    TransactionAmountComission("TransactionAmountComission"),
    TransactionStatus("TransactionStatus"),
    TUMDealerID("TUMDealerID"),
    TUMPaymentID("TUMPaymentID"),
    TUMTimeStamp("TUMTimeStamp"),
    TUMTransactionID("TUMTransactionID"),
    UserName("UserName"),
    UserSalesRecord("UserSales"),
    VoucherActivationCode("VoucherActivationCode"),
    VoucherSerialNumber("VoucherSerialNumber"),
    VoucherValue("VoucherValue");
    
    private final String text;
    
    private TUMGWTokens(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }
}

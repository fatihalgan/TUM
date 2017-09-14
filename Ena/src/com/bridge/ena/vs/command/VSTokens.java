/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.command;

/**
 *
 * @author db2admin
 */
public enum VSTokens {
    Action("action"),
    ActivationCode("activationCode"),
    Agent("agent"),
    BatchID("batchId"),
    ChangeVoucherStateMethodCallName("ChangeVoucherState"),
    Currency("currency"),
    EndReservationMethodCallName("EndReservation"),
    ExpiryDate("expiryDate"),
    ExtensionText1("extensionText1"),
    ExtensionText2("extensionText2"),
    ExtensionText3("extensionText3"),
    GetVoucherDetailsMethodCallName("GetVoucherDetails"),
    GetVoucherHistoryMethodCallName("GetVoucherHistory"),
    NewState("newState"),
    NetworkOperatorID("networkOperatorId"),
    OldState("oldState"),
    OperatorID("operatorId"),
    ReportFormat("reportFormat"),
    ReserveVoucherMethodCallName("ReserveVoucher"),
    ResponseCode("responseCode"),
    Schedulation("schedulation"),
    SerialNumber("serialNumber"),
    SerialNumberFirst("serialNumberFisrt"),
    SerialNumberLast("serialNumberLast"),
    State("state"),
    SubscriberID("subscriberId"),
    Timestamp("timestamp"),
    TransactionID("transactionId"),
    UpdateVoucherStateMethodCallName("UpdateVoucherState"),
    Value("value"),
    VoucherGroup("voucherGroup");
    
        
    private final String text;
    
    private VSTokens(String text) {
        this.text = text;
    }
    
    public String toString() {
        return text;
    }
}

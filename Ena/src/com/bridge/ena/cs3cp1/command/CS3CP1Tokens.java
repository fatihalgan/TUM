/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

/**
 *
 * @author db2admin
 */
public enum CS3CP1Tokens {
    AccountValueAfter1("accountValueAfter1"),
    ActivationNumber("activationNumber"),
    AdjustmentAmount("adjustmentAmount"),
    AdjustmentMethodCallName("AdjustmentTRequest"),
    BalanceEnquiryMethodCallName("BalanceEnquiryTRequest"),
    CurrentLanguageID("currentLanguageID"),
    FafActionDelete("DEL"),
    GetAccountDetailsMethodCallName("GetAccountDetailsTRequest"),
    GetAllowedServiceClassChangesMethodCallName("GetAllowedServiceClassChangesT"),
    GetFaFListTMethodCallName("GetFaFListTRequest"),
    NewExpiryDate("newExpiryDate"),
    PaymentProfileID("paymentProfileID"),
    RechargeAmount1DedicatedTotal("rechargeAmount1DedicatedTotal"),
    RechargeAmount1MainTotal("rechargeAmount1MainTotal"),
    RechargeAmount1PromoTotal("rechargeAmount1PromoTotal"),
    RefillMethodCallName("RefillTRequest"),
    RelativeDateAdjustmentServiceFee("relativeDateAdjustmentServiceFee"),
    RelativeDateAdjustmentSupervision("relativeDateAdjustmentSupervision"),
    ServiceFeeDate("serviceFeeDate"),
    ServiceFeeDateAfter("serviceFeeDateAfter"),
    StandartVoucherRefillMethodCallName("StandardVoucherRefillTRequest"),
    SupervisionDate("supervisionDate"),
    SupervisionDateAfter("supervisionDateAfter"),
    TransactionAmountRefill("transactionAmountRefill"),
    TransactionAmount1Refill("transactionAmount1Refill"),
    TransactionServiceClass("transactionServiceClass"),
    TransactionVVPeriodExt("transactionVVPeriodExt"),
    UpdateFafMethodCallName("UpdateFaFListTRequest"),
    UpdateServiceClassMethodCallName("UpdateServiceClassTRequest"),
    ValueVoucherDateAfter("valueVoucherDateAfter"),
    ValueVoucherRefillMethodCallName("ValueVoucherRefillTRequest");

    private final String text;

    private CS3CP1Tokens(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}

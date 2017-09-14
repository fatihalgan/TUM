/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

/**
 *
 * @author db2admin
 */
public enum CS3CP6Tokens {
	AccountFlags("accountFlags"),
    ActivationStatusFlag("activationStatusFlag"),
    AccountAfterRefill("accountAfterRefill"),
    AccountBeforeRefill("accountBeforeRefill"),
    AdjustmentAmountRelative("adjustmentAmountRelative"),
    AdjustmentDateRelative("adjustmentDateRelative"),
    DedicatedAccountUpdateInformation("dedicatedAccountUpdateInformation"),
    FafInformationList("fafInformationList"),
    FafOwnerAccount("Account"),
    FafOwnerSubscriber("Subscriber"),
    GetAccountDetailsMethodCallName("GetAccountDetails"),
    GetAllowedServiceClassChangesMethodCallName("GetAllowedServiceClasses"),
    GetBalanceAndDateMethodCallName("GetBalanceAndDate"),
    GetFaFListMethodCallName("GetFaFList"),
    getOffersMethodCallName("GetOffers"),
    LanguageIDCurrent("languageIDCurrent"),
    NegativeBarringStatusFlag("negativeBarringStatusFlag"),
    RefillInformation("refillInformation"),
    RefillMethodCallName("Refill"),
    RefillProfileID("refillProfileID"),
    RefillValueTotal("refillValueTotal"),
    RefillAmount1("refillAmount1"),
    RefillValuePromotion("refillValuePromotion"),
    RequestRefillAccountAfterFlag("requestRefillAccountAfterFlag"),
    RequestRefillAccountBeforeFlag("requestRefillAccountBeforeFlag"),
    RequestRefillDetailsFlag("requestRefillDetailsFlag"),
    SelectedOption("selectedOption"),
    ServiceClassAction("serviceClassAction"),
    ServiceClassActionSet("Set"),
    ServiceClassActionSetOriginal("SetOriginal"),
    ServiceClassActionSetTemporary("SetTemporary"),
    ServiceClassActionDeleteTemporary("DeleteTemporary"),
    ServiceClassOriginal("serviceClassOriginal"),
    ServiceClassTemporaryExpiryDate("serviceClassTemporaryExpiryDate"),
    ServiceFeeDaysExtended("serviceFeeDaysExtended"),
    ServiceFeeExpiryDate("serviceFeeExpiryDate"),
    ServiceFeeExpiryDateRelative("serviceFeeExpiryDateRelative"),
    ServiceFeePeriodExpiryFlag("serviceFeePeriodExpiryFlag"),
    ServiceFeePeriodWarningActiveFlag("serviceFeePeriodWarningActiveFlag"),
    SupervisionDaysExtended("supervisionDaysExtended"),
    SupervisionExpiryDate("supervisionExpiryDate"),
    SupervisionExpiryDateRelative("supervisionExpiryDateRelative"),
    SupervisionPeriodExpiryFlag("supervisionPeriodExpiryFlag"),
    SupervisionPeriodWarningActiveFlag("supervisionPeriodWarningActiveFlag"),
    TransactionAmount("transactionAmount"),
    TransactionAmountConverted("transactionAmountConverted"),
    UpdateBalanceAndDateMethodCallName("UpdateBalanceAndDate"),
    UpdateFaFListMethodCallName("UpdateFaFList"),
    UpdateOfferMethodCallName("UpdateOffer"),
    UpdateServiceClassMethodCallName("UpdateServiceClass"),
    VocuherActivationCode("voucherActivationCode"),
    VoucherGroup("voucherGroup"),
    VoucherSerialNumber("voucherSerialNumber");
    

    private final String text;

    private CS3CP6Tokens(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

}

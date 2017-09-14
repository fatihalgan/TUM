/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.command;

/**
 *
 * @author db2admin
 */
public enum CSTokens {
    AccountValue1("accountValue1"),
    CreditClearenceDate("creditClearenceDate"),
    Currency1("currency1"),
    DedicatedAccountID("dedicatedAccountID"),
    DedicatedAccountInformation("dedicatedAccountInformation"),
    DedicatedAccountValue1("dedicatedAccountValue1"),
    ExpiryDate("expiryDate"),
    ExternalData1("externalData1"),
    ExternalData2("externalData2"),
    FafAction("fafAction"),
    FafActionAdd("ADD"),
    FafActionSet("SET"),
    FafIndicator("fafIndicator"),
    FafInformation("fafInformation"),
    FafNumber("fafNumber"),
    FafOwner("owner"),
    OriginHostName("originHostName"),
    OriginNodeType("originNodeType"),
    OriginTimeStamp("originTimeStamp"),
    OriginTransactionID("originTransactionID"),
    RequestedOwner("requestedOwner"),
    RequestInactiveOffersFlag("requestInactiveOffersFlag"),
    RequestDedicatedAccountDetailsFlag("requestDedicatedAccountDetailsFlag"),
    RefillUnbarDateTime("refillUnbarDateTime"),
    ResponseCode("responseCode"),
    ServiceClassCurrent("serviceClassCurrent"),
    ServiceClassNew("serviceClassNew"),
    ServiceRemovalDate("serviceRemovalDate"),
    SubscriberNumber("subscriberNumber"),
    SubscriberNumberNAI("subscriberNumberNAI"),
    TemporaryBlockedFlag("temporaryBlockedFlag"),
    TransactionCurrency("transactionCurrency");
    
    
    private final String text;
    
    private CSTokens(String text) {
        this.text = text;
    }
    
    public String toString() {
        return text;
    }
}

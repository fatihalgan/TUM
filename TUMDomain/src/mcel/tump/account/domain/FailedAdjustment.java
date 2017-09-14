/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class FailedAdjustment implements Serializable {

    private Long tumTransactionId;
    private Long edgeTransactionId;
    private Date edgeTimestamp;
    private Date tumTimestamp;
    private Date csTimestamp;
    private Float beforeBalance;
    private Float afterBalance;
    private Float transactionAmount;
    private Float refillPortion;
    private Float adjustmentPortion;
    private String subscriberMSISDN;
    private Integer refillFlag;
    private Integer adjustmentFlag;

    public FailedAdjustment() {
        super();
    }

    /**
     * @return the tumTransactionId
     */
    public Long getTumTransactionId() {
        return tumTransactionId;
    }

    /**
     * @param tumTransactionId the tumTransactionId to set
     */
    public void setTumTransactionId(Long tumTransactionId) {
        this.tumTransactionId = tumTransactionId;
    }

    /**
     * @return the edgeTransactionId
     */
    public Long getEdgeTransactionId() {
        return edgeTransactionId;
    }

    /**
     * @param edgeTransactionId the edgeTransactionId to set
     */
    public void setEdgeTransactionId(Long edgeTransactionId) {
        this.edgeTransactionId = edgeTransactionId;
    }

    /**
     * @return the edgeTimestamp
     */
    public Date getEdgeTimestamp() {
        return edgeTimestamp;
    }

    /**
     * @param edgeTimestamp the edgeTimestamp to set
     */
    public void setEdgeTimestamp(Date edgeTimestamp) {
        this.edgeTimestamp = edgeTimestamp;
    }

    /**
     * @return the tumTimestamp
     */
    public Date getTumTimestamp() {
        return tumTimestamp;
    }

    /**
     * @param tumTimestamp the tumTimestamp to set
     */
    public void setTumTimestamp(Date tumTimestamp) {
        this.tumTimestamp = tumTimestamp;
    }

    /**
     * @return the csTimestamp
     */
    public Date getCsTimestamp() {
        return csTimestamp;
    }

    /**
     * @param csTimestamp the csTimestamp to set
     */
    public void setCsTimestamp(Date csTimestamp) {
        this.csTimestamp = csTimestamp;
    }

    /**
     * @return the beforeBalance
     */
    public Float getBeforeBalance() {
        return beforeBalance;
    }

    /**
     * @param beforeBalance the beforeBalance to set
     */
    public void setBeforeBalance(Float beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    /**
     * @return the afterBalance
     */
    public Float getAfterBalance() {
        return afterBalance;
    }

    /**
     * @param afterBalance the afterBalance to set
     */
    public void setAfterBalance(Float afterBalance) {
        this.afterBalance = afterBalance;
    }

    /**
     * @return the transactionAmount
     */
    public Float getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(Float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the refillPortion
     */
    public Float getRefillPortion() {
        return refillPortion;
    }

    /**
     * @param refillPortion the refillPortion to set
     */
    public void setRefillPortion(Float refillPortion) {
        this.refillPortion = refillPortion;
    }

    /**
     * @return the adjustmentPortion
     */
    public Float getAdjustmentPortion() {
        return adjustmentPortion;
    }

    /**
     * @param adjustmentPortion the adjustmentPortion to set
     */
    public void setAdjustmentPortion(Float adjustmentPortion) {
        this.adjustmentPortion = adjustmentPortion;
    }

    /**
     * @return the subscriberMSISDN
     */
    public String getSubscriberMSISDN() {
        return subscriberMSISDN;
    }

    /**
     * @param subscriberMSISDN the subscriberMSISDN to set
     */
    public void setSubscriberMSISDN(String subscriberMSISDN) {
        this.subscriberMSISDN = subscriberMSISDN;
    }

    /**
     * @return the refillFlag
     */
    public Integer getRefillFlag() {
        return refillFlag;
    }

    /**
     * @param refillFlag the refillFlag to set
     */
    public void setRefillFlag(Integer refillFlag) {
        this.refillFlag = refillFlag;
    }

    /**
     * @return the adjustmentFlag
     */
    public Integer getAdjustmentFlag() {
        return adjustmentFlag;
    }

    /**
     * @param adjustmentFlag the adjustmentFlag to set
     */
    public void setAdjustmentFlag(Integer adjustmentFlag) {
        this.adjustmentFlag = adjustmentFlag;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FailedAdjustment)) {
            return false;
        }
        FailedAdjustment castOther = (FailedAdjustment) other;
        return new EqualsBuilder().append(tumTransactionId, castOther.tumTransactionId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(436273745, 1705287181).append(tumTransactionId).toHashCode();
    }

    public Float getCorrectionAmount() {
        if(!(adjustmentFlag > 0) && (refillFlag == 0)) return 0f;
        if(transactionAmount != refillPortion + adjustmentPortion) return 0f;
        return adjustmentPortion;
    }
}

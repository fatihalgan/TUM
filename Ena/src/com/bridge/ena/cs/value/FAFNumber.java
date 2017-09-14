/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.value;

import java.io.Serializable;

/**
 *
 * @author db2admin
 */
public class FAFNumber implements Serializable {

    private String msisdn;
    private Integer indicator;

    public FAFNumber(String msisdn, Integer indicator) {
        this.msisdn = msisdn;
        this.indicator = indicator;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the indicator
     */
    public Integer getIndicator() {
        return indicator;
    }

    /**
     * @param indicator the indicator to set
     */
    public void setIndicator(Integer indicator) {
        this.indicator = indicator;
    }
}

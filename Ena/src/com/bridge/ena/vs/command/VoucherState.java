/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.command;

/**
 *
 * @author db2admin
 */
public enum VoucherState {
    
    Available(0, "Available"),
    Used(1, "Used"),
    Damaged(2, "Damaged"),
    Stolen_Missing(3, "Stolen/Missing"),
    Pending(4, "Pending"),
    Unavailable(5, "Unavailable"),
    Unknown(6, "Unknown");
    
    private final Integer stateCode;
    private final String state;
    
    private VoucherState(Integer code, String state) {
        this.stateCode = code;
        this.state = state;
    }
    
    public String toString() {
        return state;
    }
    
    public Integer getCode() {
        return stateCode;
    }
}

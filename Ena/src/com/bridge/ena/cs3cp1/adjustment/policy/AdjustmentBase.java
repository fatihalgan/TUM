/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

/**
 *
 * @author db2admin
 */
public enum AdjustmentBase {
    FreeAmount("FreeAmount"),
    MainAdjustmentPercentage("MainAdjustmentPercentage");
    
    private final String text;
    
    private AdjustmentBase(String text) {
        this.text = text;
    }
    
    public String toString() {
        return text;
    }
}

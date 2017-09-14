/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

/**
 *
 * @author db2admin
 */
public interface AdjustmentRestriction {
    
    public boolean adjustmentAllowed(String currentServiceClassID, Float mainAdjustmentAmount);
}

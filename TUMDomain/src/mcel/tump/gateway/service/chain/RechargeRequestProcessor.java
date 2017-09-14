/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;

/**
 *
 * @author db2admin
 */
public interface RechargeRequestProcessor {

    public void process(TUMRechargeRequest request, TUMRechargeResponse response);
    public void setNextHandler(RechargeRequestProcessor processor);
    public void setOnFaultHandler(RechargeRequestProcessor processor);
}

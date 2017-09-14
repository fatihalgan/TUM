/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

/**
 *
 * @author db2admin
 */
public interface CSCommandAdapter {
    public TUMRechargeRequest getRequest();
    public TUMRechargeResponse getResponse();
    public void execute();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.command;

import java.util.ArrayList;
import java.util.List;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

/**
 *
 * @author db2admin
 */
public abstract class CompositeCommand<E extends AbstractCommand> implements Command {

    protected List<E> chain = new ArrayList<E>();    
    private IXMLRPCClient xmlRpcClient;
                
    public void addCommand(E command) {
        chain.add(command);
    }
    
    public List<E> getChain() {
        return chain;
    }
    
    public IXMLRPCClient getXMLRPCClient() {
        return xmlRpcClient;
    }
    
    public void setXMLRPCClient(IXMLRPCClient xmlRpcClient) {
        this.xmlRpcClient = xmlRpcClient;
    }
    
     public List<MethodResponse> getResponse() {
        List<MethodResponse> resp = new ArrayList<MethodResponse>();
         for(AbstractCommand c : chain) {
            if(c.getResponse().getFault() != null) return resp;
            resp.add(c.getResponse());
        }
         return resp;
    }

    public int getSize() {
        return chain.size();
    }
     
    public E getLastCommand() {
        return getChain().get(chain.size() - 1);
    }

    

}

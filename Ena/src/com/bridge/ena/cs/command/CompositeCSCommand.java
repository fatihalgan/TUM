/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.command;

import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.xmlrpc.command.CompositeCommand;




/**
 *
 * @author db2admin
 */
public abstract class CompositeCSCommand<E extends AbstractCSCommand> extends CompositeCommand<E> {
    
   public abstract boolean supportsGetSubscriberInfo();
   private String subscriberNoPrefix;
   private Integer subscriberNumberNAI;
   private String originHostName;
   
   public abstract SubscriberInfo getSubscriberInfo();

   public CompositeCSCommand(String subscriberNoPrefix, Integer subscriberNumberNAI) {
       this.subscriberNoPrefix = subscriberNoPrefix;
       this.subscriberNumberNAI = subscriberNumberNAI;
   }
   
   public void addCommand(E command) {
       super.addCommand(command);
       command.setOriginHostName(getOriginHostName());
   }

   public Integer getResponseCode() {
       for(AbstractCSCommand c : chain) {
           if(c.isErrorOrFaultResponse()) return c.getResponseCode();
       }
       return chain.get(chain.size() - 1).getResponseCode();
   }
   
   public boolean isErrorOrFaultResponse() {
       return getLastCommand().isErrorOrFaultResponse();
   }
   
   public boolean isFault(){
	   return getLastCommand().isFault();
   }
   
   public Integer getFaultCode(){
	   return getLastCommand().getFaultCode();
   }
   
   public int getHttpStatusCode() {
       return getLastCommand().getHttpStatusCode();
   }

    /**
     * @return the subscriberNoPrefix
     */
    public String getSubscriberNoPrefix() {
        return subscriberNoPrefix;
    }

    /**
     * @return the subscriberNumberNAI
     */
    public Integer getSubscriberNumberNAI() {
        return subscriberNumberNAI;
    }
    
    public String getOriginHostName() {
        return originHostName;
    }

    public void setOriginHostName(String originHostName) {
        this.originHostName = originHostName;
    }
   
}

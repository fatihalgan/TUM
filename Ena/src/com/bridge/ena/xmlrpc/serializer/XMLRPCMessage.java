/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.NonterminalNode;

/**
 *
 * @author db2admin
 */
public abstract class XMLRPCMessage implements NonterminalNode {

    protected Struct members;
    
    protected XMLRPCMessage() {
        super();
    }
    
    protected XMLRPCMessage(Struct members) {
        this.members = members;
    }
    
    public Struct getMembers() {
        return members;
    }

    public void setMembers(Struct members) {
        this.members = members;
    }
    
    public void addMember(Member member) {
        if (getMembers() == null) {
            setMembers(new Struct());
        }
        getMembers().addMember(member);
    }
    
    public Member getMember(String key) {
        for(Member member : getMembers().getMembers()) {
            if(member.getName().equals(key)) return member;
        }
        return null;
    }
    
    public Object getMemberValue(String key) {
        Member member = getMember(key);
        if(member == null) return null;
        else return member.getValue();
    }
    
}

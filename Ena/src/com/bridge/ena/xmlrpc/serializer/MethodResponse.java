package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.Node;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import java.util.Iterator;
import org.dom4j.Element;

public class MethodResponse extends XMLRPCMessage {

    private Fault fault;

    public MethodResponse() {
        super();
    }
    
    public MethodResponse(Element element) {
        parseChildren(element);
    }

    public MethodResponse(Struct members) {
        super(members);
    }

    public MethodResponse(Fault fault) {
        this.fault = fault;
    }

    @Override
    public String toXML(StringBuffer buf) {
        buf.append("<methodResponse>");
        if (fault != null) {
            buf.append(fault.toXML(new StringBuffer()));
        } else if (members != null) {
            buf.append("<params>");
            buf.append("<param>");
            buf.append("<value>");
            buf.append(members.toXML(new StringBuffer()));
            buf.append("</value>");
            buf.append("</param>");
            buf.append("</params>");
        } 
        buf.append("</methodResponse>");
        return buf.toString();
    }

    public void parseChildren(Element element) {
        try {
            Iterator<Element> it = element.elementIterator();
            while (it.hasNext()) {
                Element child = it.next();
                if (TypeFactory.isIntrinsicType(child)) {
                    parseChildren(child);
                } else {
                    Node node = TypeFactory.getNode(child);
                    if (node instanceof Struct) {
                        setMembers((Struct) node);
                    } else if (node instanceof Fault) {
                        setFault((Fault) node);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Bad or invalid XML-RPC response data.");
        }   
    }

    public Fault getFault() {
        return fault;
    }

    public void setFault(Fault fault) {
        this.fault = fault;
    }
    
    public boolean isFault() {
        if(getFault() != null) return true;
        return false;
    }
    
    public void generateFaultResponse(Integer faultCode, String message) {
        fault = new Fault();
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultCode.toString(), faultCode));
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultString.toString(), message));
    }
    
}

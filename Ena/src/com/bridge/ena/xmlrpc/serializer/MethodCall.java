package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.Node;
import java.util.Iterator;
import org.dom4j.Element;

public class MethodCall extends XMLRPCMessage {

    private String methodName;
    
    public MethodCall() {
        super();
    }

    public MethodCall(Element element) {
        parseChildren(element);
    }


    public MethodCall(String methodName, Struct members) {
        super(members);
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            if (TypeFactory.isIntrinsicType(child)) {
                parseChildren(child);
            } else if (TypeFactory.isTerminalNode(child)) {
                if (child.getName().equals("methodName")) {
                    setMethodName(child.getText());
                }
            } else {
                Node node = TypeFactory.getNode(child);
                if (node instanceof Struct) {
                    setMembers((Struct) node);
                }
            }
        }
    }

    public String toXML(StringBuffer buf) {
        try {
            buf.append("<methodCall>");
            buf.append("<methodName>");
            buf.append(getMethodName());
            buf.append("</methodName>");
            buf.append("<params>");
            buf.append("<param>");
            buf.append("<value>");
            buf.append(members.toXML(new StringBuffer()));
            buf.append("</value>");
            buf.append("</param>");
            buf.append("</params>");
            buf.append("</methodCall>");
        } catch(Exception e) {
            throw new RuntimeException("Bad or invalid RPC request data");
        }
        return buf.toString();
    }
}

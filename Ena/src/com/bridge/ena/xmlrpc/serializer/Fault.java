package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.NonterminalNode;
import com.bridge.ena.xml.Node;
import java.util.Iterator;
import org.dom4j.Element;

public class Fault implements NonterminalNode {

    private Struct fault = new Struct();

    public Fault() {
    }

    public Fault(Element element) {
        parseChildren(element);
    }

    public Fault(Struct fault) {
        this.fault = fault;
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            if (TypeFactory.isIntrinsicType(child)) {
                parseChildren(child);
            } else {
                Node node = TypeFactory.getNode(child);
                setFault((Struct) node);
            }
        }
    }

    public Struct getFault() {
        return fault;
    }

    public void setFault(Struct fault) {
        this.fault = fault;
    }

    @Override
    public String toXML(StringBuffer buf) {
        buf.append("<fault>");
        buf.append("<value>");
        buf.append(getFault().toXML(new StringBuffer()));
        buf.append("</value>");
        buf.append("</fault>");
        return buf.toString();
    }
}

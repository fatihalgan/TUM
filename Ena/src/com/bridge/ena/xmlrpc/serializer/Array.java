package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.NonterminalNode;
import com.bridge.ena.xml.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public class Array implements NonterminalNode {

    private List<Struct> structs = new ArrayList<Struct>();

    public Array() {
        super();
    }

    public Array(Element element) {
        parseChildren(element);
    }

    public Array(Struct struct) {
        this.getStructs().add(struct);
    }

    public List<Struct> getStructs() {
        return structs;
    }

    public void addStruct(Struct struct) {
        this.getStructs().add(struct);
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            if (child.getName().equals("data")) {
                parseChildren(child);
            } else if (TypeFactory.isIntrinsicType(child)) {
                parseChildren(child);
            } else {
                Node node = TypeFactory.getNode(child);
                addStruct((Struct) node);
            }
        }
    }

    @Override
    public String toXML(StringBuffer buf) {
        buf.append("<array>");
        buf.append("<data>");
        for(Struct st : getStructs()) {
            buf.append("<value>");
            buf.append(st.toXML(new StringBuffer()));
            buf.append("</value>");
        }
        buf.append("</data>");
        buf.append("</array>");
        return buf.toString();
    }
    
    public Struct getValue(String key, Object value) {
        for(Struct st : getStructs()) {
            if(st.getValue(key).equals(value)) return st;
        }
        return null;
    }
}

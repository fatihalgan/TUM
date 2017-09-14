package com.bridge.ena.xml;

import java.io.Serializable;
import org.dom4j.Element;

public interface Node extends Serializable {
    public String toXML(StringBuffer buf);
    public void parseChildren(Element element);
}

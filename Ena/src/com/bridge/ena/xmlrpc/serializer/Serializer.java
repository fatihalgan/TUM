package com.bridge.ena.xmlrpc.serializer;

import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.AbstractSerializer;
import java.io.InputStream;
import com.bridge.ena.xml.BadXmlFormatException;
import org.dom4j.Element;

public class Serializer extends AbstractSerializer {

    public Serializer(InputStream in) throws BadXmlFormatException {
        super(in);
    }

    public Serializer(String xml) throws BadXmlFormatException {
        super(xml);
    }

    public Node parse() {
        Element element = document.getRootElement();
        Node node = TypeFactory.getNode(element);
        return node;
    }
}

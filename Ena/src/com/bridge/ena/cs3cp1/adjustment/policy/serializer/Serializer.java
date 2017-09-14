/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy.serializer;

import com.bridge.ena.xml.AbstractSerializer;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xml.Node;
import java.io.InputStream;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class Serializer extends AbstractSerializer {

    public Serializer(InputStream in) throws BadXmlFormatException {
        super(in);
    }

    public Serializer(String xml) throws BadXmlFormatException {
        super(xml);
    }

    @Override
    public Node parse() {
        Element element = document.getRootElement();
        Node node = TypeFactory.getNode(element);
        return node;
    }

}

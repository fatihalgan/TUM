/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xml;

import java.io.InputStream;
import java.io.StringReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 *
 * @author db2admin
 */
public abstract class AbstractSerializer {

    protected Document document = null;
    private static final Log logger = LogFactory.getLog(AbstractSerializer.class);

    public AbstractSerializer(InputStream in) throws BadXmlFormatException {
        try {
            SAXReader reader = new SAXReader();
            logger.debug("Parsing the XML-RPC Request message");
            document = reader.read(in);
            logger.trace("Content of the XML-RPC request message to be serialized: " + document.asXML());
        } catch(DocumentException de) {
            logger.error("Dom4J Could not parse incoming xml stream: " + de.getMessage());
            throw new BadXmlFormatException();
        } catch(Exception e) {
        	logger.error("Dom4J Could not parse incoming xml stream: " + e.getMessage());
            throw new BadXmlFormatException();
        }
    }

    public AbstractSerializer(String xml) throws BadXmlFormatException {
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new StringReader(xml));
        } catch(Exception e) {
            logger.error("Bad XML-RPC request string:");
            logger.error(xml);
            throw new BadXmlFormatException();
        }
    }
    
    public abstract Node parse();
}

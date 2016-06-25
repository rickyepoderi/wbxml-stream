/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.bind.si.Indication;
import es.rickyepoderi.wbxml.bind.si.Si;
import es.rickyepoderi.wbxml.stream.WbXmlInputFactory;
import es.rickyepoderi.wbxml.stream.WbXmlOutputFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author ricky
 */
public class SiStAXTest {
    
    
    /**
     * si-001.xml:
     * 
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
     * <!DOCTYPE si PUBLIC "-//WAPFORUM//DTD SI 1.0//EN" "http://www.wapforum.org/DTD/si.dtd">
     * <si>
     *   <indication created="1999-06-25T15:23:15Z" 
     *               href="http://www.xyz.com/email/123/abc.wml" 
     *               si-expires="1999-06-30T00:00:00Z">
     *     You have 4 new emails
     *   </indication>
     *</si>
     * 
     */
    
    File wbxmlFile = null;
    
    public SiStAXTest() {
        wbxmlFile = new File("src/test/examples/si/si-001.wbxml");
    }
    
    @Test(groups = {"stax"} )
    public void testReadWBXMLLoop() throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(new FileInputStream(wbxmlFile));
        boolean foundSi = false;
        boolean foundIndication = false;
        while (reader.hasNext()) {
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                if ("si".equals(reader.getLocalName())) {
                    foundSi = true;
                } else if ("indication".equals(reader.getLocalName())) {
                    foundIndication = true;
                    Assert.assertEquals(3, reader.getAttributeCount());
                    Assert.assertEquals("1999-06-25T15:23:15Z", reader.getAttributeValue(null, "created"));
                    Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", reader.getAttributeValue(null, "href"));
                    Assert.assertEquals("1999-06-30T00:00:00Z", reader.getAttributeValue(null, "si-expires"));
                    Assert.assertEquals("You have 4 new emails", reader.getElementText());
                }
            }
            reader.next();
        }
        Assert.assertTrue(foundSi);
        Assert.assertTrue(foundIndication);
        reader.close();
    }
    
    private void testNextElement(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        // read next tag
        int type = reader.nextTag();
        // it should be "si"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, type);
        Assert.assertEquals("si", reader.getName().getLocalPart());
        Assert.assertEquals(0, reader.getAttributeCount());
        // read the next tag
        type = reader.nextTag();
        // it should be "indication"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, type);
        Assert.assertEquals("indication", reader.getName().getLocalPart());
        Assert.assertEquals(3, reader.getAttributeCount());
        Assert.assertEquals("1999-06-25T15:23:15Z", reader.getAttributeValue(null, "created"));
        Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", reader.getAttributeValue(null, "href"));
        Assert.assertEquals("1999-06-30T00:00:00Z", reader.getAttributeValue(null, "si-expires"));
        // read the element text and position to END "indication"
        Assert.assertEquals("You have 4 new emails", reader.getElementText());
        // read next tag
        type = reader.nextTag();
        // "si" end
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, type);
        reader.close();
    }
    
    private void testEventNextElement(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        // read next tag
        XMLEvent event = reader.nextTag();
        // it should be "si"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals("si", event.asStartElement().getName().getLocalPart());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // read the next tag
        event = reader.nextTag();
        // it should be "indication"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals("indication", event.asStartElement().getName().getLocalPart());
        // check attributes
        Iterator iter = event.asStartElement().getAttributes();
        // href
        Assert.assertTrue(iter.hasNext());
        Attribute attr = (Attribute) iter.next();
        Assert.assertEquals(new QName("href"), attr.getName());
        Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", attr.getValue());
        // created
        Assert.assertTrue(iter.hasNext());
        attr = (Attribute) iter.next();
        Assert.assertEquals(new QName("created"), attr.getName());
        Assert.assertEquals("1999-06-25T15:23:15Z", attr.getValue());
        // si-expires
        Assert.assertTrue(iter.hasNext());
        attr = (Attribute) iter.next();
        Assert.assertEquals(new QName("si-expires"), attr.getName());
        Assert.assertEquals("1999-06-30T00:00:00Z", attr.getValue());
        // read the element text and position to END "indication"
        Assert.assertEquals("You have 4 new emails", reader.getElementText());
        // read next tag => "si" end
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        reader.close();
    }
    
    private void testNext(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        // it should be start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.getEventType());
        // read next => it should be "si"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals("si", reader.getName().getLocalPart());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next it should be "indication"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals("indication", reader.getName().getLocalPart());
        Assert.assertEquals(3, reader.getAttributeCount());
        Assert.assertEquals("1999-06-25T15:23:15Z", reader.getAttributeValue(null, "created"));
        Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", reader.getAttributeValue(null, "href"));
        Assert.assertEquals("1999-06-30T00:00:00Z", reader.getAttributeValue(null, "si-expires"));
        // now three attributes
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, reader.next());
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, reader.next());
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, reader.next());
        // read the element text and position to END "indication"
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, reader.next());
        Assert.assertEquals("You have 4 new emails", reader.getText());
        // next it is the end of indication
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        // next end of si
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        // next end docuemnt
        Assert.assertEquals(XMLStreamConstants.END_DOCUMENT, reader.next());
        reader.close();
    }
    
    private void testEventNext(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        // it should be start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.nextEvent().getEventType());
        // read next => it should be "si"
        XMLEvent event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals("si", event.asStartElement().getName().getLocalPart());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next it should be "indication"
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals("indication", event.asStartElement().getName().getLocalPart());
        // next it should be attribute href
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, event.getEventType());
        Assert.assertEquals(new QName("href"), ((Attribute)event).getName());
        Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", ((Attribute)event).getValue());
        // next it should be attribute created
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, event.getEventType());
        Assert.assertEquals(new QName("created"), ((Attribute)event).getName());
        Assert.assertEquals("1999-06-25T15:23:15Z", ((Attribute)event).getValue());
        // next it should be attribute attribute si-expires
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.ATTRIBUTE, event.getEventType());
        Assert.assertEquals(new QName("si-expires"), ((Attribute)event).getName());
        Assert.assertEquals("1999-06-30T00:00:00Z", ((Attribute)event).getValue());
        // read the element text
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, event.getEventType());
        Assert.assertEquals("You have 4 new emails", event.asCharacters().getData());
        // next it is the end of indication
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals("indication", event.asEndElement().getName().getLocalPart());
        // next end of si
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals("si", event.asEndElement().getName().getLocalPart());
        // next end docuemnt
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_DOCUMENT, event.getEventType());
        reader.close();
    }
    
    @Test(groups = {"stax"} )
    public void testReadWBXMLNextTag() throws Exception  {
        testNextElement(new FileInputStream(wbxmlFile));
    }
    
    @Test(groups = {"stax"} )
    public void testReadWBXMLNext() throws Exception  {
        testNext(new FileInputStream(wbxmlFile));
    }
    
    @Test(groups = {"stax"} )
    public void testEventReadWBXMLNextTag() throws Exception  {
        testEventNextElement(new FileInputStream(wbxmlFile));
    }
    
    @Test(groups = {"stax"} )
    public void testEventReadWBXMLNext() throws Exception  {
        testEventNext(new FileInputStream(wbxmlFile));
    }
    
    @Test(groups = {"stax"})
    public void testWriteWBML() throws Exception {
        Indication indication = new Indication();
        indication.setCreated("1999-06-25T15:23:15Z");
        indication.setHref("http://www.xyz.com/email/123/abc.wml");
        indication.setSiExpires("1999-06-30T00:00:00Z");
        indication.setvalue("You have 4 new emails");
        Si si = new Si();
        si.setIndication(indication);
        // write the "si" object
        XMLOutputFactory fact = new WbXmlOutputFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter xmlStreamWriter = fact.createXMLStreamWriter(bos);
        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement("si");
        xmlStreamWriter.writeStartElement("indication");
        xmlStreamWriter.writeAttribute("href", si.getIndication().getHref());
        xmlStreamWriter.writeAttribute("created", si.getIndication().getCreated());
        xmlStreamWriter.writeAttribute("si-expires", si.getIndication().getSiExpires());
        xmlStreamWriter.writeCharacters(si.getIndication().getvalue());
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.close();
        // check the write
        testNextElement(new ByteArrayInputStream(bos.toByteArray()));
        testNext(new ByteArrayInputStream(bos.toByteArray()));
        testEventNextElement(new ByteArrayInputStream(bos.toByteArray()));
        testEventNext(new ByteArrayInputStream(bos.toByteArray()));
    }
    
}
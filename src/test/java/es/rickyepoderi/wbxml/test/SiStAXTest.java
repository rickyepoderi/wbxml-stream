/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *    
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *    
 * As a special exception, the copyright holders of this library give 
 * you permission to link this library with independent modules to 
 * produce an executable, regardless of the license terms of these 
 * independent modules, and to copy and distribute the resulting 
 * executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the 
 * license of that module.  An independent module is a module which 
 * is not derived from or based on this library.  If you modify this 
 * library, you may extend this exception to your version of the 
 * library, but you are not obligated to do so.  If you do not wish 
 * to do so, delete this exception statement from your version.
 *
 * Project: github.com/rickyepoderi/wbxml-stream
 * 
 */
package es.rickyepoderi.wbxml.test;

import es.rickyepoderi.wbxml.bind.si.Indication;
import es.rickyepoderi.wbxml.bind.si.Si;
import es.rickyepoderi.wbxml.stream.WbXmlInputFactory;
import es.rickyepoderi.wbxml.stream.WbXmlOutputFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.EventFilter;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import org.testng.Assert;
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
    
    public SiStAXTest() {
    }
    
    private void readStreamLoop(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
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
    
    private void readEventLoop(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        boolean foundSi = false;
        boolean foundIndication = false;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                if ("si".equals(event.asStartElement().getName().getLocalPart())) {
                    foundSi = true;
                } else if ("indication".equals(event.asStartElement().getName().getLocalPart())) {
                    foundIndication = true;
                    Assert.assertEquals("1999-06-25T15:23:15Z", event.asStartElement().getAttributeByName(new QName("created")).getValue());
                    Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", event.asStartElement().getAttributeByName(new QName("href")).getValue());
                    Assert.assertEquals("1999-06-30T00:00:00Z", event.asStartElement().getAttributeByName(new QName("si-expires")).getValue());
                    Assert.assertEquals("You have 4 new emails", reader.getElementText());
                }
            }
        }
        Assert.assertTrue(foundSi);
        Assert.assertTrue(foundIndication);
        reader.close();
    }
    
    private void readStreamNextTag(InputStream in) throws Exception {
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
        // the getElementText sets the reader to end_element of indication
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        // read next tag
        type = reader.nextTag();
        // "si" end
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, type);
        reader.close();
    }
    
    private void readEventNextTag(InputStream in) throws Exception {
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
    
    private void readStreamNext(InputStream in) throws Exception {
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
        // next end document
        Assert.assertEquals(XMLStreamConstants.END_DOCUMENT, reader.next());
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readFilteredStreamStartElement(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        StreamFilter filter = new StreamFilter() {
            @Override
            public boolean accept(XMLStreamReader reader) {
                return reader.getEventType() == XMLStreamConstants.START_ELEMENT;
            }
        };
        reader = f.createFilteredReader(reader, filter);
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
        Assert.assertEquals("You have 4 new emails", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        // no more start elements
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readFilteredEventStartElement(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        EventFilter filter = new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        };
        reader = f.createFilteredReader(reader, filter);
        // read next => it should be "si"
        XMLEvent event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals("si", event.asStartElement().getName().getLocalPart());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next it should be "indication"
        event = reader.nextEvent();
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
        // no more start elements
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readFilteredStreamIndication(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        StreamFilter filter = new StreamFilter() {
            @Override
            public boolean accept(XMLStreamReader reader) {
                return reader.getEventType() == XMLStreamConstants.START_ELEMENT &&
                        "indication".equals(reader.getName().getLocalPart());
            }
        };
        reader = f.createFilteredReader(reader, filter);
        // it should be start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.getEventType());
        // next it should be "indication" cos the filter just read this element
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals("indication", reader.getName().getLocalPart());
        Assert.assertEquals(3, reader.getAttributeCount());
        Assert.assertEquals("1999-06-25T15:23:15Z", reader.getAttributeValue(null, "created"));
        Assert.assertEquals("http://www.xyz.com/email/123/abc.wml", reader.getAttributeValue(null, "href"));
        Assert.assertEquals("1999-06-30T00:00:00Z", reader.getAttributeValue(null, "si-expires"));
        Assert.assertEquals("You have 4 new emails", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        // no more start elements
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readFilteredEventIndication(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        EventFilter filter = new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return event.isStartElement() &&
                        "indication".equals(event.asStartElement().getName().getLocalPart());
            }
        };
        reader = f.createFilteredReader(reader, filter);
        // next it should be "indication" cos the filter just read this element
        XMLEvent event = reader.nextEvent();
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
        // no more start elements
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readEventNext(InputStream in) throws Exception {
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
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private byte[] writeStream() throws Exception {
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
        return bos.toByteArray();
    }
    
    private byte[] writeEvent() throws Exception {
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
        XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
        XMLEventWriter xmlEventWriter = fact.createXMLEventWriter(bos);
        xmlEventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
        xmlEventWriter.add(eventFactory.createStartElement(XMLConstants.DEFAULT_NS_PREFIX, null, "si"));
        xmlEventWriter.add(eventFactory.createStartElement(XMLConstants.DEFAULT_NS_PREFIX, null, "indication"));
        xmlEventWriter.add(eventFactory.createAttribute("href", si.getIndication().getHref()));
        xmlEventWriter.add(eventFactory.createAttribute("created", si.getIndication().getCreated()));
        xmlEventWriter.add(eventFactory.createAttribute("si-expires", si.getIndication().getSiExpires()));
        xmlEventWriter.add(eventFactory.createCharacters(si.getIndication().getvalue()));
        xmlEventWriter.add(eventFactory.createEndElement(XMLConstants.DEFAULT_NS_PREFIX, null, "indication"));
        xmlEventWriter.add(eventFactory.createEndElement(XMLConstants.DEFAULT_NS_PREFIX, null, "si"));
        xmlEventWriter.add(eventFactory.createEndDocument());
        xmlEventWriter.close();
        return bos.toByteArray();
    }
    
    @Test(groups = {"stax", "stream"} )
    public void testStream() throws Exception {
        byte[] si = this.writeStream();
        this.readStreamLoop(new ByteArrayInputStream(si));
        this.readStreamNext(new ByteArrayInputStream(si));
        this.readStreamNextTag(new ByteArrayInputStream(si));
        this.readFilteredStreamStartElement(new ByteArrayInputStream(si));
        this.readFilteredStreamIndication(new ByteArrayInputStream(si));
    }
    
    @Test(groups = {"stax", "event"} )
    public void testEvent() throws Exception  {
        byte[] si = this.writeEvent();
        this.readEventLoop(new ByteArrayInputStream(si));
        this.readEventNext(new ByteArrayInputStream(si));
        this.readEventNextTag(new ByteArrayInputStream(si));
        this.readFilteredEventStartElement(new ByteArrayInputStream(si));
        this.readFilteredEventIndication(new ByteArrayInputStream(si));
    }
    
}
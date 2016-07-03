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

import es.rickyepoderi.wbxml.stream.WbXmlInputFactory;
import es.rickyepoderi.wbxml.stream.WbXmlOutputFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import javax.xml.stream.events.XMLEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * <!DOCTYPE SyncML PUBLIC "-//SYNCML//DTD SyncML 1.1//EN" "http://www.syncml.org/docs/syncml_represent_v11_20020213.dtd">
 * <SyncML xmlns='SYNCML:SYNCML1.1'>
 *   <SyncBody>
 *     <Sync>
 *	 <Add>
 *         <CmdID>6</CmdID>
 *         <Meta><Type xmlns='syncml:metinf'>text/x-vcard</Type></Meta>
 *         <Item>
 *           <Source>
 *             <LocURI>pas-id-3F4B790300000000</LocURI>
 *           </Source>
 *           <Data>BEGIN:VCARD
 * VERSION:2.1
 * X-EVOLUTION-FILE-AS:Ximian, Inc.
 * N:
 * LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA
 * 02215=0AUSA
 * TEL;WORK;VOICE:(617) 236-0442
 * TEL;WORK;FAX:(617) 236-8630
 * EMAIL;INTERNET:[EMAIL PROTECTED]
 * URL:www.ximian.com/
 * ORG:Ximian, Inc.
 * NOTE:Welcome to the Ximian Addressbook.
 * UID:pas-id-3F4B790300000000
 * END:VCARD</Data>
 *         </Item>
 *       </Add>
 *     </Sync>
 *     <Final/>
 *   </SyncBody>
 * </SyncML>
 * 
 * @author ricky
 */
public class SyncMLStaxTest {
    
    private byte[] writeStream() throws Exception {
        // write the "syncml" object
        XMLOutputFactory fact = new WbXmlOutputFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLStreamWriter xmlStreamWriter = fact.createXMLStreamWriter(bos);
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        xmlStreamWriter.writeDTD("<!DOCTYPE SyncML PUBLIC \"-//SYNCML//DTD SyncML 1.1//EN\" \"http://www.syncml.org/docs/syncml_represent_v11_2002");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "SyncML");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "SyncBody");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Sync");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Add");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "CmdID");
        xmlStreamWriter.writeCharacters("6");
        xmlStreamWriter.writeEndElement(); //CmdiD
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Meta");
        xmlStreamWriter.writeStartElement("syncml:metinf", "Type");
        xmlStreamWriter.writeCharacters("text/x-vcard");
        xmlStreamWriter.writeEndElement();  // Type
        xmlStreamWriter.writeEndElement();  // Meta
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Item");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Source");
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "LocURI");
        xmlStreamWriter.writeCharacters("pas-id-3F4B790300000000");
        xmlStreamWriter.writeEndElement();  // LocURI
        xmlStreamWriter.writeEndElement();  // Source
        xmlStreamWriter.writeStartElement("SYNCML:SYNCML1.1", "Data");
        xmlStreamWriter.writeCData("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD");
        xmlStreamWriter.writeEndElement();  // Data
        xmlStreamWriter.writeEndElement();  // Item
        xmlStreamWriter.writeEndElement();  // Add
        xmlStreamWriter.writeEndElement();  // Sync
        xmlStreamWriter.writeEmptyElement("SYNCML:SYNCML1.1", "Final");
        xmlStreamWriter.writeEndElement();  // SyncBody
        xmlStreamWriter.writeEndElement();  // SyncML
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.close();
        return bos.toByteArray();
    }
    
    private byte[] writeEvent() throws Exception {
        // write the "syncml" object
        XMLOutputFactory fact = new WbXmlOutputFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEventWriter xmlEventWriter = fact.createXMLEventWriter(bos);
        XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
        xmlEventWriter.add(eventFactory.createStartDocument("UTF-8", "1.0"));
        xmlEventWriter.add(eventFactory.createDTD("<!DOCTYPE SyncML PUBLIC \"-//SYNCML//DTD SyncML 1.1//EN\" \"http://www.syncml.org/docs/syncml_represent_v11_2002"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "SyncML"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "SyncBody"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Sync"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Add"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "CmdID"));
        xmlEventWriter.add(eventFactory.createCharacters("6"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "CmdID"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Meta"));
        xmlEventWriter.add(eventFactory.createStartElement("metinf11", "syncml:metinf", "Type"));
        xmlEventWriter.add(eventFactory.createCharacters("text/x-vcard"));
        xmlEventWriter.add(eventFactory.createEndElement("metinf11", "syncml:metinf", "Type"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Meta"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Item"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Source"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "LocURI"));
        xmlEventWriter.add(eventFactory.createCharacters("pas-id-3F4B790300000000"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "LocURI"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Source"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Data"));
        xmlEventWriter.add(eventFactory.createCharacters("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Data"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Item"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Add"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Sync"));
        xmlEventWriter.add(eventFactory.createStartElement("syncml11", "SYNCML:SYNCML1.1", "Final"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "Final"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "SyncBody"));
        xmlEventWriter.add(eventFactory.createEndElement("syncml11", "SYNCML:SYNCML1.1", "SyncML"));
        xmlEventWriter.add(eventFactory.createEndDocument());
        xmlEventWriter.close();
        return bos.toByteArray();
    }
    
    private void readStreamNext(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        // it should be start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.getEventType());
        // read next => it should be "SyncML"
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // read next => SyncBody
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Sync
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Add
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => CmdID
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => characters
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, reader.next());
        Assert.assertEquals("6", reader.getText());
        // next => END_ELEMENT CmdID
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        // next => Meta
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Type
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => characters
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, reader.next());
        Assert.assertEquals("text/x-vcard", reader.getText());
        // next => end Type
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        // next => end Meta
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), reader.getName());
        // next => Item
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => source
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => LocURI
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next characters
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, reader.next());
        Assert.assertEquals("pas-id-3F4B790300000000", reader.getText());
        // next => end LocURI
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        // next => end Source
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), reader.getName());
        // next => Data
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Characters
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, reader.next());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", reader.getText());
        // next => end Data
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        // next => End Item
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), reader.getName());
        // next => end Add
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), reader.getName());
        // next => end Sync
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), reader.getName());
        // next => Final
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => end Final
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), reader.getName());
        // next => end SyncBody
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), reader.getName());
        // next => end SyncML
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), reader.getName());
        // next => end document
        Assert.assertEquals(XMLStreamConstants.END_DOCUMENT, reader.next());
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readEventNext(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        // start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.nextEvent().getEventType());
        // read next => it should be "SyncML"
        XMLEvent event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // read next => SyncBody
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Sync
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Add
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => CmdID
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => characters
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, event.getEventType());
        Assert.assertEquals("6", event.asCharacters().getData());
        // next => END_ELEMENT CmdID
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), event.asEndElement().getName());
        // next => Meta
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Type
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => characters
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, event.getEventType());
        Assert.assertEquals("text/x-vcard", event.asCharacters().getData());
        // next => end Type
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), event.asEndElement().getName());
        // next => end Meta
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), event.asEndElement().getName());
        // next => Item
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => source
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => LocURI
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next characters
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, event.getEventType());
        Assert.assertEquals("pas-id-3F4B790300000000", event.asCharacters().getData());
        // next => end LocURI
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), event.asEndElement().getName());
        // next => end Source
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), event.asEndElement().getName());
        // next => Data
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Characters
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.CHARACTERS, event.getEventType());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", event.asCharacters().getData());
        // next => end Data
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), event.asEndElement().getName());
        // next => End Item
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), event.asEndElement().getName());
        // next => end Add
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), event.asEndElement().getName());
        // next => end Sync
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), event.asEndElement().getName());
        // next => Final
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => end Final
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), event.asEndElement().getName());
        // next => end SyncBody
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), event.asEndElement().getName());
        // next => end SyncML
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), event.asEndElement().getName());
        // next => end document
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.END_DOCUMENT, event.getEventType());
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    private void readStreamNextTag(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLStreamReader reader = f.createXMLStreamReader(in);
        // it should be start document
        Assert.assertEquals(XMLStreamConstants.START_DOCUMENT, reader.getEventType());
        // next tag => SyncML
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => SyncBody
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => Sync
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => Add
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => CmdID
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("6", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        // next tag => Meta
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => Type
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("text/x-vcard", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        // next tag => end Meta
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), reader.getName());
        // next Tag => Item
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next Tag => Source
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => LocURI
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("pas-id-3F4B790300000000", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        // next tag => end Source
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), reader.getName());
        // next tag => Data
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        // next tag => end Item
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), reader.getName());
        // next tag => end Add
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), reader.getName());
        // next tag => end Sync
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), reader.getName());
        // next tag => Final
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next tag => end Final
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), reader.getName());
        // next tag => end SyncBody
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), reader.getName());
        // next tag => end SyncML
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.nextTag());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), reader.getName());
        reader.close();
    }
    
    private void readEventNextTag(InputStream in) throws Exception {
        XMLInputFactory f = new WbXmlInputFactory();
        XMLEventReader reader = f.createXMLEventReader(in);
        // next tag => SyncML
        XMLEvent event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => SyncBody
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => Sync
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => Add
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => CmdID
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("6", reader.getElementText());
        // next tag => Meta
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => Type
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("text/x-vcard", reader.getElementText());
        // next tag => end Meta
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), event.asEndElement().getName());
        // next Tag => Item
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next Tag => Source
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => LocURI
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("pas-id-3F4B790300000000", reader.getElementText());
        // next tag => end Source
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), event.asEndElement().getName());
        // next tag => Data
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", reader.getElementText());
        // next tag => end Item
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), event.asEndElement().getName());
        // next tag => end Add
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), event.asEndElement().getName());
        // next tag => end Sync
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), event.asEndElement().getName());
        // next tag => Final
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next tag => end Final
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), event.asEndElement().getName());
        // next tag => end SyncBody
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), event.asEndElement().getName());
        // next tag => end SyncML
        event = reader.nextTag();
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), event.asEndElement().getName());
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
        // next  => SyncML
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => SyncBody
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Sync
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Add
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => CmdID
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("6", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), reader.getName());
        // next => Meta
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Type
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("text/x-vcard", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), reader.getName());
        // next => Item
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => Source
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        // next => LocURI
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("pas-id-3F4B790300000000", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), reader.getName());
        // next => Data
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", reader.getElementText());
        Assert.assertEquals(XMLStreamConstants.END_ELEMENT, reader.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), reader.getName());
        // next => Final
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, reader.next());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), reader.getName());
        Assert.assertEquals(0, reader.getAttributeCount());
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
        // next  => SyncML
        XMLEvent event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncML"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => SyncBody
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "SyncBody"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Sync
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Sync"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Add
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Add"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => CmdID
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "CmdID"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("6", reader.getElementText());
        // next => Meta
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Meta"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Type
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("syncml:metinf", "Type"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("text/x-vcard", reader.getElementText());
        // next => Item
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Item"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => Source
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Source"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // next => LocURI
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "LocURI"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("pas-id-3F4B790300000000", reader.getElementText());
        // next => Data
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Data"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        Assert.assertEquals("BEGIN:VCARD\n"
                + "VERSION:2.1\n"
                + "X-EVOLUTION-FILE-AS:Ximian, Inc.\n"
                + "N:\n"
                + "LABEL;WORK;ENCODING=QUOTED-PRINTABLE:401 Park Drive  3 West=0ABoston, MA\n"
                + "02215=0AUSA\n"
                + "TEL;WORK;VOICE:(617) 236-0442\n"
                + "TEL;WORK;FAX:(617) 236-8630\n"
                + "EMAIL;INTERNET:[EMAIL PROTECTED]\n"
                + "URL:www.ximian.com/\n"
                + "ORG:Ximian, Inc.\n"
                + "NOTE:Welcome to the Ximian Addressbook.\n"
                + "UID:pas-id-3F4B790300000000\n"
                + "END:VCARD", reader.getElementText());
        // next => Final
        event = reader.nextEvent();
        Assert.assertEquals(XMLStreamConstants.START_ELEMENT, event.getEventType());
        Assert.assertEquals(new QName("SYNCML:SYNCML1.1", "Final"), event.asStartElement().getName());
        Assert.assertFalse(event.asStartElement().getAttributes().hasNext());
        // no more start elements
        Assert.assertFalse(reader.hasNext());
        reader.close();
    }
    
    @Test(groups = {"stax", "stream"} )
    public void testStream() throws Exception {
        byte[] syncml = this.writeStream();
        this.readStreamNext(new ByteArrayInputStream(syncml));
        this.readStreamNextTag(new ByteArrayInputStream(syncml));
        this.readFilteredStreamStartElement(new ByteArrayInputStream(syncml));
    }
    
    @Test(groups = {"stax", "event"} )
    public void testEvent() throws Exception {
        byte[] syncml = this.writeEvent();
        this.readEventNext(new ByteArrayInputStream(syncml));
        this.readEventNextTag(new ByteArrayInputStream(syncml));
        this.readFilteredEventStartElement(new ByteArrayInputStream(syncml));
    }
    
}

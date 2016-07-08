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
package es.rickyepoderi.wbxml.stream.events;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.util.XMLEventConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

/**
 * <p>Allows a user to register a way to allocate events given an XMLStreamReader.
 * The XMLEventAllocator can be set on an XMLInputFactory using the property
 * <em>javax.xml.stream.allocator</em>. A significant problem implementing this
 * interface is that there is no way to obtain the default XMLEventAllocator of 
 * the StAX implementation (absurd I have to say). So I have been force to copy the
 * <a href="http://hg.openjdk.java.net/icedtea/jdk7/jaxws/file/3f7212cae6eb/src/share/jaxws_classes/com/sun/xml/internal/fastinfoset/stax/events/StAXEventAllocatorBase.java">default implementation</a>
 * in JavaEE (minor modifications).</p>
 *
 * <p>This base class uses EventFactory to create events as recommended in the
 * JavaDoc of XMLEventAllocator. The spec for the first Allocate method states 
 * that it must NOT modify the state of the Reader.</p>
 *
 */
public class WbXmlEventAllocator implements XMLEventAllocator {

    /**
     * Internal event factory used to create the events.
     */
    private XMLEventFactory factory;

    /**
     * Constructor with specific factory
     * @param factory 
     */
    public WbXmlEventAllocator(XMLEventFactory factory) {
        this.factory = factory;
    }

    /**
     * This method creates an instance of the XMLEventAllocator. This allows the
     * XMLInputFactory to allocate a new instance per reader.
     */
    @Override
    public XMLEventAllocator newInstance() {
        return new WbXmlEventAllocator(this.factory);
    }

    /**
     * This method allocates an event given the current state of the
     * XMLStreamReader. If this XMLEventAllocator does not have a one-to-one
     * mapping between reader state and events this method will return null.
     *
     * @param streamReader The XMLStreamReader to allocate from
     * @return the event corresponding to the current reader state
     */
    @Override
    public XMLEvent allocate(XMLStreamReader streamReader) throws XMLStreamException {
        return getXMLEvent(streamReader);
    }

    /**
     * This method allocates an event or set of events given the current state
     * of the XMLStreamReader and adds the event or set of events to the
     * consumer that was passed in.
     *
     * @param streamReader The XMLStreamReader to allocate from
     * @param consumer The XMLEventConsumer to add to.
     */
    @Override
    public void allocate(XMLStreamReader streamReader, XMLEventConsumer consumer) throws XMLStreamException {
        consumer.add(getXMLEvent(streamReader));

    }
    
    /**
     * Method that constructs the event associated with the current state of the
     * reader. Currently the ENTITY_REFERENCE is not implemented.
     * @param reader The reader to get the current state
     * @return The associated XMLEvent for the current state of the reader
     */
    protected XMLEvent getXMLEvent(XMLStreamReader reader) {
        XMLEvent event = null;
        //returns the current event
        int eventType = reader.getEventType();
        //this needs to be set before creating events
        factory.setLocation(reader.getLocation());
        switch (eventType) {
            case XMLEvent.START_ELEMENT:
                StartElement startElement = factory.createStartElement(
                        new QName(reader.getNamespaceURI(), reader.getLocalName(), reader.getPrefix()),
                        getAttributes(reader), getNamespaces(reader));
                event = startElement;
                break;
            case XMLEvent.END_ELEMENT:
                EndElement endElement = factory.createEndElement(
                        new QName(reader.getNamespaceURI(), reader.getLocalName(), reader.getPrefix()), 
                        getNamespaces(reader));
                event = endElement;
                break;
            case XMLEvent.PROCESSING_INSTRUCTION:
                event = factory.createProcessingInstruction(reader.getPITarget(), reader.getPIData());
                break;
            case XMLEvent.CHARACTERS:
                if (reader.isWhiteSpace()) {
                    event = factory.createSpace(reader.getText());
                } else {
                    event = factory.createCharacters(reader.getText());
                }
                break;
            case XMLEvent.COMMENT:
                event = factory.createComment(reader.getText());
                break;
            case XMLEvent.START_DOCUMENT:
                StartDocument docEvent = factory.createStartDocument(
                        reader.getEncoding(), reader.getVersion(), reader.isStandalone());
                event = docEvent;
                break;
            case XMLEvent.END_DOCUMENT:
                EndDocument endDocumentEvent = factory.createEndDocument();
                event = endDocumentEvent;
                break;
            /*case XMLEvent.ENTITY_REFERENCE:
                event = factory.createEntityReference(reader.getLocalName(),
                        new EntityDeclarationImpl(reader.getLocalName(), reader.getText()));
                break;
            case XMLEvent.ATTRIBUTE:
                event = null;
                break;*/
            case XMLEvent.DTD:
                event = factory.createDTD(reader.getText());
                break;
            case XMLEvent.CDATA:
                event = factory.createCData(reader.getText());
                break;
            case XMLEvent.SPACE:
                event = factory.createSpace(reader.getText());
                break;
        }
        return event;
    }

    /**
     * Method that get the attributes for a start element and creates the 
     * iterator needed for the event factory.
     * @param streamReader The reader 
     * @return The iterator for all the attributes
     */
    protected Iterator<Attribute> getAttributes(XMLStreamReader streamReader) {
        List<Attribute> attrs = new ArrayList<Attribute>(streamReader.getAttributeCount());
        for (int i = 0; i < streamReader.getAttributeCount(); i++) {
            Attribute attr = factory.createAttribute(streamReader.getAttributeName(i),
                    streamReader.getAttributeValue(i));
            attrs.add(attr);
        }
        return attrs.iterator();
    }

    /**
     * Method that get the Namespaces for a start element and creates the
     * iterator needed for the event factory.
     * @param streamReader The reader
     * @return The iterator for all the namespaces
     */
    protected Iterator<Namespace> getNamespaces(XMLStreamReader streamReader) {
        List<Namespace> namespaces = new ArrayList<Namespace>(streamReader.getNamespaceCount());
        for (int i = 0; i < streamReader.getNamespaceCount(); i++) {
            Namespace namespace = factory.createNamespace(streamReader.getNamespacePrefix(i), streamReader.getNamespaceURI(i));
            namespaces.add(namespace);
        }
        return namespaces.iterator();
    }

}

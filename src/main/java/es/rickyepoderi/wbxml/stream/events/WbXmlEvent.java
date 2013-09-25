/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.definition.WbXmlDefinition;
import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * <p>WbXml-Stream implementation for the XMLEvent interface. This class
 * is a simple representation of the XMLEvent. It uses the WbXmlStreamReader
 * in order to create the event.</p>
 * 
 * @author ricky
 */
public class WbXmlEvent implements XMLEvent {

    /**
     * The event type the stream is at when created the event.
     */
    private int eventType = -1;
    
    /**
     * The definition of the parser or stream reader.
     */
    private WbXmlDefinition definition = null;
    
    /**
     * The location of the parser.
     */
    private Location loc = null;
    
    /**
     * Constructor based on the stream. It creates all the properties based
     * on the current state of the stream reader.
     * @param stream The stream reader
     */
    public WbXmlEvent(WbXmlStreamReader stream) {
        this.eventType = stream.getEventType();
        this.definition = stream.getParser().getDefinition();
        this.loc = stream.getLocation();
    }
    
    /**
     * Constructor that based on stream but fixes the type. It is used
     * from StartElement to create the attributes.
     * @param stream 
     */
    protected WbXmlEvent(WbXmlStreamReader stream, int eventType) {
        this(stream);
        this.eventType = eventType;
    }
    
    /**
     * Getter for the definition of the stream reader or parser.
     * @return The definition of the stream
     */
    protected WbXmlDefinition getDefinition() {
        return definition;
    }
    
    /**
     * Getter for the event type.
     * @return The event type of the event
     */
    @Override
    public int getEventType() {
        return eventType;
    }

    /**
     * The location of the event.
     * @return The location
     */
    @Override
    public Location getLocation() {
        return loc;
    }

    /**
     * Is this event a START_ELEMENT?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isStartElement() {
        return eventType == XMLStreamConstants.START_ELEMENT;
    }

    /**
     * If this event a ATTRIBUTE?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isAttribute() {
        return eventType == XMLStreamConstants.ATTRIBUTE;
    }

    /**
     * If this event a NAMESPACE?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isNamespace() {
        return eventType == XMLStreamConstants.NAMESPACE;
    }

    /**
     * If this event a END_ELEMENT?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isEndElement() {
        return eventType == XMLStreamConstants.END_ELEMENT;
    }

    /**
     * Is this event a ENTITY_REFERENCE?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isEntityReference() {
        return eventType == XMLStreamConstants.ENTITY_REFERENCE;
    }

    /**
     * Is this event a PROCESSING_INSTRUCTION?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isProcessingInstruction() {
        return eventType == XMLStreamConstants.PROCESSING_INSTRUCTION;
    }

    /**
     * Is this event a CHARACTERS (or CDATA)?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isCharacters() {
        return eventType == XMLStreamConstants.CHARACTERS ||
                eventType == XMLStreamConstants.CDATA;
    }

    /**
     * Is this event a START_DOCUMENT?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isStartDocument() {
        return eventType == XMLStreamConstants.START_DOCUMENT;
    }

    /**
     * Is this event a END_DOCUMENT?
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isEndDocument() {
        return eventType == XMLStreamConstants.END_DOCUMENT;
    }

    /**
     * Cast the event into a StartElement. It throws a ClassCastException
     * if it is not.
     * @return The event as a StartElement
     */
    @Override
    public StartElement asStartElement() {
        return (WbXmlStartElementEvent) this;
    }

    /**
     * Cast the event into a EndElement. It throws a ClassCastException
     * if it is not.
     * @return The event as a EndElement
     */
    @Override
    public EndElement asEndElement() {
        return (WbXmlEndElementEvent) this;
    }

    /**
     * Cast the event into a Characters. It throws a ClassCastException
     * if it is not.
     * @return The event as a Characters
     */
    @Override
    public Characters asCharacters() {
        return (WbXmlCharactersEvent) this;
    }

    /**
     * This method is provided for implementations to provide optional type 
     * information about the associated event. It is optional and will return 
     * null if no information is available. It always return null.
     * @return null
     */
    @Override
    public QName getSchemaType() {
        return null;
    }

    /**
     * This method will write the XMLEvent as per the XML 1.0 specification as 
     * Unicode characters. No indentation or whitespace should be outputted. 
     * Any user defined event type SHALL have this method called when being 
     * written to on an output stream. Built in Event types MUST implement this 
     * method, but implementations MAY choose not call these methods for 
     * optimizations reasons when writing out built in Events to an output 
     * stream. The output generated MUST be equivalent in terms of the infoset 
     * expressed.
     * It does nothing for all the wbxml-stream implementation.
     * @param writer
     * @throws XMLStreamException 
     */
    @Override
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        // nothing
    }
    
}

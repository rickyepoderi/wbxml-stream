/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.wbxml.stream.events;

import es.rickyepoderi.wbxml.stream.WbXmlStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;

/**
 * <p>Implementation of the Characters event in the wbxml-stream library.</p>
 * @author ricky
 */
public class WbXmlCharactersEvent extends WbXmlEvent implements Characters {

    /**
     * The text in the characters info.
     */
    private String text = null;
    
    /**
     * If this characters are a whitespace.
     */
    private boolean whitespace = false;
    
    /**
     * If the event was a CDATA or simple CAHRACTERS
     */
    private boolean cdata = false;
    
    /**
     * Constructor based on the stream reader position.
     * @param stream The stream in the CHARACTERS event.
     * @throws XMLStreamException Some error reading the data
     */
    public WbXmlCharactersEvent(WbXmlStreamReader stream) throws XMLStreamException {
        super(stream);
        if (getEventType() != XMLStreamConstants.CHARACTERS &&
                getEventType() != XMLStreamConstants.CDATA) {
            throw new IllegalStateException("Not at CHARACTERS or CDATA position!");
        }
        this.text = stream.getText();
        this.whitespace = stream.isWhiteSpace();
        this.cdata = stream.getEventType() == XMLStreamConstants.CDATA;
    }
    
    /**
     * Get the character data of this event
     * @return the text of the data
     */
    @Override
    public String getData() {
        return text;
    }

    /**
     * Returns true if this set of Characters is all whitespace. Whitespace 
     * inside a document is reported as CHARACTERS. This method allows checking 
     * of CHARACTERS events to see if they are composed of only whitespace 
     * characters
     * @return true if it is whitespace characters
     */
    @Override
    public boolean isWhiteSpace() {
        return whitespace;
    }

    /**
     * Returns true if this is a CData section. If this event is CData its event 
     * type will be CDATA If javax.xml.stream.isCoalescing is set to true CDATA 
     * Sections that are surrounded by non CDATA characters will be reported as 
     * a single Characters event. This method will return false in this case.
     * @return true if the event waqs a CDATA
     */
    @Override
    public boolean isCData() {
        return cdata;
    }

    /**
     * Return true if this is ignorableWhiteSpace. If this event is 
     * ignorableWhiteSpace its event type will be SPACE.
     * @return true if it is ignorableWhiteSpace
     */
    @Override
    public boolean isIgnorableWhiteSpace() {
        return whitespace;
    }
    
    /**
     * The event representation.
     * @return string representation
     */
    @Override
    public String toString() {
        return new StringBuilder("Characters: ")
                .append(this.text)
                .toString();
    }
}
